var vm = new Vue({
    el: '#app',
    data: {
        caseId: '',
        authorUserId: '',
        thinking: '',
        offerMoney: '',
        effect: '',
        mobile: ''
    },
    mounted: function() {
        var _this = this;
        var urlParams = utils.getUrlParams();
        _this.caseId = urlParams.caseId;
        _this.authorUserId = urlParams.authorUserId;
    },

    methods: {
        jinPai: function() {
            var _this = this;

            var loginUserInfo = utils.getLoginUserInfo(true);
            if (loginUserInfo.userType != '2') {
                mui.toast('只有咨询师才可以竞拍');
                return false;
            }
            rest.post({
                url: 'case/caseReply/{caseId}/{userId}', //【案源详情的竞拍】-咨询师发起竞拍
                urlParams: {
                    caseId: _this.caseId,
                    userId: loginUserInfo.userId
                },
                data: {
                    sendPerson: _this.authorUserId, //发帖人的id
                    thinking: _this.thinking,
                    offerMoney: _this.offerMoney,
                    effect: _this.effect,
                    mobile: _this.mobile
                },
                success: function(result) {
                    mui.plusReady(function() {
                        var main = plus.webview.getWebviewById("Lawbuy-Info.html");
                        //自定义事件,事件名为changeCity
                        mui.fire(main, 'jingPaiCallback', {

                        });
                        //关闭子页面
                        mui.back();
                    });
                }
            });
        }
    }
});