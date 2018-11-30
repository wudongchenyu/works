package com.taikang.sso.basic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserAuthorityMapper {
	
	@Select(value = { "select authority_url from sso_user_authority sua left join sso_authority sa on sa.id=sua.authority_id where user_id=#{userId}" })
	List<String> findAllByUserId(String userId);

}
