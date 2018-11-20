package com.lt.base.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.base.common.Commons;
import com.lt.base.common.ResultEnum;
import com.lt.base.contants.Result;
import com.lt.base.dto.PermissionAddDto;
import com.lt.base.dto.PermissionTree;
import com.lt.base.entry.Permission;
import com.lt.base.entry.User;
import com.lt.base.mybatis.PermissionMapper;
import com.lt.base.util.ResultUtils;
import com.lt.base.web.jpa.PermissionPrimaryRepository;
import com.lt.base.web.jpa.UserPrimaryRepository;

@Service
public class PermissionService {
	
	private @Autowired PermissionPrimaryRepository permissionPrimaryRepository;
	private @Autowired UserPrimaryRepository userPrimaryRepository;
	
	private @Autowired PermissionMapper permissionMapper;
	
	@Transactional(readOnly = true)
	public Collection<? extends Permission> findAll() {
		return permissionPrimaryRepository.findAll();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<Permission> increase(PermissionAddDto resource) {
		try {
			Permission permission = new Permission();
			cloneAttribute(permission,resource);
			Permission save = permissionPrimaryRepository.save(permission);
			return ResultUtils.success(ResultEnum.SAVE_PERMISSION_SUCCESS, save);
		} catch (Exception e) {
			return ResultUtils.error(ResultEnum.SAVE_PERMISSION_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<Permission> remove(String resourceId) {
		try {
			Permission one = permissionPrimaryRepository.findById(resourceId);
			if (one!=null) {
				permissionPrimaryRepository.delete(one);
			}
			return ResultUtils.success(ResultEnum.DELETE_PERMISSION_SUCCESS, one);
		} catch (Exception e) {
			return ResultUtils.error(ResultEnum.DELETE_PERMISSION_ERROR);
		}
	}
	
	@Transactional(readOnly = true)
	public JSONObject findOneByIdOrResourceCode(String resourceId, JSONObject result) {
		try {
			Permission one = permissionPrimaryRepository.findByIdOrResourceCode(resourceId,resourceId);
			result.put("code", Commons.SUCCESS);
			if (one==null) {
				permissionPrimaryRepository.delete(one);
				result.put("message", "不存在该资源");
				result.put("data", null);
				return result;
			}
			result.put("message", "资源获取成功");
			result.put("data", one);
		} catch (Exception e) {
			result.put("code", Commons.ERROR);
			result.put("message", "资源获取失败");
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject edit(Permission resource, JSONObject result) {
		try {
			Permission old = permissionPrimaryRepository.findByIdOrResourceCode(resource.getId(),resource.getId());
			if (old==null) {
				permissionPrimaryRepository.delete(old);
				result.put("code", Commons.ERROR);
				result.put("message", "不存在该资源");
				result.put("data", resource.getId());
				return result;
			}
			permissionPrimaryRepository.save(resource);
			result.put("code", Commons.SUCCESS);
			result.put("message", "资源修改成功");
		} catch (Exception e) {
			result.put("code", Commons.ERROR);
			result.put("message", "资源修改失败");
		}
		return result;
	}

	public Result<List<Permission>> list() {
		try {
			User user = userPrimaryRepository.findByUserCode(UserUtils.getUserCode());
			
			if (Objects.isNull(user)) {
				return ResultUtils.error(ResultEnum.USER_NON_EXISTENT);
			}
			
			List<Permission> list = new ArrayList<>();
			user.getRoles().forEach(role->{
				role.getResources().forEach(resource->list.add(resource));
			});
			
			if (list.isEmpty()) {
				return ResultUtils.error(ResultEnum.USER_ROLE_NON_EXISTENT);
			}
			
			return ResultUtils.success(ResultEnum.ACHIEVE_PERMISSION_SUCCESS, list);
		} catch (Exception e) {
			return ResultUtils.error(ResultEnum.ACHIEVE_PERMISSION_ERROR);
		}
	}

	public Result<List<Permission>> allList() {
		List<Permission> list = permissionPrimaryRepository.findAll();
//		PageHelper.startPage(0, 10);
//		List<PermissionTree> findAll = permissionMapper.findAll();
//		PageInfo<PermissionTree> trees = new PageInfo<PermissionTree>(findAll);
		
		return ResultUtils.success(ResultEnum.ACHIEVE_PERMISSION_SUCCESS, list);
	}
	
	private void cloneAttribute(Permission clone, PermissionAddDto beCloned) {
		clone.setMethodName(beCloned.getMethodName().equals("") ? null : beCloned.getMethodName());
		clone.setMethodPath(beCloned.getMethodPath().equals("") ? null : beCloned.getMethodPath());
		clone.setParentCode(beCloned.getParentCode().equals("") ? null : beCloned.getParentCode());
		clone.setRemake(beCloned.getRemake().equals("") ? null : beCloned.getRemake());
		clone.setResourceName(beCloned.getResourceName().equals("") ? null : beCloned.getResourceName());
		clone.setResourceString(beCloned.getResourceString().equals("") ? null : beCloned.getResourceString());
		clone.setIcon(beCloned.getIcon());
		clone.setResourceOrder(beCloned.getOrder());
		clone.setName(beCloned.getName());
	}
	
	private List<PermissionTree> getChild(String code, List<PermissionTree> trees) {
	    // 子菜单
	    List<PermissionTree> childList = new ArrayList<>();
	    for (PermissionTree menu : trees) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
            if (menu.getParentCode().equals(code)) {
                childList.add(menu);
            }
	    }
	    // 把子菜单的子菜单再循环一遍
	    for (PermissionTree menu : childList) {// 没有url子菜单还有子菜单
	            // 递归
	    	menu.setNodes(getChild(menu.getCode(), trees));
	    } // 递归退出条件
	    if (childList.size() == 0) {
	        return null;
	    }
	    return childList;
	}

	public Result<List<PermissionTree>> allTreeList() {
		List<PermissionTree> trees = permissionMapper.findAll();
		List<PermissionTree> treeList = this.getChild("0", trees);
		
		return ResultUtils.success(ResultEnum.ACHIEVE_PERMISSION_SUCCESS, treeList);
	}
	
}
