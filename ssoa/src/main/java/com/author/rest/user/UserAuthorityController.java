package com.author.rest.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.author.service.UserService;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.mysql.cj.util.StringUtils;

@WebServlet("/user/authority")
public class UserAuthorityController extends HttpServlet {
	
	private UserService userService = UserService.getInstance();

	/**
	 * 
	 */
	private static final long serialVersionUID = -28847013515955520L;

	/**
		 * Constructor of the object.
		 */
	public UserAuthorityController() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy();
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
		String authorities = request.getParameter("authorities");
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
	    
		if (StringUtils.isEmptyOrWhitespaceOnly(userId)) {
			out.println(ResultUtils.error(ResultEnum.USERID_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(authorities)) {
			out.println(ResultUtils.error(ResultEnum.AUTHORITYLIST_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		List<String> list = Arrays.asList(authorities.split(","));
		
		Result<String> result = userService.privileges(userId, list);
		
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
