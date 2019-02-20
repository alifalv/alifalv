var vm = new Vue({
    el: '#app',
    data: {
        replyContent: '',
        articleId: '',
    },
    mounted: function() {
        var _this = this;
        var loginUserInfo = utils.getLoginUserInfo();
        if (loginUserInfo) {
            _this.loginUserId = loginUserInfo.userId;
        }
        var urlParams = utils.getUrlParams();
        _this.articleId = urlParams.articleId;
    },

    methods: {
        //发布按钮
        jieDa: function() {
            var _loginUserInfo = utils.getLoginUserInfo(true);
            rest.post({
                url: 'advice/sendReply/{replyId}', //
                urlParams: {
                    replyId: this.articleId
                },
                data: {
                    //表单中取 replyContent回复内容
                    replyContent: this.replyContent,
                    loginUserId: _loginUserInfo.userId //登陆用户id
                },
                success: function(result) {
                    window.location.href = "Lawask-Info.html?articleId=" + this.articleId;
                }
            });

        },



    }
});