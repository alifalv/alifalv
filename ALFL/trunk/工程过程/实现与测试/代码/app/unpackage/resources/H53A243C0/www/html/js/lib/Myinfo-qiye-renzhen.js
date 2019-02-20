var tempAreaList = {};//省市列表缓存
var userId = storage.getItem('userId'),
	userToken = storage.getItem('token');

new Vue({
	el:'#app',
	data:{
		companyName: '',
		companyCode: '',
		realName:'',
		registerAddress: '',
		workAddress: '',
		weChat: '',
		qq: '',
		email: '',
		workImg:'',
		ImgBase64Str:'',//临时存放上传图片

		btn: '提交审核',

	},
	mounted(){
		var that = this, flag = true;

		
	},

	methods:{


		// 上传照片
		onRead: function (file) {
			var that = this;
			that.ImgBase64Str = file.content;

			ajax_json(
				"ali-legal/api/file/uploadImg",
				{ ImgBase64Str: that.ImgBase64Str },
				function (res) {
					if (res.code === 1) {
						that.workImg = res.data;
					} else {
						mui.toast('上传失败')
					}
				}
			)
		},


		// 提交审核
		saveFn:function () {
			var that = this;

			// 基础验证
			if (that.companyName == '') {
				mui.toast("请输入单位全称");
				return;
			} else if (that.bankAccountName == '') {
				mui.toast('请输入单位代码');
				return;
			} else if (that.registerAddress == '') {
				mui.toast('请输入注册地址');
				return;
			} else if (that.workAddress == '') {
				mui.toast('请输入办公地址');
				return;
			} else if (that.bankName) {
				mui.toast('请输入联系人姓名');
				return;
			} else if (that.weChat == '') {
				mui.toast('请输入联系人微信号');
				return;
			} else if (that.qq == '') {
				mui.toast('请输入联系人QQ');
				return;
			} else if (that.email == '') {
				mui.toast('请输入企业邮箱');
				return;
			} else if (that.workImg == '') {
				mui.toast('请上传证件照片');
				return;
			}

			//开始提交
			ajax_json(
				"ali-legal/api/user/companyAuthentication",
				{
					userId: userId,
					realName: that.realName,
					companyName: that.companyName,
					companyCode: that.companyCode,
					registerAddress: that.registerAddress,
					workAddress: that.workAddress,
					email: that.email,
					qq: that.qq,
					weChat: that.weChat,
					workImg: that.workImg
				},
				function (res) {
					if (res.code === 1) {
						mui.toast('已提交审核，请等待结果');
						creatMywebview('Myinfo.html', 'Myinfo.html');

					} else {
						//用户名已存在
						//验证码错误

					}
				}
			)		

			
		}
	}
	
})

