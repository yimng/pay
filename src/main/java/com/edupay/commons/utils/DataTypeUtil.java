package com.edupay.commons.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.dubbo.common.json.JSONObject;
import com.alibaba.dubbo.common.json.ParseException;

/**
 * @program: zbg-edu
 * @description: 数据转换工具
 * @author: dongminghao
 * @create: 2018-04-17
 *
 */
public class DataTypeUtil {

	/**
	 * @Description: 获取Map对象中的Key，并转换为String类型，默认为null
	 * @param map
	 * @param key
	 * @return
	 * @Author: dongminghao
	 * @Date: 2018/04/17
	 */
	public static String getMapString(Map<String,Object> map, String key){
    	if(map.containsKey(key)){
    		return map.get(key).toString();
    	}
    	else{
    		return null;
    	}
    }
	
	/**
	 * @Description: 获取Map对象中的Key，并转换为Integer类型，默认值为null
	 * @param map
	 * @param key
	 * @return
	 * @Author: dongminghao
	 * @Date: 2018/04/17
	 */
	public static Integer getMapInteger(Map<String,Object> map, String key){
		return getMapInteger(map, key, null);
    }
	
	/**
	 * 获取Map对象中的Key，并转换为Integer类型
	 * @param map
	 * @param key
	 * @param defaultValue 指定默认值
	 * @return
	 */
	public static Integer getMapInteger(Map<String,Object> map, String key, Integer defaultValue){
		Integer num = defaultValue;
    	if(map.containsKey(key)){
    		try {
				num = Integer.parseInt(map.get(key).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
    	}
		return num;
    }
	
	/**
	 * 将json转为BeanList
	 * dongminghao
	 * @param list
	 * @param json
	 * @param cls
	 */
	public static <T> void jsonConvertBeanList(List<T> list, String json, Class<T> cls){
		json = json.replaceAll("&quot;", "'").replaceAll("&#39;", "'");
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray)JSON.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(jsonArray==null || jsonArray.length()==0){
			return;
		}
		try {
			T t;
			JSONObject jsonObject;
			for(int i=0;i<jsonArray.length();i++){
				jsonObject = (JSONObject)jsonArray.get(i);
				t = cls.newInstance();
				ReflectUtil.jsonConvertBean(t, jsonObject);
				list.add(t);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 获取Map对象中的Key，并转换为日期类型
	 * @param map
	 * @param key
	 * @return
	 * @throws Exception
	 * @Author: Jack
	 * @Date: 2018/04/17
	 */
	public static Date getMapDate(Map<String,Object> map, String key){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(map.containsKey(key)){
			try{
				return sdf.parse(map.get(key).toString());
			}catch(Exception e){
				e.printStackTrace();
				try{
					return sdf.parse(sdf.format(map.get(key)));
				}catch(Exception ee)
				{
					return null;
				}
			}	
    	}
    	else{
    		return null;
    	}
    }
	
	/**
	 * 
	 * 获取Map对象中的Key，并转换为日期时间类型
	 * @param map
	 * @param key
	 * @return
	 * @throws Exception
	 * @Author: Jack
	 * @Date: 2018/04/17
	 */
	public static Date getMapDateTime(Map<String,Object> map, String key){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
    	if(map.containsKey(key)){
			try{
				return sdf.parse(map.get(key).toString());
			}catch(Exception e){
				e.printStackTrace();
				try{
					return sdf.parse(sdf.format(map.get(key)));
				}catch(Exception ee)
				{
					return null;
				}
			}	
    	}
    	else{
    		return null;
    	}
    }
	
	/**
	 * map深度克隆
	 * @param map
	 * @return
	 */
	public static <K1, K2, V> Map<K1, Map<K2, V>> mapClone(Map<K1, Map<K2, V>> map){  
	    Map<K1, Map<K2, V>> copy = new HashMap<K1, Map<K2, V>>();  
	    for(K1 key : map.keySet()){  
	    	Map<K2, V> entry = map.get(key);
	        copy.put(key, new HashMap<K2, V>(entry));  
	    }  
	    return copy;  
	}
	
	/**
	 * 根据字段类型自行区分空判断
	 * @param params
	 * @return false 为存在空
	 */
	public static boolean checkNotBlank(Object... params) {
		if(params==null || params.length==0){
			return true;
		}
		for(Object param : params){
			if(param==null){
				return false;
			}
			//String
			if(param instanceof String){
				if(StringUtils.isBlank(param.toString())){
					return false;
				}
			}
			//Integer
			else if(param instanceof Integer){
				
			}
			//Date
			else if(param instanceof Date){
				
			}
			//BigDecimal
			else if(param instanceof BigDecimal){
				
			}
			//Boolean
			else if(param instanceof Boolean){
				
			}
			//MultipartFile
			else if(param instanceof MultipartFile){
				if(((MultipartFile)param).getSize()==0){
					return false;
				}
			}
			//待补充
			else{
				
			}
		}
		return true;
	}
	
}
