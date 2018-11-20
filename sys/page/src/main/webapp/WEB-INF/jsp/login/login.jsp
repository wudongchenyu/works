<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta name="Content-Type" content="text/html; charset=UTF-8">
<link href="../static/css/default.css" rel="stylesheet" type="text/css" />
<link href="../static/css/styles.css" rel="stylesheet" type="text/css" />
<link href="../static/css/base.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../static/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="../static/js/bootstrap.js"></script>
<script type="text/javascript" src="../static/js/jquery.validate.js"></script>
<script type="text/javascript" src="../static/js/jigsaw.js"></script>
<script type="text/javascript" src="../static/js/login.js"></script>
<title>登录页面</title>
</head>
<body>
	<div class="site-nav">
		<div class="container">
			<ul class="site-nav-l">
				<li class="menu"><span>全国行政区划</span>
				<li>
			</ul>
		</div>
	</div>
	<header></header>
	<div class="login-wrapper">
		<div class="container">
			<div class="login-box">
				<ul class="login-tabs">
					<li class="active"><span>登录</span></li>
				</ul>
				<div style="clear:both;"></div>
				<div class="formdiv">
					<form id="loginForm" name='loginForm' action="login" method='POST'>
						<div class='login_fields'>
							<div class="jiange"></div>
							<div class='login_fields__user'>
								<input id="userCode" name="userCode" placeholder='账号/邮箱'
									type='text' autocomplete="off" value="" />
							</div>
							<div class="jiange"></div>
							<div class='login_fields__password'>
								<input id="pass" name="pass" placeholder='密码' type="password"
									autocomplete="new-password">
							</div>
							<div class="jiange"></div>
							<div id="captcha" style="position: relative"></div>
  							<div id="msg"><input type="hidden" id="code"></div>
							<div class="jiange"></div>
							<div class='login_fields__submit'>
								<input type="submit" value='登录'> <input type="hidden"
									name="${_csrf.parameterName}" value="${_csrf.token}" />
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>