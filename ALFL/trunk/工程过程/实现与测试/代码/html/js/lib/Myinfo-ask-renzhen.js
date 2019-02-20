new Vue({
	el:'#app',
	data:{
		// 个人咨询师认证
		realName: '',//真实姓名
		bankAccount: '',//银行卡号

		workImg:'',
		personImg:'',
		ImgBase64Str:'',//临时存放上传图片

		btn: '提交审核',

	},
	mounted: function(){
		var that = this, flag = true;
	},

	methods:{
	    a :function() {
	      alert(1)
        },


		// 上传职业证件照片
		onRead1: function (file) {
			var that = this;
			that.ImgBase64Str = file.content;

            rest.post({
                url: 'file/uploadImg',
                data : {
                    ImgBase64Str: that.ImgBase64Str
                },
                success: function (res) {
                    that.workImg = res.data;
                },
                fail: function (result) {
                    mui.toast('上传失败')
                }
            });
		},

		// 上传形象照片
		onRead2: function (file) {
			var that = this;
			that.ImgBase64Str = file.content;

            rest.post({
                url: 'file/uploadImg',
                data : {
                    ImgBase64Str: that.ImgBase64Str
                },
                success: function (res) {
                    that.personImg = res.data;
                },
                fail: function (result) {
                    mui.toast('上传失败')
                }
            });
		},


		// 提交审核
		saveFn:function (index) {
			var that = this;

			// 基础验证
			if (that.realName == '') {
				mui.toast("请输入真实姓名");
				return;
			} else if (that.bankAccount == '') {
				mui.toast('请输入银行卡号');
				return;
			// } else if (that.workImg == '') {
			// 	mui.toast('请上传证件照片正面');
			// 	return;
			// } else if (that.personImg == '') {
			// 	mui.toast('请上传证件照片反面');
			// 	return;
			}

            mui.plusReady(function() {
                var main = plus.webview.getWebviewById("Register-askman-2.html");
                //自定义事件,事件名为changeCity
                mui.fire(main, 'changeZiLiao', {
                    realName: that.realName,
                    idCard:that.bankAccount,
                    idCardFront : that.workImg,
                    idCardBack : that.personImg
                });
                //关闭子页面
                mui.back();
            });
		}


	}
	
});

