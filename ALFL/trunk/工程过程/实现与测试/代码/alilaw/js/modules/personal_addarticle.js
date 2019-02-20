/*!
 * 发布文章
 * @author Li Hongwei
 * @date 2018-09-14 15:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');
    utils.checkUserType(constants.userType.counselor);


    var urlParams = utils.getUrlParams();
    var userInfo = require('utils').getLoginUserInfo(true);
    require('validator');


    //不存在文章类型
    var type = urlParams.type;
    if (null == type || '' == type) {
        utils.jump404('文章类型为空');
        return false;
    }

    //文章编号不为空是  进行修改操作
    if (urlParams.articleId != null && '' != urlParams.articleId) {
        var articleId = urlParams.articleId;
        // 用户ID
        var loginUserId = -1;
        var loginUserInfo = utils.getLoginUserInfo();
        if (loginUserInfo) {
            loginUserId = loginUserInfo.userId;
        }
        $("#addImg").hide();
        //文章详情
        rest.post({
            url: 'article/articleDetails/{userId}/{articleId}',
            urlParams: {
                userId: loginUserId,
                articleId: articleId
            },
            data: {
                columnCode: type
            },
            success: function(result) {
                $('#title').val(result.data.title);
                $('#adviceContent').val(result.data.articleContent);
                var imghref = utils.createImgUrl(result.data.coverImg);
                var divImg = document.getElementById("clearImg");
                // 显示默认图片
                divImg.innerHTML = divImg.innerHTML + '<section id="imgSection" class="up-section"><i class="close-upimg"></i><img class="up-img Upimg" src="' + imghref + '"><input type="hidden"  name="coverImg" value="' + result.data.coverImg + '"></section>';

            }
        });

    }

    if (type != 11) { //不等于工作动态
        $('#fengmian').removeClass('ui-hide');
    }

    if (type != 10) { //我的风采没有内容
        $('#neirong').removeClass('ui-hide');
        require(['zeroclipboard', 'ueditor.ext'], function(zeroclipboard) {
            window.ZeroClipboard = zeroclipboard;
            var ue = UE.getEditor('adviceContent');
            ue.ready(function() { ue.setHeight(200); });
        });
    }
    $('#articleType').html(constants.columnCode[type]);

    require('imgUpload');
    // 修改时默认图片的展示和修改
    $("#clearImg").on('click', '.Upimg', function() {
        $("#imgSection").remove();
        $("#addImg").show();
        document.getElementById("file").click();
    });
    $("#clearImg").on('click', '.file', function() {
        $("#file").createImgUpload({
            formData: {
                "name": 'coverImg'
            },
            imageNum: 1,
            success: function(data) {},
            error: function(err) {
                C.msg.fail(err.msg);
            }
        });
    });

    //发布文章
    var $form = $('#form');
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form) {
        if (null == urlParams.articleId || '' == urlParams.articleId) {
            var loginUserInfo = utils.getLoginUserInfo(true);
            $form.validator('holdSubmit');
            var _$form = $(form);
            rest.post({
                url: 'article/user/sendArticle',
                data: {
                    isofficial: 1, //是否属于官方发布 1用户 0 官方
                    userId: userInfo.userId, //律师id
                    realName: userInfo.realName || userInfo.nickName, //作者名称
                    columnCode: type //栏目编码 取自 栏目信息的columnCode值
                        //title 文章标题 取自表
                        //articleContent 文章内容 取值表单
                        //overImg单封面图片  23445234.png 取自表
                },
                form: _$form,
                success: function(result) {
                    C.alert.ok(result.msg, function() {

                            if (window.opener) {
                                window.opener.location.reload();
                                window.close();
                            } else {
                                window.location.replace(constants.viewUrl + 'personal_home_all.html');
                            }
                        })
                        // location.replace(constants.viewUrl + 'user_consult_success.html');
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                },
                complete: function() {
                    $form.validator('holdSubmit', false);
                }
            });
        } else {
            // 修改文章
            var loginUserInfo = utils.getLoginUserInfo(true);
            $form.validator('holdSubmit');
            var _$form = $(form);
            rest.post({
                url: 'article/updateArticle',
                data: {
                    articleId: urlParams.articleId, //文章ID
                    isofficial: 1, //是否属于官方发布 1用户 0 官方
                    userId: userInfo.userId, //律师id
                    realName: userInfo.realName || userInfo.nickName, //作者名称
                    columnCode: type //栏目编码 取自 栏目信息的columnCode值
                        //title 文章标题 取自表
                        //articleContent 文章内容 取值表单
                        //overImg单封面图片  23445234.png 取自表
                },
                form: _$form,
                success: function(result) {
                    C.alert.ok(result.msg, function() {
                            // if (window.opener) {
                            //     window.opener.location.reload();
                            //     window.close();
                            // } else {
                            window.location.replace(constants.viewUrl + 'personal_home_all.html');
                            // }
                        })
                        // location.replace(constants.viewUrl + 'user_consult_success.html');
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                },
                complete: function() {
                    $form.validator('holdSubmit', false);
                }
            });
        }

    });

});