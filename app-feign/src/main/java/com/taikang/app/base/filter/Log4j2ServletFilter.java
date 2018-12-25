package com.taikang.app.base.filter;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebFilter;

import org.apache.logging.log4j.web.Log4jServletFilter;

@WebFilter(filterName = "log4jServletFilter", 
	dispatcherTypes = {
			DispatcherType.ASYNC, 
			DispatcherType.REQUEST,
			DispatcherType.INCLUDE,
			DispatcherType.FORWARD,
			DispatcherType.ERROR}, 
	asyncSupported = true, 
	urlPatterns = "/*")
public class Log4j2ServletFilter extends Log4jServletFilter{
	
	public Log4j2ServletFilter() {
		System.out.println("Log4j2ServletFilter" + System.nanoTime());
	}

}
