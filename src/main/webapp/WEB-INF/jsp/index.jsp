<%@page import="com.ucredit.hermes.common.HermesConsts"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hermes</title>
</head>
<body>
	<h1>Hermes Works!</h1>
	Version: ${build.timestamp}
	<table>
		<tr>
			<td colspan='2'><a href="<%=request.getContextPath()%>/rest/statistics/201412">统计2014年12月以后（含12月）的查询次数</a></td>
		</tr>
		<tr>
			<td colspan='2'><a href="<%=request.getContextPath()%>/rest/statistics/201411">统计2014年11月的查询次数</a></td>
		</tr>
		<tr>
			<td colspan='2'><a href="<%=request.getContextPath()%>/report">统计2014年11月之前（不含11月份）的查询次数</a></td>
		</tr>
		<tr>
			<td colspan='2'><a href="<%=request.getContextPath()%>/companyInfo">测试企业查询</a></td>
		</tr>
		<tr>
			<td colspan='2'><a href="<%=request.getContextPath()%>/rest/education">测试学历查询</a></td>
		</tr>
		<tr>
			<td colspan='2'><a href="<%=request.getContextPath()%>/rest/statistics/201412education">2014年12月学历查询补漏统计</a></td>
		</tr>
	</table>
	<hr />
	<h1>百融工作</h1>
	<table>
		<thead>
			<tr>
				<td colspan="2"><h2>百融公司查询及统计</h2></td>
			</tr>
		</thead>
		<tr>
			<td colspan='2'><a href="<%=request.getContextPath()%>/rest/bairong">百融公司查询接口</a></td>
		</tr>
		<tr>
			<td colspan='2'><a href="<%=request.getContextPath()%>/rest/bairong/statistics">统计查询百融公司数据次数接口</a></td>
		</tr>
	</table>
</body>
</html>