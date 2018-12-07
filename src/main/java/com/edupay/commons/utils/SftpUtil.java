package com.edupay.commons.utils;

import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.edupay.commons.task.MainTask;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

/**
 * SFTP工具类
 *
 * Title: SftpUtil.java
 * @author: Jack
 * @create: 2018-07-19
 *
 */
public class SftpUtil {
	
	private static Properties properties=PropertiesUtil.loadProperties("pospay-config.properties");
	private static String host=properties.getProperty("sftp.host");
	private static String port=properties.getProperty("sftp.port");
	private static String username=properties.getProperty("sftp.username");
	private static String password=properties.getProperty("sftp.password");
	private static String location=properties.getProperty("sftp.location");
	
	private static Session session = null;
    private static Channel channel = null;

	private static final Logger LOG = Logger.getLogger(SftpUtil.class.getName());

	//获取Sftp连接
    public static ChannelSftp getChannel(int timeout){
    	try{
        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(username, host, Integer.valueOf(port)); // 根据用户名，主机ip，端口获取一个Session对象
        LOG.debug("Session created.");
        session.setPassword(password); // 设置密码
        
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        LOG.debug("Session connected.");

        LOG.debug("Opening Channel.");
        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        LOG.debug("Connected successfully to ftpHost = " + host + ",as ftpUserName = " + username+ ", returning: " + channel);
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        return (ChannelSftp) channel;
    }

    public static void closeChannel(){
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
    
    public static void downloadFile(String fileName,String filePath)
    {
    	ChannelSftp chSftp = getChannel(60000);
		try{
        String sftpPath = location+"/"+fileName;
        System.out.println("sftp-path:"+sftpPath);
        
        SftpATTRS attr = chSftp.stat(sftpPath);
        long fileSize = attr.getSize();
        
        String dst = filePath;
        
        System.out.println("local-path:"+dst);
            
        chSftp.get(sftpPath, dst, new FileProgressMonitor(fileSize)); // 代码段1
            
        } catch (Exception e) {
            LOG.info("file is not exist");
        } finally {
            chSftp.quit();
            closeChannel();
        }
    }
	
	public static void main(String[] args) {
		
		URL url = MainTask.class.getResource("");
		String path=url.getPath();
		System.out.println(path);
		path=path.substring(0,path.indexOf("WEB-INF"));
		path+="/upload"+"/checkbill/";
		System.out.println(path);
		path+="checkbill_2018-07-17.txt";
	
		downloadFile("checkbill_2018-07-17.txt",path);
		
	}

}
