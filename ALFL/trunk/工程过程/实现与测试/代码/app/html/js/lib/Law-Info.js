//常用文书模板
var vm = new Vue({
    el:'#app',
    data:{
        loginUserId : -1,
        articleId : null,
		show:false,
		showFlag: false,
        userType:'', 
        articleData: {}, 
        isStar : false
    },
	destroyed () {
	  window.removeEventListener('scroll', this.handleScroll)
	},
	created() {
		  var _this = this;
		 var loginUserInfo = utils.getLoginUserInfo();
		 if(loginUserInfo) {
		     _this.loginUserId = loginUserInfo.userId;
		 }
		 var urlParams = utils.getUrlParams();
		 _this.articleId =  urlParams.articleId;
		 
		 rest.post({
		     url: 'article/articleDetails/{userId}/{articleId}',
		     urlParams : {
		         userId : _this.loginUserId,
		         articleId : urlParams.articleId
		     },
		     data : {
		         columnCode : 8
		     },
		     success: function (result) {
		         _this.articleData = result.data;
		     }
		 });
	},
    mounted :function(){
		var that = this
		 	window.onscroll = function(){
		    		//变量scrollTop是滚动条滚动时，距离顶部的距离
		    		var scrollTop = document.documentElement.scrollTop||document.body.scrollTop;
		 
					// alert(scrollTop)
		    		//变量windowHeight是可视区的高度
		    		var windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
		    		//变量scrollHeight是滚动条的总高度
		    		var scrollHeight = document.documentElement.scrollHeight||document.body.scrollHeight;
		                //滚动条到底部的条件
		                if(scrollTop > (windowHeight/3) ){
		                  that.showFlag = true
		               } else {
			  that.showFlag = false
		 }
		         }
       
    },
	destroyed () {
  // window.removeEventListener('scroll', this.scrollToTop)
},


    methods:{
	 
 
		goTop(){
			// alert(document.body.scrollTop)
			 document.body.scrollTop = 0
            document.documentElement.scrollTop = 0
		},
		beforeClose(){
			this.show = false
		},
		fenxiang(){
			this.show = true
		},
			
		confirm1(){
			this.show = false
		},
		next(articleId){
			 
			var _this = this;
			// window.reload()
			rest.post({
			    url: 'article/articleDetails/{userId}/{articleId}',
			    urlParams : {
			        userId : _this.loginUserId,
			        articleId : articleId
			    },
			    data : {
			        columnCode : 8
			    },
			    success: function (result) {
					// debugger
					// _this.articleData = {}
			        _this.articleData = result.data;
					_this.goTop()
			    }
			});
			
			
		},
		
        star : function() {
            var _this = this;
            //文章收藏
            var _$this = $(this);
            var loginUserInfo = utils.getLoginUserInfo(true);
            rest.post({
                url: 'article/saveArticleCollection/{userId}/{articleId}',
                urlParams : {
                    userId : loginUserInfo.userId,
                    articleId : _this.articleId
                },
                data : {
                    nickName : loginUserInfo.nickName
                },
                success: function(result) {
                    mui.toast(result.msg);
                    if(result.data.ok) {
                        _this.articleData.isCollection =( _this.articleData.isCollection == '是' ? '否' : '是');
                    } else {
                        _this.articleData.isCollection =( _this.articleData.isCollection == '是' ? '否' : '是');
                    }
                },
                fail : function (result) {
                    mui.toast(result.msg);
                }
            });
        }
    }
});

