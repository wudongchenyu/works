package com.author.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.author.po.Authority;
import com.author.util.DataSourceUtils;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.author.util.SystemGeneration;
import com.mysql.cj.util.StringUtils;

public class AuthorityService {
	
	private static volatile AuthorityService authorityService;
	
	private AuthorityService() {
		
	}
	
	public static synchronized AuthorityService getInstance() {
		if (null == authorityService) {
			authorityService = new AuthorityService();
		}
		return authorityService;
	}

	public Result<List<Authority>> getAllAuthority(
			String userId, 
			String id, 
			String authorityName, 
			String authorityUrl, 
			String authorityType, 
			String subordinateSystem, 
			String subordinateApp, 
			String subordinateModule, 
			String channel) {
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DataSourceUtils.openConnection();
			
			StringBuffer sqlBuffer = new StringBuffer("select sa.id,sa.authority_name,sa.authority_code,"
					+ "sa.authority_url,sa.enabled,sa.create_time,sa.authority_type,sa.channel,sa.subordinate_app,"
					+ "sa.subordinate_system,sa.subordinate_module,sa.update_time from sso_authority sa "
					+ "left join sso_user_authority sua on sua.authority_id=sa.id where 1 = 1");
			
			List<Object> params = new ArrayList<Object>();
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(userId)) {
				sqlBuffer.append(" and sua.id = ?");
				params.add(userId);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(id)) {
				sqlBuffer.append(" and sa.id = ?");
				params.add(id);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(authorityName)) {
				sqlBuffer.append(" and sa.authority_name = ?");
				params.add(authorityName);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(authorityUrl)) {
				sqlBuffer.append(" and sa.authority_url = ?");
				params.add(authorityUrl);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(authorityType)) {
				sqlBuffer.append("and sa.authority_type = ?");
				params.add(authorityType);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(subordinateSystem)) {
				sqlBuffer.append("and sa.subordinate_system = ?");
				params.add(subordinateSystem);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(subordinateApp)) {
				sqlBuffer.append("and sa.subordinate_app = ?");
				params.add(subordinateApp);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(subordinateModule)) {
				sqlBuffer.append("and sa.subordinate_module = ?");
				params.add(subordinateModule);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(channel)) {
				sqlBuffer.append("and sa.channel = ?");
				params.add(channel);
			}
			
			System.out.println(sqlBuffer.toString());
			statement = connection.prepareStatement(sqlBuffer.toString());
			
			this.setStatement(params, statement);
			
