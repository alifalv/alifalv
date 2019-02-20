/*!
 * 评价
 * @author Li Hongwei
 * @date 2018-10-10 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    require('validator');

    var urlParams = utils.getUrlParams();
    if(null == urlParams.remark || null == urlParams.businessOrder) {
        C.msg.fail('订单编号有误');
        // utils.jump404();
    }
    var loginUserInfo = utils.getLoginUserInfo(true);

    var businessType;

    //需要评价的个数
    var evaluateCount = 0;

    //需要评价个数中已评价的个数
    var evaluatedCount = 0;

    //计算需要评价的个数
    function countEvaluateCount(data) {
        if(data.userA && data.ua == 0) {
            evaluateCount++;
        }
        if(data.userB && data.ub == 0) {
            evaluateCount++;
        }
        if(data.userC && data.uc == 0) {
            evaluateCount++;
        }
    }

    /**
     * 初始化到第一个未评价的
     * @param data
     */
    function initEvaluateUser(data) {

        if (data.userA && data.ua == 0) {
            changeEvalUser(data.userA);

        } else if (data.userB && data.ub == 0) {
            changeEvalUser(data.userB);

        } else if (data.userC && data.uc == 0) {
            changeEvalUser(data.userC);
        }
    }

    function changeEvalUser(_userId) {
        $('a[data-userid]').removeClass('active');
        $('a[data-userid="' + _userId + '"]').addClass('active');

        $('#evaluateldContentBox').html('');
        $('.j-evaluateld').show();
        startRefresh();
        $formOpen.find('[name="evaluateldContent"]').val('');
        $('#userId').val(_userId);
    }

    //查询评价信息
    rest.post({
        url: 'order/listOrderEvaluate',//156.订单列表-去评价
        data : {
            businessOrder: urlParams.businessOrder,// 当前订单的编号（必要）
            remark: urlParams.remark//订单的remark值 传入过来，
        },
        success: function(result) {

            result.userName = loginUserInfo.userName;
            result.userId = loginUserInfo.userId;
            businessType = result.data[0].businessType;
            C.template('#orderTmpl').renderReplace(result, function($html) {

                //切换待评价用户
                $html.on('click', '.j-evalchange-btn', function () {
                    var _userId = $(this).data('userid');
                    changeEvalUser(_userId);
                    return false;
                });

                $html.on('click', '.j-evaled-btn', function () {
                    $('a[data-userid]').removeClass('active');
                    var _userId = $(this).data('userid');
                    $('a[data-userid="' + _userId + '"]').addClass('active');

                    rest.post({
                        form : $formOpen,
                        url: 'Evaluate/listEvaluate',
                        data : {
                            userId : _userId,//咨询师编号
                            businessOrder:urlParams.businessOrder// 当前订单的编号（必要）
                        },
                        success : function(result) {
                            showEvaluated(result.data[0]);
                        },
                        fail : function(result) {
                        }
                    });
                    return false;
                });

                countEvaluateCount(result.data[0]);
                if(evaluateCount == 0) {
                    //如果没有要评价的，就显示第一个已评价的
                    $('.j-evaled-btn:eq(0)').trigger('click');
                } else {
                    initEvaluateUser(result.data[0]);
                }
            });
        },
        fail : function (result) {
            C.msg.fail(result.msg);
        }
    });

    //评价星星事件
    $('.j-starscore').on('click', 'li' , function () {
        var _$thisAllLi = $(this).parent().find('li');
        var index = _$thisAllLi.index(this);
        _$thisAllLi.each(function(i) {
            if(i <= index) {
                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        });
        $(this).closest('.j-starscore').find('input').val(index + 1);
        return false;
    });

    /**
     * 重置星星
     */
    function startRefresh() {
        $('.j-starscore-val').val(1);
        $('.j-starscore li').removeClass('active');
        $('.j-starscore').each(function(i) {
            $(this).find('li:eq(0)').addClass('active');
        });
    }

    /**
     * 显示已评价
     * @param data
     */
    function showEvaluated(data) {
        var names = ['levelScore', 'mannerScore', 'sourceScore', 'scaleScore'];
        for(var i in names) {
            $('input[name="' + names[i] + '"]').closest('.j-starscore').find('.ui-starscore li').each(function () {
                var index = $(this).index();
                if(index < data[names[i]]) {
                    $(this).addClass('active');
                }
            });
        }
        $('#evaluateldContentBox').html(data.evaluateldContent);
        $('.j-evaluateld').hide();
    }


    //注册页面
    var $formOpen = $('#evalform');
    var formOpen_valid = $formOpen.validator(constants.validator);
    formOpen_valid.on('valid.form', function(e, form){
        var loginUserInfo = utils.getLoginUserInfo(true);

        //个人咨询开通会员
        rest.post({
            form : $formOpen,
            url: 'Evaluate/saveEvaluate',//158.订单进行评价；10.11
            data : {
                evaluateUserId : loginUserInfo.userId,
                evaluateType :businessType,//就传入订单的businessType  的内容
                businessOrder:urlParams.businessOrder// 当前订单的编号（必要）
                //form usreId:'',//咨询师的Id，
                //form sourceScore :'',//社会资源的星星，5 星就 传5 ，不参与就传入 0
                //form scaleScore :'',//收费标准
                //form levelScore:'',//法律水平
                //form mannerScore:'',//服务态度
                //form evaluateldContent:''//评价内容
            },
            success: function(result) {
                evaluatedCount++;
                if(evaluatedCount < evaluateCount) {
                    //不是最后提一条未评价的
                    C.msg.ok(result.msg);

                    //改按钮状态为已评价并选中
                    var btn = $('a[data-userid="' + $('#userId').val() + '"]')
                        .removeClass('j-evalchange-btn').removeClass('ui-btn-red-b')
                        .addClass('j-evaled-btn').addClass('ui-btn-green-b').addClass('active');

                    //查询评价信息
                    btn.trigger('click');
                } else {
                    //是最后提一条未评价的评完跳回去
                    C.alert.ok(result.msg, function () {
                        if(window.opener) {
                            window.opener.orderListPage.refresh();
                            window.close();
                        } else {
                            window.location.replace(constants.viewUrl + 'personal_order.html');
                        }
                    });
                }
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
        return false;
    });

});
