package com.edupay.pay.service;

import java.util.Map;

/**
 * 支付流水 业务接口
 *
 * Title: PayStatementsServiceImpl.java
 * @author: Jack
 * @create: 2018-06-21
 *
 */
public interface PushPayResultService {
    
    /**
	 * 支付结果补推
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/08/10
	 */
    public void pushPayResultTask();
    
    /**
     * 推送业务系统支付反馈
     * @param payCode       订单号
     * @param serialNumber  流水号
     * @param buyer_id      买家ID
     * @param payWay        支付方式  2支付宝，4微信，
     * @return
     * @author yangzhenlin
     */
    public int pushPayResult(String payCode, String serialNumber, String buyer_id, String payWay);
    
    /**
     * 推送到各系统平台支付反馈
     * @param payCode
     * @param payStatus
     * @return
     * @author yangzhenlin
     */
    public Map<String, String> pushPayResultForEdu(String payCode, String payStatus);
    
}
