package com.tutorial.customevent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class CustomEventPublisher implements ApplicationEventPublisherAware{

	private ApplicationEventPublisher publisher; 
	
	public CustomEventPublisher() {
	}
	
	public CustomEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher arg0) {
		this.publisher = arg0;
	}
	
	public void publish() {
		CustomEvent event = new CustomEvent(this);
		publisher.publishEvent(event);
	}



}
