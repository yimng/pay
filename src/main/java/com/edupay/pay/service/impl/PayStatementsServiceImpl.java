package com.edupay.pay.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.PageResult;
import com.edupay.commons.utils.XmlUtil;
import com.edupay.pay.dao.PayStatementsMapper;
import com.edupay.pay.entity.PayStatements;
import com.edupay.pay.service.PayRecordService;
import com.edupay.pay.service.PayStatementsService;
import com.edupay.pay.vo.PayRecordVo;
import com.edupay.pay.vo.PayStatementsVo;

import tk.mybatis.mapper.entity.Example;

/**
 * 支付流水 业务实现类
 *
 * Title: PayStatementsServiceImpl.java
 * @author: Jack
 * @create: 2018-06-21
 *
 */
@Component
@Service
public class PayStatementsServiceImpl implements PayStatementsService {
	
	@Autowired
	private PayStatementsMapper payStatementsMapper;
	@Autowired
	private PayRecordService payRecordService;
	
	private int insert(PayStatements ps){
		//创建时间
		ps.setCreateDate(new Date());
		return payStatementsMapper.insert(ps);
	}
	
	/**
	 * 新增支付流水
	 *
	 * Title PayRecordServiceImpl.java
	 * @author Jack
	 * @date Jun 21, 2018
	 */
	@Override
	@Transactional
	public String add(PayStatementsVo payStatementsVo,Integer payType){
		String rs=null;
		//POS机支付
		if(CommonsPayTypeConstants.PAY_TYPE_POS_LOCAL.equals(payType)||CommonsPayTypeConstants.PAY_TYPE_POS_OTHER.equals(payType)){
			rs=addPos(payStatementsVo);
		}
		//支付宝支付
		if(CommonsPayTypeConstants.PAY_TYPE_ALI.equals(payType)){
			rs=addAliPay(payStatementsVo);
		}
		//微信支付
		if(CommonsPayTypeConstants.PAY_TYPE_WX.equals(payType)){
			rs=addWeiXinPay(payStatementsVo);
		}
		return rs;
	}
	
	
	
	/**
	 * POS机支付-支付通知
	 * 
	 * @param payStatementsVo
	 * @return
	 * @Author: Jack
	 * @Date: 2018/08/01
	 */
	private String addPos(PayStatementsVo payStatementsVo){
		int r=0;
		if(null==payStatementsVo)
			return null;
		//校验支付流水是否存在
		PayStatements old=searchBySerialNumber(payStatementsVo.getSerialNumber());
		PayStatements ps=payStatementsVo.voTransEntity(payStatementsVo);
		//查询支付单号是否存在
		PayRecordVo prVo=payRecordService.searchByPayCode(ps.getPayCode());
		int type=1;//默认订单支付
		if(null==prVo){
			type=0;//无支付单则变成快速支付，并自动增加支付单
			prVo=new PayRecordVo();
			//默认教务系统
			prVo.setApplicationId(CommonsPayTypeConstants.DEFAULT_APP_ID);
			prVo.setCreateDate(ps.getPayDate());
			prVo.setMoney(ps.getMoney());
			prVo.setPayCode(ps.getPayCode());
			prVo.setPayDate(ps.getPayDate());
			prVo.setStatus(ps.getStatus());
			payRecordService.add(prVo.voTransEntity(prVo));
			prVo=payRecordService.searchByPayCode(ps.getPayCode());
			if(null!=prVo){
				ps.setPayRecordId(prVo.getPayRecordId());
			}
			payStatementsVo.setPayRecordVo(prVo);
		}
		//如果流水号不重复则执行新增
		if(null==old){
			ps.setPayRecordId(prVo.getPayRecordId());
			//如果是对账文件
			ps= initRequestContent(ps,payStatementsVo);
			r= insert(ps);
			payStatementsVo.setPayRecordVo(prVo);
			if(null==prVo.getPayType()){
				type=0;
			}
			if(0<r){
				//TODO 推送业务系统支付结果
				//快速支付
				if(0==type){
					//屏蔽实施推送业务系统，改为自动任务轮询推送
//					posPushPayResultSerivce.pushPayResultQuick(payStatementsVo);
				}
				//订单支付
				if(1==type){
					//屏蔽实施推送业务系统，改为自动任务轮询推送
//					posPushPayResultSerivce.pushPayResultOrder(payStatementsVo);
				}
				return ps.getSerialNumber();
			}
		}
		return null;
	}
	
