<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>滑动拼图验证码</title>
<link rel="stylesheet" href="css/sliderImgPuzzle.css">
<style>
    .container {
      width: 310px;
      margin: 300px auto;
    }
    input {
      display: block;
      width: 290px;
      line-height: 40px;
      margin: 10px 0;
      padding: 0 10px;
      outline: none;
      border:1px solid #c8cccf;
      border-radius: 4px;
      color:#6a6f77;
    }
    #msg {
      width: 100%;
      line-height: 40px;
      font-size: 14px;
      text-align: center;
    }
    a:link,a:visited,a:hover,a:active {
      margin-left: 100px;
      color: #0366D6;
    }

  </style>
</head>

<body>
	<div class="container">
		<div id="silderpuzzle"
			style="position: relative;padding-bottom: 15px;">
			<div class="canvasContainer" style="display: none;">
				<canvas width="325" height="155"></canvas>
				<div class="refreshIcon"></div>
				<canvas width="62" height="155" class="block"
					style="left: 76.2456px;"></canvas>
			</div>
			<div
				class="sliderImgPuzzleContainer sliderImgPuzzleContainer_success">
				<div class="sliderImgPuzzleMask" style="width: 82px;">
					<div class="sliderImgPuzzle" style="left: 82px;">
						<span class="sliderImgPuzzleIcon"></span>
					</div>
				</div>
				<span class="sliderImgPuzzleText">向右滑动滑块填充拼图</span>
			</div>
		</div>
		<div id="msg">验证成功！</div>
		<button id="reflash">刷新控件</button>
	</div>
	<script src="http://www.jq22.com/jquery/jquery-2.1.1.js"></script>
	<script src="http://libs.baidu.com/jquery/2.1.1/jquery.min.js"></script>
	<script src="js/sliderImgPuzzle.js"></script>
	<script>
//--------------------滑块拼图验证控件初始化-----
  
</script>


</body>
</html>
