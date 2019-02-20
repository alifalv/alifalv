/*!
 * 常用文书模板列表
 * @author Li Hongwei
 * @date 2018-09-01 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var Page = require('page');


    new Page($('#bookMakeModelList'), {
        url : 'article/bookMakeModelList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 10
    });
});