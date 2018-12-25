package com.taikang.sso.basic.config;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.taikang.sso.basic.util.PropertiesLoader;

public class DataSourceConnection {
	
	private static DataSourceConnection dataSourceConnection = null;
	
	private static DruidDataSource druidDataSource = null;
	
	static {
		try {
			druidDataSource = druidDataSource();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private DataSourceConnection() {
		
	}
	
	public static synchronized DataSourceConnection getInstance() {
		if (null == dataSourceConnection) {
			dataSourceConnection = new DataSourceConnection();
		}
		return dataSourceConnection;
	}
	
	public static DruidDataSource druidDataSource() throws SQLException {
		DruidDataSource druidDataSource = new DruidDataSource();
		PropertiesLoader loader = new PropertiesLoader("/sso.properties");
		String jdbcUrl = loader.getProperty("datasource.jdbc_url");
		String username = loader.getProperty("datasource.username");
		String password = loader.getProperty("datasource.password");
		String driverClassName = loader.getProperty("datasource.driver-class-name");
		
		druidDataSource.setDriverClassName(driverClassName);
		druidDataSource.setUrl(jdbcUrl);
		druidDataSource.setUsername(username);
		druidDataSource.setPassword(password);
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
//		druidDataSource.setFilters("stat");
		return druidDataSource;
	}
	
	public DruidPooledConnection getConnection() throws SQLException {
		return druidDataSource.getConnection();
	}
	
	public DruidPooledConnection getConnection(DruidDataSource druidDataSource) throws SQLException {
		return druidDataSource.getConnection();
	}
	
}
