new Vue({
    el:'#app',
    data:{

    },
    mounted: function () {

    },
    methods: {


        submitApply : function() {
            var loginUserInfo = utils.getLoginUserInfo(true);
            rest.post({
                url: 'user/freeCaseDeclare',
                data : {
                    userId : loginUserInfo.userId
                },
                success: function (result) {
                    mui.alert('','您的申请已经提交完成，平台将尽快委派律师与您联系！', function() {
                        history.back(-1);
                    });
                },
                fail: function (result) {
                    mui.toast(result.msg);
                },
                complete :function () {
                }
            });
        }
    }
});
