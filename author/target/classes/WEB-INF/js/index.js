/**
 * 
 */
function tj(){
	$.ajax({
		url: "http://127.0.0.1:8080/login",
		data: {
			userName:$("#userName").val(),
			passWord:$("#passWord").val()
		},
		type:"POST",
		dataType:"jsonp",
		jsonp: "callbackparam",
		success: function(data){
			alert(data);
		}
	});
}