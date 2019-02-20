/*!
 *
 * @author Li Hongwei
 * @date 2018-08-06 14:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";

    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var userId = urlParams.userId;
    if (null == userId || '' == userId) {
        utils.jump404('用户编号为空');
    }

    rest.post({
        url: 'user/userInfo/{userId}',
        urlParams: {
            userId: userId
        },
        success: function(result) {
            $(document).attr("title", '阿里法律 - ' + result.data.cityName + result.data.realName + '律师的个人简介');
            C.template('#homeAboutTmpl').renderReplace(result, function() {});
        }
    });

});