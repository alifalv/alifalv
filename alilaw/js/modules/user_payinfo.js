/*!
 * 支付详情
 * @author Li Hongwei
 * @date 2018-10-10 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    require('validator');

    var urlParams = utils.getUrlParams();
    if(!urlParams.businessOrder) {
        utils.jump404('订单编号为空');
    }

    var totalPrice;
    var businessType;

    //查询订单信息
    rest.post({
        url: 'order/findOrderDetails',
        data : {
            businessOrder: urlParams.businessOrder//  当前订单的编号
        },
        success: function(result) {
        	var loginUserInfo = utils.getLoginUserInfo();
            totalPrice = result.data.totalPrice;
            businessType = result.data.businessType;
            var isChangePrice = false;//金额是否能改

            if(window.opener && window.opener.location.href.indexOf('casebiddinginfo.html') > -1) {
                isChangePrice = true;
            }
            result.isChangePrice = isChangePrice;
            C.template('#orderTmpl').renderReplace(result, function($html) {
                initPayForm($html);
            });
            if(null!=loginUserInfo.userName){
            	$("#userName").val(loginUserInfo.realName);
            }
        },
        fail : function (result) {
            C.msg.fail(result.msg);
        }
    });



    var t1;
    function initPayForm($html) {

        //取消订单
        $html.on('click', '.j-orderclose-btn', function() {
            C.confirm('取消订单？', function () {
                var loginUserInfo = utils.getLoginUserInfo();
                rest.post({
                    url: 'order/cancelOrder',
                    data : {
                        businessOrder: urlParams.businessOrder,//当前订单的编号
                        userId: loginUserInfo.userId//文书申请人的userId
                    },
                    success: function(result) {
                        C.alert.ok(result.msg, function () {
                            if(window.opener) {
                                location.replace(constants.viewUrl + 'personal_info.html');
                                window.opener.close();
                                window.close();
                            } else {
                                location.replace(constants.viewUrl + 'personal_info.html')
                            }
                        });
                    },
                    fail : function (result) {
                        C.msg.fail(result.msg);
                    }
                });
            });
            return false;
        });

        var $formOpen = $('#form');
       
        //注册页面
        var formOpen_valid = $formOpen.validator(constants.validator);
        formOpen_valid.on('valid.form', function(e, form){
            $formOpen.validator('holdSubmit');

            var totalPrice = $formOpen.find('[name="totalPrice"]').val();
            if(isNaN(totalPrice)) {
                C.msg.fail('实付款金额有误');
                $formOpen.validator('holdSubmit', false);
                return false;
            }

            //个人咨询开通会员
            rest.post({
                url: 'order/gotoPay',//164.订单支付详情页面，发起支付（还没有完成，需要与支付接口调用 。）;10.14
                data : {
                    businessOrder: urlParams.businessOrder,//  当前订单的编号
                    orderPrice: $formOpen.find('[name="totalPrice"]').val(),//金额
                    userName: $formOpen.find('[name="userName"]').val(),//用户真实姓名；
                    payment: '微信支付',//微信支付:银行卡支付
                    wxPayWay : 'QCODE'
                },
                success: function(result) {
                    var data = {
                        businessOrder: urlParams.businessOrder,//  当前订单的编号
                        totalPrice : $formOpen.find('[name="totalPrice"]').val(),
                        userName: $formOpen.find('[name="userName"]').val(),//用户真实姓名；
                        businessType : businessType,
                        nowTime : utils.getNowDateStr()
                    };
                    C.template('#qrcodeInfoTmpl').renderReplace(data, function() {
                        $('#infoBox').addClass('ui-hide');
                        $('#qrcodeBox').removeClass('ui-hide');
                        require('qrcode');

                        var qrcode = new QRCode(document.getElementById("qrcode"), {
                            width: 130,
                            height: 130
                        });

                        $('#qrcodeload').html('');
                        qrcode.makeCode(result.data);
                        //getStatus();
                        t1 = window.setInterval(getStatus,3300); 
                        //t1 = window.setTimeout(getStatus,3300)
                       
                    });
                },
                fail : function (result) {
                    C.msg.fail(result.msg);
                },
                complete : function() {
                    $formOpen.validator('holdSubmit', false);
                }
            });
        });


        if($html.find('#orderPrice').length > 0) {
            $html.on('input', '#orderPrice', function () {
                var totalPrice = $(this).val();
                if(!isNaN(totalPrice)) {
                    $formOpen.find('[name="totalPrice"]').val(totalPrice);
                    $formOpen.find('.j-totalPrice-txt').html(totalPrice);
                }
                return false;
            });
        }
    }
    function getStatus(){
    	$.ajax({
			url:'/ali-legal/api/order/getRequestPayOrderStatus',
			type:'post',
			data:{"orderNum":urlParams.businessOrder},
			dataType:"json",
			success:function(data){
				//alert(data.requestPayOrderStatus);
				if(data.requestPayOrderStatus=='1'){
					window.clearInterval(t1); 
					$('#payok_btn').click();
				}
			}
		});
    }

    function setUserName(){
    	var username=loginUserInfo.userName;
    	if(username==null){
    		var name=$("#userName").val();
    		$.ajax({
    			url:'/ali-legal/api/user/UpdateUserNameAtPay',
    			type:'post',
    			data:{"userName":name,"userId":loginUserInfo.userId,"userType":loginUserInfo.userType},
    			dataType:"json",
    			success:function(data){
    				//Consolo.log(data);
    			}
    		});
    	}
    }
    $('#payok_btn').on('click', function() {
        rest.post({
            url: 'pay/wx/queryOrder/{businessOrder}',
            urlParams : {
                businessOrder: urlParams.businessOrder//  当前订单的编号
            },
            success: function(result) {
                //是否是聘请页面过来的
                if(window.opener && window.opener.location.href.indexOf('casebiddinginfo.html') > -1) {
                    C.alert.tips('请返回竞拍详情页面，再次点击聘请按钮，完成聘请',function () {
                        if(window.opener) {
                            window.opener.paySuccess && window.opener.paySuccess();
                            window.close();
                        } else {
                            window.location.replace(constants.viewUrl + 'personal_order.html');
                        }
                    });
                } else {
                    if(window.opener) {
                        window.opener.paySuccess && window.opener.paySuccess();
                        window.close();
                    } else {
                        window.location.replace(constants.viewUrl + 'personal_order.html');
                    }
                }
            },
            fail : function (result) {
                C.msg.fail(result.msg || result.error);
            }
        });
    });


});
