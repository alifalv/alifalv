/*!
 * 首页
 * @author Li Hongwei
 * @date 2018-07-02 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');

    //个人主页域名跳转
    var name = location.hostname.substring(0, location.hostname.indexOf('.simplyto.com'));
    if (name && name.length > 0 && name != 'www' && name != 'WWW') {

        if(window.location.pathname.split("/")[1].indexOf('index.html') === -1) {//排除网站首页
            rest.post({
                url: 'user/getUserIdByPersonUrl',//147.免费文书制作申请;10.8
                data : {
                    url: name //二级域名名称
                },
                success: function (result) {
                    location.href = constants.viewUrl + "home_index.html?userId=" + result.data;
                },fail:function () {

                }
            });
        }
    }

    var Swiper = require('swiper');
    var Page = require('page');


    var litigationCost = require('common/litigationCost');

    rest.post({
        url: 'user/bannerSearch',//147.免费文书制作申请;10.8
        data : {
            type: 1 //(1：PC，2：APP)
        },
        success: function (result) {
            C.template('#bannerListTmpl').renderReplace(result, function($html) {

                $html.find('img').one('error',function () {
                    $(this).attr('src', '../img/img-error.jpg');
                });

                //首页滚动banner
                var swiper = new Swiper('.swiper-container', {
                    pagination: '.swiper-pagination',
                    paginationClickable: true,
                    autoplay : 3000
                });
            });
        },
        fail: function (result) {
            C.alert.fail(result.msg);
        }
    });



    //诉讼费用计算
    $('#cost_btn').on('click', function() {
        $('#cost_result').val(litigationCost($('#cost_amount').val(), $('#litigation_type').val()));
        return false;
    });

    //右侧今日推荐
    new Page($('#toDayRecommendList'), {
        hasPage : false,
        url : 'article/listArticle/{model}/{pageNo}/{pageSize}',
        data : {
            articleState:1//必需的值；
        },
        urlParams : {
            model: 1
        },
        pageSize : 8
    });

    //右侧最新法规
    new Page($('#newLawList'), {
        hasPage : false,
        url : 'article/legalList/{pageNo}/{pageSize}',
        pageSize : 8
    });

    //右侧常用文书(文书制作模板列表)
    new Page($('#oftenDocList'), {
        hasPage : false,
        url : 'article/bookMakeModelList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 8
    });

    //右侧法律疑难问题与观点
    new Page($('#legalProblemsList'), {
        hasPage : false,
        url : 'article/questionAndViewpointList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 8
    });

    //右侧常见咨询问题
    new Page($('#lawList'), {
        hasPage : false,
        url : 'article/adviceQuestionList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 8
    });

    //左侧在线咨询(咨询列表)
    new Page($('#onlineConsultList'), {
        hasPage : false,
        url : 'advice/adviceList/{pageNo}/{pageSize}',
        data : {
            queryType : 1
        },
        pageSize : 6
    });

    //案件委托
    new Page($('#caseEntrustList'), {
        hasPage : false,
        url : 'case/caseList/{pageNo}/{pageSize}',
        data : {
            city: '',
            caseType : ''
        },
        pageSize : 6
    });

    //左侧百姓吐槽
    new Page($('#commonPeopleList'), {
        hasPage : false,
        url : 'article/commonPeopleList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 6
    });


    //阿里裁判列表
    // new Page($('#aliAdjudicationList'), {
    //     hasPage : false,
    //     url : 'article/aliAdjudicationList/{model}/{pageNo}/{pageSize}',
    //     urlParams : {
    //         model: 1
    //     },
    //     pageSize : 4,
    //     htmlLoad : function ($html) {
    //         $html.find('img').one('error',function () {
    //             $(this).attr('src', '../img/img-error.jpg');
    //         });
    //     }
    // });
    function aliAdjudicationList(declareType) {
        rest.post({
            url: 'article/aliAdjudicationList/{model}/{pageNo}/{pageSize}',
            urlParams : {
                model: 1,
                pageNo : 1,
                pageSize : 1
            },
            data : {
                declareType : declareType//declareType 申报类型id 1 要案判决 3 指导性判决 4 有意见的判决 5 枉法裁判
            },
            success: function (result) {
                C.template('#aliAdjudication' + declareType + 'Tmpl').renderReplace(result, function($html) {
                    $html.find('img').one('error',function () {
                        $(this).attr('src', '../img/img-error.jpg');
                    });
                });
            }
        });
    }

    //declareType 申报类型id 1 要案判决 5 枉法裁判 3 指导性判决4 有意见的判决
    aliAdjudicationList(1);
    aliAdjudicationList(5);
    aliAdjudicationList(3);
    aliAdjudicationList(4);




    var dataList = require('dataList');

    //左侧找律师 专业领域
    dataList.getCaseList(function(result) {
        C.template('#caseListTmpl').renderReplace(result, function() {});
    });

    //左侧找律师 职业
    dataList.getJobList(function(result) {
        C.template('#jobListTmpl').renderReplace(result, function() {});
    });

    dataList.getProvinceList(function(result) {
        C.template('#provinceTmpl').renderReplace(result, function() {
            $('#province_select').trigger('change');
        });
    });


    $('#province_select').on('change', function() {
        rest.post({
            url: 'address/cityListByProvince/{pid}',
            urlParams : {
                pid : $('#province_select').val()
            },
            success: function (result) {
                C.template('#cityTmpl').renderReplace(result, function() {});
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

    //左侧找律师
    $('#search_lawyer_btn').on('click', function () {
        var _$this = $(this);
        var _$form = _$this.parents('form');
        location.href = constants.viewUrl + 'findlawyer.html?' + _$form.serialize();
    });

    //律师之窗
    rest.post({
        url: 'user/adminRecommendCounselorList',//132.咨询师推荐列表
        success: function (result) {
            var showIndex = 6;

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
});
