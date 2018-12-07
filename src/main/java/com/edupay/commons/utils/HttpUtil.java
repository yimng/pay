package com.edupay.commons.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 仅供测试用模拟GET/POST提交
 * 
 * Title 
 * @author: dongminghao
 * @date: 2018年4月19日
 */
public class HttpUtil {

	/**
	 * 连接超时
	 */
	private static int connectTimeOut = 20000;

	/**
	 * 读取数据超时
	 */
	private static int readTimeOut = 30000;

	/**
	 * 请求编码
	 */
	private static String requestEncoding = "UTF-8";
	
	private static HttpUtil httpUtil;
	
	public static HttpUtil getInstance() {
        if (httpUtil == null) {
        	httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

	/**
	 * @return 连接超时(毫秒)
	 * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
	 */
	public static int getConnectTimeOut() {
		return HttpUtil.connectTimeOut;
	}

	/**
	 * @return 读取数据超时(毫秒)
	 * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
	 */
	public static int getReadTimeOut() {
		return HttpUtil.readTimeOut;
	}

	/**
	 * @return 请求编码
	 * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
	 */
	public static String getRequestEncoding() {
		return requestEncoding;
	}
	
	/**
	 * @param connectTimeOut 连接超时(毫秒)
	 * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
	 */
	public static void setConnectTimeOut(int connectTimeOut) {
		HttpUtil.connectTimeOut = connectTimeOut;
	}

	/**
	 * @param readTimeOut 读取数据超时(毫秒)
	 * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
	 */
	public static void setReadTimeOut(int readTimeOut) {
		HttpUtil.readTimeOut = readTimeOut;
	}

	/**
	 * @param requestEncoding 请求编码
	 * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
	 */
	public static void setRequestEncoding(String requestEncoding) {
		HttpUtil.requestEncoding = requestEncoding;
	}
	
	/**
	 * GET请求
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @return
	 */
	public String doGet(String reqUrl, Map<?, ?> parameters) {
		return doGet(reqUrl, parameters, null, "UTF-8", "?", "&");
	}
	
	/**
	 * GET请求
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @param recvEncoding HTTP响应的字符串
	 * @return
	 */
	public String doGet(String reqUrl, Map<?, ?> parameters, String recvEncoding) {
		return doGet(reqUrl, parameters, null, recvEncoding, "?", "&");
	}
	
	/**
	 * GET请求
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @param headerParamters HTTP的Header
	 * @param recvEncoding HTTP响应的字符串
	 * @return
	 */
	public String doGet(String reqUrl, Map<?, ?> parameters, Map<String, Object> headerParamters, String recvEncoding) {
		return doGet(reqUrl, parameters, headerParamters, recvEncoding, "?", "&");
	}
	
	
	/**
	 * GET请求
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @param headerParamters HTTP的Header
	 * @param recvEncoding HTTP响应的字符串
	 * @param passParametSymbol 如www.baidu.com?a=1的"?"
	 * @param connectSymbol  如www.baidu.com?a=1&b=2的"&"
	 * @return
	 */
	public String doGet(String reqUrl, Map<?, ?> parameters, Map<String, Object> headerParamters, String recvEncoding, String passParametSymbol,String connectSymbol){
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();) {
				Entry<?, ?> element = (Entry<?, ?>) iter.next();
				if(element.getValue()==null){
					continue;
				}
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(), HttpUtil.requestEncoding));
				params.append(connectSymbol);
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}
			URL url = new URL(reqUrl+passParametSymbol+params);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("GET");
			// Set header
			if(headerParamters!=null){
				for(String key : headerParamters.keySet()){
					url_con.addRequestProperty(key, headerParamters.get(key).toString());
				}
			}
			url_con.setConnectTimeout(HttpUtil.connectTimeOut);//（单位：毫秒）连接超时
			url_con.setReadTimeout(HttpUtil.readTimeOut);//（单位：毫秒）读操作超时
//			url_con.setDoOutput(true);
//			byte[] b = params.toString().getBytes();
//			url_con.getOutputStream().write(b, 0, b.length);
//			url_con.getOutputStream().flush();
//			url_con.getOutputStream().close();
			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer temp = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				temp.append(tempLine);
				temp.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = temp.toString();
			rd.close();
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return responseContent;
	}
	
	/**
	 * POST请求
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @return
	 */
	public String doPost(String reqUrl, Map<String, String> parameters) {
		HttpResponse httpResponse = doPostAll(reqUrl, parameters, null, "UTF-8");
		if(httpResponse==null){
			return null;
		}
		return httpResponse.getContent();
	}

	/**
	 * POST请求
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @param recvEncoding HTTP响应的字符串
	 * @return
	 */
	public String doPost(String reqUrl, Map<String, String> parameters, String recvEncoding) {
		HttpResponse httpResponse = doPostAll(reqUrl, parameters, null, recvEncoding);
		if(httpResponse==null){
			return null;
		}
		return httpResponse.getContent();
	}
	
	/**
	 * POST请求
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @param headerParamters HTTP的Header
	 * @param recvEncoding HTTP响应的字符串
	 * @return
	 */
	public String doPost(String reqUrl, Map<String, String> parameters, Map<String, Object> headerParamters, String recvEncoding) {
		HttpResponse httpResponse = doPostAll(reqUrl, parameters, headerParamters, recvEncoding);
		if(httpResponse==null){
			return null;
		}
		return httpResponse.getContent();
	}
	
	/**
	 * POST请求
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @param headerParamters HTTP的Header
	 * @param recvEncoding HTTP响应的字符串
	 * @return
	 */
	public HttpResponse doPostAll(String reqUrl, Map<String, String> parameters, Map<String, Object> headerParamters, String recvEncoding) {
		HttpURLConnection url_con = null;
		HttpResponse httpResponse = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();) {
				Entry<?, ?> element = (Entry<?, ?>) iter.next();
				if(element.getValue()==null){
					continue;
				}
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(), HttpUtil.requestEncoding));
				params.append("&");
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}

			URL url = new URL(reqUrl);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			// Set header
			if(headerParamters!=null){
				for(String key : headerParamters.keySet()){
					url_con.addRequestProperty(key, headerParamters.get(key).toString());
				}
			}
			url_con.setConnectTimeout(HttpUtil.connectTimeOut);//（单位：毫秒）连接超时
			url_con.setReadTimeout(HttpUtil.readTimeOut);//（单位：毫秒）读操作超时
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();
			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			String responseContent = tempStr.toString();
			httpResponse = new HttpResponse();
			httpResponse.setContent(responseContent);
			httpResponse.setHeader(url_con.getHeaderFields());
			httpResponse.setUrl(url_con.getURL());
			rd.close();
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return httpResponse;
	}
	
	/**
	 * 获取本机IP V4地址
	 * 
	 * @return
	 * @Author: Jack
	 * @Date: 2018/09/05
	 */
	public static String getLocalIp() {
        // TODO Auto-generated method stub
        InetAddress ia=null;
        try {
            ia=ia.getLocalHost();
            return ia.getHostAddress();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}

class HttpResponse {

	private String content;
	
	private Map<String,List<String>> header;
	
	private URL url;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, List<String>> getHeader() {
		return header;
	}

	public void setHeader(Map<String, List<String>> header) {
		this.header = header;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
}