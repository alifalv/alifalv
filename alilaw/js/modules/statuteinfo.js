/*!
 * 法规详情
 * @author Li Hongwei
 * @date 2018-07-22 17:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";

    var utils = require('utils');

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
    var urlParams = utils.getUrlParams();
    var articleId = urlParams.articleId;
    if (null == articleId || '' == articleId) {
        utils.jump404('文章编号为空');
        return false;
    }

    require('WdatePicker');
    var dataList = require('dataList');

    //发文单位
    dataList.getUnitList(function(result) {
        C.template('#unitListTmpl').renderReplace(result, function() {});
    });

    //效力等级
    dataList.getEffectivenessGradeList(function(result) {
        C.template('#effectivenessGradeListTmpl').renderReplace(result, function() {});
    });

    var Page = require('page');
    var loginUserId = -1;
    var loginUserInfo = utils.getLoginUserInfo();
    if (loginUserInfo) {
        loginUserId = loginUserInfo.userId;
    }

    function initShareBox() {
        var shareHtml = '<div class="bdsharebuttonbox ui-inline-block"><a href="javascript:void(0);" class="bds_more" data-cmd="more"></a><a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a><a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a><a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a><a href="#" class="bds_renren" data-cmd="renren" title="分享到人人网"></a><a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a></div>\n' +
            '<script>window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"0","bdSize":"24"},"share":{},"image":{"viewList":["qzone","tsina","tqq","renren","weixin"],"viewText":"分享到：","viewSize":"16"},"selectShare":{"bdContainerClass":null,"bdSelectMiniList":["qzone","tsina","tqq","renren","weixin"]}};with(document)0[(getElementsByTagName(\'head\')[0]||body).appendChild(createElement(\'script\')).src=\'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=\'+~(-new Date()/36e5)];</script>'
        $('.j-share-box').append(shareHtml);
    }


    rest.post({
        url: 'article/articleDetails/{userId}/{articleId}',
        urlParams: {
            userId: loginUserId,
            articleId: articleId
        },
        data: {
            columnCode: 8
        },
        success: function(result) {
            C.template('#activeTmpl').renderReplace(result, function($html) {
                initShareBox();

            });
        }
    });


    $(document).on('click', '#star_btn', function() {
        var _$this = $(this);
        var loginUserInfo = utils.getLoginUserInfo(true);
        rest.post({
            url: 'article/saveArticleCollection/{userId}/{articleId}',
            urlParams: {
                userId: loginUserInfo.userId,
                articleId: articleId
            },
            data: {
                nickName: loginUserInfo.nickName
            },
            success: function(result) {
                if (result.data.ok) {
                    _$this.addClass('active');
                    C.msg.tips(result.msg);
                } else {
                    _$this.removeClass('active');
                    C.msg.warn(result.msg);
                }
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
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

    //常用文书模板
    new Page($('#bookMakeModelList'), {
        url: 'article/bookMakeModelList/{model}/{pageNo}/{pageSize}',
        urlParams: {
            model: 1
        },
        pageSize: 10
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
});