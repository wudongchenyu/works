package com.taikang.client.base.rest;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taikang.client.base.commons.Result;
import com.taikang.client.base.po.Authority;
import com.taikang.client.base.service.AuthorityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/client/authority")
@Api(tags = "权限信息相关API")
public class AuthorityController {
	
	private AuthorityService authorityService;
	
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

}
