package com.edupay.pay.service;

import com.edupay.commons.utils.PageResult;
import com.edupay.pay.entity.PayRecord;
import com.edupay.pay.vo.PayRecordVo;

/**
 * 支付单 业务接口
 *
 * Title: PayRecordServiceImpl.java
 * @author: Jack
 * @create: 2018-06-21
 *
 */
public interface PayRecordService {
	
	/**
	 * 新增
	 * 
	 * @param adminUser
	 * @return
	 * @Author: Jack
	 * @Date: 2018/05/03
	 */
	public String add(PayRecord payRecord);
	
	/**
	 * 根据支付单号查询支付信息
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/06/21
	 */
	public PayRecordVo searchByPayCode(String payCode);
	
	
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
	public PageResult searchPage(PayRecord payRecord,Integer currentPage,Integer pageSize);
	
}
