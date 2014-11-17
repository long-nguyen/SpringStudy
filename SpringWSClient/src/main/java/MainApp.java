package main.java;

import java.util.logging.Logger;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//http://myshittycode.com/2013/10/01/using-spring-web-services-and-jaxb-to-invoke-web-service-based-on-wsdl/

public class MainApp {

	static Logger log = Logger.getLogger("Test logger");

	public static void main(String[] args) {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"Beans.xml");
		SpringWSClient client = context.getBean(SpringWSClient.class);
		System.out.println(client.sayHello("long"));
		log.info("End of app");

	}
}
