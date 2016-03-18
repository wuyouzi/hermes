<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.math.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计报告</title>
</head>
<body>
	<a href="<%=request.getContextPath()%>">返回</a>
	<hr />
	<h1>统计查询次数</h1>
	<form action="<%=request.getContextPath()%>/rest/bairong/statistics" method="post">
		起始日期：<input name="beginDateString" id="big1" type="date" value="${ beginDateString}" style="width: 200px" />格式：yyyy-MM-dd<br /> 结束日期：<input name="endDateString" id="big2" type="date" value="${ endDateString}" style="width: 200px" />格式：yyyy-MM-dd<br /> <input type="submit" value="查询" onclick="this.disabled=true;this.value='正在查询中';document.getElementById('search_result').innerHTML =''; return true;">
	</form>
	<div id="search_result">
		<%
		    Long times = (Long) request.getAttribute("times");
					if (times != null) {
						out.println("查询结果为：" + times);
					}
		%>
	</div>
	<hr />
	<!--  -->
	<h1>明细对帐</h1>
	<form action="<%=request.getContextPath()%>/rest/bairong/statisticsexport" method="post" target="blank" enctype="multipart/form-data">
		起始日期：<input name="beginDateString" id='item1' type="date" value="${ beginDateString}" style="width: 200px" />必填，格式：yyyy-MM-dd<br /> 结束日期：<input name="endDateString" id='item2' type="date" value="${ endDateString}" style="width: 200px" />必填，格式：yyyy-MM-dd<br /> 百融明细：<input type="file" name="file" id="file" />必填<br /> <input type="submit" value="导出明细" onclick="if (beforeDown()){this.disabled=true;this.value='导出耗时,请耐心等待,正在导出中.下载完刷新一下'; return true;}else {return false;}">
	</form>
	<br />
	<br />
	<br />
</body>
<script type="text/javascript">
	function beforeDown() {
		var item1 = document.getElementById('item1').value;
		var item2 = document.getElementById('item2').value;
		var file = document.getElementById('file').value;
		if (item1 == "" || item2 == "") {
			alert("请填入必填参数");
			return false;
		}
		if (file == "") {
			alert("请填入必填参数");
			return false;
		}
		return true;
	}
</script>
</html>