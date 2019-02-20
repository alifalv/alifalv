/*!
 * 第三方登录
 * @author Li Hongwei
 * @date 2018-08-05 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var constants = require('constants');
    var utils = require('utils');
    var store = require('store');

    require('sinaLogin');
    require('qqLogin');
    require('wxLogin');

    // window.callback = function(user) {
    //     alert(JSON.stringify(user))
    //     if(window.opener) {
    //         window.opener.location.reload();
    //         window.close();
    //     } else {
    //         window.location.replace(constants.viewUrl + 'personal_consult.html');
    //     }
    // };


    function login(openId, openType, accountNo) {
        rest.post({
            url: 'user/login',
            data : {
                openId : openId,
                openType : openType,
                accountNo : accountNo
            },
            success: function (result) {

            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    }

    function checkThirdAuthorization(openId, openType) {
        var index = layer.load(); //又换了种风格，并且设定最长等待10秒
        rest.post({
            url: 'user/getThreeLoginFlag',
            data : {
                openId  : openId,
                openType : openType
            },
            success: function (result) {

                //绑定了用户，返回登录成功的用户信息，返回结果等同于登录成功
                store.setCache('userInfo', result.data);
                location.href = constants.viewUrl + 'index.html';
            },
            fail: function (result) {
                location.href = constants.viewUrl + 'reg_bind.html?key=' + openId + '&type=' + openType;
            },
            complete : function () {
                layer.close(index);
            }

        });

    }


    //微信登录
    $(document).on('click','[thirdlogin="wx"]', function() {
        layer.open({
            type: 1,
            title : '微信扫一扫',
            area: ['300px', '450px'], //宽高
            content: '<div id="weChatLoginQRCode"></div>',
            success: function(layero, index){
                var obj = new WxLogin({
                    self_redirect:false,//true：手机点击确认登录后可以在 iframe 内跳转到 redirect_uri，false：手机点击确认登录后可以在 top window 跳转到 redirect_uri。默认为 false。
                    id:"weChatLoginQRCode",//二维码显示的容器
                    appid: constants.wxAppId,
                    scope: "snsapi_login",
                    state : utils.uuid(),
                    // redirect_uri: constants.viewUrl + "weChatLogin.html"
                    redirect_uri: constants.apiUrl + "wx/logincallback"
                });
            }
        });
        return false;
    });

    function sinaLogout() {
        WB2.login(function() {//登录授权
            // if(WB2.checkLogin()) {
            WB2.anyWhere(function (W) {
                W.parseCMD('/account/get_uid.json', function (oResult1, bStatus) {//获取用户uid
                    if (bStatus) {
                        W.parseCMD('/users/show.json', function (oResult2, bStatus) {//通过uid获取用户信息
                            if (bStatus) {
                                var args = {
                                    openid: oResult2.id,//获取用户openid
                                    access_token: WB2.oauthData.access_token,//获取用户access_token
                                    username: oResult2.name,//获取用户名
                                    userHeadImg: oResult2.profile_image_url//获取用户微博头像
                                };
                                console.log(args);
                                WB2.logout(function () {
                                    //然后根据实际情况进行自己网站的一些认证处理
                                    checkThirdAuthorization(args.openid, constants.openType.sina);
                                    // login(args.openid, constants.openType, args.username);
                                })
                            }
                        }, {uid: oResult1.uid}, {method: 'get', cache_time: 30});
                    }
                }, {}, {method: 'get', cache_time: 30});//默认是post请求方法
            });
            // }
        });
    }

    //新浪登录
    $(document).on('click', '[thirdlogin="sina"]', function() {
        if(WB2.checkLogin()) {
            WB2.logout(function () {
                sinaLogout();
            })
        } else {
            sinaLogout();
        }

        return false;
    });


    //QQ登录
    $(document).on('click', '[thirdlogin="qq"]', function() {

        QC.Login.showPopup({
            appId: constants.qqAppId,
            redirectURI: constants.viewUrl + 'qqLogin.html'
        });

        // if (QC.Login.check()) {//检查是否已登录
        //     //登录成功回调方法
        //     QC.Login.getMe(function (openId, accessToken) {
        //         var args = {
        //             openid: openId,
        //             userHeadImg: reqData.figureurl_qq_2,
        //             access_token: accessToken
        //         };
        //         alert(JSON.stringify(args))
        //     });
        //     QC.api('get_user_info', {}).success(function (userdata) {
        //         //可以获得用户的各种相关信息，如用户昵称
        //         var username = userdata.data.nickname;
        //         alert(JSON.stringify(userdata))
        //
        //     });
        // }
    });
});
