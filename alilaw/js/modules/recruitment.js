/*!
 * 人才招聘
 * @author Li Hongwei
 * @date 2018-08-08 21:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');

    var Page = require('page');

    //最新招聘列表
    new Page($('#newJobList'), {
        url : 'user/newJobList/{pageNo}/{pageSize}',
        data: {
            state : 1
        },
        pageSize : 30
    });


    //公务员招聘列表
    new Page($('#civilWorkerRecruitList'), {
        url : 'article/civilWorkerRecruit/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 5
    });

    //右侧 个人求职
    new Page($('#recommendResume'), {
        url : 'user/recommendResume',
        urlParams : {
            model: 1
        },
        htmlLoad : function(){
            $('#recommendResume').find('.ui-person-list-head img').one('error',function () {
                $(this).attr('src', '../img/defalut-head.png');
            });
        },
        pageSize : 10
    });




    var dataList = require('dataList');

    //邻域下拉菜单
    dataList.getEducationList(function(result) {
        C.template('#educationListTmpl').renderReplace(result, function() {});
    });

    dataList.getSalList(function(result) {
        C.template('#salListTmpl').renderReplace(result, function() {});
    });

    //职业下拉菜单
    dataList.getExperienceList(function(result) {
        C.template('#experienceListTmpl').renderReplace(result, function() {});
    });

    dataList.getProvinceList(function(result) {
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
                C.template('#city_tmpl').renderReplace(result, function() {});
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
        return false;
    });


    dataList.getProvinceList(function(result) {
        C.template('#province2_tmpl').renderReplace(result, function() {
            $('#province2_select').trigger('change');
        });
    });

    $('#province2_select').on('change', function() {
        var province_select_val = $('#province2_select').val();
        if(province_select_val == '') {
            C.template('#city2_tmpl').renderReplace({}, function() {});
            return false;
        }
        rest.post({
            url: 'address/cityListByProvince/{pid}',
            urlParams : {
                pid : province_select_val
            },
            success: function (result) {
                C.template('#city2_tmpl').renderReplace(result, function() {});
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    });
    dataList.getSalList(function(result) {
        C.template('#sal2ListTmpl').renderReplace(result, function() {});
    });

    //职业下拉菜单
    dataList.getExperienceList(function(result) {
        C.template('#experience2ListTmpl').renderReplace(result, function() {});
    });
    dataList.getEducationList(function(result) {
        C.template('#education2ListTmpl').renderReplace(result, function() {});
    });

    //快速搜索（岗位搜索）
    $('#search_job_btn').on('click', function () {
        var _$this = $(this);
        var _$form = _$this.parents('form');
        location.href = constants.viewUrl + 'recruitment_job.html?' + _$form.serialize();
    });

    //快速搜索（人才搜索）
    $('#search_resume_btn').on('click', function () {
        var _$this = $(this);
        var _$form = _$this.parents('form');
        location.href = constants.viewUrl + 'recruitment_resume.html?' + _$form.serialize();
    });

    //我要招聘
    $('.j-gojob-btn').on('click', function() {
        var loginUserInfo = utils.getLoginUserInfo();
        if(loginUserInfo && loginUserInfo.userType == '1') {
            location.href = constants.viewUrl + 'personal_jobhunt_advocate.html';
            return false;
        }

        if(loginUserInfo && loginUserInfo.userType == '2') {
            location.href = constants.viewUrl + 'personal_jobhunt_counselor.html';
            return false;
        }
        utils.jumpLogin(true);
        return false;
    });

    //我要求职
    $('.j-goresume-btn').on('click', function() {
        var loginUserInfo = utils.getLoginUserInfo();
        if(loginUserInfo && loginUserInfo.userType == '1') {
            location.href = constants.viewUrl + 'personal_jobhunt_advocate.html';
            return false;
        }

        if(loginUserInfo && loginUserInfo.userType == '2') {
            location.href = constants.viewUrl + 'personal_jobhunt_counselor.html';
            return false;
        }
        utils.jumpLogin(true);
        return false;
    });
});
