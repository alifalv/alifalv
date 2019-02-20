/*!
 * 我的随笔
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var Page = require('page');

    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var userId = urlParams.userId;
    if (null == userId || '' == userId) {
        utils.jump404('用户编号为空');
    }

    new Page($('#essayList'), {
        pageType: 'home',
        url: 'article/myEssay/{model}/{userId}/{pageNo}/{pageSize}',
        urlParams: {
            model: 1,
            userId: userId
        },
        pageSize: 6

    });
    // 个人信息
    rest.post({
        url: 'user/userInfo/{userId}',
        urlParams: {
            userId: userId
        },
        success: function(result) {
            $(document).attr("title", '阿里法律 - ' + result.data.cityName + result.data.realName + '律师的随笔');
        }
    });
});