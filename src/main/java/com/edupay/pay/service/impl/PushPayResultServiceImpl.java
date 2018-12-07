package com.edupay.pay.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONObject;
import com.edupay.app.entity.App;
import com.edupay.app.service.AppService;
import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.DateUtil;
import com.edupay.commons.utils.HttpUtil;
import com.edupay.commons.utils.PropertiesUtil;
import com.edupay.pay.entity.PayStatements;
import com.edupay.pay.service.PayRecordService;
import com.edupay.pay.service.PayStatementsService;
import com.edupay.pay.service.PosPushPayResultService;
import com.edupay.pay.service.PushPayResultService;
import com.edupay.pay.service.TerminalService;
import com.edupay.pay.vo.PayRecordVo;
import com.edupay.pay.vo.PayStatementsVo;
import com.edupay.pay.vo.TerminalVo;
import com.edupay.wxpay.sdk.MD5Util;

/**
 * 支付流水 业务实现类
 *
 * Title: PayStatementsServiceImpl.java
 * @author: Jack
 * @create: 2018-06-21
 *
 */
@Component
@Service
public class PushPayResultServiceImpl implements PushPayResultService {

	@Autowired
	private PayStatementsService payStatementsSerivce;
	@Autowired
	private PayRecordService payRecordService;
	@Autowired
	private TerminalService terminalService;
	@Autowired
	private PosPushPayResultService posPushPayResultService;
	@Autowired
	private AppService appService;
	
	private static Properties alipayConfig = PropertiesUtil.loadProperties("alipay-config.properties");
	
	/**
	 * 支付结果补推
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/08/10
	 */
	@Override
    public void pushPayResultTask(){
		List<PayStatements> erroList=payStatementsSerivce.searchByPushError();
		//补推支付通知
		for(PayStatements ps:erroList){
			pushPayResult(new PayStatementsVo(ps));
		}
	}
	
	/**
	 * POS推送支付通知
	 * 
	 * @param psVo
	 * @return
	 * @Author: Jack
	 * @Date: 2018/08/10
	 */
	public int pushPayResult(PayStatementsVo psVo){
		int rs=-1;
		if(null==psVo){
			return rs;
		}
		PayRecordVo prVo=psVo.getPayRecordVo();
		TerminalVo tVo=psVo.getTerminalVo();
		if(null==prVo){
			prVo=payRecordService.searchByPayCode(psVo.getPayCode());
			psVo.setPayRecordVo(prVo);
		}
		//POS支付
		if(null==prVo.getPayType()||prVo.getPayType().equals(CommonsPayTypeConstants.PAY_TYPE_POS_LOCAL)||prVo.getPayType().equals(CommonsPayTypeConstants.PAY_TYPE_POS_OTHER)){
			posPayPushResult(psVo,prVo,tVo);
		}
		//支付宝支付
		if(null!=prVo.getPayType()&&prVo.getPayType().equals(CommonsPayTypeConstants.PAY_TYPE_ALI)){
			
		}
		//微信支付
		if(null!=prVo.getPayType()&&prVo.getPayType().equals(CommonsPayTypeConstants.PAY_TYPE_WX)){
			
		}
		return rs;
	}
	
	private int posPayPushResult(PayStatementsVo psVo,PayRecordVo prVo,TerminalVo tVo){
		int rs=-1;
		//快捷支付
		if(StringUtils.isBlank(prVo.getApplicationKeyId())){
			if(null==tVo){
				tVo=terminalService.getTerminalVo(psVo.getRequestContent(), CommonsPayTypeConstants.PAY_TYPE_POS_TYPE_QUICK);
				psVo.setTerminalVo(tVo);
			}
			rs=posPushPayResultService.pushPayResultQuick(psVo);
		}
		//订单支付
		else{
			if(null==tVo){
				tVo=terminalService.getTerminalVo(psVo.getRequestContent(), CommonsPayTypeConstants.PAY_TYPE_POS_TYPE_ORDER);
				psVo.setTerminalVo(tVo);
			}
			rs=posPushPayResultService.pushPayResultOrder(psVo);
		}
		return rs;
	}
	
