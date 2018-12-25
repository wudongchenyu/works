package com.taikang.app.base.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;

import com.mysql.cj.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Title:RootConfiguration
 * Description:
 * @author wudon
 * @Date 2018年12月17日上午11:19:13
 */
@Order(2)
@Configuration
@ComponentScan(basePackages = "com.taikang",
excludeFilters = { 
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class) 
})
@PropertySource(value = { "classpath:application.properties",
		"classpath:application-${spring.profiles}.properties" })
@Slf4j
public class RootConfiguration {
	
	public RootConfiguration() {
		log.info("RootConfiguration:" + System.nanoTime());
	}
	
	/**
	 * 根据环境读取配置文件，可以再除了Controller里之外使用{@Value(value="${spring.profiles}")进行读取文件
	 * @return
	 */
	public @Bean static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		log.info("加载propertyPlaceholderConfigurer" + System.nanoTime());
		ClassPathResource resource = new ClassPathResource("application.properties");
		try {
			Properties loadProperties = PropertiesLoaderUtils.loadProperties(resource);
			String profiles = (String) loadProperties.get("spring.profiles");
			if (StringUtils.isEmptyOrWhitespaceOnly(profiles)) {
				profiles = "dev";
			}
			ClassPathResource resourceEnv = new ClassPathResource("application-" + profiles + ".properties");
			PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
			placeholderConfigurer.setLocations(resource, resourceEnv);
			return placeholderConfigurer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
