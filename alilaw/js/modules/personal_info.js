/*!
 * 账户设置
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var utils = require('utils');
    var userInfo = utils.getLoginUserInfo(true);
    var userId = userInfo.userId;
    var constants = require('constants');


    require('validator');
    require('cropper');
    require('libs/html2canvas.min');
    require('common/sitelogo');

    var dataList = require('dataList');


    function initUserInfoForm() {
        utils.setFormValue($('#saveUserInfoForm'), userInfo);

        //认证状态未已通过，真实姓名不允许修改
        if (userInfo.isAuthentication == '2') {
            $('#saveUserInfoForm').find('#realName').prop('readonly', true);
        }

        dataList.getProvinceList(function(result) {
            result.userInfo = userInfo;

            if (userInfo.province) {
                result.selectedId = userInfo.province;
            }
            C.template('#province_tmpl').renderReplace(result, function() {
                $('#province_select').trigger('change');
            });
        });

        $('#province_select').on('change', function() {
            rest.post({
                url: 'address/cityListByProvince/{pid}',
                urlParams: {
                    pid: $('#province_select').val()
                },
                success: function(result) {
                    if (userInfo.city) {
                        result.selectedId = userInfo.city;
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



    //修改个人信息
    var valid_options = $.extend({}, constants.validator, {
        rules: {
            checkUserName: function(element) {
                if (element.value == userInfo.userName) {
                    return '';

                } else {
                    return rest.post({
                        url: 'user/checkisRegister',
                        data: {
                            userName: element.value,
                            userType: userInfo.userType
                        },
                        success: function(result) {},
                        fail: function() {}
                    });
                }
            },
            checkNickName: function(element) {
                if (element.value == userInfo.nickName) {
                    return '';

                } else {
                    return rest.post({
                        url: 'user/checkisRegister',
                        data: {
                            nickName: element.value,
                            userType: userInfo.userType
                        },
                        success: function(result) {},
                        fail: function() {}
                    });
                }
            }
        },
        dataFilter: function(data) {
            if (data.code == 1) return "";
            else return data.msg;
        },
        fields: {
            '#userName': '用户名:required;length(~18);checkUserName;',
            '#nickName': '昵称:required;length(~18);checkNickName'
        }
    });

    var $formOpen = $(document).find('#saveUserInfoForm');
    var form_valid = $formOpen.validator(valid_options);
    form_valid.on('valid.form', function(e, form) {
        //个人咨询开通会员
        rest.post({
            form: $formOpen,
            url: 'user/saveUserInfo',
            data: {
                userId: userId
            },
            success: function(result) {
                utils.refreshUserInfoCache();
                C.alert.ok(result.msg, function() {
                    location.reload();
                });
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    });

    function updateHeaderImg(imgName) {
        $('#head_img').attr('src', utils.createImgUrl(imgName));
        $('.avatar-wrapper img').attr('src', utils.createImgUrl(imgName));

    }

    function updateUserName(userName) {
        $('#userNameText').html(userName);
    }

    function updateMobile(mobile) {
        $('#mobileText').html(mobile);
    }

    function initInfo() {
        initTirBind();
        initUserInfoForm();
        updateUserName(userInfo.userName);
        updateHeaderImg(userInfo.userImg);
        updateMobile(userInfo.mobile);
    }



    /**
     * 上传图片
     * @param base64ImgStr
     */
    function imagesAjax(base64ImgStr) {
        rest.post({
            url: 'file/uploadImg',
            data: {
                imgBase64Str: base64ImgStr
            },
            success: function(result) {
                ajaxChangeHeader(result.data);
            },
            fail: function(result) {
                C.msg.fail(result.msg || '上传头像失败');
            },
            error: function() {

            },
            complete: function() {

            }
        });
    }

    /**
     * 修改头像
     * @param imgName
     */
    function ajaxChangeHeader(imgName) {
        rest.post({
            url: 'user/changeUserProfile',
            data: {
                userId: userId,
                newImg: imgName
            },
            success: function(result) {
                utils.refreshUserInfoCache();
                C.msg.ok('修改头像成功');
                updateHeaderImg(imgName);
            },
            fail: function(result) {
                C.msg.fail(result.msg || '修改头像失败');
            },
            complete: function() {

            }
        });
    }

    $(function() {

        $("#editimg").click(function() {
            // var texts = 'C:\fakepath\21645766_1370327358364.jpg';
            // console.log("texts : " + texts);
            this.$img.cropper('replace', utils.createImgUrl(userInfo.userImg));
            // $('#edit_img').attr('src', utils.createImgUrl(userInfo.userImg));
            // var filename = document.querySelector("#avatar-name");
            // var teststr = utils.createImgUrl(userInfo.userImg); //你这里的路径写错了
            // var testend = teststr.match(/[^\\]+\.[^\(]+/i); //直接完整文件名的
            // filename.innerHTML = testend;
        });

        //做个下简易的验证  大小 格式
        $('#avatarInput').on('change', function(e) {
            var filemaxsize = 1024 * 5; //5M
            var target = $(e.target);
            var Size = target[0].files[0].size / 1024;
            if (Size > filemaxsize) {
                C.msg.fail('图片过大，请重新选择!');
                $(".avatar-wrapper").children().remove();
                return false;
            }
            if (!this.files[0].type.match(/image.*/)) {
                C.msg.fail('请选择正确的图片');

            } else {
                var filename = document.querySelector("#avatar-name");
                var texts = document.querySelector("#avatarInput").value;
                console.log("texts : " + texts);
                var teststr = texts; //你这里的路径写错了
                var testend = teststr.match(/[^\\]+\.[^\(]+/i); //直接完整文件名的
                filename.innerHTML = testend;
            }

        });

        $(".avatar-save").on("click", function() {
            var img_lg = document.getElementById('imageHead');
            // 截图小的显示框内的内容
            html2canvas(img_lg, {
                allowTaint: true,
                taintTest: false,
                onrendered: function(canvas) {
                    canvas.id = "mycanvas";
                    //生成base64图片数据
                    var dataUrl = canvas.toDataURL("image/jpeg");
                    var newImg = document.createElement("img");
                    newImg.src = dataUrl;
                    imagesAjax(dataUrl)
                }
            });
        });
        initInfo();
    });

    //修改密码
    var editpwdDialog = null;
    $('#editpwd_btn').on('click', function() {
        editpwdDialog = C.dialog({
            title: '修改密码',
            area: ['600px', '400px'],
            content: $('#editpwdDialog')
        });

    });

    var valid_options = constants.validator;
    valid_options.theme = 'yellow_bottom';
    var $editpwdform = $(document).find('#editpwdform');
    var editpwdform_valid = $editpwdform.validator(valid_options);
    editpwdform_valid.on('valid.form', function(e, form) {
        //个人咨询开通会员
        rest.post({
            form: $editpwdform,
            url: 'user/editPassword',
            data: {
                userId: userId
            },
            success: function(result) {
                C.alert.ok('密码修改成功，新密码在下次登录时生效', function() {
                    layer.close(editpwdDialog);
                });
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    });

    var editmobileDialog; //修改手机号码弹出层对象

    //修改手机号码按钮事件
    $('#editmobile_btn').on('click', function() {
        editmobileDialog = C.dialog({
            title: '修改手机号码',
            area: ['600px', '400px'],
            content: $('#editmobileDialog'),
            success: function(layero, index) {
                //旧手机号码发短信功能绑定
                utils.bindSendSms(layero.find('#sendsms_btn'), layero.find('#mobile'), 'sms/send/{mobile}');

                //新手机号码发短信功能绑定
                utils.bindSendSms(layero.find('#sendnewsms_btn'), layero.find('#newMobile'), 'sms/send/{mobile}');
            }
        });

        var mobileText = $('#mobileText').html();
        $('#editmobileDialog').find('.j-mobile').html(mobileText); //弹出框显示手机号码
        $('#editmobileDialog').find('#mobile').val(mobileText); //隐藏手机号码input用于发短信

    });

    var $editmobile1form = $(document).find('#editmobile1form'); //修改手机号码第一步form
    var $editmobileform = $(document).find('#editmobileform'); //修改手机号码第二步form

    //验证旧手机号
    var editmobile1form_valid = $editmobile1form.validator(valid_options);
    editmobile1form_valid.on('valid.form', function(e, form) {
        rest.post({
            form: $editmobile1form, // smsCode : smsCode, mobile :mobile
            url: 'user/checkCode',
            success: function(result) {
                $editmobile1form.addClass('ui-hide');
                $editmobileform.removeClass('ui-hide');
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
        return false;
    });

    //验证新手机号修改绑定
    var editmobileformvalid_options = $.extend({}, constants.validator, {
        theme: 'yellow_bottom',
        dataFilter: function(data) {
            if (data.code == 1) return "";
            else return data.msg;
        },
        fields: {
            '#newMobile': '手机号码:required;mobile;remote(' + constants.apiUrl + 'user/checkisRegister, userType=' + userInfo.userType + ')'
        }
    });
    var editmobileform_valid = $editmobileform.validator(editmobileformvalid_options);
    editmobileform_valid.on('valid.form', function(e, form) {
        rest.post({
            form: $editmobileform,
            url: 'user/editMobile',
            data: {
                //form smsCode 短信验证码
                //form newMobile新手机号码
                userId: userId
            },
            success: function(result) {
                utils.refreshUserInfoCache();
                C.msg.ok(result.msg);
                $('#mobileText').html($editmobileform.find('#newMobile').val());
                layer.close(editmobileDialog);
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
        return false;
    });

    function initTirBind() {
        C.template('#triBindTmpl').renderReplace(userInfo, function() {});
    }


    //解绑
    $('#triBox').on('click', '.j-unbind-btn', function() {
        var _$this = $(this);
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var _type = _$this.data('type');
        C.confirm('确认解除绑定？', function() {
            rest.post({
                url: 'user/unbindThreeLoginFlag',
                data: {
                    userId: _loginUserInfo.userId,
                    openType: _type
                },
                success: function(result) {
                    utils.refreshUserInfoCache();
                    C.msg.ok(result.msg);
                    _$this.removeClass('j-unbind-btn').addClass('j-bind-btn').html('绑 定');
                    _$this.closest('.j-tri-row').find('.j-tri-type').html('未绑定');
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                }
            });
        });
        return false;
    });
    require('sinaLogin');
    require('qqLogin');
    require('wxLogin');


    /**
     * 新浪绑定
     * @param userId
     * @param openId
     * @param openType
     * @param _$this
     */
    function sinaBindThreeLoginFlag(userId, openId, openType, _$this) {
        var index = layer.load(); //又换了种风格，并且设定最长等待10秒
        rest.post({
            url: 'user/bindThreeLoginFlag',
            data: {
                userId: userId,
                openId: openId,
                openType: openType
            },
            success: function(result) {
                utils.refreshUserInfoCache();
                C.msg.ok(result.msg);
                _$this.removeClass('j-bind-btn').addClass('j-unbind-btn').html('解 绑');
                _$this.closest('.j-tri-row').find('.j-tri-type').html('已绑定');
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            },
            complete: function() {
                layer.close(index);
            }

        });

    }

    //绑定
    $('#triBox').on('click', '.j-bind-btn', function() {
        var _$this = $(this);
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var _type = _$this.data('type');
        if (_type == 'WEIBO') {

            if (WB2.checkLogin()) { //检查是否已登录
                WB2.logout(function() { //退出登录方法
                    //回调方法
                });
            }

            WB2.login(function() { //登录授权
                WB2.anyWhere(function(W) {
                    W.parseCMD('/account/get_uid.json', function(oResult1, bStatus) { //获取用户uid
                        if (bStatus) {
                            W.parseCMD('/users/show.json', function(oResult2, bStatus) { //通过uid获取用户信息
                                if (bStatus) {
                                    var args = {
                                        openid: oResult2.id, //获取用户openid
                                        access_token: WB2.oauthData.access_token, //获取用户access_token
                                        username: oResult2.name, //获取用户名
                                        userHeadImg: oResult2.profile_image_url //获取用户微博头像
                                    };
                                    //然后根据实际情况进行自己网站的一些认证处理
                                    sinaBindThreeLoginFlag(_loginUserInfo.userId, args.openid, constants.openType.sina, _$this);
                                    // login(args.openid, constants.openType, args.username);
                                }
                            }, { uid: oResult1.uid }, { method: 'get', cache_time: 30 });
                        }
                    }, {}, { method: 'get', cache_time: 30 }); //默认是post请求方法
                });
            });

        } else if (_type == 'QQ') {
            QC.Login.showPopup({
                appId: constants.qqAppId,
                redirectURI: constants.viewUrl + 'qqLogin.html?userId=' + _loginUserInfo.userId
            });

        } else if (_type == 'WECHAT') {
            // C.msg.tips('开发中');
            layer.open({
                type: 1,
                title: '微信扫一扫',
                area: ['300px', '450px'], //宽高
                content: '<div id="weChatLoginQRCode"></div>',
                success: function(layero, index) {
                    var obj = new WxLogin({
                        self_redirect: false, //true：手机点击确认登录后可以在 iframe 内跳转到 redirect_uri，false：手机点击确认登录后可以在 top window 跳转到 redirect_uri。默认为 false。
                        id: "weChatLoginQRCode", //二维码显示的容器
                        appid: constants.wxAppId,
                        scope: "snsapi_login",
                        state: utils.uuid(),
                        // redirect_uri: constants.viewUrl + "weChatLogin.html"
                        redirect_uri: constants.apiUrl + "wx/logincallback"
                    });
                }
            });
            return false;

        }
        return false;
    });

    //收起展开
    var isOpen = true;
    var isClicking = false;
    $('.j-down-btn').on('click', function() {
        if (isClicking) {
            return false;
        }
        isClicking = true;
        var _$this = $(this);
        $('.down-box').slideToggle(function() {
            isClicking = false;
            if (isOpen) {
                isOpen = false;
                _$this.find('span').html('展开');
                _$this.find('i').addClass('icon-down').removeClass('icon-top');
            } else {
                isOpen = true;
                _$this.find('span').html('收起');
                _$this.find('i').addClass('icon-top').removeClass('icon-down');
            }
        });
        return false;
    });
});