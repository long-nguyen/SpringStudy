package com.tutorial.java_configuration;

public class HelloWorld {
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public void getMessage() {
		System.out.println("Your Message : " + message);
	}
	
	public void init() {
		System.out.println("Helloworld is inited");
	}
	
	public void cleanup() {
		System.out.println("Helloworld is cleaned");
	}
}
