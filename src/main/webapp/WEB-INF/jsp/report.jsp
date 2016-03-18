<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<a href="<%=request.getContextPath()%>">返回</a>
	<hr />
	<h1>2014年11月份以前的的统计报告方法</h1>
	<form action="/hermes/rest/statistics/report" name="form" method="post" enctype="multipart/form-data">
		<label> 请选择相应的csv文件：<input type="file" name="file" size=50 />
		</label> <br /> <br /> <label> 起始时间：<input type="text" name="date1" size=50 />格式：yyyyMMdd
		</label> <br /> <br /> <label> 结束时间：<input type="text" name="date2" size=50 />格式：yyyyMMdd
		</label> <br /> <br />
		<!-- 
	<label>
		文件类型：
		<select name='type'  style="width: 50px;">
			<option value='1'>专线查询</option>
			<option value='0'>互联网查询</option>
		</select>
	</label> -->
		<br /> <br /> <input type="submit" name="sumbmit" value="提交" />
	</form>
	<hr>
	2014年5月份以前用互联网查询
	<br /> 2014年5,6,7月份有专线，也有互联网查询
	<br /> 2014年8月份以后完全用专线查询
	<br />
</body>
</html>