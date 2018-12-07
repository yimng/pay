package com.edupay.pay.service.impl;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.edu.dubbo.pay.service.PayBaseInfoService;
import com.edupay.commons.constants.CommonsPayTypeConstants;
import com.edupay.commons.task.MainTask;
import com.edupay.commons.utils.BillUtil;
import com.edupay.commons.utils.DataTypeUtil;
import com.edupay.commons.utils.DateUtil;
import com.edupay.commons.utils.PropertiesUtil;
import com.edupay.commons.utils.SftpUtil;
import com.edupay.pay.service.CheckBillService;
import com.edupay.pay.service.PayStatementsService;
import com.edupay.pay.vo.PayStatementsVo;
import com.edupay.pay.vo.TerminalVo;

/**
 * POS机支付对账 业务实现类
 *
 * Title: CheckBillServiceImpl.java
 * @author: Jack
 * @create: 2018-07-31
 *
 */
@Component
@Service
public class CheckBillServiceImpl implements CheckBillService {
	
	@Autowired
	private PayStatementsService payStatementsSerivce;
	@Autowired
	private PayBaseInfoService payBaseInfoService;
	
	private static Properties properties=PropertiesUtil.loadProperties("pospay-config.properties");
	
	/**
	 * 支付对账 T+1
	 *
	 * Title PayStatementsServiceImpl.java
	 * @author Jack
	 * @date Jul 18, 2018
	 */
	public void checkBill(){
		try{
			URL url = MainTask.class.getResource("");
//			String path=url.getPath();
//			//TODO 调试结束后删除输出
//			System.out.println(path);
//			path=path.substring(0,path.indexOf("WEB-INF"));
//			path+="/upload"+"/checkbill/";
			String path=properties.getProperty("bill.filePath");
			//TODO 调试结束后删除输出
			System.out.println(path);
			//对账文件本地存储路径
			String savePath=path;
			//获取前一天的对账文件
			String checkBillFileName=getCheckBillFileName(null,1);
			//TODO 调试结束后删除输出
			System.out.println(checkBillFileName);
			//从支付商SFTP下载对账文件
			SftpUtil.downloadFile(checkBillFileName,savePath+checkBillFileName);
			//解析对账文件
			List<Map<String, Object>> list=  BillUtil.getTxtBill(savePath+checkBillFileName);
			//循环对账文件插入未正常接收的支付记录
			for (Map<String, Object> map : list) {
				PayStatementsVo payStatementsVo=initEntity(map);
				payStatementsSerivce.add(payStatementsVo,CommonsPayTypeConstants.PAY_TYPE_POS_LOCAL);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 修复补录下载对账文件对账
	 * @author Jack
	 * @param beginDate
	 * @data  Oct 11, 2018
	 */
	public void repairCheckBill(String beginDate){
		try{
			String path=properties.getProperty("bill.filePath");
			//TODO 调试结束后删除输出
			System.out.println(path);
			//对账文件本地存储路径
			String savePath=path;
			//获取需要遍历的对账文件天数
			int days=0;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			days=DateUtil.getDistanceDays(sdf.parse(beginDate));
			//循环遍历下载对账文件
			for(int i=1;i<=days;i++){
				String checkBillFileName=getCheckBillFileName(null,i);
				//TODO 调试结束后删除输出
				System.out.println("repaireStatements: "+checkBillFileName);
				//从支付商SFTP下载对账文件
				SftpUtil.downloadFile(checkBillFileName,savePath+checkBillFileName);
				//解析对账文件
				List<Map<String, Object>> list=  BillUtil.getTxtBill(savePath+checkBillFileName);
				//循环对账文件插入未正常接收的支付记录
				for (Map<String, Object> map : list) {
					PayStatementsVo payStatementsVo=initEntity(map);
					payStatementsSerivce.add(payStatementsVo,CommonsPayTypeConstants.PAY_TYPE_POS_LOCAL);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//获取对账文件名称 T+1
	private String getCheckBillFileName(Integer type,Integer days)
	{
		//默认对账文件名称-sftp无则不下载
		String defaultBillFileName="checkbill_1949-01-01.txt";
		try{
			//TODO 预留type类型给未来拓展不同支付商对账文件间隔天数不同增加判断用
//			if(==type)
			Date checkBillDate=DateUtil.getAnyDateOfNumDays(new Date(),0-days);
			
			//对账文件日期
			String checkBillDates=DateUtil.dateToString(checkBillDate, "YYYY-MM-dd");
			StringBuffer checkBillFileName=new StringBuffer();
			//对账文件头
			checkBillFileName.append(properties.getProperty("bill.fileNameHead"));
			checkBillFileName.append(checkBillDates);
			checkBillFileName.append(".");
			//对账文件类型
			checkBillFileName.append(properties.getProperty("bill.filetype"));
			defaultBillFileName=checkBillFileName.toString();
			//TODO 调试结束后删除输出
			System.out.println(defaultBillFileName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultBillFileName;	
	}
	
	private PayStatementsVo initEntity(Map<String,Object> cMap)
	{
		if(null==cMap)
			return null;
		PayStatementsVo payStatementsVo=new PayStatementsVo();
		try{
			TerminalVo terminalVo=new TerminalVo();
			//员工号
			payStatementsVo.setEmployno(DataTypeUtil.getMapString(cMap, "ygh"));
			terminalVo.setLoginName(payStatementsVo.getEmployno());
			//受理终端号
			payStatementsVo.setPosCode(DataTypeUtil.getMapString(cMap, "slzdh"));
			terminalVo.setPosCode(payStatementsVo.getPosCode());
			String posBranchId=payBaseInfoService.getPosPertainDivision(terminalVo.getPosCode()).getData();
			terminalVo.setBranchId(posBranchId);
			//商户号
			payStatementsVo.setShopId(DataTypeUtil.getMapString(cMap, "wlshh"));
			terminalVo.setShopId(payStatementsVo.getShopId());
			//支付金额
			payStatementsVo.setMoney(new BigDecimal(DataTypeUtil.getMapString(cMap, "jyje")));
			//支付单号
			payStatementsVo.setPayCode(DataTypeUtil.getMapString(cMap, "ydh"));
			//当拆分支付时屏蔽银商产生的子单号
			if(0<payStatementsVo.getPayCode().indexOf("_")){
				payStatementsVo.setPayCode(payStatementsVo.getPayCode().substring(0,payStatementsVo.getPayCode().indexOf("_")));
			}
			//支付时间
			payStatementsVo.setPayDate(DateUtil.strToDate(DataTypeUtil.getMapString(cMap, "jyrq")+" "+DataTypeUtil.getMapString(cMap, "jysj"),"yyyy-MM-dd hh:mm:ss"));
			//银行流水
			payStatementsVo.setSerialNumber(DataTypeUtil.getMapString(cMap, "jsckh"));
			//默认成功
			payStatementsVo.setStatus(1);
			//初始化POS信息
			payStatementsVo.setTerminalVo(terminalVo);
			
			if("快速签单".equals(cMap.get("ydlx"))){
				payStatementsVo.setPayTypes("P033");
	        }
	        if("拆分支付签单".equals(cMap.get("ydlx"))){
	        	payStatementsVo.setPayTypes("P007");
	        }
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return payStatementsVo;
	}
}
