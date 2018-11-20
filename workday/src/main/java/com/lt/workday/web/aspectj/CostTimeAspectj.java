package com.lt.workday.web.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class CostTimeAspectj {
	
	private Long startTime;
	
	@Pointcut(value = "execution(* com.lt.workday.web.rest..*Controller.*(..))")
	public void pointCut() {
		
	}
	
	@Before(value = "pointCut()")
	public void doBefore(JoinPoint joinPoint) {
		startTime = System.nanoTime();
	}
	
	@After(value = "pointCut()")
	public void doAfter(JoinPoint joinPoint) {
		Long endTime = System.nanoTime();
		log.info("请求执行耗时：[" + (endTime - startTime) + "]");
	}

}
