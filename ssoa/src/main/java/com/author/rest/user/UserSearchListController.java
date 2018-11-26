package com.author.rest.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.author.po.User;
import com.author.service.UserService;
import com.author.util.Result;

@WebServlet(value = "/basic/user/searchList", name = "UserSearchListController")
public class UserSearchListController extends HttpServlet {

	private static final long serialVersionUID = -6549768343495571557L;

	private UserService userService = UserService.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("调用doPost");
		
		String id = request.getParameter("id");
		String user_code = request.getParameter("user_code");
		String user_name = request.getParameter("user_name");
		String tele = request.getParameter("tele");
		String coi = request.getParameter("coi");
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
		
		Result<List<User>> result = userService.list(id, user_code, user_name, tele, coi);
		
	    out.println(JSON.toJSONString(result));
	    out.flush();
	    out.close();
	}
	
}
