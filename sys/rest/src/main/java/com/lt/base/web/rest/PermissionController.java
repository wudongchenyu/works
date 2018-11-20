package com.lt.base.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lt.base.contants.Result;
import com.lt.base.dto.PermissionAddDto;
import com.lt.base.dto.PermissionTree;
import com.lt.base.entry.Permission;
import com.lt.base.service.PermissionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "权限信息相关API")
@RestController
@RequestMapping("/permission")
public class PermissionController {
	
	private @Autowired PermissionService resourceService;
	
	@ApiOperation(value = "添加资源", notes = "添加资源信息")
	@PostMapping("/increaseResource")
	public Result<Permission> increase(
			@RequestBody 
			@ApiParam(name="资源对象",value="传入json格式",required=true)
			PermissionAddDto resource) {
		Result<Permission> result = new Result<Permission>();
		resourceService.increase(resource);
		return result;
	}

	@ApiOperation(value = "删除资源", notes = "删除资源信息")
	@ApiImplicitParam(name = "resourceId", value = "resourceId", required = true, dataType = "String")
	@PostMapping("/removeResource")
	public Result<Permission> remove(String resourceId) {
		Result<Permission> result = new Result<Permission>();
		resourceService.remove(resourceId);
		return result;
	}
	
	@ApiOperation(value = "用户资源列表", notes = "用户资源列表信息")
	@PostMapping("/listResource")
	public Result<List<Permission>> list() {
		Result<List<Permission>> result = new Result<List<Permission>>();
		result = resourceService.list();
		return result;
	}
	
	@ApiOperation(value = "全部资源列表树形结构", notes = "全部资源列表信息树形结构")
	@PostMapping("/allTreeList")
	public Result<List<PermissionTree>> allTreeList() {
		Result<List<PermissionTree>> result = new Result<List<PermissionTree>>();
		result = resourceService.allTreeList();
		return result;
	}
	
	@ApiOperation(value = "全部资源列表", notes = "全部资源列表信息")
	@PostMapping("/allList")
	public Result<List<Permission>> allList() {
		Result<List<Permission>> result = new Result<List<Permission>>();
		result = resourceService.allList();
		return result;
	}
	
}
