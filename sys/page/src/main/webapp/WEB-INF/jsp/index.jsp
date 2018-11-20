<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
<meta name="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${request.contextPath }/css/index.css">
<script type="text/javascript" src="${request.contextPath }/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="${request.contextPath }/js/bootstarp.js"></script>
<script type="text/javascript" src="${request.contextPath }/js/index.js"></script>
<script type="text/javascript">
	$(function (){
		$("#asdas").html("<option value = '00'>aaaa</option><option value = '01'>bbbb</option>");
		$("#asdas").change(function (){
			alert("'aaaaa'");
		});
	});
</script>
</head>
<body>
	<div class="box">
		<div class="taiji">
			<div class="tj_1 tj_big1"></div>
			<div class="tj_1 tj_big2"></div>
			<div class="tj_2 tj_s1">
				<div class="tj_ss tj_w"></div>
			</div>
			<div class="tj_2 tj_s2">
				<div class="tj_ss tj_b"></div>
			</div>
		</div>
	</div>
	<div>
		<select id="asdas">
			<option value = "00">aaaa</option>
		</select>
	</div>
</body>
</html>