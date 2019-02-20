/*!
 * 案源竞拍列表
 * @author Li Hongwei
 * @date 2018-07-14 15:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";

    var constants = require('constants');
    var Page = require('page');

    var dataList = require('dataList');
    dataList.getCaseList(function(result) {
        C.template('#case_tmpl').renderReplace(result, function() {});
    });

    dataList.getProvinceList(function(result) {
        C.template('#province_tmpl').renderReplace(result, function() {
            $('#province_select').trigger('change');
        });
    });

    $('#province_select').on('change', function() {
        var province_select_val = $('#province_select').val();
        if (province_select_val == '') {
            C.template('#city_tmpl').renderReplace({}, function() {});
            return false;
        }
        rest.post({
            url: 'address/cityListByProvince/{pid}',
            urlParams: {
                pid: province_select_val
            },
            success: function(result) {
                C.template('#city_tmpl').renderReplace(result, function() {});
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
        return false;
    });


    //案源竞拍
    var $caseList = $('#caseList');

    var caseListPage = new Page($caseList, {
        url: 'case/caseList/{pageNo}/{pageSize}',
        pageSize: 15
    });


    $caseList.on('click', '.j-all-btn', function() {
        $(this).find('input').prop('checked', true);
        $caseList.find('.j-local-btn input').prop('checked', false);

        $('.j-local-val').removeAttr('name');
        $('#city').attr('name', 'city');
        caseListPage.refresh();

        return false;
    });
    var store = require('store');

    $caseList.on('click', '.j-local-btn', function() {
        $(this).find('input').prop('checked', true);
        $('#caseList').find('.j-all-btn input').prop('checked', false);

        var cityId = store.getCache('cityId');
        $('.j-local-val').val(cityId).attr('name', 'city');
        $('#city').removeAttr('name');

        caseListPage.refresh();
        return false;
    })



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