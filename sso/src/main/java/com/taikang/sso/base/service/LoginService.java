package com.taikang.sso.base.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.taikang.sso.base.commons.Result;
import com.taikang.sso.base.commons.ResultEnum;
import com.taikang.sso.base.commons.ResultUtils;
import com.taikang.sso.base.dto.LoginUser;
import com.taikang.sso.base.jpa.UserPrimaryRepository;
import com.taikang.sso.base.po.User;

@Service
public class LoginService {
	
	private @Autowired UserPrimaryRepository userPrimaryRepository;
	
	private @Autowired StringRedisTemplate stringRedisTemplate;

	public Result<String> generateToken(String userName, String passWord) {
		
		if (StringUtils.isEmpty(userName) || userName.trim().length() == 0) {
			return ResultUtils.success(ResultEnum.USER_ADN_PASS_ERROR);
		}
		User user = userPrimaryRepository.findByUserName(userName);
		if (!BCrypt.checkpw(passWord, user.getPass()) && !user.getPass().equals(passWord)) {
			return ResultUtils.success(ResultEnum.USER_ADN_PASS_ERROR);
		}
		String token = UUID.randomUUID().toString().replace("-", "");
		
		LoginUser loginUser = new LoginUser(
				user.getUserCode(), 
				userName, 
				user.getTele(),
				user.isAccountNonExpired(), 
				user.isAccountNonLocked(), 
				user.isCredentialsNonExpired(), 
				user.isEnabled());
		stringRedisTemplate.opsForValue().set(token, JSONObject.toJSONString(loginUser), 10, TimeUnit.MINUTES);
		return ResultUtils.success(ResultEnum.GENERATE_TOKEN_SUCCESS, token);
	}

	public Result<String> cancelToken(String token) {
		if (!stringRedisTemplate.hasKey(token)) {
			return ResultUtils.success(ResultEnum.CANCEL_TOKEN_ERROR);
		}
		stringRedisTemplate.delete(token);
		return ResultUtils.success(ResultEnum.CANCEL_TOKEN_SUCCESS);
	}

}
