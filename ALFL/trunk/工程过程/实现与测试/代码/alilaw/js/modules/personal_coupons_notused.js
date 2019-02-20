/*!
 * 文书制作卷（未使用）
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

    var $voucherList = $('#voucherList');

    //未使用优惠卷列表
    var voucherListPage = new Page($voucherList, {
        pageType : 'home',
        url : 'user/voucherList/{pageNo}/{pageSize}',
        data : {
            state : 0,//查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
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

    //使用优惠卷事件
    $voucherList.on('click', '.j-use-btn', function() {
        var _voucherId = $(this).data('voucherid');
        var $use_dialog = $('#use_dialog');
        var use_dialog = C.dialog({
            title:false,
            closeBtn: false,
            area: ['700px', '350px'],
            content: $use_dialog //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
        });
        $use_dialog.find('.js-close-btn').on('click', function() {
            layer.close(use_dialog);
        });

        var isSending = false;
         $use_dialog.find('.js-ok-btn').on('click', function() {
            if(isSending) {
                return false;
            }
            var load = C.load();
            isSending = true;
            rest.post({
                url: 'user/freeMakevoucher',//147.免费文书制作申请;10.8
                data : {
                    userId: loginUserInfo.userId, //用户Id
                    voucherId:_voucherId//劵id
                },
                success: function (result) {
                    C.alert.ok('您的申请已经提交成功，平台将尽快委派律师与您联系！',function () {
                        layer.close(use_dialog);
                        voucherListPage.refresh();
                    });
                },
                fail: function (result) {
                    C.alert.fail(result.msg);
                },
                complete :function () {
                    isSending = false;
                    layer.close(load);
                }
            });
        });
    });
});
