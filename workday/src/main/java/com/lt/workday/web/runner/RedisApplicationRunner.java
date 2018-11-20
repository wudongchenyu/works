package com.lt.workday.web.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.lt.workday.web.service.WorkDayService;

@Component
@Order(1)
public class RedisApplicationRunner implements ApplicationRunner{
	
	private @Autowired WorkDayService workDayService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		workDayService.saveWorkDayToRedis();
	}

}
