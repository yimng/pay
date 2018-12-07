package com.edupay.pay.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.PropertiesUtil;
import com.edupay.pay.service.PayRecordService;
import com.edupay.pay.service.PayStatementsService;
import com.edupay.pay.service.PushPayResultService;
import com.edupay.pay.vo.PayRecordVo;
import com.edupay.pay.vo.PayStatementsVo;

/**
 * 支付宝支付接口
 * Title: AlipayController 
 * @author yangzhenlin 
 * @date 2018年7月18日
 */
@Controller
@RequestMapping("/edupay/alipay")
public class AlipayController extends BasePayController {
	
	@Autowired
	private PayRecordService payRecordService;
	@Autowired
	private PayStatementsService payStatementsSerivce;
	@Autowired
	private PushPayResultService pushPayResultService;
	
	private static Properties pospayConfig = PropertiesUtil.loadProperties("pospay-config.properties");
	private static Properties alipayConfig = PropertiesUtil.loadProperties("alipay-config.properties");
	
	/**
	 * 支付宝 支付请求
	 * @param model
	 * @param token
	 * @param payRecordVo
	 * @return
	 * @author yangzhenlin
	 */
	@RequestMapping(value = "/requestPay", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ModelAndView requestPay(Model model, String token, PayRecordVo payRecordVo) {
		//
		//{&quot;payeeType&quot;:1,&quot;userId&quot;:&quot;2f9816ce43574c078e633ec97676a65a&quot;}
		pushPayResultService.pushPayResult("PY20180817145353206", "2018081721001004410200633782", "zzz", "2");
		return null;
	}
	
	/**
	 * 电脑网站支付
	 * @param WIDout_trade_no  支付订单号
	 * @param WIDtotal_amount  订单金额
	 * @param WIDsubject       订单名称
	 * @param WIDbody          描述
	 * @param httpResponse
	 * @author yangzhenlin
	 */
	@RequestMapping(value = "/wepPay", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void wepPay(HttpServletResponse httpResponse, HttpServletRequest httpRequest) {
		
		String WIDout_trade_no = (String) request.getAttribute("WIDout_trade_no");  //支付单号，必填
		BigDecimal WIDtotal_amount = (BigDecimal) request.getAttribute("WIDtotal_amount");  //金额，
		String WIDsubject = (String) request.getAttribute("WIDsubject");  //订单名称，
		String WIDbody = (String) request.getAttribute("WIDbody");  //描述，备注
		System.out.println("手机支付。。。"+WIDtotal_amount);
		//
		AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getProperty("gatewayUrl"), alipayConfig.getProperty("app_id"), alipayConfig.getProperty("merchant_private_key"), "json", alipayConfig.getProperty("charset"), alipayConfig.getProperty("alipay_public_key"), alipayConfig.getProperty("sign_type"));
		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
		// 封装请求支付信息
	    AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
	    model.setOutTradeNo(WIDout_trade_no);// 商户订单号，商户网站订单系统中唯一订单号，必填
	    model.setSubject(WIDsubject);/// 订单名称，必填
	    model.setTotalAmount(WIDtotal_amount+"");//// 付款金额，必填
	    model.setBody(WIDbody);// 商品描述，可空
	    model.setTimeoutExpress("2m");// 超时时间 可空
	    model.setProductCode("QUICK_WAP_WAY");// 销售产品码 必填
	    alipay_request.setBizModel(model);
	    // 设置异步通知地址
	    alipay_request.setNotifyUrl(pospayConfig.getProperty("pay.url") + alipayConfig.getProperty("notify_url"));
	    // 设置同步地址
	    alipay_request.setReturnUrl(pospayConfig.getProperty("pay.url") + alipayConfig.getProperty("return_url"));
		
		//请求
		String form="";
        try {
            form = alipayClient.pageExecute(alipay_request).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + alipayConfig.getProperty("charset"));
        try {
        	httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
			httpResponse.getWriter().flush();
			httpResponse.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 电脑网站支付
	 * @param WIDout_trade_no  支付订单号
	 * @param WIDtotal_amount  订单金额
	 * @param WIDsubject       订单名称
	 * @param WIDbody          描述
	 * @param httpResponse
	 * @author yangzhenlin
	 */
	@RequestMapping(value = "/pagePay", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void pagePay(HttpServletResponse httpResponse, HttpServletRequest httpRequest) {
		String WIDout_trade_no = (String) request.getAttribute("WIDout_trade_no");  //支付单号，必填
		BigDecimal WIDtotal_amount = (BigDecimal) request.getAttribute("WIDtotal_amount");  //金额，
		String WIDsubject = (String) request.getAttribute("WIDsubject");  //订单名称，
		String WIDbody = (String) request.getAttribute("WIDbody");  //描述，备注
		//
		AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getProperty("gatewayUrl"), alipayConfig.getProperty("app_id"), alipayConfig.getProperty("merchant_private_key"), "json", alipayConfig.getProperty("charset"), alipayConfig.getProperty("alipay_public_key"), alipayConfig.getProperty("sign_type"));
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(pospayConfig.getProperty("pay.url") + alipayConfig.getProperty("return_url"));
		alipayRequest.setNotifyUrl(pospayConfig.getProperty("pay.url") + alipayConfig.getProperty("notify_url"));
		
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
	    AlipayTradePagePayModel model = new AlipayTradePagePayModel();
	    model.setOutTradeNo(WIDout_trade_no);//商户订单号，商户网站订单系统中唯一订单号，必填
    	model.setTotalAmount(WIDtotal_amount+"");//付款金额，必填
    	model.setSubject(WIDsubject);//商品名称，必填
//    	model.setTimeoutExpress("90m");
    	model.setProductCode("FAST_INSTANT_TRADE_PAY");
    	model.setBody(WIDbody);//商品描述
    	alipayRequest.setBizModel(model);
		//请求
		String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + alipayConfig.getProperty("charset"));
        try {
        	httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
			httpResponse.getWriter().flush();
			httpResponse.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 支付成功回调页面
	 * @param model
	 * @param token
	 * @return
	 * @author yangzhenlin
	 * @throws UnsupportedEncodingException 
	 * @throws AlipayApiException 
	 */
	@RequestMapping(value = "/return_url", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ModelAndView return_url(Model model, String token) throws UnsupportedEncodingException, AlipayApiException {
		//
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getProperty("alipay_public_key"), alipayConfig.getProperty("charset"), alipayConfig.getProperty("sign_type")); //调用SDK验证签名
		//
		if(signVerified) {
			request.setAttribute("params", params); 
			RequestDispatcher rd = request.getRequestDispatcher("paySuc");
			try {
				rd.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			//失败推送
			//推送业务系统支付反馈
			Map<String, String> maps = pushPayResultService.pushPayResultForEdu(params.get("out_trade_no"), "0");
			model.addAttribute("encryptCode", maps.get("encryptCode"));
			model.addAttribute("orderId", maps.get("orderId"));
			model.addAttribute("payStatus", maps.get("payStatus"));
			ModelAndView view = new ModelAndView("alipay/tsPage");
			return view;
		}
		return null;
	}

	/**
	 * 支付成功，跳转成功页面
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
	 */
	@RequestMapping(value = "/paySuc", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ModelAndView paySuc(Model model, String token, String params, PayRecordVo payRecordVo, 
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		//
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("params");
		//支付单号
		String out_trade_no = (String) map.get("out_trade_no");
		/*//交易时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date timestamp = null;
		try {
			timestamp = sdf.parse((String) map.get("timestamp"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//流水号
		String trade_no = (String) map.get("trade_no");
		//支付金额
		BigDecimal total_amount = new BigDecimal((String)map.get("total_amount"));
		PayStatementsVo ps = new PayStatementsVo();
		ps.setPayRecordId(out_trade_no);//支付记录id
		ps.setPayCode(out_trade_no);//支付单号
		ps.setSerialNumber(trade_no);//应用记录主键id
		ps.setMoney(total_amount);
		ps.setStatus(1);
		ps.setPayDate(timestamp);
		payStatementsSerivce.add(ps, CommonsPayTypeConstants.PAY_TYPE_ALI);*/
		
		//推送业务系统支付反馈
		Map<String, String> maps = pushPayResultService.pushPayResultForEdu(out_trade_no, "1");
		model.addAttribute("encryptCode", maps.get("encryptCode"));
		model.addAttribute("orderId", maps.get("orderId"));
		model.addAttribute("payStatus", maps.get("payStatus"));
		//
		ModelAndView view = new ModelAndView("alipay/tsPage");
		return view;
	}
	
	/**
	 * 支付宝回调
	 * @param model
	 * @param token
	 * @throws AlipayApiException
	 * @author yangzhenlin
	 * @throws IOException 
	 */
	@RequestMapping(value = "/notify_url", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String notify_url(Model model, String token, HttpServletResponse httpResponse) throws AlipayApiException, IOException {
		System.out.println("---------支付宝回调-------start-------------");
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
			
		}
		boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getProperty("alipay_public_key"), alipayConfig.getProperty("charset"), alipayConfig.getProperty("sign_type")); //调用SDK验证签名
		if(signVerified) {//验证成功
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			//买家在支付宝的用户id 
			String buyer_id = new String(request.getParameter("buyer_id").getBytes("ISO-8859-1"),"UTF-8");
			//交易付款时间
			String gmt_payment = request.getParameter("gmt_payment");
			//支付金额
			String buyer_pay_amount = request.getParameter("buyer_pay_amount");
			//
			PayRecordVo pr = payRecordService.searchByPayCode(out_trade_no);
			//验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
			if(pr==null){
				return "fail";
			}
			if(!buyer_pay_amount.equals(pr.getMoney().toString())){
				return "fail";
			}
			if( "TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status) ){
				//
				//交易时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date timestamp = null;
				try {
					timestamp = sdf.parse(gmt_payment);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//
				//支付金额
				BigDecimal total_amount = new BigDecimal(buyer_pay_amount);
				PayStatementsVo ps = new PayStatementsVo();
				ps.setPayRecordId(out_trade_no);//支付记录id
				ps.setPayCode(out_trade_no);//支付单号
				ps.setSerialNumber(trade_no);//应用记录主键id
				ps.setMoney(total_amount);
				ps.setStatus(1);
				ps.setPayDate(timestamp);
				ps.setPushStatus(20);
				payStatementsSerivce.add(ps, CommonsPayTypeConstants.PAY_TYPE_ALI);
				//推送业务系统支付反馈
				int rs = pushPayResultService.pushPayResult(out_trade_no, trade_no, buyer_id, "2");
				if(-1<rs){
					return "success";
				}
			}else{
				return "fail";
			}
		}else {//验证失败
			return "fail";
		}
		System.out.println("---------支付宝回调-------end-------------");
		return "fail";
	}
	
	/**
	 * 支付宝二维码即时到账支付
	 * @param WIDout_trade_no
	 * @param WIDtotal_amount
	 * @param WIDsubject
	 * @param WIDbody
	 * @param httpResponse
	 * @author yangzhenlin
	 */
	@RequestMapping(value = "/qrCodePay", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void qrCodePay(String WIDout_trade_no, String WIDtotal_amount, 
    		String WIDsubject, String WIDbody, HttpServletResponse httpResponse) {
		AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getProperty("gatewayUrl"), alipayConfig.getProperty("app_id"), alipayConfig.getProperty("merchant_private_key"), "json", alipayConfig.getProperty("charset"), alipayConfig.getProperty("alipay_public_key"), alipayConfig.getProperty("sign_type"));
//		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(alipayConfig.getProperty("return_url"));
		alipayRequest.setNotifyUrl(alipayConfig.getProperty("notify_url"));
		
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
	    AlipayTradePagePayModel model = new AlipayTradePagePayModel();
	    model.setOutTradeNo(WIDout_trade_no);//商户订单号，商户网站订单系统中唯一订单号，必填
    	model.setTotalAmount(WIDtotal_amount);//付款金额，必填
    	model.setSubject(WIDsubject);//付款金额，必填
//    	model.setTimeoutExpress("90m");
    	model.setProductCode("FAST_INSTANT_TRADE_PAY");
    	model.setQrPayMode("4");
    	model.setBody(WIDbody);
    	alipayRequest.setBizModel(model);
		
		//请求
		String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + alipayConfig.getProperty("charset"));
        try {
        	httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
			httpResponse.getWriter().flush();
			httpResponse.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}