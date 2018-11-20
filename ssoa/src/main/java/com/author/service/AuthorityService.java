package com.author.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.author.po.Authority;
import com.author.util.DataSourceUtils;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;

public class AuthorityService {
	
	private static AuthorityService authorityService;
	
	private AuthorityService() {
		
	}
	
	public static synchronized AuthorityService getInstance() {
		if (null == authorityService) {
			authorityService = new AuthorityService();
		}
		return authorityService;
	}

	public Result<List<Authority>> getAllAuthority(String userId) {
		List<Authority> list = new ArrayList<Authority>();
		try {
			DruidPooledConnection connection = DataSourceUtils.openConnection();
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement("select sa.id,sa.authority_name,sa.authority_code,sa.subordinate,sa.authority_url,"
					+ "sa.enabled,sa.create_time from  sso_user_authorty sua left join sso_authority sa on sa.id=sua.authorty_id"
					+ " where sa.enabled = 1 and sua.user_id=?");
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				Authority authority = new Authority();
				authority.setId(rs.getString("id"));
				authority.setCreateTime(rs.getDate("create_time"));
				authority.setEnabled(rs.getBoolean("enabled"));
				authority.setAuthorityCode(rs.getString("authority_code"));
				authority.setAuthorityName(rs.getString("authority_name"));
				authority.setAuthorityUrl(rs.getString("authority_url"));
				authority.setSubordinate(rs.getString("subordinate"));
				list.add(authority);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			return ResultUtils.error(ResultEnum.SEARCH_AUTHORTY_ERROR);
		}
		return ResultUtils.success(ResultEnum.SEARCH_AUTHORTY_SUCCESS, list);
	}
	
}
