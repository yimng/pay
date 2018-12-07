package com.edupay.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

import com.alibaba.dubbo.common.json.JSONObject;

/**
 * @program: zbg-edu
 * @description: 反射工具类
 * @author: dongminghao
 * @create: 2018-04-17
 *
 */
public class ReflectUtil {

	/**
	 * @Description: 将Map转换为实体对象，对象必须先new后传入，Map中字段类型必须与实体对应字段类型一致否则无法set
	 * @param t new的实体
	 * @param map 
	 * @return
	 * @Author: dongminghao
	 * @Date: 2018/04/17
	 */
	public static <T> void mapConvertBean(T t, Map<String, Object> map){
		if(t==null || map==null || map.size()==0){
			return;
		}
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field field : fields){
			if(!map.containsKey(field.getName())){
				continue;
			}
			setter(t, field.getName(), map.get(field.getName()), field.getType());
		}
		return;
	}
	
	/**
	 * json转为Bean
	 * dongminghao
	 * @param t
	 * @param json
	 */
	public static <T> void jsonConvertBean(T t, JSONObject json){
		if(t==null || json==null){
			return;
		}
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field field : fields){
			if(!json.contains(field.getName())){
				continue;
			}
			//针对Date特殊处理
			if("java.util.Date".equals(field.getType().getName())){
				DateFormat format = null;
				if(json.get(field.getName()).toString().length()==19){
					format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				}
				else if(json.get(field.getName()).toString().length()==10){
					format = new SimpleDateFormat("yyyy-MM-dd");
				}
				Date date = null;
				try {
					date = format.parse(json.get(field.getName()).toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(date!=null){
					setter(t, field.getName(), date, field.getType());
				}
				
			}
			else{
				setter(t, field.getName(), json.get(field.getName()), field.getType());
			}
		}
		return;
	}
	
	/**
	 * 两个实体类复制
	 * 要求：属性名，属性类型完全一致才会被复制
	 * @param from
	 * @param to
	 */
	public static <T,V> void beanConvertBean(T from, V to){
		if(from==null || to==null){
			return;
		}
		Field[] fromFields = from.getClass().getDeclaredFields();
		Field toField;
		Object value;
		for(Field fromField : fromFields){
			toField = null;
			try {
				toField = to.getClass().getDeclaredField(fromField.getName());
				if(toField!=null && fromField.getType().getName().equals(toField.getType().getName())){
					value = getter(from, toField.getName());
					if(value!=null){
						setter(to, toField.getName(), value, toField.getType());
					}
				}
			} catch (NoSuchFieldException e) {
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @Description: 执行属性的set方法
     * @param obj 操作的对象
     * @param att 操作的属性
     * @param value 设置的值
     * @param type 参数的属性
	 * @Author: dongminghao
	 * @Date: 2018/04/17
	 */
    private static void setter(Object obj, String att, Object value, Class<?> type) {
        try {
        	value = ConvertUtils.convert(value, type);
            Method method = obj.getClass().getMethod("set" + fieldName(att), type);
            method.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
	 * @Description: 执行属性的get方法
     * @param obj 操作的对象
     * @param att 操作的属性
	 * @Author: dongminghao
	 * @Date: 2018/05/06
	 */
    public static Object getter(Object obj, String att) {
    	Object value = null;
        try {
        	Method method = obj.getClass().getMethod("get" + fieldName(att));
        	value = method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    
    /**
     * @Description: 驼峰命名方式获取字段名
     * @param name
     * @return
     * @Author: dongminghao
     * @Date: 2018/04/17
     */
    private static String fieldName(String name){
		name = name.substring(0,1).toUpperCase()+name.substring(1);
		for(;name.indexOf("_")!=-1 && name.indexOf("_")+1<name.length();){
			name = name.replace("_"+name.substring(name.indexOf("_")+1, name.indexOf("_")+2), name.substring(name.indexOf("_")+1, name.indexOf("_")+2).toUpperCase());
		}
		return name;
	}
	
}
