package com.example.dubbo.demo.service.mapper;

import java.util.List;

import dubbo.demo.model.entity.Student;

public interface StudentMapper {

	void add(Student student);
	List<Student> getAll();
}
