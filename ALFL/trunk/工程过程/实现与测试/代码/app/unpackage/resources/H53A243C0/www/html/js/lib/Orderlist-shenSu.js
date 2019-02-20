var vm = new Vue({
    el:'#app',
    data:{
        businessOrder : '',
        complainType : '未达成委托',
        complainContent : ''
    },
    mounted: function(){
        var _this = this;
        var urlParams = utils.getUrlParams();
        _this.businessOrder =  urlParams.businessOrder;
    },

    methods:{
        jinPai : function () {
            var _this = this;

            var loginUserInfo = utils.getLoginUserInfo(true);

            rest.post({
                url: 'order/complain',//140. 订单申诉;9.30
                businessOrder: _this.businessOrder,//当前订单的编号（必要）
                complainType: _this.complainType,//申诉的类型  （必要，字符型）
                complainContent: _this.complainContent,//申诉的内容
                success: function(result) {
                    mui.plusReady(function() {
                        var main = plus.webview.getWebviewById("Orderlist-shenSu.html");
                        //自定义事件,事件名为changeCity
                        mui.fire(main, 'yiyiCallback', {
                            result : result
                        });
                        //关闭子页面
                        mui.back();
                    });
                },
                fail : function (result) {
                    mui.toast(result.msg);
                }
            });
        }
    }
});
