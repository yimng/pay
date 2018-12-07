package com.edupay.pay.service;

import com.edupay.pay.vo.PayStatementsVo;

/**
 * 支付流水 业务接口
 *
 * Title: PayStatementsServiceImpl.java
 * @author: Jack
 * @create: 2018-06-21
 *
 */
public interface PosPushPayResultService {
	
	/**
	 * 向业务系统推送支付流水-快速支付
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/04
	 */
    public int pushPayResultQuick(PayStatementsVo payStatementsVo);
    
    /**
	 * 向业务系统推送支付流水-订单
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/04
	 */
    public int pushPayResultOrder(PayStatementsVo payStatementsVo);
    
    /**
	 * 向业务系统推送支付流水-订单
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/04
	 */
    public int pushPayResultOrderNew(PayStatementsVo payStatementsVo);
    
}
