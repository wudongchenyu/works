package com.taikang.client.base.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.taikang.client.base.util.CommonsUtils;
import com.taikang.result.basic.commons.Result;
import com.taikang.sso.basic.dto.LoginUser;
import com.taikang.sso.basic.service.LoginService;

import io.swagger.annotations.Api;

@Api(tags = "使用jar包登录相关API")
@RestController
@RequestMapping(path = "/sso", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE,MediaType.APPLICATION_FORM_URLENCODED_VALUE})
public class LoginFromJarController {
	
	private @Autowired LoginService loginService;
	
	@PostMapping("/login")
	public Result<JSONObject> login(
			@RequestParam(value = "username", name = "username") String username, 
			@RequestParam(value = "pass", name = "pass") String pass,
			HttpServletRequest request) {
		String ip = CommonsUtils.getIpAddress(request);
		String remortAddress = request.getRequestURL().toString();
		
		Result<JSONObject> result = loginService.login(username, pass, ip, remortAddress);
		
		return result;
	}
	
	@PostMapping("/logout")
	public Result<Boolean> logout(@RequestParam(required = true) String token) {
		return loginService.logout(token);
	}

	@PostMapping("/checkToken")
	public Result<Boolean> checkToken(@RequestParam(required = true) String token) {
		return loginService.checkToken(token);
	}
	
	@PostMapping("/resolver")
	public Result<LoginUser> resolver(@RequestParam(required = true) String token) {
		return loginService.resolverToken(token);
	}
	
}
