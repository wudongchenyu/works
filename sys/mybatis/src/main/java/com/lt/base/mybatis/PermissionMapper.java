package com.lt.base.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.lt.base.dto.PermissionTree;

@Mapper
public interface PermissionMapper {
	
	@Select(value = { "select * from ibug_resource order by resource_order" })
	@Results(id = "treeMap",value = {
		@Result(column = "id", property = "id"),
		@Result(column = "name", property = "text"),
		@Result(column = "icon", property = "icon"),
		@Result(column = "resource_name", property = "href"),
		@Result(column = "resource_code", property = "code"),
		@Result(column = "parent_code", property = "parentCode"),
		@Result(column = "resource_order", property = "order"),
		@Result(column = "remake", property = "tags")
	})
	List<PermissionTree> findAll();

}
