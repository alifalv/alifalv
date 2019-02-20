var vm = new Vue({
    el: '#app',
    data: {
        loginUserId: -1,
        userType: '', //用户类型
        articleData: {},
        articleId: '', 
        authorUserName: '', //发布文章的用户名 
        authorUserImg: '', //发布文章的用户头像
        authorUserId: '', //发布文章的用户id
        serviceName: '', //标题
        isNotAuthentication: '', //是否是认证律师
        pullId: '#pullrefresh',
        isNoList: false,
        list: [],
        pageNo: 1,
        pageSize: 5
    },
	filters:({
		formatDate1(time){
			// var time1 = time.split(" ")
			var time1 =  String(time) 
			var time2 =  time1.split(" ")
			return time2[0]
             // return time
		},
		hidename(na){
			 if(na){
				 var na1 = na.substr(0, 1)
				 return na1+"**"
			 } else {
				  return "匿名用户"
			 }
		}
	}),
	created() {
		// debugger
		if(localStorage.userInfo){
			var user = JSON.parse(localStorage.userInfo)
			this.userType = user.data.userType
		}
		
		
	},
    mounted: function() {
		// debugger
        var _this = this;
        var loginUserInfo = utils.getLoginUserInfo();
		if(loginUserInfo){
			 _this.isNotAuthentication = loginUserInfo.isAuthentication;
			if (loginUserInfo) {
			    _this.loginUserId = loginUserInfo.userId;
			}
		}
       
        var urlParams = utils.getUrlParams();
        _this.articleId = urlParams.articleId;

        rest.post({
            url: 'advice/adviceDetails/{adviceId}',
            urlParams: {
                adviceId: _this.articleId
            },
            data: {
                columnCode: 9
            },
            success: function(result) {
                if (result.data.imgs) {
                    result.data.imgs = result.data.imgs.split(',');
                }
                _this.articleData = result.data;

                _this.serviceName = result.data.title;
                _this.authorUserId = result.data.userId;
                _this.authorUserName = result.data.realName;
                _this.authorUserImg = result.data.userImg;

                mui.previewImage();

            }
        });




        mui.init({
            pullRefresh: {
                container: _this.pullId, //下拉刷新容器标识，querySelector能定位的css选择器均可，比如：id、.class等
                down: {
                    style: 'circle', //必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                    color: '#2BD009',
                    // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                    indicators: true, //是否显示滚动条
                    callback: _this.pullDownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
                },
                up: {
                    auto: true,
                    indicators: true, //是否显示滚动条
                    callback: _this.pullUpRefresh
                }
            }
        });



        mui("#link").on('tap', function() {
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });
    },

    methods: {

        setErrorImg: function(e) {
            $(e.target).attr('src', 'images/index/idx-defaluthead.png');
        },
        /**
         * 跳转解答页面
         */
        jieDa: function() {
            window.location.href = "Lawask-JieDa.html?articleId=" + this.articleId;
        },

        /**
         * 发布留言
         * 【百姓吐槽详情、阿里裁判详情 】咨询师对发布人的回帖
         * 【百姓吐槽详情、阿里裁判详情、案源详情、法律咨询详情】发布人对咨询师的回贴
         */
        commonReply: function(sendUserId, replyContent, replyId) {
            var _loginUserInfo = utils.getLoginUserInfo(true);
            var _that = this;
            rest.post({
                url: 'reply/commonReply', //
                data: {
                    replyContent: replyContent, //replyContent回复内容
                    upId: 0, //upId针对回复的类型，【0：如果是第一层的回复;1:针对回复的id】百姓吐槽 2：阿里裁决取replyId
                    userId: _loginUserInfo.userId, //登陆用户id
                    caseId: replyId, //replyId 咨询师解答的ID
                    businessType: 3, //业务类型【1：百姓吐槽 2：阿里裁决 3：法律咨询 4：案件竞拍 5：文书制作】
                    serviceName: _that.serviceName, //当前回复的文章标题
                    sendUserId: sendUserId, //针对人
                    sourceArticleId: _that.articleId //文章编号
                },
                success: function(result) {
                    _that.pullDownRefresh();
                }
            });
        },
        share: function() {
            var msg = {
                href: utils.constants.pcUrl + 'referee_info.html?articleId=' + this.articleId,
                title: this.articleData.title,
                content: this.articleData.caseDesc,
                thumbs: this.articleData.declareImgs,
                pictures: this.articleData.declareImgs
            };
            shareHref(msg);
        },
        star: function() {
            var _this = this;
            //文章收藏
            var loginUserInfo = utils.getLoginUserInfo(true);
            rest.post({
                url: 'article/saveArticleCollection/{userId}/{articleId}',
                urlParams: {
                    userId: loginUserInfo.userId,
                    articleId: _this.articleId
                },
                data: {
                    nickName: loginUserInfo.nickName
                },
                success: function(result) {
                    mui.toast(result.msg);
                    if (result.data.ok) {
                        _this.articleData.isCollection = (_this.articleData.isCollection == '是' ? '否' : '是');
                    } else {
                        _this.articleData.isCollection = (_this.articleData.isCollection == '是' ? '否' : '是');
                    }
                },
                fail: function(result) {
                    mui.toast(result.msg);
                }
            });
        },

        levelOneReply: function() {
            var _this = this;
            var btnArray = ['取消', '确定'];
            mui.prompt('请输入你的评语：', '', '评论', btnArray, function(e) {
                if (e.index == 1) {
                    _this.commonReply(_this.authorUserId, e.value, 0);
                }
            })
        },

        reply: function(userId, replyId) {
            var _this = this;
            var btnArray = ['取消', '确定'];
            mui.prompt('请输入你的评语：', '', '评论', btnArray, function(e) {
                if (e.index == 1) {
                    _this.commonReply(userId, e.value, replyId);
                }
            })
        },

        dianZan: function(replyId, index) {
            var _this = this;
            var _loginUserInfo = utils.getLoginUserInfo(true);
            rest.post({
                // url: 'article/saveArticleLike/{userId}/{articleId}',
                // urlParams: {
                //     userId: _loginUserInfo.userId,
                //     articleId: _this.articleId
                // },
                url: 'advice/replyLike/{replyId}',
                urlParams: {
                    replyId: replyId
                },
                data: {
                    loginUserId: _loginUserInfo.userId
                },
                success: function(result) {
                    mui.toast(result.msg);
                    _this.list[index].likeNum = result.data.likeNum;
                    // _this.articleData.likeNum = parseInt(_this.articleData.likeNum) + parseInt(result.data.likeNum);
                },
                fail: function(result) {
                    mui.toast(result.msg);
                }
            });
        },

        daShang: function(replyId) {
            var _this = this;
            var _loginUserInfo = utils.getLoginUserInfo(true);

            var _$this = $(this);
            rest.post({
                url: 'order/gotoReward',
                async: false,
                data: {
                    title: _this.serviceName, //文章标题
                    userId: _loginUserInfo.userId, //发起人的userID
                    favoreeUserId: _this.authorUserId, //被打赏用户的id
                    connectionId: _this.articleId, //文章id
                    businessType: '法律咨询', //业务类型 【传入中文】{"百姓吐槽","法律疑难问题与观点","公务员招聘","常用文书模板","文书制作知识","法律人信息","常见咨询问题","法律培训","法律法规","阿里裁决","我的风采","工作动态","成功案例","我的随笔","法律咨询","案件委托"};
                    currentId: replyId //针对打赏数据的Id（【法律咨询】 咨询解答这一行的Id【阿里裁决】 阿里裁决的Id；【百姓吐槽】 百姓吐槽的Id）
                },

                // url: 'order/gotoReward',
                // async: false,
                // data: {
                //     title: serviceName, //文章标题
                //     userId: _loginUserInfo.userId, //发起人的userID
                //     favoreeUserId: favoreeUserId, //被打赏用户的id
                //     connectionId: adviceId, //文章id
                //     businessType: '法律咨询', //业务类型 【传入中文】{"百姓吐槽","法律疑难问题与观点","公务员招聘","常用文书模板","文书制作知识","法律人信息","常见咨询问题","法律培训","法律法规","阿里裁决","我的风采","工作动态","成功案例","我的随笔","法律咨询","案件委托"};
                //     currentId: replyid //针对打赏数据的Id（【法律咨询】 咨询解答这一行的Id【阿里裁决】 阿里裁决的Id；【百姓吐槽】 百姓吐槽的Id）
                // },
                success: function(result) {
                    creatMywebview('Order-pay.html?businessOrder=' + result.data.businessOrder, 'Order-pay.html?businessOrder=' + result.data.businessOrder);

                    // var index = C.alert.tips('正在支付中', function () {});
                    // utils.jumpPay(result.data.businessOrder, function () {
                    //     C.msg.ok('打赏成功');
                    //     layer.close(index);
                    // });
                },
                fail: function(result) {
                    // C.msg.fail(result.msg);
                }
            });

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
                url: 'advice/adviceReplyList/{relativeId}/{businessType}/{pageNo}/{pageSize}',
                urlParams: {
                    relativeId: _that.articleId, //文章编号
                    businessType: 2, //业务类型【1：百姓吐槽 2：阿里裁决 3：法律咨询 4：案件竞拍 5：文书制作】
                    pageNo: _that.pageNo,
                    pageSize: _that.pageSize
                },
                success: function(result) {
                    func(result)
                },
                fail: function(result) {}
            });
        }
    },
	 
	 
});