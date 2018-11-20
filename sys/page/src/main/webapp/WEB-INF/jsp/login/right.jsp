<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta name="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div>
		<div id = "resourceList" class = "disdiv">
			<jsp:include page="../login/permission/permissionList.jsp"/>
		</div>
		<div id = "roleList"  class = "disdiv">
			<jsp:include page="../login/role/roleList.jsp"/>
		</div>
		<div id = "userList"  class = "disdiv">
			<jsp:include page="../login/user/userList.jsp"/>
		</div>
	</div>
</body>
</html>