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
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.mysql.cj.util.StringUtils;

@WebServlet("/user/basic")
public class UserController extends HttpServlet {
	
	private UserService userService = UserService.getInstance();

	/**
	 * 
	 */
	private static final long serialVersionUID = 5659158303164911448L;

	/**
		 * Constructor of the object.
		 */
	public UserController() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy();
	}

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
	    out.println(JSON.toJSON(result).toString());
	    out.flush();
	    out.close();
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
			out.println(ResultUtils.error(ResultEnum.USERNAME_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(pass)) {
			out.println(ResultUtils.error(ResultEnum.PASSWORD_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		Result<User> result = userService.add(userName, pass, tele, coi, name);
		
	    out.println(JSON.toJSON(result).toString());
	    out.flush();
	    out.close();
	}
	
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("调用doPost");
		
		String id = request.getParameter("id");
		String userName = request.getParameter("userName");
		String name = request.getParameter("name");
		String tele = request.getParameter("tele");
		String coi = request.getParameter("coi");
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
	    
		if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
			out.println(ResultUtils.error(ResultEnum.USERNAME_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		Result<User> result = userService.edit(userName, tele, coi, name, id);
		
	    out.println(JSON.toJSON(result).toString());
	    out.flush();
	    out.close();
	}
	
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("调用doPost");
		
		String id = request.getParameter("id");
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
	    
		if (StringUtils.isEmptyOrWhitespaceOnly(id)) {
			out.println(ResultUtils.error(ResultEnum.USERID_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		Result<String> result = userService.delete(id);
		
	    out.println(JSON.toJSON(result).toString());
	    out.flush();
	    out.close();
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}

}
