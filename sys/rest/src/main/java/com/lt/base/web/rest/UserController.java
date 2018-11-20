package com.lt.base.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lt.base.contants.Result;
import com.lt.base.entry.User;
import com.lt.base.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@Api(tags = "用户信息相关API")
public class UserController {
	
	@Autowired private UserService userService;
	

	@ApiOperation(value = "添加用户信息", notes = "添加用户信息")
	@ApiImplicitParam(name = "user", value = "User", required = true, dataType = "User")
	@PostMapping("/addUser")
	public Result<User> addUser(User user) {
		Result<User> result = new Result<User>();
		result = userService.addUser(user, result);
		return result;
	}
	
	@ApiOperation(value = "用户信息列表", notes = "用户信息列表")
	@PostMapping("/list")
	public Result<List<User>> userList() {
		Result<List<User>> result = new Result<List<User>>();
		result = userService.userList(result);
		return result;
	}
	
	@ApiOperation(value = "首页展示", notes = "展示首页信息")
	@GetMapping("/show1")
	public String showInfo1() {
		return "index";
	}
	
}
