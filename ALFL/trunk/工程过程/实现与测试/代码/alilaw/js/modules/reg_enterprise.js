/*!
 * 企业会员注册
 * @author Li Hongwei
 * @date 2018-07-02 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    require('validator');
    var utils = require('utils');
    var constants = require('constants');
    require('thirdLogin');
    var urlParams = utils.getUrlParams();
    var data = {};
    if(urlParams.key && urlParams.type) {
        data = {
            openId : urlParams.key,
            openType : urlParams.type
        }
    }
    var $form = $('#form');

    utils.bindSendSms($('#sendsms_btn'), $('#mobile'), 'sms/sendReg/{mobile}/3');

    var valid_options = $.extend({}, constants.validator, {
        dataFilter: function(data) {
            if (data.code == 1) return "";
            else return data.msg;
        },
        fields: {
            '#userName' : '用户名:required;username;remote(' + constants.apiUrl + 'user/checkisRegister, userType=3)',
            '#mobile' : '手机号码:required;mobile;remote(' + constants.apiUrl + 'user/checkisRegister, userType=3)'
        }
    });
    var form_valid = $form.validator(valid_options);
    form_valid.on('valid.form', function(e, form){
        var _$form = $(form);
        data.equipmentType = 1;//设备类型   1 : pc 2 : android  3 : ios
        rest.post({
            url: 'user/companyRegister',
            form : _$form,
            data : data,
            success: function(result) {
                location.href = constants.viewUrl + 'reg_vip_enterprise.html?userId=' + result.data;
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });
});
