package com.author.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.mindrot.jbcrypt.BCrypt;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.author.po.User;
import com.author.util.DataSourceUtils;
import com.author.util.RedisUtils;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.mysql.cj.util.StringUtils;

import redis.clients.jedis.Jedis;

public class LoginService {
	
	private static LoginService loginService;
	
	private LoginService() {
		
	}
	
	public static synchronized LoginService getInstance() {
		if (null == loginService) {
			loginService = new LoginService();
		}
		return loginService;
	}
	
	public Result<String> login(String userName, String passWord, String ip) {
		if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
			return ResultUtils.success(ResultEnum.USER_ADN_PASS_ERROR);
		}
		
		User user= new User();
		try {
			DruidPooledConnection connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement("select id,user_name,user_code,name,pass,tele,coi,"
					+ "enabled,account_non_expired,account_non_locked,credentials_non_expired,"
					+ "create_time from sso_user where user_name=? and enabled = 1 and "
					+ "account_non_expired = 1 and account_non_locked = 1 and credentials_non_expired = 1 limit 1");
			statement.setString(1, userName);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				 user.setId(rs.getString("id"));
				 user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
				 user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
				 user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
				 user.setCoi(rs.getString("coi"));
				 user.setCreateTime(rs.getDate("create_time"));
				 user.setEnabled(rs.getBoolean("enabled"));
				 user.setName(rs.getString("name"));
				 user.setPass(rs.getString("pass"));
				 user.setTele(rs.getString("tele"));
				 user.setUserCode(rs.getString("user_code"));
				 user.setUserName(rs.getString("user_name"));
			}
			statement.close();
			connection.close();
			if (!BCrypt.checkpw(passWord, user.getPass()) && !user.getPass().equals(passWord)) {
				return ResultUtils.success(ResultEnum.USER_ADN_PASS_ERROR);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		StringBuffer tokensBuffer = new StringBuffer("");
		tokensBuffer.append(ip+",");
		tokensBuffer.append(System.nanoTime()+",");
		tokensBuffer.append(user.getId()+",");
		tokensBuffer.append(user.getUserName()+",");
		tokensBuffer.append(user.getName());
		String token = BCrypt.hashpw(tokensBuffer.toString(), BCrypt.gensalt());
		Jedis jedis = RedisUtils.openJedis();
		String key = "token_" + token + ",user_" + user.getUserName();
		
		Set<String> set = jedis.keys("token_*,user_lisi");
		for (String keys : set) {
			System.out.println(keys);
			jedis.del(keys);
		}
		
		jedis.set(key, tokensBuffer.toString());
		jedis.expire(key, 10*60);
		jedis.close();
		return ResultUtils.success(ResultEnum.GENERATE_TOKEN_SUCCESS, token);
	}

	public Result<String> logout(String token) {
		Jedis jedis = RedisUtils.openJedis();
		if (jedis.exists(token)) {
			jedis.del(token);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_SUCCESS);
	}

	public Result<String> checkToken(String token) {
		Jedis jedis = RedisUtils.openJedis();
		if (jedis.exists(token)) {
			jedis.close();
			return ResultUtils.success(ResultEnum.CHECK_TOKEN_SUCCESS);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_SUCCESS);
	}
	
	public String getIpAddress(HttpServletRequest request){      
        String ip = request.getHeader("x-forwarded-for");      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("Proxy-Client-IP");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("WL-Proxy-Client-IP");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("HTTP_CLIENT_IP");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getRemoteAddr();      
        }      
        return ip;      
    }
	
}
