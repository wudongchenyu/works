package com.taikang.sso.base.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taikang.sso.base.commons.Result;
import com.taikang.sso.base.service.LoginService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/sso/log")
@Api(tags = "登录管理相关API")
public class LoginController {
	
	private @Autowired LoginService loginService;
	
	@PostMapping("/login")
	public Result<String> login(String userName, String passWord) {
		return loginService.generateToken(userName, passWord);
	}
	
	@PostMapping("/logout")
	public Result<String> logout(String token) {
		return loginService.cancelToken(token);
	}

}
