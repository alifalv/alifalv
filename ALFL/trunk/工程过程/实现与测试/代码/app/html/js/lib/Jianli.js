var vm = new Vue({
    el:'#app',
    data:{
        jianliUserId : null,
        resumeId:'',
        articleData: {},
        isShowStar: false,
        isStared : false,
        collectionId : ''
    },
    mounted :function(){
        var _this = this;
        var loginUserInfo = utils.getLoginUserInfo();
        var urlParams = utils.getUrlParams();
        _this.jianliUserId =  urlParams.userId;


            rest.post({
            url: 'user/getResumeInfo/{userId}',
            urlParams : {
                userId : _this.jianliUserId
            },
            success: function (result) {
                _this.resumeId = result.data.resumeId;
                _this.articleData = result.data;

                //自己 && 咨询者 无法收藏
                if((loginUserInfo && loginUserInfo.userId == _this.loginUserId) || (loginUserInfo && loginUserInfo.userType == '1')) {
                    _this.isShowStar = false;
                } else {
                    _this.isShowStar = true;
                    _this.seacherisCollectionResume()
                }
            },
            fail : function (result) {
                mui.toast(result.msg);
            }
        });
    },

    methods:{

        starJianLi : function () {
            var _that = this;
            var loginUserInfo = utils.getLoginUserInfo(true);
            if(_that.isStared) {
                _that.deleteCollectionResumeList(function() {
                    _that.seacherisCollectionResume();
                    mui.toast('取消收藏');
                });
            } else {

                _that.collectionResume(loginUserInfo.userId, function() {
                    _that.seacherisCollectionResume();
                    mui.toast('收藏成功');
                });
            }
        },

            setErrorImg : function(e) {
                $(e.target).attr('src', 'images/index/idx-defaluthead.png');
            },

            /**
             * 查询是否收藏简历
             * @param resumeId
             */
             seacherisCollectionResume : function(func) {
                 var _that = this;
                var _loginUserInfo = utils.getLoginUserInfo();
                var userId= '-1';
                if(_loginUserInfo) {
                    userId = _loginUserInfo.userId;
                }
                rest.post({
                    url: 'user/seacherisCollectionResume',
                    data : {
                        userId :userId, //用户编号  （如果是是游客你自己生成一个userid）不能不传，
                        resumeId : _that.resumeId//简历ID
                    },
                    success: function (result) {
                        if(result.data && result.data.collectionId != '0') {
                            _that.isStared  = true;
                            _that.collectionId = result.data.collectionId;
                        } else {
                            _that.isStared  = false;
                        }
                        func && func();
                    }
                });
            }


,

        /**
         * 134.企业删除收藏的简历；10.15修改
         * @param func
         */
         deleteCollectionResumeList : function(func) {
            var _that = this;

            rest.post({
        url: 'user/deleteCollectionResumeList',//134.企业删除收藏的简历
        data : {
            collectionId: _that.collectionId
        },
        success: function(result) {
            func && func(result);
        },
        fail : function (result) {
            mui.toast(result.msg);
        }
    });
},



        /**
         * 122.企业收藏简历；9.21
         * @param userId
         * @param resumeId
         * @param func
         */
         collectionResume : function(userId, func) {
             var _that = this;
    rest.post({
        url: 'user/collectionResume',
        data : {
            userId: userId,// 咨询师id（不可空）
            resumeId :  _that.resumeId//简历ID（不可空）
        },
        success: function(result) {
            func && func(result);
        },
        fail : function (result) {
            mui.toast(result.msg);
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

