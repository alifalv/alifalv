/*!
 * 找律师
 * @author Li Hongwei
 * @date 2018-08-06 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var Page = require('page');

    var utils = require('utils');
    var urlParams = utils.getUrlParams();

    if(JSON.stringify(urlParams) != '{}') {
        //带查询条件跳转到此页面的
        initSearchForm(urlParams.speciality, urlParams.job, urlParams.conditionValue, urlParams.province, urlParams.city);
        setTimeout(function() {
            $('#search_btn').trigger('click');
        }, 500);
    } else {
        initSearchForm();
    }


    /**
     * 初始化查询条件
     * @param caseId 擅长邻域编号
     * @param jobId 职业编号
     * @param conditionValue 模糊查询
     * @param provinceSelectedId 省编号
     * @param citySelectedId 市编号
     */
    function initSearchForm(caseId, jobId, conditionValue, provinceSelectedId, citySelectedId) {
        var dataList = require('dataList');

        //邻域下拉菜单
        dataList.getCaseList(function(result) {
            if(caseId) {
                result.selectedId = caseId;
            }
            C.template('#caseListTmpl').renderReplace(result, function() {});
        });

        //职业下拉菜单
        dataList.getJobList(function(result) {
            if(jobId) {
                result.selectedId = jobId;
            }
            C.template('#jobListTmpl').renderReplace(result, function() {});
        });

        dataList.getProvinceList(function(result) {
            if(provinceSelectedId) {
                result.selectedId = provinceSelectedId;
            }
            C.template('#province_tmpl').renderReplace(result, function() {
                $('#province_select').trigger('change');
            });
        });

        $('#province_select').on('change', function() {
            var province_select_val = $('#province_select').val();
            if(province_select_val == '') {
                C.template('#city_tmpl').renderReplace({}, function() {});
                return false;
            }
            rest.post({
                url: 'address/cityListByProvince/{pid}',
                urlParams : {
                    pid : province_select_val
                },
                success: function (result) {
                    if(citySelectedId) {
                        result.selectedId = citySelectedId;
                    }
                    C.template('#city_tmpl').renderReplace(result, function() {});
                },
                fail: function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });


        if(conditionValue) {
            $('#conditionValue').val(conditionValue);
        }

    }


    //左侧 咨询师列表
    new Page($('#counselorList'), {
        url : 'user/counselorList/{pageNo}/{pageSize}',
        pageSize : 8,
        htmlLoad : function($html){
            $html.find('img').one('error',function () {
                $(this).attr('src', '../img/img-error.jpg');
            });
        }
    });

    //右侧今日推荐
    new Page($('#toDayRecommendList'), {
        hasPage : false,
        url : 'article/adviceQuestionList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 8
    });

    //右侧常用文书
    new Page($('#oftenDocList'), {
        hasPage : false,
        url : 'article/bookMakeModelList/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 8
    });

    //顶部律师推荐
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
});
