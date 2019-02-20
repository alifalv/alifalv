/*!
 * 法律法规查询
 * @author Li Hongwei
 * @date 2018-08-04 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var Page = require('page');
    require('WdatePicker');
    var utils = require('utils');

    var urlParams = utils.getUrlParams();




    /**
     * 初始化查询条件
     * @param title 标题
     * @param articleContent 文章内容
     * @param effectLevel 效力等级
     * @param sendUnit 发文单位
     * @param startTime 发布日期启
     * @param endTime 发布日期止
     */
    function initSearchForm(title, articleContent, effectLevel, sendUnit, startTime, endTime) {
        var dataList = require('dataList');

        //发文单位
        dataList.getUnitList(function(result) {
            if(sendUnit) {
                result.selectedId = sendUnit;
            }
            C.template('#unitListTmpl').renderReplace(result, function() {});
        });

        //效力等级
        dataList.getEffectivenessGradeList(function(result) {
            if(effectLevel) {
                result.selectedId = effectLevel;
            }
            C.template('#effectivenessGradeListTmpl').renderReplace(result, function() {});
        });

        if(title) {
            $('#title').val(title);
        }

        if(articleContent) {
            $('#articleContent').val(articleContent);
        }

        if(startTime) {
            $('#startTime').val(startTime);
        }

        if(endTime) {
            $('#endTime').val(endTime);
        }

    }

    //最新上传
    new Page($('#legalList'), {
        isInitRefresh : false,
        url : 'article/legalList/{pageNo}/{pageSize}',
        urlParams : {
            model: 1
        },
        pageSize : 10,
        dataFilter : function(result){
            result.searchKey = $('#title').val()
        },
        init : function () {
            if(JSON.stringify(urlParams) != '{}') {
                //带查询条件跳转到此页面的
                initSearchForm(urlParams.title, urlParams.articleContent, urlParams.effectLevel, urlParams.sendUnit, urlParams.startTime, urlParams.endTime);
                setTimeout(function() {
                    $('#search_btn').trigger('click');
                }, 300);
            } else {
                initSearchForm();
                setTimeout(function() {
                    $('#search_btn').trigger('click');
                }, 300);
            }
        }
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


    //右侧法律疑难问题与观点
    new Page($('#legalProblemsList'), {
        hasPage : false,
        url : 'article/questionAndViewpointList/{model}/{pageNo}/{pageSize}',
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




});
