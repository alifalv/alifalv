/*!
 * 发布个人咨询问题
 * @author Li Hongwei
 * @date 2018-07-14 15:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";

    var constants = require('constants');
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var userId = urlParams.userId;
    if (userId == null || userId == '') {
        utils.jump404('用户编号为空');
    }

    var userInfo = utils.getLoginUserInfo(true);
    if (null == userInfo.freeNum && 0 == userInfo.freeNum) {
        $('#reward option[value="0"]').remove();
    }

    require('validator');

    require(['zeroclipboard', 'ueditor.ext'], function(zeroclipboard) {
        window.ZeroClipboard = zeroclipboard;
        var ue = UE.getEditor('adviceContent');
        ue.ready(function() { ue.setHeight(200); });
    });


    //查询律师信息
    rest.post({
        url: 'user/userInfo/{userId}',
        urlParams: {
            userId: userId
        },
        success: function(result) {
            $('#lawyerName').html(result.data.userName + ''); //TODO 职业类型
        },
        fail: function(result) {
            C.msg.fail(result.msg);
        }
    });

    //初始化问题类型下拉菜单
    var dataList = require('dataList');
    dataList.getCaseList(function(result) {
        C.template('#case_tmpl').renderReplace(result, function() {});
    });

    //初始化图片上传控件
    require('imgUpload');
    $("#file").createImgUpload({
        formData: {
            "name": 'imgs'
        },
        imageNum: 3,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    //免费咨询
    var $formOpen = $('#form');
    var form_valid = $formOpen.validator(constants.validator);
    form_valid.on('valid.form', function(e, form) {
        var loginUserInfo = utils.getLoginUserInfo(true);
        $formOpen.validator('holdSubmit');
        var _$form = $(form);
        rest.post({
            url: 'advice/sendAdvice',
            async: false,
            data: {
                userId: loginUserInfo.userId,
                isofficial: 1, //是否属于官方发布 1用户 0 官方
                adviceUserId: userId //指定律师编号
            },
            form: _$form,
            success: function(result) {
                if (_$form.find('#reward').val() == 0 || result.data.businessOrder == '') {
                    location.replace(constants.viewUrl + 'user_consult_success.html?articleId=' + result.data.adviceId);
                } else {
                    utils.jumpPay(result.data.businessOrder, function() {
                        location.replace('user_consult_success.html?articleId=' + result.data.adviceId)
                    });
                }
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            },
            complete: function() {
                $formOpen.validator('holdSubmit', false);
            }
        });
    });

});