<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%response.setStatus(200);%>
<c:set var="base" value="${pageContext.request.contextPath }"></c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>404</title>
    <style type="text/css">

        a:link, a:visited {
            color: #007ab7;
            text-decoration: none;
        }

        h1 {
            position: relative;
            z-index: 2;
            width: 540px;
            height: 0;
            margin: 110px auto 15px;
            padding: 230px 0 0;
            overflow: hidden;
            background-image: url(${base}/static/images/Main.jpg);
            background-repeat: no-repeat;
        }

        h2 {
            position: absolute;
            top: 55px;
            left: 233px;
            margin: 0;
            font-size: 0;
            text-indent: -999px;
            -moz-user-select: none;
            -webkit-user-select: none;
            user-select: none;
            cursor: default;
            width: 404px;
            height: 90px;
        }

        h2 em {
            display: block;
            font: italic bold 200px/120px "Times New Roman", Times, Serif;
            text-indent: 0;
            letter-spacing: -5px;
            color: rgba(216, 226, 244, 0.3);
        }

        .link a {
            margin-right: 1em;
        }

        .link, .texts {
            width: 540px;
            margin: 0 auto 15px;
            color: #505050;
        }

        .texts {
            line-height: 2;
        }

        .texts dd {
            margin: 0;
            padding: 0 0 0 15px;
        }

        .texts ul {
            margin: 0;
            padding: 0;
        }
    </style>
    <!--[if lte IE 8]>
    <style type="text/css">
        h2 em {
            color: #e4ebf8;
        }
    </style>
    <![endif]-->
</head>
<body>
<h1></h1>

<p class="link">
    <a href="${base}/">返回首页</a>
    <a href="javascript:history.go(-1);">返回上一页</a>
</p>
<dl class="texts">
</dl>

</span></span></span></p>
</body>
</html>
