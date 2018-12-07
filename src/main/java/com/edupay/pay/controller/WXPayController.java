package com.edupay.pay.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.PropertiesUtil;
import com.edupay.commons.utils.QrcodeUtil;
import com.edupay.commons.utils.RequestUtils;
import com.edupay.pay.entity.PayStatements;
import com.edupay.pay.service.PayStatementsService;
import com.edupay.pay.service.PushPayResultService;
import com.edupay.pay.vo.PayStatementsVo;
import com.edupay.wxpay.sdk.OpenIdClass;
import com.edupay.wxpay.sdk.PayCommonUtil;
import com.edupay.wxpay.sdk.WxUtil;
import com.google.gson.Gson;

/**
 * 微信支付接口 Title: WXPayController
 * 
 * 1.请求微信支付
 * 2.生成支付二维码
 * 3.监控二维码扫码支付状态，反馈支付结果
 * 
 * @author yangzhenlin
 * @date 2018年7月26日
 */
@Controller
@RequestMapping("/edupay/wxpay")
public class WXPayController extends BasePayController {
	
	@Autowired
	private PayStatementsService payStatementsSerivce;
	@Autowired
	private PushPayResultService pushPayResultService;

	private static Properties pospayConfig = PropertiesUtil.loadProperties("pospay-config.properties");
	private static Properties wxpayConfig = PropertiesUtil.loadProperties("wxpay-config.properties");

