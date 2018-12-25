package com.taikang.app.base.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.taikang.app.base.dto.UserAuthority;
import com.taikang.app.base.feign.SsoTokenService;
import com.taikang.result.basic.commons.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/client/authority", produces = "application/json;charset=UTF-8")
@Api(tags = "远程调用权限信息相关API")
public class AuthorityController {

	private @Autowired SsoTokenService ssoTokenClient;

	@Value(value = "${app.subordinate.app}")
	private String subordinateApp;
	
	@Value(value = "${app.subordinate.system}")
	private String subordinateSystem;
	
	@Value(value = "${app.subordinate.module}")
	private String subordinateModule;
	
	@Value(value = "${app.subordinate.channel}")
	private String channel;

	@ApiOperation(value = "获取权限token", notes = "获取权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/generate")
	public Result<JSONObject> tokenGenerate(String token) {
		Result<JSONObject> result = ssoTokenClient.authorityTokenGenerate(token, subordinateSystem, subordinateApp, subordinateModule, channel);
		return result;
	}

	@ApiOperation(value = "注销权限token", notes = "注销权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/cancel")
	public Result<String> authorityTokenCancel(String token) {
		Result<String> result = ssoTokenClient.authorityTokenCancel(token);
		return result;
	}

	@ApiOperation(value = "验证权限token", notes = "验证权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/check")
	public Result<String> authorityTokenCheck(String token) {
		Result<String> result = ssoTokenClient.authorityTokenCheck(token);
		return result;
	}

	@ApiOperation(value = "解析权限token", notes = "解析权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/resolver")
	public Result<UserAuthority> authorityTokenResolver(String token) {
		Result<UserAuthority> result = ssoTokenClient.authorityTokenResolver(token);
		return result;
	}
	
	@GetMapping("/detail")
	public Result<String> getAuthority(){
		return null;
	}
}
