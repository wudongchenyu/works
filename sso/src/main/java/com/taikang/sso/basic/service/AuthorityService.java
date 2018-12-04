package com.taikang.sso.basic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.taikang.result.basic.commons.Result;
import com.taikang.result.basic.commons.enums.ResultEnum;
import com.taikang.result.basic.util.ResultUtils;
import com.taikang.sso.basic.jpa.AuthorityRepository;
import com.taikang.sso.basic.po.Authority;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthorityService {
	
	private @Autowired AuthorityRepository authorityRepository;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Result<Authority> add(String authorityName, String subordinate, String authorityUrl) {
		try {
			Authority authority = new Authority();
			authority.setAuthorityName(authorityName);
			authority.setAuthorityUrl(authorityUrl);
			authority.setSubordinate(subordinate);
			Authority save = authorityRepository.save(authority);
			return ResultUtils.success(ResultEnum.SAVE_PERMISSION_SUCCESS, save);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResultUtils.success(ResultEnum.SAVE_PERMISSION_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<String> delete(String id) {
		try {
			Authority authority = authorityRepository.findById(id);
			if (null == authority) {
				return ResultUtils.success(ResultEnum.AUTHORITY_NON_EXISTENT);
			}
			authority.setEnabled(false);
			authorityRepository.save(authority);
			return ResultUtils.success(ResultEnum.DELETE_AUTHORITY_SUCCESS);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResultUtils.success(ResultEnum.DELETE_AUTHORITY_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<Authority> edit(String id, String authorityName, String subordinate, String authorityUrl) {
		try {
			Authority authority = authorityRepository.findById(id);
			if (null == authority) {
				return ResultUtils.success(ResultEnum.AUTHORITY_NON_EXISTENT);
			}
			authority.setAuthorityName(authorityName);
			authority.setAuthorityUrl(authorityUrl);
			authority.setSubordinate(subordinate);
			authorityRepository.save(authority);
			return ResultUtils.success(ResultEnum.EDIT_AUTHORITY_SUCCESS);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResultUtils.success(ResultEnum.EDIT_AUTHORITY_ERROR);
		}
	}

	public Result<Authority> detail(String id) {
		try {
			Authority authority = authorityRepository.findById(id);
			if (null == authority) {
				return ResultUtils.success(ResultEnum.AUTHORITY_NON_EXISTENT);
			}
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORITY_SUCCESS,authority);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORITY_ERROR);
		}
	}

	public Result<List<Authority>> list(String id) {
		try {
			List<Authority> list = authorityRepository.findAllByEnabled(true);
			if (null == list || list.isEmpty()) {
				return ResultUtils.success(ResultEnum.AUTHORITY_NON_EXISTENT);
			}
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORITY_SUCCESS,list);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORITY_ERROR);
		}
	}

}
