package com.lt.base.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.base.common.Commons;
import com.lt.base.common.ResultEnum;
import com.lt.base.contants.Result;
import com.lt.base.entry.Role;
import com.lt.base.util.ResultUtils;
import com.lt.base.web.jpa.RolePrimaryRepository;

@Service
public class RoleService {
	
	private @Autowired RolePrimaryRepository rolePrimaryRepository;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject addRole(Role role) {
		JSONObject result = new JSONObject();
		try {
			Role save = rolePrimaryRepository.save(role);
			result.put("code", Commons.SUCCESS);
			result.put("message", "保存角色成功");
			result.put("id", save.getId());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", Commons.ERROR);
			result.put("message", "保存角色失败");
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Role findByRoleCode(String roleCode) {
		return rolePrimaryRepository.findByRoleCode(roleCode);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Collection<? extends Role> findAll() {
		return rolePrimaryRepository.findAll();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<Role> remove(String roleId) {
		try {
			Role one = rolePrimaryRepository.findById(roleId);
			if (one!=null) {
				rolePrimaryRepository.delete(one);
			}
			return ResultUtils.success(ResultEnum.DELETE_ROLE_SUCCESS, one);
		} catch (Exception e) {
			return ResultUtils.error(ResultEnum.DELETE_ROLE_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<Role> increase(Role role) {
		try {
			Role save = rolePrimaryRepository.save(role);
			return ResultUtils.success(ResultEnum.SAVE_ROLE_SUCCESS, save);
		} catch (Exception e) {
			return ResultUtils.error(ResultEnum.SAVE_ROLE_ERROR);
		}
	}

	@Transactional(readOnly = true)
	public Result<List<Role>> getList() {
		List<Role> findAll = rolePrimaryRepository.findAll();
		return ResultUtils.success(ResultEnum.SEARCH_ROLE_SUCCESS, findAll);
	}

}
