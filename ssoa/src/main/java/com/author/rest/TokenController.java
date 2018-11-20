package com.author.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.author.service.LoginService;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.mysql.cj.util.StringUtils;

@WebServlet("/checkToken")
public class TokenController extends HttpServlet {

	private LoginService loginService = LoginService.getInstance();

	/**
	 * 
	 */
	private static final long serialVersionUID = -3267319997956805877L;

	/**
	 * Constructor of the object.
	 */
	public TokenController() {
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
	 * @param request  the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException      if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request  the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException      if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("调用doPost");
		String token = request.getParameter("token");
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
			out.println(ResultUtils.success(ResultEnum.CANCEL_TOKEN_ERROR));
			out.flush();
			out.close();
			return;
		}
		out.println(loginService.checkToken(token));
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
