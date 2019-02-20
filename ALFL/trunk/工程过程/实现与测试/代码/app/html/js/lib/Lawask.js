new Vue({
    el: '#app',
    data: {
        pullId: '#newCase',
        pullId2: '#newCase2',
        isNoList: false,
		active:'0', 
        list: [],
		caseId: '',
        queryType: 1,
        caseTitle: '',
        caseList: [],  
        pageNo: 1,
        pageSize: 5,
        conditionValue: '',
		loading: false,   //是否处于加载状态
		 finished: false,  //是否已加载完所有数据 
		 isLoading: false,   //是否处于下拉刷新状态
    },
	filters:({
		hidename(na){
			 if(na){
				 var na1 = na.substr(0, 1)
				 return na1+"**"
			 } else {
				  return "匿名用户"
			 }
		}
	}),
	
    mounted: function() {
		 let winHeight = document.documentElement.clientHeight                          //视口大小
		 document.getElementById('list-content').style.height = (winHeight - 46) +'px'  //调整上拉加载框高度
        var _that = this;
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
		 
		
		

	 
		
		mui('.mui-scroll-wrapper').scroll({
	deceleration: 0.0005 //flick 减速系数，系数越大，滚动速度越慢，滚动距离越小，默认值0.0006
});

        mui("#app").on('tap', "#searchbtn", function() {
            _that.tapSearch();
        });

        mui("#pullrefresh").on('tap', "a", function() {
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });
		
        mui("#app").on('tap', ".flex_Horizontal", function() {
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });

        // 案源类型
        rest.post({
            url: 'systemConfig/caseList',
            success: function(result) {
                _that.caseList = result.data;
            }
        });
    },

    methods: {
		onLoad() {      //上拉加载
		     
			 this.pageNo =  this.pageNo++
			 
		    }, 
		    onRefresh() {       //下拉刷新
		        setTimeout(() => {
		            this.finished = false;
		            this.isLoading = false;
		            this.list = []
		            this.onLoad()
		        }, 500);
		    },
		change(e){
			this.active = e;
			 if(e == 0){
				this.queryType = 1
			 }else {
				 this.queryType = 2
			 }
			this.pullDownRefresh();
		},
        searchCase: function() {
            this.pullDownRefresh();
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
                url: 'advice/adviceList/{pageNo}/{pageSize}',
                urlParams: {
                    pageNo: _that.pageNo,
                    pageSize: _that.pageSize
                },
                data: {
                    queryType: _that.queryType,
                    caseId: this.caseId,
                    keyWord: this.caseTitle,
                    conditionValue: _that.conditionValue
                },
                success: function(result) {
                    func(result)
                },
                fail: function(result) {}
            });
        }
    }
});