package com.tutorial;

import java.util.List;

import javax.sql.DataSource;


public interface StudentDAO {
	
	/***
	 * Dùng để khởi tạo tài nguyên database, ví dụ: connection
	 * @param ds
	 */
	public void setDataSource(DataSource ds);	
	public void create(String name, Integer age, Integer marks, Integer year);
	public List<Student> listStudents();
}
