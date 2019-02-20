var vm = new Vue({
    el: '#app',
    data: {
        loginUserId: -1,
        articleData: {},
        caseId: '',
        authorUserName: '', //发布文章的用户名
        authorUserImg: '', //发布文章的用户头像
        authorUserId: '', //发布文章的用户id
        serviceName: '', //标题
        caseState: '',
        isShowJingPaiBtn: false,
        isAuthenticationUser: '', //认证律师
        userType: '',

        pullId: '#pullrefresh',
        isNoList: false,
        list: [],
        pageNo: 1,
        pageSize: 5
    },
	filters:({
		formatDate(time){
			var time1 = time.split(" ")
			return time1[0]
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
    mounted: function() {
        var _this = this;
        var loginUserInfo = utils.getLoginUserInfo();
		if(loginUserInfo){
			_this.isAuthenticationUser = loginUserInfo.isAuthentication;
			_this.userType = loginUserInfo.userType;
			if (loginUserInfo) {
			    _this.loginUserId = loginUserInfo.userId;
			}
		}
        
        var urlParams = utils.getUrlParams();
        _this.caseId = urlParams.caseId;

        rest.post({
            url: 'case/caseDetails/{caseId}',
            urlParams: {
                caseId: _this.caseId
            },
            success: function(result) {
                if (result.data.caseImgs) {
                    result.data.caseImgs = result.data.caseImgs.split(',');
                }
                _this.articleData = result.data;

                _this.serviceName = result.data.title;
                _this.authorUserId = result.data.userId;
                _this.authorUserName = result.data.realName;
                _this.authorUserImg = result.data.userImg;
                _this.caseState = result.data.caseState;

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

        //竞拍结果回掉
        window.addEventListener("jingPaiCallback", function(e) {
            _this.pullDownRefresh();
            mui.toast('竞拍已提交');
        });


        //只限除自己以外的咨询师竞拍
        if (null == loginUserInfo || (loginUserInfo.userType == '2' && loginUserInfo.userId != _this.authorUserId)) {

            //非竞拍状态时，对所有人屏蔽竞拍功能
            if ('0' == _this.caseState) {
                _this.isShowJingPaiBtn = true;
            }
        }
    },

    methods: {

        setErrorImg: function(e) {
            $(e.target).attr('src', 'images/index/idx-defaluthead.png');
        },

        /**
         * 发布留言
         * 【百姓吐槽详情、阿里裁判详情 】咨询师对发布人的回帖
         * 【百姓吐槽详情、阿里裁判详情、案源详情、法律咨询详情】发布人对咨询师的回贴
         */
        commonReply: function(sendUserId, replyContent, upId) {
            var _loginUserInfo = utils.getLoginUserInfo(true);
            var _that = this;
            rest.post({
                url: 'reply/commonReply',
                data: {
                    replyContent: replyContent, //replyContent回复内容
                    upId: upId, //upId针对回复的类型，【0：如果是第一层的回复;1:针对回复的id】百姓吐槽 2：阿里裁决取replyId
                    userId: _loginUserInfo.userId, //登陆用户id
                    caseId: _that.caseId, //文章的Id
                    businesstype: 2, //业务类型【1：百姓吐槽 2：阿里裁决 3：法律咨询 4：案件竞拍 5：文书制作】
                    serviceName: _that.serviceName, //当前回复的文章标题
                    sendUserId: sendUserId, //针对人
                    sourceArticleId: _that.articleId //文章编号
                },
                success: function(result) {
                    _that.pullDownRefresh();
                }
            });
        },

        levelOneReply: function() {
            var href = 'Lawbuy-JingPai.html?caseId=' + this.caseId + '&authorUserId=' + this.authorUserId;
            creatMywebview(href, href);
        },

        pinQing: function() {
            mui.toast('建设中');
            //TODO
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
            // var _loginUserInfo = utils.getLoginUserInfo(true);
            var _loginUserInfo = utils.getLoginUserInfo();

            var _that = this;
            rest.post({
                url: 'case/offerList/{caseId}/{sendCaseUserId}/{loginUserId}/{pageNo}/{pageSize}',
                urlParams: {
                    caseId: _that.caseId, //文章编号
                    sendCaseUserId: _that.authorUserId, //发帖人的id
                    loginUserId: _loginUserInfo.userId, //当前登陆人的Id
                    pageNo: _that.pageNo,
                    pageSize: _that.pageSize
                },
                success: function(result) {
                    func(result)
                },
                fail: function(result) {}
            });
        },

        // 我要竞拍跳转
        jinPai: function() {
            var _that = this;
            window.location.href = 'Lawbuy-JingPai.html?caseId=' + _that.caseId + '&authorUserId=' + _that.authorUserId;
        }
    }
});