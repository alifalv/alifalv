/*!
 * 发布我要吐槽
 * @author Li Hongwei
 * @date 2018-07-14 15:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var constants = require('constants');
    var utils = require('utils');
    var loginUserInfo = utils.getLoginUserInfo(true);
    require('validator');

    require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
        window.ZeroClipboard = zeroclipboard;
        var ue = UE.getEditor('articleContent');
        ue.ready(function() {ue.setHeight(200);});
    });

    var $form = $('#form');

    $form.find('[name="realName"]').val(loginUserInfo.nickName || loginUserInfo.userName);

    require('imgUpload');
    $("#file").createImgUpload({
        formData: {
            "name": 'imgs'
        },
        imageNum : 10,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    //百姓吐槽
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        var loginUserInfo = utils.getLoginUserInfo(true);
        $form.validator('holdSubmit');
        var _$form = $(form);
        rest.post({
            url: 'article/sendCommonPeople',
            data : {
                userId : loginUserInfo.userId,
                isofficial : 1//是否官方【0: 官方 1：用户】
                // sOutJoin:'',// 是否是外链 不要管
                // outJoinUrl:'' //外链地址 不要管
            },
            form : _$form,
            success: function(result) {
                //PS： 用户发布吐槽后，需要管理员在后台审核后才可以展示 ；官方发布后，也再需要点发布才可以展示

                window.location.replace(constants.viewUrl + 'complain_success.html?articleId=' + result.data.articleId)
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
