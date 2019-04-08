package com.example.dubbo.demo.web.service;

import com.example.dubbo.demo.api.DemoApi;

import dubbo.demo.model.entity.Student;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消费者服务层
 *
 * @author chenlong
 * @date 2019-03-24 00:49
 * @since 1.0.0
 */
@Service
public class DemoService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoService.class);

	@Autowired
	private DemoApi demoApi;

	public String sayHello(String name) {
		return demoApi.sayHello(name);
	}
	
	public List<Student> getAll(){
		return demoApi.getAll();
	}
	
	public void add(Student student){
		demoApi.add(student);
	}
}
