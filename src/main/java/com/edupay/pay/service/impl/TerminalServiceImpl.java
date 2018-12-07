package com.edupay.pay.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.edu.dubbo.pay.service.PayBaseInfoService;
import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.utils.DataTypeUtil;
import com.edupay.commons.utils.XmlUtil;
import com.edupay.pay.service.TerminalService;
import com.edupay.pay.vo.TerminalVo;

/**
 * POS机终端 业务实现类
 *
 * Title: TerminalServiceImpl.java
 * @author: Jack
 * @create: 2018-08-10
 *
 */
@Component
@Service
public class TerminalServiceImpl implements TerminalService {
	
	@Autowired
	private PayBaseInfoService payBaseInfoService;
	
	/**
	 * 解析请求，做终端信息校验
	 * 
	 * @param context
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/10
	 */
	public TerminalVo getTerminalVo(String context,Integer payType)
	{
		if(StringUtils.isBlank(context))
			return null;
		TerminalVo tVo=new TerminalVo();
		//TODO 解析报文获取数据内容
		Map<String,Object> cMap=XmlUtil.getStringMsg(context);
		//版本
		tVo.setVersion(DataTypeUtil.getMapString(cMap, "version"));
		//请求类型
		tVo.setTransType(DataTypeUtil.getMapString(cMap, "transtype"));
		//POS编号
		tVo.setPosCode(DataTypeUtil.getMapString(cMap, "termid"));
		//员工编号
		String loginName=DataTypeUtil.getMapString(cMap,"employno");
		if(StringUtils.isNotBlank(loginName)){
			Pattern pattern = Pattern.compile("[0-9]*");
			//输入项为纯数字则增加前缀
			if(pattern.matcher(loginName).matches()){
				loginName="ZBG"+loginName;
			}
			tVo.setLoginName(loginName);
		}
		//商户号
		tVo.setShopId(DataTypeUtil.getMapString(cMap,"shopid"));
		//请求时间
		tVo.setRequestTime(DataTypeUtil.getMapString(cMap, "request_time"));
		//mac
		tVo.setMac(DataTypeUtil.getMapString(cMap, "mac"));
		
		int checkFlag=0;
		//获取POS机隶属分部ID
		String posBranchId=payBaseInfoService.getPosPertainDivision(tVo.getPosCode()).getData();
		tVo.setBranchId(posBranchId);
		if(StringUtils.isNotBlank(posBranchId)){
			//快捷支付需要校验员工号
			if(CommonsPayTypeConstants.PAY_TYPE_POS_TYPE_QUICK.equals(payType)){
				//获取员工号隶属分部ID集
				List<String> userBranchIdLst=payBaseInfoService.getDivisionByUserId(tVo.getLoginName()).getData();
				if(null!=userBranchIdLst&&0<userBranchIdLst.size()){
					for(String userBranchId:userBranchIdLst){
						if(userBranchId.equals(posBranchId)){
							checkFlag=1;
						}
					}
				}
			}
			//订单支付无需校验员工号
			if(CommonsPayTypeConstants.PAY_TYPE_POS_TYPE_ORDER.equals(payType)){
				checkFlag=1;
			}
		}
		tVo.setCheckFlag(checkFlag);
//		tVo.setCheckFlag(1);//假设校验通过
		return tVo;
	}
}
