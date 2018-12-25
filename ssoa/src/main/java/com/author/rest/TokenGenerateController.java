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

@WebServlet(value = "/basic/token/generate", name = "TokenGenerateController")
public class TokenGenerateController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5156026892717957263L;
	private LoginService loginService = LoginService.getInstance();

	/**
	 * Constructor of the object.
	 */
	public TokenGenerateController() {
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
		Enumeration<String> names = request.getParameterNames();
		String element = names.nextElement();
		
		JSONObject object = JSON.parseObject(element);
		
		String userName = object.getString("userName");
		String passWord = object.getString("passWord");
		String ip = object.getString("ip");
		String remortAddress = object.getString("remortAddress");
		System.out.println("userName:" + userName);
		System.out.println("passWord:" + passWord);
		System.out.println("ip:" + ip);
		System.out.println("remortAddress:" + remortAddress);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
			out.println(ResultUtils.paramError(ResultEnum.USER_ADN_PASS_NULL_ERROR));
			out.flush();
			out.close();
			return;
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(passWord)) {
			out.println(ResultUtils.paramError(ResultEnum.USER_ADN_PASS_NULL_ERROR));
			out.flush();
			out.close();
			return;
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(ip)) {
			ip = loginService.getIpAddress(request);
		}

		Result<JSONObject> result = loginService.login(userName, passWord, ip, remortAddress);
		result.getData().put("ip", ip);
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