			ResultSet rs = statement.executeQuery();
			
			
			List<Authority> list = new ArrayList<Authority>();
			while (rs.next()) {
				Authority authority = new Authority();
				authority.setId(rs.getString("id"));
				authority.setCreateTime(rs.getTimestamp("create_time"));
				authority.setEnabled(rs.getBoolean("enabled"));
				authority.setAuthorityCode(rs.getString("authority_code"));
				authority.setAuthorityName(rs.getString("authority_name"));
				authority.setAuthorityUrl(rs.getString("authority_url"));
				authority.setAuthorityType(rs.getString("authority_type"));
				authority.setChannel(rs.getString("channel"));
				authority.setSubordinateApp(rs.getString("subordinate_app"));
				authority.setSubordinateModule(rs.getString("subordinate_module"));
				authority.setSubordinateSystem(rs.getString("subordinate_system"));
				authority.setUpdateTime(rs.getTimestamp("update_time"));
				list.add(authority);
			}
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORTY_SUCCESS, list);
		} catch (SQLException e) {
			e.printStackTrace();
			return ResultUtils.error(ResultEnum.SEARCH_AUTHORTY_ERROR);
		}finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	public Result<Authority> addAuthority(
			String authorityName, 
			String authorityUrl, 
			String authorityType, 
			String subordinateSystem, 
			String subordinateApp, 
			String subordinateModule, 
			String channel) {
		
		Authority authority = new Authority();
		authority.setId(SystemGeneration.getUuidNumber(""));
		authority.setAuthorityName(authorityName);
		authority.setAuthorityUrl(authorityUrl);
		authority.setAuthorityType(authorityType);
		authority.setChannel(channel);
		authority.setSubordinateApp(subordinateApp);
		authority.setSubordinateModule(subordinateModule);
		authority.setSubordinateSystem(subordinateSystem);
		
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement("insert into sso_authority("
					+ "id,authority_name,authority_code,authority_url,enabled,"
					+ "create_time,authority_type,channel,subordinate_app,"
					+ "subordinate_system,subordinate_module,update_time) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?)");
			
			statement.setString(1, authority.getId());
			statement.setString(2, authority.getAuthorityName());
			statement.setString(3, authority.getAuthorityCode());
			statement.setString(4, authority.getAuthorityUrl());
			statement.setBoolean(5, authority.isEnabled());
			statement.setTimestamp(6, new Timestamp(authority.getCreateTime().getTime()));
			statement.setString(7, authority.getAuthorityType());
			statement.setString(8, authority.getChannel());
			statement.setString(9, authority.getSubordinateApp());
			statement.setString(10, authority.getSubordinateSystem());
			statement.setString(11, authority.getSubordinateModule());
			statement.setTimestamp(12, new Timestamp(authority.getUpdateTime().getTime()));
			
			int updateCount = statement.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			
			if (updateCount != 1) {
				return ResultUtils.error(ResultEnum.SEARCH_AUTHORTY_ERROR);
			}
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORTY_SUCCESS, authority);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return ResultUtils.error(ResultEnum.SEARCH_AUTHORTY_ERROR, e.getMessage());
		}finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Result<Authority> editAuthority(
			String id, String authorityName, 
			String authorityUrl, 
			String authorityType, 
			String subordinateSystem, 
			String subordinateApp, 
			String subordinateModule, 
			String channel) {
		
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		PreparedStatement statementUpdate = null;
		try {
			connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement("select id,authority_name,authority_code,"
					+ "authority_url,enabled,create_time,authority_type,channel,subordinate_app,"
					+ "subordinate_system,subordinate_module,update_time from sso_authority where id = ?");
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();
			
			Authority authority = new Authority();
			if (rs.next()) {
				authority.setId(rs.getString("id"));
				authority.setCreateTime(rs.getTimestamp("create_time"));
				authority.setEnabled(rs.getBoolean("enabled"));
				authority.setAuthorityCode(rs.getString("authority_code"));
				authority.setAuthorityName(rs.getString("authority_name"));
				authority.setAuthorityUrl(rs.getString("authority_url"));
				authority.setAuthorityType(rs.getString("authority_type"));
				authority.setChannel(rs.getString("channel"));
				authority.setSubordinateApp(rs.getString("subordinate_app"));
				authority.setSubordinateModule(rs.getString("subordinate_module"));
				authority.setSubordinateSystem(rs.getString("subordinate_system"));
				authority.setUpdateTime(rs.getTimestamp("update_time"));
			}
			
			StringBuffer sqlBuffer = new StringBuffer("update sso_authority set id = id");
			
			List<Object> params = new ArrayList<Object>();
			if (!StringUtils.isEmptyOrWhitespaceOnly(authorityName)) {
				sqlBuffer.append(",authority_name = ?");
				params.add(authorityName);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(authorityUrl)) {
				sqlBuffer.append(",authority_url = ?");
				params.add(authorityUrl);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(authorityType)) {
				sqlBuffer.append(",authority_type = ?");
				params.add(authorityType);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(subordinateSystem)) {
				sqlBuffer.append(",subordinate_system = ?");
				params.add(subordinateSystem);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(subordinateApp)) {
				sqlBuffer.append(",subordinate_app = ?");
				params.add(subordinateApp);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(subordinateModule)) {
				sqlBuffer.append(",subordinate_module = ?");
				params.add(subordinateModule);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(channel)) {
				sqlBuffer.append(",channel = ?");
				params.add(channel);
			}
			
			sqlBuffer.append(" where enabled=1 and id=?");
			System.out.println(sqlBuffer.toString());
			statementUpdate = connection.prepareStatement(sqlBuffer.toString());
			this.setStatement(params, statementUpdate);
			statementUpdate.setString(params.size()+1, id);
			
			statementUpdate.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			return ResultUtils.success(ResultEnum.EDIT_AUTHORTY_SUCCESS, authority);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return ResultUtils.error(ResultEnum.EDIT_AUTHORTY_ERROR, e.getMessage());
		}finally {
			try {
				statement.close();
				statementUpdate.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Result<String> delete(String id) {
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement("delete from sso_authority where id=?");
			statement.setString(1, id);
			statement.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			return ResultUtils.success(ResultEnum.DELETE_AUTHORITY_SUCCESS, id);
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return ResultUtils.success(ResultEnum.DELETE_AUTHORITY_ERROR);
		}finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Result<Authority> detail(String id) {
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			try {
				connection = DataSourceUtils.openConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			StringBuffer sqlBuffer = new StringBuffer("select id,authority_name,authority_code,"
					+ "authority_url,enabled,create_time,authority_type,channel,subordinate_app,"
					+ "subordinate_system,subordinate_module,update_time from sso_authority where id = ?");
			
			statement = connection.prepareStatement(sqlBuffer.toString());
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();
			
			Authority authority = new Authority();
			if (rs.next()) {
				authority.setId(rs.getString("id"));
				authority.setCreateTime(rs.getTimestamp("create_time"));
				authority.setEnabled(rs.getBoolean("enabled"));
				authority.setAuthorityCode(rs.getString("authority_code"));
				authority.setAuthorityName(rs.getString("authority_name"));
				authority.setAuthorityUrl(rs.getString("authority_url"));
				authority.setAuthorityType(rs.getString("authority_type"));
				authority.setChannel(rs.getString("channel"));
				authority.setSubordinateApp(rs.getString("subordinate_app"));
				authority.setSubordinateModule(rs.getString("subordinate_module"));
				authority.setSubordinateSystem(rs.getString("subordinate_system"));
				authority.setUpdateTime(rs.getTimestamp("update_time"));
			}else {
				authority = null;
			}
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORTY_SUCCESS, authority);
		} catch (SQLException e) {
			e.printStackTrace();
			return ResultUtils.error(ResultEnum.SEARCH_AUTHORTY_ERROR);
		}finally {
			try {
				if (null != statement) {
					statement.close();
				}
				if (null != connection) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setStatement(List<Object> params, PreparedStatement statement) throws SQLException {
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i) instanceof String) {
				statement.setString(i+1, (String) params.get(i));
				continue;
			}
			if (params.get(i) instanceof Integer) {
				statement.setInt(i+1, (Integer) params.get(i));
				continue;
			}
			if (params.get(i) instanceof Double) {
				statement.setDouble(i+1, (Double) params.get(i));
				continue;
			}
			if (params.get(i) instanceof java.util.Date) {
				statement.setDate(i+1, (Date) params.get(i));
				continue;
			}
		}
	}

}
