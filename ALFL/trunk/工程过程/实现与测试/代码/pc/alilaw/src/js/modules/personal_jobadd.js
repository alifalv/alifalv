/*!
 * 新增岗位
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var dataList = require('dataList');
    require('validator');
    var constants = require('constants');



    require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
        window.ZeroClipboard = zeroclipboard;
        var ue = UE.getEditor('jobDesc');
        ue.ready(function() {ue.setHeight(200);});
    });


    //工作经验
    dataList.getExperienceList(function(result) {
        C.template('#experienceListTmpl').renderReplace(result, function() {});
    });

    //学历要求
    dataList.getEducationList(function(result) {
        C.template('#educationListTmpl').renderReplace(result, function() {});
    });

    //工资范围
    dataList.getSalList(function(result) {
        C.template('#salListTmpl').renderReplace(result, function() {});
    });

    dataList.getProvinceList(function(result) {
        C.template('#provinceListTmpl').renderReplace(result, function() {
            $('#province').trigger('change');
        });
    });

    //城市
    $('#province').on('change', function() {
        rest.post({
            url: 'address/cityListByProvince/{pid}',
            urlParams : {
                pid : $('#province').val()
            },
            success: function (result) {
                C.template('#cityListTmpl').renderReplace(result, function() {});
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    });


    //保存
    var $form = $('#form');
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        var loginUserInfo = utils.getLoginUserInfo(true);
        var _$form = $(form);
        rest.post({
            url: 'user/sendJob',
            data : {
                userId : loginUserInfo.userId,
                userType : 1//后台发布，userType 传0过来 ，PC端传非0
            },
            form : _$form,
            success: function(result) {
                C.alert.ok(result.msg, function() {
                    if(window.opener) {
                        window.opener.location.reload();
                        window.close();
                    } else {
                        window.location.replace(constants.viewUrl + 'personal_consult.html');
                    }
                });
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

});
