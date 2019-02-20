/*!
 * 文书制作卷（已使用）
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var loginUserInfo = utils.getLoginUserInfo(true);
    var loginUserId = loginUserInfo.userId;
    var Page = require('page');

    //已使用优惠卷列表
    var $voucherList = $('#voucherList');
    var voucherListPage = new Page($voucherList, {
        pageType : 'home',
        url : 'user/voucherList/{pageNo}/{pageSize}',
        data : {
            state : 3,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
            userId: loginUserId
        },
        pageSize : 8
    });


    function initCount() {
        rest.post({
            url: 'user/voucherCount',//147.免费文书制作申请;10.8
            data : {
                state : 0,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                userId: loginUserId
            },
            success: function (result) {
                $('#notused_count').html(result.data.voucherCount)
            }
        });
        rest.post({
            url: 'user/voucherCount',//147.免费文书制作申请;10.8
            data : {
                state : 3,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                userId: loginUserId
            },
            success: function (result) {
                $('#used_count').html(result.data.voucherCount)
            }
        });
        rest.post({
            url: 'user/voucherCount',//147.免费文书制作申请;10.8
            data : {
                state : 2,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                userId: loginUserId
            },
            success: function (result) {
                $('#expired_count').html(result.data.voucherCount)
            }
        });
    }

    initCount();
});
