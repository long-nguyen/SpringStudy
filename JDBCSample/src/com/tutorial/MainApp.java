package com.tutorial;

import java.util.List;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

	public static void main(String[] args) {
		// use application context to load configuration file, init all objects
		// in configuration file, let check the Beans.xml
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"Beans.xml");
		StudentJDBC studentJdbc = (StudentJDBC) context.getBean("studentJDBC");
		studentJdbc.create("Zara", 11);
		studentJdbc.create("Nuha", 2);
		studentJdbc.create("Ayan", 15);

		List<Student> listData = studentJdbc.listStudents();
		for (Student record : listData) {
			System.out.print("ID : " + record.getId());
			System.out.print(", Name : " + record.getName());
			System.out.println(", Age : " + record.getAge());
		}

		studentJdbc.update(2, 20);
		Student student = studentJdbc.getStudent(2);
		System.out.print("ID : " + student.getId());
		System.out.print(", Name : " + student.getName());
		System.out.println(", Age : " + student.getAge());

	}
}
