/*!
 * 我的随笔
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var Page = require('page');

    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var userId = urlParams.userId;
    if(null == userId || '' == userId) {
        utils.jump404('用户编号为空');
    }

    new Page($('#essayList'), {
        pageType : 'home',
        url : 'article/myEssay/{model}/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1,
            userId : userId
        },
        pageSize : 6
    });

});
