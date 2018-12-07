package com.edupay.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bit_pay_statements")
public class PayStatements {
    /**
     * 支付流水id
     */
    @Id
    @Column(name = "pay_statements_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UUID")
    private String payStatementsId;

    /**
     * 支付记录id
     */
    @Column(name = "pay_record_id")
    private String payRecordId;

    /**
     * 应用id;1教务平台;2官网平台
     */
    @Column(name = "pay_code")
    private String payCode;

    /**
     * 应用记录主键id
     */
    @Column(name = "serial_number")
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
    @Column(name = "pay_date")
    private Date payDate;
    
    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 支付账号
     */
    @Column(name = "pay_account")
    private String payAccount;
    
    /**
     * 请求报文
     */
    @Column(name = "request_content")
    private String requestContent;
    
    /**
     * 推送状态
     */
    @Column(name = "push_status")
    private Integer pushStatus;
    

    /**
     * 推送状态
     */
    @Column(name = "push_result")
    private String pushResult;

    /**
     * 获取支付流水id
     *
     * @return pay_statements_id - 支付流水id
     */
    public String getPayStatementsId() {
        return payStatementsId;
    }

    /**
     * 设置支付流水id
     *
     * @param payStatementsId 支付流水id
     */
    public void setPayStatementsId(String payStatementsId) {
        this.payStatementsId = payStatementsId;
    }

    /**
     * 获取支付记录id
     *
     * @return pay_record_id - 支付记录id
     */
    public String getPayRecordId() {
        return payRecordId;
    }

    /**
     * 设置支付记录id
     *
     * @param payRecordId 支付记录id
     */
    public void setPayRecordId(String payRecordId) {
        this.payRecordId = payRecordId;
    }

    /**
     * 获取应用id;1教务平台;2官网平台
     *
     * @return pay_code - 应用id;1教务平台;2官网平台
     */
    public String getPayCode() {
        return payCode;
    }

    /**
     * 设置应用id;1教务平台;2官网平台
     *
     * @param payCode 应用id;1教务平台;2官网平台
     */
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    /**
     * 获取应用记录主键id
     *
     * @return serial_number - 应用记录主键id
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * 设置应用记录主键id
     *
     * @param serialNumber 应用记录主键id
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * 获取应用记录请求参数
     *
     * @return money - 应用记录请求参数
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置应用记录请求参数
     *
     * @param money 应用记录请求参数
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取状态：0等待;1成功;2废弃
     *
     * @return status - 状态：0等待;1成功;2废弃
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0等待;1成功;2废弃
     *
     * @param status 状态：0等待;1成功;2废弃
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取支付时间
     *
     * @return pay_date - 支付时间
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * 设置支付时间
     *
     * @param payDate 支付时间
     */
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    /**
     * 获取支付账号
     *
     * @return pay_account - 支付账号
     */
    public String getPayAccount() {
        return payAccount;
    }

    /**
     * 设置支付账号
     *
     * @param payAccount 支付账号
     */
    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    /**
     * 获取推送状态
     *
     * @return push_status - 推送状态
     */
	public Integer getPushStatus() {
		return pushStatus;
	}

	/**
     * 设置推送状态
     *
     * @param pushStatus 推送状态
     */
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}