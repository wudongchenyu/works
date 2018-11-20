package com.lt.base.web.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
public class SqlCostAspectj {
	
	private Long startTime;
	
	@Pointcut(value = "execution(* com.lt.base.mybatis..*Mapper.*(..))")
	public void pointCut() {
		
	}
	
	@Before(value = "pointCut()")
	public void doBefore(JoinPoint joinPoint) {
		startTime = System.nanoTime();
	}
	
	@After(value = "pointCut()")
	public void doAfter(JoinPoint joinPoint) {
		Long endTime = System.nanoTime();
		log.info("SQL执行耗时：[" + (endTime - startTime) + "]");
	}

}
