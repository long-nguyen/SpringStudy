package com.tutorial;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tutorial.customevent.CustomEventPublisher;

/***
 * Event: Contex thì có nhiều event, ví dụ như refresh, start,stop, closed..bean có thể lắng nghe các event đó
 * 
 * @author long.nguyen-tien
 *
 */
public class MainApp {

	public static void main(String[] args) {
		//use application context to load configuration file, init all objects in configuration file, let check the Beans.xml
//		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
//		context.start();
//		HelloWorld obj = (HelloWorld)context.getBean("helloWorld");
//		obj.getMessage();
//		context.stop();
		
		//Sample 2: custom event
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		CustomEventPublisher cvp = (CustomEventPublisher)context.getBean("customEventPublisher");
		cvp.publish();
		cvp.publish();
		
	}
}
