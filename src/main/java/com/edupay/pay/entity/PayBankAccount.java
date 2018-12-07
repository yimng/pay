package com.edupay.pay.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "bit_pay_bank_account")
public class PayBankAccount implements Serializable {
    /**
     * 支付账户id
     */
    @Id
    @Column(name = "pay_bank_account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payBankAccountId;

    /**
     * 应用id;1教务平台;2官网平台
     */
    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 应用平台账户id
     */
    @Column(name = "application_account_id")
    private String applicationAccountId;

    /**
     * 账户类型：1银联;2支付宝;3微信
     */
    @Column(name = "account_type")
    private Integer accountType;

    /**
     * 账户名
     */
    @Column(name = "bank_account_name")
    private String bankAccountName;

    /**
     * 账号
     */
    @Column(name = "bank_account_no")
    private String bankAccountNo;

    /**
     * 大客户id
     */
    @Column(name = "partner_id")
    private String partnerId;

    /**
     * 开户行
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 开户行网点
     */
    @Column(name = "bank_branch")
    private String bankBranch;

    /**
     * 状态：1启用;0停用
     */
    private Integer status;

    /**
     * 省市
     */
    private String city;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 创建人用户id
     */
    @Column(name = "create_uid")
    private String createUid;

    private static final long serialVersionUID = 1L;

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
     * 获取应用平台账户id
     *
     * @return application_account_id - 应用平台账户id
     */
    public String getApplicationAccountId() {
        return applicationAccountId;
    }

    /**
     * 设置应用平台账户id
     *
     * @param applicationAccountId 应用平台账户id
     */
    public void setApplicationAccountId(String applicationAccountId) {
        this.applicationAccountId = applicationAccountId;
    }

    /**
     * 获取账户类型：1银联;2支付宝;3微信
     *
     * @return account_type - 账户类型：1银联;2支付宝;3微信
     */
    public Integer getAccountType() {
        return accountType;
    }

    /**
     * 设置账户类型：1银联;2支付宝;3微信
     *
     * @param accountType 账户类型：1银联;2支付宝;3微信
     */
    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    /**
     * 获取账户名
     *
     * @return bank_account_name - 账户名
     */
    public String getBankAccountName() {
        return bankAccountName;
    }

    /**
     * 设置账户名
     *
     * @param bankAccountName 账户名
     */
    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    /**
     * 获取账号
     *
     * @return bank_account_no - 账号
     */
    public String getBankAccountNo() {
        return bankAccountNo;
    }

    /**
     * 设置账号
     *
     * @param bankAccountNo 账号
     */
    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    /**
     * 获取大客户id
     *
     * @return partner_id - 大客户id
     */
    public String getPartnerId() {
        return partnerId;
    }

    /**
     * 设置大客户id
     *
     * @param partnerId 大客户id
     */
    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    /**
     * 获取开户行
     *
     * @return bank_name - 开户行
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置开户行
     *
     * @param bankName 开户行
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * 获取开户行网点
     *
     * @return bank_branch - 开户行网点
     */
    public String getBankBranch() {
        return bankBranch;
    }

    /**
     * 设置开户行网点
     *
     * @param bankBranch 开户行网点
     */
    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    /**
     * 获取状态：1启用;0停用
     *
     * @return status - 状态：1启用;0停用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：1启用;0停用
     *
     * @param status 状态：1启用;0停用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取省市
     *
     * @return city - 省市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置省市
     *
     * @param city 省市
     */
    public void setCity(String city) {
        this.city = city;
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

    /**
     * 获取创建人用户id
     *
     * @return create_uid - 创建人用户id
     */
    public String getCreateUid() {
        return createUid;
    }

    /**
     * 设置创建人用户id
     *
     * @param createUid 创建人用户id
     */
    public void setCreateUid(String createUid) {
        this.createUid = createUid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", payBankAccountId=").append(payBankAccountId);
        sb.append(", applicationId=").append(applicationId);
        sb.append(", applicationAccountId=").append(applicationAccountId);
        sb.append(", accountType=").append(accountType);
        sb.append(", bankAccountName=").append(bankAccountName);
        sb.append(", bankAccountNo=").append(bankAccountNo);
        sb.append(", partnerId=").append(partnerId);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankBranch=").append(bankBranch);
        sb.append(", status=").append(status);
        sb.append(", city=").append(city);
        sb.append(", remark=").append(remark);
        sb.append(", createDate=").append(createDate);
        sb.append(", createUid=").append(createUid);
        sb.append("]");
        return sb.toString();
    }
}