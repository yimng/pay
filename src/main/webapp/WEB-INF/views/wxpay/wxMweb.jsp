<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="shortcut icon"   href="<%=basePath %>edupay/img/favicon.ico" type="image/x-icon" />
<link rel="stylesheet"      href="<%=basePath %>edupay/css/zbmain.css"  type="text/css">
<script type="text/javascript"  src="<%=basePath %>edupay/jquery-1.12.3.min.js"></script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<style>
</style>
</head>
<body>

</body>

<script>
var mweb_url = '${mweb_url }';
window.location.href = mweb_url;
</script>
