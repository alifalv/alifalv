var vm = new Vue({
    el:'#app',
    data:{
        advertiseId : '',
        companyId:'',
        articleData: {},
        companyUserId : '',
        jobName : '',
        isShowStar: false,
        isStared : false,
        collectionId : ''
    },
    mounted :function(){
        var _this = this;
        var loginUserInfo = utils.getLoginUserInfo();
        var urlParams = utils.getUrlParams();
        _this.advertiseId =  urlParams.advertiseId;

            rest.post({
            url: 'user/seacherJobDetail',
            data : {
                advertiseId : _this.advertiseId
            },
            success: function (result) {
                _this.companyUserId = result.data.userId;
                _this.companyId = result.data.companyId;
                _this.jobName = result.data.jobName;
                _this.articleData = result.data;
                _this.getCompanyInfo();
                //如果是自己发布的岗位就无法收藏和申请
                var loginUserInfo = utils.getLoginUserInfo();
                if((loginUserInfo && loginUserInfo.userId == _this.companyUserId) || (loginUserInfo && loginUserInfo.userType == '3')) {
                    _this.isShowStar = false;

                } else {
                    _this.isShowStar = true;
                    _this.seacherisCollectionResume()
                }
            },
            fail : function (result) {
                mui.toast(result.msg);
            }
        });
    },

    methods:{


        getCompanyInfo : function () {
            var _that = this;

            rest.post({
                url: 'user/getCompanyInfo/{userId}',
                urlParams : {
                    userId  : _that.companyUserId
                },
                success: function (result) {
                    _that.articleData = $.extend({},_that.articleData,result.data);
                },
                fail : function (result) {
                    mui.toast(result.msg);
                }
            });
        },

        seacherisCollectionResume : function(func) {
            var _that = this;
            var _loginUserInfo = utils.getLoginUserInfo();
            var userId= '-1';
            if(_loginUserInfo) {
                userId = _loginUserInfo.userId;
            }
            rest.post({
                url: 'user/seacherisCollectionJob',
                data : {
                    userId :userId, //用户编号  （如果是是游客你自己生成一个userid）不能不传，
                    advertiseId : _that.advertiseId
                },
                success: function (result) {
                    if(result.data && result.data.collectionId != '0') {
                        _that.isStared  = true;
                        _that.collectionId = result.data.collectionId;
                    } else {
                        _that.isStared  = false;
                    }
                    func && func();
                }
            });
        },

        /**
         * 88.	用户投递简历；9.19修改
         * @param userId
         * @param advertiseId
         * @param companyId
         * @param resumeId
         */
         deliverResume : function(userId, advertiseId, companyId, resumeId) {
            rest.post({
                url: 'user/deliverResume',//用户投递简历
                data: {
                    userId: userId,
                    advertiseId: advertiseId,
                    companyId : companyId,
                    resumeId : resumeId
                },
                success: function (result) {
                    mui.toast(result.msg);

                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });

        },
        sendJob: function() {
            var _that = this;
            var loginUserInfo = utils.getLoginUserInfo(true);
            if(null == _that.companyId) {
                mui.toast('接口未查询到该岗位所属公司编号');
                return false;
            }

            var btnArray = ['否', '是'];
            mui.confirm('确认申请该职位？', '', btnArray, function(e) {
                if (e.index == 1) {
                    rest.post({
                        url: 'user/seacherisResumeJob',//是否投递过简历
                        data: {
                            userId: loginUserInfo.userId,
                            advertiseId : _that.advertiseId
                        },
                        success: function (result) {
                            if(result.data && result.data.id == '0') {//0未投递
                                rest.post({
                                    url: 'user/getResumeInfo/{userId}',//用户投递简历
                                    urlParams: {
                                        userId: loginUserInfo.userId
                                    },
                                    success: function (result) {
                                        if(result.data && result.data.resumeId) {
                                            _that.deliverResume(loginUserInfo.userId, _that.advertiseId, _that.companyId, result.data.resumeId);
                                        } else {
                                            mui.toast('请先添加个人简历');
                                        }
                                    },
                                    fail: function (result) {
                                        mui.toast(result.msg);
                                    }
                                });
                            } else {
                                mui.toast('您已经投递过简历了');
                            }
                        },
                        fail: function (result) {
                            mui.toast(result.msg);
                        }
                    });
                }
            })
        },

        star : function () {
            var _that = this;
            var _$this = $(this);
            var loginUserInfo = utils.getLoginUserInfo(true);

            if(_that.isStared != true) {///Id=0 为没收藏
                rest.post({
                    url: 'user/collectionJob',
                    data : {
                        userId : loginUserInfo.userId,
                        advertiseId  : _that.advertiseId
                    },
                    success: function (result) {
                        _that.isStared = true;
                        _that.collectionId = result.data.collectionId;
                        mui.toast(result.msg || '收藏成功');

                    },
                    fail : function (result) {
                        mui.toast(result.msg);
                    }
                });
            } else {
                rest.post({
                    url: 'user/deleteCollectionJobList/{collectionId}',
                    urlParams : {
                        collectionId  : _that.collectionId
                    },
                    success: function (result) {
                        _that.isStared = false;
                        _that.collectionId = 0;
                        mui.toast(result.msg || '已取消收藏');
                    },
                    fail : function (result) {
                        mui.toast(result.msg);
                    }
                });
            }
        },

        juBao : function () {
            var _that = this;
            var loginUserInfo = utils.getLoginUserInfo(true);

            var btnArray = ['否', '是'];
            mui.confirm('确认举报该职位？', '', btnArray, function(e) {
                if (e.index == 1) {
                    rest.post({
                        url: 'user/complainJob',
                        data: {
                            reportTitle : _that.jobName,
                            advertiseId: _that.advertiseId,
                            userId : loginUserInfo.userId,
                            userName : loginUserInfo.userName,
                            mobile : loginUserInfo.mobile
                        },
                        success: function (result) {
                            mui.toast(result.msg);
                        },
                        fail: function (result) {
                            mui.toast(result.msg);
                        }
                    });
                }
            })
        }
    }
});

