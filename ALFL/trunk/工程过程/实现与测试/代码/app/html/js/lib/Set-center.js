
var vm = new Vue({
	el:'#app',
	data:{
		mobile:'',
		version:'',
		cache:''
	},
	mounted : function(){
        var loginUserInfo = utils.getLoginUserInfo(true);

        var that = this;

		// 获取应用版本
		plus.runtime.getProperty(plus.runtime.appid, function (inf) {
			that.version = inf.version;
		});

		// 获取应用缓存大小
		plus.cache.calculate(function (size) {
			that.cache = (parseInt(size) / 1024 / 1024).toFixed(2);
		});

		// 获取用户信息
        rest.post({
            url: 'user/userInfo/{userId}',
            urlParams : {
                userId: loginUserInfo.userId
            },
            success: function (result) {
                that.mobile = result.data.mobile;
            },
            fail: function (result) {}
        });

	},

	methods:{
		//清理缓存
		clearStorge:function () {
			
			plus.cache.clear();
			this.cache = 0;
			mui.toast('清除缓存成功');
		},

		//登出
		logout:function () {
			creatMywebview('Login.html', 'Login.html');
            store.setCache('userInfo', '');
            store.setCache('token', '');
		}
	}
	
})



