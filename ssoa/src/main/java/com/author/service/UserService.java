package com.author.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.author.po.User;
import com.author.util.DataSourceUtils;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.author.util.SystemGeneration;

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

}
