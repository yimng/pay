package com.edupay.wxpay.sdk;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class WxUtil {
	private final static int CONNECT_TIMEOUT = 5000; // in milliseconds  
    private final static String DEFAULT_ENCODING = "UTF-8";  
  
    public static String postData(String urlStr, String data) {  
        return postData(urlStr, data, null);  
    }  
  
    public static String postData(String urlStr, String data, String contentType) {  
    	OutputStreamWriter writer = null;
    	InputStreamReader read = null;
        BufferedReader reader = null;  
        try {  
            URL url = new URL(urlStr);  
            URLConnection conn = url.openConnection();  
            conn.setDoOutput(true);  
            conn.setConnectTimeout(CONNECT_TIMEOUT);  
            conn.setReadTimeout(CONNECT_TIMEOUT);  
            if (contentType != null)  
                conn.setRequestProperty("content-type", contentType);  
            writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);  
            if (data == null)  
                data = "";  
            writer.write(data);  
            writer.flush();  
            writer.close();
            read = new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING);
            reader = new BufferedReader(read);  
            StringBuilder sb = new StringBuilder();  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                sb.append(line);  
                sb.append("\r\n");  
            }  
            return sb.toString();  
        } catch (IOException e) {  
        } finally {
        	if (reader != null) {
	            try {  
	            	reader.close();
	            } catch (IOException e) {}
        	}
        	if(read!=null) {
        		try {
					read.close();
				} catch (IOException e) {}
        	}
        	if(writer!=null) {
        		try {
					writer.close();
				} catch (IOException e) {}
        	}
        }  
        return null;  
    }  
    
    

	/**

	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。

	 * 

	 * @param strxml

	 * @return

	 * @throws JDOMException

	 * @throws IOException

	 */

	public static Map doXMLParse(String strxml) throws JDOMException, IOException {

		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

 

		if (null == strxml || "".equals(strxml)) {

			return null;

		}

 

		Map m = new HashMap();

 

		InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));

		SAXBuilder builder = new SAXBuilder();

		Document doc = builder.build(in);

		Element root = doc.getRootElement();

		List list = root.getChildren();

		Iterator it = list.iterator();

		while (it.hasNext()) {

			Element e = (Element) it.next();

			String k = e.getName();

			String v = "";

			List children = e.getChildren();

			if (children.isEmpty()) {

				v = e.getTextNormalize();

			} else {

				v = WxUtil.getChildrenText(children);

			}

 

			m.put(k, v);

		}

 

		// 关闭流

		in.close();

 

		return m;

	}

 

	/**

	 * 获取子结点的xml

	 * 

	 * @param children

	 * @return String

	 */

	public static String getChildrenText(List children) {

		StringBuffer sb = new StringBuffer();

		if (!children.isEmpty()) {

			Iterator it = children.iterator();

			while (it.hasNext()) {

				Element e = (Element) it.next();

				String name = e.getName();

				String value = e.getTextNormalize();

				List list = e.getChildren();

				sb.append("<" + name + ">");

				if (!list.isEmpty()) {

					sb.append(WxUtil.getChildrenText(list));

				}

				sb.append(value);

				sb.append("</" + name + ">");

			}

		}

		return sb.toString();

	}

}
