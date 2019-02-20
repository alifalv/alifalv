var tempAreaList = {};//省市列表缓存

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
        nickName:null,//名字
		equipmentType:1,//设备类型
		checked:false,//是否同意服务协议

		addrShow: false,//地址显示状态
		addrLoading: true,//异步加载地址时loading
		addrText: '未选择',//所选地址展示
		province:null,//选中省
        provinceId : '',
		city:null,//选中市
        cityId : '',
        provinceList:[],//省列表
		cityList:[],//市列表
		areaList: [ //地址展示列表
			{ values: [], className: 'column1', defaultIndex: 0},
			{ values: [], className: 'column2', defaultIndex: 0}
		],

		jobText: '未选择',//所选职业展示
        jobId : '',
		jobShow:false,//职业显示状态
		jobList:[],//职业列表

		caseList : [],
        caseShow:false,
		majorText:'未选择',//专业
        caseId : '',

		upLoad: '点击上传'//点击上传
	},
	mounted : function(){
		var that = this, flag = true;

		//如果地址5s未加载成功则关闭地址loading
		setTimeout(function () {
			that.addrLoading = false;
		}, 5000);

		//获取省份
        rest.post({
            url: 'address/provinceList',
            success: function (res) {
                that.provinceList = res.data;

				//获取城市
				for (var i in that.provinceList) {
					(function (e) {
						var _id = that.provinceList[i].provinceId,//获取ID作为请求参数
							_name = that.provinceList[i].provinceName;//单个省

						// 展示省

                        that.areaList[0].values.push({
                            text :that.provinceList[i].provinceName,
                            value :that.provinceList[i].provinceId
                        });

                        // that.areaList[0].values.push(that.provinceList[i].provinceName);


                        rest.post({
                            url: 'address/cityListByProvince/{id}',
                            urlParams : {
                                id: _id
                            },
                            success: function (res2) {
                                that.cityList = [];//单个省的市数组
                                for (var j in res2.data) {

                                    that.cityList.push({
                                        text :res2.data[j].cityName,
                                        value :res2.data[j].cityId
                                    });
                                }
                                tempAreaList[_name] = that.cityList;//存储单组省市


                                if (j == res2.data.length-1 ){//异步加载flag
                                    that.addrLoading = false;

                                    for (var j in tempAreaList['北京市']) {//展示市
                                        if (flag) {
                                            that.areaList[1].values.push(tempAreaList['北京市'][j]);
                                            flag = false;
                                        }
                                    }
                                }
                            }
                        });
					})(i)
				}
				
			}
        });

        rest.post({
            url: 'systemConfig/caseList',
            success: function (res) {
                for (var i in res.data) {
                    that.caseList.push({
                        text :res.data[i].caseName,
						caseId :res.data[i].caseId
					});
                }

                // that.caseList = res.data;
            }
        });

		//获取职业列表
        rest.post({
            url: 'systemConfig/jobList',
            success: function (res) {
                for (var i in res.data) {
                    that.jobList.push({
                        text :res.data[i].jobName,
                        value :res.data[i].jobId
                    });
                }
            }
        });
	},

	methods:{
        casePop : function () {
            var that = this;
            that.caseShow = !that.caseShow;
        },

        //发送验证码
        sendCode:function () {
            var that = this,time = 60;


            if(!(/^1[3-9]\d{9}$/.test(that.mobile))) {
                mui.toast("请输入正确的手机号");
                return;
            }

            rest.post({
                url: 'sms/sendReg/{mobile}/2',
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

		//地址弹窗隐藏与显示
		addrPop:function () {
			var that = this;
			that.addrShow = !that.addrShow;
		},

		//改变地址选择
		addrChange: function (picker,values) {
			picker.setColumnValues(1, tempAreaList[values[0].text]);
		},

		//确认地址选择
		addrConfirm: function (value) {
			var that = this;
			that.addrShow = !that.addrShow;
			
			if (value[0] == undefined || value[1] == undefined){//未改变地址直接确认
				that.province = that.areaList[0].values[0].text;
				that.city = that.areaList[1].values[0].text;
				that.addrText = that.province + ' ' + that.city;

                that.provinceId = that.areaList[0].values[0].value;
                that.cityId = that.areaList[1].values[0].value;

			} else if (value[0].text == value[1].text){//省市相同就仅展示市
				that.addrText = value[1].text;
                that.provinceId = value[0].value;
                that.cityId = value[1].value;

			}else{//展示省市
				that.province = value[0].text;
				that.city = value[1].text;

                that.provinceId = value[0].value;
                that.cityId = value[1].value;
				that.addrText = value[0].text + ' ' + value[1].text;
			}
        },

		//职业弹窗隐藏与显示
		jobPop:function () {
			this.jobShow = !this.jobShow;
		},

        caseConfirm : function (value, index) {
            var that = this;
            that.caseShow = !that.caseShow;
            that.majorText = value.text;
            that.caseId = value.caseId;
        },

		//确认职业选择
		jobConfirm: function (value) {
			var that = this;
			that.jobShow = !that.jobShow;
			that.jobText = value.text;
            that.jobId = value.value;

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
            } else if (that.nickName == null || that.nickName.length < 6 || that.nickName.length > 16) {
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
			} else if (that.code == null) {
				mui.toast('请输入验证码');
				return;
			} else if (that.addrText == that.jobText == that.majorText) {
				mui.toast('请完善个人信息');
				return;
			}

			//服务器注册和验证
            rest.post({
                url: 'user/counselorRegister',
                data: {
                    userName: that.userName,
                    mobile: that.mobile,
                    nickName : that.nickName,
                    code : that.code,
                    password: that.password,
                    province : that.provinceId,
					city : that.cityId,
                    job : that.jobId,
                    cases : that.caseId,
                    equipmentType: that.equipmentType
                },
                success: function (res) {
					mui.toast(res.msg);
					creatMywebview('Register-ansman-sucess.html', 'Register-ansman-sucess.html');
                }
            });
		}
	}
	
});

