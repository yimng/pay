package com.edupay.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016091900545990";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCor8kJsZhkLBYWAX8tuMYZlnhwmz6k2FT+OEXiwG8zUC7oiqN3jrAXEzpvpmsuej4g3fqw/L11Mez1Hl2IRPHUE0b5Y85H1t0f2pGi0OFKUcIkPMxeAfSV9kl24MRg0QWJv3FRsXU2qlnjcJqxh7yjzAg+dfS9hk13MKg3gsmfqCGaRtSYlbf0WHQbo750Tnx19q7uIT1vPflSzbFiT9KPy/5y1ehz84JZKJtWDZc5OouxBkyCltuwRu254fhCSBPC0QuqcVQVDa9fSZseL8oYKIDO4jEhSZji+O1vahx5ejm2Fj86rJKgarztKsdcuiy5uBl2OVzLHp3V/UyXVNl5AgMBAAECggEAMJE0C7NzUdwTm9BggpSBjNpy4NEBz0pt5ifjjy6ilZ6Pcup/uCt/7ZTerUi38VDG1IW06QJLthNiywwsF9Dp44qNU751pN9rGQHADqWfR9uUTwSmPqbHeenIKw7523oy4v8YrFGwksWnTcGc+GJBYNOcyAcEKREi+p8qv5jLXkj8cvx4BzGZlxmHmlO4ICw9xiLnRh8vdbLorNYF9NNtbn60W/ExjUjSIu7jV1AjHMzZI8FSYXKl4/eyY27gk3O8gog9YGuy2LOMA2iqbjP1IrUwOooQL+tTKTHYUnLIdvyyw32Mv8qODlT5IPC37S65g+R8B14ZibqQ5jk99Sio9QKBgQDtWm/xcFFNWxNbw0lyEyhL3NGMF2sGzzhyyKIsLoVXJMK85Xb9hReUJFvAkN1BG7Vb/dTm2AUu4WUg8rvashL36sZcalA4xvZhyveMoyWlM0tQuyMCPRW84cDOpV7rUieRxVyWjU0QwHGe1DZzPJ3UydNalor3UdU4QPDjyG8MIwKBgQC18FmibsEIyHf56nC0jNSmynBtur5VDtVsthBm+gC/XSQ3U8wq+jkvlw3Bp5Qhkf7UtcA7JlQyhLO4hTHrOXRRqVVKv8TOIRVMiQl15E1u1wVja1G9dMEiQytfpnEfrqBpxdJHuKE3KWYHbYVgrSyFDaV/Cr3rJ2uvrPqpOE5/swKBgGTATeJTxf8rGNWL2OJWZXWGX2CzFBhI3/JBXi2Y9bre/NbIJLFKzI5yvIvKMlEP3PUWk0gGDHUw8geBYeJcUpbwDpFuA22Gb1FloZ0OJ82vL1ouhZbOrSqHPZu6MMd9ERTcjQfPJRZmShScihxyb6DCpze9zXFcSEDfuCFIlHK3AoGBAKk/y8KFUWGAS8vbQblUAUafr/MD86dgwz38KEoDa8qZNOWbOKUKXUReOtfZsr1/+RWbdhYQU+ci96VBkuyFaCXHZqbAuq4erJekK37WQQpJUKPp5AJgqIHdBm5WgYm9g1CiTDmYEbEk+9sRETCcKHwl87y5G2+0HkMwoYXl8XBtAoGABSfs8cuIfHsoPsSuz+QmcfEDij3nZaC84hd4UxB+u2xBCkrQ/kcZQ0kBpzKHYyT+q+oj3w7QomgIli0z9l/Hv9lYNtLPxkzg1RXSzW1F4Vy7h1tRd6CPnDh73rjYkYYNZXagl3CW7tZG9zk1nIynL4oeec7s0mSk6Md+c8/bXX0=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
//    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqK/JCbGYZCwWFgF/LbjGGZZ4cJs+pNhU/jhF4sBvM1Au6Iqjd46wFxM6b6ZrLno+IN36sPy9dTHs9R5diETx1BNG+WPOR9bdH9qRotDhSlHCJDzMXgH0lfZJduDEYNEFib9xUbF1NqpZ43CasYe8o8wIPnX0vYZNdzCoN4LJn6ghmkbUmJW39Fh0G6O+dE58dfau7iE9bz35Us2xYk/Sj8v+ctXoc/OCWSibVg2XOTqLsQZMgpbbsEbtueH4QkgTwtELqnFUFQ2vX0mbHi/KGCiAzuIxIUmY4vjtb2oceXo5thY/OqySoGq87SrHXLosubgZdjlcyx6d1f1Ml1TZeQIDAQAB";
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu0YZ9d5LTBR6IZOczFb8r6VSmr/q6R2LHv0AvOAJ1qPhPwZZuNMrdCeS3T0sy0t2xoUtIT6UIdiKekvcElj4yno2mQ62huKBYH05Oo5ukffuzL6XS6jnKsPamrY0eNQ18fib3QSDkDOgIg3zMjv3zLmXgVL6z9Qm00uEar9pKNx0ShJuanLvMouLtght06uvxee5lfdL1yDsPL9Qazv4xdXEwGfVjOxV7Vd7n7NwL6/BRy39vbS0lMdBI4zy4Y44/nklamUy6efiPH/w+03psjz+CrY0jY2h/WNo+p1UU0wF4tgOPdDTvdEmbi1fO67QAYU7hJgkGSOAEIaPWQRsYwIDAQAB";
	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://localhost:8080/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";//"http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:8080/edupay/alipay/return_url";//"http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";//"https://openapi.alipay.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

