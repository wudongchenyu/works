package com.lt.base.entry;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alibaba.fastjson.annotation.JSONField;
import com.lt.base.common.SystemGeneration;

import jdk.jfr.Description;
import lombok.Data;

@Data
@Entity
@Table(name = "ibug_user")
public class User implements UserDetails{
	
	private static final long serialVersionUID = -5143951884356117635L;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Description(value = "主键")
	@Column(nullable = false, unique = true)
	private String id;
	
	@Description(value = "编号")
	@Column(length = 20)
	private String userCode = SystemGeneration.getTimeNumber("U");
	
	@Description(value = "姓名")
	@Column(length = 20)
	private String name;
	
	@Description(value = "密码")
	@Column(length = 80)
	private String pass;
	
	@Description(value = "性别")
	@Column(length = 3)
	private String sex;
	
	@Description(value = "用户类型")
	@Column(length = 3)
	private String type;
	
	@Description(value = "联系方式")
	@Column(length = 15)
	private String tele;
	
	@Column(name="EMAIL", nullable=false)
    private String email;
	
	@Description(value = "地址")
	@Column(length = 100)
	private String address;
	
	@Description(value = "身份证")
	@Column(length = 18)
	private String coi;
	
	@ManyToMany(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.MERGE})
	@JoinTable(name = "ibug_userrole",joinColumns = {@JoinColumn(name = "userid", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "roleid", referencedColumnName = "id"
				)})
	private Set<Role> roles;

	@Description(value = "创建时间")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false)
	@JSONField(format = "yyyy-MM-dd")
	private Date createTime = new Date();
	
	@Description(value = "是否删除")
	@Column(length = 1)
	private String flag = "0";
	
	@Description(value = "备注")
	@Column(length = 1000)
	private String remake;

	@Description(value = "是否禁用")
	@Column(length = 1)
	private String disable = "0";
	
	@Column
	private boolean accountNonExpired;
	
	@Column
	private boolean accountNonLocked;
	
	@Column
	private boolean credentialsNonExpired;
	
	@Column
	private boolean enabled;
	
	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.pass;
	}

	@Override
	public String getUsername() {
		return this.name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
}
