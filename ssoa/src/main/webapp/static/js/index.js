/**
 * 
 */
function tj(){
	$.ajax({
		headers:{
			Accept:"application/x-www-form-urlencoded",
			ContextType:"application/x-www-form-urlencoded;charset=utf-8"
		},
		url: "basic/login",
		data: {
			userName:$("#userName").val(),
			passWord:$("#passWord").val(),
			fromUrl:$("#fromUrl").val()
		},
		type:"POST",
		dataType:"json",
		ContextType: "application/json;charset=UTF-8",
		success: function(result){
			if(result.code=="100031" || result.code == 100031){
				$('<div>')
				.appendTo('body')
				.addClass('alert alert-success')
				.html(result.codeMessage)
				.show()
				.delay(1500)
				.fadeOut();
//				alert("result.data.fromUrl:" + result.data.fromUrl);
				window.location.href=result.data.fromUrl+"?code="+result.data.code;
			}
		}
	});
}