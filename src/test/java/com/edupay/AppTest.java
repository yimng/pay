package com.edupay;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		context.start();
		//MessageService service = (MessageService) context.getBean("messageService");
		//service.selectMessageTextById("1");

	}

}
