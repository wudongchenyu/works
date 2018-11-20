package com.lt.base.outrest.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lt.base.contants.Result;
import com.lt.base.entry.Role;
import com.lt.base.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(tags = "角色信息相关API")
@RestController
@RequestMapping("/role")
public class RoleController {
	
	private @Autowired RoleService roleService;
	
	@ApiOperation(value = "添加角色", notes = "添加角色信息")
	@ApiImplicitParam(name = "user", value = "User", required = true, dataType = "User")
	@PostMapping("/increaseRole")
	public Result<Role> increase(Role role) {
		Result<Role> result = new Result<Role>();
		result = roleService.increase(role);
		return result;
	}

	@ApiOperation(value = "删除角色", notes = "添加角色信息")
	@ApiImplicitParam(name = "user", value = "User", required = true, dataType = "User")
	@PostMapping("/removeRole")
	public Result<Role> remove(String roleId) {
		Result<Role> result = new Result<Role>();
		result = roleService.remove(roleId);
		return result;
	}
	
}
