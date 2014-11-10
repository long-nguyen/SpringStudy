package com.tutorial;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class TestBeanPostProcessor implements BeanPostProcessor{

	@Override
	public Object postProcessAfterInitialization(Object bean, String arg1)
			throws BeansException {
		System.out.print("Post init process");
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String arg1)
			throws BeansException {
		System.out.print("after init process");
		return bean;
	}

}
