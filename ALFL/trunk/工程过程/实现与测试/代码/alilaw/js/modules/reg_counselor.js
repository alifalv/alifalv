/*!
 * 律师注册
 * @author Li Hongwei
 * @date 2018-07-02 11:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";

    require('validator');
    var constants = require('constants');
    var dataList = require('dataList');
    var utils = require('utils');
    require('thirdLogin');

    var urlParams = utils.getUrlParams();
    var data = {};
    if (urlParams.key && urlParams.type) {
        data = {
            openId: urlParams.key,
            openType: urlParams.type
        }
    }

    var $form = $('#form');

    dataList.getJobList(function(result) {
        C.template('#job_tmpl').renderReplace(result, function() {});
    });

    dataList.getProvinceList(function(result) {
        C.template('#province_tmpl').renderReplace(result, function() {
            $('#province_select').trigger('change');
        });
    });

    dataList.getCaseList(function(result) {
        C.template('#case_tmpl').renderReplace(result, function() {});
    });

    $('#province_select').on('change', function() {
        rest.post({
            url: 'address/cityListByProvince/{pid}',
            urlParams: {
                pid: $('#province_select').val()
            },
            success: function(result) {
                C.template('#city_tmpl').renderReplace(result, function() {});
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    });
    utils.bindSendSms($('#sendsms_btn'), $('#mobile'), 'sms/sendReg/{mobile}/2');

    var valid_options = $.extend({}, constants.validator, {
        dataFilter: function(data) {
            if (data.code == 1) return "";
            else return data.msg;
        },
        fields: {
            '#userName': '用户名:required;username;remote(' + constants.apiUrl + 'user/checkisRegister, userType=2)',
            '#mobile': '手机号码:required;mobile;remote(' + constants.apiUrl + 'user/checkisRegister, userType=3)',
            '#nickName': '昵称:required;length(~18);checkNickName'
        },
        rules: {
            checkNickName: function(element) {
                return rest.post({
                    url: 'user/checkisRegister',
                    data: {
                        nickName: element.value,
                        userType: 2
                    },
                    success: function(result) {},
                    fail: function() {}
                });
            }
        }
    });
    var form_valid = $form.validator(valid_options);
    form_valid.on('valid.form', function(e, form) {
        var _$form = $(form);
        var _cases = '';
        _$form.find('input[name="cases"]:checked').each(function(i, e) {
            if (i === 0) {
                _cases += ('cases=' + $(this).val());
            } else {
                _cases += ('&cases=' + $(this).val());
            }
        });

        data.equipmentType = 1; //设备类型   1 : pc 2 : android  3 : ios
        rest.post({
            url: 'user/counselorRegister?' + _cases,
            form: _$form,
            data: data,
            success: function(result) {
                location.replace(constants.viewUrl + 'reg_counselor_success.html');
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    });
});