package com.edupay.app.service;

import java.util.List;

import com.edupay.app.entity.App;

/**
 * 应用  业务接口
 * Title: AppService 
 * @author yangzhenlin 
 * @date 2018年7月4日
 */
public interface AppService {

	/**
	 * 新增
	 * @param app
	 * @return
	 * @author yangzhenlin
	 */
	public String add(App app) throws RuntimeException;;
	
	/** 
	 * 根据token 获取对应的 应用ID
	 * @param token
	 * @return
	 * @author yangzhenlin
	 */
	public Integer getAppIdByToken(String token);
	
	/**
	 * 根据appID 获取 应用信息
	 * @param appId
	 * @return
	 * @author yangzhenlin
	 */
	public App getAppById(Integer appId);
	
	/**
	 * 修改应用信息
	 * @param app
	 * @return
	 * @author yangzhenlin
	 */
	public Integer updApp(App app);
	
	/**
	 * 获取应用集合列表
	 * @return
	 * @author yangzhenlin
	 */
	public List<App> getAppList();
}
