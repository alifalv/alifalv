/*!
 * 法律法规查询
 * @author Li Hongwei
 * @date 2018-08-04 11:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";

    var Page = require('page');
    require('WdatePicker');
    var utils = require('utils');

    var urlParams = utils.getUrlParams();
    /**
     * 效率等级和法文单位的关联
     */
    $("#effectLevel").on("change", function() {
        var mySelect = $("#sendUnit option");
        mySelect.each(function(i, el) {
            $(this).show();
            if ($(this).parent().is('span')) {
                $(this).unwrap();
            }
        })

        //法律
        if ($("option:selected", this).val() == '1') {
            var mySelect = $("#sendUnit option");
            var num = "1"; //某个值
            mySelect.each(function(i, el) {
                $(this).show();
                if ($(this).parent().is('span')) {
                    $(this).unwrap();
                }
                if ($(el).val() != num) {
                    $(this).hide();
                    if (!$(this).parent().is('span')) {
                        $(this).wrap("<span style='display:none'></span>");
                    }
                }
            })
        }
        //司法解释
        if ($("option:selected", this).val() == '2') {
            var mySelect = $("#sendUnit option");
            var num = "6"; //某个值
            var num1 = "20"; //某个值
            mySelect.each(function(i, el) {
                $(this).hide();
                if (!$(this).parent().is('span')) {
                    $(this).wrap("<span style='display:none'></span>");
                }
                if ($(el).val() == num || $(el).val() == num1) {
                    $(this).show();
                    if ($(this).parent().is('span')) {
                        $(this).unwrap();
                    }
                }
            })
        }
        //行政法规
        if ($("option:selected", this).val() == '3') {
            var mySelect = $("#sendUnit option");
            var num = "2"; //某个值
            mySelect.each(function(i, el) {
                $(this).hide();
                if (!$(this).parent().is('span')) {
                    $(this).wrap("<span style='display:none'></span>");
                }
                if ($(el).val() == num) {
                    $(this).show();
                    if ($(this).parent().is('span')) {
                        $(this).unwrap();
                    }
                }
            })
        }
        //地方性法规
        if ($("option:selected", this).val() == '5') {
            $("#sendSelect").hide();
            $("#provinceSelect").show();
            $("#sendUnit2").attr("name", "sendUnit");
            $("#sendUnit").attr("name", "sendUnit1");
            document.getElementById('sendLabel').innerHTML = "所属地区：";
        } else {
            $("#sendUnit").attr("name", "sendUnit");
            $("#sendUnit2").attr("name", "sendUnit2");
            $("#sendSelect").show();
            $("#provinceSelect").hide();
            document.getElementById('sendLabel').innerHTML = "发文单位：";
        }
        // 重置下拉框选择为全部
        if ($("option:selected", this).val() == '1') {
            var job = 1;
            document.getElementsByName("sendUnit")[0].value = job;
        } else if ($("option:selected", this).val() == '2') {
            var job = 6;
            document.getElementsByName("sendUnit")[0].value = job;
        } else if ($("option:selected", this).val() == '3') {
            var job = 2;
            document.getElementsByName("sendUnit")[0].value = job;
        } else if ($("option:selected", this).val() == '') {
            var job = '';
            document.getElementsByName("sendUnit")[0].value = job;
        }

    });

    /**
     * 初始化查询条件
     * @param title 标题
     * @param articleContent 文章内容
     * @param effectLevel 效力等级
     * @param sendUnit 发文单位
     * @param startTime 发布日期启
     * @param endTime 发布日期止
     */
    function initSearchForm(type, title, articleContent, effectLevel, sendUnit, startTime, endTime) {
        var dataList = require('dataList');
        //发文单位
        dataList.getUnitList(function(result) {
            if (sendUnit) {
                result.selectedId = sendUnit;
            }
            C.template('#unitListTmpl').renderReplace(result, function() {});
        });

        //效力等级
        dataList.getEffectivenessGradeList(function(result) {
            if (effectLevel) {
                result.selectedId = effectLevel;
            }
            C.template('#effectivenessGradeListTmpl').renderReplace(result, function() {});
        });
        if (title) {
            $('#title').val(title);
        }
        if (type == 1) {
            $('#type').val(type);
        }

        if (articleContent) {
            $('#articleContent').val(articleContent);
        }

        if (startTime) {
            $('#startTime').val(startTime);
        }

        if (endTime) {
            $('#endTime').val(endTime);
        }

    }

    //最新上传
    new Page($('#legalList'), {
        isInitRefresh: false,
        url: 'article/legalList/{pageNo}/{pageSize}',
        urlParams: {
            model: 1
        },
        pageSize: 13,

        dataFilter: function(result) {
            result.searchKey = $('#title').val();
            // $('#type').val('2');
        },
        init: function() {
            if (JSON.stringify(urlParams) != '{}') {
                //带查询条件跳转到此页面的
                initSearchForm(2, urlParams.title, urlParams.articleContent, urlParams.effectLevel, urlParams.sendUnit, urlParams.startTime, urlParams.endTime);
                setTimeout(function() {
                    $('#search_btn').trigger('click');
                }, 300);
            } else {
                initSearchForm(1);
                setTimeout(function() {
                    $('#search_btn').trigger('click');
                }, 300);
            }
        }
    });


    //右侧今日推荐
    new Page($('#toDayRecommendList'), {
        hasPage: false,
        url: 'article/listArticle/{model}/{pageNo}/{pageSize}',
        data: {
            articleState: 1 //必需的值；
        },
        urlParams: {
            model: 1
        },
        pageSize: 8
    });


    //右侧法律疑难问题与观点
    new Page($('#legalProblemsList'), {
        hasPage: false,
        url: 'article/questionAndViewpointList/{model}/{pageNo}/{pageSize}',
        urlParams: {
            model: 1
        },
        pageSize: 8
    });

    //右侧常用文书
    new Page($('#oftenDocList'), {
        hasPage: false,
        url: 'article/bookMakeModelList/{model}/{pageNo}/{pageSize}',
        urlParams: {
            model: 1
        },
        pageSize: 8
    });

    // /**
    //  * 地区 （省）
    //  */
    // var dataList = require('dataList');
    // dataList.getProvinceList(function(result) {
    //     C.template('#provinceTmpl').renderReplace(result, function() {
    //         $('#province_select').trigger('change');
    //     });
    // });

});