var vm = new Vue({
	el:'#app',
	data:{
		mobile:'',
		newMobile: '',
		code: '',
        disabled:false,
        codeValue:'获取验证码',
		version:'',
		cache:'',
	},
	mounted:function(){
		
		var
		that = this;
        var loginUserInfo = utils.getLoginUserInfo(true);

		// 获取用户信息
        rest.post({
            url: 'user/userInfo/{userId}',
            urlParams : {
                userId : loginUserInfo.userId
            },
            success: function (result) {
                that.mobile = result.data.mobile;

            }
        });
	},

	methods:{

		//发送验证码
		sendCode: function () {
            var that = this,time = 60;
            if(!(/^1[3-9]\d{9}$/.test(that.newMobile))) {
                mui.toast("请输入正确的手机号");
                return;
            }
            rest.post({
                url: 'sms/send/{mobile}',
                urlParams : {
                    mobile  : that.newMobile
                },
                success: function (result) {
                    that.disabled = true;
                    that.codeValue = '倒计时' + time + 's';
                    time--;

                    var timer = setInterval(function () {
                        if (time == 1) {
                            clearInterval(timer);
                            that.disabled = false;
                            that.codeValue = '获取验证码';
                        } else {
                            time--;
                            that.codeValue = '倒计时' + time + 's';
                        }
                    }, 1000);
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
		},

		// 确认
		sureFn: function () {
            var loginUserInfo = utils.getLoginUserInfo(true);

            var that = this;
			// 基础验证
			if (that.code == '') {
				mui.toast('请输入验证码');
				return;
			}

            rest.post({
                url: 'user/editMobile',
                data : {
                    userId: loginUserInfo.userId,
                    smsCode: that.code,
                    newMobile: that.newMobile
                },
                success: function (result) {
                    mui.toast(result.msg);
                    creatMywebview('Myinfo.html', 'Myinfo.html');
                },
                fail : function (result) {
                    mui.toast(result.msg);
                }
            });

		}
		
	}
	
});



