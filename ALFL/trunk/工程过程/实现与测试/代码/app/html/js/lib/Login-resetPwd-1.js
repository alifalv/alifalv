new Vue({
	el:'#app',
	data:{
		userName: null,
		mobile: null,
		code:null,
		equipmentType:'android'//设备类型
	},
	mounted : function(){
		
	},

	methods:{

		//发送验证码
		sendCode:function () {
            var that = this;
			if(!(/^1[34578]\d{9}$/.test(that.mobile))) {
				mui.toast("请输入正确的手机号");
				return;
			}

            rest.post({
                isNeedToken: false,
                url: 'sms/send/{mobile}',
                urlParams : {
                    mobile  : that.mobile
                },
                success: function (result) {
                    mui.toast(result.msg);
                },
                fail: function (result) {
                    mui.toast(result.msg);

                }
            });
		},

		// 下一步
		nextStep:function () {
			var that = this;
			// 基础验证
			if(!(/^1[34578]\d{9}$/.test(that.mobile))) {
				mui.toast("请输入正确的手机号");
				return;
			}else if(that.code == null) {
				mui.toast('请输入验证码');
				return;
			}


            rest.post({
                isNeedToken: false,
                url: 'user/consultantRegister',
                data : {
                    userName: that.userName,
                    password: that.password,
                    mobile: that.mobile,
                    equipmentType:that.equipmentType
                },
                success: function (result) {
                    storage.setItem("userInfo",res.rows);//缓存用户基本信息
                    mui.toast('注册成功');
                    creatMywebview('Register-askman-2.html','Register-askman-2.html');
                },
                fail: function (result) {
                    mui.toast(result.msg);

                }
            });
		}
	}
	
});
