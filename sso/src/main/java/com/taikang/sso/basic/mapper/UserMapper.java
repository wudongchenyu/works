package com.taikang.sso.basic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.taikang.sso.basic.po.User;

@Mapper
public interface UserMapper {

	@Select(value = { "select * from sso_user" })
	List<User> findAll();

}
