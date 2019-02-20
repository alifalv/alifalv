/*!
 * 发布咨询问题
 * @author Li Hongwei
 * @date 2018-07-14 15:12
 * @version 1.0
 */


define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    var userInfo = utils.getLoginUserInfo(true);
    require('validator');

    if(null == userInfo.freeNum && 0 == userInfo.freeNum) {
        $('#reward option[value="0"]').remove();
    }


    require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
        window.ZeroClipboard = zeroclipboard;
        var ue = UE.getEditor('adviceContent');
        ue.ready(function() {ue.setHeight(200);});
    });


    var dataList = require('dataList');
    dataList.getCaseList(function(result) {
        C.template('#case_tmpl').renderReplace(result, function() {});
    });

    require('imgUpload');
    $("#file").createImgUpload({
        formData: {
            // "path": "artScene/",
            "name": 'imgs'
        },
        imageNum : 3,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    var $formOpen = $('#form');
    var form_valid = $formOpen.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        var loginUserInfo = utils.getLoginUserInfo(true);

        var _$form = $(form);
        $formOpen.validator('holdSubmit');
        _$form.find('button[type="submit"]').prop('disabled', true).addClass('ui-btn-gray').removeClass('ui-btn-red').html('发布中');
        rest.post({
            url: 'advice/sendAdvice',
            async : false,
            data : {
                userId : userInfo.userId,
                isofficial : 1//是否属于官方发布 1用户 0 官方
            },
            form : _$form,
            success: function(result) {
                if(_$form.find('#reward').val() == 0 || result.data.businessOrder == '') {
                    location.replace(constants.viewUrl + 'user_consult_success.html?articleId=' + result.data.adviceId);
                } else {
                    utils.jumpPay(result.data.businessOrder, function () {
                        location.replace('user_consult_success.html?articleId=' + result.data.adviceId)
                    });
                }
            },
            fail : function (result) {
                C.msg.fail(result.msg);
                $formOpen.validator('holdSubmit', false);
                _$form.find('button[type="submit"]').prop('disabled', false).removeClass('ui-btn-gray').addClass('ui-btn-red').html('发布');

            },
            error : function() {
                $formOpen.validator('holdSubmit', false);
                _$form.find('button[type="submit"]').prop('disabled', false).removeClass('ui-btn-gray').addClass('ui-btn-red').html('发布');

            }

    });
    });

});

