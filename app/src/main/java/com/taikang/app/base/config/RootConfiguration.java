/**
 * Title:RootConfig
 * Description:
 * Company:
 * @author wdcy
 * @Date 2018年3月22日 上午11:33:16
 */
package com.taikang.app.base.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * Title:RootConfig
 * Description:
 * Company:
 * @author wdcy
 * @Date 2018年3月22日 上午11:33:16
 */
@Order(2)
@Configuration
@ComponentScan(basePackages = "com.taikang",
includeFilters = { 
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class) 
},
excludeFilters = { 
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class) 
})
@PropertySource(value = { "classpath:application.properties" })
@Slf4j
public class RootConfiguration {
	
	@Value("${datasource.jdbc_url}")
	private String url;
	
	@Value("${datasource.username}")
	private String userName;
	
	@Value("${datasource.password}")
	private String passWord;
	
	@Value("${datasource.driver-class-name}")
	private String driverClass;
	
	public RootConfiguration() {
		log.info("RootConfiguration:" + System.nanoTime());
	}
	
	public @Bean(name = "primaryDataSource") @Primary @Qualifier("primaryDataSource") DataSource primaryDataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		System.out.println("url:" + url);
		System.out.println("userName:" + userName);
		System.out.println("passWord:" + passWord);
		System.out.println("driverClass:" + driverClass);
		druidDataSource.setDriverClassName(driverClass);
		druidDataSource.setUrl(url);
		druidDataSource.setUsername(userName);
		druidDataSource.setPassword(passWord);
		druidDataSource.setInitialSize(1);
		druidDataSource.setMinIdle(1);
		druidDataSource.setMaxActive(10);
		druidDataSource.setMaxWait(10000);
		druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
		druidDataSource.setMinEvictableIdleTimeMillis(300000);
		druidDataSource.setTestWhileIdle(true);
		druidDataSource.setTestOnBorrow(true);
		druidDataSource.setTestOnReturn(true);
		druidDataSource.setPoolPreparedStatements(true);
		druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
		druidDataSource.setDefaultAutoCommit(true);
		druidDataSource.setValidationQuery("select 1");
		return druidDataSource;
	}


}
