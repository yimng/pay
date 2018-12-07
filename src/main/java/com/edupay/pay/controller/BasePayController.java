package com.edupay.pay.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.edupay.app.service.AppService;
import com.edupay.commons.api.controller.ApiCommonController;
import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.DataTypeUtil;
import com.edupay.commons.utils.PropertiesUtil;
import com.edupay.pay.service.TerminalService;
import com.edupay.pay.vo.ApplicationPayRecordVo;
import com.edupay.pay.vo.PayRecordVo;
import com.edupay.pay.vo.TerminalVo;

/**
 * 终端设备校验
 *
 * Title: TerminalController.java
 * @author: Jack
 * @create: 2018-07-08
 *
 */
@Controller
public class BasePayController extends ApiCommonController{
	
	@Autowired
	private TerminalService terminalService;
	@Autowired
	private AppService appService;
	
	/**
	 * 校验AppToken是否存在
	 * 
	 * @param payToken
	 * @return
	 * @Author: Jack
	 * @Date: 2018/08/27
	 */
	public Integer getAppId(String payToken){
		Integer appId=appService.getAppIdByToken(payToken);
		if(appId==null)
		{
			return null;
		}
		return appId;
	}
		
	/**
	 * 解析请求，做终端信息校验
	 * 
	 * @param context
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/10
	 */
	public TerminalVo getTerminalVo(String context,Integer payType){
		return terminalService.getTerminalVo(context, payType);
	}
	
	/**
	 * 生成返回报文
	 * 
	 * @param tVo
	 * @param type
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/10
	 */
	public Map<String,String> getRsXmlHeader(TerminalVo tVo,Integer type)
	{
		Map<String,String> cMap=new HashMap<String,String>();
		//版本
		cMap.put("version", tVo.getVersion());
		//请求类型
		cMap.put("transtype",tVo.getTransType());
		//POS编号
		cMap.put("termid",tVo.getPosCode());
		//员工编号
		cMap.put("employno",tVo.getLoginName());
		//商户号
		cMap.put("shopid",tVo.getShopId());
		//请求时间
		cMap.put("request_time",tVo.getRequestTime());
		//响应时间
		cMap.put("response_time",tVo.getResponseTime());
		//mac
		cMap.put("mac",tVo.getMac());
		//校验结果码
		String msg="";
		if(1==tVo.getCheckFlag()){
			cMap.put("response_code", "00");//校验成功
			if(1==type)
				msg="校验成功";
			if(2==type)
				msg="交易成功";
		}
		else{
			cMap.put("response_code", "01");//校验失败
			if(1==type)
				msg="校验失败";
			if(2==type)
				msg="交易失败";
		}
		//校验结果信息
		cMap.put("response_msg", msg);
		
		
		return cMap;
	}
    
	
	
	/**
	 * 做支付信息校验及信息拼装
	 * 
	 * @param prVo
	 * @param cmap
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/10
	 */
	public Map<String,String> checkPayInfo(TerminalVo tVo,PayRecordVo prVo,Map<String,String> cmap)
	{
		//默认校验通过
		cmap.put("flag", "0");
		//校验支付数据,支付单号不存在和支付金额大于支付单待支付金额则返回 0-数据错误，不允许支付
		if(null==prVo||0>prVo.getPayMoney().compareTo(BigDecimal.ZERO)){
			cmap.put("status", "0");
			//校验不通过
			cmap.put("flag", "1");
			cmap.put("response_code", "01");
			cmap.put("response_msg", "交易失败");
			return cmap;
		}
		//加载商户号配置信息-中博诚通，中博教育
		Properties properties=PropertiesUtil.loadProperties("pospay-config.properties");
		String zbedu=properties.getProperty("shopid.zbedu");
		String zbct=properties.getProperty("shopid.zbct");
		//是否拆分订单
		List<ApplicationPayRecordVo> aprLst=new ArrayList<ApplicationPayRecordVo>();
		if(StringUtils.isNotBlank(prVo.getApplicationParameter())){
			DataTypeUtil.jsonConvertBeanList(aprLst, prVo.getApplicationParameter(), ApplicationPayRecordVo.class);
		}
		ApplicationPayRecordVo aprVo=aprLst.get(0);
		//校验商户号
		if(CommonsPayTypeConstants.PAY_ZB_PAYEE_TYPE_ZBCT.equals(aprVo.getZbPayeeType())){
//			if(!zbct.equals(cmap.get("shopid"))){
			if(-1==zbct.indexOf(cmap.get("shopid"))){
				cmap.put("status", "1");
				//校验不通过
				cmap.put("flag", "1");
			}
		}
		if(CommonsPayTypeConstants.PAY_ZB_PAYEE_TYPE_ZBEDU.equals(aprVo.getZbPayeeType())){
//			if(!zbedu.equals(cmap.get("shopid"))){
			if(-1==zbedu.indexOf(cmap.get("shopid"))){
				cmap.put("status", "1");
				//校验不通过
				cmap.put("flag", "1");
			}
		}
		//校验分部
		//本部支付POS机分部与订单分部必须一致
		if(CommonsPayTypeConstants.PAY_TYPE_POS_LOCAL.equals(prVo.getPayType())){
			if(!aprVo.getBranchId().equals(tVo.getBranchId())){
				cmap.put("status", "1");
				//校验不通过
				cmap.put("flag", "1");
			}
		}
		//跨区支付允许POS机分部与订单分部不同
//		if(CommonsPayTypeConstants.PAY_TYPE_POS_OTHER.equals(prVo.getPayType())){
//				
//		}
			
		if("1".equals(cmap.get("flag"))){
			cmap.put("response_code", "01");
			cmap.put("response_msg", "交易失败");
		}
		else{//交易成功填充业务数据
			//订单是否支持拆分支付
			if(0==aprVo.getIsSplit()){
				cmap.put("status", "3");//不允许拆单支付
			}
			if(1==aprVo.getIsSplit()){
				cmap.put("status", "2");//允许拆单支付
			}
			//金额
			//订单总金额
			cmap.put("orderamt", prVo.getMoney().toString());
			//订单待支付金额(本次支付后还需支付金额)
			cmap.put("cod", prVo.getPayMoney().toString());
		}	
		return cmap;
	}
}
