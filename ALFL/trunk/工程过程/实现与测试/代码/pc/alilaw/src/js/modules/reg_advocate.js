/*!
 * 咨询者注册
 * @author Li Hongwei
 * @date 2018-07-02 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    require('validator');
    var constants = require('constants');
    var utils = require('utils');
    var urlParams = utils.getUrlParams();

    var data = {};
    if(urlParams.key && urlParams.type) {
        data = {
            openId : urlParams.key,
            openType : urlParams.type
        }
    }

    var $reg_form = $('#reg_form');

    utils.bindSendSms($('#sendsms_btn'), $('#mobile'), 'sms/sendReg/{mobile}/1');


    var valid_options = $.extend({}, constants.validator, {
        dataFilter: function(data) {
            if (data.code == 1) return "";
            else return data.msg;
        },
        fields: {
            '#userName' : '用户名:required;username;remote(' + constants.apiUrl + 'user/checkisRegister, userType=1)',
            '#mobile' : '手机号码:required;mobile;remote(' + constants.apiUrl + 'user/checkisRegister, userType=1)'
        }
    });

    var form_valid = $reg_form.validator(valid_options);
    form_valid.on('valid.form', function(e, form){
        var _$form = $(form);
        data.age = 0;
        data.equipmentType = 1;
        rest.post({
            form : _$form,
            url: 'user/consultantRegister',
            data : data,
            success: function(result) {
                location.href = constants.viewUrl + 'reg_vip_advocate.html?userId=' + result.data;
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });
});
