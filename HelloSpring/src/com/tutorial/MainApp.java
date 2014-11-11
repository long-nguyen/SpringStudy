package com.tutorial;

import java.util.logging.Logger;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

	static Logger log = Logger.getLogger("Test logger");

	public static void main(String[] args) {
		// use application context to load configuration file, init all objects
		// in configuration file, let check the Beans.xml
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"Beans.xml");
		// Use application's bean - Singleton
		// HelloWorld obj = (HelloWorld)context.getBean("helloWorld");
		// obj.setMessage("ABC");
		// obj.getMessage();
		//
		// HelloWorld objB = (HelloWorld)context.getBean("helloWorld");
		// objB.getMessage();
		//
		//
		// ///Sample 2 - prototype
		// HelloWorld objC =
		// (HelloWorld)context.getBean("helloWorld-prototype");
		// obj.setMessage("ABC");
		// obj.getMessage();
		//
		// HelloWorld objD =
		// (HelloWorld)context.getBean("helloWorld-prototype");
		// objD.getMessage();

		// Sample 3 - Bean lifecycle
		// TestLifeCycle objE = (TestLifeCycle)context.getBean("testLifeCycle");
		// objE.getPropety();
		// //NOTE: set bean = null has no meaning
		// // objE = null;
		// context.registerShutdownHook();

		// Sample 4- Test inheritance
		// test-inheritance1 là abstract nên ko thể init
		TestInheritance inhe2 = (TestInheritance) context
				.getBean("test-inherit-2");
		inhe2.getMessage1();
		inhe2.getMessage2();
		context.registerShutdownHook();

		// NOTE: Beans are always retain in container regardless of we call it or not

		// Sample 5: Log
		log.info("End of app");

	}
}
