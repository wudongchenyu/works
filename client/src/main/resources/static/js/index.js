/**
 * 
 */
function tj(){
	var username = $("#userName").val();
	var pass = $("#passWord").val();
	var data={"username":username,"pass":pass};
	$.ajax({
		url: "/sso/login",
		data: JSON.stringify({'username':username,'pass':pass}),
		type:"post",
		contentType:'application/json',
		dataType:"json",
		success: function(result){
			if(result.code=="100031" || result.code == 100031){
				$('<div>')
				.appendTo('body')
				.addClass('alert alert-success')
				.html(result.codeMessage)
				.show()
				.delay(1500)
				.fadeOut();
			}
		}
	});
}