<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<style>
</style>
</head>
<body>

	正在跳转商户页面...
	
	<form name="formAction" id="formAction" method="post">
		<input id="encryptCode" name="encryptCode" value=${encryptCode }  type="hidden"/>
		<input id="orderId"     name="orderId"     value=${orderId }      type="hidden"/>
		<input id="payStatus"   name="payStatus"   value=${payStatus }    type="hidden"/>
	</form>
</body>
<script language="javascript">
window.onload=function(){
	//
// 	formAction("https://wwwdemo.zbgedu.com/index/pay/notify.html");//demo环境
	//
// 	formAction("https://wwwt.zbgedu.com/index/pay/notify.html");//预生产环境
	//
	formAction("https://www.zbgedu.com/index/pay/notify.html");//正式生产环境
}
function formAction(url)
{
	document.formAction.action=url;
	document.formAction.submit();
}
</script>
</html>