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
	<h1>统计2014年12月及以后的报表</h1>
	<form action="<%=request.getContextPath()%>/rest/statistics/201412" method="post">
		起始日期：<input name="beginDateString" id="big1" type="date" value="${ beginDateString}" style="width: 200px" />格式：yyyy-MM-dd<br /> 结束日期：<input name="endDateString" id="big2" type="date" value="${ endDateString}" style="width: 200px" />格式：yyyy-MM-dd<br /> <input type="submit" value="查询" onclick="this.disabled=true;this.value='正在查询中'">
	</form>
	<br>
	<form action="<%=request.getContextPath()%>/rest/statistics/201412export" method="post" target="blank">
		起始日期：<input name="beginDateString" id='item1' type="date" value="${ beginDateString}" style="width: 200px" />格式：yyyy-MM-dd<br /> 结束日期：<input name="endDateString" id='item2' type="date" value="${ endDateString}" style="width: 200px" />格式：yyyy-MM-dd<br /> <input type="submit" value="导出明细" onclick="if (beforeDown()){this.disabled=true;this.value='导出耗时,请耐心等待,正在导出中.下载完刷新一下'; return true;}else {return false;}">
	</form>
	<%
	    List<Map<String, Object>> retdataMap = (List<Map<String, Object>>)request.getAttribute("data");
			if(retdataMap!=null){
			    if(retdataMap.size() ==0 ){
			        out.println( " <tr><td colspan='2'>没有查到结果</td></tr>");
			    }else {
			        int qiYeXinXiBaogao = 0;
				    out.println( "<table><tr><td>查询分类</td><td>查询次数</td></tr>");
				  	for (Map<String, Object> item : retdataMap) {
				  	    String key = (String)item.get("name");
						out.println( "<tr><td>"+key+"</td><td>"+item.get("queryTimes")+"</td></tr>");
						
				  	}
				  	out.println( "<tr><td>企业信用报告(企业基本信息+企业高管信息+企业对外投资信息)</td><td>"+qiYeXinXiBaogao+"</td></tr>");
			  		out.println( "</table>");
			    }
			}
	%>
</body>
<script type="text/javascript">
	function beforebigSearch() {
		var item1 = document.getElementById('big1').value;
		var item2 = document.getElementById('big12').value;
		if (item1 == "" || item2 == "") { return false; }
		return true;
	}
	function beforeDown() {
		var item1 = document.getElementById('item1').value;
		var item2 = document.getElementById('item2').value;
		if (item1 == "" || item2 == "") { return false; }
		return true;
	}
</script>
</html>