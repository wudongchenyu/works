$().ready(function () {
	$("#loginForm").validate({
		submitHandler:function(form){
			if($('#userCode').val()==null 
					|| $('#userCode').val()=='' 
					|| $('#userCode').val().length<2 
					|| $('#userCode').val().length>30){
				$('<div>')
					.appendTo('body')
					.addClass('alert alert-success')
					.html('请输入正确的账号')
					.show()
					.delay(1500)
					.fadeOut();
				return;
			}
			if($('#pass').val()==null 
					|| $('#pass').val()=='' 
					|| $('#pass').val().length<3 
					|| $('#pass').val().length>12){
				$('<div>')
					.appendTo('body')
					.addClass('alert alert-success')
					.html('请输入正确的密码')
					.show()
					.delay(1500)
					.fadeOut();
				return;
			}
			if($('#code').val()!='success'){
				$('<div>').appendTo('body').addClass('alert alert-success').html('请移动滑块到正确位置').show().delay(1500).fadeOut();
				return;
			}
			form.submit();
        } 
	});
  jigsaw.init(document.getElementById('captcha'), function () {
	    	$('#code').val('success');
	  });
});
function checkParam() {
	$("#loginForm").validate({
		rules:{
			userCode:{
				required:true,
				minlength:6,
				maxlength:30
			},
			pass:{
				required:true,
				minlength:6,
				maxlength:12
			},
			code:{
				required: true,
				minlength:7
			}
		},
		messages:{
			userCode:{
				required:"请输入账号",
				minlength:"账号长度不能小于6位",
				maxlength:"账号长度不能超过30位"
			},
			pass:{
				required:"请输入密码",
				minlength:"密码长度不能小于6位",
				maxlength:"密码长度不能超过12位"
			},
			code:{
				required: "请滑动验证码到正确位置",
				minlength:"请滑动验证码到正确位置",
			}
		},
		submitHandler:function(form){
			if($('#code').val()=='success'){
				form.submit();
			}
        } 
	});
}