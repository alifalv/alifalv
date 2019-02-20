/*!
 * 案例判罚
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var Page = require('page');

    //阿里裁决
    //{"total":1,"code":1,"records":2,"page":1,"rows":[{"isPush":1,"isDelete":0,"articleId":"6","isofficial":0,"title":"吐槽2","userId":1,"articleState":1,"sendTime":"2018-07-19 21:50:40","realName":"小康","txtPath":"sadasdsdfasd","columnCode":"bxtc","isOutJoin":0,"lookNum":3},{"isPush":1,"isDelete":0,"articleId":"5","isofficial":0,"title":"吐槽1","userId":1,"articleState":1,"sendTime":"2018-07-18 21:48:50","realName":"小康","txtPath":"sadasd","columnCode":"bxtc","isOutJoin":0,"lookNum":23}]}
    new Page($('#sentenceList'), {
        url : 'article/commonPeopleList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 20
    });

});
