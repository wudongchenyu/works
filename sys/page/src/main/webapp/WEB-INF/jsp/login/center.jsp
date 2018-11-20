<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="X-UA-Compatible" content="ie=edge">
    <title>系统中心</title>
    <link rel="stylesheet" href="../static/css/base.css" type="text/css" > <!--初始化文件-->
    <link rel="stylesheet" href="../static/css/menu.css" type="text/css" > <!--主样式-->
    <link rel="stylesheet" href="../static/css/bootstrap-treeview.min.css" type="text/css" > <!--主样式-->
    <link rel="stylesheet" href="../static/css/jquery.dataTables.css" type="text/css" > <!--主样式-->
    <script type="text/javascript" src="../static/js/jquery-3.3.1.js"></script>
	<script type="text/javascript" src="../static/js/bootstrap.js"></script>
	<script type="text/javascript" src="../static/js/bootstrap-treeview.min.js"></script>
	<script type="text/javascript" src="../static/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../static/js/adapter.js"></script>
    <script type="text/javascript" src="../static/js/center.js"></script>
    <script type="text/javascript" src="../static/js/permission/permission.js"></script>
    <script type="text/javascript" src="../static/js/user/user.js"></script>
    <script type="text/javascript" src="../static/js/role/role.js"></script>
    
    
</head>
<body>
<div class = "alldiv">
	<div class = "top">
		<jsp:include page="../login/base/top.jsp"></jsp:include>
	</div>
	<div class = "left">
		<jsp:include page="../login/left.jsp"></jsp:include>
	</div>
	<div class = "right">
		<jsp:include page="../login/right.jsp"></jsp:include>
	</div>
</div>
</body>
</html>