var userId = storage.getItem('userId'),
	userToken = storage.getItem('token');
var vm = new Vue({
	el:'#app',
	data:{
		userInfo:{},

        list1List: [],//未使用
        list1IsNot : false,
        list1PageNo:1,
        list1PageSize:5,
        list1ListId : '#list1',

        list2List: [],//已使用
        list2IsNot : false,
        list2PageNo:1,
        list2PageSize:5,
        list2ListId : '#list2',

        list3List: [],//已过期
        list3IsNot : false,
        list3PageNo:1,
        list3PageSize:5,
        list3ListId : '#list3',

        notused_count : 0,
        used_count :0,
        expired_count : 0
	},
	mounted :function(){
		
		var
		that = this;

        mui.init();
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

        mui(that.list3ListId).pullRefresh({
            down: {
                style: 'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                color: '#2BD009',
                // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                indicators: true, //是否显示滚动条
                callback: that.list3DownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
            },
            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.list3UpRefresh
            }
        });

        function initCount() {
            var loginUserInfo = utils.getLoginUserInfo();
            rest.post({
                url: 'user/voucherCount',//147.免费文书制作申请;10.8
                data : {
                    state : 0,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                    userId: loginUserInfo.userId
                },
                success: function (result) {
                    that.notused_count = result.data.voucherCount;
                }
            });
            rest.post({
                url: 'user/voucherCount',//147.免费文书制作申请;10.8
                data : {
                    state : 3,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                    userId:  loginUserInfo.userId
                },
                success: function (result) {
                    that.used_count = result.data.voucherCount;
                }
            });
            rest.post({
                url: 'user/voucherCount',//147.免费文书制作申请;10.8
                data : {
                    state : 2,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                    userId:  loginUserInfo.userId
                },
                success: function (result) {
                    that.expired_count = result.data.voucherCount;
                }
            });
        }
        initCount();
	},

	methods:{
         initCount : function() {
             var loginUserInfo = utils.getLoginUserInfo(true);

             rest.post({
                url: 'user/voucherCount',//147.免费文书制作申请;10.8
                data : {
                    state : 0,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                    userId: loginUserInfo.userId
                },
                success: function (result) {
                    $('#notused_count').html(result.data.voucherCount)
                }
            });
            rest.post({
                url: 'user/voucherCount',//147.免费文书制作申请;10.8
                data : {
                    state : 3,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                    userId: loginUserInfo.userId
                },
                success: function (result) {
                    $('#used_count').html(result.data.voucherCount)
                }
            });
            rest.post({
                url: 'user/voucherCount',//147.免费文书制作申请;10.8
                data : {
                    state : 2,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                    userId: loginUserInfo.userId
                },
                success: function (result) {
                    $('#expired_count').html(result.data.voucherCount)
                }
            });
        },




		//立即使用
		useVoucher:function () {
		},


        list1DownRefresh: function () {
            var _that = this;

            setTimeout(function () {
                _that.list1List = [];
                _that.list1PageNo = 1;
                _that.tolist1List(_that.list1DownSuccess);//具体取数据的方法
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
                _that.list1IsNot = true;
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
                data : {
                    state : 0,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                    userId: loginUserInfo.userId
                },
                url: 'user/voucherList/{pageNo}/{pageSize}',
                urlParams: {
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
                _that.tolist2List(_that.list2DownSuccess);//具体取数据的方法
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
                _that.list2IsNot = true;
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
                data : {
                    state : 2,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                    userId: loginUserInfo.userId
                },
                url: 'user/voucherList/{pageNo}/{pageSize}',
                urlParams: {
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
        },



        list3DownRefresh: function () {
            var _that = this;

            setTimeout(function () {
                _that.list3List = [];
                _that.list3PageNo = 1;
                _that.tolist3List(_that.list3DownSuccess);//具体取数据的方法
            }, 500);
        },

        list3UpRefresh: function () {
            var _that = this;
            setTimeout(function () {
                _that.tolist3List(_that.list3UpSuccess);//具体取数据的方法
            }, 500);
        },

        list3UpSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.list3ListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function () {
                    mui(_that.list3ListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.list3ListId).pullRefresh().endPullupToRefresh(false);
                _that.list3List = _that.list3List.concat(result.rows);
                _that.list3PageNo++;
            }
        },

        list3DownSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.list3IsNot = true;
                mui(_that.list3ListId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.list3ListId).pullRefresh().endPulldownToRefresh(false);
                _that.list3List = result.rows;
                _that.list3PageNo++;
            }
        },

        tolist3List: function (func) {
            var loginUserInfo = utils.getLoginUserInfo(true);
            var _that = this;
            rest.post({
                data : {
                    state : 3,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                    userId: loginUserInfo.userId
                },
                url: 'user/voucherList/{pageNo}/{pageSize}',
                urlParams: {
                    pageNo: _that.list3PageNo,
                    pageSize: _that.list3PageSize
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



