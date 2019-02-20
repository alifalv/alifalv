new Vue({
	el:'#app',
	data:{
		// 竞拍的案件
        list1List: [],//已过期
        list1IsNot : false,
        list1PageNo:1,
        list1PageSize:5,
        list1ListId : '#list1',

        // 委托的案件
        list2List: [],//已过期
        list2IsNot : false,
        list2PageNo:1,
        list2PageSize:5,
        list2ListId : '#list2'
	},
	mounted : function(){
		var that = this;

        mui.init();

        //竞拍的案件
        mui(that.list1ListId).pullRefresh({
            down: {
                style: 'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                color: '#2BD009',
                // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                indicators: true, //是否显示滚动条
                callback: that.list1DownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
            },
            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.list1UpRefresh
            }
        });

        //委托的案件
        mui(that.list2ListId).pullRefresh({
            down: {
                style: 'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                color: '#2BD009',
                // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                indicators: true, //是否显示滚动条
                callback: that.list2DownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
            },
            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.list2UpRefresh
            }
        });

        mui("#list1").on('tap', 'li', function(){
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });

        mui("#list2").on('tap', 'li', function(){
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });

        mui("#list2").on('tap',".deletebtn",function(){
            var caseId = $(this).data('caseid');
            mui.confirm('确定删除?', '提示', ['取消', '确定'], function(e) {
                if(e.index == 1) {
                    rest.post({
                        url: 'case/deleteCase/{caseId}',
                        urlParams : {
                            caseId: caseId
                        },
                        success: function(result) {
                            mui.toast(result.msg);

                            if(result.data.ok) {
                                mui.toast(result.msg);
                                that.list2UpRefresh();
                            }
                        },
                        fail : function (result) {
                            mui.toast(result.msg);

                        }
                    });
                }
            });
            return false;
        });
	},

	methods:{
        list1DownRefresh: function () {
            var _that = this;
            setTimeout(function () {
                _that.list1List = [];
                _that.list1PageNo = 1;
                _that.tolist1List(_that.list1DownSuccess);//_that.list1List = [];
                _that.list1PageNo = 1;
            }, 500);
        },

        list1UpRefresh: function () {
            var _that = this;
            setTimeout(function () {
                _that.tolist1List(_that.list1UpSuccess);//具体取数据的方法
            }, 500);
        },

        list1UpSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.list1ListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function () {
                    mui(_that.list1ListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.list1ListId).pullRefresh().endPullupToRefresh(false);
                _that.list1List = _that.list1List.concat(result.rows);
                _that.list1PageNo++;
            }
        },

        list1DownSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.isNolist1List = true;
                mui(_that.list1ListId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.list1ListId).pullRefresh().endPulldownToRefresh(false);
                _that.list1List = result.rows;
                _that.list1PageNo++;
            }
        },

        tolist1List: function (func) {
            var loginUserInfo = utils.getLoginUserInfo(true);
            var _that = this;
            rest.post({
                url: 'case/myOfferCase/{userId}/{pageNo}/{pageSize}',//TODO
                urlParams: {
                    userId : loginUserInfo.userId,
                    pageNo: _that.list1PageNo,
                    pageSize: _that.list1PageSize
                },

                success: function (result) {
                    func(result)
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
        },


        list2DownRefresh: function () {
            var _that = this;
            setTimeout(function () {
                _that.list2List = [];
                _that.list2PageNo = 1;
                _that.tolist2List(_that.list2DownSuccess);//_that.list2List = [];
                _that.list2PageNo = 1;//具体取数据的方法
            }, 500);
        },

        list2UpRefresh: function () {
            var _that = this;
            setTimeout(function () {
                _that.tolist2List(_that.list2UpSuccess);//具体取数据的方法
            }, 500);
        },

        list2UpSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.list2ListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function () {
                    mui(_that.list2ListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.list2ListId).pullRefresh().endPullupToRefresh(false);
                _that.list2List = _that.list2List.concat(result.rows);
                _that.list2PageNo++;
            }
        },

        list2DownSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.isNolist2List = true;
                mui(_that.list2ListId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.list2ListId).pullRefresh().endPulldownToRefresh(false);
                _that.list2List = result.rows;
                _that.list2PageNo++;
            }
        },

        tolist2List: function (func) {
            var loginUserInfo = utils.getLoginUserInfo(true);
            var _that = this;
            rest.post({
                url: 'case/myCaseList/{userId}/{pageNo}/{pageSize}',//TODO
                urlParams: {
                    userId : loginUserInfo.userId,
                    pageNo: _that.list2PageNo,
                    pageSize: _that.list2PageSize
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
	
})
