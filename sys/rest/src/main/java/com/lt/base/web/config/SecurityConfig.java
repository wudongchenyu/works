package com.lt.base.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.lt.base.web.handler.LoginFailureHandler;
import com.lt.base.web.handler.LoginSuccessHandler;
import com.lt.base.web.inter.MyAbstractSecurityInterceptor;

/**
 * 第一步创建spring security javaconfig
 * @author gws
 *
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.lt.base.web.rest"})
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private MyAbstractSecurityInterceptor myAbstractSecurityInterceptor;
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private LoginFailureHandler failureHandler;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("*.jsp","swagger*.*","*.css","*.js","*.html");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new Pbkdf2PasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.addFilterBefore(myAbstractSecurityInterceptor, FilterSecurityInterceptor.class)
			.authorizeRequests()
				.antMatchers("/loginForm/","/login/","/logout/","/user/show2","/user/upload","/upload/up","/webjars/*","/swagger-resources","/swagger-resources/*","/swagger-ui.html").permitAll()
				.antMatchers("/user/*","/show/*")
				.authenticated()
				.antMatchers("/user/addUser/").hasRole("ADD_USER")
				.antMatchers("/user/show1/").hasRole("ADMIN")
				.antMatchers("/show/").hasRole("SHOW")
				.antMatchers("/center/").hasRole("CENTER")
				.antMatchers("/permission/increaseResource/").hasRole("INCREASE_RESOURCE")
				.antMatchers("/permission/removeResource/").hasRole("REMOVE_RESOURCE")
				.antMatchers("/role/removeRole/").hasRole("REMOVE_ROLE")
				.antMatchers("/role/list/").hasRole("LIST_ROLE")
				.antMatchers("/role/increaseRole/").hasRole("INCREASE_ROLE")
				.antMatchers("/user/list/").hasRole("USER_LIST")
				.antMatchers("/permission/allList/").hasRole("ALL_RESOURCE")
				.antMatchers("/permission/allTreeList/").hasRole("ALL_TREE_RESOURCE")
				.and()
			.formLogin()
				.loginPage("/loginForm")
				.loginProcessingUrl("/login")
				.usernameParameter("userCode")
				.passwordParameter("pass")
				.permitAll()
				.successHandler(successHandler)
				.failureHandler(failureHandler)
				
				.and()
			.httpBasic()
				
				.and()
			.logout()
				.logoutUrl("/logout")
				.permitAll()
				.and()
			.csrf()
				.disable()
				.headers()
				.frameOptions()
				.sameOrigin()
				
				.and()
			.sessionManagement()
				.sessionFixation()
				.changeSessionId()
				.maximumSessions(1)
				.expiredUrl("/")
		;
	}
	
}
