function initRoleDataTables() {
	$('#roles').DataTable({
		ajax : {
			url : "/role/list",
			contentType : "application/json",
			data : JSON.stringify({
				timestamp : new Date().getTime()
			}),
			dataType : "JSON",
			type : "post"
		},
		aoColumnDefs : [
			{
				aDataSort : [ 0, 1 ],
				aTargets : [ 0 ]
			},
			{
				aDataSort : [ 1, 0 ],
				aTargets : [ 1 ]
			},
			{
				aDataSort : [ 2, 3, 4 ],
				aTargets : [ 2 ]
			}
		],
		columns : [
			{
				"sClass" : "text-center",
				"data" : "id", //行单选框
				"render" : function(data, type, full, meta) {
					return '<input id="checkchild" type="checkbox"  class="checkchild"  value="' + data + '" />';
				},
				"bSortable" : false
			},
			{
				title : "序号",
				data : "index", //序号
				render : function(data, type, row, meta) {
					var startIndex = meta.settings._iDisplayStart;
					return startIndex + meta.row + 1;
				}
			}, {
				title : "名称",
				data : "name"
			}, {
				title : "编号",
				data : "roleCode"
			}, {
				title : "备注",
				data : "remake"
			}, {
				title : "创建时间",
				data : "createTime"
			} ],
		language : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "显示 _MENU_ 项结果",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
			"sInfoEmpty" : "显示第 0 至 0 项结果，共 0 项",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});
}