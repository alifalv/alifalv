/*!
 * 咨询者执业认证
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');
    utils.checkUserType(constants.userType.advocate);

    require('imgUpload');
    var dataList = require('dataList');
    require('validator');

    var $form = $('#form');

    var loginUserInfo = utils.getLoginUserInfo(true);
    var loginUserId = loginUserInfo.userId;

    getUserInfo();

    //证件正面拍照
    function initIdCardFrontImg(imgUrl) {
        var _options ={
            formData: {
                "name": 'idCardFront'
            },
            imageNum: 1,
            success: function (data) {},
            error: function (err) {
                C.msg.fail(err.msg);
            }
        };
        if(imgUrl) {
            _options.imgUrl = imgUrl;
        }
        $("#idCardFront").createImgUpload(_options);
    }

    //证件反面拍照
    function initIdCardBackImg(imgUrl) {
        var _options = {
            formData: {
                "name": 'idCardBack'
            },
            imageNum : 1,
            success: function(data) {},
            error: function(err) {
                C.msg.fail(err.msg);
            }
        };
        if(imgUrl) {
            _options.imgUrl = imgUrl;
        }
        $("#idCardBack").createImgUpload(_options);
    }

    //开启表单编辑
    function hideDisabledForm() {
        $('.ui-disabled-mask').addClass('ui-hide');
        $('#save_btn').removeClass('ui-hide');
        bindSaveBtn();
    }

    function initAuthenticationStatus(value) {
        if(value == '0') {
            //可以编辑
            hideDisabledForm()
        } else if(value == '1') {
            //不可以编辑
        } else if(value == '2') {
            //不可以编辑
        } else if(value == '3') {
            //可以编辑
            hideDisabledForm()
        } else {
            //不可以编辑
        }
        $('#authenticationStatus').html(utils.getAuthenticationTxt(value));
    }

    //咨询者认证信息保存
    function bindSaveBtn() {
        var isSend = false;
        var form_valid_options = constants.validator;
        form_valid_options.fields = {
            idCardFront: "required;",
            idCardBack : "required;"
        };
        form_valid_options.ignore = ':hidden';

        var login_form_valid = $form.validator(form_valid_options);
        login_form_valid.on('valid.form', function(e, form){
            isSend = true;
            var loginUserInfo = utils.getLoginUserInfo(true);
            var _$form = $(form);
            rest.post({
                url: 'user/realNameAuthConsultant',
                form : _$form,
                data : {
                    userId : loginUserInfo.userId
                },
                success: function(result) {
                    utils.refreshUserInfoCache();

                    C.alert.ok(result.msg, function() {
                        location.reload();
                    });
                },
                fail : function (result) {
                    C.msg.fail(result.msg);
                },
                complete : function () {
                    isSend = false;
                }
            });
        });
    }

    //获取用户信息
    function getUserInfo() {
        rest.post({
            url: 'user/userInfo/{userId}',
            urlParams : {
                userId : loginUserInfo.userId
            },
            success: function (result) {
                initCompanyInfoForm(result.data);
            }
        });
    }

    /**
     * form表单设值
     * @param _data
     */
    function setFormValue($form, _data) {
        $form.find('option').prop('selected', false);
        for(var key in _data) {
            var _$obj = $form.find('[name="' + key + '"]');
            if(_$obj.is("select")) {
                _$obj.find('option[value="' + _data[key] + '"]').prop('selected', true);
            } else {
                _$obj.val(_data[key]);
            }
        }
    }

    /**
     * 初始化咨询师执业认证
     */
    function initCompanyInfoForm(data) {
        initIdCardBackImg(data.idCardBack);
        initIdCardFrontImg(data.idCardFront);
        setFormValue($form, data);
        initAuthenticationStatus(data.isAuthentication);
    }

});
