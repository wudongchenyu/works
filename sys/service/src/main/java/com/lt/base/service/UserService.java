package com.lt.base.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lt.base.common.ResultEnum;
import com.lt.base.contants.Result;
import com.lt.base.entry.User;
import com.lt.base.mybatis.UserMapper;
import com.lt.base.util.ResultUtils;
import com.lt.base.web.jpa.UserPrimaryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	private @Resource UserPrimaryRepository userPrimaryRepository;
	
	private @Autowired UserMapper userMapper;

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<User> addUser(User user, Result<User> result) {
		try {
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
	public User findByUserCodeOrNameOrEmail(String userCode, String name, String email) {
		return userPrimaryRepository.findByUserCodeOrNameOrEmail(userCode, name, email);
	}

//	@Transactional(readOnly = true)
	public Result<List<User>> userList(Result<List<User>> result) {
		List<User> list = userMapper.findAll();
		return ResultUtils.success(ResultEnum.SEARCH_USER_SUCCESS, list);
	}

}
