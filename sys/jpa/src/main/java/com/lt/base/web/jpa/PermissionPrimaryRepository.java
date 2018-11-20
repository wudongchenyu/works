package com.lt.base.web.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lt.base.entry.Permission;

public interface PermissionPrimaryRepository extends JpaRepository<Permission, Long>{

	Permission findByResourceName(String resourceName);
	
	Permission findByResourceCodeOrResourceString(String resourceCode, String resourceString);

	Permission findById(String resourceId);

	Permission findByIdOrResourceCode(String id,String sourceCode);
}
