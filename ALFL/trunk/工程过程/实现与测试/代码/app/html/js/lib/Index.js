new Vue({
	el: '#app',
	data: {
		tjlsList: [],
		active:"0", 
		topP:"0",
		flag: true,
		isFixed: false,
        offsetTop:0,
		cityName: "",
		newAskList: [], //最新资讯
		isNoNewAskList: false,
		newAskPageNo: 1,
		newAskPageSize: 10,
		newAskListId: '#newAskList',
		legalList: [],
		newCaseList: [], //最新案源
		isNoNewCaseList: false,
		newCasePageNo: 1,
		newCasePageSize: 10,
		newCaseListId: '#newCaseList',
		aherfdata:"84984" ,
		loading: false,   //是否处于加载状态
		 finished: false,  //是否已加载完所有数据 
		 isLoading: false,   //是否处于下拉刷新状态
	},
	mounted: function() {
		var that = this;


		this.getcity()
		




		rest.post({
			url: 'article/legalList/{pageNo}/{pageSize}',
			data: {
				title: '',
				articleContent: '',
				effectLevel: '',
				sendUnit: '',
				startTime: '',
				endTime: ''
			},
			urlParams: {
				pageNo: 1,
				pageSize: 10
			},

			success: function(result) {
				that.legalList = result.rows;
				myswiper();
				myswiper2();
			},
			fail: function(result) {
				mui.toast(result.msg);
			}
		});

		rest.post({
			isNeedToken: false,
			url: 'user/adminRecommendCounselorList',
			data: {
				city: utils.getCityId()
			},
			success: function(result) {
				that.tjlsList = result.data;
			},
			fail: function(result) {}
		});


		mui.init();
		mui(that.newAskListId).pullRefresh({

			up: {
				auto: true,
				indicators: true, //是否显示滚动条
				callback: that.newAskUpRefresh
			}
		});

		mui(that.newCaseListId).pullRefresh({

			up: {
				auto: true,
				indicators: true, //是否显示滚动条
				callback: that.newCaseUpRefresh
			}
		});
		mui('#newAskList').pullRefresh().endPulldownToRefresh(true); //暂时禁止滚动
		mui("#consultation,#newAskList, #newCaseList").on('tap', "a", function() {
			var href = this.getAttribute("href");
			creatMywebview(href, href);
			return false;
		});
		mui("#lihref").on('tap', ".flex_Horizontal", function() {
			var href = this.getAttribute("href");
			creatMywebview(href, href);
			return false;
		});
		window.onscroll = function(){
		   		//变量scrollTop是滚动条滚动时，距离顶部的距离
		   		var scrollTop = document.documentElement.scrollTop||document.body.scrollTop;
		   		//变量windowHeight是可视区的高度
		   		var windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
		   		//变量scrollHeight是滚动条的总高度
		   		var scrollHeight = document.documentElement.scrollHeight||document.body.scrollHeight;
		               //滚动条到底部的条件
		               if(scrollTop+windowHeight==scrollHeight){
		                //写后台加载数据的函数
		                   var defaultTab2 = document.getElementById("defaultTab2");
		                   //模拟首页点击
						   that.onLoad()
		                   // mui.trigger(defaultTab2, 'tap');
						   // alert("距顶部"+scrollTop+"可视区高度"+windowHeight+"滚动条总高度"+scrollHeight);
		              }   
		        }
		window.addEventListener('scroll',this.initHeight);
		window.addEventListener('scroll',this.handleScroll);
      this.$nextTick( () => {
        this.offsetTop = document.querySelector('#boxFixed').offsetTop;
      })

 
	},
	created(){
   	
   },

	methods: {
		initHeight () {
        var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
        this.isFixed = scrollTop > this.offsetTop ? true : false;
      },
	  handleScroll () {
			let scrollY = window.scrollY;
			// this.topP = scrollY
			// mui.toast(scrollY);
// 			if (scrollY > 100) {
// 			   
// 			} else {
// 			   
// 			}
		   },
		 onLoad() {      //上拉加载
		 if(!this.flag){
			 return
		 }
        if(this.active == 0){
				   this.newAskPageNo =  this.newAskPageNo++
				   // alert(this.newAskPageNo)
				   this.newAskUpRefresh()
				   this.flag = false
			   }else {
				   this.newCasePageNo =  this.newCasePageNo++
				   // alert(this.newCasePageNo)
				   this.newCaseUpRefresh()
				   this.flag = false
			   }
            },
		 
			
            onRefresh() {       //下拉刷新
			this.newAskDownRefresh()
			this.newCaseDownRefresh()
			
                setTimeout(() => {
                    this.finished = false;
                    this.isLoading = false;
                    this.list = []
                    this.onLoad()
                }, 500);
				 
            },
			change(e){
				 
				this.active = e;
				 
			 
			},
		toinfo(e){
			// alert(e);
			// window.location.href="Law-Info.html?articleId= + e"
			// window.location.href=`Law-Info.html?articleId=${e}` 
			this.aherfdata = e
			if(this.aherfdata== ''){
				 
				return
				
			} else {
				var myaa = document.getElementById("myaa");
				mui.trigger(myaa, 'click');
			}
			
		},
		getcity() {
			var _that = this
			var map, geolocation; //加载地图，调用浏览器定位服务
			map = new AMap.Map('container', {
				resizeEnable: true
			});
			map.plugin('AMap.Geolocation', function() {
				geolocation = new AMap.Geolocation({
					enableHighAccuracy: true,
					timeout: 10000
				});
				geolocation.getCityInfo(getCity)
			});

			function getCity(status, result) {
				if (status != 'complete') {
					console.log(status)
					// showToast('定位失败');
				} else {
					console.log(result)
					console.log(result.city)
					_that.cityName = result.city
					localStorage.cityName = result.city
					_that.getALLCity()
				}
			}
		},

		//获取城市列表
		getALLCity() {
			var that = this
			
			if(localStorage.citylist){
				return;
			}
			rest.post({
				isNeedToken: false,
				url: 'address/allCityList',
				urlParams: {
				 
				},
				data: {
					 
				},
				success: function(result) {
					console.log(result.data)
					localStorage.citylist = JSON.stringify(result.data)
					
				 
					var cityName =  localStorage.cityName
					that.setCityId(cityName) 
					
				},
				fail: function(result) {}
			});
		},
		
		setCityId(cityName) {   //设置城市id
	 
		    var allCityList = JSON.parse(localStorage.citylist)
			console.log(cityName)
			console.log(allCityList)
		    for (var i = 0; i < allCityList.length; i++) {
		        if (cityName.indexOf(allCityList[i].cityName) > -1 || allCityList[i].cityName.indexOf(cityName) > -1) {
		            console.log(allCityList[i].cityId)
		            localStorage.cityId =  allCityList[i].cityId;
		            return;
		        }
		    }
		},

		newAskDownRefresh: function() {
			var _that = this;

			setTimeout(function() {
				_that.newAskList = [];
				_that.newAskPageNo = 1;
				_that.toNewAskList(_that.newAskDownSuccess); //具体取数据的方法
			}, 500);
		},

		newAskUpRefresh: function() {
			var _that = this;
			setTimeout(function() {
				_that.toNewAskList(_that.newAskUpSuccess); //具体取数据的方法
			}, 500);
		},

		newAskUpSuccess: function(result) {
			var _that = this;
			if (null == result.rows || result.rows.length == 0) {
				mui(_that.newAskListId).pullRefresh().endPullupToRefresh(true);
				setTimeout(function() {
					mui(_that.newAskListId).pullRefresh().refresh(true);
				}, 2000);
			} else {
				mui(_that.newAskListId).pullRefresh().endPullupToRefresh(false);
				_that.newAskList = _that.newAskList.concat(result.rows);
				_that.newAskPageNo++;
			}
		},

		newAskDownSuccess: function(result) {
			var _that = this;
			if (null == result.rows || result.rows.length == 0) {
				_that.isNoNewAskList = true;
				mui(_that.newAskListId).pullRefresh().endPulldownToRefresh(true);
			} else {
				mui(_that.newAskListId).pullRefresh().endPulldownToRefresh(false);
				_that.newAskList = result.rows;
				_that.newAskPageNo++;
			}
		},

		//最新咨询
		toNewAskList: function(func) {
			var _that = this;
			rest.post({
				isNeedToken: false,
				url: 'advice/adviceList/{pageNo}/{pageSize}',
				urlParams: {
					pageNo: _that.newAskPageNo,
					pageSize: _that.newAskPageSize
				},
				data: {
					queryType: 1,
					conditionValue: _that.conditionValue
				},
				success: function(result) {
					func(result)
					_that.flag= true
				},
				fail: function(result) {}
			});
		},

		newCaseDownRefresh: function() {
			var _that = this;

			setTimeout(function() {
				_that.newCaseList = [];
				_that.newCasePageNo = 1;
				_that.toNewCaseList(_that.newCaseDownSuccess); //具体取数据的方法
			}, 500);
		},

		newCaseUpRefresh: function() {
			var _that = this;
			setTimeout(function() {
				_that.toNewCaseList(_that.newCaseUpSuccess); //具体取数据的方法
			}, 500);
		},

		newCaseUpSuccess: function(result) {
			var _that = this;
			if (null == result.rows || result.rows.length == 0) {
				mui(_that.newCaseListId).pullRefresh().endPullupToRefresh(true);
				setTimeout(function() {
					mui(_that.newCaseListId).pullRefresh().refresh(true);
				}, 2000);
			} else {
				mui(_that.newCaseListId).pullRefresh().endPullupToRefresh(false);
				_that.newCaseList = _that.newCaseList.concat(result.rows);
				_that.newCasePageNo++;
			}
		},

		newCaseDownSuccess: function(result) {
			var _that = this;
			if (null == result.rows || result.rows.length == 0) {
				_that.isNoNewCaseList = true;
				mui(_that.newCaseListId).pullRefresh().endPulldownToRefresh(true);
			} else {
				mui(_that.newCaseListId).pullRefresh().endPulldownToRefresh(false);
				_that.newCaseList = result.rows;
				_that.newCasePageNo++;
			}
		},

		//最新案源
		toNewCaseList: function(func) {
			var _that = this;
			rest.post({
				isNeedToken: false,
				url: 'case/caseList/{pageNo}/{pageSize}',
				urlParams: {
					pageNo: _that.newCasePageNo,
					pageSize: _that.newCasePageSize
				},
				data: {
					conditionValue: _that.conditionValue,
					is_delete: 0
				},
				success: function(result) {
					func(result)
					_that.flag= true
				},
				fail: function(result) {}
			});
		}
	},
	destroyed () {
      window.removeEventListener('scroll', this.handleScroll)
    },
});


function myswiper() {
	var swiper = new Swiper('.swiper-container', {
		//effect: 'flip',
		loop: true,
		autoplay: 5000,
		pagination: '.swiper-pagination',
		paginationClickable: true
	});
}

function myswiper2() {
	var swiper = new Swiper('.swiper-container2', {
		loop: true,
		autoplay: 2000,
		direction: 'vertical',
		pagination: '.swiper-pagination',
		paginationClickable: true
	});
}
