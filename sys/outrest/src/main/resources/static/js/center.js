$(
		function() {
			var timestamp=new Date().getTime();
			$.ajax({
					url:"/permission/allTreeList",
					contentType: "application/json",
					data:JSON.stringify({timestamp:timestamp}),
					dataType:"JSON",
					type:"post",
					success:function(data){
						if (data.data) {
							initTree(data.data);
						}
					}
			});
			
			
		}
);

function initTree(data){
	$('#mytree').treeview({
		data: data,//数据源参数
		selectable:true,
		expandIcon: "glyphicon glyphicon-plus",
        collapseIcon: "glyphicon glyphicon-minus",
        emptyIcon: "glyphicon",
        nodeIcon: "glyphicon glyphicon-plus",
        selectedIcon: "glyphicon glyphicon-plus",
        checkedIcon: "glyphicon glyphicon-check",
		uncheckedIcon: "glyphicon glyphicon-unchecked",
		enableLinks: false,
		levels:0,
		onNodeSelected: function(event, node) {
			if (node.code=="RS20180813164753464") {
				$("#resourceList").show();
				initDataTables();
			}
			console.log(event,node);
		},
		onNodeUnselected: function (event, node) {
			$("#resourceList").hide();
			console.log(event,node);
		}
	});
}
function itemOnclick(target){
	//找到当前节点id
	var nodeid = $(target).attr('data-nodeid');
	var tree = $('#mytree');
	//获取当前节点对象
	var node = tree.treeview('getNode', nodeid);
	if(node.state.expanded){ 
		//处于展开状态则折叠
		tree.treeview('collapseNode', node.nodeId);
	} else {
		//展开
		tree.treeview('expandNode', node.nodeId);
	}
}