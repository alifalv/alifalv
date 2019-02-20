/*!
 * 今日推荐
 * @author Li Hongwei
 * @date 2018-08-08 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var Page = require('page');

    //阿里裁判列表
    new Page($('#lawList'), {
        url : 'article/listArticle/{model}/{pageNo}/{pageSize}',
        data : {
            articleState:1//必需的值；
        },
        urlParams : {
            model: 1
        },
        pageSize : 10
    });
});