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
<title>中博支付-请选择支付方式</title>
<style>
</style>
</head>

<body>
<!-- shortcut -->
        <div class="shortcut">
    <div class="w">
        <a class="s-logo" href="https://www.zbgedu.com/" target="_blank" psa="PCashier_jd">
        	<img alt="中博 收银台" src="/edupay/img/logo.png" width="170" height="28">
        </a>
        <ul class="s-right">
            <!-- <li class="s-item fore1">
                <a href="" target="_blank" class="link-user">wduEJmDMkYyFmR</a>
            </li>
            <li class="s-div">|</li>
            <li class="s-item fore2" psa="PCashier_myOrder">
                <a class="op-i-ext" href="" target="_blank">我的订单</a>
            </li>
            <li class="s-div">|</li>
            <li class="s-item fore3" psa="PCashier_help">
                <a class="op-i-ext" target="_blank" href="">支付帮助</a>
            </li> -->
        </ul>
        <span class="clr"></span>
    </div>
</div>

<div class="main">
    <div class="w">
        <!-- order 订单信息 -->
                <div class="order clearfix order-init order-init-oldUser-noQrcode">
    <!-- 订单信息 -->
    <div class="o-left"><h3 class="o-title">订单提交成功，请尽快付款！订单号：${out_trade_no }</h3>

        <p class="o-tips">
                        <span class="o-tips-time" id="deleteOrderTip"></span>

        </p>
    </div>
    <!-- 订单信息 end --><!-- 订单金额 -->
    <div class="o-right">
        <div class="o-price"><em>应付金额</em><strong>${order_price }</strong><em>元</em></div>
            </div>
    <!-- 订单金额 end -->
    <div class="o-list j_orderList" id="listPayOrderInfo"><!-- 单笔订单 -->

        <!-- 多笔订单 end -->
    </div>
</div>
        <!-- order 订单信息 end -->

        <!-- payment 支付方式选择 -->
        <div class="payment">
            <!-- 微信支付 -->
            <div class="pay-weixin">
                <div class="p-w-hd">微信支付</div>
                <div class="p-w-bd" style="position:relative">
                    <div class="j_weixinInfo" style="position:absolute; top: -36px; left: 130px;">距离二维码过期还剩<span class="j_qrCodeCountdown font-bold font-red">60</span>秒，过期后请刷新页面重新获取二维码。</div>
                    <div class="p-w-box">
                        <div class="pw-box-hd">
                            <img alt="s" id="qrcodeImage" style="height:300px;width:300px;" src="${pageContext.request.contextPath}/edupay/wxpay/getQrcode?code_url=${code_url}&out_trade_no=${out_trade_no}&t=<s:property value='verifyResultNote'/>"/>
                        </div>
                        <div class="pw-retry j_weixiRetry" style="display: none;">
                            <a class="ui-button ui-button-gray j_weixiRetryButton" href="javascript:;">获取失败 点击重新获取二维码  </a>
                        </div>
                        <div class="pw-box-ft">
                            <p>请使用微信扫一扫</p>
                            <p>扫描二维码支付</p>
                        </div>
                    </div>
                    <div class="p-w-sidebar"></div>
                </div>
            </div>
            <!-- 微信支付 end -->
            <!-- payment-change 变更支付方式 -->
            <div class="payment-change">
                <a class="pc-wrap" onclick="window.history.go(-1)">
                    <i class="pc-w-arrow-left">&lt;</i>
                    <strong>选择其他支付方式</strong>
                </a>
            </div>
            <!-- payment-change 变更支付方式 end -->
        </div>
        <!-- payment 支付方式选择 end -->
    </div>
</div>

<!-- 收银台 footer -->
<div class="p-footer">
    <div class="pf-wrap w">
        <div class="pf-line">
            <span class="pf-l-copyright">Copyright © 2004-2018  中博.com 版权所有</span>
        </div>
    </div>
</div>
<form  name=formAction  id=formAction  method=post  target="_blank" >
		<input id="out_trade_no" name="out_trade_no" type="hidden"/>
		<input id="time_end"     name="time_end"     type="hidden"/>
		<input id="openid"       name="openid"       type="hidden"/>
		<input id="total_fee"    name="total_fee"    type="hidden"/>
		<input id="return_code"  name="return_code" value="0" type="hidden"/>
	</form>
<script>
// 页面倒计时
  var num = $('.j_weixinInfo span').html();
  var djs = window.setInterval(function(){
    num--;
    if(num<1){
    	//window.location.reload()
    	//去掉定时器
		window.clearInterval(djs);
    }
    $('.j_weixinInfo span').html(num)
  },1000)
  //
  //
  function hello(){
		var out_trade_no = '${out_trade_no}';
		if(out_trade_no!=null){
			sp(out_trade_no);
		}
	} 
	//重复执行某个方法 
	var t1 = window.setInterval(hello, 3000); 
	
	//
	function sp(out_trade_no) {
		$.ajax({
			url: "../wxpay/queryWechatSaoPay",
		    type: "post",
		    data: {out_trade_no: out_trade_no},
		    dataType: "json",
		    contentType: "application/x-www-form-urlencoded; charset=utf-8",
		    success: function (data) { //返回json结果
		    	if(data.return_code==1){
		    		//去掉定时器
		    		window.clearInterval(t1);
		    		
		    		var openid = data.return_code;
		    		var time_end = data.time_end;
		    		var total_fee = data.total_fee;
		    		
		    		document.getElementById('out_trade_no').value = out_trade_no;
		    		document.getElementById('time_end').value = time_end;
		    		document.getElementById('openid').value = openid;
		    		document.getElementById('total_fee').value = total_fee;
		    		
		    		window.location = "../wxpay/paySuc?out_trade_no="+out_trade_no+
		    				"&time_end="+time_end+
		    				"&openid="+openid+
		    				"&total_fee="+total_fee+
		    				"&return_code=1";
		    	}
		    }
		});
	}
</script>
