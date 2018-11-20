package com.lt.base.web.rest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.lt.base.service.UserUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value = "LoginController",tags = "登录中心相关API")
@Controller
@Slf4j
public class LoginController {
	
	@ApiOperation(value = "首页展示", notes = "展示首页信息")
	@GetMapping("/loginForm")
	public String loginForm() {
		return "login/login";
	}
	
	@ApiOperation(value = "登录页面", notes = "登录页面")
	@GetMapping("/login")
	public String login() {
		return "index";
	}
	
	@ApiOperation(value = "系统中心", notes = "系统中心")
	@GetMapping("/center")
	public String loginSuccess() {
		UserDetails userDetails = UserUtils.getUserDetails();
		if (userDetails != null) {
			log.info("姓名：" + userDetails.getUsername()+"于"+System.currentTimeMillis()+"登录系统");
			userDetails.getAuthorities().forEach(au->{
				log.info("权限"+au.getAuthority());
			});
		}
		return "login/center";
	}
	
	@ApiOperation(value = "登录失败返回页面", notes = "登录失败返回页面")
	@GetMapping("/logout")
	public String logout() {
		return "index";
	}

}
