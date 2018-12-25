package com.author.rest.authority;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.author.po.Authority;
import com.author.service.AuthorityService;
import com.author.util.Result;
import com.author.util.ResultEnum;
import com.author.util.ResultUtils;
import com.mysql.cj.util.StringUtils;

@WebServlet(value = "/basic/authority/edit" ,name = "AuthorityEditController")
public class AuthorityEditController extends HttpServlet {
	
	private AuthorityService authorityService = AuthorityService.getInstance();

	/**
	 * 
	 */
	private static final long serialVersionUID = 5532126198274241253L;

	/**
		 * Constructor of the object.
		 */
	public AuthorityEditController() {
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
		System.out.println("调用doPut");
		
		String id = request.getParameter("id");
		String authorityName = request.getParameter("authorityName");
		String authorityUrl = request.getParameter("authorityUrl");
		String authorityType = request.getParameter("authorityType");
		String subordinateSystem = request.getParameter("subordinateSystem");
		String subordinateApp = request.getParameter("subordinateApp");
		String subordinateModule = request.getParameter("subordinateModule");
		String channel = request.getParameter("channel");
		
		response.setContentType("application/json;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
	    
	    if (StringUtils.isEmptyOrWhitespaceOnly(id)) {
			out.println(ResultUtils.paramError(ResultEnum.AUTHORITYID_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
	    
		if (StringUtils.isEmptyOrWhitespaceOnly(authorityName)) {
			out.println(ResultUtils.paramError(ResultEnum.AUTHORITYNAME_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(authorityType)) {
			out.println(ResultUtils.paramError(ResultEnum.AUTHORITY_TYPE_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(authorityUrl)) {
			out.println(ResultUtils.paramError(ResultEnum.AUTHORITYURL_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(subordinateSystem)) {
			out.println(ResultUtils.paramError(ResultEnum.AUTHORITY_SUBORDINATE_SYSTEM_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(subordinateApp)) {
			out.println(ResultUtils.paramError(ResultEnum.AUTHORITY_SUBORDINATE_APP_NULL_ERROR));
		    out.flush();
		    out.close();
		    return;
		}
		
		Result<Authority> result = authorityService.editAuthority(
				id, authorityName, 
				authorityUrl, 
				authorityType, 
				subordinateSystem, 
				subordinateApp, 
				subordinateModule, 
				channel);
		
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
