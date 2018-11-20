package com.lt.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.lt.base")
public class OutrestApplication {
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(OutrestApplication.class, args);
	}
	
}
