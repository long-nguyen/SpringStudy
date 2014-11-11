package com.tutorial;

import java.util.List;

import javax.sql.DataSource;


public interface StudentDAO {
	
	/***
	 * Dùng để khởi tạo tài nguyên database, ví dụ: connection
	 * @param ds
	 */
	public void setDataSource(DataSource ds);	
	public Student getStudent(Integer id);
	public void create(String name, Integer id);
	public List<Student> listStudents();
	public void delete(Integer id);
	public void update(Integer id, Integer age);
}
