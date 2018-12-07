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
public class CommonsApiErrorCodeConstants {
	
	/**
	 * 参数错误
	 */
	public static final String PARAM_ERROR = "001";
	
	
	/**
	 * 没有授权
	 */
	public static final String NO_AUTHORIZATION = "401";
	
	/**
	 * HttpRequestMethodNotSupportedException
	 */
	public static final String METHOD_NOT_SUPPORTED = "405";
	
	/**
	 * 后台错误
	 */
	public static final String EXCEPTION_EROOR = "501";
	
	/**
	 * 未知错误
	 */
	public static final String UNKNOW_ERROR = "999";
	
	/**
	 * 未登录
	 */
	public static final String NO_LOGIN = "1000";
	
	/**
	 * 用户名或密码错误
	 */
	public static final String INCORRECT_USERNAME_OR_PASSWORD = "1001";
	/**
	 * 已经存在
	 */
	public static final String NAME_AREALDY="1002";
	
}
