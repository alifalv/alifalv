/*!
 * 会员注册普通用户
 * @author Li Hongwei
 * @date 2018-07-02 11:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    require('validator');
    var urlParams = utils.getUrlParams();



    var userId = urlParams.userId;

    //防止直接访问
    if (userId == null || userId == '') {
        window.location.replace(constants.viewUrl + 'reg_advocate.html');
    }

    //先写死
    // var dataList = require('dataList');
    // dataList.getDriverLevelList(function() {
    //     $('#driverLevelList ')
    // });
    // dataList.getVipLevelList(function() {
    //     $('#driverLevelList ')
    // });

    var $vipLevelList = $('#vipLevelList');
    var $driverLevelList = $('#driverLevelList');
    var $year = $('#year');


    //最后连续充值年份
    var lastContinuousYear = 0;

    //已消费积分
    var consumptionIntegral = 0;

    var amount = 0; //总金额
    var year = 0;
    var driverLevelAmount = 0;

    //计算总额
    function calculateAmount() {
        amount = year * driverLevelAmount;
        $('#amount').html(amount + '元');
    }

    //初始化默认值
    function initAmount() {
        year = parseInt($year.val());
        driverLevelAmount = parseInt($driverLevelList.find('.btn.ui-checked').data('amount'));
        calculateAmount();
    }

    //会员类型按钮点击事件
    $vipLevelList.on('click', '.btn', function(e) {
        var _$this = $(this);
        if (!_$this.hasClass('ui-disabled')) {
            $vipLevelList.find('.btn').removeClass('ui-checked');
            _$this.addClass('ui-checked');
            $('input[name="vipLevel"]').val(_$this.data('id'));
            calculateAmount();
        }
        return false;
    });

    //车辆类型按钮点击事件
    $driverLevelList.on('click', '.btn', function(e) {
        var _$this = $(this);
        $driverLevelList.find('.btn').removeClass('ui-checked');
        _$this.addClass('ui-checked');
        $('input[name="carType"]').val(_$this.data('id'));
        driverLevelAmount = parseInt(_$this.data('amount'));
        calculateAmount();
    });

    //年份下拉菜单选择事件
    $year.on('change', function() {
        year = parseInt($(this).val());
        if (year >= 3) { //首次购买3年自动升级为白金会员
            $('#ptvip_btn').removeClass('ui-disabled');
        } else {
            $('#ptvip_btn').addClass('ui-disabled');
        }

        calculateAmount();
    });

    initAmount();

    require('imgUpload');
    $("#idcardzm").createImgUpload({
        formData: {
            // "path": "artScene/",
            "name": 'idCardFront'
        },
        imageNum: 1,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    $("#idcardbm").createImgUpload({
        formData: {
            // "path": "artScene/",
            "name": 'idCardBack'
        },
        imageNum: 1,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    var infoDialog = null;
    $('#supInfo').on('click', function() {
        infoDialog = C.dialog({
            area: ['900px', '680px'],
            content: $('#myModal') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
        });
    });

    var $formOpen = $('#formOpen');

    //注册页面
    var formOpen_valid = $formOpen.validator(constants.validator);
    formOpen_valid.on('valid.form', function(e, form) {
        //个人咨询开通会员
        rest.post({
            url: 'user/openVipByConsultant',
            form: $formOpen,
            async: false,
            data: {
                userId: userId,
                userType: 1 //1：普通用户  ； 2 ： 律师
            },
            success: function(result) {
                //{"msg":"资料已提交成功，支付订单已生成.","code":1,"data":{"orderType":1,"isPay":2,"businessOrder":"20181028133730922165","totalPrice":400,"sign":"{\"type\":\"初次\",\"userType\":\"1\"}","orderNum":1,"remark":"","userName":"李珊","userId":89,"orderState":1,"isComplain":0,"sender":0,"orderPrice":400,"businessType":"会员充值","reception":0,"evaluate":0,"orderName":"黄金会员"}}

                C.alert.tips('正在支付中', function() {});
                utils.jumpPay(result.data.businessOrder, function() {
                    location.replace(constants.viewUrl + 'reg_advocate_success.html?type=' + result.data.orderName)
                });
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    });

    //补充资料
    var valid_options = constants.validator;
    valid_options.theme = 'yellow_bottom';
    var form_valid = $(document).find('#form').validator(valid_options);
    form_valid.on('valid.form', function(e, form) {

        var formData = $(this).serializeObject();
        $formOpen.find('input[name="realName"]').val(formData.realName);
        $formOpen.find('input[name="idCard"]').val(formData.idCard);
        $formOpen.find('input[name="idCardFront"]').val(formData.idCardFront || '');
        $formOpen.find('input[name="idCardBack"]').val(formData.idCardBack || '');
        layer.close(infoDialog);

    });

});