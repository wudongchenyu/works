package com.taikang.app.base.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.web.Log4jServletContextListener;
import org.apache.logging.log4j.web.Log4jWebSupport;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.IntrospectorCleanupListener;

import lombok.extern.slf4j.Slf4j;

/**
 * WebAppInitializer
 * @author wudon
 *
 */
@Order(1)
@Slf4j
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
	
	public WebAppInitializer() {
		log.info("WebAppInitializer():" + System.nanoTime());
		log.info("WebAppInitializer():" + System.nanoTime());
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses()
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		log.info("WebAppInitializer.getRootConfigClasses:" + System.nanoTime());
		return new Class<?>[]{RootConfiguration.class};
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getServletConfigClasses()
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		log.info("WebAppInitializer.getServletConfigClasses:" + System.nanoTime());
		return new Class<?>[]{
			MvcConfiguration.class, 
			Swagger2Config.class,
			RestTemplateConfig.class};
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#getServletMappings()
	 */
	@Override
	protected String[] getServletMappings() {
		log.info("WebAppInitializer.getServletMappings:" + System.nanoTime());
		return new String[]{"/"};
	}
	
	@Override
	protected void registerContextLoaderListener(ServletContext servletContext) {
		log.info("WebAppInitializer.registerContextLoaderListener:" + System.nanoTime());
		String loggerPath = "classpath:log4j2-spring.xml";
		servletContext.setInitParameter(Log4jWebSupport.LOG4J_CONFIG_LOCATION, loggerPath);
		log.info(servletContext.getInitParameter(Log4jWebSupport.LOG4J_CONFIG_LOCATION));
		servletContext.addListener(Log4jServletContextListener.class);
		servletContext.addListener(RequestContextListener.class);
		servletContext.addListener(IntrospectorCleanupListener.class);
		
		DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        delegatingFilterProxy.setTargetBeanName("ssoTokenFilter");
        delegatingFilterProxy.setTargetFilterLifecycle(true);
        FilterRegistration filterRegistration = servletContext.addFilter("ssoTokenFilter",delegatingFilterProxy);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/api/*");
		
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		log.info("WebAppInitializer.onStartup:" + System.nanoTime());
		super.onStartup(servletContext);
	}
}
