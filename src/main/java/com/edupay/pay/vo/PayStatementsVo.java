package com.edupay.pay.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.pay.entity.PayStatements;

public class PayStatementsVo {
	
	public PayStatementsVo(){}
	
	public PayStatementsVo(PayStatements ps){
		this.setMoney(ps.getMoney());
		this.setPayAccount(ps.getPayAccount());
		this.setPayCode(ps.getPayCode());
		this.setPayDate(ps.getPayDate());
		this.setPayRecordId(ps.getPayRecordId());
		this.setPayStatementsId(ps.getPayStatementsId());
		this.setSerialNumber(ps.getSerialNumber());
		this.setStatus(ps.getStatus());
		this.setPushStatus(ps.getPushStatus());
		this.setPushResult(ps.getPushResult());
		this.setRequestContent(ps.getRequestContent());
	}
	
	public PayStatements voTransEntity(PayStatementsVo psVo){
		
		PayStatements ps=new PayStatements();
		ps.setMoney(psVo.getMoney());
		ps.setPayAccount(psVo.getPayAccount());
		ps.setPayCode(psVo.getPayCode());
		ps.setPayDate(psVo.getPayDate());
		ps.setPayRecordId(psVo.getPayRecordId());
		ps.setPayStatementsId(psVo.getPayStatementsId());
		ps.setSerialNumber(psVo.getSerialNumber());
		ps.setStatus(psVo.getStatus());
		ps.setPushStatus(psVo.getPushStatus());
		if(null==ps.getPushStatus()){
			ps.setPushStatus(CommonsPayTypeConstants.PAY_RS_PUSH_STATUS_ERROR);
		}
		ps.setPushResult(psVo.getPushResult());
		ps.setRequestContent(psVo.getRequestContent());
		return ps;
	}
	
	/**
     * 终端号
     */
    private String posCode;
    
    /**
     * 商户号
     */
    private String shopId;
	
	/**
     * 员工号
     */
    private String employno;
	
    /**
     * 支付流水id
     */
    private String payStatementsId;

    /**
     * 支付记录id
     */
    private String payRecordId;

    /**
     * 支付单号
     */
    private String payCode;

    /**
     * 应用记录主键id
     */
    private String serialNumber;

    /**
     * 应用记录请求参数
     */
    private BigDecimal money;

    /**
     * 状态：0等待;1成功;2废弃
     */
    private Integer status;

    /**
     * 支付时间
     */
    private Date payDate;
    
    /**
     * 推送状态
     */
    private Integer pushStatus;
    private String  pushResult;
    private String  payTypes;//P033快速支付，P007订单拆分支付
    
    private String requestContent;

    /**
     * 支付账号
     */
    private String payAccount;
    /**
     * 终端信息
     */
    private TerminalVo terminalVo;
    /**
     * 支付端消息
     */
    private PayRecordVo payRecordVo;
    
	public String getPayStatementsId() {
		return payStatementsId;
	}
	public void setPayStatementsId(String payStatementsId) {
		this.payStatementsId = payStatementsId;
	}
	public String getPayRecordId() {
		return payRecordId;
	}
	public void setPayRecordId(String payRecordId) {
		this.payRecordId = payRecordId;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public String getPayAccount() {
		return payAccount;
	}
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
	public TerminalVo getTerminalVo() {
		return terminalVo;
	}
	public void setTerminalVo(TerminalVo terminalVo) {
		this.terminalVo = terminalVo;
	}
	public PayRecordVo getPayRecordVo() {
		return payRecordVo;
	}
	public void setPayRecordVo(PayRecordVo payRecordVo) {
		this.payRecordVo = payRecordVo;
	}

	public String getEmployno() {
		return employno;
	}

	public void setEmployno(String employno) {
		this.employno = employno;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}

	public String getPushResult() {
		return pushResult;
	}

	public void setPushResult(String pushResult) {
		this.pushResult = pushResult;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getPayTypes() {
		return payTypes;
	}

	public void setPayTypes(String payTypes) {
		this.payTypes = payTypes;
	}
}