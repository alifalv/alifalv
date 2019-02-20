new Vue({
	el:'#app',
	data:{
        disabled:false,
        codeValue:'获取验证码',

        userName: null,
		password: null,
		password2:null,
		mobile: null,//手机
		code:null,//验证码
		companyName:null,//单位全称
		companyCode:null,//社会信用代码
		equipmentType:1,//设备类型
		checked:false//是否同意服务协议
	},
	mounted :function(){
		var that = this;

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
                url: 'sms/sendReg/{mobile}/3',
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

		// 注册
		nextStep:function () {
			var that = this;
			//creatMywebview('Register-askman-2.html','Register-askman-2.html')
			// 基础验证
			if(!(/^1[34578]\d{9}$/.test(that.mobile))) {
				mui.toast("请输入正确的手机号");
				return;
			} else if (that.userName == null || that.userName.length < 6 || that.userName.length > 16) {
				mui.toast('请输入6-16位用户名');
				return;
			} else if(that.password == null || that.password.length < 6 || that.password.length > 16) {
				mui.toast('请输入6-16位密码');
				return;
			} else if(that.password2 != that.password) {
				mui.toast('两次输入密码不同');
				return;
			} else if (that.companyName == that.companyCode == null) {
				mui.toast('请完善企业信息');
				return;
			} else if (!that.checked) {
				mui.toast('请阅读并同意《平台服务协议》');
				return;
			} else if (that.code == null) {
				mui.toast('请输入验证码');
				return;
			}

			//服务器注册和验证
            //服务器注册和验证
            rest.post({
                url: 'user/consultantRegister',
                data: {
                    userName: that.userName,
                    mobile: that.mobile,
                    code : that.code,
                    password: that.password,
                    companyName:that.companyName,//单位全称
                    companyCode:that.companyCode,//社会信用代码
                    equipmentType: that.equipmentType
                },
                success: function (res) {
                    mui.toast(res.msg);
                    creatMywebview('Register-qiye-2.html','Register-qiye-2.html');
                }
            });



		}
	}
	
});

