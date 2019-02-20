
var vm = new Vue({
	el:'#app',
	data:{
		userName: null,
		password: null,
		deviceId: '868403029831879'//plus.device.uuid
	},
	mounted: function(){ 
		
	},

	methods:{
		// 登录验证
		loginFn:function () {
			var that = this;
			//基础验证
			if (that.userName == null || that.userName.length < 5 || that.userName.length > 16) {
				mui.toast('请输入5-16位用户名');
				return;
			} else if(that.password == null) {
				mui.toast('请输入密码');
				return;
			} 
            rest.post({
                isNeedToken: false,
                url: 'user/login',
                data : {
                    userName: that.userName,
                    password: that.password,
                    deviceId: that.deviceId
                },
                success: function (result) {
                    mui.toast(result.msg);
                    store.setCache('userInfo', result.data);
                    store.setCache('token', result.data.token);

                    // storage.setItem( 'userId', result.data.id);
                    // storage.setItem( 'token', result.data.token);


                    var urlParams = utils.getUrlParams();
                    if(urlParams.jump) {
                        location.replace(urlParams.jump);
                    } else {
                        location.replace('Myinfo.html');
                    }
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
		}
	}
	
})




var auths = {};

function plusReady() {

	// 获取登录认证通道
	plus.oauth.getServices(function(services) {
		for(var i in services) {
			var service = services[i];
			auths[service.id] = service;
		}
	}, function(e) {
		//outLine("获取登录认证失败：" + e.message);
	});
}
document.addEventListener('plusready', plusReady, false);
// 登录认证
function login(id) {
	var auth = auths[id];
	if(auth) {
		var w = null;
		if(plus.os.name == "Android") {
			w = plus.nativeUI.showWaiting();
		}
		document.addEventListener("pause", function() {
			setTimeout(function() {
				w && w.close();
				w = null;
			}, 2000);
		}, false);
		auth.login(function() {
			w && w.close();
			w = null;
			userinfo(auth);
		}, function(e) {
			w && w.close();
			w = null; 
			plus.nativeUI.alert("详情错误信息请参考授权登录(OAuth)规范文档：http://www.html5plus.org/#specification#/specification/OAuth.html", null, "登录失败[" + e.code + "]：" + e.message);
		});
	} else {
		plus.nativeUI.alert("无效的登录认证通道！", null, "登录");
	}
}
// 获取用户信息
function userinfo(a) {
	a.getUserInfo(function() {
		plus.nativeUI.alert("登录成功！", null, "登录");
		console.log(JSON.stringify(a.userInfo))
		var wvs = plus.webview.getWebviewById('Myinfo.html');
		mui.fire(wvs, 'refreshMyinfo', {
			userInfo: a.userInfo
		});
	}, function(e) {
		plus.nativeUI.alert("获取用户信息失败！", null, "登录");
	});
}

// 注销登录
function logoutAll() {
	for(var i in auths) {
		logout(auths[i]);
	}
}

function logout(auth) {
	auth.logout(function() {
		//outLine("注销\"" + auth.description + "\"成功");
	}, function(e) {
		//outLine("注销\"" + auth.description + "\"失败：" + e.message);
	});
}

