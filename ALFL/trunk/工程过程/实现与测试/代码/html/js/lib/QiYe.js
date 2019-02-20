var vm = new Vue({
    el:'#app',
    data:{
        companyUserId : '',
        companyId:'',
        articleData: {},
        loginUserId :-1,
        isShowStar: true,
        collectionId : '',

        pullId : '#pullrefresh',
        isNoList : false,
        list:[],
        pageNo:1,
        pageSize:5
    },
    mounted :function(){
        var _this = this;
        var loginUserInfo = utils.getLoginUserInfo();
        var urlParams = utils.getUrlParams();
        _this.companyUserId =  urlParams.userId;

        rest.post({
            url: 'user/getCompanyInfo/{userId}',
            urlParams : {
                userId  : _this.companyUserId
            },
            success: function (result) {
                _this.companyId = result.data.companyId;
                _this.articleData = result.data;

                //如果是自己发布的岗位就无法收藏和申请
                var loginUserInfo = utils.getLoginUserInfo();
                if((loginUserInfo && loginUserInfo.userId == _this.companyUserId)) {
                    _this.isShowStar = false;

                } else {
                    _this.isShowStar = true;
                }
            },
            fail : function (result) {
                mui.toast(result.msg);
            }
        });




        mui.init({
            pullRefresh : {
                container:_this.pullId,//下拉刷新容器标识，querySelector能定位的css选择器均可，比如：id、.class等
                down : {
                    style:'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                    color:'#2BD009',
                    // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                    indicators: true, //是否显示滚动条
                    callback :_this.pullDownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
                },
                up: {
                    auto:true,
                    indicators: true, //是否显示滚动条
                    callback: _this.pullUpRefresh
                }
            }
        });

        mui("#pullrefresh").on('tap',"a",function(){
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });


        mui("#pullrefresh").on('tap',".j-starRow",function(){
            var advertiseId = $(this).data('advertiseid');
            var collectionId = $(this).data('collectionid');
            _this.starRow(advertiseId, collectionId);
            return false;
        });


    },

    methods:{


        starRow : function (advertiseId, collectionId) {
            var _that = this;
            var loginUserInfo = utils.getLoginUserInfo(true);

            if(collectionId != '0') {
                //取消收藏
                rest.post({
                    url: 'user/deleteCollectionJobList/{collectionId}',
                    urlParams : {
                        collectionId  : collectionId
                    },
                    success: function (result) {
                        _that.pullDownRefresh();
                        mui.toast('已取消收藏');
                    },
                    fail : function (result) {
                        mui.toast(result.msg);
                    }
                });

            } else {
                //收藏
                if(advertiseId == null || advertiseId == '') {
                    mui.toast('接口查询advertiseId失败');
                    return false;
                }
                rest.post({
                    url: 'user/collectionJob',
                    data : {
                        userId : loginUserInfo.userId,
                        advertiseId  : advertiseId
                    },
                    success: function (result) {
                        _that.pullDownRefresh();
                        mui.toast(result.msg || '收藏成功');
                    },
                    fail : function (result) {
                        mui.toast(result.msg);
                    }
                });
            }
        },

        //未使用
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
        },













        tapSearch : function () {
            var _that = this;
            _that.conditionValue = _that.$refs.conditionValue.value;
            _that.pullDownRefresh();
        },

        pullDownRefresh : function() {
            var _that = this;

            setTimeout(function() {
                _that.list = [];
                _that.pageNo = 1;
                _that.toList(_that.downSuccess);//具体取数据的方法
            }, 500);
        },

        pullUpRefresh : function () {
            var _that = this;
            setTimeout(function() {
                _that.toList(_that.upSuccess);//具体取数据的方法
            }, 500);
        },

        upSuccess : function(result){
            var _that = this;
            if(null == result.rows || result.rows.length == 0) {
                mui(_that.pullId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function(){
                    mui(_that.pullId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.pullId).pullRefresh().endPullupToRefresh(false);
                _that.list = _that.list.concat(result.rows);
                _that.pageNo++;
            }
        },

        downSuccess : function(result){
            var _that = this;
            if(null == result.rows || result.rows.length == 0) {
                _that.isNoList = true;
                mui(_that.pullId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.pullId).pullRefresh().endPulldownToRefresh(false);
                _that.list = result.rows;
                _that.pageNo++;
            }
        },

        toList : function(func) {
            var _that = this;
            var loginUserInfo = utils.getLoginUserInfo();
            if(loginUserInfo) {
                _that.loginUserId = loginUserInfo.userId;
            }

            rest.post({
                pageType : 'home',
                url : 'user/newJobList/{pageNo}/{pageSize}',
                urlParams : {
                    pageNo  : _that.pageNo,
                    pageSize : _that.pageSize
                },
                data : {
                    state : 1,
                    userId: _that.companyUserId,
                    loginUserId : _that.loginUserId
                },
                success: function (result) {
                    func(result)
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
        }
    }
});

