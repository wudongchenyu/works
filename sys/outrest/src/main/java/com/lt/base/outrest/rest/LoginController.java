package com.lt.base.outrest.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lt.base.common.ResultEnum;
import com.lt.base.contants.Result;
import com.lt.base.service.LoginService;
import com.lt.base.service.UserUtils;
import com.lt.base.util.ResultUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value = "LoginController",tags = "登录中心相关API")
@RestController
@Slf4j
public class LoginController {
	
	private @Autowired LoginService loginService;
	
	@ApiOperation(value = "首页展示", notes = "展示首页信息")
	@GetMapping("/loginForm")
	public String loginForm() {
		return "login/login";
	}
	
	@ApiOperation(value = "登录页面", notes = "登录页面")
	@GetMapping("/login")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "userCode", value = "userCode", required = true, dataType = "String"), 
			@ApiImplicitParam(name = "pass", value = "pass", required = true, dataType = "String")})
	public Result<JSONObject> logina(String userCode, String pass) {
		log.info(userCode + "登录系统");
		String token = loginService.generateToken(userCode, pass);
		if (null != token) {
			JSONObject object = new JSONObject();
			object.put("token", token);
			object.put("userCode", UserUtils.getUserCode());
			return ResultUtils.success(ResultEnum.SUCCESS, object);
		}
		return ResultUtils.error(ResultEnum.USER_NON_EXISTENT);
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
