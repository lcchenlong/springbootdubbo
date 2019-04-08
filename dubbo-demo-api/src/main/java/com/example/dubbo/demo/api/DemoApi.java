package com.example.dubbo.demo.api;

import java.util.List;

import dubbo.demo.model.entity.Student;

/**
 * Demo 接口定义
 *
 * @author longlong
 * @date 2019-03-23 22:35
 * @since 1.0.0
 */
public interface DemoApi {
	/**
	 * 测试方法
	 * @param name
	 * @return
	 */
	String sayHello(String name);
	
	/**
	 * 添加学生
	 * @param student
	 */
	void add(Student student);
	
	/**
	 * 获取所有学生类列表
	 * @return
	 */
	List<Student> getAll();
}
