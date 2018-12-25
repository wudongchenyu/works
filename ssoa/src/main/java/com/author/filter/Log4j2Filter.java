package com.author.filter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.apache.logging.log4j.web.Log4jServletFilter;

@WebFilter(filterName = "log4jServletFilter", 
dispatcherTypes = {
		DispatcherType.ASYNC, 
		DispatcherType.REQUEST,
		DispatcherType.INCLUDE,
		DispatcherType.FORWARD,
		DispatcherType.ERROR}, 
asyncSupported = true, 
urlPatterns = "/*",
initParams = {
		@WebInitParam(name = "encoding", value = "utf-8")
})
public class Log4j2Filter extends Log4jServletFilter{
	
	public Log4j2Filter() {
		System.out.println("Log4j2Filter");
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}

}
