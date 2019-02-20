//我的招聘
var vm = new Vue({
    el: '#app',
    data: {
        collecResumeList: [],//最新资讯
        collecResumeListIsNot : false,
        collecResumeListPageNo:1,
        collecResumeListPageSize:5,
        collecResumeListId : '#collecResumeList',

        collectionResumeList: [],//最新案源
        collectionResumeListIsNot : false,
        collectionResumeListPageNo:1,
        collectionResumeListPageSize:5,
        collectionResumeListId : '#collectionResumeList',

        userType: ''
    },
    mounted: function () {

        var that = this;

        mui.init();
        mui(that.collecResumeListId).pullRefresh({
            down: {
                style: 'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                color: '#2BD009',
                // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                indicators: true, //是否显示滚动条
                callback: that.collecResumeListDownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
            },
            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.collecResumeListUpRefresh
            }
        });

        mui(that.collectionResumeListId).pullRefresh({
            down: {
                style: 'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                color: '#2BD009',
                // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                indicators: true, //是否显示滚动条
                callback: that.collectionResumeListDownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
            },
            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.collectionResumeListUpRefresh
            }
        });


        mui("#collecResumeList").on('tap',".deletebtn",function(){
            var collectionId = $(this).data('collectionid');
            mui.confirm('确定删除?', '提示', ['取消', '确定'], function(e) {
                if(e.index == 1) {
                    //130.企业删除收到的简历
                    rest.post({
                        url: 'user/companyDelSendAdvertise',
                        data : {
                            Id : collectionId
                        },
                        success: function(result) {
                            mui.toast(result.msg);
                            that.collecResumeListDownRefresh();
                        },
                        fail : function (result) {
                            mui.toast(result.msg);

                        }
                    });
                }
            });
            return false;
        });


        mui("#collectionResumeList").on('tap',".deletebtn",function(){
            var collectionId = $(this).data('collectionid');
            mui.confirm('确定删除?', '提示', ['取消', '确定'], function(e) {
                if(e.index == 1) {
                    //129.用户删除收藏的岗位
                    rest.post({
                        url: 'user/deleteCollectionResumeList',
                        data : {
                            collectionId: collectionId
                        },
                        success: function(result) {
                            mui.toast(result.msg);
                            that.collectionResumeListDownRefresh();
                        },
                        fail : function (result) {
                            mui.toast(result.msg);
                        }
                    });
                }
            });
            return false;
        });

        mui('#newAsk, #newCase').on('tap', 'a',function(){
            var href = $(this).attr('href');
            creatMywebview(href, href);
            return false;
        });
    },

    methods: {
        setErrorImg : function(e) {
            $(e.target).attr('src', 'images/index/idx-defaluthead.png');
        },
        collecResumeListDownRefresh: function () {
            var _that = this;

            setTimeout(function () {
                _that.collecResumeListList = [];
                _that.collecResumeListPageNo = 1;
                _that.toCollecResumeList(_that.collecResumeListDownSuccess);//具体取数据的方法
            }, 500);
        },

        collecResumeListUpRefresh: function () {
            var _that = this;
            setTimeout(function () {
                _that.toCollecResumeList(_that.collecResumeListUpSuccess);//具体取数据的方法
            }, 500);
        },

        collecResumeListUpSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.collecResumeListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function () {
                    mui(_that.collecResumeListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.collecResumeListId).pullRefresh().endPullupToRefresh(false);
                _that.collecResumeList = _that.collecResumeList.concat(result.rows);
                _that.collecResumeListPageNo++;
            }
        },

        collecResumeListDownSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.collecResumeListIsNot = true;
                mui(_that.collecResumeListId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.collecResumeListId).pullRefresh().endPulldownToRefresh(false);
                _that.collecResumeList = result.rows;
                _that.collecResumeListPageNo++;
            }
        },

        toCollecResumeList: function (func) {
            var loginUserInfo = utils.getLoginUserInfo(true);

            var _that = this;
            rest.post({
                url: 'user/collecResumeList/{userId}/{pageNo}/{pageSize}',
                urlParams: {
                    userId :loginUserInfo.userId,
                    pageNo: _that.collecResumeListPageNo,
                    pageSize: _that.collecResumeListPageSize
                },
                data: {
                    conditionValue: _that.conditionValue
                },
                success: function (result) {
                    func(result)
                },
                fail: function (result) {
                }
            });
        },

        collectionResumeListDownRefresh: function () {
            var _that = this;

            setTimeout(function () {
                _that.collectionResumeList = [];
                _that.collectionResumeListPageNo = 1;
                _that.toCollectionResumeList(_that.collectionResumeListDownSuccess);//具体取数据的方法
            }, 500);
        },

        collectionResumeListUpRefresh: function () {
            var _that = this;
            setTimeout(function () {
                _that.toCollectionResumeList(_that.collectionResumeListUpSuccess);//具体取数据的方法
            }, 500);
        },

        collectionResumeListUpSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.collectionResumeListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function () {
                    mui(_that.collectionResumeListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.collectionResumeListId).pullRefresh().endPullupToRefresh(false);
                _that.collectionResumeList = _that.collectionResumeList.concat(result.rows);
                _that.collectionResumeListPageNo++;
            }
        },

        collectionResumeListDownSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.collectionResumeListIsNot = true;
                mui(_that.collectionResumeListId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.collectionResumeListId).pullRefresh().endPulldownToRefresh(false);
                _that.collectionResumeList = result.rows;
                _that.collectionResumeListPageNo++;
            }
        },

        toCollectionResumeList: function (func) {
            var loginUserInfo = utils.getLoginUserInfo(true);

            var _that = this;
            rest.post({
                url: 'user/collectionResumeList/{userId}/{pageNo}/{pageSize}',
                urlParams: {
                    userId : loginUserInfo.userId,
                    pageNo: _that.collectionResumeListPageNo,
                    pageSize: _that.collectionResumeListPageSize
                },
                data: {
                    conditionValue: _that.conditionValue
                },
                success: function (result) {
                    func(result)
                },
                fail: function (result) {
                }
            });
        }
    }

});