	/**
	 * 当使用对账文件补录支付通知时初始化请求报文
	 * @param payStatementsVo
	 * @return
	 */
	private PayStatements initRequestContent(PayStatements ps,PayStatementsVo payStatementsVo){
		if(StringUtils.isBlank(ps.getRequestContent())){
			ps.setRequestContent(XmlUtil.createCheckBillRequestContent(payStatementsVo));
		}
		return ps;
	}
	
	/**
	 * 支付宝支付-支付通知
	 * 
	 * @param payStatementsVo
	 * @return
	 * @Author: Jack
	 * @Date: 2018/08/01
	 */
	private String addAliPay(PayStatementsVo payStatementsVo){
		int r=0;
		if(null==payStatementsVo)
			return null;
		//校验支付流水是否存在
		PayStatements old=searchBySerialNumber(payStatementsVo.getSerialNumber());
		PayStatements ps=payStatementsVo.voTransEntity(payStatementsVo);
		//查询支付单号是否存在
		PayRecordVo prVo=payRecordService.searchByPayCode(ps.getPayCode());
//		int type=1;//默认订单支付
		if(null==prVo){
//			type=0;//无支付单则变成快速支付，并自动增加支付单
			prVo=new PayRecordVo();
			prVo.setApplicationId(1);
			prVo.setCreateDate(ps.getPayDate());
			prVo.setMoney(ps.getMoney());
			prVo.setPayCode(ps.getPayCode());
			prVo.setPayDate(ps.getPayDate());
			prVo.setStatus(ps.getStatus());
			payRecordService.add(prVo.voTransEntity(prVo));
			prVo=payRecordService.searchByPayCode(ps.getPayCode());
			if(null!=prVo){
				ps.setPayRecordId(prVo.getPayRecordId());
			}
			payStatementsVo.setPayRecordVo(prVo);
		}
		//如果流水号不重复则执行新增
		if(null==old){
			r= insert(ps);
			if(0<r){
				//TODO 推送业务系统支付结果
//				pushPayResultSerivce.pushPayResultOrder(payStatementsVo);
				return ps.getSerialNumber();
			}
		}
		return null;
	}
	
	/**
	 * 支付宝支付-支付通知
	 * 
	 * @param payStatementsVo
	 * @return
	 * @Author: Jack
	 * @Date: 2018/08/01
	 */
	private String addWeiXinPay(PayStatementsVo payStatementsVo){
		int r=0;
		if(null==payStatementsVo)
			return null;
		//校验支付流水是否存在
		PayStatements old=searchBySerialNumber(payStatementsVo.getSerialNumber());
		PayStatements ps=payStatementsVo.voTransEntity(payStatementsVo);
		//查询支付单号是否存在
		PayRecordVo prVo=payRecordService.searchByPayCode(ps.getPayCode());
//		int type=1;//默认订单支付
		if(null==prVo){
//			type=0;//无支付单则变成快速支付，并自动增加支付单
			prVo=new PayRecordVo();
			prVo.setApplicationId(1);
			prVo.setCreateDate(ps.getPayDate());
			prVo.setMoney(ps.getMoney());
			prVo.setPayCode(ps.getPayCode());
			prVo.setPayDate(ps.getPayDate());
			prVo.setStatus(ps.getStatus());
			payRecordService.add(prVo.voTransEntity(prVo));
			prVo=payRecordService.searchByPayCode(ps.getPayCode());
			if(null!=prVo){
				ps.setPayRecordId(prVo.getPayRecordId());
			}
			payStatementsVo.setPayRecordVo(prVo);
		}
		//如果流水号不重复则执行新增
		if(null==old){
			r= insert(ps);
			if(0<r){
				//TODO 推送业务系统支付结果
//				posPushPayResultSerivce.pushPayResultOrder(payStatementsVo);
				return ps.getSerialNumber();
			}
		}
		return null;
	}
	
