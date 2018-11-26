package com.author.rest.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.author.po.User;
import com.author.service.UserService;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.mysql.cj.util.StringUtils;

@WebServlet(value = "/basic/user/add", name = "UserAddController")
public class UserAddController extends HttpServlet{

	private static final long serialVersionUID = -7807602351661839813L;

	private UserService userService = UserService.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("调用doPost");
		String userName = request.getParameter("userName");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String tele = request.getParameter("tele");
		String coi = request.getParameter("coi");
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
	    
		if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
			out.println(ResultUtils.paramError(ResultEnum.USERNAME_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(pass)) {
			out.println(ResultUtils.paramError(ResultEnum.PASSWORD_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		Result<User> result = userService.add(userName, pass, tele, coi, name);
		
	    out.println(JSON.toJSONString(result));
	    out.flush();
	    out.close();
	}
	
}
