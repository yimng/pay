<%@ page language="java" import="com.edupay.app.entity.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="base" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8">
	<title>应用信息</title>
	
</head>
<body>
	<form name="formAction" id="formAction" method="post">
	<input type="hidden" id="appId" name="appId" value="${app.appId }" />
		<table>
			<tr>
				<td>应用名称：</td>
				<td><input type="text" id="appName" name="appName" value="${app.appName }" /></td>
			</tr>
			<tr>
				<td>接口地址：</td>
				<td><input type="text" id="url" name="url" value="${app.url }" /></td>
			</tr>
			<tr>
				<td>密钥：</td>
				<td><input type="text" id="token" name="token" value="${app.token }" onfocus=this.blur() style="background:#CCCCCC" />
					<input type="button" value="更换密钥" onclick="updToken()" />
				</td>
			</tr>
			<tr>
				<td>状态：</td>
				<td>
					<select name="state">
						<option value ="1" <c:if test="${app.state==1 }">selected="selected"</c:if> >启用</option>
						<option value ="0" <c:if test="${app.state!=1 }">selected="selected"</c:if> >禁用</option>
					</select>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input type="button" value="保存" onclick="save()" /></td>
			</tr>
			
		</table>
	</form>
	
</body>
<script>
	function save(){
		var appId = document.getElementById('appId').value;
		var appName = document.getElementById('appName').value;
		var url = document.getElementById('url').value;
		if(appName=="" || url==""){
			alert("名称、地址不能为空。");
			return;
		}
		if(appId!=""){
			formAction("../app/updApp");
		}else{
			formAction("../app/addApp");
		}
	}
	function updToken(){
		var tk = (S4()+S4()+S4()+S4()+S4()+S4()+S4()+S4());
		document.getElementById('token').value = tk;
	}
	function S4(){
		return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	}
	function formAction(url)
	{
		document.formAction.action=url;
		document.formAction.submit();
	}
</script>
</html>
