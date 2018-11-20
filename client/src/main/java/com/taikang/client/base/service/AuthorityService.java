package com.taikang.client.base.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.taikang.client.base.commons.Result;
import com.taikang.client.base.commons.ResultEnum;
import com.taikang.client.base.commons.ResultUtils;
import com.taikang.client.base.dao.jpa.AuthorityPrimaryRepository;
import com.taikang.client.base.po.Authority;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthorityService {
	
	private AuthorityPrimaryRepository authorityPrimaryRepository;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Result<Authority> add(String authorityName, String subordinate, String authorityUrl) {
		try {
			Authority authority = new Authority();
			authority.setAuthorityName(authorityName);
			authority.setAuthorityUrl(authorityUrl);
			authority.setSubordinate(subordinate);
			Authority save = authorityPrimaryRepository.save(authority);
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
			Authority authority = authorityPrimaryRepository.findById(id);
			if (null == authority) {
				return ResultUtils.success(ResultEnum.AUTHORITY_NON_EXISTENT);
			}
			authority.setEnabled(false);
			authorityPrimaryRepository.save(authority);
			return ResultUtils.success(ResultEnum.DELETE_AUTHORITY_SUCCESS);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResultUtils.success(ResultEnum.DELETE_AUTHORITY_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Result<Authority> edit(String id, String authorityName, String subordinate, String authorityUrl) {
		try {
			Authority authority = authorityPrimaryRepository.findById(id);
			if (null == authority) {
				return ResultUtils.success(ResultEnum.AUTHORITY_NON_EXISTENT);
			}
			authority.setAuthorityName(authorityName);
			authority.setAuthorityUrl(authorityUrl);
			authority.setSubordinate(subordinate);
			authorityPrimaryRepository.save(authority);
			return ResultUtils.success(ResultEnum.EDIT_AUTHORITY_SUCCESS);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResultUtils.success(ResultEnum.EDIT_AUTHORITY_ERROR);
		}
	}

	public Result<Authority> detail(String id) {
		try {
			Authority authority = authorityPrimaryRepository.findById(id);
			if (null == authority) {
				return ResultUtils.success(ResultEnum.AUTHORITY_NON_EXISTENT);
			}
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORITY_SUCCESS,authority);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORITY_ERROR);
		}
	}

	public Result<List<Authority>> list(String id) {
		try {
			List<Authority> list = authorityPrimaryRepository.findAllByEnabled(true);
			if (null == list || list.isEmpty()) {
				return ResultUtils.success(ResultEnum.AUTHORITY_NON_EXISTENT);
			}
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORITY_SUCCESS,list);
		} catch (Exception e) {
			return ResultUtils.success(ResultEnum.SEARCH_AUTHORITY_ERROR);
		}
	}

}
