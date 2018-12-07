package com.edupay.commons.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串处理工具类
 *
 * Title: StringUtil.java
 * @author: Jack
 * @create: 2018-04-21
 *
 */
public class StringUtil {
	
	/**
	 * 字符串类型转换为int类型
	 * 
	 * @param str
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	public static int stringToInt(String str){
		if(StringUtils.isNotBlank(str))
		{
			try{
				return Integer.parseInt(str);
			}catch(Exception e)
			{
				e.printStackTrace();
				return -1;
			}
		}
		return -1;
	}
	
	/**
	 * 将具有分隔符拼装的字符串解析成字符串集合
	 * 
	 * @param str 需解析的字符串
	 * @param delimiter 分隔符
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	public static List<String> stringToList(String str,String delimiter){
		if(StringUtils.isNotBlank(str)&&StringUtils.isNotBlank(delimiter)){
			String[] names = str.split(delimiter);
			return getListByArray(names);
		}
		return null;
	}
	
	/**
	 * 将具有分隔符拼装的字符串解析成字符串集合-增加拼装符
	 * 
	 * @param str 需解析的字符串
	 * @param delimiter 分隔符
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	public static List<String> stringToList(String str,String delimiter,String lab){
		if(StringUtils.isNotBlank(str)&&StringUtils.isNotBlank(delimiter)){
			String[] names = str.split(delimiter);
			return getListByArray(names,lab);
		}
		return null;
	}
	
	/**
	 * 将具有分隔符拼装的字符串解析成int集合
	 * 
	 * @param str 需解析的字符串
	 * @param delimiter 分隔符
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	public static List<Integer> stringToIntegerList(String str,String delimiter){
		if(StringUtils.isNotBlank(str)&&StringUtils.isNotBlank(delimiter)){
			String[] names = str.split(delimiter);
			return getIntegerListByArray(names);
		}
		return null;
	}
	
	/**
	 * 将字符串集合转换成成int集合
	 * 
	 * @param str 需解析的字符串
	 * @param delimiter 分隔符
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	public static List<Integer> stringListToIntegerList(List<String> strLst){
		List<Integer> intLst=new ArrayList<Integer>();
		if(null==strLst)
			return null;
		for(String str:strLst)
		{
			if(StringUtils.isNotBlank(str))
				intLst.add(Integer.parseInt(str));
		}
		return intLst;
	}
	
	/**
	 * 将字符串集合拼装成具有分隔符拼装的字符串
	 * 
	 * @param str 需解析的字符串
	 * @param delimiter 分隔符
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	public static String listToString(List<String> lst,String delimiter){
		if(null!=lst&&0<lst.size()&&StringUtils.isNotBlank(delimiter))
		{
			StringBuffer str=new StringBuffer();
			for(int i=0,j=lst.size();i<j;i++)
			{
				str.append(lst.get(i));
				if(i<j-1)
				{
					str.append(delimiter);
				}
			}
			return str.toString();
		}
		return null;
	}
	
	/**
	 * 将字符串集合拼装成具有分隔符拼装的字符串(Sql查询传参用)
	 * 
	 * @param str 需解析的字符串
	 * @param delimiter 分隔符
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	public static String listToStringForSql(List<String> lst,String delimiter){
		if(null!=lst&&0<lst.size()&&StringUtils.isNotBlank(delimiter))
		{
			StringBuffer str=new StringBuffer();
			for(int i=0,j=lst.size();i<j;i++)
			{
				str.append("'");
				str.append(lst.get(i));
				str.append("'");
				if(i<j-1)
				{
					str.append(delimiter);
				}
			}
			return str.toString();
		}
		return null;
	}
	
	/**
	 * 将数组解析为集合返回
	 * 
	 * @param arrays
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	private static List<String> getListByArray(String[] arrays){
		List<String> lst=new ArrayList<String>();
		if(0<arrays.length){
			for(int i=0,j=arrays.length;i<j;i++)
			{
				lst.add(arrays[i]);
			}
			return lst;
		}
		return null;
	}
	
	/**
	 * 将数组解析为集合返回-含标签符
	 * 
	 * @param arrays
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	private static List<String> getListByArray(String[] arrays,String lab){
		List<String> lst=new ArrayList<String>();
		if(0<arrays.length){
			for(int i=0,j=arrays.length;i<j;i++)
			{
				lst.add(lab+arrays[i]+lab);
			}
			return lst;
		}
		return null;
	}
	
	/**
	 * 将数组解析为int集合返回
	 * 
	 * @param arrays
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/21
	 */
	private static List<Integer> getIntegerListByArray(String[] arrays){
		List<Integer> lst=new ArrayList<Integer>();
		if(0<arrays.length){
			for(int i=0,j=arrays.length;i<j;i++)
			{
				if(null!=arrays[i])
				lst.add(Integer.parseInt(arrays[i]));
			}
			return lst;
		}
		return null;
	}
	
