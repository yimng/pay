package com.edupay.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * Description: All Rights Reserved.
 *
 * @version 1.0 2013-1-24 上午9:36:04 by 于科为 kw.yu@zuche.com
 */
public class PropertiesUtil {

	/**
	 * getProperties : 取得路径下的属性信息
	 *
	 * @param path
	 *
	 * @return Properties
	 *
	 * @throws
	 */
	public static Properties getProperties(String path) {
		File file = loadFile(path);
		Properties properties = new Properties();
		String baseMessage = "读取配置文件错误。";
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			properties.load(fileInputStream);
		} catch (FileNotFoundException e) {
			String message = baseMessage + "文件:" + path + "不存在。";
			throw new RuntimeException(message + "异常信息如下:" + e.getMessage());
		} catch (IOException e) {
			String message = baseMessage;
			throw new RuntimeException(message + "异常信息如下:" + e.getMessage());
		} catch (Exception e) {
			String message = baseMessage;
			throw new RuntimeException(message + "异常信息如下:" + e.getMessage());
		} finally {
			if(fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {}
			}
		}
		return properties;
	}

	/**
	 * writeProperties : 写入路径下的属性信息
	 *
	 * @param properties
	 * @param filePath
	 * @param comments   void
	 *
	 * @throws
	 */
	public static void writeProperties(Properties properties, String filePath, String comments) {
		File propertiesFile = new File(filePath);
		File directoryPath = propertiesFile.getParentFile();

		if (!directoryPath.exists()) {
			directoryPath.mkdirs();
		}

		String baseMessage = "写入配置文件" + filePath + "错误。";
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(propertiesFile);
			properties.store(outputStream, comments);
		} catch (IOException e) {
			throw new RuntimeException(baseMessage + "异常信息如下:" + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(baseMessage + "异常信息如下:" + e.getMessage());
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					throw new RuntimeException("关闭文件流错误,异常信息如下:" + e.getMessage());
				} catch (Exception e) {
					throw new RuntimeException("关闭文件流错误,异常信息如下:" + e.getMessage());
				} finally {
					outputStream = null;
				}
			}
		}
	}

	/**
	 * loadFile :读入文件
	 *
	 * @param filePath
	 *
	 * @return File
	 *
	 * @throws
	 */
	private static File loadFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new RuntimeException("文件: " + filePath + "不存在....");
		} else if (!filePath.endsWith(".properties")) {
			throw new RuntimeException("文件: " + filePath + "非.properties文件。无法正确解析");
		} else {
			return file;
		}
	}
	
	/**
	 * 获取配置文件信息(相对路径)
	 * @param name
	 * @return
	 */
	public static Properties loadProperties(String name){
		if(StringUtils.isBlank(name)){
			return null;
		}
		Properties properties = null;
		InputStream inputStream = null;
		try {
			inputStream = PropertiesUtil.class.getResourceAsStream("/"+name);
			properties = new Properties();
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
		return properties;
	}
	
	private static Properties properties=PropertiesUtil.loadProperties("pospay-config.properties");
	private static String host=properties.getProperty("sftp.host");
	private static String port=properties.getProperty("sftp.port");
	private static String username=properties.getProperty("sftp.username");
	private static String password=properties.getProperty("sftp.password");
	private static String localtion=properties.getProperty("sftp.location");

	public static void main(String[] args) {
//		Properties properties = loadProperties("jdbc.properties");
		System.out.println(host);
		System.out.println(port);
		System.out.println(username);
		System.out.println(password);
		System.out.println(localtion);
		
		
		
	}
}
