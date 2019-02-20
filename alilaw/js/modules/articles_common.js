/*!
 * 常见咨询问题
 * @author Li Hongwei
 * @date 2018-07-01 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var Page = require('page');

    //疑难问题
    new Page($('#lawList'), {
        url : 'article/adviceQuestionList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 10
    });
});