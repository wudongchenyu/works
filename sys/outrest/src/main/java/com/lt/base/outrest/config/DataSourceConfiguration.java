package com.lt.base.outrest.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfiguration {
	
	@ConfigurationProperties(prefix = "spring.primary.datasource")
	public @Bean(name = "primaryDataSource") @Primary @Qualifier("primaryDataSource") DataSource primaryDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

}
