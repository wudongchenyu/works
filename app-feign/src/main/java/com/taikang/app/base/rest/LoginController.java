package com.taikang.app.base.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.taikang.app.base.dto.LoginUser;
import com.taikang.app.base.feign.SsoTokenService;
import com.taikang.app.base.util.CommonsUtils;
import com.taikang.result.basic.commons.Result;

import io.swagger.annotations.Api;

@Api(tags = "远程调用登录相关API")
@RestController
@RequestMapping(path = "/client", produces = "application/json;charset=UTF-8")
public class LoginController {
	
	private @Autowired SsoTokenService ssoClient;
	
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
	
	@PostMapping("/resolver")
	public Result<LoginUser> resolver(@RequestParam(required = true) String token) {
		return ssoClient.resolver(token);
	}
	
}
