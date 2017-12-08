

	function changeImg() {
		var imgSrc = $("#imgObj");
		var src = imgSrc.attr("src");
		imgSrc.attr("src", chgUrl(src));
	}
	// 时间戳
	// 为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳
	function chgUrl(url) {
		var timestamp = (new Date()).valueOf();
		url = url.substring(0, 17);
		if ((url.indexOf("&") >= 0)) {
			url = url + "×tamp=" + timestamp;
		} else {
			url = url + "?timestamp=" + timestamp;
		}
		return url;
	}

	document.onkeydown = function(e) {
		var event = e || window.event;
		var code = event.keyCode || event.which || event.charCode;
		if (code == 13) {
			login();
		}
	}
	
	function cleardata() {
		$('#loginForm').form('clear');
	}
	
	function login() {
		if ($("input[name='username']").val() == ""
				|| $("input[name='password']").val() == ""
				|| $("input[name='code']").val() == "") {
			$("#showMsg").html("用户名、密码或验证码为空，请输入");
			$("input[name='username']").focus();
		} else {
			// ajax异步提交
			$.ajax({
				type : "POST", // post提交方式默认是get
				url : "tologin.action",
				data : $("#loginForm").serialize(), // 序列化
				error : function() { // 设置表单提交出错
					$("#showMsg").html(); // 登录错误提示信息
				},
				success : function(data) {
					
					if (data.status == "1") {
						$("#showMsg").html(data.msg);
					} else {

						document.location = "index";
					}
				}
			});
		}
	}
	
	$(function() {
	// bg switcher
	$("input[name='username']").focus();
	var $btns = $(".bg-switch .bg");
	$btns.click(function(e) {
		e.preventDefault();
		$btns.removeClass("active");
		$(this).addClass("active");
		var bg = $(this).data("img");

		$("html").css("background-image",
				"url('../mystatic/img/bgs/" + bg + "')");
	});
	})
