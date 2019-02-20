/*!
 * 我的风采
 * @author Li Hongwei
 * @date 2018-07-22 12:12
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

    new Page($('#styleList'), {
        pageType : 'home',
        url : 'article/myMien/{model}/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1,
            userId : userId
        },
        pageSize : 8,
        htmlLoad : function($html){
            require('viewer');
            var viewer = new Viewer(document.getElementById('styleList'));
        }
    });
});
