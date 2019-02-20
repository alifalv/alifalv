new Vue({
    el:'#app',
    data:{
        list1List: [],//未使用
        list1IsNot : false,
        list1PageNo:1,
        list1PageSize:5,
        list1ListId : '#list1',
        userBalance : 0

    },
    mounted:function(){
        var
            that = this;

        var loginUserInfo = utils.getLoginUserInfo(true);

        that.userBalance = loginUserInfo.userBalance;
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

    },

    methods: {

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
                url: 'walletRecord/listWalletRecord/{pageNo}/{pageSize}',
                data : {
                    userId: loginUserInfo.userId
                },
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
        }
    }
})
