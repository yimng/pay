package com.edupay.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.json.JSON;
import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.PropertiesUtil;
import com.edupay.pay.service.PayRecordService;
import com.edupay.pay.vo.PayRecordVo;

import weixin.popular.api.SnsAPI;

/**
 * 支付单
 *
 * Title: PayRecordController.java
 * @author: Jack
 * @create: 2018-07-08
 *
 */
@Controller
@RequestMapping("/edupay/payRecord")
public class PayRecordController extends BasePayController{
	
	@Autowired
	private PayRecordService payRecordService;
	
	/**
	 * 支付请求（业务系统请求支付系统）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/requestPay",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ModelAndView requestPay(String payToken,PayRecordVo payRecordVo, HttpServletRequest request){
		System.out.println("payToken----------"+payToken);
    	//TODO 校验token
		if(null==getAppId(payToken)){
			return null;
		}
		//POS机支付
		if(CommonsPayTypeConstants.PAY_TYPE_POS_LOCAL==payRecordVo.getPayType()||CommonsPayTypeConstants.PAY_TYPE_POS_OTHER==payRecordVo.getPayType()) {
			try {
				String rs=payRecordService.add(payRecordVo.voTransEntity(payRecordVo));
				Properties properties=PropertiesUtil.loadProperties("pospay-config.properties");
				String url=properties.getProperty("pay.url");
				url+=properties.getProperty("pay.request.qrcode");
				url+=rs;
				String json = JSON.json(success(url));
				responseOutWithJson(response, json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//支付宝支付 20
		if(CommonsPayTypeConstants.PAY_TYPE_ALI==payRecordVo.getPayType()){
			String tradeNo = payRecordService.add(payRecordVo.voTransEntity(payRecordVo));
			request.setAttribute("WIDout_trade_no", tradeNo);  //订单号，必填
			request.setAttribute("WIDtotal_amount", payRecordVo.getMoney());  //金额，必填;
			request.setAttribute("WIDsubject", tradeNo);  //订单名称，必填
			request.setAttribute("WIDbody", payRecordVo.getRemark());  //描述，备注, 必填
			String path = "../alipay/pagePay";//默认web网页支付
			if(CommonsPayTypeConstants.PAY_TYPE_MOBILE.equals(payRecordVo.getPayEqu()))//手机H5支付
			{
				path = "../alipay/wepPay";
			}
			RequestDispatcher rd = request.getRequestDispatcher(path);
			try {
				rd.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//微信支付 30
		if(CommonsPayTypeConstants.PAY_TYPE_WX==payRecordVo.getPayType()){
			//默认网页扫码支付
			String wxpayUrl = "../wxpay/weixin_pay";
			//支付浏览器类型   "weixin" 微信内置浏览器； "other" 其他浏览器
			if("other".equals(payRecordVo.getBrowserType())){
				//添加支付单，生成支付单号
				String tradeNo = payRecordService.add(payRecordVo.voTransEntity(payRecordVo));
				request.setAttribute("out_trade_no", tradeNo);  //订单号，必填
				request.setAttribute("order_price", payRecordVo.getMoney());  //金额，必填;
				request.setAttribute("body", payRecordVo.getRemark());  //商品名称, 必填
				request.setAttribute("orderId", payRecordVo.getApplicationKeyId());
				
				wxpayUrl = "../wxpay/h5_pay";
			}
			else if("weixin".equals(payRecordVo.getBrowserType())){
				wxpayUrl = "../payRecord/gzh_requestPay";
			}
			RequestDispatcher rd = request.getRequestDispatcher(wxpayUrl);
			try {
				rd.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return null;
	}
	
	@RequestMapping(value = "/gzh_requestPay",method = {RequestMethod.POST,RequestMethod.GET})
    public String gzh_pay(String payToken,PayRecordVo payRecordVo, HttpServletRequest request){
		System.out.println("payToken----------"+payToken);
    	//TODO 校验token
		if(null==getAppId(payToken)){
			return null;
		}
		//微信支付 30
		if(CommonsPayTypeConstants.PAY_TYPE_WX==payRecordVo.getPayType()){
			//添加支付单，生成支付单号
			String tradeNo = payRecordService.add(payRecordVo.voTransEntity(payRecordVo));
			request.setAttribute("out_trade_no", tradeNo);  //订单号，必填
			request.setAttribute("order_price", payRecordVo.getMoney());  //金额，必填;
			request.setAttribute("body", payRecordVo.getRemark());  //商品名称, 必填
			
			//默认网页扫码支付
			String wxpayUrl = "../wxpay/weixin_pay";
			
			//支付浏览器类型   "weixin" 微信内置浏览器； "other" 其他浏览器
			if("other".equals(payRecordVo.getBrowserType())){
				wxpayUrl = "../wxpay/h5_pay";
			}
			else if("weixin".equals(payRecordVo.getBrowserType())){
				wxpayUrl = "../wxpay/gzhPay";
				Properties pospayConfig = PropertiesUtil.loadProperties("pospay-config.properties");
				Properties wxpayConfig = PropertiesUtil.loadProperties("wxpay-config.properties");
				String redirect_uri = pospayConfig.getProperty("pay.url") + "/edupay/wxpay/gzhPay?out_trade_no="+tradeNo+"&order_price="+payRecordVo.getMoney()+"&orderId="+payRecordVo.getApplicationKeyId()+"&body="+payRecordVo.getRemark();
				//也可以通过state传递参数 redirect_uri 后面加参数未经过验证
				wxpayUrl = SnsAPI.connectOauth2Authorize(wxpayConfig.getProperty("APP_ID"), redirect_uri, true,null);
				return "redirect:"+wxpayUrl;
			}
			RequestDispatcher rd = request.getRequestDispatcher(wxpayUrl);
			try {
				rd.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return null;
	}
	
//	/**
//	 * 支付单分页查询
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/searchPage",method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String, Object> searchPage(String payToken,PayRecord payRecord,Integer currentPage,Integer pageSize){
//		//TODO 校验token
//		if(null==getAppId(payToken)){
//			return null;
//		}
//		if(null==currentPage)
//			return error("参数错误",CommonsApiErrorCodeConstants.PARAM_ERROR);
//		if(null==pageSize)
//			return error("参数错误",CommonsApiErrorCodeConstants.PARAM_ERROR);
//		if(null==payRecord)
//			return error("参数错误",CommonsApiErrorCodeConstants.PARAM_ERROR);
//    	
//		PageResult page=payRecordService.searchPage(payRecord,currentPage,pageSize);
//		
//		return successPage(page);
//	}
	
	//重定义response返回方式
	private void responseOutWithJson(HttpServletResponse response, String json) {  
	    response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        out.append(json);  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	           out.close();  
	        }  
	    }  
	}
}
