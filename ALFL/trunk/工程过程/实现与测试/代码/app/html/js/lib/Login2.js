
var vm = new Vue({
	el:'#app',
	data:{
		userName: null,
		password: null,
		deviceId: '868403029831879'//plus.device.uuid
	},
	mounted: function(){
		// this.plusReady() 
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

 mui.plusReady(function() {  
            plus.oauth.getServices(function(services) {
                auths = services;
            }, function(e) {
                alert("获取登录服务列表失败：" + e.message + " - " + e.code);
            });
        })

     

        // 登录操作
        function authLogin(type) {
			 
            var s;
            for (var i = 0; i < auths.length; i++) {
                if (auths[i].id == type) {
                    s = auths[i];
                    break;
                }
            }
			// alert(s)
			
            if (!s.authResult) {
                s.login(function(e) {
					// alert(JSON.stringify(e))
                    mui.toast("登录认证成功！" );
					 
                    authUserInfo(type);
                }, function(e) {
					// alert(JSON.stringify(e))
                    mui.toast("登录认证失败！");
                });
            } else {
                // mui.toast("已经登录认证！");
				 authUserInfo(type);
            }
        }
        //注销
        function authLogout() {
            for (var i in auths) {
                var s = auths[i];
                if (s.authResult) {
                    s.logout(function(e) {
                        console.log("注销登录认证成功！");
                    }, function(e) {
                        console.log("注销登录认证失败！");
                    });
                }
            }
        }
        // 微信登录认证信息
        function authUserInfo(type) {
            var s;
            for (var i = 0; i < auths.length; i++) {
                if (auths[i].id == type) {
                    s = auths[i];
                    break;
                }
            }
            if (!s.authResult) {
                mui.toast("未授权登录！");
            } else {
                s.getUserInfo(function(e) {
                    var josnStr = JSON.stringify(s.userInfo);
					 
                    var jsonObj = s.userInfo;
                    // console.log("获取用户信息成功：" + josnStr);
//                     alert(josnStr)
//                     showData(type,jsonObj); 
                    // authLogout();
					
					// plus.nativeUI.alert("登录成功！", null, "登录");
					 var openId;
					 var optype;
					 if(s.userInfo.openid){
						  openId = s.userInfo.openid;
						  optype = "WECHAT"
					 }else {
						 openId = s.authResult.openid
						 optype = "QQ"
					 }
					// alert(openId)
					check(openId,optype)  //验证用户是否绑定
					

                }, function(e) { 
                    alert("获取用户信息失败：" + e.message + " - " + e.code);
                });
            }
        }
		
		 function check(openId,optype) {  //验证用户是否绑定
		 	 var that = this;
			 store.setCache('openId', openId);
			 store.setCache('optype', optype);
			 rest.post({
			     isNeedToken: false,
			     url: 'user/getThreeLoginFlag',
			     data : {
			         openId : openId,
			         openType : optype,
			     },
			     success: function (result) {
			         // mui.toast(result.msg + "1111");
					 
					 
					 if(result.code == 1){
						  store.setCache('userInfo', result.data);
						  store.setCache('token', result.data.token);
						  var urlParams = utils.getUrlParams();
						  if(urlParams.jump) {
						      location.replace(urlParams.jump);
						  } else {
						      location.replace('Myinfo.html');
						  }
					 }else{
						 location.replace('Login-bind.html');
					 }
			     },
			     fail: function (result) {
					 // debugger
			         mui.toast(result.msg);
					 location.replace('Login-bind.html');
			     }
			 });
			 
			 
			 
		 }
		
		
        // 显示用户头像信息
        function showData(type,data) {
//             switch (type){
//                 case 'weixin':
//                     headImage.src = data.headimgurl;
//                     break;
//                 case 'qq':
//                     headImage.src = data.figureurl_qq_2;
//                     break;
//                 case 'sinaweibo':
//                     headImage.src = data.avatar_large;
//                     break;
//                 default:
//                     break;
//             }
        }
 
 
//  function userinfo(a) {
//  	a.getUserInfo(function() {
//  		plus.nativeUI.alert("登录成功！", null, "登录");
//  		console.log(JSON.stringify(a.userInfo))
//  		var wvs = plus.webview.getWebviewById('Myinfo.html');
//  		mui.fire(wvs, 'refreshMyinfo', {
//  			userInfo: a.userInfo
//  		});
//  	}, function(e) {
//  		plus.nativeUI.alert("获取用户信息失败！", null, "登录");
//  	});
//  }




