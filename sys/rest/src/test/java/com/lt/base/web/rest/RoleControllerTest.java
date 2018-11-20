package com.lt.base.web.rest;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lt.base.RestApplication;
import com.lt.base.entry.Permission;
import com.lt.base.entry.Role;
import com.lt.base.service.PermissionService;
import com.lt.base.service.RoleService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RestApplication.class)
@AutoConfigureTestDatabase(replace=Replace.NONE)
class RoleControllerTest {
	
	private @Autowired RoleService roleService;
	
	private @Autowired PermissionService permissionService;

	@Test
	void testIncrease() {
		Role role = roleService.findByRoleCode("R20180622084351085");
		Set<Permission> resources = new HashSet<>();
		resources.addAll(permissionService.findAll());
		role.setResources(resources);
		roleService.increase(role);
	}

}
