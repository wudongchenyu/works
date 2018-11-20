package com.taikang.client.base.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taikang.client.base.commons.Result;
import com.taikang.client.base.feign.SsoClient;
import com.taikang.client.base.util.CommonsUtils;

import io.swagger.annotations.Api;

@Api(tags = "登录相关API")
@RestController("/sso")
public class LoginController {
	
	private @Autowired SsoClient ssoClient;
	
	@PostMapping("/login")
	public Result<String> login(
			@RequestParam(required = true) String userName, 
			@RequestParam(required = true) String passWord,
			HttpServletRequest request) {
		String ip = CommonsUtils.getIpAddress(request);
		String remortAddress = request.getRequestURL().toString();
		return ssoClient.login(userName, ip, passWord, remortAddress);
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
