new Vue({
    el:'#app',
    data:{
        list1List: [],//未使用
        list1IsNot : false,
        list1PageNo:1,
        list1PageSize:5,
        list1ListId : '#list1',
        userBalance : 0,
        money:0,
        payment : '微信支付',
        loginUserInfo : {}

    },
    mounted:function(){
        var
            that = this;

        var loginUserInfo = utils.getLoginUserInfo(true);
            that.loginUserInfo = loginUserInfo;
        that.userBalance = loginUserInfo.userBalance;

        mui(".tixianbtn").on('tap', function(){
            var txType = $(this).data('txtype');
            that.payment = txType;
            return false;
        });

    },

    methods: {

        tiXian : function () {
            var userInfo = utils.getLoginUserInfo(true);
            var that = this;
            rest.post({
                url: 'order/withdrawDeposit',
                data : {
                    userId: userInfo.userId,
                    money: that.money,//提现金额
                    payment: that.payment//提现方式【微信支付：银行卡支付】二种方式
                },
                success : function(result) {
                    mui.alert('',result.msg, function() {
                        history.back(-1);
                    });

                }
            });
        }
    }
});
