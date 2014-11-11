package com.tutorial;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tutorial.annotation.Bar;
import com.tutorial.java_configuration.HelloWorld;
import com.tutorial.java_configuration.HelloWorldConfig;

public class MainApp {
	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext("Bean.xml");
		
		//Sample: Kết nối 2 beans: Bean nọ nằm trong bean kia
//		TextEditor te = (TextEditor) context.getBean("textEditor");
//		te.spellCheck();
		
		//Sample 2: Bean mà có list, map...
//		BeanList bean2 = (BeanList)context.getBean("beanList");
//		bean2.getAddressList();
//		bean2.getAddressMap();
//		bean2.getBeanList();
		
		//Sample 3: Inject by annotation
//		Bar bar = (Bar)context.getBean("bar");
//		bar.printFooName();
		
		//Sample 4: Java based configuration
		ApplicationContext context = new AnnotationConfigApplicationContext(HelloWorldConfig.class);
		HelloWorld helloWorld = context.getBean(HelloWorld.class);
		helloWorld.setMessage("hehe");
		helloWorld.getMessage();
		
	}
}
