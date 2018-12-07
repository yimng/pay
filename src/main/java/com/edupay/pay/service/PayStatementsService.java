package com.edupay.pay.service;

import java.util.List;

import com.edupay.commons.utils.PageResult;
import com.edupay.pay.entity.PayStatements;
import com.edupay.pay.vo.PayStatementsVo;

/**
 * 支付流水 业务接口
 *
 * Title: PayStatementsServiceImpl.java
 * @author: Jack
 * @create: 2018-06-21
 *
 */
public interface PayStatementsService {
	
	/**
	 * 新增
	 * 
	 * @param adminUser
	 * @return
	 * @Author: Jack
	 * @Date: 2018/05/03
	 */
	public String add(PayStatementsVo payStatementsVo,Integer payType);
	
	/**
	 * 根据支付单号查询支付信息
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/06/21
	 */
	public PayStatements searchBySerialNumber(String serialNumber);
	
	/**
	 * 根据 支付单号查询支付流水集合
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/05
	 */
	public List<PayStatements> searchByPayCode(String payCode);
	
	
	/**
	 * 分页查询
	 * 
	 * @param payRecord
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @Author: Jack
	 * @Date: 2018/06/21
	 */
	public PageResult searchPage(PayStatements payStatements,Integer currentPage,Integer pageSize);
	
	/**
	 * 更新支付通知推送状态
	 * 
	 * @param psVo
	 * @Author: Jack
	 * @Date: 2018/07/31
	 */
	public void updatePushStateSuccess(PayStatementsVo psVo);
	
	/**
	 * 查询推送失败支付流水集合
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/08/10	
	 */
	public List<PayStatements> searchByPushError();
}
