<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.math.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计报告</title>
</head>
<body>
	<form action="<%=request.getContextPath()%>/rest/statistics/hermesCountReport">
		起始日期：<input name="beginDate" type="date" value="${beginDate }" style="width: 200px" />格式：yyyy-MM-dd<br /> 结束日期：<input name="endDate" type="date" value="${endDate }" style="width: 200px" />格式：yyyy-MM-dd<br /> <input type="submit" value="查询" onclick="this.disabled=true;this.value='正在查询中'">
	</form>
	<br>
	<%
	    List<Map<String, Object>> hermesCountdataList = (List<Map<String, Object>>) request
	        .getAttribute("hermesCountdata");
	    List<Map<String, Object>> pengyuandataList = (List<Map<String, Object>>) request
	        .getAttribute("pengyuandata");

	    if (hermesCountdataList != null) {
	        int qiYeXinXiBaogao = 0;
	        out.println("<table><tr><td colspan='2'>hermesCount表数据分析</td></tr>");
	        out.println("<tr><td>查询分类</td><td>查询次数</td></tr>");
	        for (Map<String, Object> item : hermesCountdataList) {
	            String key = (String) item.get("key");

	            out.println("<tr><td>" + key + "</td><td>"
	                + item.get("searchcount") + "</td></tr>");
	            if ("企业基本信息".equals(key) || "企业高管信息".equals(key)
	                || "企业对外投资信息".equals(key)) {
	                qiYeXinXiBaogao += ((BigInteger) item
	                    .get("searchcount")).intValue();
	            }
	        }

	        out.println("<tr><td>企业信用报告</td><td>" + qiYeXinXiBaogao
	            + "</td></tr>");
	        out.println("</table>");

	    }
	    if (pengyuandataList != null) {

	        out.println("<table><tr><td colspan='2'>pengYuan_counts表数据分析（做参考）</td></tr>");
	        out.println("<tr><td>查询分类</td><td>查询次数</td></tr>");
	        for (Map<String, Object> item : pengyuandataList) {
	            String key = (String) item.get("name");
	            out.println("<tr><td>" + key + "</td><td>"
	                + item.get("searchcount") + "</td></tr>");
	        }

	        out.println("</table>");

	    }
	%>
</body>
</html>