	/**
	 * 支付流水号重复性校验
	 *
	 * @param payCode	订单编号
	 * @return	是否重复，true重复、false非重复
	 */
	public boolean isExistBySerialNumber(String serialNumber){
		boolean flag = false;
		//空判断
		if(StringUtils.isBlank(serialNumber)){
			return flag;
		}
		//根据支付单号查询
		PayStatements payStatements=new PayStatements();
		payStatements.setSerialNumber(serialNumber);
    	//查询该订单编号条数
    	int count = payStatementsMapper.selectCountByExample(payStatements);
    	//若存在数据则返回true
    	if(count > 0){
    		flag = true;
    	}
		return flag;
	}
	
	/**
	 * 根据支付流水查询支付信息
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/06/21
	 */
	@Override
	public PayStatements searchBySerialNumber(String serialNumber) {
		
		//根据支付单号查询
		//根据支付单号查询
		Example example = new Example(PayStatements.class);
				
		Example.Criteria criteria=example.createCriteria();
				
		criteria.andEqualTo("serialNumber",serialNumber);
		
		//查询数据
		List<PayStatements> list = payStatementsMapper.selectByExample(example);
		
		if(null!=list&&1==list.size())
			return list.get(0);
		return null;
	}
	
	/**
	 * 更新支付通知推送状态
	 * 
	 * @param psVo
	 * @Author: Jack
	 * @Date: 2018/07/31
	 */
	@Override
	public void updatePushStateSuccess(PayStatementsVo psVo) {
		
		Example example = new Example(PayStatements.class);
		
		Example.Criteria criteria=example.createCriteria();
				
		criteria.andEqualTo("serialNumber",psVo.getSerialNumber());
		
		payStatementsMapper.updateByExampleSelective(psVo.voTransEntity(psVo), example);
	}
	
	/**
	 * 根据 支付单号查询支付流水集合
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/05
	 */
	@Override
	public List<PayStatements> searchByPayCode(String payCode) {
		
		//根据支付单号查询
		Example example = new Example(PayStatements.class);
		
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("payCode",payCode);
		
		//查询数据
		List<PayStatements> list = payStatementsMapper.selectByExample(example);
		
		return list;
	}

	
	/**
	 * 支付流水分页查询
	 * 
	 * @param payStatements
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @Author: Jack
	 * @Date: 2018/06/21
	 */
	@Override
	public PageResult searchPage(PayStatements payStatements,Integer currentPage,Integer pageSize) {
		
		//封装返回数据
		PageResult page = new PageResult();
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		
		Example example = new Example(PayStatements.class);
		
		Example.Criteria criteria=example.createCriteria();
		
		if(StringUtils.isNotBlank(payStatements.getPayCode())){
			criteria.andEqualTo("payCode",payStatements.getPayCode());
		}
		//查询总条数
		int count = payStatementsMapper.selectCountByExample(example);
		List<PayStatements> list = payStatementsMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getStartRecord(),page.getPageSize()));
		
    	page.setTotalRecord(count);
    	page.setPageResult(list);
    	
		return page;
	}
	
	/**
	 * 查询推送失败支付流水集合
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/08/10
	 */
	@Override
	public List<PayStatements> searchByPushError() {
		
		//根据支付单号查询
		Example example = new Example(PayStatements.class);
		
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("pushStatus",CommonsPayTypeConstants.PAY_RS_PUSH_STATUS_ERROR);
		
		//查询数据
		List<PayStatements> list = payStatementsMapper.selectByExample(example);
		
		return list;
	}
}
