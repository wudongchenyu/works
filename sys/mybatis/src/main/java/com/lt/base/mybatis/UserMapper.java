package com.lt.base.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.lt.base.entry.User;

@Mapper
public interface UserMapper {

	@Select(value = { "select * from ibug_user" })
	List<User> findAll();

}
