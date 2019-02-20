new Vue({
    el: '#app',
    data: {
        pullId: '#pullrefresh',
		morenImg: "",
        isNoList: false,
        list: [],
        pageNo: 1,
        pageSize: 5,
        conditionValue: '', 
        queryType: 1,
        sortType: 1, 
		areaId:"", 
		arealist:[ ],
		jobId:"",
		joblist:[ ],
		caseId:"",
		caselist:[ ],
		aiId:"",
		ailist:[ ]
    },
    mounted: function() {
        var _that = this;
		
		this.getSelList()
        mui.init({
            pullRefresh: {
                container: _that.pullId, //下拉刷新容器标识，querySelector能定位的css选择器均可，比如：id、.class等
                down: {
                    style: 'circle', //必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                    color: '#2BD009',
                    // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                    indicators: true, //是否显示滚动条
                    callback: this.pullDownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
                },
                up: {
                    auto: true,
                    indicators: true, //是否显示滚动条
                    callback: _that.pullUpRefresh
                }
            }
        });

        mui("#app").on('tap', "#searchbtn", function() {
            _that.tapSearch();
        });

        mui(".lawerbox").on('tap', "a", function() {
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });
    },

    methods: {
        query: function(value) {
            //TODO
        },
		getSelList(){ //搜索筛选项
			  var _that = this;
			  
			rest.post({
			    isNeedToken: false,
			    url: 'systemConfig/jobList',
			    urlParams: {
			      
			    },
			    data: {
			      
			    },
			    success: function(result) {
			        
					console.log(result)
					_that.joblist = result.data
			    },
			    fail: function(result) {}
			});
			
			rest.post({
			    isNeedToken: false,
			    url: 'systemConfig/caseList',
			    urlParams: {
			      
			    },
			    data: {
			      
			    },
			    success: function(result) {
					console.log(result)
					_that.caselist = result.data
			    },
			    fail: function(result) {}
			});
			
			rest.post({
			    isNeedToken: false,
			    url: 'address/provinceList',
			    urlParams: {
			      
			    },
			    data: {
			      
			    },
			    success: function(result) {
					console.log(result)
					_that.arealist = result.data
			    },
			    fail: function(result) {}
			});
		},
		
        tapSearch: function() {
            var _that = this;
            _that.conditionValue = _that.$refs.conditionValue.value;
            _that.pullDownRefresh();
        },

        pullDownRefresh: function() {
            var _that = this;

            setTimeout(function() {
                _that.list = [];
                _that.pageNo = 1;
                _that.toList(_that.downSuccess); //具体取数据的方法
            }, 500);
        },

        pullUpRefresh: function() {
            var _that = this;
            setTimeout(function() {
                _that.toList(_that.upSuccess); //具体取数据的方法
            }, 500);
        },

        upSuccess: function(result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.pullId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function() {
                    mui(_that.pullId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.pullId).pullRefresh().endPullupToRefresh(false);
                _that.list = _that.list.concat(result.rows);
                _that.pageNo++;
            }
        },

        downSuccess: function(result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.isNoList = true;
                mui(_that.pullId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.pullId).pullRefresh().endPulldownToRefresh(false);
                _that.list = result.rows;
                _that.pageNo++;
            }
        },

        toList: function(func) {
            var _that = this;
            rest.post({
                isNeedToken: false,
                url: 'user/counselorList/{pageNo}/{pageSize}',
                urlParams: {
                    pageNo: _that.pageNo,
                    pageSize: _that.pageSize,
					
                },
                data: {
                    queryType: 1,
                    sortType: 1,
                    name: _that.conditionValue,
					province:_that.areaId,
					speciality:_that.caseId,
					job:_that.jobId,
                },
                success: function(result) {
                    func(result)
                },
                fail: function(result) {}
            });
        }
    }

});