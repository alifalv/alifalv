/*!
 * 修改岗位信息
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var advertiseId = urlParams.advertiseId;
    if(null == advertiseId || '' == advertiseId) {
        utils.jump404('岗位编号为空');
        return false;
    }
    require('validator');
    var constants = require('constants');


    function initUEditor(content) {
        require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
            window.ZeroClipboard = zeroclipboard;
            var ue = UE.getEditor('jobDesc');
            ue.ready(function() {
                ue.setHeight(200);
                ue.setContent(content);  //赋值给UEdit
            });
        });
    }

    function initJobForm() {
        var _loginUserInfo = utils.getLoginUserInfo(true);
        rest.post({
            url: 'user/seacherJobDetail',
            data : {
                advertiseId  : advertiseId
            },
            success: function (result) {
                utils.setFormValue($('#form'), result.data);
                initSearchForm(result.data.qualification, result.data.workExperience, result.data.sal, result.data.province, result.data.city);
                initUEditor(result.data.jobDesc);
            },
            fail: function (result) {
                utils.jump500(result.msg);
            }
        });

    }
    initJobForm();


    /**
     *
     * @param educationId
     * @param experienceId
     * @param salId
     * @param provinceSelectedId
     * @param citySelectedId
     */
    function initSearchForm(educationId, experienceId, salId, provinceSelectedId, citySelectedId) {

        var dataList = require('dataList');

        //学历要求
        dataList.getEducationList(function(result) {
            if(educationId) {
                result.selectedId = educationId;
            }
            C.template('#educationListTmpl').renderReplace(result, function() {});
        });


        //工作经验
        dataList.getExperienceList(function(result) {
            if(experienceId) {
                result.selectedId = experienceId;
            }
            C.template('#experienceListTmpl').renderReplace(result, function() {});
        });


        //工资范围
        dataList.getSalList(function(result) {
            if(experienceId) {
                result.selectedId = salId;
            }
            C.template('#salListTmpl').renderReplace(result, function() {});
        });



        //城市
        dataList.getProvinceList(function(result) {
            if(provinceSelectedId) {
                result.selectedId = provinceSelectedId;
            }
            C.template('#provinceListTmpl').renderReplace(result, function() {
                $('#province').trigger('change');
            });
        });



        $('#province_select').on('change', function() {
            var province_select_val = $('#province_select').val();
            if(province_select_val == '') {
                C.template('#city_tmpl').renderReplace({}, function() {});
                return false;
            }
            rest.post({
                url: 'address/cityListByProvince/{pid}',
                urlParams : {
                    pid : province_select_val
                },
                success: function (result) {
                    if(citySelectedId) {
                        result.selectedId = citySelectedId;
                    }
                    C.template('#city_tmpl').renderReplace(result, function() {});
                },
                fail: function (result) {
                    C.msg.fail(result.msg);
                }
            });
            return false;
        });

        //城市
        $('#province').on('change', function() {
            var province_select_val = $('#province').val();
            if(province_select_val == '') {
                C.template('#city_tmpl').renderReplace({}, function() {});
                return false;
            }
            rest.post({
                url: 'address/cityListByProvince/{pid}',
                urlParams : {
                    pid : province_select_val
                },
                success: function (result) {
                    if(citySelectedId) {
                        result.selectedId = citySelectedId;
                    }
                    C.template('#cityListTmpl').renderReplace(result, function() {});
                },
                fail: function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });

    }


    //保存
    var $form = $('#form');
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        var loginUserInfo = utils.getLoginUserInfo(true);
        $form.validator('holdSubmit');

        var _$form = $(form);
        rest.post({
            url: 'user/updateJob',
            data : {
                userId : loginUserInfo.userId,
                advertiseId : advertiseId//岗位ID（不可空）
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
            },
            complete : function() {
                $form.validator('holdSubmit', false);
            }
        });
    });

});
