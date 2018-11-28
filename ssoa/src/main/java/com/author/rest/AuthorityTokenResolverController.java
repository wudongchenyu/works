package com.author.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.author.service.LoginService;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.mysql.cj.util.StringUtils;

@WebServlet(value = "/basic/authority/token/resolver" ,name = "AuthorityTokenResolverController")
public class AuthorityTokenResolverController extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8115258904885541633L;
	private LoginService loginService = LoginService.getInstance();

	/**
		 * Constructor of the object.
		 */
	public AuthorityTokenResolverController() {
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
		this.doPost(request, response);
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
		System.out.println("调用/basic/authority/token/resolver");
		
		String token = request.getParameter("authorityToken");
		System.out.println("token:" + token);
		
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
		if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
			out.println(ResultUtils.success(ResultEnum.TOKEN_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		Result<List<String>> result = loginService.resolverAuthorityToken(token);
	    out.println(JSON.toJSONString(result));
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
