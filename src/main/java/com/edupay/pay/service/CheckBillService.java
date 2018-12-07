package com.edupay.pay.service;

/**
 * POS机支付 业务接口
 *
 * Title: PayStatementsServiceImpl.java
 * @author: Jack
 * @create: 2018-06-21
 *
 */
public interface CheckBillService {
	
    /**
     * 支付对账
     * 
     * @Author: Jack
     * @Date: 2018/07/18
     */
    public void checkBill();
    
    /**
	 * 修复补录下载对账文件对账
	 * @author Jack
	 * @param beginDate
	 * @data  Oct 11, 2018
	 */
	public void repairCheckBill(String beginDate);
	
}
