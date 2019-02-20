/*!
 * 投诉建议
 * @author Li Hongwei
 * @date 2018-10-09 16:02
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    utils.getLoginUserInfo(true);
    require('validator');

    var $form = $('#form');

    //投诉建议
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        $form.validator('holdSubmit');
        var loginUserInfo = utils.getLoginUserInfo(true);
        var _$form = $(form);
        rest.post({
            url: 'user/userSuggest',//136.用户意见反馈;9.29
            data : {
                suggestTitle:'', //反馈标题（目前没有标题）
                userId : loginUserInfo.userId //反馈人用户ID
                //form suggestContent 反馈内容
                //form suggestName 反馈人姓名
                //form mobile 反馈人手机号码
            },
            form : _$form,
            success: function(result) {
                C.alert.ok(result.msg, function() {
                    location.href = constants.viewUrl + 'index.html';
                });
                _$form[0].reset();
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
