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
	<title>应用</title>
</head>
<style>
	
</style>
<body>
	<form name="formAction" id="formAction" method="post">
		<table>
			<tr>
				<td>应用名称</td><td>地址</td><td>密钥</td><td>创建时间</td><td>状态</td><td>操作</td>
			</tr>
			<c:forEach items="${list }" var="app" varStatus="status" >
				<tr>
					<td>${app.appName }</td><td>${app.url }</td>
					<td>${app.token }</td>
					<td><fmt:formatDate value="${app.createDate }" pattern="yyyy-MM-dd"/></td>
					<td>${app.state==1?"启用":"禁用" }</td>
					<td><input type="button" value="编辑" onclick="getAppById(${app.appId })" /></td>
				</tr>
			</c:forEach>
			<tr>
				<td></td>
				<td><input type="button" value="新增" onclick="add()" /></td>
			</tr>
		</table>
	</form>
	
</body>
<script>
	function add(){
		var page = "appManage/appInfo";
		formAction("../app/toPage?page="+page);
	}
	function getAppById(appId){
		formAction("../app/getAppById?appId="+appId);
	}
	function formAction(url)
	{
		document.formAction.action=url;
		document.formAction.submit();
	}
</script>
</html>
