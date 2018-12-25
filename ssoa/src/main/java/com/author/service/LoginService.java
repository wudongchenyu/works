package com.author.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.author.util.SystemGeneration;
import com.mysql.cj.util.StringUtils;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;

@Log4j2
public class LoginService {
	
	private volatile static LoginService loginService;
	
	private LoginService() {
		
	}
	
	public static synchronized LoginService getInstance() {
		if (null == loginService) {
			loginService = new LoginService();
		}
		return loginService;
	}
	
	public Result<JSONObject> login(String userName, String passWord, String fromUrl) {
		if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
			return ResultUtils.error(ResultEnum.USER_ADN_PASS_ERROR);
		}
		
		User user= new User();
		try {
			log.info("开始获取数据库连接：" + System.nanoTime());
			DruidPooledConnection connection = DataSourceUtils.openConnection();
			log.info("获取数据库连接成功：" + System.nanoTime());
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement("select id,user_name,user_code,name,pass,tele,coi,"
					+ "enabled,account_non_expired,account_non_locked,credentials_non_expired,"
					+ "create_time from sso_user where user_name=? and enabled = 1 and "
					+ "account_non_expired = 1 and account_non_locked = 1 and credentials_non_expired = 1 limit 1");
			statement.setString(1, userName);
			log.info("开始执行查询：" + System.nanoTime());
			ResultSet rs = statement.executeQuery();
			log.info("执行查询结束：" + System.nanoTime());
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
			log.info("结果集处理结束：" + System.nanoTime());
			statement.close();
			connection.close();
			log.info("数据连接关闭结束：" + System.nanoTime());
			if (!BCrypt.checkpw(passWord, user.getPass()) && !user.getPass().equals(passWord)) {
				return ResultUtils.error(ResultEnum.USER_ADN_PASS_ERROR);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResultUtils.error(ResultEnum.DATABASE_CONNECTION_EXIT_ERROR);
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(user.getId())) {
			return ResultUtils.error(ResultEnum.USER_ADN_PASS_ERROR);
		}
		
		log.info("获取redis连接：" + System.nanoTime());
		Jedis jedis = RedisUtils.openJedis();
		log.info("获取redis结束：" + System.nanoTime());
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		
		Set<String> tokenSet = jedis.keys("token_*," + userName);
		Iterator<String> iterator = tokenSet.iterator();
		String token = null;
		log.info("判断有没有token：" + System.nanoTime());
		if (iterator.hasNext()) {
			log.info("有token");
			token = iterator.next();
			token = token.substring(6,token.indexOf(",user_"));
		}else {
			log.info("没有token,开始生成token：" + System.nanoTime());
			LoginUser loginUser = new LoginUser();
			loginUser.setIp(fromUrl);
			loginUser.setName(user.getName());
			loginUser.setUserId(user.getId());
			loginUser.setUserName(user.getUserName());
			String loginUserString = JSON.toJSONString(loginUser);
			
			token = PassDecode.aesEncrypt(loginUserString);
			log.info("生成token结束：" + System.nanoTime());
		}
		
		String code = SystemGeneration.getUuidNumber("");
		String key = "code_" + code + ",user_" + user.getUserName();
		log.info("开始删除redis中该用户旧数据：" + System.nanoTime());
		Set<String> set = jedis.keys("code_*," + user.getUserName());
		for (String keys : set) {
			log.info(keys);
			jedis.del(keys);
		}
		log.info("删除redis中该用户旧数据结束：" + System.nanoTime());
		log.info("保存新数据到redis开始：" + System.nanoTime());
		jedis.set(key, token);
		jedis.expire(key, 10*60);
		jedis.close();
		log.info("保存新数据到redis结束：" + System.nanoTime());
		
		
		JSONObject toJsonObject = new JSONObject();
		toJsonObject.put("token", token);
		toJsonObject.put("code", code);
		log.info("返回数据封装结束：" + System.nanoTime());
		return ResultUtils.success(ResultEnum.GENERATE_TOKEN_SUCCESS, toJsonObject);
	}

	public Result<String> logout(String token) {
		Jedis jedis = RedisUtils.openJedis();
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		Set<String> set = jedis.keys("*" + token + "*");
		for (String keys : set) {
			log.info(keys);
			jedis.del(keys);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_SUCCESS);
	}

	public Result<String> checkToken(String token) {
		Jedis jedis = RedisUtils.openJedis();
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		
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
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		String key = "token_" + token + ",user_" + user.getUserName();
		
		Set<String> set = jedis.keys("token_*,user_" + user.getUserName());
		for (String keys : set) {
			log.info(keys);
			jedis.del(keys);
		}
		
		jedis.set(key, loginUserString);
		jedis.expire(key, 10*60);
		jedis.close();
		JSONObject toJsonObject = new JSONObject();
		toJsonObject.put("token", token);
		return ResultUtils.success(ResultEnum.GENERATE_TOKEN_SUCCESS, toJsonObject);
	}

