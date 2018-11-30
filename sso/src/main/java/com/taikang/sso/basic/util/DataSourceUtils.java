package com.taikang.sso.basic.util;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.taikang.sso.basic.config.DataSourceConnection;

public class DataSourceUtils {
	
	public static DruidPooledConnection openConnection() throws SQLException {
		DataSourceConnection dataSource = DataSourceConnection.getInstance();
		return dataSource.getConnection();
	}
	
	public static void closeConnection(DruidPooledConnection connection) throws SQLException {
		connection.close();
	}

}
