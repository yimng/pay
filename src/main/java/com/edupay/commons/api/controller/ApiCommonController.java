package com.edupay.commons.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.edupay.cache.redis.service.RedisRawService;
import com.edupay.commons.constants.CommonsApiErrorCodeConstants;
import com.edupay.commons.utils.PageResult;
import com.edupay.commons.utils.StringEscapeEditor;

/**
 * 公共对外接口基类
 *
 * Title ApiCommonController 
 * @author wanglu  
 * @date 2018年6月3日
 */
public class ApiCommonController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    @Autowired
    private RedisRawService redisRawService;
	
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    // 时间类型转换
    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              HttpServletResponse response, ServletRequestDataBinder binder)
            throws Exception {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd"), true));
        binder.registerCustomEditor(String.class, new StringEscapeEditor(false,
                false, false, true));

        response.setHeader("Access-Control-Allow-Origin", "*");
    }
    
    /**
     * 初始化Response
     */
    protected HttpServletResponse initResponse(HttpServletResponse response) {
        response.setContentType("text/plain;charset=UTF-8");
        response.setDateHeader("Expires", 1L);
        response.addHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        return response;
    }

    /**
     * 失败后返回
     *
     * @param msg
     * @return
     */
    protected Map<String, Object> error(String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", "error");
        map.put("msg", msg);
        return map;
    }
    
    /**
     * 失败后返回
     *
     * @param msg
     * @param code 错误码
     * @return
     */
    protected Map<String, Object> error(String msg,String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        return error(msg,map);
    }
    
    protected Map<String, Object> error(String msg,String code, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("data", data);
        return error(msg,map);
    }
    
    /**
     * 针对Service层捕获异常的形式返回错误信息
     *
     * @param e
     * @return
     */
    protected Map<String, Object> error(RuntimeException e) {
    	//获取异常信息
    	String exceptionMsg = e.getMessage();
    	//解析异常信息
    	if(StringUtils.isBlank(exceptionMsg)){
    		e.printStackTrace();
    		return error("服务器异常", CommonsApiErrorCodeConstants.UNKNOW_ERROR);
    	}
    	//异常信息格式为“code#msg”
    	String[] exceptionMsgArr = exceptionMsg.split("#");
    	if(exceptionMsgArr == null || exceptionMsgArr.length != 2){
    		e.printStackTrace();
    		return error("服务器异常", CommonsApiErrorCodeConstants.UNKNOW_ERROR);
    	}
    	String code = exceptionMsgArr[0];
    	String msg = exceptionMsgArr[1];
        return error(msg,code);
    }
    
    /**
     * 失败后返回（支持结构体
     * 
     * @param msg
     * @param data
     * @return
     * @Author: Jack
     * @Date: 2018/04/24
     */
    protected Map<String, Object> error(String msg,Map<String, Object> data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", "error");
        map.put("msg", msg);
        map.put("data",data);
        return map;
    }

    /**
     * 成功后返回
     *
     * @return
     */
    protected Map<String, Object> success(Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", "success");
        map.put("data", data);
        map.put("msg", "成功返回");
        return map;
    }
    
    /**
     * 分页成功返回
     * @param pageResult 返回数据
     * @param totalRecord 总数
     * @param pageSize 每页记录数
     * @param currentPage 当前页
     * @return
     */
    protected Map<String, Object> success(Object pageResult,int totalRecord,int pageSize,int currentPage) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", "success");
        map.put("data", pageResult);
        map.put("totalRecord", totalRecord);
        map.put("pageSize", pageSize);
        map.put("currentPage", currentPage);
        map.put("msg", "成功返回");
        return map;
    }
    
    /**
     * 分页成功返回
     * @param pageResult
     * @return
     */
    protected Map<String, Object> successPage(PageResult pageResult) {
        return success(pageResult.getPageResult(), pageResult.getTotalRecord(), pageResult.getPageSize(), pageResult.getCurrentPage());
    }
    
    /**
     * 通过token获取用户id，并更新token的有效时间
     * @param token
     * @param seconds
     * @return
     */
    protected String getMemberId(String token){
    	int seconds = 6*60 * 60;
    	String memberId = "";
    	if(!StringUtils.isEmpty(token)){
    		byte[] object = redisRawService.get(token);
    		if(object != null){
    			memberId = new String(object);
    			if(seconds>0){
    				redisRawService.set(token,memberId,Long.valueOf(seconds));
    			}
    		}
    	}
    	return memberId;
    }
    
}
