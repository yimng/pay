<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
String appId = request.getParameter("appid");
String timeStamp = request.getParameter("timeStamp");
String nonceStr = request.getParameter("nonceStr");
String packageValue = request.getParameter("package");
String paySign = request.getParameter("paySign");

String orderNo = request.getParameter("orderNo");
String totalFee  = request.getParameter("totalFee");
%>
<!DOCTYPE html>
<html>
<head>
<title>微信支付</title>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densitydpi=device-dpi">
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<meta name="format-detection" content="telephone=no"/>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<style>
/* CSS RESET */
* { padding: 0; margin: 0; }
body { font: 12px "微软雅黑", Arial; background: #efeff4; min-width: 320px; max-width: 640px; color: #000; }
a { text-decoration: none; color: #666666; }
a, img { border: none; }
img { vertical-align: middle; }
ul, li { list-style: none; }
em, i { font-style: normal; }
.clear { clear: both }
.clear_wl:after { content: "."; height: 0; visibility: hidden; display: block; clear: both; }
.fl { float: left }
.fr { float: right }
.all_w { width: 91.3%; margin: 0 auto; }
.cancelpay { width: 91.3%; margin: 0 auto; }
/*基础字体属性*/
.f10 { font-size: 10px }
.f11 { font-size: 11px }
.f12 { font-size: 12px }
.f14 { font-size: 14px }
.f13 { font-size: 13px }
.f16 { font-size: 16px }
.f18 { font-size: 18px }
.f20 { font-size: 20px }
.f22 { font-size: 22px }
.f24 { font-size: 24px }
.f26 { font-size: 26px }
.f28 { font-size: 28px }
.f32 { font-size: 32px }
.fb { font-weight: bold }
/********/
.header { background: #393a3e; color: #f5f7f6; height: auto; overflow: hidden; }
.gofh { float: left; height: 45px; display: -webkit-box; -webkit-box-orient: horizontal; -webkit-box-pack: center; -webkit-box-align: center; }
.gofh a { padding-right: 10px; border-right: 1px solid #2e2f33; }
.gofh a img { width: 50%; }
.ttwenz { float: left; height: 45px; }
.ttwenz h4 { font-size: 16px; font-weight: 400; margin-top: 2px; }
.ttwenz h5 { font-size: 12px; font-weight: 400; color: #6c7071; }
.wenx_xx { text-align: center; font-size: 16px; padding: 18px 0; }
.wenx_xx .wxzf_price { font-size: 45px; }
.skf_xinf { height: 43px; border-top: 1px solid #ddd; border-bottom: 1px solid #ddd; line-height: 43px; background: #FFF; font-size: 12px; overflow: hidden; }
.skf_xinf .bt { color: #767676; float: left; }
.ljzf_but { border-radius: 3px; height: 45px; line-height: 45px; background: #44bf16; display: block; text-align: center; font-size: 16px; margin-top: 14px; color: #fff; }
.qxzf_but { border-radius: 3px; height: 45px; line-height: 45px; background: #bbbbbb; display: block; text-align: center; font-size: 16px; margin-top: 14px; color: #fff; }
/**/
.ftc_wzsf { display:none; width: 100%; height: 100%; position: fixed; z-index: 999; top: 0; left: 0; }
.ftc_wzsf .hbbj { width: 100%; height: 100%; position: absolute; z-index: 8; background: #000; opacity: 0.4; top: 0; left: 0; }
.ftc_wzsf .srzfmm_box { position: absolute; z-index: 10; background: #f8f8f8; width: 88%; left: 50%; margin-left: -44%; top: 25px; }
.qsrzfmm_bt { font-size: 16px; border-bottom: 1px solid #c9daca; overflow: hidden; }
.qsrzfmm_bt a { display: block; width: 10%; padding: 10px 0; text-align: center; }
.qsrzfmm_bt img.tx { width: 10%; padding: 10px 0; }
.qsrzfmm_bt span { padding: 15px 5px; }
.zfmmxx_shop { text-align: center; font-size: 12px; padding: 10px 0; overflow: hidden; }
.zfmmxx_shop .mz { font-size: 14px; float: left; width: 100%; }
.zfmmxx_shop .wxzf_price { font-size: 24px; float: left; width: 100%; }
.blank_yh { width: 89%; margin: 0 auto; line-height: 40px; display: block; color: #636363; font-size: 16px; padding: 5px 0; overflow: hidden; border-bottom: 1px solid #e6e6e6; border-top: 1px solid #e6e6e6; }
.blank_yh img { height: 40px; }
.ml5 { margin-left: 5px; }
.mm_box { width: 89%; margin: 10px auto; height: 40px; overflow: hidden; border: 1px solid #bebebe; }
.mm_box li { border-right: 1px solid #efefef; height: 40px; float: left; width: 16.3%; background: #FFF; }
.mm_box li.mmdd{ background:#FFF url(../images/dd_03.jpg) center no-repeat ; background-size:25%;}
.mm_box li:last-child { border-right: none; }
.xiaq_tb { padding: 5px 0; text-align: center; border-top: 1px solid #dadada; }
.numb_box { position: absolute; z-index: 10; background: #f5f5f5; width: 100%; bottom: 0px; }
.nub_ggg { border: 1px solid #dadada; overflow: hidden; border-bottom: none; }
.nub_ggg li { width: 33.3333%; border-bottom: 1px solid #dadada; float: left; text-align: center; font-size: 22px; }
.nub_ggg li a { display: block; color: #000; height: 50px; line-height: 50px; overflow: hidden; }
.nub_ggg li a:active  { background: #e0e0e0;}
.nub_ggg li a.zj_x { border-left: 1px solid #dadada; border-right: 1px solid #dadada; }
.nub_ggg li span { display: block; color: #e0e0e0; background: #e0e0e0; height: 50px; line-height: 50px; overflow: hidden; }
.nub_ggg li span.del img { width: 30%; }

.fh_but{ position:absolute; right:0px; top:12px; font-size:14px; color:#20d81f;}
.zfcg_box{ background:#f2f2f2;  height: 56px; line-height:56px;   font-size:20px; color:#1ea300; }
.zfcg_box img{ width:10%;}

.cgzf_info{ background:#FFF; border-top:1px solid #dfdfdd; }

.spxx_shop{ background:#FFF; margin-left:4.35%; border-top:1px solid #dfdfdd; padding:10px 0; }
.spxx_shop td{ color:#7b7b7b; font-size:14px; padding:10px 0;}

.wzxfcgde_tb{ position:fixed; width:100%; z-index:999; bottom:20px; text-align:center;}
.wzxfcgde_tb img{ width:20.6%;}
.mlr_pm{margin-right:4.35%;}

</style>
<body>
<div class="header">
  <div class="all_w ">
    <div class="gofh"> <a href="#"><img src="/edupay/img/logo.png" ></a> </div>
    <div class="ttwenz">
<!--       <h4>确认交易</h4> -->
<!--       <h5>微信安全支付</h5> -->
    </div>
  </div>
</div>
<div class="wenx_xx">
  <div class="mz">支付单号：${orderNo }</div>
  <div class="wxzf_price">￥${totalFee }</div>
</div>
<!-- <div class="skf_xinf"> -->
<!--   <div class="all_w"> <span class="bt">收款方</span> <span class="fr"> </span> </div> -->
<!-- </div> -->
<a onclick="callpay()" class="ljzf_but all_w">立即支付</a>
<a onclick="cancelpay()" class="qxzf_but cancelpay">取消支付</a>
</body>
<script type="text/javascript">

$(function(){
	$(".affirm-info").height($(window).height());
	$(window).resize(function(){
		//main-body高度适应屏幕
		$(".affirm-info").height($(window).height());
	});
});
function onBridgeReady(){
    WeixinJSBridge.invoke('getBrandWCPayRequest',{
    	"appId" : '${appid}',
	     "timeStamp" : '${timeStamp}',
	     "nonceStr" : '${nonceStr}', 
	     "package" : "prepay_id="+'${packagess}',
	     "signType" : "MD5",
	     "paySign" : '${paySign}' 
	     },function(res){
		    //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
         if(res.err_msg == "get_brand_wcpay_request:ok"){
        	 //微信 自带 支付成功效果
        	 var out_trade_no = '${orderNo}';
             var url = "http://pay.zbgedu.com/edupay/wxpay/paySuc?out_trade_no=" + out_trade_no;//http://pay.zbgedu.com/edupay/
             window.location.href = url;
         }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
             alert("用户取消支付!");
             var orderId = '${orderId}';
             var url = "https://www.zbgedu.com/index/Usercenter/myorderinfo.html?id=" + orderId;
             window.location.href = url;
         }else if(res.err_msg == "get_brand_wcpay_request：fail"){  
             alert("支付失败!");
             var orderId = '${orderId}';
             var url = "https://www.zbgedu.com/index/Usercenter/myorderinfo.html?id=" + orderId;
             window.location.href = url;
         }  
	})
}
function callpay(){  
      if (typeof WeixinJSBridge == "undefined"){
         if( document.addEventListener ){
               document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
           }else if (document.attachEvent){
               document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
               document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
          }
       }else{
         onBridgeReady();
       }
}

function cancelpay(){
	var orderId = '${orderId}';
    var url = "https://www.zbgedu.com/index/Usercenter/myorderinfo.html?id=" + orderId;
    window.location.href = url;
}

function accDiv(arg1,arg2){
    var t1=0,t2=0,r1,r2;
    try{t1=arg1.toString().split(".")[1].length;}catch(e){}
    try{t2=arg2.toString().split(".")[1].length;}catch(e){}
    with(Math){
        r1=Number(arg1.toString().replace(".",""));
        r2=Number(arg2.toString().replace(".",""));
        return (r1/r2)*pow(10,t2-t1);
    }
}
</script>
</html>
