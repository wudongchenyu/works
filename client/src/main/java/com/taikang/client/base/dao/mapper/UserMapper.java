package com.taikang.client.base.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.taikang.client.base.po.User;

@Mapper
public interface UserMapper {

	@Select(value = { "select * from ibug_user" })
	List<User> findAll();

}
