package com.taikang.app.base.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.taikang.result.basic.commons.Result;
import com.taikang.sso.basic.dto.UserAuthorityToken;
import com.taikang.sso.basic.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/sso/authority", produces = "application/json;charset=UTF-8")
@Api(tags = "使用jar包权限信息相关API")
public class AuthorityFromJarController {

	private @Autowired LoginService loginService;

	@ApiOperation(value = "获取权限token", notes = "获取权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/generate")
	public Result<JSONObject> tokenGenerate(String token) {
		Result<JSONObject> result = loginService.authorityToken(token);
		return result;
	}

	@ApiOperation(value = "注销权限token", notes = "注销权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/cancel")
	public Result<Boolean> authorityTokenCancel(String token) {
		Result<Boolean> result = loginService.authorityTokenCancel(token);
		return result;
	}

	@ApiOperation(value = "验证权限token", notes = "验证权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/check")
	public Result<Boolean> authorityTokenCheck(String token) {
		Result<Boolean> result = loginService.checkAuthorityToken(token);
		return result;
	}

	@ApiOperation(value = "解析权限token", notes = "解析权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/resolver")
	public Result<UserAuthorityToken> authorityTokenResolver(String token) {
		Result<UserAuthorityToken> result = loginService.resolverAuthorityToken(token);
		return result;
	}

}
