package com.lt.base.web.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lt.base.entry.User;

public interface UserPrimaryRepository extends JpaRepository<User, Long>{

	User findByName(String name);
	
	User findByNameOrUserCode(String name, String userCode);

	User findByUserCode(String userCode);

	User findByUserCodeOrNameOrEmail(String userCode, String name, String email);
	
}
