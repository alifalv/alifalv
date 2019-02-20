/*!
 * 企业咨询者认证
 * @author Li Hongwei
 * @date 2018-08-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');
    utils.checkUserType(constants.userType.enterprise);


    require('imgUpload');
    require('validator');

    var loginUserInfo = utils.getLoginUserInfo(true);

    var $form = $('#form');

    getUserInfo();

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
     * 初始化咨询师执业认证
     */
    function initCompanyInfoForm(data) {
        initWorkImg(data.workImg);
        setFormValue($form, data);
        initAuthenticationStatus(data.isAuthentication);
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


    //从业资格照
    function initWorkImg(imgUrl) {
        var _options ={
            formData: {
                "name": 'workImg'
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
        $("#workImg").createImgUpload(_options);
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

    //开启表单编辑
    function hideDisabledForm() {
        $('.ui-disabled-mask').addClass('ui-hide');
        $('#save_btn').removeClass('ui-hide');
        bindSaveBtn();
    }


    //企业咨询者保存认证信息

    function bindSaveBtn() {
        var form_valid_options = constants.validator;
        form_valid_options.fields = {
            workImg: "required;"//为从业资格照添加校验
        };
        form_valid_options.ignore = ':hidden';//忽略隐藏的input校验

        var login_form_valid = $form.validator(form_valid_options);
        login_form_valid.on('valid.form', function(e, form){
            var loginUserInfo = utils.getLoginUserInfo(true);
            $form.validator('holdSubmit');
            var _$form = $(form);
            rest.post({
                url: 'user/companyAuthentication',
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
                    $form.validator('holdSubmit', false);
                }
            });
        });
    }
});
