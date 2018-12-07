package com.edupay.pay.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.DataTypeUtil;
import com.edupay.commons.utils.DateUtil;
import com.edupay.commons.utils.XmlUtil;
import com.edupay.pay.service.CheckBillService;
import com.edupay.pay.service.PayRecordService;
import com.edupay.pay.service.PayStatementsService;
import com.edupay.pay.service.TerminalService;
import com.edupay.pay.vo.PayRecordVo;
import com.edupay.pay.vo.PayStatementsVo;
import com.edupay.pay.vo.TerminalVo;

/**
 * 支付流水
 *
 * Title: PayStatetementsController.java
 * @author: Jack
 * @create: 2018-07-08
 *
 */
@Controller
@RequestMapping("/edupay/payRecord")
public class PayStatementsController extends BasePayController{
	
	@Autowired
	private PayStatementsService payStatementsService;
	@Autowired
	private PayRecordService payRecordSerivce;
	@Autowired
	private TerminalService terminalService;
	@Autowired
	private CheckBillService checkBillService;
	
	private static final Logger LOG = Logger.getLogger(PayStatementsController.class.getName());
	
	/**
	 * 接收支付流水（三方支付商请求支付系统发送支付通知）-订单支付
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/recivePayResultForOrder",method = RequestMethod.POST)
    @ResponseBody
    public String requestPay(String context){
		//打印请求报文日志
		LOG.info("recivePayResultForOrder:"+context);
		System.out.println("@recivePayResultForOrder:"+context);
		LOG.info("recivePayResultForOrder:"+"beginTime "+new Date());
		System.out.println("@recivePayResultForOrder:"+"beginTime "+new Date());
		//TODO 校验token 查询pos管理和员工管理是否符合同一分部条件
		TerminalVo tVo=terminalService.getTerminalVo(context,CommonsPayTypeConstants.PAY_TYPE_POS_TYPE_ORDER);
		Map<String,String> cmap=getRsXmlHeader(tVo,2);
		//当终端信息校验成功后，校验支付信息
		if(1==tVo.getCheckFlag()){
			//TODO 初始化支付通知信息
			PayStatementsVo payStatementsVo=initEntity(context,2);
			payStatementsVo.setTerminalVo(tVo);
			PayRecordVo prVo=payStatementsVo.getPayRecordVo();
			//订单待支付金额减去本次支付金额(校验本次支付金额是否合法-小于等于待支付金额)
			if(null!=prVo){
				BigDecimal toPayMoney=prVo.getPayMoney().subtract(payStatementsVo.getMoney());
				prVo.setPayMoney(toPayMoney);
			}
			cmap=checkPayInfo(tVo,prVo,cmap);
			
			//当校验通过则存储入库并更新数据
			if("0".equals(cmap.get("flag"))){
				payStatementsVo.setRequestContent(context);
				String serialNumber=payStatementsService.add(payStatementsVo,CommonsPayTypeConstants.PAY_TYPE_POS_LOCAL);
				if(StringUtils.isBlank(serialNumber))
				{
					cmap.put("status", "0");
					//校验不通过
					cmap.put("flag", "1");
					cmap.put("response_code", "01");
					cmap.put("response_msg", "交易失败");
				}
			}
			//重新查询待支付金额
			prVo=payRecordSerivce.searchByPayCode(payStatementsVo.getPayCode());
			cmap.put("cod", prVo.getPayMoney().toString());
		}
		String rs=getRsXML(cmap,CommonsPayTypeConstants.PAY_POS_TYPE_ORDER_PAY);
		LOG.info("searchPayInfoForOrder:"+"endTime "+new Date());
		System.out.println("@searchPayInfoForOrder:"+"endTime "+new Date());
		return rs;
	}
	
	/**
	 * 接收支付流水（三方支付商请求支付系统发送支付通知）-快捷支付 P033
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/recivePayResultForQuick",method = RequestMethod.POST)
    @ResponseBody
    public String recivePayResultForQuick(String context){
		//打印请求报文日志
		LOG.info("recivePayResultForQuick:"+context);
		System.out.println("@recivePayResultForQuick:"+context);
		LOG.info("recivePayResultForQuick:"+"beginTime "+new Date());
		System.out.println("@recivePayResultForQuick:"+"beginTime "+new Date());
		//TODO 校验token 查询pos管理和员工管理是否符合同一分部条件
		TerminalVo tVo=terminalService.getTerminalVo(context,CommonsPayTypeConstants.PAY_TYPE_POS_TYPE_QUICK);
		Map<String,String> cmap=getRsXmlHeader(tVo,2);
		String rs=null;
		//当终端信息校验成功后，校验支付信息
		if(1==tVo.getCheckFlag()){			
			//TODO 初始化支付通知信息
			PayStatementsVo payStatementsVo=initEntity(context,1);
			payStatementsVo.setTerminalVo(tVo);
			payStatementsVo.setRequestContent(context);
			String serialNumber=payStatementsService.add(payStatementsVo,CommonsPayTypeConstants.PAY_TYPE_POS_LOCAL);
			cmap.put("orderno", payStatementsVo.getPayCode());
			cmap.put("cod", payStatementsVo.getMoney().toString());
			if(StringUtils.isBlank(serialNumber))
			{
				cmap.put("status", "0");
				//校验不通过
				cmap.put("flag", "1");
				cmap.put("response_code", "01");
				cmap.put("response_msg", "交易失败");
			}
			else{
				cmap.put("cod", BigDecimal.ZERO.toString());
			}
		}
		rs=getRsXML(cmap,CommonsPayTypeConstants.PAY_POS_TYPE_QUICK_PAY);
		LOG.info("recivePayResultForQuick:"+"endTime "+new Date());
		System.out.println("@recivePayResultForQuick:"+"endTime "+new Date());
		return rs;
	}
	
	/**
	 * 修复补录对账文件（因配置或网络导致对账文件下载失败时使用
	 * @param token
	 * @param beginDate
	 * @return
	 */
	@RequestMapping(value = "/repairPayStatements",method = RequestMethod.POST)
    @ResponseBody
    public String repairPayStatements(String token,String beginDate){
		
		if(StringUtils.isBlank(beginDate)){
			return "参数错误";
		}
		if(!"Aisue@sl2384!sd".equals(token)){
			return "无权操作";
		}
		
		checkBillService.repairCheckBill(beginDate);;
		return "success";
	}
	