	/**
     * 推送业务系统支付反馈
     * @param payCode       订单号
     * @param serialNumber  流水号
     * @param buyer_id      买家ID
     * @param payWay        支付方式  2支付宝，4微信，
     * @return
     * @author yangzhenlin
     */
    public int pushPayResult(String payCode, String serialNumber, String buyer_id, String payWay){
    	System.out.println(payCode+"---"+serialNumber+"---"+buyer_id+"---"+payWay);
    	PayStatements ps = payStatementsSerivce.searchBySerialNumber(serialNumber);
		PayRecordVo pr = payRecordService.searchByPayCode(payCode);
//    	Integer appId = pr.getApplicationId();
//    	if(null==appId){
//    		appId = CommonsPayTypeConstants.DEFAULT_APP_ID;
//    	}
    	App app = appService.getAppById(1);
//		String url = "http://123.126.152.178:8198/edu/order/paymentByOfficial";
		String url = app.getUrl() + alipayConfig.getProperty("edu_return_url");
		//缴费订单ID      orderId
		//支付流水号            otherPayNo
		//缴费金额                money
		//中博账户类型（1.中博教育  2.中博诚通）  payeeType
		//缴费时间                    payDate
		//备注                            remark
		//缴费账户ID        bankAccountId
		//支付类型（2.支付宝 4.微信）            payWay
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("token", app.getToken());
		parameters.put("orderId", pr.getApplicationKeyId());
		parameters.put("otherPayNo", ps.getSerialNumber()); //流水号
		parameters.put("money", ps.getMoney().toString());		//缴费金额
		String parameter = pr.getApplicationParameter();
		parameter = parameter.replaceAll("&quot;", "'");
		JSONObject jo = null;
		try {
			jo = (JSONObject) JSON.parse(parameter);
		} catch (com.alibaba.dubbo.common.json.ParseException e) {
			e.printStackTrace();
		}
		parameters.put("payeeType", (String) jo.get("payeeType"));
		parameters.put("userId", (String) jo.get("userId"));
		String payDate=null;
		try{
			payDate=DateUtil.dateToString(ps.getPayDate(), "yyyy-MM-dd HH:mm:ss");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		parameters.put("payDate", payDate);	//支付时间
		parameters.put("remark", pr.getRemark());
		parameters.put("bankAccountId",buyer_id);//TODO
		parameters.put("payWay", payWay);//2支付宝；4微信
		HttpUtil httpUtil=new HttpUtil();
		String content = httpUtil.doPost(url, parameters, "UTF-8");
		System.out.println("content-----------------"+content);
		Integer rs=-1;
		if(StringUtils.isNotBlank(content)){
			rs= content.indexOf("\"state\" : \"success\"");
		}
		//TODO 修改支付记录推送结果
		if(-1<rs)
		{
			ps.setPushStatus(1);
		}
		PayStatementsVo payStatementsVo = new PayStatementsVo();
		payStatementsVo.setPushResult(content);
		BeanUtils.copyProperties(ps, payStatementsVo);
		//2支付宝；4微信
		if("2".equals(payWay)){
			payStatementsVo.setPushStatus(21);
		}else if("4".equals(payWay)){
			payStatementsVo.setPushStatus(31);
		}
		payStatementsSerivce.updatePushStateSuccess(payStatementsVo);
    	return rs;
    }
    
    /**
     * 推送到各系统平台支付反馈
     * @param payCode
     * @param payStatus
     * @return
     * @author yangzhenlin
     */
    public Map<String, String> pushPayResultForEdu(String payCode, String payStatus){
    	PayRecordVo pr = payRecordService.searchByPayCode(payCode);
		String encryptCode = alipayConfig.getProperty("encryptCode");
		encryptCode = MD5Util.MD5Encode(encryptCode, "UTF-8").toUpperCase();
		
		Map<String, String> map = new HashMap<>();
		map.put("encryptCode", encryptCode);//口令
		map.put("orderId", pr.getApplicationKeyId());//订单号
		map.put("payStatus", payStatus); //结果
		return map;
    }
	
}
