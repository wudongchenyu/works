package com.taikang.client.base.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.taikang.client.base.commons.Result;
import com.taikang.client.base.dto.UserAuthority;
import com.taikang.client.base.feign.SsoTokenClient;
import com.taikang.client.base.po.Authority;
import com.taikang.client.base.service.AuthorityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/client/authority", produces = "application/json;charset=UTF-8")
@Api(tags = "权限信息相关API")
public class AuthorityController {
	
	private @Autowired AuthorityService authorityService;
	
	private @Autowired SsoTokenClient ssoTokenClient;
	
	@ApiOperation(value = "添加权限信息", notes = "添加权限信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "authorityName", value = "名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinate", value = "所属", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityUrl", value = "URL", required = true, dataType = "String")})
	@PostMapping("/add")
	public Result<Authority> add(String authorityName, String subordinate, String authorityUrl) {
		Result<Authority> result = authorityService.add(authorityName, subordinate, authorityUrl);
		return result;
	}
	
	@ApiOperation(value = "添加权限信息", notes = "添加权限信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String")})
	@PostMapping("/delete")
	public Result<String> delete(String id) {
		Result<String> result = authorityService.delete(id);
		return result;
	}
	
	@ApiOperation(value = "添加权限信息", notes = "添加权限信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityName", value = "名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinate", value = "所属", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityUrl", value = "URL", required = true, dataType = "String")})
	@PostMapping("/edit")
	public Result<Authority> edit(String id,String authorityName, String subordinate, String authorityUrl) {
		Result<Authority> result = authorityService.edit(id, authorityName, subordinate, authorityUrl);
		return result;
	}
	
	@ApiOperation(value = "添加权限信息", notes = "添加权限信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityName", value = "名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinate", value = "所属", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityUrl", value = "URL", required = true, dataType = "String")})
	@PostMapping("/detail")
	public Result<Authority> detail(String id) {
		Result<Authority> result = authorityService.detail(id);
		return result;
	}
	
	@ApiOperation(value = "添加权限信息", notes = "添加权限信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityName", value = "名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subordinate", value = "所属", required = true, dataType = "String"),
			@ApiImplicitParam(name = "authorityUrl", value = "URL", required = true, dataType = "String")})
	@PostMapping("/list")
	public Result<List<Authority>> list(String id) {
		Result<List<Authority>> result = authorityService.list(id);
		return result;
	}
	
	@ApiOperation(value = "获取权限token", notes = "获取权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")})
	@PostMapping("/token/generate")
	public Result<JSONObject> tokenGenerate(String token) {
		Result<JSONObject> result = ssoTokenClient.authorityTokenGenerate(token);
		return result;
	}
	
	@ApiOperation(value = "注销权限token", notes = "注销权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")})
	@PostMapping("/token/cancel")
	public Result<String> authorityTokenCancel(String token) {
		Result<String> result = ssoTokenClient.authorityTokenCancel(token);
		return result;
	}
	
	@ApiOperation(value = "验证权限token", notes = "验证权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")})
	@PostMapping("/token/check")
	public Result<String> authorityTokenCheck(String token) {
		Result<String> result = ssoTokenClient.authorityTokenCheck(token);
		return result;
	}
	
	@ApiOperation(value = "解析权限token", notes = "解析权限token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")})
	@PostMapping("/token/resolver")
	public Result<UserAuthority> authorityTokenResolver(String token) {
		Result<UserAuthority> result = ssoTokenClient.authorityTokenResolver(token);
		return result;
	}

}
