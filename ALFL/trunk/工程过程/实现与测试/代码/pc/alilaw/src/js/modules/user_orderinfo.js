/*!
 * 订单详情
 * @author Li Hongwei
 * @date 2018-10-11 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    require('validator');

    var urlParams = utils.getUrlParams();
    if(!urlParams.businessOrder) {
        utils.jump404('订单编号为空');
    }
    utils.getLoginUserInfo(true);


    //查询评价信息
    rest.post({
        url: 'order/findOrderDetails',//145. 文书制作按钮;9.30
        data : {
            businessOrder: urlParams.businessOrder// 当前订单的编号
        },
        success: function(result) {
            if(result.data) {
                C.template('#orderTmpl').renderReplace(result, function() {});
            } else {
                utils.jump404('订单信息不存在');
            }
        },
        fail : function (result) {
            utils.jump404(result.msg);
        }
    });


});
