package com.edupay.commons.api.controller;

import java.util.Map;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edupay.commons.constants.CommonsApiErrorCodeConstants;

/**
 * 全局捕捉异常
 * 
 * Title 
 * @author: dongminghao
 * @date: 2018年5月21日
 */
@ControllerAdvice
public class GlobalExceptionResolver extends ApiCommonController {

	@ResponseBody
	@ExceptionHandler(value = Exception.class)
    public Map<String, Object> defaultErrorHandler(Exception ex) throws Exception {
		if(ex instanceof HttpRequestMethodNotSupportedException){
			return error("HttpRequestMethodNotSupportedException",CommonsApiErrorCodeConstants.METHOD_NOT_SUPPORTED);
		}
		else{
			ex.printStackTrace();
			return error("服务器错误",CommonsApiErrorCodeConstants.UNKNOW_ERROR);
		}
    }

}
