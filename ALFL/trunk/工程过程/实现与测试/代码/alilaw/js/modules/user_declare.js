/*!
 * 我要申报
 * @author Li Hongwei
 * @date 2018-07-14 15:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var utils = require('utils');
    var loginUserInfo = utils.getLoginUserInfo(true);

    $('#realName').val(loginUserInfo.nickName || loginUserInfo.userName);

    var constants = require('constants');

    require('validator');
    require(['zeroclipboard', 'ueditor.ext'], function(zeroclipboard) {
        window.ZeroClipboard = zeroclipboard;
        var ue = UE.getEditor('caseDesc');
        ue.ready(function() { ue.setHeight(200); });
    });


    require('fileUpload');
    //法律意见书
    $("#bookName").createFileUpload({
        formData: {
            "name": 'bookName'
        },
        imageNum: 1,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    require('imgUpload');
    //判决书图片
    $("#declareImgs").createImgUpload({
        formData: {
            "name": 'declareImgs'
        },
        imageNum: 50,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    证据图片
    $("#evidenceImgs").createImgUpload({
        formData: {
            "name": 'evidenceImgs'
        },
        imageNum: 50,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    //主题图片
    $("#primaryImg").createImgUpload({
        formData: {
            "name": 'primaryImg'
        },
        imageNum: 1,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    var $form = $('#form');
    var login_form_valid = $form.validator(constants.validator);
    login_form_valid.on('valid.form', function(e, form) {
        var loginUserInfo = utils.getLoginUserInfo(true);
        $form.validator('holdSubmit');
        rest.post({
            form: $(form),
            url: 'article/aliDeclare',
            data: {
                userId: loginUserInfo.userId,
                isofficial: 1 //是否官方【0: 官方 1：用户】
            },
            success: function(result) {
                // C.alert.ok(result.msg, function() {
                location.replace(constants.viewUrl + 'declare_success.html');
                // })
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            },
            error: function() {
                C.msg.fail('连接失败');
            },
            complete: function() {
                $form.validator('holdSubmit', false);
            }
        });
    });
});