package com.tutorial;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

	public static void main(String[] args) {
		// use application context to load configuration file, init all objects
		// in configuration file, let check the Beans.xml
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"Beans.xml");
		//In case of using Declarative approach, we get JDBC bean and cast it to interface level( default?). You can cast it to StudentJDBC, but you need to declare in XML
		//<aop:config proxy-target-class="true">

		StudentDAO studentJDBC = (StudentDAO) context.getBean("studentJDBC");
		studentJDBC.create("Long", 12, 80, 2010);
	}
}
