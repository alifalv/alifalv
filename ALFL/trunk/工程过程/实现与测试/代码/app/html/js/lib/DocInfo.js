//常用文书模板
var vm = new Vue({
    el:'#app',
    data:{
        loginUserId : -1,
        articleId : null,
        userType:'',
        articleData: {},
        isStar : false
    },
    mounted :function(){
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
                columnCode : 3
            },
            success: function (result) {
                _this.articleData = result.data;
            },
            fail : function (result) {
                mui.toast(result.msg);
            }
        });
    },

    methods:{
		next(articleId){
			// alert(articleId)
			var _this = this; 
			rest.post({
			    url: 'article/articleDetails/{userId}/{articleId}',
			    urlParams : {
			        userId : _this.loginUserId,
			        articleId :  articleId
			    },
			    data : {
			        columnCode : 3
			    },
			    success: function (result) {
			        _this.articleData = result.data;
			    },
			    fail : function (result) {
			        mui.toast(result.msg);
			    }
			});
			
		},
			
        share : function () {
            var msg = {
                href: utils.constants.pcUrl + 'doctemplateinfo.html?articleId=' + this.articleId,
                title: this.articleData.title,
                content: this.articleData.articleContent,
                thumbs:  [utils.constants.shareImgUrl],
                pictures: [utils.constants.shareImgUrl]
            };
            shareHref(msg);
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

