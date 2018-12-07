package com.edupay.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.edupay.app.dao.AppMapper;
import com.edupay.app.entity.App;
import com.edupay.app.service.AppService;

import tk.mybatis.mapper.entity.Example;

/**
 * 应用 业务实现
 * Title: AppServiceImpl 
 * @author yangzhenlin 
 * @date 2018年7月4日
 */
@Component
@Service
public class AppServiceImpl implements AppService {

	@Autowired
	private AppMapper appMapper;
	
	private static final String HAS_NAME = "303";
	
	@Override
	public String add(App app) throws RuntimeException{
		if(isExistByAppName(app.getAppName(), null)){
			throw new RuntimeException(HAS_NAME+"#名称不能重复");
		}
		String token;
		do {
			token = UUID.randomUUID().toString();
			token = token.replace("-", "");
		} while (getAppIdByToken(token)!=null);
		app.setToken(token);
		app.setCreateDate(new Date());
		int r = appMapper.insert(app);
		if(0<r)
			return app.getAppName();
		return null;
	}
	
	public boolean isExistByAppName(String appName, Integer appId){
		boolean flag = false;
		//空判断
		if(StringUtils.isBlank(appName)){
			return flag;
		}
		// 查询
		Example example = new Example(App.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("appName",appName);
		if(appId!=null){
			criteria.andNotEqualTo("appId",appId);
		}
		//查询总条数
		int count = appMapper.selectCountByExample(example);
    	//若存在数据则返回true
    	if(count > 0){
    		flag = true;
    	}
		return flag;
	}

	@Override
	public Integer getAppIdByToken(String token) {
		//空判断
		if(StringUtils.isBlank(token)){
			return null;
		}
		//根据 token 查询
    	//
		Example example = new Example(App.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("token", token);
		//查询
		List<App> list = appMapper.selectByExample(example);
    	//若存在数据则返回 appID
    	if(list!=null && list.size()>0){
    		return list.get(0).getAppId();
    	}
		return null;
	}

	@Override
	public List<App> getAppList() {
		Example example = new Example(App.class);
		List<App> list = appMapper.selectByExample(example);
		return list;
	}

	@Override
	public App getAppById(Integer appId) {
		App app = appMapper.selectByPrimaryKey(appId);
		return app;
	}

	@Override
	public Integer updApp(App app) {
		if(isExistByAppName(app.getAppName(), app.getAppId())){
			return -1;
		}
		if(getAppIdByToken(app.getToken())!=null){
			return -2;
		}
		int r = appMapper.updateByPrimaryKeySelective(app);
		return r;
	}

}
