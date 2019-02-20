/*!
 * 发布文章
 * @author Li Hongwei
 * @date 2018-09-14 15:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');
    utils.checkUserType(constants.userType.counselor);


    var urlParams = utils.getUrlParams();
    var userInfo = require('utils').getLoginUserInfo(true);
    require('validator');

    //不存在文章类型
    var type = urlParams.type;
    if(null == type || '' == type) {
        utils.jump404('文章类型为空');
        return false;
    }

    if(type != 11) {//不等于工作动态
        $('#fengmian').removeClass('ui-hide');
    }

    if(type != 10) {//我的风采没有内容
        $('#neirong').removeClass('ui-hide');
        require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
            window.ZeroClipboard = zeroclipboard;
            var ue = UE.getEditor('adviceContent');
            ue.ready(function() {ue.setHeight(200);});
        });
    }
    $('#articleType').html(constants.columnCode[type]);

    require('imgUpload');
    $("#file").createImgUpload({
        formData: {
            "name": 'coverImg'
        },
        imageNum : 1,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    //发布文章
    var $form = $('#form');
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        var loginUserInfo = utils.getLoginUserInfo(true);
        $form.validator('holdSubmit');
        var _$form = $(form);
        rest.post({
            url: 'article/user/sendArticle',
            data : {
                isofficial :1,//是否属于官方发布 1用户 0 官方
                userId :userInfo.userId,//咨询师id
                realName:userInfo.realName || userInfo.nickName, //作者名称
                columnCode:type //栏目编码 取自 栏目信息的columnCode值
                //title 文章标题 取自表
                //articleContent 文章内容 取值表单
                 //overImg单封面图片  23445234.png 取自表
            },
            form : _$form,
            success: function(result) {
                C.alert.ok(result.msg, function() {

                    if(window.opener) {
                        window.opener.location.reload();
                        window.close();
                    } else {
                        window.location.replace(constants.viewUrl + 'personal_home_all.html');
                    }
                })
                // location.replace(constants.viewUrl + 'user_consult_success.html');
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
