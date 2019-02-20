/*!
 * 律师执业认证
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');
    utils.checkUserType(constants.userType.counselor);


    require('imgUpload');
    var dataList = require('dataList');
    require('validator');

    var $form = $('#form');

    var loginUserInfo = utils.getLoginUserInfo(true);
    var loginUserId = loginUserInfo.userId;

    getUserInfo();

    function initJobSelect(jobId) {
        dataList.getJobList(function(result) {
            if (jobId) {
                result.selectedId = jobId;
            }
            C.template('#job_tmpl').renderReplace(result, function() {});
        });
    }

    function initCaseSelect(list) {
        dataList.getCaseList(function(result) {
            C.template('#case_tmpl').renderReplace(result, function() {
                setFormSpecialityList(list);
            });
        });
    }
    /**
     * 初始化地区下拉菜单
     * @param provinceSelectedId
     * @param citySelectedId
     */
    function initProvinceCitySelect(provinceSelectedId, citySelectedId) {
        dataList.getProvinceList(function(result) {
            if (provinceSelectedId) {
                result.selectedId = provinceSelectedId;
            }
            C.template('#province_tmpl').renderReplace(result, function() {
                // if(provinceSelectedId) {
                $('#province_select').trigger('change');
                // }
            });
        });

        $('#province_select').on('change', function() {
            rest.post({
                url: 'address/cityListByProvince/{pid}',
                urlParams: {
                    pid: $('#province_select').val()
                },
                success: function(result) {
                    if (citySelectedId) {
                        result.selectedId = citySelectedId;
                    }
                    C.template('#city_tmpl').renderReplace(result, function() {

                    });
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    }

    //从业资格照
    function initWorkImg(imgUrl) {
        var _options = {
            formData: {
                "name": 'workImg'
            },
            imageNum: 1,
            success: function(data) {},
            error: function(err) {
                C.msg.fail(err.msg);
            }
        };
        if (imgUrl) {
            _options.imgUrl = imgUrl;
        }
        $("#workImg").createImgUpload(_options);
    }

    //律师形象照
    function initPersonImg(imgUrl) {
        var _options = {
            formData: {
                "name": 'personImg'
            },
            imageNum: 1,
            success: function(data) {},
            error: function(err) {
                C.msg.fail(err.msg);
            }
        };
        if (imgUrl) {
            _options.imgUrl = imgUrl;
        }
        $("#personImg").createImgUpload(_options);
    }

    //开启表单编辑
    function hideDisabledForm() {
        $('.ui-disabled-mask').addClass('ui-hide');
        $('#save_btn').removeClass('ui-hide');
        bindSaveBtn();
    }

    function initAuthenticationStatus(value) {
        // 0：未认证 1：待审核 2：通过 3 : 未通过
        if (value == '0') {
            hideDisabledForm()
        } else if (value == '2') {
            hideDisabledForm()
        } else if (value == '3') {
            hideDisabledForm()
        } else {}
        $('#authenticationStatus').html(utils.getAuthenticationTxt(value));
    }

    //律师认证信息保存
    function bindSaveBtn() {
        var isSend = false;
        var form_valid_options = constants.validator;
        form_valid_options.fields = {
            workImg: "required;",
            personImg: "required;"
        };
        form_valid_options.ignore = ':hidden';

        var login_form_valid = $form.validator(form_valid_options);
        login_form_valid.on('valid.form', function(e, form) {
            isSend = true;
            var loginUserInfo = utils.getLoginUserInfo(true);
            var _$form = $(form);
            var _cases = '';
            _$form.find('input[name="cases"]:checked').each(function(i, e) {
                if (i === 0) {
                    _cases += ('cases=' + $(this).val());
                } else {
                    _cases += ('&cases=' + $(this).val());
                }
            });
            rest.post({
                url: 'user/counselorAuthentication?' + _cases,
                form: _$form,
                data: {
                    userId: loginUserInfo.userId
                },
                success: function(result) {
                    utils.refreshUserInfoCache();
                    C.alert.ok(result.msg, function() {
                        location.reload();
                    });
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                },
                complete: function() {
                    isSend = false;
                }
            });
        });
    }

    //获取用户信息
    function getUserInfo() {
        rest.post({
            url: 'user/userInfo/{userId}',
            urlParams: {
                userId: loginUserInfo.userId
            },
            success: function(result) {
                initCompanyInfoForm(result.data);
            }
        });
    }

    /**
     * 为表单擅长专业设值
     * @param list
     */
    function setFormSpecialityList(list) {
        for (var i = 0; i < list.length; i++) {
            $form.find('input:checkbox[name="cases"][value="' + list[i].typeId + '"]').prop('checked', true);
        }
    }

    /**
     * 初始化律师执业认证
     */
    function initCompanyInfoForm(data) {
        initPersonImg(data.personImg);
        initWorkImg(data.workImg);
        utils.setFormValue($form, data);
        initProvinceCitySelect(data.province, data.city);
        initJobSelect(data.job);
        initCaseSelect(data.specialityList);
        initAuthenticationStatus(data.isAuthentication);
    }

});