package com.tutorial;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class TestLifeCycle implements DisposableBean, InitializingBean{
	private String myProperty;

	public void setProperty(String message) {
		this.myProperty = message;
	}

	public void getPropety() {
		System.out.println("Your property : " + myProperty);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Bean is inited");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("Bean is destroyed");
	}

}
