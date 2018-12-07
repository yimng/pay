package com.edupay.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.edupay.pay.vo.PayStatementsVo;

public class XmlUtil {

	/**
	 * 读xml文件，解析
	 * @param fileUrl
	 * @return
	 * @author yangzhenlin
	 */
	public static Map<String, Object> getMsg(String fileUrl){
		Map<String, Object> map = new HashMap<>();
		FileOutputStream psFileOutputStream = null;
		PrintStream ps = null;
		FileInputStream documentFileInputStream = null;
		try {
			FileInputStream fis = new FileInputStream(fileUrl);
        	byte[] b = new byte[fis.available()];
        	fis.read(b);
        	String context = new String(b);
        	fis.close();
        	boolean flg = context.startsWith("context=");//判断字符串是否已百度二字开头
        	boolean flg2 = context.endsWith("11111111111111111111111111111111");//判断是否以指定内容结束
        	if(flg2){
        		context = context.substring(0, context.length()-32);//去尾  32个1
        	}
        	if(flg){
        		context = context.substring(8, context.length());//去头  context=
        	}
        	//重写文件
        	File file = new File(fileUrl);
        	psFileOutputStream = new FileOutputStream(file);
        	ps = new PrintStream(psFileOutputStream);
        	ps.print(context);//往文件里写入字符串 
        	//解析数据，封装map对象
        	SAXBuilder saxBuilder = new SAXBuilder();
        	documentFileInputStream = new FileInputStream(fileUrl);
        	Document document = saxBuilder.build(documentFileInputStream);
            //获取根节点
            Element rootElement = document.getRootElement();
            //获取根节点的子节点，返回子节点的数组
            List<Element> childList = rootElement.getChildren();
            for(Element childElement : childList){
            	//获取childElement的子节点
                List<Element> children = childElement.getChildren();
                for(Element child : children){
                	map.put(child.getName(), child.getValue());
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(documentFileInputStream!=null) {
				try {
					documentFileInputStream.close();
				} catch (IOException e) {}
			}
			if(ps!=null) {
				try {
					ps.close();
				} catch (Exception e) {}
			}
			if(psFileOutputStream!=null) {
				try {
					psFileOutputStream.close();
				} catch (IOException e) {}
			}
		}
		return map;
	}
	
	/**
	 * 解析xml结构类型String串
	 * @param msg
	 * @return
	 * @author yangzhenlin
	 */
	public static Map<String, Object> getStringMsg(String msg){
		Map<String, Object> map = new HashMap<>();
		try {
			msg = msg.replace("&lt;", '<' +""); 
			msg = msg.replace("&gt;", '>' +""); 
			msg = msg.replace("&quot;", '"' +""); 
			msg = msg.replace(" ", " ");
        	boolean flg = msg.startsWith("context=");//判断字符串是否已百度二字开头
        	boolean flg2 = msg.endsWith("11111111111111111111111111111111");//判断是否以指定内容结束
        	if(flg2){
        		msg = msg.substring(0, msg.length()-32);//去尾  32个1
        	}
        	if(flg){
        		msg = msg.substring(8, msg.length());//去头  context=
        	}
        	//String 转 Document对象
        	StringReader sr = new StringReader(msg);
    		InputSource is = new InputSource(sr);
    		Document document = (new SAXBuilder()).build(is);
        	//解析数据，封装map对象
            //获取根节点
            Element rootElement = document.getRootElement();
            //获取根节点的子节点，返回子节点的数组
            List<Element> childList = rootElement.getChildren();
            for(Element childElement : childList){
            	//获取childElement的子节点
                List<Element> children = childElement.getChildren();
                for(Element child : children){
                	map.put(child.getName(), child.getValue());
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 写
	 * @param values 
	 * @param fileUrl
	 * @author yangzhenlin
	 */
	public static void createMsg(String[] values, String fileUrl){
		Document document=new Document();
		//根节点
        Element root=new Element("transaction");
        //二级节点
        Element header = new Element("transaction_header");
        Element body = new Element("transaction_body");
        //三级节点
        Element headerChild_version = new Element("version");
        Element headerChild_transtype = new Element("transtype");
        Element headerChild_employno  = new Element("employno");
        Element headerChild_termid  = new Element("termid");
        Element headerChild_shopid  = new Element("shopid");
        Element headerChild_request_time  = new Element("request_time");
        Element headerChild_response_time  = new Element("response_time");
        Element headerChild_response_code  = new Element("response_code");
        Element headerChild_response_msg  = new Element("response_msg");
        Element headerChild_mac  = new Element("mac");
        Element bodyChild_orderno = new Element("orderno");
        Element bodyChild_cod = new Element("cod");
        //
        headerChild_version.setText(values[0]);
        headerChild_transtype.setText(values[1]);
        headerChild_employno.setText(values[2]);
        headerChild_termid.setText(values[3]);
        headerChild_shopid.setText(values[4]);
        headerChild_request_time.setText(values[5]);
        headerChild_response_time.setText(values[6]);
        headerChild_response_code.setText(values[7]);
        headerChild_response_msg.setText(values[8]);
        headerChild_mac.setText(values[9]);
        bodyChild_orderno.setText(values[10]);
        bodyChild_cod.setText(values[11]);
        //拼装
        header.addContent(headerChild_version);
        header.addContent(headerChild_transtype);
        header.addContent(headerChild_employno);
        header.addContent(headerChild_termid);
        header.addContent(headerChild_shopid);
        header.addContent(headerChild_request_time);
        header.addContent(headerChild_response_time);
        header.addContent(headerChild_response_code);
        header.addContent(headerChild_response_msg);
        header.addContent(headerChild_mac);
        body.addContent(bodyChild_orderno);
        body.addContent(bodyChild_cod);
        //
        root.addContent(header);
        root.addContent(body);
        document.setRootElement(root);
        //
        XMLOutputter outputter=new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());//设置文本格式
        File file = new File(fileUrl);
        FileOutputStream outFileOutputStream = null;
        FileInputStream fis = null;
        FileOutputStream psFileOutputStream = null;
        PrintStream ps = null;
        try {
        	outFileOutputStream = new FileOutputStream(file);
        	outputter.output(document, outFileOutputStream);//生成文件
        	//读文件
        	fis = new FileInputStream(fileUrl);
        	byte[] b=new byte[fis.available()];
        	fis.read(b);
        	String context = new String(b);
        	fis.close();
        	//重写文件
        	psFileOutputStream = new FileOutputStream(file);
            ps = new PrintStream(psFileOutputStream);
            context = "context="+context;
            ps.print(context);//往文件里写入字符串  
            ps.append("11111111111111111111111111111111");//在已有的基础上添加字符串
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ps!=null) {
				try {
					ps.close();
				} catch (Exception e) {}
			}
			if(psFileOutputStream!=null) {
				try {
					psFileOutputStream.close();
				} catch (IOException e) {}
			}
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {}
			}
			if(outFileOutputStream!=null) {
				try {
					outFileOutputStream.close();
				} catch (IOException e) {}
			}
		}
	}
	
	public static String createP007Msg(Map<String, String> map, String begin, String end){
		Document document=new Document();
		//根节点
        Element root=new Element("transaction");
        //二级节点
        Element header = new Element("transaction_header");
        Element body = new Element("transaction_body");
        //三级节点
        Element headerChild_version = new Element("version");             //
        Element headerChild_transtype = new Element("transtype");         //
        Element headerChild_employno  = new Element("employno");          //
        Element headerChild_shopid  = new Element("shopid");              //
        Element headerChild_termid  = new Element("termid");              //
        Element headerChild_response_time  = new Element("response_time");
        Element headerChild_response_code  = new Element("response_code");
        Element headerChild_response_msg  = new Element("response_msg");
        Element headerChild_mac  = new Element("mac");                    //
        //
        Element bodyChild_cod = new Element("cod");             //代收款金额
        //节点赋值
        headerChild_version.setText(map.get("version"));
        headerChild_transtype.setText(map.get("transtype"));
        headerChild_employno.setText(map.get("employno"));
        headerChild_shopid.setText(map.get("shopid"));
        headerChild_termid.setText(map.get("termid"));
        headerChild_response_time.setText(map.get("response_time"));
        headerChild_response_code.setText(map.get("response_code"));
        headerChild_response_msg.setText(map.get("response_msg"));
        headerChild_mac.setText(map.get("mac"));
        //
        bodyChild_cod.setText(map.get("cod"));
        //拼装
        header.addContent(headerChild_version);
        header.addContent(headerChild_transtype);
        header.addContent(headerChild_employno);
        header.addContent(headerChild_shopid);
        header.addContent(headerChild_termid);
        header.addContent(headerChild_response_time);
        header.addContent(headerChild_response_code);
        header.addContent(headerChild_response_msg);
        header.addContent(headerChild_mac);
        //
        body.addContent(bodyChild_cod);
        //
        root.addContent(header);
        root.addContent(body);
        document.setRootElement(root);
        //document对象转换String字符串
        String xmlStr = documentToString(document, begin, end);
        return xmlStr;
	}
	
	public static String createP074Msg(Map<String, String> map, String begin, String end){
		Document document=new Document();
		//根节点
        Element root=new Element("transaction");
        //二级节点
        Element header = new Element("transaction_header");
        Element body = new Element("transaction_body");
        //三级节点
        Element headerChild_version = new Element("version");             //
        Element headerChild_transtype = new Element("transtype");         //
        Element headerChild_employno  = new Element("employno");          //
        Element headerChild_shopid  = new Element("shopid");              //
        Element headerChild_termid  = new Element("termid");              //
        Element headerChild_response_time  = new Element("response_time");
        Element headerChild_response_code  = new Element("response_code");
        Element headerChild_response_msg  = new Element("response_msg");
        Element headerChild_mac  = new Element("mac");                    //
        //
        Element bodyChild_netcode = new Element("netcode");
        Element bodyChild_netname = new Element("netname");
        Element bodyChild_weight = new Element("weight");
        Element bodyChild_orderamt = new Element("orderamt");
        Element bodyChild_cod = new Element("cod");             //代收款金额
        Element bodyChild_goodscount = new Element("goodscount");
        Element bodyChild_address = new Element("address");
        Element bodyChild_people = new Element("people");
        Element bodyChild_peopletel = new Element("peopletel");
        Element bodyChild_status = new Element("status");
        Element bodyChild_memo = new Element("memo");
        Element bodyChild_dssn = new Element("dssn");
        Element bodyChild_dsname = new Element("dsname");
        Element bodyChild_dsorderno = new Element("dsorderno");
        Element bodyChild_dlvryno = new Element("dlvryno");
        Element bodyChild_buzitype = new Element("buzitype");
        Element bodyChild_fee = new Element("fee");
        //节点赋值
        headerChild_version.setText(map.get("version"));
        headerChild_transtype.setText(map.get("transtype"));
        headerChild_employno.setText(map.get("employno"));
        headerChild_shopid.setText(map.get("shopid"));
        headerChild_termid.setText(map.get("termid"));
        headerChild_mac.setText(map.get("mac"));
        headerChild_response_time.setText(map.get("response_time"));
        headerChild_response_code.setText(map.get("response_code"));
        headerChild_response_msg.setText(map.get("response_msg"));
        //
        bodyChild_netcode.setText(map.get("netcode"));
        bodyChild_netname.setText(map.get("netname"));
        bodyChild_weight.setText(map.get("weight"));
        bodyChild_orderamt.setText(map.get("orderamt"));   
        bodyChild_cod.setText(map.get("cod"));
        bodyChild_goodscount.setText(map.get("goodscount"));
        bodyChild_address.setText(map.get("address"));
        bodyChild_people.setText(map.get("people"));
        bodyChild_peopletel.setText(map.get("peopletel"));
        bodyChild_status.setText(map.get("status"));
        bodyChild_memo.setText(map.get("memo"));
        bodyChild_dssn.setText(map.get("dssn"));
        bodyChild_dsname.setText(map.get("dsname"));
        bodyChild_dsorderno.setText(map.get("dsorderno"));
        bodyChild_dlvryno.setText(map.get("dlvryno"));
        bodyChild_buzitype.setText(map.get("buzitype"));
        bodyChild_fee.setText(map.get("fee"));
        //拼装
        header.addContent(headerChild_version);
        header.addContent(headerChild_transtype);
        header.addContent(headerChild_employno);
        header.addContent(headerChild_shopid);
        header.addContent(headerChild_termid);
        header.addContent(headerChild_mac);
        header.addContent(headerChild_response_time);
        header.addContent(headerChild_response_code);
        header.addContent(headerChild_response_msg);
        //
        body.addContent(bodyChild_netcode);
        body.addContent(bodyChild_netname);
        body.addContent(bodyChild_weight);
        body.addContent(bodyChild_orderamt);   
        body.addContent(bodyChild_cod);
        body.addContent(bodyChild_goodscount);
        body.addContent(bodyChild_address);
        body.addContent(bodyChild_people);
        body.addContent(bodyChild_peopletel);
        body.addContent(bodyChild_status);
        body.addContent(bodyChild_memo);
        body.addContent(bodyChild_dssn);
        body.addContent(bodyChild_dsname);
        body.addContent(bodyChild_dsorderno);
        body.addContent(bodyChild_dlvryno);
        body.addContent(bodyChild_buzitype);
        body.addContent(bodyChild_fee);
        //
        root.addContent(header);
        root.addContent(body);
        document.setRootElement(root);
        //document对象转换String字符串
        String xmlStr = documentToString(document, begin, end);
        return xmlStr;
	}
	
	public static String createP033Msg(Map<String, String> map, String begin, String end){
		Document document=new Document();
		//根节点
        Element root=new Element("transaction");
        //二级节点
        Element header = new Element("transaction_header");
        Element body = new Element("transaction_body");
        //三级节点
        Element headerChild_version = new Element("version");             //
        Element headerChild_transtype = new Element("transtype");         //
        Element headerChild_employno  = new Element("employno");          //
        Element headerChild_shopid  = new Element("shopid");              //
        Element headerChild_termid  = new Element("termid");              //
//        Element headerChild_request_time  = new Element("request_time");  //
        Element headerChild_response_time  = new Element("response_time");
        Element headerChild_response_code  = new Element("response_code");
        Element headerChild_response_msg  = new Element("response_msg");
        Element headerChild_mac  = new Element("mac");                    //
        //
        Element bodyChild_orderno = new Element("orderno");     //运单号 
        Element bodyChild_cod = new Element("cod");             //代收款金额
//        Element bodyChild_payway = new Element("payway");       //代收款支付方式
//        Element bodyChild_banktrace = new Element("banktrace"); //银行系统参考号 
//        Element bodyChild_postrace = new Element("postrace");   //POS 机的流水号 
//        Element bodyChild_tracetime = new Element("tracetime"); //银行交易时间
//        Element bodyChild_cardid = new Element("cardid");       //银行卡号 
//        Element bodyChild_signflag = new Element("signflag");   //本人签收标记
//        Element bodyChild_signer = new Element("signer");       //签收人 
//        Element bodyChild_cardtype = new Element("cardtype");   //卡类型
//        Element bodyChild_issuebankcode = new Element("issuebankcode"); // 发卡行机构代码
        //节点赋值
        headerChild_version.setText(map.get("version"));
        headerChild_transtype.setText(map.get("transtype"));
        headerChild_employno.setText(map.get("employno"));
        headerChild_shopid.setText(map.get("shopid"));
        headerChild_termid.setText(map.get("termid"));
//        headerChild_request_time.setText(map.get("request_time"));
        headerChild_response_time.setText(map.get("response_time"));
        headerChild_response_code.setText(map.get("response_code"));
        headerChild_response_msg.setText(map.get("response_msg"));
        headerChild_mac.setText(map.get("mac"));
        //
        bodyChild_orderno.setText(map.get("orderno"));
        bodyChild_cod.setText(map.get("cod"));
//        bodyChild_payway.setText(map.get("payway"));       //代收款支付方式
//        bodyChild_banktrace.setText(map.get("banktrace")); //银行系统参考号 
//        bodyChild_postrace.setText(map.get("postrace"));   //POS 机的流水号 
//        bodyChild_tracetime.setText(map.get("tracetime")); //银行交易时间
//        bodyChild_cardid.setText(map.get("cardid"));       //银行卡号 
//        bodyChild_signflag.setText(map.get("signflag"));   //本人签收标记
//        bodyChild_signer.setText(map.get("signer"));       //签收人 
//        bodyChild_cardtype.setText(map.get("cardtype"));   //卡类型
//        bodyChild_issuebankcode.setText(map.get("issuebankcode")); // 发卡行机构代码
        //拼装
        header.addContent(headerChild_version);
        header.addContent(headerChild_transtype);
        header.addContent(headerChild_employno);
        header.addContent(headerChild_shopid);
        header.addContent(headerChild_termid);
//        header.addContent(headerChild_request_time);
        header.addContent(headerChild_response_time);
        header.addContent(headerChild_response_code);
        header.addContent(headerChild_response_msg);
        header.addContent(headerChild_mac);
        //
        body.addContent(bodyChild_orderno);
        body.addContent(bodyChild_cod);
//        body.addContent(bodyChild_payway);
//        body.addContent(bodyChild_banktrace);
//        body.addContent(bodyChild_postrace);
//        body.addContent(bodyChild_tracetime);
//        body.addContent(bodyChild_cardid);
//        body.addContent(bodyChild_signflag);
//        body.addContent(bodyChild_signer);
//        body.addContent(bodyChild_cardtype);
//        body.addContent(bodyChild_issuebankcode);
        //
        root.addContent(header);
        root.addContent(body);
        document.setRootElement(root);
        //document对象转换String字符串
        String xmlStr = documentToString(document, begin, end);
        return xmlStr;
	}
	
	public static String createP034Msg(Map<String, String> map, String begin, String end){
		Document document=new Document();
		//根节点
        Element root=new Element("transaction");
        //二级节点
        Element header = new Element("transaction_header");
        Element body = new Element("transaction_body");
        //三级节点
        Element headerChild_version = new Element("version");             //
        Element headerChild_transtype = new Element("transtype");         //
        Element headerChild_employno  = new Element("employno");          //
        Element headerChild_shopid  = new Element("shopid");              //
        Element headerChild_termid  = new Element("termid");              //
//        Element headerChild_request_time  = new Element("request_time");  //
        Element headerChild_response_time  = new Element("response_time");
        Element headerChild_response_code  = new Element("response_code");
        Element headerChild_response_msg  = new Element("response_msg");
        Element headerChild_mac  = new Element("mac");                    //
        //
        Element bodyChild_orderno = new Element("orderno");     //运单号 
        Element bodyChild_cod = new Element("cod");             //代收款金额
//        Element bodyChild_payway = new Element("payway");       //代收款支付方式
//        Element bodyChild_banktrace = new Element("banktrace"); //银行系统参考号 
//        Element bodyChild_postrace = new Element("postrace");   //POS 机的流水号 
//        Element bodyChild_tracetime = new Element("tracetime"); //银行交易时间
//        Element bodyChild_cardid = new Element("cardid");       //银行卡号 
//        Element bodyChild_signflag = new Element("signflag");   //本人签收标记
//        Element bodyChild_signer = new Element("signer");       //签收人 
//        Element bodyChild_cardtype = new Element("cardtype");   //卡类型
//        Element bodyChild_issuebankcode = new Element("issuebankcode"); // 发卡行机构代码
        //节点赋值
        headerChild_version.setText(map.get("version"));
        headerChild_transtype.setText(map.get("transtype"));
        headerChild_employno.setText(map.get("employno"));
        headerChild_shopid.setText(map.get("shopid"));
        headerChild_termid.setText(map.get("termid"));
//        headerChild_request_time.setText(map.get("request_time"));
        headerChild_response_time.setText(map.get("response_time"));
        headerChild_response_code.setText(map.get("response_code"));
        headerChild_response_msg.setText(map.get("response_msg"));
        headerChild_mac.setText(map.get("mac"));
        //
        bodyChild_orderno.setText(map.get("orderno"));
        bodyChild_cod.setText(map.get("cod"));
//        bodyChild_payway.setText(map.get("payway"));       //代收款支付方式
//        bodyChild_banktrace.setText(map.get("banktrace")); //银行系统参考号 
//        bodyChild_postrace.setText(map.get("postrace"));   //POS 机的流水号 
//        bodyChild_tracetime.setText(map.get("tracetime")); //银行交易时间
//        bodyChild_cardid.setText(map.get("cardid"));       //银行卡号 
//        bodyChild_signflag.setText(map.get("signflag"));   //本人签收标记
//        bodyChild_signer.setText(map.get("signer"));       //签收人 
//        bodyChild_cardtype.setText(map.get("cardtype"));   //卡类型
//        bodyChild_issuebankcode.setText(map.get("issuebankcode")); // 发卡行机构代码
        //拼装
        header.addContent(headerChild_version);
        header.addContent(headerChild_transtype);
        header.addContent(headerChild_employno);
        header.addContent(headerChild_shopid);
        header.addContent(headerChild_termid);
//        header.addContent(headerChild_request_time);
        header.addContent(headerChild_response_time);
        header.addContent(headerChild_response_code);
        header.addContent(headerChild_response_msg);
        header.addContent(headerChild_mac);
        //
        body.addContent(bodyChild_orderno);
        body.addContent(bodyChild_cod);
//        body.addContent(bodyChild_payway);
//        body.addContent(bodyChild_banktrace);
//        body.addContent(bodyChild_postrace);
//        body.addContent(bodyChild_tracetime);
//        body.addContent(bodyChild_cardid);
//        body.addContent(bodyChild_signflag);
//        body.addContent(bodyChild_signer);
//        body.addContent(bodyChild_cardtype);
//        body.addContent(bodyChild_issuebankcode);
        //
        root.addContent(header);
        root.addContent(body);
        document.setRootElement(root);
        //document对象转换String字符串
        String xmlStr = documentToString(document, begin, end);
        return xmlStr;
	}
	
	public static String createCheckBillRequestContent(PayStatementsVo payStatementsVo){
		Document document=new Document();
		//根节点
        Element root=new Element("transaction");
        //二级节点
        Element header = new Element("transaction_header");
        Element body = new Element("transaction_body");
        //三级节点
        Element headerChild_version = new Element("version");             //
        Element headerChild_transtype = new Element("transtype");         //
        Element headerChild_employno  = new Element("employno");          //
        Element headerChild_shopid  = new Element("shopid");              //
        Element headerChild_termid  = new Element("termid");              //
        Element headerChild_request_time  = new Element("request_time");  //
        //
        Element bodyChild_orderno = new Element("orderno");     //运单号 
        Element bodyChild_cod = new Element("cod");             //代收款金额
//        Element bodyChild_payway = new Element("payway");       //代收款支付方式
        Element bodyChild_banktrace = new Element("banktrace"); //银行系统参考号 
//        Element bodyChild_postrace = new Element("postrace");   //POS 机的流水号 
        Element bodyChild_tracetime = new Element("tracetime"); //银行交易时间
        
        //节点赋值
//        headerChild_version.setText(map.get("version"));
        headerChild_version.setText("V2.1.2");
//        headerChild_transtype.setText(map.get("transtype"));
        headerChild_transtype.setText(payStatementsVo.getPayTypes());
        
//        headerChild_employno.setText(map.get("employno"));
        headerChild_employno.setText(payStatementsVo.getEmployno());        
//        headerChild_shopid.setText(map.get("shopid"));
        headerChild_shopid.setText(payStatementsVo.getShopId());
//        headerChild_termid.setText(map.get("termid"));
        headerChild_termid.setText(payStatementsVo.getPosCode());        
//        headerChild_request_time.setText(map.get("request_time"));
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhMMss");
        headerChild_request_time.setText(sdf.format(new Date()));
        //
//        bodyChild_orderno.setText(map.get("orderno"));
        bodyChild_orderno.setText(payStatementsVo.getPayCode());        
//        bodyChild_cod.setText(map.get("cod"));
        bodyChild_cod.setText(payStatementsVo.getMoney().toString());
        
//        bodyChild_payway.setText(map.get("payway"));       //代收款支付方式
        
//        bodyChild_banktrace.setText(map.get("banktrace")); //银行系统参考号 
        bodyChild_banktrace.setText(payStatementsVo.getSerialNumber()); //银行系统参考号 
        
//        bodyChild_postrace.setText(map.get("postrace"));   //POS 机的流水号 
        
//        bodyChild_tracetime.setText(map.get("tracetime")); //银行交易时间
        bodyChild_tracetime.setText(sdf.format(payStatementsVo.getPayDate())); //银行交易时间
        
        //拼装
        header.addContent(headerChild_version);
        header.addContent(headerChild_transtype);
        header.addContent(headerChild_employno);
        header.addContent(headerChild_shopid);
        header.addContent(headerChild_termid);
        header.addContent(headerChild_request_time);
        //
        body.addContent(bodyChild_orderno);
        body.addContent(bodyChild_cod);
//        body.addContent(bodyChild_payway);
        body.addContent(bodyChild_banktrace);
//        body.addContent(bodyChild_postrace);
        body.addContent(bodyChild_tracetime);
        //
        root.addContent(header);
        root.addContent(body);
        document.setRootElement(root);
        //document对象转换String字符串
        String xmlStr = documentToString(document, null, null);
        return xmlStr;
	}
	
	/**
	 * Document对象转换为String串
	 * @param document
	 * @param begin
	 * @param end
	 * @return
	 * @author yangzhenlin
	 */
	public static String documentToString(Document document, String begin, String end){
		//document对象转换String字符串
        Format format = Format.getPrettyFormat();
        XMLOutputter xmlout = new XMLOutputter(format);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        String xmlStr = "";
        try {
			xmlout.output(document, bo);
			xmlStr = bo.toString();
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bo.close();
			} catch (IOException e) {}
		}
        xmlStr = fotmatXml(xmlStr);
        if(!StringUtils.isBlank(begin)){
        	xmlStr = begin + xmlStr;
        }
        if(!StringUtils.isBlank(end)){
        	xmlStr = xmlStr + end;
        }
        return xmlStr;
	}
	
	/**
	 * xml中的单封闭标签替换为成对标签，如：<transtype /> →→  <transtype></transtype>
	 * @param xmlMsg
	 * @return
	 * @author yangzhenlin
	 */
	public static String fotmatXml(String xmlMsg) {
		OutputFormat format=new OutputFormat();
		format.setEncoding("UTF-8");
		//关闭自闭合标签
		format.setExpandEmptyElements(true);
		StringWriter strWtr=new StringWriter();
		XMLWriter xmlWrt = null;
		try {
			xmlWrt=new XMLWriter(strWtr,format);
			xmlWrt.write(DocumentHelper.parseText(xmlMsg));
			xmlWrt.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(xmlWrt!=null) {
				try {
					xmlWrt.close();
				} catch (IOException e) {}
			}
		}
		//格式化，去掉换行
		String formatXml = strWtr.toString().replaceAll("\r\n", "");
		return formatXml;
	}
	
	public static void main(String[] args) {
		String str = 
				"context=<?xml version='1.0' encoding='utf-8'?>"
				+ "<transaction>"
					+ "<transaction_header>"
						+ "<version>V2.1.2</version>"
						+ "<transtype>P034</transtype>"
						+ "<employno>20170450</employno>"
						+ "<shopid>898111982490630</shopid>"
						+ "<termid>89DK5911</termid>"
						+ "<request_time>20180809104509</request_time>"
					+ "</transaction_header>"
					+ "<transaction_body>"
						+ "<orderno>147258369</orderno>"
						+ "<cod>0.01</cod>"
					+ "</transaction_body>"
				+ "</transaction>";
//				"<?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&lt;transaction&gt;&lt;transaction_header&gt;&lt;version&gt;1.0&lt;/version&gt;&lt;transtype&gt;2222222222&lt;/transtype&gt;&lt;employno&gt;3333333&lt;/employno&gt;&lt;termid&gt;4&lt;/termid&gt;&lt;shopid&gt;5&lt;/shopid&gt;&lt;request_time&gt;6&lt;/request_time&gt;&lt;response_time&gt;7&lt;/response_time&gt;&lt;response_code&gt;8&lt;/response_code&gt;&lt;response_msg&gt;9&lt;/response_msg&gt;&lt;mac&gt;10&lt;/mac&gt;&lt;/transaction_header&gt;&lt;transaction_body&gt;&lt;orderno&gt;11&lt;/orderno&gt;&lt;cod&gt;12&lt;/cod&gt;&lt;/transaction_body&gt;&lt;/transaction&gt;";
		str = str.replace("&lt;", '<' +"");
		str = str.replace("&gt;", '>' +"");
		str = str.replace("&quot;", '"' +"");
		str = str.replace(" ", " ");
		System.out.println("str--"+str);
		
		Map map = getStringMsg(str);
		System.out.println("map--"+map);
		
//		String xmlStr = createP007Msg(map, null, null);
//		System.out.println("createP007Msg--"+xmlStr);
//		
//		String xmlStr1 = createP033Msg(map, null, null);
//		System.out.println("createP007Msg--"+xmlStr1);
//		
//		String xmlStr2 = createP034Msg(map, null, null);
//		System.out.println("createP007Msg--"+xmlStr2);
//		
//		String xmlStr3 = createP074Msg(map, null, null);
//		System.out.println("createP007Msg--"+xmlStr3);
	}
	
}
