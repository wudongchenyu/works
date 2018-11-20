package com.lt.workday.web.config;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "entityManagerFactoryPrimary",
		transactionManagerRef = "transactionManagerPrimary",
		basePackages = "com.lt.workday.web.jpa"
		)
@AutoConfigureAfter(DataSourceConfiguration.class)
public class PrimaryDataSourceConfiguration {
	
	@Autowired
	private JpaProperties jpaProperties;
	
	@Autowired
	@Qualifier("primaryDataSource")
	private DataSource primaryDataSource;
	
	public @Bean @Primary LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
		
		return builder.dataSource(primaryDataSource)
				.properties(getVendorPrpperties())
				.packages("com.lt.workday.web.po")
				.persistenceUnit("primaryPersistenceUnit")
				.build();
	}
	
	public @Bean EntityManager entityManagerPrimary(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
	}
	
	public @Bean @Primary PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
	}

	private Map<String, ?> getVendorPrpperties() {
		return jpaProperties.getHibernateProperties(new HibernateSettings());
	}

}
