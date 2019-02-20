/*!
 * 案件委托
 * @author Li Hongwei
 * @date 2018-07-14 15:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var loginUserInfo = utils.getLoginUserInfo(true);

    var constants = require('constants');

    var store = require('store');
    require('validator');


    require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
        window.ZeroClipboard = zeroclipboard;
        var ue = UE.getEditor('caseDesc');
        ue.ready(function() {ue.setHeight(200);});
    });


    var dataList = require('dataList');
    dataList.getCaseList(function(result) {
        C.template('#case_tmpl').renderReplace(result, function() {});
    });

    dataList.getProvinceList(function(result) {
        C.template('#province_tmpl').renderReplace(result, function() {
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
                C.template('#city_tmpl').renderReplace(result, function() {});
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

    require('imgUpload');
    $(".file").createImgUpload({
        formData: {
            "name": 'caseImgs'
        },
        imageNum : 50,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    var $form = $('#form');
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        var loginUserInfo = utils.getLoginUserInfo(true);
        $form.validator('holdSubmit');
        var _$form = $(form);
        rest.post({
            url: 'case/sendCase',
            data : {
                resource : 1,//来源 1 正常发布咨询流程 2 在某个咨询师主页点击了免费咨询按钮
                // adviceUserId: '',//咨询师用户id resource等于2才会有这个值
                userId : loginUserInfo.userId,
                isofficial : 1//是否属于官方发布 1用户 0 官方
            },
            form : _$form,
            success: function(result) {
                window.location.replace(constants.viewUrl + 'user_case_success.html?caseId=' + result.data.caseId);
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            },
            complete : function () {
                $form.validator('holdSubmit', false);
            }
        });
    });

});
