/*!
 * 文章搜索
 * @author Li Hongwei
 * @date 2018-08-22 17:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var Page = require('page');

    var urlParams = utils.getUrlParams();

    var searchKeywords = urlParams.s||'';

    $('#allsearch_text').val(searchKeywords);

    // 站内搜索（法律法规，法律咨询）
    new Page($('#searchList'), {
        url : 'user/localSearch/{pageNo}/{pageSize}',
        data : {
            searchKeywords : searchKeywords//搜索内容
        },
        pageSize : 10,
        addData : {
            searchKey :  searchKeywords
        }
    });
});