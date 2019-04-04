package com.example.dubbo.demo.service.impl;

import com.example.dubbo.demo.api.DemoApi;
import com.example.dubbo.demo.service.mapper.StudentMapper;

import dubbo.demo.model.entity.Student;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * class description
 *
 * @author jeftom <jeftom@tenwe.com>
 * @date 2019-03-23 23:04
 * @since 1.0.0
 */
@Service
public class DemoApiImpl implements DemoApi {
	
	@Autowired
	private StudentMapper studentMapper;
	/**
	 * 实现 sayHello 接口
	 *
	 * @param name
	 * @return
	 */
	@Override
	public String sayHello(String name) {
		return "Hello, " + name + " (from Spring Boot with dubbo-2.7.1)";
	}

	@Override
	public void add(Student student) {
		// TODO Auto-generated method stub
		int i= 1/0;
		studentMapper.add(student);
	}

	@Override
	public List<Student> getAll() {
		// TODO Auto-generated method stub
		return studentMapper.getAll();
	}
}
