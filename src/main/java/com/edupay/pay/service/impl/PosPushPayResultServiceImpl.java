package com.edupay.pay.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.edupay.app.entity.App;
import com.edupay.app.service.AppService;
import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.DataTypeUtil;
import com.edupay.commons.utils.DateUtil;
import com.edupay.commons.utils.HttpUtil;
import com.edupay.commons.utils.PropertiesUtil;
import com.edupay.pay.service.PayStatementsService;
import com.edupay.pay.service.PosPushPayResultService;
import com.edupay.pay.vo.ApplicationPayRecordVo;
import com.edupay.pay.vo.PayStatementsVo;

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
public class PosPushPayResultServiceImpl implements PosPushPayResultService {
	
	@Autowired
	private AppService appService;
	@Autowired
	private PayStatementsService payStatementsSerivce;
	
	private static Properties properties=PropertiesUtil.loadProperties("pospay-config.properties");
	private static final Logger LOG = Logger.getLogger(PosPushPayResultServiceImpl.class.getName());
	
	/**
	 * 向业务系统推送支付流水-订单
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/04
	 */
	@Override
    public int pushPayResultOrder(PayStatementsVo payStatementsVo){
    	Integer appId=payStatementsVo.getPayRecordVo().getApplicationId();
    	if(null==appId){
    		appId=CommonsPayTypeConstants.DEFAULT_APP_ID;
    	}
    	App app=appService.getAppById(appId);
//		String url = "http://123.126.152.178:8198/edu/studentPayfee/paymentByPos";
		String url=app.getUrl();
		//本部支付
		if(CommonsPayTypeConstants.PAY_TYPE_POS_LOCAL.equals(payStatementsVo.getPayRecordVo().getPayType())){
			url+=properties.getProperty("push.api.jiaowu.order.local");
		}
		//跨区支付
		if(CommonsPayTypeConstants.PAY_TYPE_POS_OTHER.equals(payStatementsVo.getPayRecordVo().getPayType())){
			url+=properties.getProperty("push.api.jiaowu.order.other");		
		}
		Map<String, String> parameters = new HashMap<String, String>();
		//TODO 安全校验 应用key
		parameters.put("token",app.getToken());
		
		parameters.put("orderId",payStatementsVo.getPayRecordVo().getApplicationKeyId());
		
		String param=payStatementsVo.getPayRecordVo().getApplicationParameter();
		
		List<ApplicationPayRecordVo> aprLst=new ArrayList<ApplicationPayRecordVo>();
		
		String bankAccountId=null;
		Integer zbPayType=0;
		String userId=null;
		if(StringUtils.isNotBlank(param)){
			DataTypeUtil.jsonConvertBeanList(aprLst, param, ApplicationPayRecordVo.class);
		}
    	if(null!=aprLst&&0<aprLst.size())
    	{
    		ApplicationPayRecordVo aprVo=aprLst.get(0);
    		bankAccountId=aprVo.getBankAccountId();
    		zbPayType=aprVo.getZbPayeeType();
    		userId=aprVo.getUserId();
    	}
		parameters.put("bankAccountId",bankAccountId);
		parameters.put("otherPayNo",payStatementsVo.getSerialNumber()); //流水号
//		parameters.put("userNumber",payStatementsVo.getTerminalVo().getLoginName()); //员工号
		parameters.put("userId",userId); //员工ID
		parameters.put("money",payStatementsVo.getMoney().toString());		//缴费金额
		parameters.put("posCode",payStatementsVo.getTerminalVo().getPosCode());	//终端编号
		//TODO 判断收费方
//		if(properties.getProperty("shopid.zbedu").equals(payStatementsVo.getTerminalVo().getShopId())){
		System.out.println("@pushZBPayeeType "+properties.getProperty("shopid.zbedu"));
		System.out.println(properties.getProperty("shopid.zbedu").indexOf(payStatementsVo.getTerminalVo().getShopId()));
		if(-1!=properties.getProperty("shopid.zbedu").indexOf(payStatementsVo.getTerminalVo().getShopId())){
			parameters.put("payeeType",CommonsPayTypeConstants.PAY_ZB_PAYEE_TYPE_ZBEDU.toString());
		}
//		if(properties.getProperty("shopid.zbct").equals(payStatementsVo.getTerminalVo().getShopId())){
		System.out.println("@pushZBPayeeType "+properties.getProperty("shopid.zbct"));
		System.out.println(properties.getProperty("shopid.zbct").indexOf(payStatementsVo.getTerminalVo().getShopId()));
		if(-1!=properties.getProperty("shopid.zbct").indexOf(payStatementsVo.getTerminalVo().getShopId())){
			parameters.put("payeeType",CommonsPayTypeConstants.PAY_ZB_PAYEE_TYPE_ZBCT.toString());
		}		
		parameters.put("branchId",payStatementsVo.getTerminalVo().getBranchId());
		
		String payDate=null;
		try{
			payDate=DateUtil.dateToString(payStatementsVo.getPayDate(), "yyyy-MM-dd HH:mm:ss");
		}catch(Exception e){
			e.printStackTrace();
		}
		parameters.put("payDate",payDate);	//支付时间
		
		
		HttpUtil httpUtil=new HttpUtil();
		//输出请求服务器IP监控推送情况
		LOG.info("@pushServerIp:"+httpUtil.getLocalIp()+"  content:"+parameters);
		System.out.println("@pushServerIp:"+httpUtil.getLocalIp()+"  content:"+parameters);
		String content = httpUtil.doPost(url, parameters, "UTF-8");
		payStatementsVo.setPushResult(content);
//		System.out.println(content);
		
		Integer rs=-1;
		if(StringUtils.isNotBlank(content)){
			rs= content.indexOf("\"state\" : \"success\"");
		}
		//TODO 修改支付记录推送结果
		if(-1<rs){
			payStatementsVo.setPushStatus(1);
		}
		else{//缴费流水重复（推送成功后重复推送情况）
			rs= content.indexOf("\"code\" : \"110\"");
			if(-1<rs){
				payStatementsVo.setPushStatus(1);
			}
		}
		payStatementsSerivce.updatePushStateSuccess(payStatementsVo);
		return rs;
	}

