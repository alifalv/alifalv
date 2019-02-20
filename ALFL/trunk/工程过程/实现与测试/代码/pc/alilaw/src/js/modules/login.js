/*!
 * 首页
 * @author Li Hongwei
 * @date 2018-07-02 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    require('thirdLogin');
    require('validator');
    var utils = require('utils');
    var constants = require('constants');
    var store = require('store');
    var isSend = false;
    var $btn_login = $('#btn_login');

    initInputUserName();
    var login_form_valid = $('#login_form').validator(constants.validator);
    login_form_valid.on('valid.form', function(e, form){
        $btn_login.addClass('ui-btn-disabled').html('正在登录...');
        if(isSend) {
            return;
        }
        isSend = true;
        rest.post({
            form : $(form),
            url: 'user/login',
            async : false,
            data : {deviceId : 'pc'},
            success: function(result) {
                store.setCache('userInfo', result.data);
                store.setCache('token', result.data.token);

                //企业咨询者没有注册会员跳转到充值页面
                // if(result.data.userType == 3 && result.data.vipLevel == 0) {
                //     location.replace(constants.viewUrl + 'reg_vip_enterprise.html?userId=' + result.data.userId);
                //     return;
                // }
                rememberUserName();
                checkPay(result.data.userId, function (re) {
                    if(re.data && re.data.length > 0) {
                        setTimeout(function () {
                            window.close();
                        },200);
                        utils.jumpPay(re.data[0].businessOrder, function () {
                            C.alert.ok('开通成功！', function() {
                                location.replace(constants.viewUrl + 'personal_consult.html');
                            });
                        });
                    } else{
                        var urlParams = utils.getUrlParams();
                        if(urlParams.jump) {
                            location.href = urlParams.jump;
                        } else {
                            // location.href = constants.viewUrl + 'index.html';
                            location.href = constants.viewUrl + 'personal_consult.html';
                        }
                    }
                });
            },
            fail : function (result) {
                C.msg.fail(result.msg);
                $btn_login.removeClass('ui-btn-disabled').html('登 录');
                isSend = false;
            },
            error : function() {
                $btn_login.removeClass('ui-btn-disabled').html('登 录');
                isSend = false;
            }
        });
    });


    /**
     * 191.注册用户登陆后，检测有没有会员充值的订单，如果有就需要跳到支付页面；11.9
     * @param userId
     * @param func
     */
    function checkPay(userId, func) {

        rest.post({
            async : false,
            url: 'user/checkLoginIsPay',
            data : {userId : userId},
            success: function(result) {
                func && func(result);
            },
            fail : function (result) {
                C.msg.fail(result.msg);
                $btn_login.removeClass('ui-btn-disabled').html('登 录');
                isSend = false;
            },
            error : function() {
                $btn_login.removeClass('ui-btn-disabled').html('登 录');
                isSend = false;
            }
        });
    }

    function rememberUserName() {
        var prop = $('#remember_btn').prop('checked');
        if(prop) {
            store.setCache('username', $('#userName').val());
        }
    }

    function initInputUserName() {
        var username = store.getCache('username');
        if(null != username) {
            $('#userName').val(username);
        }
    }
});
