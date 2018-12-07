package com.edupay.pay.vo;

import java.io.Serializable;
import java.util.Date;

import com.edupay.commons.utils.DateUtil;

/**
 * 终端Vo （POS）
 *
 * Title: TerminalVo.java
 * @author: Jack
 * @create: 2018-07-08
 *
 */
public class TerminalVo implements Serializable {
	
	/**
	 * @Description: 
	 * @Author: Jack
	 * @Date: 2018/07/08
	 */
	private static final long serialVersionUID = 960907392914702596L;
	
	public TerminalVo(){}
	
	//版本号 version
	private String version;
	//请求类型 transtype
	private String transType;
	//商户号 shopid
	private String shopId;
	private String zbPayeeType;
	
	//POS机编号 termid
    private String posCode;
    //所属分部 
    private String branchId;
	//员工工号 employno
    private String loginName;
    //校验结果
    private int checkFlag;
    //请求时间 request_time
    private String requestTime;
    //返回时间 response_time
    private String responseTime;
    //mac
    private String mac;

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getZbPayeeType() {
		return zbPayeeType;
	}
	public void setZbPayeeType(String zbPayeeType) {
		this.zbPayeeType = zbPayeeType;
	}
	public String getPosCode() {
		return posCode;
	}
	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public int getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(int checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getResponseTime() {
		try{
			responseTime=DateUtil.dateToString(new Date(),"YYYYMMddHHmmss");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	
}