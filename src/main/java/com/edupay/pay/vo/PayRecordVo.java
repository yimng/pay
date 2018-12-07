package com.edupay.pay.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.edupay.pay.entity.PayRecord;

/**
 * 支付单 Vo 类
 *
 * Title: PayRecordVo.java
 * @author: Jack
 * @create: 2018-07-08
 *
 */
public class PayRecordVo implements Serializable {
	
	public PayRecordVo(){}
	
	public PayRecordVo(PayRecord pr){
		this.setApplicationId(pr.getApplicationId());
		this.setApplicationKeyId(pr.getApplicationKeyId());
		this.setApplicationParameter(pr.getApplicationParameter());
		this.setCreateDate(pr.getCreateDate());
		this.setInitialIp(pr.getInitialIp());
		this.setIsAutoCheck(pr.getIsAutoCheck());
		this.setMoney(pr.getMoney());
		this.setPayMoney(pr.getMoney());
		this.setPayBankAccountId(pr.getPayBankAccountId());
		this.setPayDate(pr.getPayDate());
		this.setPayRecordId(pr.getPayRecordId());
		this.setPayThirdId(pr.getPayThirdId());
		this.setPayThirdKeyId(pr.getPayThirdKeyId());
		this.setRemark(pr.getRemark());
		this.setServiceFee(pr.getServiceFee());
		this.setStatus(pr.getStatus());
		this.setPayCode(pr.getPayCode());
		this.setPayType(pr.getPayType());
	}
	
	public PayRecord voTransEntity(PayRecordVo prVo){
		
		PayRecord pr=new PayRecord();
		pr.setApplicationId(prVo.getApplicationId());
		pr.setApplicationKeyId(prVo.getApplicationKeyId());
		pr.setApplicationParameter(prVo.getApplicationParameter());
		pr.setCreateDate(prVo.getCreateDate());
		pr.setInitialIp(prVo.getInitialIp());
		pr.setIsAutoCheck(prVo.getIsAutoCheck());
		pr.setMoney(prVo.getMoney());
		pr.setPayBankAccountId(prVo.getPayBankAccountId());
		pr.setPayDate(prVo.getPayDate());
		pr.setPayRecordId(prVo.getPayRecordId());
		pr.setPayThirdId(prVo.getPayThirdId());
		pr.setPayThirdKeyId(prVo.getPayThirdKeyId());
		pr.setRemark(prVo.getRemark());
		pr.setServiceFee(prVo.getServiceFee());
		pr.setStatus(prVo.getStatus());
		pr.setPayCode(prVo.getPayCode());
		pr.setPayType(prVo.getPayType());
		return pr;
	}
    
	/**
     * 支付记录id
     */
	private String payRecordId;
	private String payCode;

    /**
     * 支付账户id
     */
    private Integer payBankAccountId;

    /**
     * 应用id;1教务平台;2官网平台
     */
    private Integer applicationId;

    /**
     * 应用记录主键id
     */
    private String applicationKeyId;

    /**
     * 应用记录请求参数
     */
    private String applicationParameter;

    /**
     * 金额
     */
    private BigDecimal money;		//应支付金额
    private BigDecimal payMoney;	//待支付金额
    
    /**
     * 手续费
     */
    private BigDecimal serviceFee;

    /**
     * 支付第三方平台id;1银联;
     */
    private Integer payThirdId;

    /**
     * 支付第三方平台主键id
     */
    private String payThirdKeyId;

    /**
     * 是否是自动对帐产生的成功;1是;0否
     */
    private Integer isAutoCheck;
    
    /**
     * 原始IP
     */
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
    private Date payDate;

    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 支付方式 10-本地POS，11-跨区POS，20-支付宝，30-微信
     */
    private Integer payType;
    
    /**
     * 支付设备    "web"网页支付； "mobile"手机支付
     */
    private String payEqu;
    
    /**
     * 支付浏览器类型   "weixin" 微信内置浏览器； "other" 其他浏览器
     */
    private String browserType;

    private static final long serialVersionUID = 1L;

	public String getPayEqu() {
		return payEqu;
	}

	public void setPayEqu(String payEqu) {
		this.payEqu = payEqu;
	}

	public String getPayRecordId() {
		return payRecordId;
	}

	public void setPayRecordId(String payRecordId) {
		this.payRecordId = payRecordId;
	}

	public Integer getPayBankAccountId() {
		return payBankAccountId;
	}

	public void setPayBankAccountId(Integer payBankAccountId) {
		this.payBankAccountId = payBankAccountId;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationKeyId() {
		return applicationKeyId;
	}

	public void setApplicationKeyId(String applicationKeyId) {
		this.applicationKeyId = applicationKeyId;
	}

	public String getApplicationParameter() {
		return applicationParameter;
	}

	public void setApplicationParameter(String applicationParameter) {
		this.applicationParameter = applicationParameter;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getPayThirdId() {
		return payThirdId;
	}

	public void setPayThirdId(Integer payThirdId) {
		this.payThirdId = payThirdId;
	}

	public String getPayThirdKeyId() {
		return payThirdKeyId;
	}

	public void setPayThirdKeyId(String payThirdKeyId) {
		this.payThirdKeyId = payThirdKeyId;
	}

	public Integer getIsAutoCheck() {
		return isAutoCheck;
	}

	public void setIsAutoCheck(Integer isAutoCheck) {
		this.isAutoCheck = isAutoCheck;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getInitialIp() {
		return initialIp;
	}

	public void setInitialIp(String initialIp) {
		this.initialIp = initialIp;
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
	
	
}