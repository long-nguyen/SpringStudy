package com.tutorial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;










import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class StudentJDBC implements StudentDAO{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	//Important, need
	private PlatformTransactionManager transactionManager;
	
	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	@Override
	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/***
	 * We will run this create method in programmatic way. Means manually call commit and rollback
	 */
	@Override
	public void create(String name, Integer age, Integer marks, Integer year) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			//TODO: Change sql to wrong command to see rollback result
			String sql1 = "insert into student(name, age) values(?,?)";
			jdbcTemplate.update(sql1, name, age);
			//add mark for first student
			String sql2 = "insert into mark(sid, marks, year) values(?,?,?)";
			jdbcTemplate.update(sql2, 1, marks, year);
			
			transactionManager.commit(status);
			System.out.println("Just commited, status:"+ status.isCompleted());
		}catch(DataAccessException e) {
			transactionManager.rollback(status);
			System.err.println("Error, rollback status:" + status.isCompleted());
			e.printStackTrace();
		}
	}

	@Override
	public List<Student> listStudents() {
		String sql = "select * from student";
		List<Student> list = jdbcTemplate.query(sql, new StudentMapper());
		return list;
	}

	
	class StudentMapper implements RowMapper<Student> {

		@Override
		public Student mapRow(ResultSet rs, int arg1) throws SQLException {
			Student student = new Student();
			student.setId(rs.getInt("id"));
			student.setName(rs.getString("name"));
			student.setAge(rs.getInt("age"));
			student.setMarks(rs.getInt("marks"));
			student.setYear(rs.getInt("year"));
			return student;
		}
		
	}



}
