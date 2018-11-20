package com.taikang.client.base.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taikang.client.base.commons.Result;
import com.taikang.client.base.dto.UserResult;
import com.taikang.client.base.po.User;
import com.taikang.client.base.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/client/user")
@Api(tags = "用户信息相关API")
public class UserController {
	
	@Autowired private UserService userService;
	

	@ApiOperation(value = "添加用户信息", notes = "添加用户信息")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "userName", value = "账号", required = true, dataType = "String"),
			@ApiImplicitParam(name = "pass", value = "密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "tele", value = "联系方式", required = true, dataType = "String"),
			@ApiImplicitParam(name = "coi", value = "身份证号", required = true, dataType = "String")})
	@PostMapping("/add")
	public Result<User> addUser(String userName, String pass, String tele, String coi) {
		Result<User> result = userService.addUser(userName, pass, tele, coi);
		return result;
	}
	
	@ApiOperation(value = "删除用户信息", notes = "删除用户信息")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String")
			})
	@PostMapping("/delete")
	public Result<User> deleteUser(String id) {
		Result<User> result = userService.deleteUser(id);
		return result;
	}
	
	@ApiOperation(value = "修改用户信息", notes = "修改用户信息")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String"),
			@ApiImplicitParam(name = "userName", value = "账号", required = true, dataType = "String"),
			@ApiImplicitParam(name = "pass", value = "密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "tele", value = "联系方式", required = true, dataType = "String"),
			@ApiImplicitParam(name = "coi", value = "身份证号", required = true, dataType = "String")})
	@PostMapping("/edit")
	public Result<User> editUser(String id,String userName, String pass, String tele, String coi) {
		Result<User> result = userService.editUser(id, userName, pass, tele, coi);
		return result;
	}
	
	@ApiOperation(value = "获取用户信息", notes = "获取用户信息")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "userName", value = "账号", required = true, dataType = "String")})
	@GetMapping("/detail")
	public Result<UserResult> detail(String userName) {
		Result<UserResult> result = userService.detail(userName);
		return result;
	}
	
	@ApiOperation(value = "用户信息列表", notes = "用户信息列表")
	@GetMapping("/list")
	public Result<List<UserResult>> userList() {
		Result<List<UserResult>> result = userService.userList();
		return result;
	}
	
}
