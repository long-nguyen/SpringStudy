package com.tutorial.annotation;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
/**
 * Mục đích là test các anotation cho việc injection
 * Bar không hề có khai báo về property foo trong xml, mà lại khai báo bằng annotation trong java code
 * 
 * @author long.nguyen-tien
 *
 */

public class Bar {
	//Có 2 cách để inject, là dùng autowired vào resource
	
	//Ta muôn nhét object kiểu Foo vào bar , nên ta dùng @Autowired. thì nó tìm trong context(xml) xem có bean nào kiểu foo ko thì gán vào.
	//Nhưng trong xml lại có 2 cái bean như vậy, nên ta dùng thêm @Qualifier
	@Autowired
	@Qualifier(value="secondaryFoo")
	private Foo foo;
	
	//Đối với cái foo2 này, ta tìm trong context có bean nào có name là foo thì nhét vào
	@Resource(name="foo")
	private Foo foo2;
	
	public void setFoo(Foo foo) {
		this.foo = foo;
	}
	
	public void printFooName() {
		System.out.println(foo.getName());
		System.out.println(foo2.getName());
	}
}
