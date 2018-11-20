package com.taikang.client.base.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taikang.client.base.po.Authority;

public interface AuthorityPrimaryRepository extends JpaRepository<Authority, Long>{

	Authority findById(String id);
	
	List<Authority> findAllByEnabled(Boolean enabled);

}
