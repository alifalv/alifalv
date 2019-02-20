var tempAreaList = {}; //省市列表缓存
var userId = storage.getItem('userId'),
    userToken = storage.getItem('token');

new Vue({
    el: '#app',
    data: {
        // 个人咨询师认证
        realName: '', //真实姓名
        bankAccount: '', //银行卡号
        bankName: '', //开户行
        bankAccountName: '', //银行姓名
        workNum: '',

        addrText: '', //所选地址展示
        addrShow: false, //地址显示状态
        addrLoading: true, //异步加载地址时loading
        province: '', //省
        city: '', //市

        provinceList: [], //省列表
        cityList: [], //市列表
        areaList: [ //地址展示列表
            { values: [], className: 'column1', defaultIndex: 0 },
            { values: [], className: 'column2', defaultIndex: 0 }
        ],

        jobText: '', //所选职业展示
        jobShow: false, //职业显示状态
        jobList: [], //职业列表

        caseText: '', //案件类型
        caseShow: false, //案件类型显示状态
        caseList: [], //案件类型列表

        companyName: '',
        bankAccountName: '',
        registerAddress: '',
        workAddress: '',
        weChat: '',
        qq: '',
        email: '',

        workImg: '',
        personImg: '',
        ImgBase64Str: '', //临时存放上传图片

        btn: '提交审核'

    },
    mounted: function() {
        var that = this,
            flag = true;

        //如果地址5s未加载成功则关闭地址loading
        setTimeout(function() {
            that.addrLoading = false;
        }, 5000);

        //获取省份
        rest.post({
            url: 'address/provinceList',
            success: function(res) {
                if (res.code === 1) {
                    that.provinceList = res.data;
                } else {
                    mui.toast('请求地址数据失败，请检查网络');
                }
                //获取城市
                for (var i in that.provinceList) {
                    (function(i) {
                        var _id = that.provinceList[i].provinceId, //获取ID作为请求参数
                            _name = that.provinceList[i].provinceName; //单个省

                        // 展示省
                        that.areaList[0].values.push(that.provinceList[i].provinceName);


                        rest.post({
                            url: 'address/cityListByProvince/{provinceId}',
                            urlParams: {
                                provinceId: _id
                            },
                            success: function(result) {
                                for (var j in result.data) {
                                    that.cityList.push(result.data[j].cityName);
                                }
                                tempAreaList[_name] = that.cityList; //存储单组省市


                                if (j == result.data.length - 1) { //异步加载flag
                                    that.addrLoading = false;

                                    for (var j in tempAreaList['北京市']) { //展示市
                                        if (flag) {
                                            that.areaList[1].values.push(tempAreaList['北京市'][j]);
                                            flag = false;
                                        }
                                    }
                                }
                            },
                            fail: function(result) {
                                mui.toast(result.msg);
                            }
                        });
                    })(i)
                }
            },
            fail: function(result) {
                mui.toast(result.msg);
            }
        });


        //获取职业列表
        rest.post({
            url: 'systemConfig/jobList',
            success: function(result) {
                for (var i in result.data) {
                    that.jobList.push(result.data[i].jobName);
                }
            },
            fail: function(result) {
                mui.toast(result.msg);
            }
        });

        //获取案件类型列表
        rest.post({
            url: 'systemConfig/caseList',
            success: function(result) {
                for (var i in result.data) {
                    that.caseList.push(result.data[i].caseName);
                }
            },
            fail: function(result) {
                mui.toast(result.msg);
            }
        });
    },

    methods: {

        //地址弹窗隐藏与显示
        addrPop: function() {
            var that = this;
            that.addrShow = !that.addrShow;
        },

        //改变地址选择
        addrChange: function(picker, values) {
            picker.setColumnValues(1, tempAreaList[values[0]]);
        },

        //确认地址选择
        addrConfirm: function(value) {
            var that = this;
            that.addrShow = !that.addrShow;

            if (value[0] == undefined || value[1] == undefined) { //未改变地址直接确认
                that.province = that.areaList[0].values[0];
                that.city = that.areaList[1].values[0];
                that.addrText = that.province + ' ' + that.city;

            } else if (value[0] == value[1]) { //省市相同就仅展示市
                that.addrText = value[1];

            } else { //展示省市
                that.province = value[0];
                that.city = value[1];
                that.addrText = value[0] + ' ' + value[1];
            }

        },

        //职业弹窗隐藏与显示
        jobPop: function() {
            this.jobShow = !this.jobShow;
        },

        //确认职业选择
        jobConfirm: function(value) {
            var that = this;
            that.jobShow = !that.jobShow;

            that.jobText = value;
        },

        //案件类型弹窗隐藏与显示
        casePop: function() {
            this.caseShow = !this.caseShow;
        },

        //确认案件类型选择
        caseConfirm: function(value) {
            var that = this;
            that.caseShow = !that.caseShow;
            that.caseText = value;
        },

        // 上传职业证件照片
        onRead1: function(file) {
            var that = this;
            that.ImgBase64Str = file.content;

            rest.post({
                url: 'file/uploadImg',
                data: { imgBase64Str: that.ImgBase64Str },
                success: function(result) {
                    that.workImg = result.data;
                },
                fail: function(result) {
                    mui.toast(result.msg);
                }
            });
        },

        // 上传形象照片
        onRead2: function(file) {
            var that = this;
            that.ImgBase64Str = file.content;

            rest.post({
                url: 'file/uploadImg',
                data: { imgBase64Str: that.ImgBase64Str },
                success: function(result) {
                    that.personImg = res.data;
                },
                fail: function(result) {
                    mui.toast(result.msg);
                }
            });
        },


        // 提交审核
        saveFn: function() {
            var that = this;

            // 基础验证
            if (that.realName == '') {
                mui.toast("请输入手机号");
                return;
            } else if (that.addrText == '') {
                mui.toast('请输入用户名');
                return;
            } else if (that.jobText == '') {
                mui.toast('请输入用户名');
                return;
            } else if (that.caseText == '') {
                mui.toast('请输入用户名');
                return;
            } else if (that.bankAccount == '' || that.bankAccount.length < 16 || that.bankAccount.length > 19) {
                mui.toast('请输入正确的银行卡号');
                return;
            } else if (that.bankName == '') {
                mui.toast('请输入开户银行');
                return;
            } else if (that.bankAccountName == '') {
                mui.toast('请输入开户人姓名');
                return;
            } else if (that.workNum == '') {
                mui.toast('请输入执业证号或实习证号');
                return;
            } else if (that.workImg == '') {
                mui.toast('请上传职业证件照片');
                return;
            } else if (that.personImg == '') {
                mui.toast('请上传形象照片');
                return;
            } else if (that.companyName == '') {
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
            }

            rest.post({
                url: 'user/counselorAuthentication',
                data: {
                    userId: userId,
                    realName: that.realName,
                    province: that.province,
                    city: that.city,
                    job: that.job,
                    cases: that.cases,
                    bankAccount: that.bankAccount,
                    bankName: that.bankName,
                    bankAccountName: that.bankAccountName,
                    companyName: that.companyName,
                    registerAddress: that.registerAddress,
                    workAddress: that.workAddress,
                    email: that.email,
                    qq: that.qq,
                    weChat: that.weChat,
                    personImg: that.personImg,
                    workImg: that.workImg
                },
                success: function(result) {
                    mui.toast('已提交审核，请等待结果');
                    creatMywebview('Myinfo.html', 'Myinfo.html');
                },
                fail: function(result) {
                    mui.toast(result.msg);

                }
            });

        }
    }

});