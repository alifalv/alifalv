/*!
 * qq登录与绑定
 * @author Li Hongwei
 * @date 2018-08-05 15:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var store = require('store');

    window.callback = function(user) {
        if(urlParams.userId) {
            bindThreeLoginFlag(urlParams.userId,user.openid, 'QQ');
        } else {
            checkThirdAuthorization(user.openid, 'QQ');
        }
    };

    function bindThreeLoginFlag(userId, openId, openType) {
        rest.post({
            url: 'user/bindThreeLoginFlag',
            data : {
                userId : userId,
                openId  : openId,
                openType : openType
            },
            success: function (result) {
                utils.refreshUserInfoCache(function() {
                    if(window.opener) {
                        window.opener.C.msg.ok(result.msg);
                        var _$this = window.opener.$('#triBox').find('[data-type="QQ"]');
                        _$this.removeClass('j-bind-btn').addClass('j-unbind-btn').html('解 绑');
                        _$this.closest('.j-tri-row').find('.j-tri-type').html('已绑定');
                        window.close();
                    } else {
                        window.location.replace(constants.viewUrl + 'personal_info.html');
                    }
                });
            },
            fail: function (result) {
                if(window.opener) {
                    window.opener.C.msg.fail(result.msg);
                    window.close();
                } else {
                    window.location.replace(constants.viewUrl + 'personal_info.html');
                }
            }

        });

    }


    function checkThirdAuthorization(openId, openType) {
        var index = layer.load();
        rest.post({
            url: 'user/getThreeLoginFlag',
            data : {
                openId  : openId,
                openType : openType
            },
            success: function (result) {
                store.setCache('userInfo', result.data);
                // 绑定了用户，返回登录成功的用户信息，返回结果等同于登录成功

                if(window.opener) {
                    window.opener.location.replace(constants.viewUrl + 'personal_consult.html');
                    window.close();
                } else {
                    window.location.replace(constants.viewUrl + 'personal_consult.html');
                }
            },
            fail: function (result) {
                if(window.opener) {
                    window.opener.location.replace(constants.viewUrl + 'reg_bind.html?key=' + openId + '&type=' + openType);
                    window.close();
                } else {
                    window.location.replace(constants.viewUrl + 'reg_bind.html?key=' + openId + '&type=' + openType);
                }
            },
            complete : function () {
                layer.close(index);
            }

        });

    }

        var accessToken = window.location.hash.substring(1);
        //使用Access Token来获取用户的OpenID
        var path = "https://graph.qq.com/oauth2.0/me?";
        var queryParams = [accessToken, 'callback=callback'];
        var query = queryParams.join('&');
        var url = path + query;
        var script = document.createElement('script');
        script.src = url;
        document.body.appendChild(script);
});
