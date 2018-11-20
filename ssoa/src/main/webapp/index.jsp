<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>单点登录系统</title>
	<meta name="pragma" content="no-cache">
	<meta name="cache-control" content="no-cache">
	<meta name="expires" content="0">    
	<meta name="keywords" content="keyword1,keyword2,keyword3">
	<meta name="description" content="This is my page">
	<link href="static/css/alert.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="static/js/jquery-3.3.1.js"></script>
	<script type="text/javascript" src="static/js/index.js"></script>
  </head>
  
  <body>
    <div>
		<input type="text" id="userName" name="userName"><br>
		<input type="password" id="passWord" name="passWord"><br>
		<input type="hidden" id="fromUrl" name = "fromUrl" value="${fromUrl}"><br>
		<button onclick="tj();">提交</button>
	</div>
  </body>
</html>
