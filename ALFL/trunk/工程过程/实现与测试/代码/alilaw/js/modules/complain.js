/*!
 * 百姓吐槽
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";


    var Page = require('page');
    new Page($('#complainlist'), {
        url : 'article/commonPeopleList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model:1
        },
        pageSize : 20
    });

});
