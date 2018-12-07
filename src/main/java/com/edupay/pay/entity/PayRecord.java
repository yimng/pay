package com.edupay.pay.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bit_pay_record")
public class PayRecord implements Serializable {
    /**
     * 支付记录id
     */
    @Id
    @Column(name = "pay_record_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UUID")
    private String payRecordId;

    /**
     * 支付单号
     */
    @Column(name = "pay_code")
    private String payCode;
    
    /**
     * 支付账户id
     */
    @Column(name = "pay_bank_account_id")
    private Integer payBankAccountId;

    /**
     * 应用id;1教务平台;2官网平台
     */
    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 应用记录主键id
     */
    @Column(name = "application_key_id")
    private String applicationKeyId;

    /**
     * 应用记录请求参数
     */
    @Column(name = "application_parameter")
    private String applicationParameter;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 手续费
     */
    @Column(name = "service_fee")
    private BigDecimal serviceFee;

    /**
     * 支付第三方平台id;1银联;
     */
    @Column(name = "pay_third_id")
    private Integer payThirdId;

    /**
     * 支付第三方平台主键id
     */
    @Column(name = "pay_third_key_id")
    private String payThirdKeyId;

    /**
     * 是否是自动对帐产生的成功;1是;0否
     */
    @Column(name = "is_auto_check")
    private Integer isAutoCheck;

    /**
     * 原始IP
     */
    @Column(name = "initial_ip")
    private String initialIp;

    /**
     * 状态：0等待;1成功;2废弃
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 交费时间
     */
    @Column(name = "pay_date")
    private Date payDate;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;
    
    /**
     * 支付方式
     */
    private Integer payType;

    private static final long serialVersionUID = 1L;

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
     * 获取支付账户id
     *
     * @return pay_bank_account_id - 支付账户id
     */
    public Integer getPayBankAccountId() {
        return payBankAccountId;
    }

    /**
     * 设置支付账户id
     *
     * @param payBankAccountId 支付账户id
     */
    public void setPayBankAccountId(Integer payBankAccountId) {
        this.payBankAccountId = payBankAccountId;
    }

    /**
     * 获取应用id;1教务平台;2官网平台
     *
     * @return application_id - 应用id;1教务平台;2官网平台
     */
    public Integer getApplicationId() {
        return applicationId;
    }

    /**
     * 设置应用id;1教务平台;2官网平台
     *
     * @param applicationId 应用id;1教务平台;2官网平台
     */
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 获取应用记录主键id
     *
     * @return application_key_id - 应用记录主键id
     */
    public String getApplicationKeyId() {
        return applicationKeyId;
    }

    /**
     * 设置应用记录主键id
     *
     * @param applicationKeyId 应用记录主键id
     */
    public void setApplicationKeyId(String applicationKeyId) {
        this.applicationKeyId = applicationKeyId;
    }

    /**
     * 获取应用记录请求参数
     *
     * @return application_parameter - 应用记录请求参数
     */
    public String getApplicationParameter() {
        return applicationParameter;
    }

    /**
     * 设置应用记录请求参数
     *
     * @param applicationParameter 应用记录请求参数
     */
    public void setApplicationParameter(String applicationParameter) {
        this.applicationParameter = applicationParameter;
    }

    /**
     * 获取金额
     *
     * @return money - 金额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置金额
     *
     * @param money 金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取手续费
     *
     * @return service_fee - 手续费
     */
    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    /**
     * 设置手续费
     *
     * @param serviceFee 手续费
     */
    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    /**
     * 获取支付第三方平台id;1银联;
     *
     * @return pay_third_id - 支付第三方平台id;1银联;
     */
    public Integer getPayThirdId() {
        return payThirdId;
    }

    /**
     * 设置支付第三方平台id;1银联;
     *
     * @param payThirdId 支付第三方平台id;1银联;
     */
    public void setPayThirdId(Integer payThirdId) {
        this.payThirdId = payThirdId;
    }

    /**
     * 获取支付第三方平台主键id
     *
     * @return pay_third_key_id - 支付第三方平台主键id
     */
    public String getPayThirdKeyId() {
        return payThirdKeyId;
    }

    /**
     * 设置支付第三方平台主键id
     *
     * @param payThirdKeyId 支付第三方平台主键id
     */
    public void setPayThirdKeyId(String payThirdKeyId) {
        this.payThirdKeyId = payThirdKeyId;
    }

    /**
     * 获取是否是自动对帐产生的成功;1是;0否
     *
     * @return is_auto_check - 是否是自动对帐产生的成功;1是;0否
     */
    public Integer getIsAutoCheck() {
        return isAutoCheck;
    }

    /**
     * 设置是否是自动对帐产生的成功;1是;0否
     *
     * @param isAutoCheck 是否是自动对帐产生的成功;1是;0否
     */
    public void setIsAutoCheck(Integer isAutoCheck) {
        this.isAutoCheck = isAutoCheck;
    }

    /**
     * 获取原始IP
     *
     * @return initial_ip - 原始IP
     */
    public String getInitialIp() {
        return initialIp;
    }

    /**
     * 设置原始IP
     *
     * @param initialIp 原始IP
     */
    public void setInitialIp(String initialIp) {
        this.initialIp = initialIp;
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
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取交费时间
     *
     * @return pay_date - 交费时间
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * 设置交费时间
     *
     * @param payDate 交费时间
     */
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", payRecordId=").append(payRecordId);
        sb.append(", payCode=").append(payCode);
        sb.append(", payBankAccountId=").append(payBankAccountId);
        sb.append(", applicationId=").append(applicationId);
        sb.append(", applicationKeyId=").append(applicationKeyId);
        sb.append(", applicationParameter=").append(applicationParameter);
        sb.append(", money=").append(money);
        sb.append(", serviceFee=").append(serviceFee);
        sb.append(", payThirdId=").append(payThirdId);
        sb.append(", payThirdKeyId=").append(payThirdKeyId);
        sb.append(", isAutoCheck=").append(isAutoCheck);
        sb.append(", initialIp=").append(initialIp);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", payDate=").append(payDate);
        sb.append(", createDate=").append(createDate);
        sb.append("]");
        return sb.toString();
    }

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
}