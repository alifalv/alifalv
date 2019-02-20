/*!
 * 发布培训
 * @author Li Hongwei
 * @date 2018-07-22 17:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    utils.getLoginUserInfo(true);
    var constants = require('constants');
    require('validator');

    require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
        window.ZeroClipboard = zeroclipboard;
        var ue = UE.getEditor('trainDesc');
        ue.ready(function() {ue.setHeight(200);});
    });
    var dataList = require('dataList');
    require('WdatePicker');


    dataList.getProvinceList(function(result) {
        C.template('#provinceTmpl').renderReplace(result, function() {
            $('#province_select').trigger('change');
        });
    });

    $('#province_select').on('change', function() {
        rest.post({
            url: 'address/cityListByProvince/{pid}',
            urlParams : {
                pid : $('#province_select').val()
            },
            success: function (result) {
                C.template('#cityTmpl').renderReplace(result, function() {});
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

    var $form = $('#form');
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        $form.validator('holdSubmit');
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var _$form = $(form);
        rest.post({
            url: 'article/sendLegalTrain',
            data : {
                isofficial :1,//是否属于官方发布 1用户 0 官方
                userId : _loginUserInfo.userId,
                realName : _loginUserInfo.realName
            },
            form : _$form,
            success: function(result) {
                // C.msg.ok(result.msg, function () {
                //     location.replace(constants.viewUrl + 'training_release.html');
                // });
                location.replace(constants.viewUrl + 'training_success.html');
                //没有返回articleId智能跳到列表
                // location.replace(constants.viewUrl + 'user_consult_success.html?articleId=' + result.data.articleId);
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            },
            complete : function() {
                $form.validator('holdSubmit', false);
            }
        });
    });

});