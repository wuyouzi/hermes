<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.bfd.facade.Educationallevel,com.bfd.facade.Biz_industry,com.bfd.facade.Marriage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>百融公司查询接口</title>
<script src="<%=request.getContextPath()%>/js/json3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
</head>
<style>
.inputwidth {
	width: 280px;
}
</style>
<body>
	<a href="<%=request.getContextPath()%>">返回</a>&nbsp;&nbsp;&nbsp;&nbsp;
	<hr />
	<div>
		<div style="width: 50%; float: left">
			<table>
				<thead>
					<tr>
						<td colspan="3" align="center"><h2>百融公司查询接口</h2></td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>身份证号</td>
						<td><input name='id' id='idcard' class="inputwidth"></td>
						<td>&nbsp;110101199002021234</td>
					</tr>
					<tr>
						<td>手机号</td>
						<td><input name='cell' id='cell' class="inputwidth"></td>
						<td>&nbsp;18800011111</td>
					</tr>
					<!-- 
			 -->
					<tr>
						<td>电子邮件</td>
						<td><input name='mail' id='mail' class="inputwidth"></td>
						<td>&nbsp;baifendian@126.com</td>
					</tr>
					<tr>
						<td>姓名</td>
						<td><input name='name' id='name' class="inputwidth"></td>
						<td>&nbsp;张三</td>
					</tr>
					<tr>
						<td>家庭地址</td>
						<td><input name='home_addr' id='home_addr' class="inputwidth"></td>
						<td>&nbsp;河北省保定市**区**路**号</td>
					</tr>
					<tr>
						<td>公司地址</td>
						<td><input name='biz_addr' id='biz_addr' class="inputwidth"></td>
						<td>&nbsp;北京市石景山区政达路10号</td>
					</tr>
					<!-- 
			<tr>
				<td>户籍地址</td>
				<td><input name='per_addr' id='per_addr' class="inputwidth"></td>
				<td>&nbsp;上海市静安区**路**号</td>
			</tr> -->
					<tr>
						<td>申请地址<br />(移动应用为GPS地址)
						</td>
						<td><input name='apply_addr' id='apply_addr' class="inputwidth"></td>
						<td>&nbsp;上海市黄浦区南京东路1号8楼401</td>
					</tr>
					<tr>
						<td>公司座机号</td>
						<td><input name='tel_biz' id='tel_biz' class="inputwidth"></td>
						<td>&nbsp;021-55558888-12345</td>
					</tr>
					<tr>
						<td>家庭座机号</td>
						<td><input name='tel_home' id='tel_home' class="inputwidth"></td>
						<td>&nbsp;010-22229999</td>
					</tr>
					<tr>
						<td>公司名称</td>
						<td><input name='biz_workfor' id='biz_workfor' class="inputwidth"></td>
						<td>&nbsp;北京市水利局</td>
					</tr>
					<tr>
						<td>联系人姓名</td>
						<td><input name='linkman_name' id='linkman_name' class="inputwidth"></td>
						<td>&nbsp;李四</td>
					</tr>
					<tr>
						<td>联系人手机号</td>
						<td><input name='linkman_cell' id='linkman_cell' class="inputwidth"></td>
						<td>&nbsp;15901110000</td>
					</tr>
					<tr>
						<td>单位所属行业</td>
						<td><select name='biz_industry' id='biz_industry' class="inputwidth">
								<option value=""></option>
								<!-- 
					<option value="金融/保险">金融/保险</option>
					<option value="政府机关">政府机关</option>
					<option value="旅游/饭店/宾馆/娱乐">旅游/饭店/宾馆/娱乐</option>
					<option value="能源及通信服务">能源及通信服务</option>
					<option value="公共事业">公共事业</option>
					<option value="邮政/交通运输/物流业">邮政/交通运输/物流业</option>
					<option value="批发/零售/百货业">批发/零售/百货业</option>
					<option value="轻工业">轻工业</option>
					<option value="房地产/基础建设/物管">房地产/基础建设/物管</option>
					<option value="国内贸易公司">国内贸易公司</option>
					<option value="制造业">制造业</option>
 					<option value="律师/会计师/咨询/培训">律师/会计师/咨询/培训</option>
 					<option value="进出口贸易">进出口贸易</option>
 					<option value="IT产业">IT产业</option>
 					<option value="媒体/出版/广告/文艺">媒体/出版/广告/文艺</option>
 					<option value="医疗">医疗</option>
 					<option value="其他">其他</option>  -->
								<%
								    Biz_industry[] biz_industrys = Biz_industry.values();
										            for (Biz_industry item : biz_industrys) {
										                  out.println("<option value=\""+item.name()+"\">"+item.name()+"</option>");
										            }
								%>
						</select></td>
						<td>&nbsp;</td>
					</tr>
					<!--
			<tr>
				<td>其他地址</td>
				<td><input name='oth_addr' id='oth_addr' class="inputwidth"></td>
				<td>&nbsp;上海市黄浦区南京东路1号8楼401</td>
			</tr>
			<tr>
				<td>部署百融代码的bfdid </td>
				<td><input name='gid' id='gid' class="inputwidth"></td>
				<td>&nbsp;如果有，bffd842b2b4843320000404b001ec95d5407b53b</td>
			</tr>
			<tr>
				<td>IMEI号(移动应用)</td>
				<td><input name='imei' id='imei' class="inputwidth"></td>
				<td>&nbsp;493002407599521</td>
			</tr>
			<tr>
				<td>IMSI号(移动应用)</td>
				<td><input name='imsi' id='imsi' class="inputwidth"></td>
				<td>&nbsp;493002407599521</td>
			</tr>
			<tr>
				<td>手机品牌(移动应用)</td>
				<td><input name='mobile_type' id='mobile_type' class="inputwidth"></td>
				<td>&nbsp;Nokia</td>
			</tr>
			<tr>
				<td>性别</td>
				<td><select name='sex' id='sex' class="inputwidth">
					<option value="男">男</option>
					<option value="女">女</option>
				</select>
				</td>
				<td>&nbsp;</td>
			</tr>
			 <tr>
				<td>年龄</td>
				<td><input name='age' id='age' class="inputwidth"></td>
				<td>&nbsp;25</td>
			</tr>-->
					<tr>
						<td>学历</td>
						<td><select name='educationallevel' id='educationallevel' class="inputwidth">
								<option value=""></option>
								<%
								    Educationallevel[] enums = Educationallevel.values();
										            for (Educationallevel item : enums) {
										                  out.println("<option value=\""+item.name()+"\">"+item.name()+"</option>");
										            }
								%>
						</select></td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>婚姻状况</td>
						<td><select name='marriage' id='marriage' class="inputwidth">
								<option value=""></option>
								<%
								    Marriage[] marriages = Marriage.values();
										            for (Marriage item : marriages) {
										                  out.println("<option value=\""+item.name()+"\">"+item.name()+"</option>");
										            }
								%>
						</select></td>
						<td>&nbsp;</td>
					</tr>
					<!-- 
			
			<tr>
				<td>年收入</td>
				 <td><input name='income' id='income' class="inputwidth"></td>
				<td>&nbsp;100000.0</td>
			</tr>
			 <tr>
				<td>职位</td>
				<td><select name='biz_positon' id='biz_positon' class="inputwidth">
					<option value=""></option>
					<option value="员工">员工</option>
					<option value="基层主管">基层主管</option>
					<option value="中层主管">中层主管</option>
					<option value="高层主管">高层主管</option> 
				</select>
				</td>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td>公司性质</td>
				<td>
				<select name='biz_type' id='biz_type' class="inputwidth">
					<option value=""></option>
					<option value="外资企业">外资企业</option>
					<option value="合资企业">合资企业</option>
					<option value="国营企业">国营企业</option>
					<option value="民营企业">民营企业</option>
					<option value="上市公司">上市公司</option>
					<option value="非盈利组织">非盈利组织</option>
					<option value="政府机关">政府机关</option>
					<option value="事业单位">事业单位</option>
					<option value="个体工商">个体工商</option>
					<option value="其他">其他</option>
				</select>
				</td>
				<td>&nbsp;</td>
			</tr>
			-->
					<tr>
						<td>银行卡号</td>
						<td><input name='bank_id' id='bank_id' class="inputwidth"></td>
						<td>&nbsp;信用卡可不提供,1234567890123456</td>
					</tr>
					<!-- 
			<tr>
				<td>住房性质</td>
				<td>
				<select name='house_type' id='house_type' class="inputwidth">
					<option value=""></option>
					<option value="有房有贷款">有房有贷款</option>
					<option value="有房无贷款">有房无贷款</option>
					<option value="租借房">租借房</option>
					<option value="其他">其他</option> 
				</select>
				</td>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td>申请渠道</td>
				<td>
				<select name='apply_source' id='apply_source' class="inputwidth">
					<option value=""></option>
					<option value="网络申请">网络申请</option>
					<option value="柜台申请">柜台申请</option>
					<option value="销售人员办理">销售人员办理</option>
					<option value="其他">其他</option> 
				</select>
				</td>
				<td>&nbsp;</td>
			</tr> -->
					<!-- 
			 <tr>
				<td>申请产品</td>
				 <td><input name='apply_product' id='apply_product' class="inputwidth"></td>
				<td>&nbsp;信用卡</td>
			</tr>
			<tr>
				<td>申请额度</td>
				 <td><input name='apply_money' id='apply_money' class="inputwidth"></td>
				<td>&nbsp;100000.0</td>
			</tr>
			<tr>
				<td>申请时间</td>
				 <td><input name='apply_time' id='apply_time' class="inputwidth"></td>
				<td>&nbsp;2014-09-10</td>
			</tr>
			<tr>
				<td>贷款原因</td>
				 <td><input name='loan_reason' id='loan_reason' class="inputwidth"></td>
				<td>&nbsp;购车</td>
			</tr>
			
			<tr>
				<td>还款期数</td>
				 <td><input name='refund_periods' id='refund_periods' class="inputwidth"></td>
				<td>&nbsp;信用卡可不提供,36</td>
			</tr>
			
			<tr>
				<td>联系人关系</td>
				 <td><input name='linkman_rela' id='linkman_rela' class="inputwidth"></td>
				<td>&nbsp;配偶</td>
			</tr>
			<tr>
				<td>客户登陆APP的次数</td>
				 <td><input name='app_visit_num' id='app_visit_num' class="inputwidth"></td>
				<td>&nbsp;11</td>
			</tr>
			<tr>
				<td>学历、学籍证明附件数量</td>
				 <td><input name='edu_att_num' id='edu_att_num' class="inputwidth"></td>
				<td>&nbsp;34</td>
			</tr>
			<tr>
				<td>银行卡流水附件数量</td>
				 <td><input name='bank_running_att_num' id='bank_running_att_num' class="inputwidth"></td>
				<td>&nbsp;13</td>
			</tr> -->
				</tbody>
				<tfoot>
					<tr>
						<td colspan="3" align="center"><input type="button" style="width: 250px; height: 100px" value='查询' onclick='doSearchFn(this)'></td>
					</tr>
				</tfoot>
			</table>
		</div>
		<div style="width: 50%; float: right;" id="showmsg"></div>
	</div>
