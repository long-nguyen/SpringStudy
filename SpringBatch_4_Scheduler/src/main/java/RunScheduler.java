package main.java;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This is the main action of the scheduler
 * We need job launcher & job property, but we will try using autowired, instead of using property in xml file
 * (thay vì dùng <property></property>, thì mình dùng autowired cho ngắn, chú ý phải có annotation ở xml thì mới gọi được
 * @author long.nguyen-tien
 *
 */
//generic stereotype for any Spring-managed component
@Component
public class RunScheduler {
	// Use Autowired to bind object in context
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;

	public void run() {
		try {
			//P.S JobParamater need to be unique each time a batch job to run, for testing purpose, we just pass in a new Date() everything running the job.
			String dateParam = new Date().toString();
			JobParameters param = new JobParametersBuilder().addString("date",
					dateParam).toJobParameters();

			System.out.println(dateParam);

			JobExecution execution = jobLauncher.run(job, param);
			System.out.println("Exit Status : " + execution.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
