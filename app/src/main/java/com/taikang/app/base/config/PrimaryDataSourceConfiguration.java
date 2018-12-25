package com.taikang.app.base.config;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Order(5)
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "entityManagerFactoryPrimary",
		transactionManagerRef = "transactionManagerPrimary",
		basePackages = {"com.taikang.sso.basic.jpa","com.taikang.app.base.dto.jpa"}
		)
public class PrimaryDataSourceConfiguration {
	
	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddl;
	
	@Value("${hibernate.dialect}")
	private String dialect;
	
	@Value("${hibernate.show-sql}")
	private String showSql;
	
	private Properties jpaProperties;
	
	@Autowired
	@Qualifier("primaryDataSource")
	private DataSource primaryDataSource;
	
	public @Bean @Primary LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setGenerateDdl(false);
	    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	    factory.setJpaProperties(getJpaProperties());
	    factory.setJpaVendorAdapter(vendorAdapter);
	    factory.setPackagesToScan("com.taikang.sso.basic.po");
	    factory.setDataSource(primaryDataSource);
		
		return factory;
	}
	
	public @Bean EntityManager entityManagerPrimary() {
		return entityManagerFactoryPrimary().getObject().createEntityManager();
	}
	
	public @Bean @Primary PlatformTransactionManager transactionManagerPrimary() {
		return new JpaTransactionManager(entityManagerFactoryPrimary().getObject());
	}

	public Properties getJpaProperties() {
		jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.dialect", dialect);
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
		jpaProperties.setProperty("hibernate.use_sql_comments", "true");
		jpaProperties.setProperty("hibernate.show-sql", showSql);
		return jpaProperties;
	}

}
