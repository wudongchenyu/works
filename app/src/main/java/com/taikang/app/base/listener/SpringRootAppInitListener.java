package com.taikang.app.base.listener;

import java.util.Properties;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import com.mysql.cj.util.StringUtils;
import com.taikang.app.base.util.PropertiesLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringRootAppInitListener implements ApplicationContextInitializer<ConfigurableApplicationContext>{

	private static final String DEFAULT_CONFIG_PREFIX = "application"; 
	private static final String DEFAULT_CONFIG_SUFFIX = ".properties"; 
	private static final String DEFAULT_CONFIG_DELIMITE = "-"; 
	private static final String DEFAULT_CONFIG_KEY = "spring.profiles.active";

	public SpringRootAppInitListener() {
		log.info("SpringRootAppInitListener");
	}
	
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		log.info("SpringRootAppInitListener.initialize");
		MutablePropertySources sources = applicationContext.getEnvironment().getPropertySources(); 
		//加载默认的配置文件 
		PropertiesLoader propertiesLoader = new PropertiesLoader(DEFAULT_CONFIG_PREFIX + DEFAULT_CONFIG_SUFFIX);
		Properties defaultProperties = propertiesLoader.getProperties(); 
		//获取要加载的配置文件的后缀名称 
		String suFix = defaultProperties.getProperty(DEFAULT_CONFIG_KEY); 
		//加载配置文件 
		Properties properties = null; 
		if (!StringUtils.isEmptyOrWhitespaceOnly(suFix)) { 
			properties = propertiesLoader.loadClassPathProperties(DEFAULT_CONFIG_PREFIX + DEFAULT_CONFIG_DELIMITE + suFix + DEFAULT_CONFIG_SUFFIX); } 
		//两个配置文件合并 
		propertiesLoader.mergerProperties(defaultProperties, properties); 
		//加载到spring 的配置中去 
		sources.addFirst(new PropertiesPropertySource(DEFAULT_CONFIG_PREFIX, properties)); 
	}

}
