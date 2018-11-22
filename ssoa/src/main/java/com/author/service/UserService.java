package com.author.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.author.po.User;
import com.author.util.DataSourceUtils;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.author.util.SystemGeneration;
import com.mysql.cj.util.StringUtils;

public class UserService {
	
	private static UserService userService;
	
	private UserService() {
		
	}
	
	public static synchronized UserService getInstance() {
		if (userService == null) {
			userService = new UserService();
		}
		return userService;
	}

	public Result<User> add(String userName, String pass, String tele, String coi, String name) {
		
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			User user = new User();
			user.setId(SystemGeneration.getUuidNumber(""));
			user.setCoi(coi);
			user.setName(name);
			user.setUserName(userName);
			user.setTele(tele);
			user.setPass(BCrypt.hashpw(pass, BCrypt.gensalt()));
			connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement("insert into sso_user(id,name,user_code,"
					+ "user_name,pass,tele,coi,enabled,credentials_non_expired,create_time,account_non_locked,"
					+ "account_non_expired) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			statement.setString(1, user.getId());
			statement.setString(2, user.getName());
			statement.setString(3, user.getUserCode());
			statement.setString(4, user.getUserName());
			statement.setString(5, user.getPass());
			statement.setString(6, user.getTele());
			statement.setString(7, user.getCoi());
			statement.setBoolean(8, user.isEnabled());
			statement.setBoolean(9, user.isCredentialsNonExpired());
			statement.setDate(10, (Date) user.getCreateTime());
			statement.setBoolean(11, user.isAccountNonLocked());
			statement.setBoolean(12, user.isAccountNonExpired());
			statement.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			return ResultUtils.success(ResultEnum.SAVE_USER_SUCCESS, user);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.SAVE_USER_ERROR);
		}finally {
			try {
				statement.close();
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
			statement = connection.prepareStatement("delete from sso_user where id=?");
			statement.setString(1, id);
			statement.executeUpdate();
			connection.commit();
			return ResultUtils.success(ResultEnum.DELETE_USER_SUCCESS, id);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.DELETE_USER_ERROR);
		}finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Result<User> detail(String id) {
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DataSourceUtils.openConnection();
			statement = connection.prepareStatement("select id,name,user_code," + 
					"user_name,tele,coi,enabled,credentials_non_expired,create_time,account_non_locked," + 
					"account_non_expired from sso_user where id = ?");
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();
			User user = new User();
			if (rs.next()) {
				user.setId(rs.getString("id"));
				user.setCreateTime(rs.getDate("create_time"));
				user.setEnabled(rs.getBoolean("enabled"));
				user.setAccountNonExpired(rs.getBoolean("credentials_non_expired"));
				user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
				user.setCoi(rs.getString("coi"));
				user.setCredentialsNonExpired(rs.getBoolean("account_non_expired"));
				user.setName(rs.getString("name"));
				user.setPass(null);
				user.setTele(rs.getString("tele"));
				user.setUserCode(rs.getString("user_code"));
				user.setUserName(rs.getString("user_name"));
			}
			return ResultUtils.success(ResultEnum.SEARCH_USER_SUCCESS, user);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.SEARCH_USER_ERROR);
		}finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Result<List<User>> list(String id, String user_code, String user_name, String tele, String coi) {
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			StringBuffer sqlBuffer = new StringBuffer("select id,name,user_code,user_name,tele,coi,"
					+ "enabled,credentials_non_expired,create_time,account_non_locked,account_non_expired from sso_user where enabled=1 ");
			
			connection = DataSourceUtils.openConnection();
			List<Object> params = new ArrayList<Object>();
			if (!StringUtils.isEmptyOrWhitespaceOnly(user_code)) {
				sqlBuffer.append("and user_code = ?");
				params.add(user_code);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(user_name)) {
				sqlBuffer.append("and user_name = ?");
				params.add(user_name);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(tele)) {
				sqlBuffer.append("and tele = ?");
				params.add(tele);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(coi)) {
				sqlBuffer.append("and coi = ?");
				params.add(coi);
			}
			statement = connection.prepareStatement(sqlBuffer.toString());
			this.setStatement(params, statement);
			
			ResultSet rs = statement.executeQuery();
			List<User> list = new ArrayList<User>();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setCreateTime(rs.getDate("create_time"));
				user.setEnabled(rs.getBoolean("enabled"));
				user.setAccountNonExpired(rs.getBoolean("credentials_non_expired"));
				user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
				user.setCoi(rs.getString("coi"));
				user.setCredentialsNonExpired(rs.getBoolean("account_non_expired"));
				user.setName(rs.getString("name"));
				user.setPass(null);
				user.setTele(rs.getString("tele"));
				user.setUserCode(rs.getString("user_code"));
				user.setUserName(rs.getString("user_name"));
				list.add(user);
			}
			return ResultUtils.success(ResultEnum.SEARCH_USER_SUCCESS, list);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.SEARCH_USER_ERROR);
		}finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void setStatement(List<Object> params, PreparedStatement statement) throws SQLException {
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i) instanceof String) {
				statement.setString(i+1, (String) params.get(i));
			}
			if (params.get(i) instanceof Integer) {
				statement.setInt(i+1, (Integer) params.get(i));
			}
			if (params.get(i) instanceof Double) {
				statement.setDouble(i+1, (Double) params.get(i));
			}
			if (params.get(i) instanceof java.util.Date) {
				statement.setDate(i+1, (Date) params.get(i));
			}
		}
	}

	public Result<User> edit(String userName, String tele, String coi, String name, String id) {
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		PreparedStatement statementUpdate = null;
		try {
			connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement("select id,name,user_code," + 
					"user_name,tele,coi,enabled,credentials_non_expired,create_time,account_non_locked," + 
					"account_non_expired from sso_user where id = ?");
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();
			User user = new User();
			if (rs.next()) {
				user.setId(rs.getString("id"));
				user.setCreateTime(rs.getDate("create_time"));
				user.setEnabled(rs.getBoolean("enabled"));
				user.setAccountNonExpired(rs.getBoolean("credentials_non_expired"));
				user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
				user.setCoi(rs.getString("coi"));
				user.setCredentialsNonExpired(rs.getBoolean("account_non_expired"));
				user.setName(rs.getString("name"));
				user.setPass(null);
				user.setTele(rs.getString("tele"));
				user.setUserCode(rs.getString("user_code"));
				user.setUserName(rs.getString("user_name"));
			}
			
			StringBuffer sqlBuffer = new StringBuffer("update sso_user set id = id");
			
			List<Object> params = new ArrayList<Object>();
			if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
				sqlBuffer.append(",user_name = ?");
				params.add(userName);
			}
			
			if (StringUtils.isEmptyOrWhitespaceOnly(name)) {
				sqlBuffer.append(",name = ?");
				params.add(name);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(tele)) {
				sqlBuffer.append(",tele = ?");
				params.add(tele);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(coi)) {
				sqlBuffer.append(",coi = ?");
				params.add(coi);
			}
			sqlBuffer.append(" where enabled=1 and id=?");
			statementUpdate = connection.prepareStatement(sqlBuffer.toString());
			this.setStatement(params, statement);
			statementUpdate.setString(params.size()+1, id);
			statementUpdate.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			return ResultUtils.success(ResultEnum.EDIT_USER_SUCCESS);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.EDIT_USER_ERROR);
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

	public Result<String> privileges(String userId, List<String> list) {
		
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			
			connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement("insert into sso_user_authority(id,user_id,authorty_id) VALUES(?,?,?)");
			
			for (String authority : list) {
				statement.setString(1, SystemGeneration.getUuidNumber(""));
				statement.setString(2, userId);
				statement.setString(3, authority);
				statement.addBatch();
			}
			statement.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			return ResultUtils.success(ResultEnum.SAVE_USER_SUCCESS);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.SAVE_USER_ERROR);
		}finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
