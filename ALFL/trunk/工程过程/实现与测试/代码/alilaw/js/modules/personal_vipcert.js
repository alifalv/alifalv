/*!
 * VIP证书
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');

    var $form = $('#form');

    var loginUserInfo = utils.getLoginUserInfo(true);


    C.template('#vipCertTmpl').renderReplace(loginUserInfo, function($html) {
    });
});
