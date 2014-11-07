package com.tutorial;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

	public static void main(String[] args) {
		//use application context to load configuration file, init all objects in configuration file, let check the Beans.xml
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		//Use application's bean
		HelloWorld obj = (HelloWorld)context.getBean("helloWorld");
		obj.getMessage();
	}
}
