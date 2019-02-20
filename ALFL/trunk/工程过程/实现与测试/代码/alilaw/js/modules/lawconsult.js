/*!
 * 法律咨询
 * @author Li Hongwei
 * @date 2018-07-01 11:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var Page = require('page');
    require('WdatePicker');

    //咨询列表
    new Page($('#adviceList'), {
        url: 'advice/adviceList/{pageNo}/{pageSize}',
        pageSize: 15
    });


    var dataList = require('dataList');
    dataList.getCaseList(function(result) {
        C.template('#case_tmpl').renderReplace(result, function() {});
    });


    rest.post({
        url: 'user/adminRecommendCounselorList', //132.律师推荐列表
        success: function(result) {
            var showIndex = 8;

            if (result.data && result.data.length < showIndex) {
                for (var i = result.data.length; i < showIndex; i++) {
                    result.data.push({ userName: '诚邀律师', provinceName: '欢 迎', cityName: '联 系', mobile: '0731-85580003' });
                }
            }
            result.data = $.extend([], result.data.slice(0, showIndex));
            C.template('#counselorRecommendListTmpl').renderReplace(result, function() {
                $('#counselorRecommendList img').error(function() {
                    $(this).attr('src', '../img/head-defalut.png');
                })
            });
        },
        fail: function(result) {
            C.msg.fail(result.msg);
        }
    });



    //右侧地区律师推荐
    var store = require('store');
    var $cityCounselorRecommendList = $('#cityCounselorRecommendList');
    var cityId = store.getCache('cityId');
    var cityName = store.getCache('cityName');
    $cityCounselorRecommendList.find('.j-citname').html(cityName);
    rest.post({
        url: 'user/counselorRecommendList', //【案源详情的竞拍】-律师发起竞拍
        data: {
            city: cityId
        },
        success: function(result) {
            var showIndex = 6;
            result.data = $.extend([], result.data.slice(0, showIndex));
            C.template('#cityCounselorRecommendListTmpl').renderReplace(result, function($html) {
                $html.find('img').one('error', function() {
                    $(this).attr('src', '../img/img-error.jpg');
                });
            });
        }
    });
});