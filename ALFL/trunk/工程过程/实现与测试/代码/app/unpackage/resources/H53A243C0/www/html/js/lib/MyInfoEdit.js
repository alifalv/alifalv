var vm = new Vue({
	el:'#app',
	data:{
		userInfo:{},
		ImgBase64Str:''
	},
	mounted:function(){
		
		var that = this;

		// 获取用户信息

        var loginUserInfo = utils.getLoginUserInfo(true);
        rest.post({
            url: 'user/userInfo/{userId}',
            urlParams : {
                userId: loginUserInfo.userId
            },
            success: function (result) {
                that.userInfo = result.data;
                that.ImgBase64Str = utils.createImgUrl(result.data.userImg);
            },
            fail: function (result) {}
        });
	},

	methods:{
        setErrorImg : function(e) {
            $(e.target).attr('src', 'images/index/idx-defaluthead.png');
        },

		// 上传头像图片
		onRead: function (file) {
			var that = this;
			rest.post({
				url: 'file/uploadImg',
				data: {
					imgBase64Str : file.content
				},
				success: function (result) {
                    that.ajaxChangeHeader(result.data);
                },
				fail: function (result) {
					mui.toast(result.msg);
				},
				error :function() {
					mui.toast('连接失败');
				}
			});

		},

        /**
         * 修改头像
         * @param imgName
         */
        ajaxChangeHeader: function (imgName) {
            var that = this;
            var loginUserInfo = utils.getLoginUserInfo(true);
            rest.post({
                url: 'user/changeUserProfile',
                data: {
                    userId: loginUserInfo.userId,
                    newImg: imgName
                },
                success: function (result) {
                    mui.toast(result.msg);
                    that.ImgBase64Str = utils.createImgUrl(imgName);
                },
                fail: function (result) {
                    mui.toast(result.msg || '修改头像失败');
                }
            });
        }
	}
	
});



