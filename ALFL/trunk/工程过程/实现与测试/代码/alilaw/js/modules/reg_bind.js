/*!
 * 用户绑定
 * @author Li Hongwei
 * @date 2018-08-22 22:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";

    var utils = require('utils');
    var constants = require('constants');
    var store = require('store');
    var urlParams = utils.getUrlParams();
    require('validator');

    var data = {
        key: urlParams.key,
        type: urlParams.type
    };
    C.template('#regBoxTmpl').renderReplace(data, function($html) {});

    var login_form_valid = $('#bind_form').validator(constants.validator);
    login_form_valid.on('valid.form', function(e, form) {
        rest.post({
            form: $(form),
            url: 'user/login',
            data: {
                deviceId: 'pc',
                openId: urlParams.key,
                openType: urlParams.type,
                accountNo: urlParams.accountNo
            },
            success: function(result) {
                store.setCache('userInfo', result.data);
                store.setCache('token', result.data.token);
                var utils = require('utils');
                var urlParams = utils.getUrlParams();
                if (urlParams.jump) {
                    location.href = urlParams.jump;
                } else {
                    location.href = 'index.html';
                }
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    });
});