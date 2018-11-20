package com.lt.base.outrest.config;

import java.util.Properties;

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

import com.github.pagehelper.PageHelper;

@Configuration
@MapperScan(basePackages = "com.lt.base.mybatis")
@EnableTransactionManagement
@AutoConfigureAfter(DataSourceConfiguration.class)
public class MyBatisConfiguration {
	
	@Autowired
	@Qualifier("primaryDataSource")
	private DataSource primaryDataSource;
	
	public @Bean SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(this.primaryDataSource);
		bean.setTypeAliasesPackage("com.lt.base.entry");
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
	
    public @Bean PageHelper pageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum","true");
        properties.setProperty("rowBoundsWithCount","true");
        properties.setProperty("reasonable","true");
        properties.setProperty("dialect","postgresql");    //配置postgresql数据库的方言支持Oracle,Mysql,MariaDB,SQLite,Hsqldb,PostgreSQL六种数据库
        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
