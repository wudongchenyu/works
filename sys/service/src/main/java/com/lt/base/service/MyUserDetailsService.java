 package com.lt.base.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lt.base.dto.LoginUser;
import com.lt.base.entry.Permission;
import com.lt.base.entry.Role;
import com.lt.base.entry.User;

import lombok.extern.slf4j.Slf4j;

@Service("userDetailsService")
@Slf4j
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String number) throws UsernameNotFoundException {
		User user = userService.findByUserCodeOrNameOrEmail(number,number,number);
		if(user==null){
            log.info("用户不存在");
            throw new UsernameNotFoundException("用户不存在");
        }
		
		LoginUser loginUser = new LoginUser();
		loginUser.setAccountNonExpired(true);
		loginUser.setAccountNonLocked(true);
		loginUser.setAuthorities(getGrantedAuthorities(user));
		loginUser.setCredentialsNonExpired(true);
		loginUser.setEnabled(true);
		loginUser.setName(user.getName());
		loginUser.setType(user.getType());
		loginUser.setUserCode(user.getUserCode());
		loginUser.setPass(user.getPass());
		return loginUser;
//		return new org.springframework.security.core.userdetails.User(user.getUserCode(), user.getPassword(), 
//                true, true, true, true, getGrantedAuthorities(user));
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        Set<GrantedAuthority> set = new HashSet<GrantedAuthority>();
        log.info("根据用户获取角色");
        Set<Role> roles = user.getRoles();
        log.info("遍历角色");
        roles.forEach(role->{
        	log.info("根据角色获取资源");
        	Set<Permission> resources = role.getResources();
        	log.info("根据资源获取权限");
        	resources.forEach(resource->{
        		log.info(resource.getAuthority());
        		if (Objects.nonNull(resource.getAuthority()) && !resource.getAuthority().equals("")) {
        			set.add(new SimpleGrantedAuthority(resource.getAuthority()));
				}
        	});
        });
        authorities.addAll(set);
        return authorities;
    }

}
