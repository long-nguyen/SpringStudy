package main.java;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * When we run the application(main), 
 * the scheduler is run automatically
 * Mục tiêu của bài này: 
 * + tạo 1 scheduler, cứ 5 giây 1 lần là nó tự gọi hàm run của RunScheduler.
 * + scheduler này chỉ start ban đầu 1 lần, bởi hàm main
 * + Đọc dữ liệu từ mysql
 * + Ghi ra file xml
 * @author long.nguyen-tien
 *
 */
public class Main {
	public static void main(String[] args) {
		String [] springConfig = {"main/resources/spring/jobs.xml", "main/resources/spring/job-manager.xml"};
		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
	}
}
