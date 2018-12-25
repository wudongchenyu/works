package com.taikang.app.base.dto.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taikang.sso.basic.po.Authority;

public interface AuthorityPrimaryRepository extends JpaRepository<Authority	, Long>{

	List<Authority> findAllByEnabled(Boolean enabled);
	
}
