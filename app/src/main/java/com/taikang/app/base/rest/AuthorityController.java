package com.taikang.app.base.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.taikang.app.base.dto.UserAuthority;
import com.taikang.app.base.feign.SsoTokenClient;
import com.taikang.result.basic.commons.Result;
import com.taikang.sso.basic.dto.UserAuthorityToken;
import com.taikang.sso.basic.po.Authority;
import com.taikang.sso.basic.service.AuthorityService;
import com.taikang.sso.basic.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/client/authority", produces = "application/json;charset=UTF-8")
@Api(tags = "远程调用权限信息相关API")
public class AuthorityController {

	private @Autowired AuthorityService authorityService;

	private @Autowired SsoTokenClient ssoTokenClient;

	private @Autowired LoginService loginService;
	
	@ApiOperation(value = "添加权限信息", notes = "添加权限信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "authorityName", value = "权限名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityUrl", value = "权限URL", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityType", value = "权限类型", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinateSystem", value = "权限所属系统", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinateApp", value = "权限所属应用", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinateModule", value = "权限所属模块", dataType = "String"),
			@ApiImplicitParam(name = "channel", value = "权限访问渠道", dataType = "String") })
	@PostMapping("/add")
	public Result<Authority> add(
			@RequestParam(name="authorityName",required=true)String authorityName, 
			@RequestParam(name="authorityUrl",required=true)String authorityUrl, 
			@RequestParam(name="authorityType",required=true)String authorityType, 
			@RequestParam(name="subordinateSystem",required=true)String subordinateSystem,
			@RequestParam(name="subordinateApp",required=true)String subordinateApp, 
			@RequestParam(name="subordinateModule",required=false)String subordinateModule,
			@RequestParam(name="channel",required=false)String channel) {
		Result<Authority> result = authorityService.add(
				authorityName, 
				authorityUrl, 
				authorityType, 
				subordinateSystem, 
				subordinateApp, 
				subordinateModule, 
				channel);
		return result;
	}

	@ApiOperation(value = "添加权限信息", notes = "添加权限信息")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String") })
	@PostMapping("/delete")
	public Result<String> delete(String id) {
		Result<String> result = authorityService.delete(id);
		return result;
	}

	@ApiOperation(value = "添加权限信息", notes = "添加权限信息")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityName", value = "权限名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityUrl", value = "权限URL", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityType", value = "权限类型", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinateSystem", value = "权限所属系统", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinateApp", value = "权限所属应用", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinateModule", value = "权限所属模块", dataType = "String"),
			@ApiImplicitParam(name = "channel", value = "权限访问渠道", dataType = "String")  })
	@PostMapping("/edit")
	public Result<Authority> edit(
			@RequestParam(name="id",required=true)String id, 
			@RequestParam(name="authorityName",required=true)String authorityName, 
			@RequestParam(name="authorityUrl",required=true)String authorityUrl, 
			@RequestParam(name="authorityType",required=true)String authorityType, 
			@RequestParam(name="subordinateSystem",required=true)String subordinateSystem,
			@RequestParam(name="subordinateApp",required=true)String subordinateApp, 
			@RequestParam(name="subordinateModule",required=true)String subordinateModule,
			@RequestParam(name="channel",required=true)String channel) {
		Result<Authority> result = authorityService.edit(
				id, authorityName, 
				authorityUrl, 
				authorityType, 
				subordinateSystem, 
				subordinateApp, 
				subordinateModule, 
				channel);
		return result;
	}

	@ApiOperation(value = "添加权限信息", notes = "添加权限信息")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String")})
	@PostMapping("/detail")
	public Result<Authority> detail(String id) {
		Result<Authority> result = authorityService.detail(id);
		return result;
	}

	@ApiOperation(value = "权限列表查询", notes = "权限列表查询")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "id", value = "ID", dataType = "String"),
			@ApiImplicitParam(name = "authorityName", value = "名称", dataType = "String"),
			@ApiImplicitParam(name = "subordinate", value = "所属", dataType = "String"),
			@ApiImplicitParam(name = "authorityUrl", value = "URL", dataType = "String") })
	@PostMapping("/list")
	public Result<List<Authority>> list(String id, String authorityName, String subordinate, String authorityUrl) {
		
		Result<List<Authority>> result = authorityService.list(id);
		return result;
	}

	@ApiOperation(value = "获取权限token", notes = "获取权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/generate")
	public Result<JSONObject> tokenGenerate(String token) {
		Result<JSONObject> result = ssoTokenClient.authorityTokenGenerate(token);
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

	@ApiOperation(value = "解析权限token", notes = "解析权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/resolvers")
	public Result<UserAuthorityToken> authorityTokenResolvers(String token) {
		return loginService.resolverAuthorityToken(token);
	}
	
	@ApiOperation(value = "获取权限token", notes = "获取权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String") })
	@PostMapping("/token/generates")
	public Result<JSONObject> tokenGenerates(String token) {
		Result<JSONObject> result = loginService.authorityToken(token);
		return result;
	}

}
