/*!
 * 个人主页设置
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');

    $('.j-home-sld').html(constants.home_sld);
    utils.checkUserType(constants.userType.counselor);

    var loginUserInfo = utils.getLoginUserInfo(true);
    var loginUserId = loginUserInfo.userId;
    require('validator');
    require('imgUpload');


    //从业资格照
    function initCodeImg(imgUrl) {
        var _options ={
            formData: {
                "name": 'codeImg'
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
        $("#codeImg").createImgUpload(_options);
    }

    //个人主页地址保存
    var $url_form = $('#url_form');
    var valid_options = $.extend({},constants.validator);
    valid_options.theme = 'yellow_bottom';
    var url_form_valid = $url_form.validator(valid_options);
    url_form_valid.on('valid.form', function(e, form){

        var loginUserInfo = utils.getLoginUserInfo(true);

        //个人咨询开通会员
        rest.post({
            url: 'user/openCounselorPersonUrl',
            form : $url_form,
            data : {
                userId: loginUserInfo.userId
            },
            success: function(result) {
                utils.refreshUserInfoCache();
                C.alert.ok(result.msg, function() {
                    location.reload();
                });
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

    var $info_form = $('#info_form');
    var form_valid_options = constants.validator;
    form_valid_options.fields = {
        codeImg: "required;"
    };
    form_valid_options.ignore = ':hidden';
    var info_form_valid = $info_form.validator(form_valid_options);
    info_form_valid.on('valid.form', function(e, form){

        var loginUserInfo = utils.getLoginUserInfo(true);

        rest.post({
            url: 'user/settingCounselorInfo',
            form : $info_form,
            data : {
                userId: loginUserInfo.userId
            },
            success: function(result) {
                utils.refreshUserInfoCache();
                C.msg.ok(result.msg);
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });


    function getUserInfo() {
        rest.post({
            url: 'user/userInfo/{userId}',
            urlParams : {
                userId : loginUserInfo.userId
            },
            success: function (result) {
                initForm(result.data);
            }
        });
    }

    function initForm(data) {
        utils.setFormValue($url_form, data);
        utils.setFormValue($info_form, data);
        initCodeImg(data.codeImg)
    }
    getUserInfo();
});
