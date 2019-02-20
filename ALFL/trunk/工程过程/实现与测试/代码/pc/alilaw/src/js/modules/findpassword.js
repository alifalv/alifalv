/*!
 * 找回密码
 * @author Li Hongwei
 * @date 2018-08-06 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    require('validator');
    var utils = require('utils');

    /**
     * 跳转步骤
     * @param num 步骤
     */
    function showFindPwdBoxByNum(num) {
        var _index = num-1;
        var $findpwd_tabs = $('.findpwd-tabs li');
        $findpwd_tabs.eq(_index).addClass('active');
        $findpwd_tabs.each(function(index) {
            if(_index != index) {
                $(this).removeClass('active');
            }
        });

        var $findpwd_box_li = $('.findpwd-box li');
        $findpwd_box_li.eq(_index).removeClass('hide');
        $findpwd_box_li.each(function(index) {
            if(_index != index) {
                $(this).addClass('hide');
            }
        });
    }

    utils.bindSendSms($('#sendsms_btn'), $('#mobile'));

    //第一步
    var $findpwd1_form = $('#findpwd1_form');
    var findpwd1_form_valid = $findpwd1_form.validator(constants.validator);
    findpwd1_form_valid.on('valid.form', function(e, form){
        showFindPwdBoxByNum(2);
    });

    //第二步
    var $findpwd2_form = $('#findpwd2_form');
    var findpwd2_form_valid = $findpwd2_form.validator(constants.validator);
    findpwd2_form_valid.on('valid.form', function(e, form){
        rest.post({
            url: 'user/findPassword',
            data: {
                mobile: $.trim($findpwd1_form.find('input[name="mobile"]').val()),
                code : $.trim($findpwd1_form.find('input[name="code"]').val()),
                newPassword : $.trim($findpwd2_form.find('input[name="newPassword"]').val())
            },
            success: function(result) {
                showFindPwdBoxByNum(3);
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

});
