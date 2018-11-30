package com.author.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.mindrot.jbcrypt.BCrypt;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.author.po.LoginUser;
import com.author.po.User;
import com.author.po.UserAuthority;
import com.author.util.DataSourceUtils;
import com.author.util.PassDecode;
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
	
	public Result<JSONObject> login(String userName, String passWord, String ip) {
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
		
		Set<String> set = jedis.keys("token_*," + user.getUserName());
		for (String keys : set) {
			System.out.println(keys);
			jedis.del(keys);
		}
		
		jedis.set(key, tokensBuffer.toString());
		jedis.expire(key, 10*60);
		jedis.close();
		JSONObject toJsonObject = new JSONObject();
		toJsonObject.put("token", token);
		return ResultUtils.success(ResultEnum.GENERATE_TOKEN_SUCCESS, toJsonObject);
	}

	public Result<String> logout(String token) {
		Jedis jedis = RedisUtils.openJedis();
		Set<String> set = jedis.keys("*" + token + "*");
		for (String keys : set) {
			System.out.println(keys);
			jedis.del(keys);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_SUCCESS);
	}

	public Result<String> checkToken(String token) {
		Jedis jedis = RedisUtils.openJedis();
		
		Set<String> set = jedis.keys("*" + token + "*");
		if (null != set && set.size() > 0) {
			jedis.close();
			return ResultUtils.success(ResultEnum.CHECK_TOKEN_SUCCESS);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_ERROR);
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

	public Result<JSONObject> login(String userName, String passWord, String ip, String remortAddress) {
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
		
		LoginUser loginUser = new LoginUser();
		loginUser.setIp(ip);
		loginUser.setName(user.getName());
		loginUser.setUserId(user.getId());
		loginUser.setUserName(user.getUserName());
		loginUser.setRemortAddress(remortAddress);
		
		String loginUserString = JSON.toJSONString(loginUser);
		
		String token = PassDecode.aesEncrypt(loginUserString);//BCrypt.hashpw(tokensBuffer.toString(), BCrypt.gensalt());
		Jedis jedis = RedisUtils.openJedis();
		String key = "token_" + token + ",user_" + user.getUserName();
		
		Set<String> set = jedis.keys("token_*,user_" + user.getUserName());
		for (String keys : set) {
			System.out.println(keys);
			jedis.del(keys);
		}
		
		jedis.set(key, loginUserString);
		jedis.expire(key, 10*60);
		jedis.close();
		JSONObject toJsonObject = new JSONObject();
		toJsonObject.put("token", token);
		return ResultUtils.success(ResultEnum.GENERATE_TOKEN_SUCCESS, toJsonObject);
	}

	public Result<JSONObject> authorityToken(String token) {
		
		LoginUser user = this.getLoginUser(token);
		
		List<String> list = new ArrayList<String>();
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement("select authority_url from sso_user_authority sua left join sso_authority sa on sa.id=sua.authority_id where user_id=?");
			statement.setString(1, user.getUserId());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("authority_url"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (list.isEmpty()) {
			
		}
		
		UserAuthority ua = new UserAuthority();
		ua.setAuthorities(list);
		ua.setUserId(user.getUserId());
		
		String jsonString = JSON.toJSONString(ua);
		
		String authoritiesToken = PassDecode.aesEncrypt(jsonString);//BCrypt.hashpw(tokensBuffer.toString(), BCrypt.gensalt());
		Jedis jedis = RedisUtils.openJedis();
		String key = "authority_token:" + authoritiesToken + ",user:" + user.getUserName();
		System.out.println(key);
		Set<String> set = jedis.keys("authority_token:*,user:" + user.getUserName());
		for (String keys : set) {
			jedis.del(keys);
		}
		
		jedis.set(key, jsonString);
		jedis.expire(key, 10*60);
		jedis.close();
		JSONObject toJsonObject = new JSONObject();
		toJsonObject.put("authoritiesToken", authoritiesToken);
		return ResultUtils.success(ResultEnum.GENERATE_TOKEN_SUCCESS, toJsonObject);
	}

	private LoginUser getLoginUser(String token) {
		String decrypt = PassDecode.aesDecrypt(token);
		LoginUser loginUser = JSON.parseObject(decrypt, new TypeReference<LoginUser>() {});
		return loginUser;
	}
	
	public static void main(String[] args) {
		System.out.println("asd".hashCode());
	}

	public Result<String> checkAuthorityToken(String token) {
		Jedis jedis = RedisUtils.openJedis();
		
		Set<String> set = jedis.keys("*" + token + "*");
		if (null != set && set.size() > 0) {
			jedis.close();
			return ResultUtils.success(ResultEnum.CHECK_TOKEN_SUCCESS);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_ERROR);
	}

	public Result<UserAuthority> resolverAuthorityToken(String token) {
		
		try {
			String decrypt = PassDecode.aesDecrypt(token);
			
			UserAuthority ua = JSON.parseObject(decrypt, new TypeReference<UserAuthority>() {});
			return ResultUtils.success(ResultEnum.RESOLVER_TOKEN_SUCCESS, ua);
		} catch (Exception e) {
			return ResultUtils.error(ResultEnum.RESOLVER_TOKEN_ERROR);
		}
		
	}

	public Result<String> authorityTokenCancel(String token) {
		Jedis jedis = RedisUtils.openJedis();
		Set<String> set = jedis.keys("*" + token + "*");
		for (String keys : set) {
			System.out.println(keys);
			jedis.del(keys);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_SUCCESS);
	}

	public Result<LoginUser> resolverToken(String token) {
		try {
			String decrypt = PassDecode.aesDecrypt(token);
			LoginUser loginUser = JSON.parseObject(decrypt, new TypeReference<LoginUser>() {});
			return ResultUtils.success(ResultEnum.RESOLVER_TOKEN_SUCCESS, loginUser);
		} catch (Exception e) {
			return ResultUtils.error(ResultEnum.RESOLVER_TOKEN_ERROR);
		}
		
	}
	
}
