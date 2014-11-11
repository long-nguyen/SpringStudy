package com.tutorial.customevent;

import org.springframework.context.ApplicationListener;

/***
 * Đay là thằng bean để lắng nghe custom event
 * @author long.nguyen-tien
 *
 */
public class CustomEventHandler implements ApplicationListener<CustomEvent>{

	@Override
	public void onApplicationEvent(CustomEvent arg0) {
		System.out.println(arg0.toString());
	}

}
