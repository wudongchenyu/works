package com.taikang.sso.basic.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taikang.sso.basic.po.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByUserName(String userName);
	
	User findByUserNameAndEnabled(String userName,Boolean enabled);
	
	User findById(String id);
	
	User findByIdOrNameOrTeleOrCoi(String id, String name, String tele, String coi);
	
	User findByNameOrUserCode(String name, String userCode);

	User findByUserCode(String userCode);

	User findByUserCodeOrName(String userCode, String name);
	
}
