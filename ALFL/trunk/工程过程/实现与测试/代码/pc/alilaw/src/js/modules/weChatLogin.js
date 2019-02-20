/*!
 * 微信支付跳转
 * @author Li Hongwei
 * @date 2018-08-05 15:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var store = require('store');

    checkThirdAuthorization(urlParams.key, 'WECHAT');


    function checkThirdAuthorization(openId, openType) {
        var index = layer.load(); //又换了种风格，并且设定最长等待10秒
        rest.post({
            url: 'user/getThreeLoginFlag',
            data : {
                openId  : openId,
                openType : openType
            },
            success: function (result) {
                store.setCache('userInfo', result.data);
                // 绑定了用户，返回登录成功的用户信息，返回结果等同于登录成功

                if(window.opener) {
                    window.opener.location.replace(constants.viewUrl + 'personal_consult.html');
                    window.close();
                } else {
                    window.location.replace(constants.viewUrl + 'personal_consult.html');
                }
            },
            fail: function (result) {
                if(window.opener) {
                    window.opener.location.replace(constants.viewUrl + 'reg_bind.html?key=' + openId + '&type=' + openType);
                    window.close();
                } else {
                    window.location.replace(constants.viewUrl + 'reg_bind.html?key=' + openId + '&type=' + openType);
                }
            },
            complete : function () {
                layer.close(index);
            }

        });
    }

});