	private PayStatementsVo initEntity(String context,Integer type)
	{
		if(StringUtils.isBlank(context))
			return null;
		PayStatementsVo payStatementsVo=new PayStatementsVo();
		//TODO 解析报文获取数据内容
		Map<String,Object> cMap=XmlUtil.getStringMsg(context);
		//支付金额
		payStatementsVo.setMoney(new BigDecimal(DataTypeUtil.getMapString(cMap, "cod")));
		//支付银行卡
		//TODO 待确认 中博教育和中博诚通 对应值
//		payStatements.setPayAccount(DataTypeUtil.getMapString(cMap, "cod"));
		//支付单号
		payStatementsVo.setPayCode(DataTypeUtil.getMapString(cMap, "orderno"));
		PayRecordVo payRecordVo=payRecordSerivce.searchByPayCode(payStatementsVo.getPayCode());
		if(null!=payRecordVo){
			payStatementsVo.setPayRecordId(payRecordVo.getPayRecordId());
			payStatementsVo.setPayRecordVo(payRecordVo);
		}
		//支付时间
		try{
			payStatementsVo.setPayDate(DateUtil.strToDate(DataTypeUtil.getMapString(cMap, "tracetime"),"yyyyMMddHHmmss"));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		//银行流水
		payStatementsVo.setSerialNumber(DataTypeUtil.getMapString(cMap, "banktrace"));
		//默认成功
		payStatementsVo.setStatus(1);
		//快捷支付
		if(1==type){
			
		}
		//订单支付
		if(2==type){
			
		}
		
		return payStatementsVo;
	}
	
//	/**
//	 * 查询支付流水-分页
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/searchPageForStatements",method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String, Object> searchPageForStatements(String payToken,PayStatements payStatements,Integer currentPage,Integer pageSize){
//		//TODO 校验token
//		if(null==getAppId(payToken)){
//			return null;
//		}
//		if(null==currentPage)
//			return error("参数错误",CommonsApiErrorCodeConstants.PARAM_ERROR);
//		if(null==pageSize)
//			return error("参数错误",CommonsApiErrorCodeConstants.PARAM_ERROR);
//		if(null==payStatements)
//			return error("参数错误",CommonsApiErrorCodeConstants.PARAM_ERROR);
//    	
//		PageResult page=payStatementsService.searchPage(payStatements,currentPage,pageSize);
//		
//		return successPage(page);
//	}
	
	//获取返回报文
	private String getRsXML(Map<String,String> cmap,String type)
	{
		String rsXML=null;
		
		//快速支付-校验
		if(CommonsPayTypeConstants.PAY_POS_TYPE_QUICK_PAY.equals(type)){
			rsXML= XmlUtil.createP033Msg(cmap, null, null);
		}
		//订单支付-校验-返回支付信息
		if(CommonsPayTypeConstants.PAY_POS_TYPE_ORDER_PAY.equals(type)){
			rsXML= XmlUtil.createP007Msg(cmap, null, null);
		}
		return rsXML;
	}
}
