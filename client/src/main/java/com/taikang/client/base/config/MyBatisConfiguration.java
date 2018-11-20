package com.taikang.client.base.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan(basePackages = "com.taikang.client.base.mybatis")
@EnableTransactionManagement
@AutoConfigureAfter(DataSourceConfiguration.class)
public class MyBatisConfiguration {
	
	@Autowired
	@Qualifier("primaryDataSource")
	private DataSource primaryDataSource;
	
	public @Bean SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(this.primaryDataSource);
		bean.setTypeAliasesPackage("com.taikang.client.base.po");
		org.apache.ibatis.session.Configuration configuration = 
				new org.apache.ibatis.session.Configuration();
		bean.setConfiguration(configuration);
		return bean.getObject();
	}
	
	public @Bean SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	public @Bean(name = "platformTransactionManager") PlatformTransactionManager platformTransactionManager() {
		return new DataSourceTransactionManager(this.primaryDataSource);
	}

}
