var vm = new Vue({
    el: '#app',
    data: {
        title: '',
        articleContent: '',
        effectivenessGradeList: [],
        effectivenessId: '',
        unitList: [],
        unitId: '',
        loginUserId: -1,
        articleId: null,
        userType: '',
        articleData: {},
        isStar: false,
		noMore:false,
		
		 
		 

        list1List: [], //未使用
        list1IsNot: false,
        list1PageNo: 1,
        list1PageSize: 5,
        list1ListId: '#list1',

        list2List: [], //已使用
        list2IsNot: false,
        list2PageNo: 1,
        list2PageSize: 5,
        list2ListId: '#list2'
    },
    mounted: function() {
        var that = this;
        $('.buttongroup').on('tap', '#showmore', function(e) {
            var hiddenpart = $('.hiddenpart'),
                showbox = $('.inputbox');
            if (hiddenpart.css('display') == 'none') {
                showbox.addClass('show');
                setTimeout(function() {
                    $('.selectrow select').removeAttr('disabled');
                }, 100)
            } else {
                showbox.removeClass('show');
                $('.selectrow select').attr('disabled', 'disabled');
            }
        });
		
		

        rest.post({
            url: 'systemConfig/effectivenessGradeList',
            success: function(result) {
                that.effectivenessGradeList = result.data;
            }
        });

        rest.post({
            url: 'systemConfig/unitList',
            success: function(result) {
                that.unitList = result.data;
            }
        });

        mui(".listbox").on('tap', "a", function() {
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });


        mui.init();
        mui(that.list1ListId).pullRefresh({
            down: {
                style: 'circle', //必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                color: '#2BD009',
                // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                indicators: true, //是否显示滚动条
                callback: this.list1DownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
            },
            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.list1UpRefresh
            }
        });

        mui(that.list2ListId).pullRefresh({
            down: {
                style: 'circle', //必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
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
    },

    methods: {
        search: function() {
            this.list1DownRefresh();
        },
		resetV(){
			this.title=''
			this.articleContent=''
			this.unitId=''
			this.effectivenessId=''
		},
		//获取发文单位
		getdwList(){
			var _this =this
			rest.post({
			    data: {
			      type: 1
			    },
			    url: 'article/legalList/1/10',
			    success: function(result) {
			         console.log(result)
			    },
			    fail: function(result) {
			        mui.toast(result.msg);
			    }
			});
			
		},

        list1DownRefresh: function() {
            var _that = this;

            setTimeout(function() {
                _that.list1List = [];
                _that.list1PageNo = 1;
                _that.tolist1List(_that.list1DownSuccess); //具体取数据的方法
            }, 500);
        },

        list1UpRefresh: function() {
            var _that = this;
            setTimeout(function() {
                _that.tolist1List(_that.list1UpSuccess); //具体取数据的方法
            }, 500);
        },

        list1UpSuccess: function(result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.list1ListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function() {
                    mui(_that.list1ListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.list1ListId).pullRefresh().endPullupToRefresh(false);
                _that.list1List = _that.list1List.concat(result.rows);
                _that.list1PageNo++;
            }
        },

        list1DownSuccess: function(result) {
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

        tolist1List: function(func) {
            var _that = this;
            rest.post({
                data: {
                    title: _that.title,
                    articleContent: _that.articleContent,
                    sendUnit: _that.unitId,
                    effectLevel: _that.effectivenessId
                },
                url: 'article/legalList/{pageNo}/{pageSize}',
                urlParams: {
                    pageNo: _that.list1PageNo,
                    pageSize: _that.list1PageSize
                },

                success: function(result) {
                    func(result)
					console.log(result)
					if(result.rows.length == 0){
						_that.noMore = true
					}else{
						_that.noMore = false
					}
                },
                fail: function(result) {
                    mui.toast(result.msg);
                }
            });
        },

        list2DownRefresh: function() {
            var _that = this;

            setTimeout(function() {
                _that.list2List = [];
                _that.list2PageNo = 1;
                _that.tolist2List(_that.list2DownSuccess); //具体取数据的方法
            }, 500);
        },

        list2UpRefresh: function() {
            var _that = this;
            setTimeout(function() {
                _that.tolist2List(_that.list2UpSuccess); //具体取数据的方法
            }, 500);
        },

        list2UpSuccess: function(result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.list2ListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function() {
                    mui(_that.list2ListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.list2ListId).pullRefresh().endPullupToRefresh(false);
                _that.list2List = _that.list2List.concat(result.rows);
                _that.list2PageNo++;
            }
        },

        list2DownSuccess: function(result) {
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

        tolist2List: function(func) {
            var loginUserInfo = utils.getLoginUserInfo(true);
            var _that = this;
            rest.post({
                data: {
                    userId: loginUserInfo.userId,
                    special: '1'
                },
                url: 'order/listBusinessOrder/{model}/{pageNo}/{pageSize}',
                urlParams: {
                    model: 1,
                    pageNo: _that.list2PageNo,
                    pageSize: _that.list2PageSize
                },
                success: function(result) {
                    func(result)
                },
                fail: function(result) {
                    mui.toast(result.msg);
                }
            });
        }
    }
});