	/**
	 * 有分隔符拼装的字符串内部排序
	 * 
	 * @param str
	 * @param delimiter
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/23
	 */
	public static String orderByString(String str,String delimiter)
	{
		if(StringUtils.isNotBlank(str)){
			String[] strArray = str.split(delimiter);
			Arrays.sort(strArray);
			return arrayToString(strArray,delimiter);
		} 
		return str;
	}
	
	/**
	 * 去掉带有分隔符的字符串中重复元素
	 * 
	 * @param str
	 * @param delimiter
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/24
	 */
	public static String removeRepetition(String str,String delimiter)
	{
		if(StringUtils.isNotBlank(str)){
			String[] strArray = str.split(delimiter);
			Object[] rsArray=null;
		    //实例化一个set集合
		    Set<Object> set = new HashSet<Object>();
		    //遍历数组并存入集合,如果元素已存在则不会重复存入
		    for (int i = 0; i < strArray.length; i++){
		    	if(StringUtils.isNotBlank(strArray[i]))
		    	set.add(strArray[i]);
		    }
		    //返回Set集合的数组形式
		    rsArray=set.toArray();
		    return arrayToString(rsArray,delimiter);
		}
		return str;
	}
	
	/**
	 * 去掉字符串集合中的重复元素
	 * 
	 * @param str
	 * @param delimiter
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/24
	 */
	public static List<String> removeRepetition(List<String> strLst)
	{
		if(null!=strLst){
		
			Object[] rsArray=null;
		    //实例化一个set集合
		    Set<Object> set = new HashSet<Object>();
		    //遍历数组并存入集合,如果元素已存在则不会重复存入
		    for (int i = 0,j=strLst.size(); i < j; i++){
		    	if(StringUtils.isNotBlank(strLst.get(i)))
		    	set.add(strLst.get(i));
		    }
		    //返回Set集合的数组形式
		    rsArray=set.toArray();
		    return arrayToStringLst(rsArray);
		}
		return null;
	}
	
	/**
	 * 获取两组字符串元素交集
	 * 
	 * @param str
	 * @param delimiter
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/24
	 */
	public static String getIntersection(String str1,String str2,String delimiter)
	{
		if(StringUtils.isNotBlank(str1)&&StringUtils.isBlank(str2))
			return str1;
		if(StringUtils.isNotBlank(str2)&&StringUtils.isBlank(str1))
			return str2;
		if(StringUtils.isNotBlank(str1)&&StringUtils.isNotBlank(str2)){
			String[] strArray1 = str1.split(delimiter);
			String[] strArray2 = str2.split(delimiter);
			Object[] rsArray=null;
			Set<Object> set = new HashSet<Object>();
		    // 遍历ch1数组
		    for(int i=0; i<strArray1.length; i++)
		    {
		    	String s1=strArray1[i];
		       // 遍历ch2数组
		       for(int j=0;j<strArray2.length; j++)
		       {
		    	   String s2=strArray2[j];
		           if(s1.equals(s2)){
		        	   set.add(s1);
		           }
		       }
			}
		    //返回Set集合的数组形式
		    rsArray=set.toArray();
		    return arrayToString(rsArray,delimiter);
		}
		return null;
	}
	
	/**
	 * 数组转换成带有分隔符的字符串
	 * 
	 * @param objArray
	 * @param delimiter
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/24
	 */
	private static String arrayToString(Object[] objArray,String delimiter)
	{
		StringBuffer sbf=new StringBuffer();
		if(null!=objArray)
		{
			for(int i=0,j=objArray.length;i<j;i++)
		    {
		    	sbf.append(objArray[i]);
				if(i<j-1)
				{
					sbf.append(delimiter);
				}
			}
			return sbf.toString();
		}
		return null;
	}
	
	/**
	 * 数组转换成字符串集合
	 * 
	 * @param objArray
	 * @return
	 * @Author: Jack
	 * @Date: 2018/04/24
	 */
	private static List<String> arrayToStringLst(Object[] objArray)
	{
		List<String> strLst=new ArrayList<String>();
		if(null!=objArray)
		{
			for(int i=0,j=objArray.length;i<j;i++)
		    {
				strLst.add(objArray[i].toString());
			}
		}
		return strLst;
	}
	
	/**
	 * 获取uuid
	 *
	 * @return
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
	
}
