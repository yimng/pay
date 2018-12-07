package com.edupay.commons.constants;


/**
 * API错误号常量
 * 
 * 业务常量建议范围定义在100-900之间
 * 
 * Title 
 * @author: dongminghao
 * @date: 2018年4月24日
 */
public class CommonsPayTypeConstants {
	
	/**
	 * 支付类型
	 */
	public static final Integer PAY_TYPE_POS_LOCAL = 10;//本部支付
	public static final Integer PAY_TYPE_POS_OTHER = 11;//异地缴费
	public static final Integer PAY_TYPE_ALI = 20;
	public static final Integer PAY_TYPE_WX = 30;
	
	/**
	 * 支付设备类型
	 */
	public static final String PAY_TYPE_WEB = "web";        //网页支付
	public static final String PAY_TYPE_MOBILE = "mobile";  //手机支付
	
	/**
	 * POS支付报文类型
	 */
	public static final String PAY_POS_TYPE_QUICK_CHECK = "P034";
	public static final String PAY_POS_TYPE_QUICK_PAY = "P033";
	public static final String PAY_POS_TYPE_ORDER_CHECK = "P074";
	public static final String PAY_POS_TYPE_ORDER_PAY = "P007";
	
	/**
	 * 签约公司
	 */
	public static final Integer PAY_ZB_PAYEE_TYPE_ZBEDU=1;
	public static final Integer PAY_ZB_PAYEE_TYPE_ZBCT=2;
	
	/**
	 * 默认接入APP 
	 */
	public static final Integer DEFAULT_APP_ID=1;	//教务系统
	
	/**
	 * 支付结果推送状态
	 */
	public static final Integer PAY_RS_PUSH_STATUS_SUCCESS=1; //已推送
	public static final Integer PAY_RS_PUSH_STATUS_ERROR=0;	  //未推送
	
	/**
	 * 
	 */
	public static final Integer PAY_TYPE_POS_TYPE_ORDER=1;//订单支付
	public static final Integer PAY_TYPE_POS_TYPE_QUICK=0;//快捷支付
}
