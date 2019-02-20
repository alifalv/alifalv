/*!
 * 我竟拍的案件
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var userInfo = utils.getLoginUserInfo(true);
    var userId = userInfo.userId;

    var Page = require('page');
    new Page($('#myOfferCaseList'), {
        pageType : 'home',
        url : 'case/myOfferCase/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            userId: userId
        },
        pageSize : 8
    });

});
