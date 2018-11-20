package com.lt.base.web.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lt.base.entry.Role;

public interface RolePrimaryRepository extends JpaRepository<Role, Long>{

	Role findByName(String name);

	Role findByRoleCode(String roleCode);
	
	Role findByRoleCodeOrName(String roleCode, String name);

	Role findById(String roleId);
	
}
