package com.edupay.commons.utils;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;

public class UUIDGenerateUtils {
	public UUIDGenerateUtils() {
	}
	
	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = StringUtils.replace(uuid,"-","");
		return uuid.toUpperCase();
	}
	
	/**
	 * 获得指定数目的UUID
	 * 
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] uuidArray = new String[number];
		for (int i = 0; i < number; i++) {
			uuidArray[i] = getUUID();
		}
		return uuidArray;
	}

	/**
	 * 主函数入口方法
	 *
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		final String uuid = getUUID();
		System.out.println(uuid);
	}
	
}