package com.taikang.client.base.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.taikang.client.base.commons.Result;
import com.taikang.client.base.feign.SsoClient;
import com.taikang.client.base.util.CommonsUtils;

import io.swagger.annotations.Api;

@Api(tags = "登录相关API")
@RestController
@RequestMapping(path = "/sso", produces = "application/json;charset=UTF-8")
public class LoginController {
	
	private @Autowired SsoClient ssoClient;
	
	@PostMapping("/login")
	public Result<JSONObject> login(
			@RequestParam(value = "username") String username, 
			@RequestParam(value = "pass") String pass,
			HttpServletRequest request) {
		String ip = CommonsUtils.getIpAddress(request);
		String remortAddress = request.getRequestURL().toString();
		
		Result<JSONObject> result = ssoClient.login(username, ip, pass, remortAddress);
		
		return result;
	}
	
	@PostMapping("/logout")
	public Result<String> logout(@RequestParam(required = true) String token) {
		return ssoClient.logout(token);
	}

	@PostMapping("/checkToken")
	public Result<String> checkToken(@RequestParam(required = true) String token) {
		return ssoClient.checkToken(token);
	}
	

}
