function makeTable(results) {
	$("#towerTable>tbody").empty();
	var detail = "";
	 for (var i = 0; i < results.length; i++) {
		var line = "<td>" + (i + 1) + "</td>";
		line += "<td>" + results[i].id + "</td>";
		line += "<td>" + results[i].ct + "</td>";
		if(results[i].cm==null){
			line += "<td>" + "" + "</td>";
		}else{
			line += "<td>" + results[i].cm + "</td>";
		}
		if(results[i].cd==null){
			line += "<td>" + "" + "</td>";
		}else{
			line += "<td>" + results[i].cd + "</td>";
		}
		if(results[i].cu==null){
			line += "<td>" + "" + "</td>";
		}else{
			line += "<td>" + results[i].cu + "</td>";
		}
		
		line += "<td>" + results[i].upkeepIncom + "</td>";
		line += "<td>" + results[i].upkeepExpend + "</td>";
		line += "<td>" + results[i].siteIncom + "</td>";
		line += "<td>" + results[i].siteExpend + "</td>";
		line += "<td>" + results[i].upkeepStatus + "</td>";
		line += "<td>" + results[i].siteStatus + "</td>";
		line += ('<td><a href="###" onclick="deleteRow('+"'"+results[i].id+"'"+')">删除</a></td>');
		
		detail += "<tr>" + line + "</tr>"; 
		
	};	
	$("#towerTable>tbody").append(detail);
}
var curPage = 1;
var pageSize = 10;

function refreshPage(flag) { // 是否重置分页器标记
	var id = $("#zdid").val().trim();
	var ttmc = $("#ttmc").val().trim();
	var upkeepStatus = $("#upkeep").val();
	var siteStatus = $("#site").val();
	$.ajax({
		type : 'post',
		url : '/station/list.action',
		data : JSON.stringify({
			"id" : id,
			"ttmc" : ttmc,
			"pageSize" : pageSize,
			"curPage" : curPage,
			"upkeepStatus" : upkeepStatus,
			"siteStatus" : siteStatus
		}),
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
	
			makeTable(data.rows);
			if (flag) {
				initPage(data.total);
			}
		},
		error : function(textStatus, errorThrown) {
		}
	});
}

function changeLength(index){
	pageSize = index;
	refreshPage(true);
}

function searchData(){
	refreshPage(true);
}

function deleteRow(row) { 
	$.messager.confirm('Confirm', 'Are you sure to delete?', function(r){
		if (r){
			 
			$.ajax({
                type: "get",
                url: "/station/delete.action?id=" + row,
                success: function(){
                	document.location = "towerlist";

                }
            }); 
		}
	});
}

function resetCondition(){
	$("#zdid").val(""); 
	$("#ttmc").val("");
	$("#upkeep").val("");
	$("#site").val("");
}

function initPage(totalCount) {
	$("#paginator").empty();
	curPage = 1;
	if (totalCount > 0) {
		var totalPages = (totalCount % pageSize === 0) ? Math
				.floor(totalCount / pageSize) : Math.floor(totalCount
				/ pageSize) + 1;
		$.jqPaginator('#paginator', {
			totalPages : totalPages,
			visiblePages : totalPages > 5 ? 5 : totalPages,
			currentPage : 1,
			onPageChange : function(num, type) { // 页数, 动作change
				curPage = num;
				refreshPage(false);
				$("#table_info").html("Showing "+((curPage-1)*pageSize+1)+" to "+curPage*pageSize+" of "+ totalCount +" entries");
			}
		});
	} else {
		$("#towerTable>tbody").append("<tr><td colspan='13'>暂无数据</td></tr>");
		$("#table_info").html("Showing 0 to 0 of 0 entries");
	}
	
}

function filedownload(){
	var zdid = $("#zdid").val();
	var ttmc = $("#ttmc").val();  
	var upkeepStatus = $("#upkeep").val();
	var siteStatus = $("#site").val();
    location.href = "/station/export.action?id=" + zdid + "&ttmc=" + ttmc+ "&upkeep=" + upkeepStatus+ "&site=" + siteStatus;
 }

function deleteAll() { 
	$.messager.confirm('Confirm', 'Are you sure to deleteAll?', function(r){
		if (r){
			 
			$.ajax({
                type: "get",
                url: "/station/deleteAll.action",
                success: function(){
                	document.location = "towerlist";

                }
            }); 
		}
	});
}

refreshPage(true);
