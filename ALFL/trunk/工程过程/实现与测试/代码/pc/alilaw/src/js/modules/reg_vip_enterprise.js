/*!
 * 企业会员注册
 * @author Li Hongwei
 * @date 2018-07-02 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');

    var urlParams = utils.getUrlParams();
    var userId = urlParams.userId;
    //防止直接访问
    if(userId == null || userId == '') {
        window.location.replace(constants.viewUrl + 'reg_enterprise.html');
    }
    var constants = require('constants');
    var MAX_YEAR = 12;
    var MIN_YEAR = 1;

    function selectedVipType(_$this) {
        $('.j-viptype-btn').removeClass('active');
        _$this.addClass('active');
    }

    //会员类型选择事件
    $('.j-viptype-btn').on('click', function () {
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

    $('.j-kaitong-btn').on('click', function () {
        var _$viptype_btn= $(this).closest('.j-viptype-btn');
        selectedVipType(_$viptype_btn);

        openVipByCompany(0, getVipLevel(), userId, getOpenYear(), function (result) {
            C.alert.tips('正在支付中', function () {});
            utils.jumpPay(result.data.businessOrder, function () {
                location.replace(constants.viewUrl + 'reg_success.html')
            });
        })
    });
});
