package com.edupay.pay.service;

import com.edupay.pay.vo.TerminalVo;

/**
 * POS机终端 业务接口
 *
 * Title: TerminalSerivce.java
 * @author: Jack
 * @create: 2018-08-10
 *
 */
public interface TerminalService {
	
	/**
	 * 解析请求，做终端信息校验
	 * 
	 * @param context
	 * @return
	 * @Author: Jack
	 * @Date: 2018/07/10
	 */
	public TerminalVo getTerminalVo(String context,Integer payType);
	
}
