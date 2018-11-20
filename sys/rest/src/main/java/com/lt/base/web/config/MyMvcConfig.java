package com.lt.base.web.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.lt.base.web.inter.WebRequestHandleTimeInterceptor;

@Configuration//这是Java配置类
@ComponentScan(basePackages = {"com.lt.base.web.rest"})//扫描com包
public class MyMvcConfig implements WebMvcConfigurer{
	
	public @Bean WebRequestHandleTimeInterceptor webRequestHandleTimeInterceptor(){
		return new WebRequestHandleTimeInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(webRequestHandleTimeInterceptor());
	}

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	public @Bean MultipartResolver multipartResolver() {
    	CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    	multipartResolver.setDefaultEncoding("utf-8");
    	multipartResolver.setMaxUploadSize(2048 * 1024);
    	multipartResolver.setMaxInMemorySize(2048);
    	return multipartResolver;
	}

	@Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        requestMappingHandlerAdapter.setMessageConverters(Collections.singletonList(fastJsonHttpMessageConverter()));
        requestMappingHandlerAdapter.setContentNegotiationManager(mvcContentNegotiationManager());
        return requestMappingHandlerAdapter;
    }
	
	@Bean
	@Autowired
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue
        );
        // 解决乱码的问题
        List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        return fastConverter;
    }
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.viewResolver(viewResolver());
		registry.viewResolver(thymeleafViewResolver());
	}
	
	@Bean
	@Autowired
	public ContentNegotiationManager mvcContentNegotiationManager() {
		return new ContentNegotiationManager();
	}
	
	public @Bean InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setExposeContextBeansAsAttributes(true);
		return viewResolver;
	}
	
	public @Bean ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		thymeleafViewResolver.setTemplateEngine(templateEngine());
		thymeleafViewResolver.setViewNames(new String[] {"value/*"});
		thymeleafViewResolver.setOrder(2);
		return thymeleafViewResolver;
	}
	
	public @Bean @Autowired SpringTemplateEngine templateEngine() {
		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
		springTemplateEngine.setTemplateResolver(springResourceTemplateResolver());
		return springTemplateEngine;
	}
	
	public @Bean SpringResourceTemplateResolver springResourceTemplateResolver() {
		SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
		springResourceTemplateResolver.setPrefix("");
		springResourceTemplateResolver.setSuffix("");
		springResourceTemplateResolver.setTemplateMode("HTML5");
		springResourceTemplateResolver.setCacheable(false);
		springResourceTemplateResolver.setCharacterEncoding("UTF-8");
		return springResourceTemplateResolver;
	}
}
