package com.lt.workday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.lt.workday")
public class WorkdayApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkdayApplication.class, args);
	}
}
