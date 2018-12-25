package com.author.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.web.Log4jServletContextListener;
import org.apache.logging.log4j.web.Log4jWebSupport;

@WebListener(value = "log4j2Listener")
public class Log4j2Listener extends Log4jServletContextListener{
	
	private ServletContext servletContext;
	
	public Log4j2Listener() {
		System.out.println("Log4j2Listener");
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		servletContext = event.getServletContext();
		servletContext.setInitParameter(Log4jWebSupport.LOG4J_CONFIG_LOCATION, "classpath:log4j2-spring.xml");
		super.contextInitialized(event);
	}

}
