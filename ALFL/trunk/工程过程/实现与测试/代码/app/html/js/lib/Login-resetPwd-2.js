new Vue({
	el:'#app',
	data:{
		userName: null,
		password: null,
		password2:null,
		mobile: null,
		code:null,
		equipmentType:'android',//设备类型
		checked:false,
	},
	mounted(){
		
	},

	methods:{

		//发送验证码
		sendCode:function () {

			if(!(/^1[34578]\d{9}$/.test(that.mobile))) {
				mui.toast("请输入正确的手机号");
				return;
			}
			ajax_json(
				"ali-legal/api/sms/send/"+that.mobile, 
				{},
				function (res) {
					if (res.code === 1) {
						mui.toast('发送成功');
					}else{
						mui.toast('发送失败');
					}
				}
			)
		},

		// 下一步
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
			} else if(!that.checked) {
				mui.toast('请阅读并同意《平台服务协议》');
				return;
			} else if(that.code == null) {
				mui.toast('请输入验证码');
				return;
			}

			//服务器注册和验证
			ajax_json(
				"ali-legal/api/user/consultantRegister", 
				{
					userName: that.userName,
					password: that.password,
					mobile: that.mobile,
					equipmentType:that.equipmentType
				},
				function (res) {
					if (res.code === 1) {
						storage.setItem("userInfo",res.rows);//缓存用户基本信息
						mui.toast('注册成功');
						creatMywebview('Register-askman-2.html','Register-askman-2.html');
					}else{
						//用户名已存在
						//验证码错误
						
					}
				}
			)
		}
	}
	
})
