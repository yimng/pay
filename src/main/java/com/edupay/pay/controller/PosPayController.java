package com.edupay.pay.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.DataTypeUtil;
import com.edupay.commons.utils.XmlUtil;
import com.edupay.pay.service.PayRecordService;
import com.edupay.pay.vo.PayRecordVo;
import com.edupay.pay.vo.TerminalVo;

/**
 * 支付单
 *
 * Title: PayRecordController.java
 * @author: Jack
 * @create: 2018-07-08
 *
 */
@Controller
@RequestMapping("/edupay/payRecord")
public class PosPayController extends BasePayController{
	
	@Autowired
	private PayRecordService payRecordService;
	
	private static final Logger LOG = Logger.getLogger(PosPayController.class.getName());
	
	/**
	 * 快捷支付请求（三方查询支付系统）-P034
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/searchPayInfoForQuick",method = RequestMethod.POST)
    @ResponseBody
    public String searchPayInfoForQuick(String context){
		//打印请求报文日志
		LOG.info("searchPayInfoForQuick:"+context);
		System.out.println("@searchPayInfoForQuick:"+context);
		LOG.info("searchPayInfoForQuick:"+"beginTime "+new Date());
		System.out.println("@searchPayInfoForQuick:"+"beginTime "+new Date());
    	//TODO 校验token 查询pos管理和员工管理是否符合同一分部条件
		TerminalVo tVo=getTerminalVo(context,CommonsPayTypeConstants.PAY_TYPE_POS_TYPE_QUICK);
		PayRecordVo payRecordVo=new PayRecordVo();
		
		String rs=null;
		//TODO 解析报文获取金额数据
		if(1==tVo.getCheckFlag()){
			Map<String,Object> cMap=XmlUtil.getStringMsg(context);
			//支付金额
			payRecordVo.setApplicationId(1);//默认教务系统
			payRecordVo.setMoney(new BigDecimal(DataTypeUtil.getMapString(cMap, "cod")));
			payRecordVo.setPayCode(DataTypeUtil.getMapString(cMap, "orderno"));
			
			rs=payRecordService.add(payRecordVo.voTransEntity(payRecordVo));
			payRecordVo.setPayCode(rs);
		}
		rs=getRsXML(tVo,payRecordVo,CommonsPayTypeConstants.PAY_POS_TYPE_QUICK_CHECK);
		LOG.info("searchPayInfoForQuick:"+"endTime "+new Date());
		System.out.println("@searchPayInfoForQuick:"+"endTime "+new Date());
		return rs;
	}
	
	/**
	 * 订单支付请求（三方查询支付系统）-P074
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/searchPayInfoForOrder",method = RequestMethod.POST)
    @ResponseBody
    public String searchPayInfoForOrder(String context){
		//打印请求报文日志
		LOG.info("searchPayInfoForOrder:"+context);
		System.out.println("@searchPayInfoForOrder:"+context);
		LOG.info("searchPayInfoForOrder:"+"beginTime "+new Date());
		System.out.println("@searchPayInfoForOrder:"+"beginTime "+new Date());
		//TODO 校验token 查询pos管理和员工管理是否符合同一分部条件
		TerminalVo tVo=getTerminalVo(context,CommonsPayTypeConstants.PAY_TYPE_POS_TYPE_ORDER);
		PayRecordVo prVo=null;
		if(1==tVo.getCheckFlag()){
			prVo=payRecordService.searchByPayCode(getXmlPayCode(context));
		}
		String rs=getRsXML(tVo,prVo,CommonsPayTypeConstants.PAY_POS_TYPE_ORDER_CHECK);
		LOG.info("searchPayInfoForOrder:"+"endTime "+new Date());
		System.out.println("@searchPayInfoForOrder:"+"endTime "+new Date());
		return rs;
	}
	
	private String getRsXML(TerminalVo tVo,PayRecordVo prVo,String type)
	{
		String rsXML=null;
		Map<String,String> cmap=getRsXmlHeader(tVo,1);
		//快速支付-校验
		if(CommonsPayTypeConstants.PAY_POS_TYPE_QUICK_CHECK.equals(type)){
			//返回支付单号
			if(StringUtils.isNotBlank(prVo.getPayCode())){
				cmap.put("orderno", prVo.getPayCode());
			}
			if(null!=prVo.getMoney()){
				cmap.put("cod", prVo.getMoney().toString());
			}
			rsXML= XmlUtil.createP034Msg(cmap, null, null);
		}
		//订单支付-校验-返回支付信息
		if(CommonsPayTypeConstants.PAY_POS_TYPE_ORDER_CHECK.equals(type)){
			//校验支付信息
			if(1==tVo.getCheckFlag()){
				cmap=checkPayInfo(tVo,prVo,cmap);
			}
			rsXML= XmlUtil.createP074Msg(cmap, null, null);
		}
		return rsXML;
	}
	
	private String getXmlPayCode(String context)
	{
		if(StringUtils.isBlank(context))
			return null;
		//TODO 解析报文获取数据内容
		Map<String,Object> cMap=XmlUtil.getStringMsg(context);
		//支付单号
		return DataTypeUtil.getMapString(cMap, "orderno");
	}
}
