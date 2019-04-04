package com.example.dubbo.demo.api;

import java.util.List;

import dubbo.demo.model.entity.Student;

/**
 * Demo 接口定义
 *
 * @author jeftom
 * @date 2019-03-23 22:35
 * @since 1.0.0
 */
public interface DemoApi {
	String sayHello(String name);
	void add(Student student);
	List<Student> getAll();
}
