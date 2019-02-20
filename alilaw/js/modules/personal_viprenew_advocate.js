/*!
 * 普通用户续费
 * @author Li Hongwei
 * @date 2018-11-09 00:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    utils.checkUserType(constants.userType.advocate);


    require('validator');


    var loginUserInfo = utils.getLoginUserInfo(true);
    var userId = loginUserInfo.userId;


    var $vipLevelList = $('#vipLevelList');
    var $driverLevelList = $('#driverLevelList');
    var $year = $('#year');

    var isFirstVip = false; //是否首充
    var bcjState = false; //是否补差价状态
    var $formOpen = $('#formOpen');


    //最后连续充值年份
    var lastContinuousYear = 0;

    //已消费积分
    var consumptionIntegral = 0;

    var amount = 0; //总金额
    var year = 0;
    var driverLevelAmount = 0;

    initInfo();


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
        if (!_$this.hasClass('ui-disabled')) {
            $driverLevelList.find('.btn').removeClass('ui-checked');
            _$this.addClass('ui-checked');
            $('input[name="carType"]').val(_$this.data('id'));
            driverLevelAmount = parseInt(_$this.data('amount'));
            calculateAmount();
        }
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


    //注册页面
    var formOpen_valid = $formOpen.validator(constants.validator);
    formOpen_valid.on('valid.form', function(e, form) {
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var _openType = getOpenType();

        if (0 == _openType) {
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
                    var _alert = C.alert.tips('正在支付中', function() {});
                    utils.jumpPay(result.data.businessOrder, function() {
                        layer.close(_alert);
                        C.alert.ok('开通成功!', function() {
                            location.reload();
                        });
                    });
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                }
            });
        } else if (1 == _openType) {
            continueVip(getOpenYear(), _loginUserInfo.userId, function(re) {
                var _alert = C.alert.tips('正在支付中', function() {});
                utils.jumpPay(re.data.businessOrder, function() {
                    layer.close(_alert);
                    C.alert.ok('开通成功!', function() {
                        location.reload();
                    });
                });
            })
        }

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

    //补差价按钮
    $('.j-bcj-btn').on('click', function() {
        var loginUserInfo = utils.getLoginUserInfo(true);

        if (bcjState) {
            bbcj();
        } else {
            bcj();
            var userId = loginUserInfo.userId;
            var vipLevel = getVipLevel();
            queryPersonAutoRenew(vipLevel, userId, function(result) {
                var tipsHtml = '到期时间' + result.data.expireTime + '需要补的金额:' + result.data.money + '升级的等级名称：' + result.data.levelName;
                $('.j-tips').html(tipsHtml);
            });
        }
        return false;
    });


    function bcj() {
        bcjState = true;
        $('.j-bcj-btn').removeClass('ui-btn-gray-b').removeClass('ui-color-darkgray').addClass('ui-btn-red').addClass('ui-color-white');
    }

    //不补差价
    function bbcj() {
        bcjState = false;
        $('.j-bcj-btn').addClass('ui-btn-gray-b').addClass('ui-color-darkgray').removeClass('ui-btn-red').removeClass('ui-color-white');
    }


    function initInfo() {
        var vipLevelTexts = ['非会员', '黄金会员', '白金会员', '钻石会员'];
        $('.j-now-viplevel').html(vipLevelTexts[parseInt(loginUserInfo.vipLevel)]);


        //后台说的这个字段是空就是未开通过会员
        if (null === loginUserInfo.expireTime) {
            isFirstVip = true;
            console.log('是首充，expireTime：', loginUserInfo.expireTime);
            console.log('已认证，isAuthentication：', loginUserInfo.isAuthentication);

            //删除续充
            $('.j-xc-box').remove();

            if (loginUserInfo.isAuthentication == '0') {

                //后台说的!没有认证资料的才需要填资料
                $('.j-first-box').removeClass('ui-hide');
            } else {
                console.log('realName：', loginUserInfo.realName);
                console.log('idCard：', loginUserInfo.idCard);
                console.log('idCardFront：', loginUserInfo.idCardFront);
                console.log('idCardBack：', loginUserInfo.idCardBack);

                //已提交认证信息的（指除未认证状态以外的状态）， 查询提交的认证信息用来调首次会员
                $formOpen.find('input[name="realName"]').val(loginUserInfo.realName);
                $formOpen.find('input[name="idCard"]').val(loginUserInfo.idCard);
                $formOpen.find('input[name="idCardFront"]').val(loginUserInfo.idCardFront);
                $formOpen.find('input[name="idCardBack"]').val(loginUserInfo.idCardBack);
                $('.j-first-box').addClass('ui-hide');
            }
        } else {
            console.log('是续充，expireTime：', loginUserInfo.expireTime);

            $('#driverLevelList .ui-vipform-btn').removeClass('ui-checked');
            $('#driverLevelList .ui-vipform-btn').each(function() {
                var $this = $(this);
                if ($this.data('id') == loginUserInfo.carType) {
                    $this.addClass('ui-checked');
                } else {
                    $this.addClass('ui-disabled');
                }
            });

            isFirstVip = false;
            $('.j-xc-box').removeClass('ui-hide');
            $('.j-first-box').remove();
        }
    }


    /**
     * 律师与普通用户补差价查询
     * @param vipLevel 需要升级的等级 【 0：基础会员；1：黄金会员; 2:白金会员 ； 3：钻石会员】
     * @param userId 用户Id
     * @param func
     */
    function queryPersonAutoRenew(vipLevel, userId, func) {
        rest.post({
            url: 'user/queryPersonAutoRenew',
            data: {
                vipLevel: vipLevel,
                userId: userId
            },
            success: function(result) {
                func && func(result)
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    }

    /**
     * 165.普通用户和律师续充Vip；11.9
     * @param vipLevel
     * @param userId
     * @param func
     */
    function continueVip(openYear, userId, func) {
        rest.post({
            url: 'user/continueVip',
            async: false,
            data: {
                openYear: openYear,
                userId: userId
            },
            success: function(result) {
                func && func(result)
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    }

    function getVipLevel() {
        return parseInt($('#vipLevelList .ui-vipform-btn.ui-checked').data('id'));
    }


    function getOpenYear() {
        return parseInt($('#year').val());
    }

    /**
     *  充值类型
     * @return {*} 0:首冲， 1:续充， 2:补差价
     */
    function getOpenType() {
        var type;
        if (isFirstVip) {
            type = 0;
        } else {
            if (bcjState) {
                type = 2;
            } else {
                type = 1;
            }
        }
        return type;
    }

});