    /**
	 * 向业务系统推送支付流水-快速支付
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/04
	 */
	@Override
    public int pushPayResultQuick(PayStatementsVo payStatementsVo){
    	Integer appId=payStatementsVo.getPayRecordVo().getApplicationId();
    	if(null==appId){
    		appId=CommonsPayTypeConstants.DEFAULT_APP_ID;
    	}
    	App app=appService.getAppById(appId);
//		String url = "http://123.126.152.178:8198/edu/studentPayfee/paymentByPosQuick";
		String url=app.getUrl();
		url+=properties.getProperty("push.api.jiaowu.quick");

		Map<String, String> parameters = new HashMap<String, String>();
		//TODO 安全校验 应用key
		parameters.put("token",app.getToken());
		parameters.put("otherPayNo",payStatementsVo.getSerialNumber()); //流水号
		parameters.put("userNumber",payStatementsVo.getTerminalVo().getLoginName()); //员工号
		parameters.put("money",payStatementsVo.getMoney().toString());		//缴费金额
		parameters.put("posCode",payStatementsVo.getTerminalVo().getPosCode());	//终端编号
		//TODO 判断收费方
//		if(properties.getProperty("shopid.zbedu").equals(payStatementsVo.getTerminalVo().getShopId())){
		System.out.println("@pushZBPayeeType "+properties.getProperty("shopid.zbedu"));
		System.out.println(properties.getProperty("shopid.zbedu").indexOf(payStatementsVo.getTerminalVo().getShopId()));
		if(-1!=properties.getProperty("shopid.zbedu").indexOf(payStatementsVo.getTerminalVo().getShopId())){
			parameters.put("payeeType",CommonsPayTypeConstants.PAY_ZB_PAYEE_TYPE_ZBEDU.toString());
		}
//		if(properties.getProperty("shopid.zbct").equals(payStatementsVo.getTerminalVo().getShopId())){
		System.out.println("@pushZBPayeeType "+properties.getProperty("shopid.zbct"));
		System.out.println(properties.getProperty("shopid.zbct").indexOf(payStatementsVo.getTerminalVo().getShopId()));
		if(-1!=properties.getProperty("shopid.zbct").indexOf(payStatementsVo.getTerminalVo().getShopId())){
			parameters.put("payeeType",CommonsPayTypeConstants.PAY_ZB_PAYEE_TYPE_ZBCT.toString());
		}
		parameters.put("branchId",payStatementsVo.getTerminalVo().getBranchId());
		
		String payDate=null;
		try{
			payDate=DateUtil.dateToString(payStatementsVo.getPayDate(), "yyyy-MM-dd HH:mm:ss");
		}catch(Exception e){
			e.printStackTrace();
		}
		parameters.put("payDate",payDate);	//支付时间
		
		HttpUtil httpUtil=new HttpUtil();
		//输出请求服务器IP监控推送情况
		LOG.info("@pushServerIp:"+httpUtil.getLocalIp()+"  content:"+parameters);
		System.out.println("@pushServerIp:"+httpUtil.getLocalIp()+"  content:"+parameters);
		String content = httpUtil.doPost(url, parameters, "UTF-8");
		payStatementsVo.setPushResult(content);
		System.out.println(content);
		Integer rs=-1;
		if(StringUtils.isNotBlank(content)){
			rs= content.indexOf("\"state\" : \"success\"");
		}
		//TODO 修改支付记录推送结果
		if(-1<rs){
			payStatementsVo.setPushStatus(1);
		}
		else{//缴费流水重复（推送成功后重复推送情况）
			rs= content.indexOf("\"code\" : \"110\"");
			if(-1<rs){
				payStatementsVo.setPushStatus(1);
			}
		}
		payStatementsSerivce.updatePushStateSuccess(payStatementsVo);
		return rs;
	}
	
