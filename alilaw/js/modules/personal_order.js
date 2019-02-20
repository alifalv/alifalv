/*!
 * 我的订单
 * @author Li Hongwei
 * @date 2018-10-11 16:50
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var loginUserInfo = utils.getLoginUserInfo(true);
    var constants = require('constants');
    var Page = require('page');
    var $orderList = $('#orderList');
    require('validator');


    //订单列表
    var orderListPage = new Page($orderList, {
        pageType : 'home',
        url : 'order/listBusinessOrder/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model:1
        },
        data : {
            userId : loginUserInfo.userId
        },
        addData : {
            userType : loginUserInfo.userType
        },
        pageSize : 5
    });

    window.orderListPage = orderListPage;



    //订单申诉（文书制作）
    var infoDialog;
    $orderList.on('click', '.j-complain-btn', function () {
        utils.getLoginUserInfo(true);
        var businessOrder = $(this).data('businessorder');
        $formOpen.find('input[name="businessOrder"]').val(businessOrder);
        infoDialog = C.dialog({
            title : '订单申诉',
            area: ['500px', '430px'],
            content: $('#myModal')
        });
        return false;
    });


    //订单申诉
    var $formOpen = $(document).find('#complainForm');
    var valid_options = constants.validator;
    valid_options.theme = 'yellow_bottom';
    var form_valid = $formOpen.validator(valid_options);
    form_valid.on('valid.form', function(e, form){
        rest.post({
            form : $(form),
            url: 'order/complain',//140. 订单申诉;9.30
            //businessOrder: businessOrder,//当前订单的编号（必要）
            //complainType: complainType,//申诉的类型  （必要，字符型）
            //complainContent: complainContent//申诉的内容
            success: function(result) {
                C.msg.ok(result.msg);
                layer.close(infoDialog);
                orderListPage.refresh();
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

    //取消订单
    $orderList.on('click', '.j-close-btn', function () {
        utils.getLoginUserInfo(true);
        var businessOrder = $(this).data('businessorder');
        var userId = $(this).data('userid');
        C.confirm('确认取消订单？',function () {
            cancelOrder(businessOrder, userId);
        });
    });

    //去评价
    // $orderList.on('click', '.j-evaluation-btn', function () {
    //
    // });

    //发货确认
    $orderList.on('click', '.j-reception-btn', function () {
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var businessOrder = $(this).data('businessorder');
        var favoreeUserId = _loginUserInfo.userId;
        C.confirm('确认发货？',function () {
            sender(businessOrder, favoreeUserId);
        });
    });

    //收货确认
    $orderList.on('click', '.j-sender-btn', function () {
        utils.getLoginUserInfo(true);
        var businessOrder = $(this).data('businessorder');
        var userId = $(this).data('userid');
        C.confirm('确认收货？',function () {
            reception(businessOrder, userId);
        });
    });

    //完成确认
    $orderList.on('click', '.j-complete-btn', function () {
        utils.getLoginUserInfo(true);
        var businessOrder = $(this).data('businessorder');
        C.confirm('确认完成？',function () {
            caseComplete(businessOrder);
        });
    });

    /**
     * 收货确认(法律文书制作)
     * @param businessOrder 当前订单的编号
     * @param userId 文书申请人的userId
     */
    function reception(businessOrder, userId) {
        rest.post({
            url: 'order/reception',//143.订单列表系列中 【法律文书制作： 确认收货按钮功能 】;9.30
            data: {
                businessOrder: businessOrder,//当前订单的编号
                userId: userId//文书申请人的userId
            },
            success: function (result) {
                C.msg.ok(result.msg);
                orderListPage.refresh();
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    }

    /**
     * 发货确认(法律文书制作)
     * @param businessOrder 当前订单的编号
     * @param favoreeUserId 文书制作人的Id
     */
    function sender(businessOrder, favoreeUserId) {
        rest.post({
            url: 'order/sender',//142. 订单列表系列中 【法律文书制作： 确认发货按钮功能 】;9.30
            data: {
                businessOrder: businessOrder,//当前订单的编号
                favoreeUserId: favoreeUserId//文书制作人的Id
            },
            success: function (result) {
                C.msg.ok(result.msg);
                orderListPage.refresh();
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    }

    /**
     * 取消订单
     * @param businessOrder 当前订单的编号
     * @param userId 文书申请人的userId
     */
    function cancelOrder(businessOrder, userId) {
        rest.post({
            url: 'order/cancelOrder',//144.订单列表系列中 【法律文书制作： 取消按钮功能 】;9.30
            data : {
                businessOrder: businessOrder,//当前订单的编号
                userId: userId//文书申请人的userId
            },
            success: function(result) {
                C.msg.ok(result.msg);
                orderListPage.refresh();
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    }


    //完成确认（案件委托）
    function caseComplete(businessOrder) {
        rest.post({
            url: 'case/caseDeputeAccomplish',//141.订单列表系列中 【案件委托： 确认完成按钮功能 】;9.30
            data : {
                businessOrder: businessOrder//当前订单的编号
            },
            success: function(result) {
                C.msg.ok(result.msg);
                orderListPage.refresh();
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    }

});
