function initDataTables() {
	$.ajax({
		url:"/permission/allList",
		contentType: "application/json",
		data:JSON.stringify({timestamp:timestamp}),
		dataType:"JSON",
		type:"post",
		success:function(data){
			$('#permissions').DataTable({
			"processing": true,
			"retrieve":true,
			"destroy":true,
			"ajax": data.data,
			"columns": [
				{field:'id'},
				{field:"name"},
				{field:"resourceString"},
				{field:"resourceName"}
			]
		});
		}
	});
}