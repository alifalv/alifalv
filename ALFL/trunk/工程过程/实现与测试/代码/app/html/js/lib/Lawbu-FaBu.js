var tempAreaList = {}; //省市列表缓存

new Vue({
    el: '#app',
    data: {
        disabled: false,
        companyName: '',
        caseTitle: '',
        caseDesc: '',
        caseHope: '',
        caseAsk: '',
        mobile: '',
        offerMoney: '',
        workImg1: '',
        workImg2: '',
        workImg3: '',
        caseImgs: '',
        caseImg1: '',
        caseImg2: '',
        caseImg3: '',

        addrShow: false, //地址显示状态
        addrLoading: true, //异步加载地址时loading
        addrText: '未选择', //所选地址展示
        province: null, //选中省
        provinceId: '',
        city: null, //选中市
        cityId: '',
        provinceList: [], //省列表
        cityList: [], //市列表
        areaList: [ //地址展示列表
            { values: [], className: 'column1', defaultIndex: 1 },
            { values: [], className: 'column2', defaultIndex: 1 }
        ],

        caseList: [],
        caseShow: false,
        majorText: '未选择', //专业
        caseId: '',

        upLoad: '点击上传' //点击上传
    },
    mounted: function() {
        var that = this,
            flag = true;
		var loginUserInfo = utils.getLoginUserInfo(true);
// 		if(!loginUserInfo){
// 			location.replace('Login.html');
// 			return
// 		}
        //如果地址5s未加载成功则关闭地址loading
        setTimeout(function() {
            that.addrLoading = false;
        }, 5000);

        //获取省份
        rest.post({
            url: 'address/provinceList',
            success: function(res) {
                that.provinceList = res.data;
                //获取城市
                for (var i in that.provinceList) {
                    (function(e) {
                        var _id = that.provinceList[i].provinceId, //获取ID作为请求参数
                            _name = that.provinceList[i].provinceName; //单个省
                        // 展示省
                        that.areaList[0].values.push({
                            text: that.provinceList[i].provinceName,
                            value: that.provinceList[i].provinceId
                        });
                        // that.areaList[0].values.push(that.provinceList[i].provinceName);
                        rest.post({
                            url: 'address/cityListByProvince/{id}',
                            urlParams: {
                                id: _id
                            },
                            success: function(res2) {
                                that.cityList = []; //单个省的市数组
                                for (var j in res2.data) {

                                    that.cityList.push({
                                        text: res2.data[j].cityName,
                                        value: res2.data[j].cityId
                                    });
                                }
                                tempAreaList[_name] = that.cityList; //存储单组省市
                                if (j == res2.data.length - 1) { //异步加载flag
                                    that.addrLoading = false;

                                    for (var j in tempAreaList['上海市']) { //展示市
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
            success: function(res) {
                for (var i in res.data) {
                    that.caseList.push({
                        text: res.data[i].caseName,
                        caseId: res.data[i].caseId
                    });
                }
                // that.caseList = res.data;
            }
        });


    },

    methods: {
        casePop: function() {
            var that = this;
            that.caseShow = !that.caseShow;
        },

        //发送验证码
        sendCode: function() {
            var that = this,
                time = 60;
            if (!(/^1[3-9]\d{9}$/.test(that.mobile))) {
                mui.toast("请输入正确的手机号");
                return;
            }

        },

        //地址弹窗隐藏与显示
        addrPop: function() {
            var that = this;
            that.addrShow = !that.addrShow;
            // $(".column1 ul li:first").attr("class", "van-ellipsis van-picker-column__item van-picker-column__item--selected");
            // $(".column1 ul").attr("style", "transition: all 200ms ease 0s; transform: translate3d(0px, 88px, 0px); line-height: 44px;");

        },

        //改变地址选择
        addrChange: function(picker, values) {
            picker.setColumnValues(1, tempAreaList[values[0].text]);
        },

        //确认地址选择
        addrConfirm: function(value) {
            var that = this;
            that.addrShow = !that.addrShow;

            if (value[0] == undefined || value[1] == undefined) { //未改变地址直接确认
                that.province = that.areaList[0].values[0].text;
                that.city = that.areaList[1].values[0].text;
                that.addrText = that.province + ' ' + that.city;

                that.provinceId = that.areaList[0].values[0].value;
                that.cityId = that.areaList[1].values[0].value;

            } else if (value[0].text == value[1].text) { //省市相同就仅展示市
                that.addrText = value[1].text;
                that.provinceId = value[0].value;
                that.cityId = value[1].value;

            } else { //展示省市
                that.province = value[0].text;
                that.city = value[1].text;

                that.provinceId = value[0].value;
                that.cityId = value[1].value;
                that.addrText = value[0].text + ' ' + value[1].text;
            }
        },

        caseConfirm: function(value, index) {
            var that = this;
            that.caseShow = !that.caseShow;
            that.majorText = value.text;
            that.caseId = value.caseId;
        },

        // 上传照片
        onRead: function(file) {
            var that = this;
            that.uploadImg(1, file);
        },
        onRead1: function(file) {
            var that = this;
            that.uploadImg(2, file);
        },

        onRead2: function(file) {
            var that = this;
            that.uploadImg(3, file);
        },
        uploadImg: function(type, file) {
            var that = this;
            that.ImgBase64Str = file.content;

            rest.post({
                url: 'file/uploadImg',
                data: { imgBase64Str: that.ImgBase64Str },
                success: function(result) {
                    if (type == 1) {
                        that.workImg1 = utils.constants.apiUrl + result.data;
                        if (that.caseImgs.length > 0) {
                            that.caseImg1 = ',' + result.data;
                        } else {
                            that.caseImg1 = result.data;
                        }
                    } else if (type == 2) {
                        that.workImg2 = utils.constants.apiUrl + result.data;
                        if (that.caseImgs.length > 0) {
                            that.caseImg2 = ',' + result.data;
                        } else {
                            that.caseImg2 = result.data;
                        }
                    } else if (type == 3) {
                        that.workImg3 = utils.constants.apiUrl + result.data;
                        if (that.caseImgs.length > 0) {
                            that.caseImg3 = ',' + result.data;
                        } else {
                            that.caseImg3 = result.data;
                        }
                    }
                    that.caseImgs = result.data;
                },
                fail: function(result) {
                    mui.toast(result.msg);
                }
            });
        },



        // 注册
        nextStep: function() {
            var loginUserInfo = utils.getLoginUserInfo();
            var that = this;
            //creatMywebview('Register-askman-2.html','Register-askman-2.html')
            // 基础验证
            that.caseImgs = that.caseImg1 + that.caseImg2 + that.caseImg3;

            if (that.caseTitle == null || that.caseTitle.length < 5 || that.caseTitle.length > 30) {
                mui.toast('请输入5-30字符的案情标题');
                return;
            } else if (that.caseDesc == null) {
                mui.toast('请输入案件详情');
                return;
            } else if (that.provinceId.length < 1 || that.cityId.length < 1) {
                mui.toast('请选择省市');
                return;
            } else if (that.caseId.length < 1) {
                mui.toast('请选择案情类型');
                return;
            } else if (that.offerMoney.length < 1) {
                mui.toast('请输入报价');
                return;
            } else if (!(/^1[34578]\d{9}$/.test(that.mobile))) {
                mui.toast("请输入正确的手机号");
                return;
            }

            //服务器注册和验证
            rest.post({
                // url: 'user/counselorRegister',
                url: 'case/sendCase',
                data: {
                    caseTitle: that.caseTitle,
                    caseDesc: that.caseDesc,
                    caseHope: that.caseHope,
                    caseAsk: that.caseAsk,
                    province: that.provinceId,
                    city: that.cityId,
                    mobile: that.mobile,
                    caseType: that.caseId,
                    caseImgs: that.caseImgs,
                    offerType: 1,
                    offerMoney: that.offerMoney,
                    resource: 1,
                    userId: loginUserInfo.userId,
                    isofficial: 1
                },
                success: function(res) {
                    mui.toast(res.msg);
                    window.location.href = "Lawbuy.html";
                }
            });
        }
    }

});