package com.tutorial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;




import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class StudentJDBC implements StudentDAO{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Student getStudent(Integer id) {
		String sql = "select * from student where id = ?";
		Student std = jdbcTemplate.queryForObject(sql, new Object[]{id}, new StudentMapper());
		return std;
	}

	@Override
	public void create(String name, Integer age) {
		String sql = "insert into student(name, age) values(?,?)";
		int res= jdbcTemplate.update(sql, name, age);
		System.out.println("Created success?"+ (res==1));
	}

	@Override
	public List<Student> listStudents() {
		String sql = "select * from student";
		List<Student> list = jdbcTemplate.query(sql, new StudentMapper());
		return list;
	}

	@Override
	public void delete(Integer id) {
		String sql = "delete from student where id = ? ";
		int res = jdbcTemplate.update(sql, id);
		System.out.println("Created success?"+ (res==1));
	}

	@Override
	public void update(Integer id, Integer age) {
		String sql = "update student set age = ? where id = ? ";
		int res = jdbcTemplate.update(sql, age, id);
		System.out.println("Created success?"+ (res==1));	
	}
	
	
	class StudentMapper implements RowMapper<Student> {

		@Override
		public Student mapRow(ResultSet rs, int arg1) throws SQLException {
			Student student = new Student();
			student.setId(rs.getInt("id"));
			student.setName(rs.getString("name"));
			student.setAge(rs.getInt("age"));
			return student;
		}
		
	}

}