	/**
	 * 发起微信支付请求，调起微信客户端支付
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author yangzhenlin
	 */
	@RequestMapping(value = "/gzhPay", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView gzhPay(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//获取code 这个在微信支付调用时会自动加上这个参数 无须设置
		String code = request.getParameter("code");
		//获取用户openID(JSAPI支付必须传openid)
		String openId = getOpenId(code);
		// 账号信息
		String appid = wxpayConfig.getProperty("APP_ID"); // appid
		String mch_id = wxpayConfig.getProperty("MCH_ID"); // 商业号
		String key = wxpayConfig.getProperty("API_KEY"); // key
		//随机字符串
		String currTime = PayCommonUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;

		String orderId = request.getParameter("orderId");
		String order_priceStr = request.getParameter("order_price"); // 价格// 注意：价格的单位是分
		BigDecimal order_price = new BigDecimal(order_priceStr);
		model.addAttribute("order_price", order_price);
		BigDecimal bs = new BigDecimal(100);
		order_price = order_price.multiply(bs);
		int total_fee = order_price.intValue();
		String body =   request.getParameter("body"); // 商品描述
		String out_trade_no =  request.getParameter("out_trade_no"); // 支付单号，必填
		// 获取发起电脑 ip
		String spbill_create_ip = RequestUtils.getRemoteAddr(request);//HttpUtil.getRealIp(request);
		// 回调接口
		String notify_url = pospayConfig.getProperty("pay.url") + wxpayConfig.getProperty("NOTIFY_URL");
		String trade_type = "JSAPI";
		//封装参数
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", appid);// 应用ID
		packageParams.put("mch_id", mch_id);// 商户号
		packageParams.put("nonce_str", nonce_str);// 随机字符串
		packageParams.put("body", body);// 商品描述
		packageParams.put("out_trade_no", out_trade_no);// 商户订单号
		packageParams.put("total_fee", total_fee+"");// 总金额
		packageParams.put("spbill_create_ip", spbill_create_ip);// 终端IP
		packageParams.put("notify_url", notify_url);// 通知地址
		packageParams.put("trade_type", trade_type);// 交易类型
		packageParams.put("openid", openId);//用户openID

		String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
		packageParams.put("sign", sign);// 签名
		String requestXML = PayCommonUtil.getRequestXml(packageParams);
		System.out.println(".....requestXML....."+requestXML);
		String resXml = WxUtil.postData(wxpayConfig.getProperty("UFDODER_URL"), requestXML);
		Map map = null;
		try {
			map = WxUtil.doXMLParse(resXml);
			System.out.println(".....map........"+map.toString());
			String returnCode = (String) map.get("return_code");
			String returnMsg = (String) map.get("return_msg");
			if("SUCCESS".equals(returnCode)){
				String resultCode = (String) map.get("result_code");
				String errCodeDes = (String) map.get("err_code_des");
				if("SUCCESS".equals(resultCode)){
					//获取预支付交易会话标识
					String prepay_id = (String) map.get("prepay_id");
					String prepay_id2 = "prepay_id=" + prepay_id;
					String packages = prepay_id2;
					SortedMap<Object, Object> finalpackage = new TreeMap<Object, Object>();
					String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
					String nonceStr = packageParams.get("nonce_str").toString();
					finalpackage.put("appId",  appid);
					finalpackage.put("timeStamp", timestamp);
					finalpackage.put("nonceStr", nonceStr);
					finalpackage.put("package", packages);  
					finalpackage.put("signType", "MD5");
					//这里很重要  参数一定要正确 狗日的腾讯 参数到这里就成大写了
					//可能报错信息(支付验证签名失败 get_brand_wcpay_request:fail)
					sign = PayCommonUtil.createSign("UTF-8", finalpackage, key);
					
					model.addAttribute("timeStamp", timestamp);
					model.addAttribute("nonceStr", nonceStr);
					model.addAttribute("paySign", sign);
					model.addAttribute("appid", appid);
					model.addAttribute("orderNo", out_trade_no);
					model.addAttribute("totalFee", order_priceStr);
					model.addAttribute("packagess", prepay_id);
					model.addAttribute("orderId", orderId);
					
					ModelAndView view = new ModelAndView("wxpay/payPage");
					return view;
				}else{
					model.addAttribute("returnMsg", errCodeDes);
					ModelAndView view = new ModelAndView("error/500");
					return view;
				}
			}else{
				model.addAttribute("returnMsg", returnMsg);
				ModelAndView view = new ModelAndView("error/500");
				return view;
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发起微信支付请求，调起微信客户端支付
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author yangzhenlin
	 */
	@RequestMapping(value = "/h5_pay", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView h5_pay(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 账号信息
		String appid = wxpayConfig.getProperty("APP_ID"); // appid
		String mch_id = wxpayConfig.getProperty("MCH_ID"); // 商业号
		String key = wxpayConfig.getProperty("API_KEY"); // key
		//随机字符串
		String currTime = PayCommonUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;

		BigDecimal order_price = (BigDecimal) request.getAttribute("order_price"); // 价格// 注意：价格的单位是分
		model.addAttribute("order_price", order_price);
		BigDecimal bs = new BigDecimal(100);
		order_price = order_price.multiply(bs);
		int total_fee = order_price.intValue();
		String body = (String) request.getAttribute("body"); // 商品描述
		String out_trade_no = (String) request.getAttribute("out_trade_no"); // 支付单号，必填
		// 获取发起电脑 ip
		String spbill_create_ip = RequestUtils.getRemoteAddr(request);//HttpUtil.getRealIp(request);
		// 回调接口
		String notify_url = pospayConfig.getProperty("pay.url") + wxpayConfig.getProperty("NOTIFY_URL");
		String trade_type = "MWEB";
		//封装参数
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", appid);// 应用ID
		packageParams.put("mch_id", mch_id);// 商户号
		packageParams.put("nonce_str", nonce_str);// 随机字符串
		packageParams.put("body", body);// 商品描述
		packageParams.put("out_trade_no", out_trade_no);// 商户订单号
		packageParams.put("total_fee", total_fee+"");// 总金额
		packageParams.put("spbill_create_ip", spbill_create_ip);// 终端IP
		packageParams.put("notify_url", notify_url);// 通知地址
		packageParams.put("trade_type", trade_type);// 交易类型
		
		String h5info = "{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"https://zbesdemo.zbgedu.com\",\"wap_name\":\"测试\"}}";
		packageParams.put("scene_info",h5info);

		String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
		packageParams.put("sign", sign);// 签名
		String requestXML = PayCommonUtil.getRequestXml(packageParams);
		System.out.println("... .. requestXML   ....."+requestXML);
		String resXml = WxUtil.postData(wxpayConfig.getProperty("UFDODER_URL"), requestXML);
		Map map = null;
		try {
			map = WxUtil.doXMLParse(resXml);
			System.out.println("..  ...  map  ........"+map.toString());
			String return_code = (String) map.get("return_code");
			if("FAIL".equals(return_code)){
				ModelAndView view = new ModelAndView("error/500");
				return view;
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(map!=null){
			String return_code = (String) map.get("return_code");
			if("SUCCESS".equals(return_code)){
				String result_code = (String) map.get("result_code");
				if("SUCCESS".equals(result_code)){
					String mweb_url = (String) map.get("mweb_url");
					
					String redirect_url = wxpayConfig.getProperty("redirect_url");
					mweb_url = mweb_url + "&redirect_url="+redirect_url+"%3fout_trade_no%3d"+out_trade_no;
					
					model.addAttribute("mweb_url", mweb_url);
					
					ModelAndView view = new ModelAndView("wxpay/wxMweb");
					return view;
				}
			}
		}
		return null;
	}
	
	/**
	 * 发起微信支付请求，生成二维码
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author yangzhenlin
	 */
	@RequestMapping(value = "/weixin_pay", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ModelAndView weixin_pay(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 账号信息
		String appid = wxpayConfig.getProperty("APP_ID"); // appid
		String mch_id = wxpayConfig.getProperty("MCH_ID"); // 商业号
		String key = wxpayConfig.getProperty("API_KEY"); // key
		//随机字符串
		String currTime = PayCommonUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;

		BigDecimal order_price = (BigDecimal) request.getAttribute("order_price"); // 价格// 注意：价格的单位是分
		model.addAttribute("order_price", order_price);
		BigDecimal bs = new BigDecimal(100);
		order_price = order_price.multiply(bs);
		int total_fee = order_price.intValue();
		String body = (String) request.getAttribute("body"); // 商品描述
		String out_trade_no = (String) request.getAttribute("out_trade_no"); // 支付单号，必填
		// 获取发起电脑 ip
		String spbill_create_ip = RequestUtils.getRemoteAddr(request);//HttpUtil.getRealIp(request);
		// 回调接口
		String notify_url = pospayConfig.getProperty("pay.url") + wxpayConfig.getProperty("NOTIFY_URL");
		String trade_type = "NATIVE";
		//封装参数
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", appid);// 应用ID
		packageParams.put("mch_id", mch_id);// 商户号
		packageParams.put("nonce_str", nonce_str);// 随机字符串
		packageParams.put("body", body);// 商品描述
		packageParams.put("out_trade_no", out_trade_no);// 商户订单号
		packageParams.put("total_fee", total_fee+"");// 总金额
		packageParams.put("spbill_create_ip", spbill_create_ip);// 终端IP
		packageParams.put("notify_url", notify_url);// 通知地址
		packageParams.put("trade_type", trade_type);// 交易类型
		String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
		packageParams.put("sign", sign);// 签名
		String requestXML = PayCommonUtil.getRequestXml(packageParams);
//		System.out.println(".....requestXML......"+requestXML);
		String resXml = WxUtil.postData(wxpayConfig.getProperty("UFDODER_URL"), requestXML);
		Map map = null;
		try {
			map = WxUtil.doXMLParse(resXml);
//			System.out.println("...............map........"+map.toString());
			String return_code = (String) map.get("return_code");
			if("FAIL".equals(return_code)){
				ModelAndView view = new ModelAndView("error/500");
				return view;
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(map!=null){
			String return_code = (String) map.get("return_code");
			if("SUCCESS".equals(return_code)){
				String result_code = (String) map.get("result_code");
				if("SUCCESS".equals(result_code)){
					String prepay_id = (String) map.get("prepay_id");
					String code_url = (String) map.get("code_url");
					
					model.addAttribute("code_url", code_url);
					model.addAttribute("out_trade_no", out_trade_no);
					model.addAttribute("prepay_id", prepay_id);
					ModelAndView view = new ModelAndView("wxpay/wxQrCode");
					return view;
				}
			}
		}
		return null;
	}
	
	/**
	 * 监控二维码支付状态
	 * @param model
	 * @param request
	 * @param response
	 * @param out_trade_no   支付订单号
	 * @return
	 * @throws SocketException
	 * @throws UnsupportedEncodingException
	 * @author yangzhenlin
	 */
	@RequestMapping(value = "/queryWechatSaoPay", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, String> queryWechatSaoPay(Model model, HttpServletRequest request, HttpServletResponse response, 
			String out_trade_no) throws SocketException, UnsupportedEncodingException {
		Map<String, String> resultMap = new HashMap<String, String>();
		if (out_trade_no == null || out_trade_no.trim().equals("")) {
			resultMap.put("return_code", "0");
			resultMap.put("return_msg", "订单号为空");
			return resultMap;
		}
		// 商户的信息 
		String appid = wxpayConfig.getProperty("APP_ID"); // appid
		String mch_id = wxpayConfig.getProperty("MCH_ID"); // 商业号
		String key = wxpayConfig.getProperty("API_KEY"); // key

		// 查询微信支付状态
		try {
			Map<String, String> map = queryWeiXinPay(appid, mch_id, key, out_trade_no);
			if (map == null || map.isEmpty()) {
				resultMap.put("return_code", "0");
				resultMap.put("return_msg", "查询支付状态失败!");
				return resultMap;
			} else {
				String return_code  = map.get("return_code");
				if("SUCCESS".equals(return_code)){
					String result_code = (String) map.get("result_code");
					if("SUCCESS".equals(result_code)){
						String trade_state = map.get("trade_state"); // 交易状态
						if("SUCCESS".equals(trade_state)){
							
							String total_fee = map.get("total_fee");// 交易金额
							String openid = map.get("openid");//支付用户
							String time_end = map.get("time_end");//支付时间，流水号
							resultMap.put("return_code", "1");
							resultMap.put("total_fee", total_fee);
							resultMap.put("orderId", out_trade_no);
							resultMap.put("openid", openid);
							resultMap.put("time_end", time_end);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("return_code", "0");
			resultMap.put("return_msg", "查询支付状态失败!");
			return resultMap;
		}
		return resultMap;
	}
	
	/**
	 * 支付成功，添加流水单
	 * @param model
	 * @param token
	 * @param params
	 * @param payRecordVo
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @author yangzhenlin
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "/paySuc", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ModelAndView paySuc(Model model, String out_trade_no, String time_end, String openid, String total_fee, 
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws InterruptedException {
		Thread.sleep(500);
//		System.out.println("-");
//		System.out.println("------------"+out_trade_no);
//		System.out.println("-");
		//推送业务系统支付反馈（即时推送官网）
		//
		String payStatus = "0";
		List<PayStatements> list = payStatementsSerivce.searchByPayCode(out_trade_no);
		if(!CollectionUtils.isEmpty(list)){
			payStatus = "1";
		}
		
		Map<String, String> maps = pushPayResultService.pushPayResultForEdu(out_trade_no, payStatus);
		model.addAttribute("encryptCode", maps.get("encryptCode"));
		model.addAttribute("orderId", maps.get("orderId"));
		model.addAttribute("payStatus", maps.get("payStatus"));
		//
		ModelAndView view = new ModelAndView("alipay/tsPage");
		return view;
	}

	/**
	 * 
	 * @param appid
	 * @param mch_id
	 * @param key
	 * @param orderId
	 * @return
	 * @throws Exception
	 * @author yangzhenlin
	 */
	public static Map<String, String> queryWeiXinPay(String appid, String mch_id, String key, String orderId)
			throws Exception {
		Map<String, String> map = null;
		try {
			//
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			packageParams.put("appid", appid);// 应用ID
			packageParams.put("mch_id", mch_id);// 商户号
			packageParams.put("out_trade_no", orderId);// 商户订单号
			//
			String currTime = PayCommonUtil.getCurrTime();
			String strTime = currTime.substring(8, currTime.length());
			String strRandom = PayCommonUtil.buildRandom(4) + "";
			String nonce_str = strTime + strRandom;
			packageParams.put("nonce_str", nonce_str);// 随机字符串
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign);// 签名
			//
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = WxUtil.postData("https://api.mch.weixin.qq.com/pay/orderquery", requestXML);
			try {
				map = WxUtil.doXMLParse(resXml);
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String return_code = (String) map.get("return_code");
			String return_msg = (String) map.get("return_msg");
			String result_code = (String) map.get("result_code"); // 业务结果
			String err_code = (String) map.get("err_code"); // 错误代码 err_code
			String err_code_des = (String) map.get("err_code_des"); // 错误代码描述
			String openid = (String) map.get("openid"); // 用户标识 openid
			String trade_state = (String) map.get("trade_state"); // 交易状态
			String out_trade_no = (String) map.get("out_trade_no"); // 商户订单号
			String time_end = (String) map.get("time_end"); // 支付完成时间 time_end
			String trade_state_desc = (String) map.get("trade_state_desc"); // 交易状态描述

			if ("SUCCESS".equals(return_code)) {// 微信返回状态码为成功
				if ("SUCCESS".equals(result_code)) {// 业务结果状态码为成功
					if ("SUCCESS".equals(trade_state)) {// 交易状态为成功
						return map;
					}
					else {
						// 交易状态为不是成功
						map = null;
						return map;
					}
				} else {
					// 业务结果状态码为失败
					map = null;
					return map;
				}
			} else {
				// 微信返回状态码为失败
				map = null;
				return map;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 支付成功回调
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @author yangzhenlin
	 */
	@RequestMapping(value = "/weixin_notify", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void weixin_notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("------- 微信 支付成功回调-----start-------↓-↓-↓-↓-↓-↓-↓-↓-↓-↓-↓-↓");
		// 读取参数
		InputStream inputStream;
		StringBuffer sb = new StringBuffer();
		inputStream = request.getInputStream();
		String s;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		while ((s = in.readLine()) != null) {
			sb.append(s);
		}
		in.close();
		inputStream.close();
		// 解析xml成map
		Map<String, String> m = new HashMap<String, String>();
		try {
			m = WxUtil.doXMLParse(sb.toString());
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		System.out.println("m--------"+m);
		// 过滤空 设置 TreeMap
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String parameter = (String) it.next();
			String parameterValue = m.get(parameter);
			String v = "";
			if (null != parameterValue) {
				v = parameterValue.trim();
			}
			packageParams.put(parameter, v);
		}
		// 账号信息
		String key = wxpayConfig.getProperty("API_KEY"); // key
		// 判断签名是否正确
		if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, key)) {
			// 处理业务开始
			String resXml = "";
			if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
				// 这里是支付成功
				String mch_id = (String) packageParams.get("mch_id");// 商户号
				String openid = (String) packageParams.get("openid");// 用户标识
				String out_trade_no = (String) packageParams.get("out_trade_no");// 商户订单号
				String total_fee =  (String) packageParams.get("total_fee");// 缴费金额
				String time_end = (String) packageParams.get("time_end");// 支付完成时间
				//添加流水表
				//交易时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = new Date();
				try {
					date = sdf.parse(time_end);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//流水号
				String trade_no = time_end;
				//支付金额
				BigDecimal total_amount = new BigDecimal(total_fee);
				total_amount = total_amount.divide(new BigDecimal(100));
				//
				PayStatementsVo ps = new PayStatementsVo();
				ps.setPayRecordId(out_trade_no);//支付记录id
				ps.setPayCode(out_trade_no);//支付单号
				ps.setSerialNumber(trade_no);//应用记录主键id
				ps.setMoney(total_amount);
				ps.setStatus(1);
				ps.setPayDate(date);
				ps.setPushStatus(30);
				payStatementsSerivce.add(ps, CommonsPayTypeConstants.PAY_TYPE_WX);
				
				// 推送业务系统结果
				System.out.println("---推送业务系统支付反馈------wx--------");
				int rs = pushPayResultService.pushPayResult(out_trade_no, trade_no, openid, "4");
				if(-1<rs){
					// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
					resXml = "<xml>"
							+ "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>"
							+ "</xml>";
				}
				else{
					resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";
				}
			} else {
				resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";
			}
			// 处理业务完毕
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
		}
		System.out.println("------- 微信 支付成功回调------end----------↑-↑-↑-↑-↑-↑-↑-↑-↑-↑-↑");
	}
	
	/**
	 * 生成二维码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getQrcode",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String payToken, String code_url, String out_trade_no){
    	//TODO 校验token
    	if(StringUtils.isBlank(code_url) || StringUtils.isBlank(out_trade_no))
    		return null;
		int width = 300;
        int height = 300;
        String format = "png";
        HttpHeaders headers = new HttpHeaders(); 
        String filePath= request.getSession().getServletContext().getRealPath("/upload");
        filePath=filePath.replace("\\", "/");
        filePath+="/"+out_trade_no+".png";
        new ResponseEntity<byte[]>(QrcodeUtil.generationQrcode(code_url,filePath,format,width,height),headers,HttpStatus.CREATED);
        getImage(filePath);
		return null;
	}
    private void getImage(String filePath){
    	try{
	        FileInputStream is = new FileInputStream(filePath);
	        int i = is.available(); // 得到文件大小  
	        byte data[] = new byte[i];  
	        is.read(data); // 读数据  
	        is.close();  
	        response.setContentType("image/*"); // 设置返回的文件类型  
	        OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象  
	        toClient.write(data); // 输出数据  
	        toClient.close(); 
        }catch(Exception e) {
        	e.printStackTrace();
        }
        clearFile(filePath);
    }
    private void clearFile(String filePath) {
    	File file=new File(filePath);
        file.delete();
    }
    
    private  String getOpenId(String code){
		if (code != null) {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
					+ "appid="+ wxpayConfig.getProperty("APP_ID")
					+ "&secret="+ wxpayConfig.getProperty("APP_SECRET") + "&code="
					+code + "&grant_type=authorization_code";
			String returnData = getReturnData(url);
			Gson gson = new Gson();
			OpenIdClass openIdClass = gson.fromJson(returnData,
					OpenIdClass.class);
			if (openIdClass.getOpenid() != null) {
				return openIdClass.getOpenid();
			}
		}
		return "";
	}
    private String getReturnData(String urlString) {
		String res = "";
		try {
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url
					.openConnection();
			conn.connect();
			java.io.BufferedReader in = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream(),
							"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}