	/**
	 * 向业务系统推送支付流水-订单
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/04
	 */
	@Override
    public int pushPayResultOrderNew(PayStatementsVo payStatementsVo){

		Map<String, String> parameters = new HashMap<String, String>();
		
		Integer appId=payStatementsVo.getPayRecordVo().getApplicationId();
    	if(null==appId){
    		appId=CommonsPayTypeConstants.DEFAULT_APP_ID;
    	}
    	App app=appService.getAppById(appId);
//		String url = "http://123.126.152.178:8198/edu/studentPayfee/paymentByPos";
		String url=app.getUrl();
		
		parameters.put("token",app.getToken());	// 安全校验 应用key
		parameters.put("orderNumber",payStatementsVo.getPayRecordVo().getApplicationKeyId());	//订单主键
		parameters.put("serialNumber",payStatementsVo.getSerialNumber()); //支付流水号
		parameters.put("money",payStatementsVo.getMoney().toString());//支付金额
		parameters.put("userNumber",payStatementsVo.getTerminalVo().getLoginName()); //员工号
		parameters.put("posCode",payStatementsVo.getTerminalVo().getPosCode());	//终端编号
		parameters.put("param",payStatementsVo.getPayRecordVo().getApplicationParameter()); //订单额外参数
		parameters.put("remark", null);
		String payDate=null;
		try{
			payDate=DateUtil.dateToString(payStatementsVo.getPayDate(), "yyyy-MM-dd HH:mm:ss");
		}catch(Exception e){
			e.printStackTrace();
		}
		parameters.put("payDate",payDate);	//支付时间
		
		HttpUtil httpUtil=new HttpUtil();
		//输出请求服务器IP监控推送情况
		LOG.info("@pushServerIp:"+httpUtil.getLocalIp()+"  content:"+parameters);
		System.out.println("@pushServerIp:"+httpUtil.getLocalIp()+"  content:"+parameters);
		String content = httpUtil.doPost(url, parameters, "UTF-8");
//		System.out.println(content);
		return content.indexOf("\"result\":0");
	}
	
	
}
