package com.example.dubbo.demo.service;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

//@EnableDubboConfig
//@DubboComponentScan("com.example.dubbo.demo.service.impl")
@MapperScan("com.example.dubbo.demo.service.mapper")
@SpringBootApplication
@ImportResource(locations="classpath:dubbo-config.xml")
public class DubboDemoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboDemoServiceApplication.class, args);
	}

}
