package com.tutorial;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

	public static void main(String[] args) {
		//use application context to load configuration file, init all objects in configuration file, let check the Beans.xml
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
	      StudentJDBC studentJDBC = 
	    	      (StudentJDBC)context.getBean("studentJDBC");

	      studentJDBC.create("Zara", 11, 99, 2010);
	}
}
