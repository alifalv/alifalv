/*!
 * 人才招聘
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var Page = require('page');

    var urlParams = utils.getUrlParams();



    /**
     * 初始化查询条件
     * @param caseId 擅长邻域编号
     * @param jobId 职业编号
     * @param salId 模糊查询
     * @param provinceSelectedId 省编号
     * @param citySelectedId 市编号
     * @param type 市编号
     * @param job 市编号
     */
    function initSearchForm(educationId, experienceId, salId, provinceSelectedId, citySelectedId, type, job) {
        var dataList = require('dataList');

        //邻域下拉菜单
        dataList.getEducationList(function(result) {
            if(educationId) {
                result.selectedId = educationId;
            }
            C.template('#educationListTmpl').renderReplace(result, function() {});
        });

        dataList.getSalList(function(result) {
            if(salId) {
                result.selectedId = salId;
            }
            C.template('#salListTmpl').renderReplace(result, function() {});
        });

        //职业下拉菜单
        dataList.getExperienceList(function(result) {
            if(experienceId) {
                result.selectedId = experienceId;
            }
            C.template('#experienceListTmpl').renderReplace(result, function() {});
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
            return false;
        });

        $('#type').val(type);
        $('#job').val(job);
    }

    //咨询师列表
    new Page($('#userResumeSearchList'), {
        isInitRefresh : false,
        url : 'user/userResumeSearch/{pageNo}/{pageSize}',
        pageSize : 10,
        init : function () {
            if(JSON.stringify(urlParams) != '{}') {
                //带查询条件跳转到此页面的
                initSearchForm(urlParams.qualification, urlParams.workExperience, urlParams.sal, urlParams.province, urlParams.city, urlParams.type, urlParams.job);
                setTimeout(function() {
                    $('#search_btn').trigger('click');
                }, 500);
            } else {
                initSearchForm();
                setTimeout(function() {
                    $('#search_btn').trigger('click');
                }, 500);
            }
        }
    });
});
