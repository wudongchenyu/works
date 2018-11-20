package com.taikang.client.base.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taikang.client.base.commons.Result;
import com.taikang.client.base.commons.ResultEnum;
import com.taikang.client.base.commons.ResultUtils;
import com.taikang.client.base.dao.jpa.UserPrimaryRepository;
import com.taikang.client.base.dao.mapper.UserMapper;
import com.taikang.client.base.dto.UserResult;
import com.taikang.client.base.po.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	private @Resource UserPrimaryRepository userPrimaryRepository;
	
	private @Autowired UserMapper userMapper;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Result<User> addUser(String userName, String pass, String tele, String coi) {
		try {
			User user = new User();
			user.setCoi(coi);
			user.setName(userName);
			user.setUserName(userName);
			user.setTele(tele);
			user.setPass(BCrypt.hashpw(pass, BCrypt.gensalt()));
			User save = userPrimaryRepository.save(user);
			return ResultUtils.success(ResultEnum.SAVE_USER_SUCCESS, save);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return ResultUtils.success(ResultEnum.SAVE_USER_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User findByUserCode(String userCode) {
		return userPrimaryRepository.findByUserCode(userCode);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User findByUserCodeOrName(String userCode, String name) {
		return userPrimaryRepository.findByUserCodeOrName(userCode, name);
	}

//	@Transactional(readOnly = true)
	public Result<List<UserResult>> userList() {
		List<User> list = userMapper.findAll();
		
		List<UserResult> collect = list
				.parallelStream()
				.map(user->this.createUserResult(user))
				.collect(Collectors.toList());
		
		return ResultUtils.success(ResultEnum.SEARCH_USER_SUCCESS, collect);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<User> deleteUser(String id) {
		try {
			User user = userPrimaryRepository.findById(id);
			if (null == user) {
				return ResultUtils.success(ResultEnum.USER_NON_EXISTENT);
			}
			user.setEnabled(false);
			userPrimaryRepository.save(user);
			return ResultUtils.success(ResultEnum.DELETE_USER_SUCCESS);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.DELETE_USER_ERROR);
		}
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<User> editUser(String id, String userName, String pass, String tele, String coi) {
		try {
			User user = userPrimaryRepository.findById(id);
			if (null == user) {
				return ResultUtils.success(ResultEnum.USER_NON_EXISTENT);
			}
			user.setCoi(coi);
			user.setName(userName);
			user.setPass(pass);
			user.setTele(tele);
			userPrimaryRepository.save(user);
			return ResultUtils.success(ResultEnum.EDIT_USER_SUCCESS);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.EDIT_USER_ERROR);
		}
	}

	public Result<UserResult> detail(String userName) {
		User user = userPrimaryRepository.findByUserName(userName);
		if (null == user) {
			return ResultUtils.success(ResultEnum.DETAIL_USER_ERROR);
		}
		UserResult result = new UserResult();
		result.setAccountNonExpired(user.isAccountNonExpired());
		result.setAccountNonLocked(user.isAccountNonLocked());
		result.setCoi(user.getCoi());
		result.setCreateTime(user.getCreateTime());
		result.setCredentialsNonExpired(user.isCredentialsNonExpired());
		result.setEnabled(user.isEnabled());
		result.setId(user.getId());
		result.setName(user.getName());
		result.setTele(user.getTele());
		result.setUserCode(user.getUserCode());
		result.setUserName(user.getUserName());
		return ResultUtils.success(ResultEnum.DETAIL_USER_SUCCESS, result);
	}
	
	private UserResult createUserResult(User user) {
		UserResult result = new UserResult();
		result.setAccountNonExpired(user.isAccountNonExpired());
		result.setAccountNonLocked(user.isAccountNonLocked());
		result.setCoi(user.getCoi());
		result.setCreateTime(user.getCreateTime());
		result.setCredentialsNonExpired(user.isCredentialsNonExpired());
		result.setEnabled(user.isEnabled());
		result.setId(user.getId());
		result.setName(user.getName());
		result.setTele(user.getTele());
		result.setUserCode(user.getUserCode());
		result.setUserName(user.getUserName());
		return result;
	}

}
