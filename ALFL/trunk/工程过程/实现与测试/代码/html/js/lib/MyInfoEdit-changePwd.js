new Vue({
	el:'#app',
	data:{
		oldPassword: '',
		password: '',
		password2: ''
	},
	mounted : function(){
		var
		that = this;
	},

	methods:{

		// 确认
		sureFn:function () {
			var that = this;
            var loginUserInfo = utils.getLoginUserInfo(true);

            //creatMywebview('Register-askman-2.html','Register-askman-2.html')
			// 基础验证
			if (that.oldPassword == '') {
				mui.toast('请输入旧密码');
				return;
			} else if (that.password == '') {
				mui.toast('请输入新密码');
				return;
			} else if(that.password2 != that.password) {
				mui.toast('两次输入密码不同');
				return;
			}

            rest.post({
                url: 'user/editPassword',
                data : {
                    userId: loginUserInfo.userId,
                    oldPassword: that.oldPassword,
                    newPassword: that.password
                },
                success: function (result) {
                    mui.alert(result.msg,'提示',function () {
                        creatMywebview('Set-center', 'Set-center');
                    })
                },
                fail : function (result) {
                    mui.toast(result.msg);
                }
            });


		}
	}
	
})
