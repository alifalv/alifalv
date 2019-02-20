/*!
 * 企业会员升级
 * @author Li Hongwei
 * @date 2018-11-09 00:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    utils.checkUserType(constants.userType.enterprise);


    require('validator');
    var MAX_YEAR = 12;
    var MIN_YEAR = 1;
    var bcjState = false;//是否补差价状态
    var loginUserInfo = utils.getLoginUserInfo(true);
    var isFirstVip = false;

    initInfo();

    var $form = $('#form');
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        var loginUserInfo = utils.getLoginUserInfo(true);
        openVipByCompany(getOpenType(), getVipLevel(), loginUserInfo.userId, getOpenYear(), function (result) {
            var _alert = C.alert.tips('正在支付' ,function () {});
            utils.jumpPay(result.data.businessOrder, function () {
                layer.close(_alert);
                C.alert.ok('开通成功', function () {
                    location.reload();
                });
            });
        })
    });

    //补差价按钮
    $('.j-bcj-btn').on('click', function () {
        var loginUserInfo = utils.getLoginUserInfo(true);

        if(bcjState) {
            bbcj();
        } else {
            bcj();
            var userId = loginUserInfo.userId;
            var vipLevel = getVipLevel();
            bargainMoney(vipLevel, userId, function(result) {

                var tipsHtml = '到期时间' + result.data.expireTime + '需要补的金额:' + result.data.money +  '升级的等级名称：' + result.data.levelName
                $('.j-tips').html(tipsHtml);
            });
        }
        return false;
    });


    function selectedVipType(_$this) {
        // var _$this = $(this);
        var vipType = _$this.data('viptype');
        var amount = parseInt(_$this.data('amount'));

        $('.j-viptype-btn').removeClass('active');
        _$this.addClass('active');
    }

    //会员类型选择事件
    $('.j-viptype-btn').on('click', function () {
        // var _$this = $(this);
        // var vipType = _$this.data('viptype');
        // var amount = parseInt(_$this.data('amount'));
        //
        // $('.j-viptype-btn').removeClass('active');
        // _$this.addClass('active');

        selectedVipType($(this));
        return false;
    });

    //增加会员年份事件
    $('.j-add').on('click', function () {
        var _$this = $(this);
        var _$parent = _$this.closest('.j-year-select');
        var _$year_val = _$parent.find('.j-year-val');
        var _now_year_val = parseInt(_$year_val.html());
        if(_now_year_val < MAX_YEAR) {
            _now_year_val = _now_year_val + 1;
            _$year_val.html(_now_year_val);
        }

        var _$viptype_btn= _$this.closest('.j-viptype-btn');
        selectedVipType(_$viptype_btn);

        //计算券张数
        var _index = $('.j-viptype-btn').index(_$viptype_btn);
        var _couponNum = constants.enterpriseYearCouponNum[_index];
        _$viptype_btn.find('.j-coupon-num').html(_couponNum * _now_year_val);

        //计算金额
        _$viptype_btn.find('.j-amount-val').html(_now_year_val *  parseInt(getVipTypeAmount()));
        return false;
    });

    //减少会员年份事件
    $('.j-sub').on('click', function () {
        var _$this = $(this);
        var _$parent = _$this.closest('.j-year-select');
        var _$year_val = _$parent.find('.j-year-val');
        var _now_year_val = parseInt(_$year_val.html());
        if(_now_year_val > MIN_YEAR) {
            _now_year_val = _now_year_val - 1;
            _$year_val.html(_now_year_val);
        }

        var _$viptype_btn= _$this.closest('.j-viptype-btn');
        selectedVipType(_$viptype_btn);

        //计算券张数
        var _index = $('.j-viptype-btn').index(_$viptype_btn);
        var _couponNum = constants.enterpriseYearCouponNum[_index];
        _$viptype_btn.find('.j-coupon-num').html(_couponNum * _now_year_val);

        _$viptype_btn.find('.j-amount-val').html(_now_year_val *  parseInt(getVipTypeAmount()));
        return false;
    });



    /**
     * 企业用户补差价的查询接口
     * @param vipLevel 需要升级的等级 【 0：基础会员；1：黄金会员; 2:白金会员 ； 3：钻石会员】
     * @param userId
     * @param func
     */
    function bargainMoney(vipLevel, userId, func) {
        rest.post({
            url: 'user/queryCompanyAutoRenew',
            data : {
                vipLevel: vipLevel,
                userId : userId
            },
            success: function(result) {
                func && func(result)
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    }

    /**
     * 企业咨询者开通会员
     * @param type 开通时的类型【0：初次；1：续充；2补差价】
     * @param vipLevel
     * @param userId
     * @param openYear 开通年数
     * @param func
     */
    function openVipByCompany(type, vipLevel, userId, openYear, func) {
        rest.post({
            url: 'user/openVipByCompany',
            async : false,
            data : {
                vipLevel: vipLevel,
                userId : userId,
                openYear : openYear,
                type : type
            },
            success: function(result) {
                func && func(result)
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    }

    function getVipLevel() {
       return parseInt($('.j-viptype-btn.active').data('viptype'));
    }

    function getVipTypeAmount() {
        return parseInt($('.j-viptype-btn.active').data('amount'));
    }

    function getOpenYear() {
        return parseInt($('.j-viptype-btn.active .j-year-val').html());
    }

    function getOpenType() {
        var type;
        if(isFirstVip) {
            type = 0;
        } else {
            if(bcjState) {
                type = 2;
            } else {
                type = 1;
            }
        }
        return type;
    }




    function initInfo() {
        var vipLevelTexts = ['非会员', '黄金会员', '白金会员', '钻石会员'];
        $('.j-now-viplevel').html(vipLevelTexts[parseInt(loginUserInfo.vipLevel)]);

        if(null === loginUserInfo.expireTime) {
            isFirstVip = true;
            $('.j-bcj-btn').addClass('ui-hide');
        } else {
            $('.j-bcj-btn').removeClass('ui-hide');
        }
    }


    function bcj() {
        bcjState = true;
        $('.j-bcj-btn').removeClass('ui-btn-gray-b').removeClass('ui-color-darkgray').addClass('ui-btn-red').addClass('ui-color-white');
    }

    //不补差价
    function bbcj() {
        bcjState = false;
        $('.j-bcj-btn').addClass('ui-btn-gray-b').addClass('ui-color-darkgray').removeClass('ui-btn-red').removeClass('ui-color-white');
    }

    function changeBtnColor() {

    }

});
