package com.edupay.commons.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillUtil {
	
	public static List<Map<String, Object>> getTxtBill(String fileUrl){
		List<Map<String, Object>> list = new ArrayList<>();
		String fileContent = "";
		FileInputStream fileInputStream = null;
		InputStreamReader read = null;
		BufferedReader reader = null;
		try {
			File f = new File(fileUrl);
			if(f.isFile()&&f.exists()){
				fileInputStream = new FileInputStream(f);
				read = new InputStreamReader(fileInputStream,"GBK");
				reader=new BufferedReader(read);
				String line;       
	            while ( (line = reader.readLine())!=null ){
	                fileContent += line+";";
	            }
	            read.close();
	            //先按行;分组
	            String[] zu = fileContent.split(";");
	            //游标0，对账文件第一行是title，需要从第二行开始读取有效对账数据
	            for (int i = 1; i < zu.length; i++) {
					//再按逗号,分单值
	            	if(zu[i]!=null && !"".equals(zu[i])){
			            String[] vals = zu[i].split(",",-1);
			            Map<String, Object> map = new HashMap<>();
			            map = array2Map(vals);
			            list.add(map);
	            	}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {}
			}
			if(read!=null) {
				try {
					read.close();
				} catch (IOException e) {}
			}
			if(fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {}
			}
		}
		return list;
	}
	
	public static Map<String, Object> array2Map(String[] vals){
		Map<String, Object> map = new HashMap<>();
		map.put("ywly", vals[0]);   //业务来源,
        map.put("jyrq", vals[1]);   //交易日期,
        map.put("jysj", vals[2]);   //交易时间,		//交易日期+交易时间   tracetime 支付时间
        map.put("ydh", vals[3]);    //运单号,			//支付单号    orderno
        map.put("ydlx", vals[4]); //运单类型,
        map.put("jyje", vals[5]); //交易金额,			//待收款金额 cod 
        map.put("hk", vals[6]); //货款,
        map.put("yf", vals[7]); //运费,
        map.put("zffs", vals[8]); //支付方式,
        map.put("qsrq", vals[9]); //清算日期,
        map.put("qsje", vals[10]); //清算金额,
        map.put("sksxf", vals[11]); //刷卡手续费,
        map.put("kh", vals[12]); //卡号,
        map.put("fkhmc", vals[13]); //发卡行名称,
        map.put("kz", vals[14]); //卡种,
        map.put("pzh", vals[15]); //凭证号,
        map.put("jsckh", vals[16]); //检索参考号,		//银行系统参考号 banktrace
        map.put("jszdh", vals[17]); //清算终端号,
        map.put("slzdh", vals[18]); //受理终端号,		//终端号 termid
        map.put("jylx", vals[19]); //交易类型,
        map.put("wlshh", vals[20]); //物流商户号,		//商户号shopid
        map.put("psshmc", vals[21]); //配送商户名称,
        map.put("psshh", vals[22]); //配送商户号,
        map.put("ddh", vals[23]); //订单号,
        map.put("zdh", vals[24]); //账单号,
        map.put("sftb", vals[25]); //是否投保,
        map.put("wdbh", vals[26]); //网点编号,
        map.put("wdmc", vals[27]); //网点名称,
        map.put("zydsbm", vals[28]); //自有电商编码,
        map.put("zydsmc", vals[29]); //自有电商名称,
        map.put("bz", vals[30]); //备注,
        map.put("ygh", vals[31]); //员工号
        return map;
	}
	
	public static void main(String[] args) {
//		List<Map<String, Object>> list=  getTxtBill("C:/20180712.txt");
//		for (Map<String, Object> map : list) {
//			System.out.println(map.get("ydh"));
//		}
//		Date checkBillDate=DateUtil.getAnyDateOfNumDays(new Date(),-1);
//		try{
//		String checkBillDates=DateUtil.dateToString(checkBillDate, "YYYY-MM-dd");
//		StringBuffer checkBillFileName=new StringBuffer();
//		checkBillFileName.append("checkbill_");
//		checkBillFileName.append(checkBillDates);
//		checkBillFileName.append(".txt");
//		System.out.println(checkBillFileName.toString());
//		
//		}catch(Exception e)
//		{e.printStackTrace();}
		System.out.println(0<=new BigDecimal("0.00").compareTo(BigDecimal.ZERO));
		System.out.println(0<new BigDecimal("0.0").compareTo(BigDecimal.ZERO));
		System.out.println(0<new BigDecimal("-0.01").compareTo(new BigDecimal("-0.1")));
		System.out.println(0<new BigDecimal("0.01").compareTo(BigDecimal.ZERO));
	}
}
