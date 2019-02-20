/*!
 * 阿里裁判列表
 * @author Li Hongwei
 * @date 2018-08-08 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var Page = require('page');

    //阿里裁判列表
    new Page($('#lawList'), {
        url : 'article/aliAdjudicationList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 10
    });
});