new Vue({
	el:'#app',
	data:{
		userName: '',
		password: '',
		password2:'',
		mobile: '',
		code:'',
		codeValue:'获取验证码',
		disabled:false,
		equipmentType:2,//设备类型   1 : pc 2 : android  3 : ios
		checked:false,
		userId : ''
	},
	mounted : function(){

	},

	methods:{



		//发送验证码
		sendCode:function () {
			var that = this,time = 60;


            if(!(/^1[3-9]\d{9}$/.test(that.mobile))) {
				mui.toast("请输入正确的手机号");
				return;
            }

            rest.post({
                url: 'sms/sendReg/{mobile}/1',
                urlParams : {
                    mobile  : that.mobile
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


		// 下一步
		nextStep:function () {
			var that = this;
			//creatMywebview('Register-askman-2.html','Register-askman-2.html')
			// 基础验证
			if(!(/^1[3-9]\d{9}$/.test(that.mobile))) {
				mui.toast("请输入正确的手机号");
				return;
			} else if (that.userName.length < 6 || that.userName.length > 16) {
				mui.toast('请输入6-16位用户名');
				return;
			} else if(that.password.length < 6 || that.password.length > 16) {
				mui.toast('请输入6-16位密码');
				return;
			} else if(that.password2 != that.password) {
				mui.toast('两次输入密码不同');
				return;
			} else if(!that.checked) {
				mui.toast('请阅读并同意《平台服务协议》');
				return;
			} else if(that.code == '') {
				mui.toast('请输入验证码');
				return;
			}

			//服务器注册和验证
            rest.post({
                url: 'user/consultantRegister',
                data : {
                    userName: that.userName,
                    code: that.code,
                    password: that.password,
                    mobile: that.mobile,
                    equipmentType:that.equipmentType
                },
                success: function (result) {
                    mui.toast(result.msg);
                    creatMywebview('Register-askman-2.html?userId=' + result.data,'Register-askman-2.html?userId=' + result.data);
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });


		}
	}
	
});
