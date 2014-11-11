import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


/**
 * This project is a Spring batch sample which use only java for declaration
 * And do not use XML
 * @author long.nguyen-tien
 *
 */

/**
 * Again: A BATCH is NOT a LOOP. Batch is a series of task running without human interaction(first time only).
 * A batch can loop again if you tell it to loop
 * @author long.nguyen-tien
 *http://spring.io/guides/gs/batch-processing/
 */
//ComponentScan will find BatchConfigurations
@ComponentScan
@EnableAutoConfiguration
public class Main {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Main.class, args);
		List<Person> result = context.getBean(JdbcTemplate.class).query("select * from people", new RowMapper<Person>(){

			@Override
			public Person mapRow(ResultSet rs, int arg1) throws SQLException {
				return new Person(rs.getString(1), rs.getString(2));
			}
			
		});
		
		for (Person person : result) {
			System.out.println("Found <" + person + "> in the database.");
		}	
	}
}
