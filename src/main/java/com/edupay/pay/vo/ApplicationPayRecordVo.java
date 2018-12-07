package com.edupay.pay.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 对接业务系统接收支付请求Vo类
 *
 * Title: ApplicationPayRecordVo.java
 * @author: Jack
 * @create: 2018-06-25
 *
 */
public class ApplicationPayRecordVo implements Serializable {

	public ApplicationPayRecordVo(){}
    
	/**
	 * 订单ID
	 */
	private String orderId;	
	/**
	 * 订单编号
	 */
	private String orderNo;	
	/**
	 * 应支付金额
	 */
	private BigDecimal money;	
	/**
	 * 账户类型
	 */
	private Integer zbPayeeType;	
	/**
	 * 费用分类
	 */
	private Integer feeClass;	
	/**
	 * 是否允许拆单
	 */
	private Integer isSplit;		
	/**
	 * 账户ID
	 */
	private String bankAccountId;
	/**
	 * 分部ID
	 */
	private String branchId;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	
	/**
	 * @Description: 
	 * @Author: Jack
	 * @Date: 2018/06/25
	 */
	private static final long serialVersionUID = -5734191489227898067L;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getZbPayeeType() {
		return zbPayeeType;
	}

	public void setZbPayeeType(Integer zbPayeeType) {
		this.zbPayeeType = zbPayeeType;
	}

	public Integer getFeeClass() {
		return feeClass;
	}

	public void setFeeClass(Integer feeClass) {
		this.feeClass = feeClass;
	}

	public Integer getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(Integer isSplit) {
		this.isSplit = isSplit;
	}

	public String getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
}