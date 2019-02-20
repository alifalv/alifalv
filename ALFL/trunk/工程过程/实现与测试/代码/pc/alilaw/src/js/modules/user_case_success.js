/*!
 * 发布我要吐槽
 * @author Li Hongwei
 * @date 2018-07-14 15:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();

    //不存在文章编号
    var caseId = urlParams.caseId;
    if(null == caseId || '' == caseId) {
        utils.jump404('文章编号为空');
        return false;
    }
    var constants = require('constants');

    $('#case_btn').attr('href', constants.viewUrl + 'casebiddinginfo.html?caseId=' + caseId);

});
