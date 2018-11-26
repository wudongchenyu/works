package com.author.rest.authority;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.author.po.Authority;
import com.author.service.AuthorityService;
import com.author.util.Result;

@WebServlet(value = "/basic/authority/searchList" ,name = "AuthoritySearchListController")
public class AuthoritySearchListController extends HttpServlet {
	
	private AuthorityService authorityService = AuthorityService.getInstance();

	/**
	 * 
	 */
	private static final long serialVersionUID = 5532126198274241253L;

	/**
		 * Constructor of the object.
		 */
	public AuthoritySearchListController() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
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
		System.out.println("调用doGet");
		
		String userId = request.getParameter("userId");
		String id = request.getParameter("id");
		String authorityCode = request.getParameter("authorityCode");
		String authorityName = request.getParameter("authorityName");
		String enabled = request.getParameter("enabled");
		
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
		
		Result<List<Authority>> result = authorityService.getAllAuthority(userId, id, authorityCode, authorityName, enabled);
		
	    out.println(JSON.toJSONString(result));
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
		
		String userId = request.getParameter("userId");
		String id = request.getParameter("id");
		String authorityCode = request.getParameter("authorityCode");
		String authorityName = request.getParameter("authorityName");
		String enabled = request.getParameter("enabled");
		
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
		
		Result<List<Authority>> result = authorityService.getAllAuthority(userId, id, authorityCode, authorityName, enabled);
		
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