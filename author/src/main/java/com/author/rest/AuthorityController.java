package com.author.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;

@WebServlet("/authority")
public class AuthorityController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5532126198274241253L;

	/**
		 * Constructor of the object.
		 */
	public AuthorityController() {
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

		System.out.println("调用doPost");
		
		String userName = request.getParameter("userName");
		String password = request.getParameter("passWord");
		
		response.setContentType("text/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
	    JSONObject object = new JSONObject();
	    object.put("class", this.getClass());
	    object.put("method", "doPost");
	    
		if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
			object.put("error", "用户名不能为空");
			out.println(object);
		    out.flush();
		    out.close();
		    return;
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(password)) {
			object.put("error", "密码不能为空");
			out.println(object);
		    out.flush();
		    out.close();
		    return;
		}
		
	    out.println(object);
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
