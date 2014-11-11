package com.tutorial.java_configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Cái @Configuration để cho biết là class này được quản lý bởi Spring container
 * nó có tác dụng tạo ra bean
 * Cái @Bean là để khai báo 1 bean, tương tự trong xml 
 * @author long.nguyen-tien
 *
 */
@Configuration
public class HelloWorldConfig {
	
	@Bean(initMethod = "init", destroyMethod ="cleanup")
	@Scope("prototype")
	public HelloWorld helloWorld(){
		return new HelloWorld();
	}

}
