//常用文书模板
var vm = new Vue({
    el: '#app',
    data: {
        totalPrice: '',
        orderPrice: '',
        userName: '',
		userPhone:'',
		mobile:"",
        businessType: '',
        businessOrder: '',
        isChangePrice: false,
        orderInfo: {}
    },
    mounted: function() {
        var that = this;
        var urlParams = utils.getUrlParams();
        that.businessOrder = urlParams.businessOrder;

 

        rest.post({
            url: 'order/findOrderDetails',
            data: {  
                businessOrder: that.businessOrder //  当前订单的编号
            },
            success: function(result) {
                that.totalPrice = result.data.totalPrice;
                that.orderPrice = result.data.totalPrice;
                that.userName = result.data.userName;
                that.businessType = result.data.businessType;
                that.orderInfo = result.data;
                //TODO 判断过来的页面是casebiddinginfo.html
                if (window.opener && window.opener.location.href.indexOf('casebiddinginfo.html') > -1) {
                    that.isChangePrice = true;
                }

            },
            fail: function(result) {
                mui.toast(result.msg)
            }
        });
    },

    methods: {

        changePrice: function() {
            this.totalPrice = this.orderPrice;
        },

        pay: function() {
            this.weChatPay(this.businessOrder, this.totalPrice, this.userName,this.mobile);
        },

        weChatPay: function(businessOrder, totalPrice, userName,mobile) {
            var _this = this;
            rest.post({
                url: 'order/gotoPay', //164.订单支付详情页面，发起支付（还没有完成，需要与支付接口调用 。）;10.14
                data: {
                    businessOrder: businessOrder, //  当前订单的编号
                    orderPrice: this.totalPrice, //金额
                    userName: userName, //用户真实姓名；
                    mobile: this.mobile, //用户真实姓名；
                    payment: '微信支付', //微信支付:银行卡支付
                    wxPayWay: 'H5'
                },
                success: function(result) {
                    alert(result.data);
                    _this.orderPrice = result.data;
                    rest.get({
                        baseUrl: null,
                        url: result.data, //164.订单支付详情页面，发起支付（还没有完成，需要与支付接口调用 。）;10.14
                        success: function(result) {
                            mui.openWindow({
                                url: result.data,
                                id: 'payurl',
                                success: function(result) {
                                    alert(result);
                                },
                                styles: {
                                    additionalHttpHeaders: {
                                        referer: "http://www.alifalv.cn"
                                    }
                                },
                                waiting: {
                                    autoShow: true, //自动显示等待框，默认为true
                                    title: '正在加载支付页面......' //等待对话框上显示的提示内容
                                }
                            })
                        },
                        fail: function(result) {
                            mui.toast(result.msg);
                        }
                    });
                },
                fail: function(result) {
                    mui.toast(result.msg);
                }
            });
        }
    }
});