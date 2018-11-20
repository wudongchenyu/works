/**
 * 
 */
function tj(){
	$.ajax({
		url: "login",
		data: {
			userName:$("#userName").val(),
			passWord:$("#passWord").val(),
			fromUrl:$("#fromUrl").val()
		},
		type:"POST",
		dataType:"json",
		success: function(result){
			if(result.code=="110031" || result.code == 110031){
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