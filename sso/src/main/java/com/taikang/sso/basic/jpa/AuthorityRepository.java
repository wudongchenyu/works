package com.taikang.sso.basic.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taikang.sso.basic.po.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{

	Authority findById(String id);
	
	List<Authority> findAllByEnabled(Boolean enabled);

}
