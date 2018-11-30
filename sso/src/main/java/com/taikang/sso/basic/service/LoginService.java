package com.taikang.sso.basic.service;

import java.util.List;
import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mysql.cj.util.StringUtils;
import com.taikang.result.basic.commons.Result;
import com.taikang.result.basic.commons.enums.ResultEnum;
import com.taikang.result.basic.util.ResultUtils;
import com.taikang.sso.basic.dto.LoginUser;
import com.taikang.sso.basic.dto.UserAuthorityToken;
import com.taikang.sso.basic.jpa.UserRepository;
import com.taikang.sso.basic.mapper.UserAuthorityMapper;
import com.taikang.sso.basic.po.User;
import com.taikang.sso.basic.util.PassDecode;
import com.taikang.sso.basic.util.RedisUtils;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Service
@Slf4j
public class LoginService {
	
	private @Autowired UserRepository userRepository;
	
	private @Autowired UserAuthorityMapper userAuthorityMapper;
	
	/**
	 * 登录
	 * @param userName
	 * @param passWord
	 * @param ip
	 * @param remortAddress
	 * @return
	 */
	public Result<JSONObject> login(String userName, String passWord, String ip, String remortAddress) {
		if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
			return ResultUtils.success(ResultEnum.USER_ADN_PASS_ERROR);
		}
		
		User user = userRepository.findByUserNameAndEnabled(userName, true);
		
		if (null == user) {
			log.error("用户不存在：" + ResultEnum.USER_ADN_PASS_ERROR.getMessage());
			return ResultUtils.error(ResultEnum.USER_ADN_PASS_ERROR);
		}
		log.info("密码：" + passWord);
		log.info("用户密码：" + user.getPass());
		//验证用户名密码
		if (!BCrypt.checkpw(passWord, user.getPass()) && !user.getPass().equals(passWord)) {
			log.error("密码错误：" + ResultEnum.USER_ADN_PASS_ERROR.getMessage());
			return ResultUtils.error(ResultEnum.USER_ADN_PASS_ERROR);
		}
		
		LoginUser loginUser = new LoginUser();
		loginUser.setIp(ip);
		loginUser.setName(user.getName());
		loginUser.setUserId(user.getId());
		loginUser.setUserName(user.getUserName());
		loginUser.setRemortAddress(remortAddress);
		
		String loginUserString = JSON.toJSONString(loginUser);
		log.info("loginUserString：" + loginUserString);
		String token = PassDecode.aesEncrypt(loginUserString);
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
		log.info("token：" + toJsonObject.toJSONString());
		return ResultUtils.success(ResultEnum.GENERATE_TOKEN_SUCCESS, toJsonObject);
	}

	/**
	 * 注销
	 * @param token
	 * @return
	 */
	public Result<Boolean> logout(String token) {
		Jedis jedis = RedisUtils.openJedis();
		Set<String> set = jedis.keys("*" + token + "*");
		
		if (null == set || set.size() == 0) {
			return ResultUtils.success(ResultEnum.CANCEL_TOKEN_ERROR);
		}
		
		for (String keys : set) {
			System.out.println(keys);
			jedis.del(keys);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_SUCCESS, true);
	}

	/**
	 * 校验token
	 * @param token
	 * @return
	 */
	public Result<Boolean> checkToken(String token) {
		Jedis jedis = RedisUtils.openJedis();
		
		Set<String> set = jedis.keys("*" + token + "*");
		jedis.close();
		if (null != set && set.size() > 0) {
			return ResultUtils.success(ResultEnum.CHECK_TOKEN_SUCCESS, true);
		}
		return ResultUtils.error(ResultEnum.CANCEL_TOKEN_ERROR);
	}
	
	/**
	 * 获取全限token
	 * @param token
	 * @return
	 */
	public Result<JSONObject> authorityToken(String token) {
		
		LoginUser user = this.getLoginUser(token);
		
		if (null == user) {
			return ResultUtils.error(ResultEnum.RESOLVER_TOKEN_ERROR);
		}
		
		List<String> list = userAuthorityMapper.findAllByUserId(user.getUserId());
		
		if (list.isEmpty()) {
			
		}
		
		UserAuthorityToken ua = new UserAuthorityToken();
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

	/**
	 * 校验全限token
	 * @param token
	 * @return
	 */
	public Result<Boolean> checkAuthorityToken(String token) {
		Jedis jedis = RedisUtils.openJedis();
		
		Set<String> set = jedis.keys("*" + token + "*");
		if (null != set && set.size() > 0) {
			jedis.close();
			return ResultUtils.success(ResultEnum.CHECK_TOKEN_SUCCESS, true);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_ERROR);
	}

	/**
	 * 解析全限token
	 * @param token
	 * @return
	 */
	public Result<UserAuthorityToken> resolverAuthorityToken(String token) {
		
		try {
			String decrypt = PassDecode.aesDecrypt(token);
			
			UserAuthorityToken ua = JSON.parseObject(decrypt, new TypeReference<UserAuthorityToken>() {});
			return ResultUtils.success(ResultEnum.RESOLVER_TOKEN_SUCCESS, ua);
		} catch (Exception e) {
			return ResultUtils.error(ResultEnum.RESOLVER_TOKEN_ERROR);
		}
		
	}

	/**
	 * 注销权限token
	 * @param token
	 * @return
	 */
	public Result<Boolean> authorityTokenCancel(String token) {
		Jedis jedis = RedisUtils.openJedis();
		Set<String> set = jedis.keys("*" + token + "*");
		
		if (null == set || set.size() == 0) {
			jedis.close();
			return ResultUtils.error(ResultEnum.CANCEL_TOKEN_ERROR);
		}
		
		for (String keys : set) {
			System.out.println(keys);
			jedis.del(keys);
		}
		jedis.close();
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_SUCCESS, true);
	}

	/**
	 * 解析token
	 * @param token
	 * @return
	 */
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
