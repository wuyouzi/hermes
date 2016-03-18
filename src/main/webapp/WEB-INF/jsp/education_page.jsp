<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学历查询测试页面</title>
<script src="<%=request.getContextPath()%>/js/json3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
</head>
<body>
	<a href="<%=request.getContextPath()%>">返回</a>
	<hr />
	<table>
		<tr>
			<td>操作人员名称：</td>
			<td><input type="text" value='123' id="operationName"></td>
		</tr>
		<tr>
			<td>姓名：</td>
			<td><input type="text" id="name" />例如：张三</td>
		</tr>
		<tr>
			<td>证件号码：</td>
			<td><input type="text" id="documentNo" /></td>
		</tr>
		<tr>
			<td>学历证编号：</td>
			<td><input type="text" id="levelNo" /></td>
		</tr>
		<tr>
			<td>毕业年份：</td>
			<td><input type="text" id="graduateYear" />例如：2007</td>
		</tr>
		<tr>
			<td>院校：</td>
			<td><input type="text" id="college" />例如：xx大学</td>
		</tr>
		<tr>
			<td>强制刷新：</td>
			<td><select id='constraint'>
					<option value="false">否</option>
					<option value="true">是</option>
			</select></td>
		</tr>
		<tr>
			<td>有效天数：</td>
			<td><input type="text" id="activeDays"></td>
		</tr>
		<tr>
			<td>有效日期：</td>
			<td><input type="text" id="activeDate" />格式：yyyy-MM-dd</td>
		</tr>
		<tr>
			<td colspan="2"><input type="reset" value="查询" id="searchButn" onclick="searchFn()" /></td>
		</tr>
	</table>
	<div id="showmsg" style='width: 100%'></div>
</body>
<script type="text/javascript">
function searchFn(){
	var operationName = $("#operationName").val();
	var name = $("#name").val();
	var documentNo = $("#documentNo").val();
	var levelNo = $("#levelNo").val();
	var graduateYear = $("#graduateYear").val();
	var college = $("#college").val();
	
	var constraint = $("#constraint").val();
	
	
	var activeDays = $("#activeDays").val();
	var activeDate = $("#activeDate").val();
	var url = "<%=request.getContextPath()%>/rest/education/" + operationName + "?name=" + name + "&documentNo=" + documentNo + "&levelNo=" + levelNo + "&graduateYear=" + graduateYear + "&college=" + college + "&constraint=" + constraint + "&activeDays=" + activeDays + "&activeDate=" + activeDate
		document.getElementById("showmsg").innerHTML = "";
		$.ajax({
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			url : encodeURI(url),
			type : "GET",
			success : function(data) {
				document.getElementById("showmsg").innerHTML = "";
				var ret = JSON.stringify(data);
				//alert(ret);
				document.getElementById("showmsg").innerHTML = ret;
			},
			//data:{companyName:encodeURIComponent(companyName),orgCode:orgCode,constraint:constraint,registerNo:registerNo,activeDays:activeDays,activeDate:activeDate},
			cache : false,
			timeout : 5000,
			error : function() {
				alert("error");
			} });
	}
</script>
</html>