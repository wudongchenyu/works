package com.taikang.sso.basic.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taikang.sso.basic.po.UserAuthority;

public interface UserAuthorityRespository extends JpaRepository<UserAuthority, Long>{

}
