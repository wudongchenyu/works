package com.taikang.app.base.config;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.IntrospectorCleanupListener;

import com.taikang.app.base.listener.SpringRootAppInitListener;

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
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses()
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{RootConfiguration.class};
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getServletConfigClasses()
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{
			MvcConfiguration.class, 
			PrimaryDataSourceConfiguration.class, 
			MyBatisConfiguration.class,
			RedisConfiguration.class,
			Swagger2Config.class,
			FeignClientConfiguration.class};
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#getServletMappings()
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
	
	@Override
	protected ApplicationContextInitializer<?>[] getRootApplicationContextInitializers() {
		SpringRootAppInitListener springRootAppInitListener = new SpringRootAppInitListener(); 
		return new ApplicationContextInitializer[]{springRootAppInitListener};
	}
	
	@Override
	protected void registerContextLoaderListener(ServletContext servletContext) {
		servletContext.addListener(RequestContextListener.class);
		servletContext.addListener(IntrospectorCleanupListener.class);
	}
	
	@Override
	protected Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
		return super.registerServletFilter(servletContext, filter);
	}

}
