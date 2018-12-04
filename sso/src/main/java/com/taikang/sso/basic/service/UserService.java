package com.taikang.sso.basic.service;

import java.util.List;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.taikang.result.basic.commons.Result;
import com.taikang.result.basic.commons.enums.ResultEnum;
import com.taikang.result.basic.util.ResultUtils;
import com.taikang.sso.basic.dto.UserResult;
import com.taikang.sso.basic.jpa.UserRepository;
import com.taikang.sso.basic.mapper.UserMapper;
import com.taikang.sso.basic.po.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	private @Autowired UserRepository userRepository;
	
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
			User save = userRepository.save(user);
			return ResultUtils.success(ResultEnum.SAVE_USER_SUCCESS, save);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResultUtils.success(ResultEnum.SAVE_USER_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User findByUserCode(String userCode) {
		return userRepository.findByUserCode(userCode);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User findByUserCodeOrName(String userCode, String name) {
		return userRepository.findByUserCodeOrName(userCode, name);
	}

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
			User user = userRepository.findById(id);
			if (null == user) {
				return ResultUtils.success(ResultEnum.USER_NON_EXISTENT);
			}
			user.setEnabled(false);
			userRepository.save(user);
			return ResultUtils.success(ResultEnum.DELETE_USER_SUCCESS);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResultUtils.success(ResultEnum.DELETE_USER_ERROR);
		}
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<User> editUser(String id, String userName, String pass, String tele, String coi) {
		try {
			User user = userRepository.findById(id);
			if (null == user) {
				return ResultUtils.success(ResultEnum.USER_NON_EXISTENT);
			}
			user.setCoi(coi);
			user.setName(userName);
			user.setPass(pass);
			user.setTele(tele);
			userRepository.save(user);
			return ResultUtils.success(ResultEnum.EDIT_USER_SUCCESS);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResultUtils.success(ResultEnum.EDIT_USER_ERROR);
		}
	}

	public Result<UserResult> detail(String userName) {
		User user = userRepository.findByUserName(userName);
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
