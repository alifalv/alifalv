new Vue({
	el:'#app',
	data:{
        collecResumeList: [],//最新资讯
        collecResumeListIsNot : false,
        collecResumeListPageNo:1,
        collecResumeListPageSize:10,
        collecResumeListId : '#collecResumeList'
	},
	mounted:function(){
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

        mui(that.collecResumeListId).on('tap', 'a',function(){
            var href = $(this).attr('href');
            creatMywebview(href, href);
            return false;
        });
    },

	methods:{
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
                url: 'user/messageSearch/{pageNo}/{pageSize}',
                data : {
                    userId :loginUserInfo.userId
                },
                urlParams: {
                    pageNo: _that.collecResumeListPageNo,
                    pageSize: _that.collecResumeListPageSize
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
