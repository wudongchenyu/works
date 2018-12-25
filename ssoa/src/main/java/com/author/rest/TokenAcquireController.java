package com.author.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.author.service.LoginService;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.mysql.cj.util.StringUtils;

@WebServlet(value = "/basic/token/acquire", name = "TokenAcquireController")
public class TokenAcquireController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5156026892717957263L;
	private LoginService loginService = LoginService.getInstance();

	/**
	 * Constructor of the object.
	 */
	public TokenAcquireController() {
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
		System.out.println("调用/basic/token/generate");

		String code = request.getParameter("code");
		
		if (StringUtils.isEmptyOrWhitespaceOnly(code)) {
			Enumeration<String> names = request.getParameterNames();
			String element = names.nextElement();
			
			JSONObject object = JSON.parseObject(element);
			
			code = object.getString("code");
		}
		
		
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (StringUtils.isEmptyOrWhitespaceOnly(code)) {
			out.println(ResultUtils.paramError(ResultEnum.CODE_ERROR));
			out.flush();
			out.close();
			return;
		}

		Result<JSONObject> result = loginService.acquireToken(code);
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
