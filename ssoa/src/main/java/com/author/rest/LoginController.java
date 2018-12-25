package com.author.rest;

import java.io.IOException;
import java.io.PrintWriter;

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

import lombok.extern.log4j.Log4j2;

@Log4j2
@WebServlet(value = "/basic/login" ,name = "LoginController")
public class LoginController extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5156026892717957263L;
	private LoginService loginService = LoginService.getInstance();

	/**
		 * Constructor of the object.
		 */
	public LoginController() {
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
		log.info("调用doPost");
		log.info("开始获取参数：" + System.nanoTime());
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");
		String fromUrl = request.getParameter("fromUrl");
		log.info("获取参数成功：" + System.nanoTime());
		log.info("userName:" + userName);
		log.info("passWord:" + passWord);
		log.info("ip:" + fromUrl);
		
		if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
			out.println(ResultUtils.success(ResultEnum.USER_ADN_PASS_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(passWord)) {
			out.println(ResultUtils.success(ResultEnum.USER_ADN_PASS_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		log.info("参数验证结束：" + System.nanoTime());
		Result<JSONObject> result = loginService.login(userName, passWord, fromUrl);
		if (null == result.getData()) {
			result.setData(new JSONObject());
		}
		result.getData().put("fromUrl", fromUrl);
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
