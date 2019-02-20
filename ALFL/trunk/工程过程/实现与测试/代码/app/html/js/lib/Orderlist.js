//常用文书模板
var vm = new Vue({
    el:'#app',
    data:{
        activeHash : '',

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

        list3List: [],//已使用
        list3IsNot : false,
        list3PageNo:1,
        list3PageSize:5,
        list3ListId : '#list3',

        list4List: [],//已过期
        list4IsNot : false,
        list4PageNo:1,
        list4PageSize:5,
        list4ListId : '#list4',

        list5List: [],//已过期
        list5IsNot : false,
        list5PageNo:1,
        list5PageSize:5,
        list5ListId : '#list5'
    },
    mounted :function(){


        var that = this;
        var loginUserInfo = utils.getLoginUserInfo(true);
        that.activeHash = location.hash.replace('#', '');
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

        mui(that.list4ListId).pullRefresh({
            down: {
                style: 'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                color: '#2BD009',
                // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                indicators: true, //是否显示滚动条
                callback: that.list4DownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
            },
            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.list4UpRefresh
            }
        });


        mui(that.list5ListId).pullRefresh({
            down: {
                style: 'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                color: '#2BD009',
                // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                indicators: true, //是否显示滚动条
                callback: that.list5DownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
            },
            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.list5UpRefresh
            }
        });

        mui('#app').on('tap', 'li',function(){
            var href = $(this).attr('href');
            creatMywebview(href, href);
            return false;
        });

        mui('#app').on('tap', '.j-btn-zhifu',function(){
            var businessOrder = $(this).data('businessorder');
            that.zhiFu(businessOrder);
            return false;
        });

        mui('#app').on('tap', '.j-btn-pingjia',function(){
            var businessOrder = $(this).data('businessorder');
            that.pingjia(businessOrder);
            return false;
        });

        mui('#app').on('tap', '.j-btn-wancheng',function(){
            utils.getLoginUserInfo(true);
            var businessOrder = $(this).data('businessorder');
            C.confirm('确认完成？',function () {
                that.wancheng(businessOrder);
            });
            return false;
        });

        mui('#app').on('tap', '.j-btn-shouhuo',function(){
            utils.getLoginUserInfo(true);
            var businessOrder = $(this).data('businessorder');
            var userId = $(this).data('userid');
            mui.confirm('确认取消订单?', '提示', ['取消', '确定'], function(e) {
                if(e.index == 1) {
                    that.shouhuo(businessOrder, userId);
                }
            });
            return false;
        });

        mui('#app').on('tap', '.j-btn-fahuo',function(){
            var _loginUserInfo = utils.getLoginUserInfo(true);
            var businessOrder = $(this).data('businessorder');
            var favoreeUserId = _loginUserInfo.userId;
            mui.confirm('确认取消订单?', '提示', ['取消', '确定'], function(e) {
                if(e.index == 1) {
                    that.fahuo(businessOrder, favoreeUserId);
                }
            });
            return false;
        });

        mui('#app').on('tap', '.j-btn-quxiao',function(){
            utils.getLoginUserInfo(true);
            var businessOrder = $(this).data('businessorder');
            var userId = $(this).data('userid');
            mui.confirm('确认取消订单?', '提示', ['取消', '确定'], function(e) {
                if(e.index == 1) {
                    that.quxiao(businessOrder, userId);
                }
            });
            return false;
        });

        mui('#app').on('tap', '.j-btn-yiyi',function(){
            var businessOrder = $(this).data('businessorder');
            creatMywebview('Orderlist-shenSu.html?businessOrder=' + businessOrder, 'Orderlist-shenSu.html?businessOrder=' + businessOrder);
            return false;
        });

        window.addEventListener("yiyiCallback", function(e) {
            that.list1DownRefresh();
            mui.toast(e.detail.result.msg);
        });

    },

    methods:{
        zhiFu : function (businessOrder) {
            creatMywebview('Order-pay.html?businessOrder=' + businessOrder, '.html?businessOrder=' + businessOrder);
            return false;
        },

        pingjia : function (businessOrder) {
            //TODO url
            creatMywebview('.html?businessOrder=' + businessOrder, '.html?businessOrder=' + businessOrder);
            return false;
        },

        //完成确认
        wancheng : function (businessOrder) {
            var that = this;
            rest.post({
                url: 'case/caseDeputeAccomplish',//141.订单列表系列中 【案件委托： 确认完成按钮功能 】;9.30
                data : {
                    businessOrder: businessOrder//当前订单的编号
                },
                success: function(result) {
                    mui.toast(result.msg);
                    that.list1DownRefresh();
                },
                fail : function (result) {
                    mui.toast(result.msg);
                }
            });
        },

        /**
         * 收货确认(法律文书制作)
         * @param businessOrder 当前订单的编号
         * @param userId 文书申请人的userId
         */
        shouhuo : function (businessOrder, userId) {
            var that = this;
            rest.post({
                url: 'order/reception',//143.订单列表系列中 【法律文书制作： 确认收货按钮功能 】;9.30
                data: {
                    businessOrder: businessOrder,//当前订单的编号
                    userId: userId//文书申请人的userId
                },
                success: function (result) {
                    mui.toast(result.msg);
                    that.list1DownRefresh();
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
        },

        /**
         * 发货确认(法律文书制作)
         * @param businessOrder 当前订单的编号
         * @param favoreeUserId 文书制作人的Id
         */
        fahuo : function (businessOrder, favoreeUserId) {
            var that = this;
            rest.post({
                url: 'order/sender',//142. 订单列表系列中 【法律文书制作： 确认发货按钮功能 】;9.30
                data: {
                    businessOrder: businessOrder,//当前订单的编号
                    favoreeUserId: favoreeUserId//文书制作人的Id
                },
                success: function (result) {
                    mui.toast(result.msg);
                    that.list1DownRefresh();
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
        },

        /**
         * 取消订单
         * @param businessOrder 当前订单的编号
         * @param userId 文书申请人的userId
         */
        quxiao : function (businessOrder, userId) {
            var that = this;
            rest.post({
                url: 'order/cancelOrder',//144.订单列表系列中 【法律文书制作： 取消按钮功能 】;9.30
                data : {
                    businessOrder: businessOrder,//当前订单的编号
                    userId: userId//文书申请人的userId
                },
                success: function(result) {
                    mui.toast(result.msg);
                    that.list1DownRefresh();
                },
                fail : function (result) {
                    mui.toast(result.msg);

                }
            });
        },

        yiyi : function () {
            alert(1);
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
                    userId: loginUserInfo.userId,
                    special : ''
                },
                url: 'order/listBusinessOrder/{model}/{pageNo}/{pageSize}',
                urlParams: {
                    model:1,
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
                    userId: loginUserInfo.userId,
                    special : '1'
                },
                url: 'order/listBusinessOrder/{model}/{pageNo}/{pageSize}',
                urlParams: {
                    model:1,
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
                    userId: loginUserInfo.userId,
                    special : ''
                },
                url: 'order/listBusinessOrder/{model}/{pageNo}/{pageSize}',
                urlParams: {
                    model:1,
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
        },

        list4DownRefresh: function () {
            var _that = this;

            setTimeout(function () {
                _that.list4List = [];
                _that.list4PageNo = 1;
                _that.tolist4List(_that.list4DownSuccess);//具体取数据的方法
            }, 400);
        },

        list4UpRefresh: function () {
            var _that = this;
            setTimeout(function () {
                _that.tolist4List(_that.list4UpSuccess);//具体取数据的方法
            }, 400);
        },

        list4UpSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.list4ListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function () {
                    mui(_that.list4ListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.list4ListId).pullRefresh().endPullupToRefresh(false);
                _that.list4List = _that.list4List.concat(result.rows);
                _that.list4PageNo++;
            }
        },

        list4DownSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.list4IsNot = true;
                mui(_that.list4ListId).pullRefresh().endPulldownToRefresh(true);

            } else {
                mui(_that.list4ListId).pullRefresh().endPulldownToRefresh(false);
                _that.list4List = result.rows;
                _that.list4PageNo++;
            }
        },

        tolist4List: function (func) {
            var loginUserInfo = utils.getLoginUserInfo(true);
            var _that = this;
            rest.post({
                data : {
                    userId: loginUserInfo.userId,
                    special : '2'
                },
                url: 'order/listBusinessOrder/{model}/{pageNo}/{pageSize}',
                urlParams: {
                    model:1,
                    pageNo: _that.list4PageNo,
                    pageSize: _that.list4PageSize
                },
                success: function (result) {
                    func(result)
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
        },

        list5DownRefresh: function () {
            var _that = this;

            setTimeout(function () {
                _that.list5List = [];
                _that.list5PageNo = 1;
                _that.tolist5List(_that.list5DownSuccess);//具体取数据的方法
            }, 500);
        },

        list5UpRefresh: function () {
            var _that = this;
            setTimeout(function () {
                _that.tolist5List(_that.list5UpSuccess);//具体取数据的方法
            }, 500);
        },

        list5UpSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.list5ListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function () {
                    mui(_that.list5ListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.list5ListId).pullRefresh().endPullupToRefresh(false);
                _that.list5List = _that.list5List.concat(result.rows);
                _that.list5PageNo++;
            }
        },

        list5DownSuccess: function (result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.list5IsNot = true;
                mui(_that.list5ListId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.list5ListId).pullRefresh().endPulldownToRefresh(false);
                _that.list5List = result.rows;
                _that.list5PageNo++;
            }
        },

        tolist5List: function (func) {
            var loginUserInfo = utils.getLoginUserInfo(true);
            var _that = this;
            rest.post({
                data : {
                    userId: loginUserInfo.userId,
                    special : '3'
                },
                url: 'order/listBusinessOrder/{model}/{pageNo}/{pageSize}',
                urlParams: {
                    model:1,
                    pageNo: _that.list5PageNo,
                    pageSize: _that.list5PageSize
                },
                success: function (result) {
                    func(result);
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
        }
    }
});