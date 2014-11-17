package main.java;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***
 * This sample is to test:
 * + Read all records from mySQL database
 * + Report 
 * + write it to xml file
 * @author long.nguyen-tien
 *
 */
public class Main {
	public static void main(String[] args) {
		String [] springConfig = {"main/resources/spring/jobs.xml", "main/resources/spring/job-manager.xml"};
		
		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		JobLauncher jobLauncher = (JobLauncher)context.getBean("jobLauncher");
		Job job = (Job)context.getBean("job1");
		
		try{
			JobExecution execution = jobLauncher.run(job, new JobParameters());
			System.out.println("Exit status:" + execution.getStatus());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
