package com.taikang.app.base.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Order(3)
@Configuration//这是Java配置类
@EnableWebMvc
@ComponentScan(basePackages = {"com.taikang"},
includeFilters = { 
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class) 
}
)//扫描com包
public class MvcConfiguration implements WebMvcConfigurer{
	
	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Bean
	@Autowired
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue, 
                SerializerFeature.WriteNullNumberAsZero, 
                SerializerFeature.WriteNullStringAsEmpty
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
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
	    while(iterator.hasNext()){
	        HttpMessageConverter<?> converter = iterator.next();
	        if(converter instanceof MappingJackson2HttpMessageConverter){
	            iterator.remove();
	        }
	    }
		converters.add(fastJsonHttpMessageConverter());
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