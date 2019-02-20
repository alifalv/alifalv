/*!
 * 法律文书制作
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var Page = require('page');
    var constants = require('constants');

    var dataList = require('dataList');
    dataList.getCaseList(function(result) {
        C.template('#caseListTmpl').renderReplace(result, function() {});
    });

    dataList.getJobList(function(result) {
        C.template('#jobListTmpl').renderReplace(result, function() {});
    });

    $('#search_btn').on('click', function () {
        var _$this = $(this);
        var _$form = _$this.parents('form');
        location.href = constants.viewUrl + 'findlawyer.html?' + _$form.serialize();
    });

    //文书制作模板列表
    new Page($('#bookMakeModelList'), {
        url : 'article/bookMakeModelList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 5
    });



    //文书制作知识列表
    new Page($('#bookMakeKnowledgeList'), {
        url : 'article/bookMakeKnowledgeList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 5
    });

    //长沙律师推荐
    rest.post({
        url: 'user/adminRecommendCounselorList',//132.咨询师推荐列表
        success: function (result) {
            var showIndex = 8;

            if(result.data && result.data.length < showIndex) {
                for(var i = result.data.length; i < showIndex; i++) {
                    result.data.push({userName : '诚邀律师', provinceName : '欢 迎', cityName : '联 系', mobile : '0731-85580003'});
                }
            }
            result.data = $.extend([], result.data.slice(0, showIndex));
            C.template('#counselorRecommendListTmpl').renderReplace(result, function() {
                $('#counselorRecommendList img').error(function () {
                    $(this).attr('src', '../img/head-defalut.png');
                })
            });
        },
        fail: function (result) {
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
        url: 'user/counselorRecommendList', //【案源详情的竞拍】-咨询师发起竞拍
        data : {
            city :cityId
        },
        success: function (result) {
            var showIndex = 6;
            result.data = $.extend([], result.data.slice(0, showIndex));
            C.template('#cityCounselorRecommendListTmpl').renderReplace(result,function ($html) {
                $html.find('img').one('error', function () {
                    $(this).attr('src', '../img/img-error.jpg');
                });
            });
        }
    });
});
