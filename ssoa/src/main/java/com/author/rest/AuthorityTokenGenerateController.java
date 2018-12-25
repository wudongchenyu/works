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

@WebServlet(value = "/basic/authority/token/generate" ,name = "AuthorityTokenGenerateController")
public class AuthorityTokenGenerateController extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2037273903413680425L;
	private LoginService loginService = LoginService.getInstance();

	/**
		 * Constructor of the object.
		 */
	public AuthorityTokenGenerateController() {
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
		System.out.println("调用/basic/authority/token/generate");
		
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
		
		String token = request.getParameter("token");
		String subordinateSystem = request.getParameter("subordinateSystem");
		String subordinateApp = request.getParameter("subordinateApp");
		String subordinateModule = request.getParameter("subordinateModule");
		String channel = request.getParameter("channel");
		if (null == token && null == subordinateSystem && null == subordinateApp && null == subordinateModule && null == channel) {
			Enumeration<String> names = request.getParameterNames();
			if (!names.hasMoreElements()) {
				out.println(ResultUtils.success(ResultEnum.TOKEN_NULL_ERROR));
			    out.flush();
			    out.close();
			    return;
			}
			String element = names.nextElement();
			JSONObject object = JSON.parseObject(element);
			
			if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
				token = object.getString("token");
			}
			
			if (StringUtils.isEmptyOrWhitespaceOnly(subordinateSystem)) {
				subordinateSystem = object.getString("subordinateSystem");
			}
			
			if (StringUtils.isEmptyOrWhitespaceOnly(subordinateApp)) {
				subordinateApp = object.getString("subordinateApp");
			}
			
			if (StringUtils.isEmptyOrWhitespaceOnly(subordinateModule)) {
				subordinateModule = object.getString("subordinateModule");
			}
			
			if (StringUtils.isEmptyOrWhitespaceOnly(channel)) {
				channel = object.getString("channel");
			}
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
			out.println(ResultUtils.success(ResultEnum.TOKEN_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		Result<JSONObject> result = loginService.authorityToken(token, subordinateSystem, subordinateApp, subordinateModule, channel);
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
