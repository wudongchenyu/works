package com.author.pool;

import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.author.util.PropertiesUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DataSourceConnection {
	
	/**
	 * volatile 作用 
	 * 1）保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。
	 * 2）禁止进行指令重排序。
	 */
	private static volatile DataSourceConnection dataSourceConnection;
	
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
	
	public static DataSourceConnection getInstance() {
		if (null == dataSourceConnection) {
			synchronized(DataSourceConnection.class) {
				if (null == dataSourceConnection) {
					dataSourceConnection = new DataSourceConnection();
				}
			}
		}
		return dataSourceConnection;
	}
	
	public static DruidDataSource druidDataSource() throws SQLException {
		log.info("获取数据库连接");
		DruidDataSource druidDataSource = new DruidDataSource();
		Properties properties = PropertiesUtils.getProperties();
		String jdbcUrl = properties.getProperty("datasource.jdbc_url");
		String username = properties.getProperty("datasource.username");
		String password = properties.getProperty("datasource.password");
		String driverClassName = properties.getProperty("datasource.driver-class-name");
		
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
	
	public synchronized DruidPooledConnection getConnection() throws SQLException {
		return druidDataSource.getConnection();
	}
	
	public DruidPooledConnection getConnection(DruidDataSource druidDataSource) throws SQLException {
		return druidDataSource.getConnection();
	}
	
}
