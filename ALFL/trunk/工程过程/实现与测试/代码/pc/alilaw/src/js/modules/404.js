/*!
 * 404页面
 * @author Li Hongwei
 * @date 2018-07-29 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    if(urlParams.txt) {
        $('#txt').html('(' + urlParams.txt + ')');
    }
});