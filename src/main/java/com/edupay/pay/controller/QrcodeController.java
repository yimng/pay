package com.edupay.pay.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.edupay.commons.utils.QrcodeUtil;

/**
 * 支付二维码
 *
 * Title: QrcodeController.java
 * @author: Jack
 * @create: 2018-07-08
 *
 */
@Controller
@RequestMapping("/edupay/qrcode")
public class QrcodeController extends BasePayController{
	
	/**
	 * 加载二维码页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/viewQrcode", method = RequestMethod.GET)
	public ModelAndView viewQrcode(String payToken,String payCode){
//		//TODO 校验token
//		if(null==getAppId(payToken)){
//			return null;
//		}
		ModelAndView view = new ModelAndView("viewQrcode");
		if(StringUtils.isBlank(payCode)){
			payCode=request.getParameter("payCode");
		}
		if(StringUtils.isBlank(payToken)){
			payToken=request.getParameter("payToken");
		}
		
		//TODO 校验Token和支付单号是否有效
		
		try{
			request.setAttribute("payCode",payCode);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return view;
	}
	
	/**
	 * 生成二维码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getQrcode",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String payToken,String payCode){
    	//TODO 校验token
//    	if(null==getAppId(payToken)){
//    		return null;
//    	}
		int width = 300;
        int height = 300;
        String format = "png";
       
        HttpHeaders headers = new HttpHeaders(); 
        String filePath= request.getSession().getServletContext().getRealPath("/upload");
        filePath=filePath.replace("\\", "/");
        filePath+="/"+payCode+".png";
        new ResponseEntity<byte[]>(QrcodeUtil.generationQrcode(initOldContent(payCode),filePath,format,width,height),headers,HttpStatus.CREATED);
//        new ResponseEntity<byte[]>(QrcodeUtil.generationQrcode(initNewContent(payCode),filePath,format,width,height),headers,HttpStatus.CREATED);
		
        getImage(filePath);
        
		return null;
	}
    
    private void getImage(String filePath){
    	FileInputStream is = null;
    	OutputStream toClient = null;
    	try{
	        is = new FileInputStream(filePath);
	        int i = is.available(); // 得到文件大小  
	        byte data[] = new byte[i];  
	        is.read(data); // 读数据  
	        is.close();
	        response.setContentType("image/*"); // 设置返回的文件类型  
	        toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象  
	        toClient.write(data); // 输出数据  
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	if(toClient!=null) {
        		try {
					toClient.close();
				} catch (IOException e) {}
        	}
        	if(is!=null) {
        		try {
					is.close();
				} catch (IOException e) {}
        	}
		}
        clearFile(filePath);
        
    }
    
    //旧POS机规则非json格式
    private String initOldContent(String payCode)
    {
    	 StringBuffer qrcodeContent = new StringBuffer();
         qrcodeContent.append(payCode);
         return qrcodeContent.toString();
    }
    
//    //新POS机规则
//    private String initNewContent(String payCode)
//    {
//    	 StringBuffer qrcodeContent = new StringBuffer();
//         qrcodeContent.append("{");
//         qrcodeContent.append("\"orderno\":");
//         qrcodeContent.append("\""+payCode+"\"");
//         qrcodeContent.append("}");
//         return qrcodeContent.toString();
//    }
    
    private void clearFile(String filePath)
    {
    	File file=new File(filePath);
        file.delete();
    }
	
}
