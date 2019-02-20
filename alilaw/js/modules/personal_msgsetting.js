/*!
 * 消息设置
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var loginUserInfo = utils.getLoginUserInfo(true);

    rest.post({
        url: 'user/msgSeacher',
        data : {
            userId  : loginUserInfo.userId
        },
        success: function (result) {
            C.template('#infoTmpl').renderReplace(result, function($html) {

                $html.find('[name="replymsg"]').on('change', function() {
                    var loginUserInfo = utils.getLoginUserInfo(true);
                    msgSetting(loginUserInfo.userId, 0, $(this).val());
                    return false;
                });

                $html.find('[name="sysmsg"]').on('change', function() {
                    var loginUserInfo = utils.getLoginUserInfo(true);
                    msgSetting(loginUserInfo.userId, 1, $(this).val());
                    return false;
                });
            });
        },
        fail: function (result) {
            C.msg.fail(result.msg);
        }
    });

    /**
     * 消息设置
     * @param userId
     * @param type 1：系统消息 0：回复消息
     * @param state 0：不接收，1接收
     */
    function msgSetting(userId, type, state) {
        rest.post({
            url: 'user/msgSetting',
            data : {
                userId  : userId,
                type : type,
                state: state
            },
            success: function (result) {
                C.msg.ok(result.msg, function () {

                });
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    }


});