</body>
<script type="text/javascript">
function doSearchFn(btn){
	 
	/**
	身份证号
	*/
	var id = $("#idcard").val();
	/**
	手机
	*/
	var cell = $("#cell").val();
	/**
	邮件
	*/
	var mail = $("#mail").val();
	/**
	姓名
	*/
	var name = $("#name").val();
	
	/**
	家庭地址
	*/
	var home_addr = $("#home_addr").val();
	
	/**
	公司地址
	*/
	var biz_addr = $("#biz_addr").val();
	/**
	申请地址
	*/
	var apply_addr = $("#apply_addr").val();
	
	/**
	公司座机号
	*/
	var tel_biz = $("#tel_biz").val();
	/**
	家庭座机号
	*/
	var tel_home = $("#tel_home").val();
	
	/**
	公司名称
	*/
	var biz_workfor = $("#biz_workfor").val();
 
	/**
	联系人姓名
	*/
	var linkman_name = $("#linkman_name").val();
	
	/**
	联系人手机
	*/
	var linkman_cell = $("#linkman_cell").val();
	
	/**
	单位所属行业
	*/
	var biz_industry =  $("#biz_industry").val();
	/**
	学历
	*/
	var educationallevel = $("#educationallevel").val();
	/**
	婚姻状况
	*/
	var marriage= $("#marriage").val();
	/**
	银行卡号
	*/
	var bank_id =  $("#bank_id").val();
	
	var  data = {
	id:id,
	cell:cell,
	mail:mail,
	name:name,
	home_addr:home_addr,
	biz_addr:biz_addr,
	apply_addr:apply_addr,
	tel_biz:tel_biz,
	tel_home:tel_home,
	biz_workfor:biz_workfor,
	linkman_name:linkman_name,
	linkman_cell:linkman_cell,
	biz_industry:biz_industry,
	educationallevel:educationallevel,
	marriage:marriage,
	bank_id:bank_id 
	};
	//alert(JSON.stringify(data) );
 
	var url = "<%=request.getContextPath()%>
	/rest/bairong/search";
		document.getElementById("showmsg").innerHTML = "";
		$.ajax({
		//contentType: "application/x-www-form-urlencoded; charset=UTF-8", 
		url : url,//encodeURI(url),
		type : "POST", data : data, success : function(json) {
			document.getElementById("showmsg").innerHTML = "";
			var ret = JSON.stringify(json);
			//alert(ret);
			document.getElementById("showmsg").innerHTML = ret;
		},
		//data:{companyName:encodeURIComponent(companyName),orgCode:orgCode,constraint:constraint,registerNo:registerNo,activeDays:activeDays,activeDate:activeDate},
		cache : false, timeout : 5000, error : function() {
			alert("error");
		} });
	}
</script>
</html>