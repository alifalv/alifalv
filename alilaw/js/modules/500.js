/*!
 * 500页面
 * @author Li Hongwei
 * @date 2018-07-29 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    $('#txt').html(urlParams.txt || '服务器异常');

});