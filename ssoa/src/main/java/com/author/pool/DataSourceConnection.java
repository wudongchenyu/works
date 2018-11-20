package com.author.pool;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.mysql.cj.jdbc.Driver;

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
	
	public static synchronized DataSourceConnection getInstance() {
		if (null == dataSourceConnection) {
			dataSourceConnection = new DataSourceConnection();
		}
		return dataSourceConnection;
	}
	
	public static DruidDataSource druidDataSource() throws SQLException {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriver(new Driver());
		druidDataSource.setUrl("jdbc:mysql://localhost:3306/primarydb?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Hongkong");
		druidDataSource.setUsername("primarys");
		druidDataSource.setPassword("primarys");
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