	public Result<JSONObject> authorityToken(
			String token, 
			String subordinateSystem, 
			String subordinateApp, 
			String subordinateModule, 
			String channel) {
		
		LoginUser user = this.getLoginUser(token);
		
		List<String> list = new ArrayList<String>();
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			StringBuffer sqlBuffer = new StringBuffer("select authority_url from sso_user_authority sua left join sso_authority sa on sa.id=sua.authority_id where ");
			
			List<Object> params = new ArrayList<Object>();
			
			sqlBuffer.append(" user_id=?");
			params.add(user.getUserId());
			if (!StringUtils.isEmptyOrWhitespaceOnly(subordinateSystem)) {
				sqlBuffer.append(" and sa.subordinate_system = ?");
				params.add(subordinateSystem);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(subordinateApp)) {
				sqlBuffer.append(" and sa.subordinate_app = ?");
				params.add(subordinateApp);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(subordinateModule)) {
				sqlBuffer.append(" and sa.subordinate_module = ?");
				params.add(subordinateModule);
			}
			
			if (!StringUtils.isEmptyOrWhitespaceOnly(channel)) {
				sqlBuffer.append(" and INSTR(sa.channel,?)>0");
				params.add(channel);
			}
			log.info(sqlBuffer.toString());
			statement = connection.prepareStatement(sqlBuffer.toString());
			this.setStatement(params, statement);
			
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
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		String key = "authority_token:" + authoritiesToken + ",user:" + user.getUserName();
		log.info(key);
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
		log.info("asd".hashCode());
	}

	public Result<String> checkAuthorityToken(String token) {
		Jedis jedis = RedisUtils.openJedis();
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		
		Set<String> set = jedis.keys("*" + token + "*");
		if (null != set && set.size() > 0) {
			jedis.close();
			return ResultUtils.success(ResultEnum.CHECK_TOKEN_SUCCESS);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_ERROR);
	}

	public Result<UserAuthority> resolverAuthorityToken(String token) {
		Jedis jedis = RedisUtils.openJedis();
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		
		Set<String> set = jedis.keys("*" + token + "*");
		jedis.close();
		
		if (null == set || set.size() == 0) {
			return ResultUtils.error(ResultEnum.CANCEL_TOKEN_ERROR);
		}
		
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
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		Set<String> set = jedis.keys("*" + token + "*");
		for (String keys : set) {
			log.info(keys);
			jedis.del(keys);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_SUCCESS);
	}

	public Result<LoginUser> resolverToken(String token) {
		Jedis jedis = RedisUtils.openJedis();
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		
		Set<String> set = jedis.keys("*" + token + "*");
		jedis.close();
		
		if (null == set || set.size() == 0) {
			return ResultUtils.error(ResultEnum.CANCEL_TOKEN_ERROR);
		}
		try {
			String decrypt = PassDecode.aesDecrypt(token);
			LoginUser loginUser = JSON.parseObject(decrypt, new TypeReference<LoginUser>() {});
			return ResultUtils.success(ResultEnum.RESOLVER_TOKEN_SUCCESS, loginUser);
		} catch (Exception e) {
			return ResultUtils.error(ResultEnum.RESOLVER_TOKEN_ERROR);
		}
		
	}

	public Result<JSONObject> acquireToken(String code) {
		Jedis jedis = RedisUtils.openJedis();
		if (jedis == null) {
			ResultUtils.error(ResultEnum.JEDIS_NOT_EXIT_ERROR);
		}
		String key = "code_" + code + ",user_*";
		Set<String> set = jedis.keys(key);
		if (null == set || set.size() == 0) {
			return ResultUtils.error(ResultEnum.CANCEL_TOKEN_ERROR);
		}
		key = set.iterator().next();
		String token = jedis.get(key);
		jedis.del(key);
		
		String decrypt = PassDecode.aesDecrypt(token);
		LoginUser loginUser = JSON.parseObject(decrypt, new TypeReference<LoginUser>() {});
		
		key = "token_" + token + ",user_" + loginUser.getUserName();
		jedis.set(key, JSON.toJSONString(loginUser));
		jedis.expire(key, 10*60);
		jedis.close();
		JSONObject toJsonObject = new JSONObject();
		toJsonObject.put("token", token);
		return ResultUtils.success(ResultEnum.GENERATE_TOKEN_SUCCESS, toJsonObject);
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
