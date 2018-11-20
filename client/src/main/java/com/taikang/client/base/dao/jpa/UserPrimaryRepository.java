package com.taikang.client.base.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taikang.client.base.po.User;

public interface UserPrimaryRepository extends JpaRepository<User, Long>{

	User findByUserName(String userName);
	
	User findById(String id);
	
	User findByIdOrNameOrTeleOrCoi(String id, String name, String tele, String coi);
	
	User findByNameOrUserCode(String name, String userCode);

	User findByUserCode(String userCode);

	User findByUserCodeOrName(String userCode, String name);
	
}
