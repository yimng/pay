package com.edupay.pay.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edupay.commons.utils.PageResult;
import com.edupay.pay.dao.PayRecordMapper;
import com.edupay.pay.entity.PayRecord;
import com.edupay.pay.entity.PayStatements;
import com.edupay.pay.service.PayRecordService;
import com.edupay.pay.service.PayStatementsService;
import com.edupay.pay.vo.PayRecordVo;

import tk.mybatis.mapper.entity.Example;

/**
 * 支付单 业务实现类
 *
 * Title: PayRecordServiceImpl.java
 * @author: Jack
 * @create: 2018-06-21
 *
 */
@Component
@Service
public class PayRecordServiceImpl implements PayRecordService {

	
	@Autowired
	private PayRecordMapper payRecordMapper;
	@Autowired
	private PayStatementsService payStatementsSerivce;
	
	/**
	 * 新增
	 *
	 * Title PayRecordServiceImpl.java
	 * @author Jack
	 * @date Jun 21, 2018
	 */
	@Override
	@Transactional
	public String add(PayRecord payRecord){
		if(null==payRecord)
			return null;
		payRecord.setStatus(0);
		payRecord.setCreateDate(new Date());
		if(StringUtils.isBlank(payRecord.getPayCode())){
			payRecord.setPayCode(buildOrderSn());
		}
		if(!isExistByPayCode(payRecord.getPayCode())){
			payRecordMapper.insert(payRecord);
		}
		return payRecord.getPayCode();
	}
	
	public String buildOrderSn() {
		String orderSn;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		do {
			String strOrderSn = format.format(new Date());
			orderSn = "PY" + strOrderSn;//(longOrderSn + 1);
		} while (isExistByPayCode(orderSn));
		return orderSn;
	}
	
	/**
	 * 支付单号重复性校验
	 *
	 * @param payCode	订单编号
	 * @return	是否重复，true重复、false非重复
	 */
	private boolean isExistByPayCode(String payCode){
		boolean flag = false;
		//空判断
		if(StringUtils.isBlank(payCode)){
			return flag;
		}
		//根据支付单号查询
		
		Example example = new Example(PayRecord.class);
		
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("payCode", payCode);
		
    	//查询该订单编号条数
    	int count = payRecordMapper.selectCountByExample(example);
    	//若存在数据则返回true
    	if(count > 0){
    		flag = true;
    	}
		return flag;
	}
	
	/**
	 * 根据支付单号查询支付信息
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/06/21
	 */
	@Override
	public PayRecordVo searchByPayCode(String payCode) {
		
		Example example = new Example(PayRecord.class);
		
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("payCode", payCode);
		
		//查询数据
		List<PayRecord> list = payRecordMapper.selectByExample(example);
		PayRecord pr=null;
		if(null!=list&&1==list.size()){
			pr=list.get(0);
		}
		PayRecordVo prVo=null;
		//查询待支付金额
		if(null!=pr){
			prVo=new PayRecordVo(pr);
			List<PayStatements> psLst=payStatementsSerivce.searchByPayCode(pr.getPayCode());
			if(null!=psLst){
				BigDecimal payMoney=pr.getMoney();
				for(PayStatements ps:psLst){
					payMoney=payMoney.subtract(ps.getMoney());
					if(0<payMoney.compareTo(BigDecimal.ZERO)){
						prVo.setPayMoney(payMoney);
					}
					else{
						prVo.setPayMoney(BigDecimal.ZERO);
					}
				}
			}
		}
		return prVo;
	}

	
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
	@Override
	public PageResult searchPage(PayRecord payRecord,Integer currentPage,Integer pageSize) {
		//封装返回数据
		PageResult page = new PageResult();
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		Example example = new Example(PayRecord.class);
		Example.Criteria criteria=example.createCriteria();
		if(null!=payRecord.getApplicationId()){
			criteria.andEqualTo("applicationId",payRecord.getApplicationId());
		}
		if(StringUtils.isNotBlank(payRecord.getPayCode())){
			criteria.andLike("payCode", "%"+payRecord.getPayCode()+"%");
		}
		//查询总条数
		int count = payRecordMapper.selectCountByExample(example);
		List<PayRecord> list = payRecordMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getStartRecord(),page.getPageSize()));
    	page.setTotalRecord(count);
    	page.setPageResult(list);
		return page;
	}
}
