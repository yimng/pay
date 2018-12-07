package com.edupay.unionpay.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "bit_unionpay_record")
public class UnionpayRecord {
    /**
     * 银联交易对账id
     */
    @Id
    @Column(name = "unionpay_record_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UUID")
    private String unionpayRecordId;

    /**
     * 应用记录主键id(订单号)
     */
    @Column(name = "application_key_id")
    private String applicationKeyId;

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
     * 对账状态：0没有对账;1对账成功;
     */
    private Integer status;

    /**
     * 支付第三方平台主键id(银联记录id)
     */
    @Column(name = "pay_third_key_id")
    private String payThirdKeyId;

    /**
     * 支付第三方平台交易时间
     */
    @Column(name = "pay_date")
    private Date payDate;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 获取银联交易对账id
     *
     * @return unionpay_record_id - 银联交易对账id
     */
    public String getUnionpayRecordId() {
        return unionpayRecordId;
    }

    /**
     * 设置银联交易对账id
     *
     * @param unionpayRecordId 银联交易对账id
     */
    public void setUnionpayRecordId(String unionpayRecordId) {
        this.unionpayRecordId = unionpayRecordId;
    }

    /**
     * 获取应用记录主键id(订单号)
     *
     * @return application_key_id - 应用记录主键id(订单号)
     */
    public String getApplicationKeyId() {
        return applicationKeyId;
    }

    /**
     * 设置应用记录主键id(订单号)
     *
     * @param applicationKeyId 应用记录主键id(订单号)
     */
    public void setApplicationKeyId(String applicationKeyId) {
        this.applicationKeyId = applicationKeyId;
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
     * 获取对账状态：0没有对账;1对账成功;
     *
     * @return status - 对账状态：0没有对账;1对账成功;
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置对账状态：0没有对账;1对账成功;
     *
     * @param status 对账状态：0没有对账;1对账成功;
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取支付第三方平台主键id(银联记录id)
     *
     * @return pay_third_key_id - 支付第三方平台主键id(银联记录id)
     */
    public String getPayThirdKeyId() {
        return payThirdKeyId;
    }

    /**
     * 设置支付第三方平台主键id(银联记录id)
     *
     * @param payThirdKeyId 支付第三方平台主键id(银联记录id)
     */
    public void setPayThirdKeyId(String payThirdKeyId) {
        this.payThirdKeyId = payThirdKeyId;
    }

    /**
     * 获取支付第三方平台交易时间
     *
     * @return pay_date - 支付第三方平台交易时间
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * 设置支付第三方平台交易时间
     *
     * @param payDate 支付第三方平台交易时间
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
}