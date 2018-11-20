package com.lt.base.web.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lt.base.RestApplication;
import com.lt.base.dto.PermissionAddDto;
import com.lt.base.service.PermissionService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RestApplication.class)
@AutoConfigureTestDatabase(replace=Replace.NONE)
class PermissionControllerTest {
	
	private @Autowired PermissionService permissionService;

	@Test
	void testIncrease() {
		PermissionAddDto add = new PermissionAddDto();
		add.setIcon("glyphicon glyphicon-triangle-right");
		add.setMethodName("list");
		add.setMethodPath("com.lt.base.web.rest.RoleController");
		add.setName("角色列表");
		add.setOrder(3);
		add.setParentCode("RS20180807110423652");
		add.setRemake("角色列表");
		add.setResourceName("/role/list/");
		add.setResourceString("ROLE_LIST_ROLE");
		permissionService.increase(add);
	}

	@Test
	void testRemove() {
	}

	@Test
	void testList() {
	}

	@Test
	void testAllTreeList() {
	}

	@Test
	void testAllList() {
	}

}
