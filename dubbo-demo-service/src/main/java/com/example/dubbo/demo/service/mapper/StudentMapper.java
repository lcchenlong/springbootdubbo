package com.example.dubbo.demo.service.mapper;

import java.util.List;

import dubbo.demo.model.entity.Student;

/**
 * 学生数据库操作类Mapper
 * @author chenlong12
 *
 */
public interface StudentMapper {

	/**
	 * 添加学生
	 * @param student
	 */
	void add(Student student);
	/**
	 * 获取所有学生
	 * @return
	 */
	List<Student> getAll();
}
