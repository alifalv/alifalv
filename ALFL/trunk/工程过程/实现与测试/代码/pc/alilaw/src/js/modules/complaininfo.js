/*!
 * 百姓吐槽详情
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function (require, exports, module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();

    //不存在文章编号
    var articleId = urlParams.articleId;
    if(null == articleId || '' == articleId) {
        utils.jump404('文章编号为空');
        return false;
    }

    var Page = require('page');
    require('validator');
    var constants = require('constants');
    var userInfo = utils.getLoginUserInfo();


    $('#star_btn').on('click', function() {
        var _$this = $(this);
        var loginUserInfo = utils.getLoginUserInfo(true);
        rest.post({
            url: 'article/saveArticleCollection/{userId}/{articleId}',
            urlParams : {
                userId : loginUserInfo.userId,
                articleId : articleId
            },
            data : {
                nickName : loginUserInfo.nickName
            },
            success: function(result) {
                if(result.data.ok) {
                    _$this.addClass('active');
                    C.msg.tips(result.msg);
                } else {
                    _$this.removeClass('active');
                    C.msg.warn(result.msg);
                }
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });


    var authorUserName;//发布文章的用户名
    var authorUserImg;//发布文章的用户头像
    var authorUserId;//发布文章的用户id
    var serviceName;//标题
    var loginUserImg = '../img/defalut-head.png';//登入用户的头像
    if(null != userInfo) {
        loginUserImg = userInfo.userImg;
    }


    function initShareBox() {
        var shareHtml = '<div class="bdsharebuttonbox ui-inline-block"><a href="javascript:void(0);" class="bds_more" data-cmd="more"></a><a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a><a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a><a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a><a href="#" class="bds_renren" data-cmd="renren" title="分享到人人网"></a><a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a></div>\n' +
            '<script>window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"0","bdSize":"24"},"share":{},"image":{"viewList":["qzone","tsina","tqq","renren","weixin"],"viewText":"分享到：","viewSize":"16"},"selectShare":{"bdContainerClass":null,"bdSelectMiniList":["qzone","tsina","tqq","renren","weixin"]}};with(document)0[(getElementsByTagName(\'head\')[0]||body).appendChild(createElement(\'script\')).src=\'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=\'+~(-new Date()/36e5)];</script>'
        $('.j-share-box').append(shareHtml);
    }
    var loginUserId = -1;

    function initContent() {
        var loginUserInfo = utils.getLoginUserInfo();
        if(loginUserInfo) {
            loginUserId = loginUserInfo.userId;
        }

        rest.post({
            url: 'article/articleDetails/{userId}/{articleId}',
            urlParams : {
                userId : loginUserId,
                articleId : articleId
            },
            data : {
                columnCode : 0
            },
            success: function (result) {
                serviceName = result.data.title;
                authorUserId = result.data.userId;
                authorUserName = result.data.realName;
                authorUserImg = result.data.userImg;
                C.template('#activeTmpl').renderReplace(result, function($html) {
                    initShareBox();
                    initReply(authorUserId);

                    require('viewer');
                    var viewer = new Viewer(document.getElementById('img_viewer'));

                    //点赞
                    $html.on('click', '.j-like-btn', function () {
                        var _loginUserInfo = utils.getLoginUserInfo(true);

                        var _$this = $(this);
                        rest.post({
                            url: 'article/saveArticleLike/{userId}/{articleId}',
                            urlParams : {
                                userId : _loginUserInfo.userId,
                                articleId:articleId
                            },
                            success: function(result) {
                                C.msg.ok(result.msg);
                                _$this.find('span').html(parseInt(_$this.find('span').html()) + parseInt(result.data.likeNum));
                            },
                            fail : function (result) {
                                C.msg.fail(result.msg);
                            }
                        });

                    });

                    //打赏
                    $html.on('click', '.j-reward-btn', function () {
                        var _loginUserInfo = utils.getLoginUserInfo(true);

                        var _$this = $(this);
                        rest.post({
                            url: 'order/gotoReward',
                            async : false,
                            data : {
                                title : serviceName,//文章标题
                                userId : _loginUserInfo.userId,//发起人的userID
                                favoreeUserId:authorUserId,//被打赏用户的id
                                connectionId:articleId,//文章id
                                businessType :'百姓吐槽',//业务类型 【传入中文】{"百姓吐槽","法律疑难问题与观点","公务员招聘","常用文书模板","文书制作知识","法律人信息","常见咨询问题","法律培训","法律法规","阿里裁决","我的风采","工作动态","成功案例","我的随笔","法律咨询","案件委托"};
                                currentId: articleId//针对打赏数据的Id（【法律咨询】 咨询解答这一行的Id【阿里裁决】 阿里裁决的Id；【百姓吐槽】 百姓吐槽的Id）
                            },
                            success: function(result) {
                                var index = C.alert.tips('正在支付中', function () {});
                                utils.jumpPay(result.data.businessOrder, function () {
                                    C.msg.ok('打赏成功');
                                    layer.close(index);
                                });
                            },
                            fail : function (result) {
                                C.msg.fail(result.msg);
                            }
                        });

                    });
                });
            }
        });

    }


    //文章收藏
    $(document).on('click', '#star_btn', function() {
        var _$this = $(this);
        var loginUserInfo = utils.getLoginUserInfo(true);
        rest.post({
            url: 'article/saveArticleCollection/{userId}/{articleId}',
            urlParams : {
                userId : loginUserInfo.userId,
                articleId : articleId
            },
            data : {
                nickName : loginUserInfo.nickName
            },
            success: function(result) {
                if(result.data.ok) {
                    _$this.addClass('active');
                    C.msg.tips(result.msg);
                } else {
                    _$this.removeClass('active');
                    C.msg.warn(result.msg);
                }
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
        return false;
    });


    /**
     * 百姓吐槽发布留言
     * 【百姓吐槽详情、阿里裁判详情 】咨询师对发布人的回帖
     * 【百姓吐槽详情、阿里裁判详情、案源详情、法律咨询详情】发布人对咨询师的回贴
     *
     * @param $form
     */
    function commonReply($form,sendUserId, func) {
        rest.post({
            form: $form,
            url: 'reply/commonReply',//
            data: {
                //表单中取 replyContent回复内容
                //表单中取 upId针对回复的类型，【0：如果是第一层的回复;1:针对回复的id】百姓吐槽 2：阿里裁决取replyId
                userId : userInfo.userId,//登陆用户id
                caseId : articleId,//文章的Id
                businessType : 1,//业务类型【1：百姓吐槽 2：阿里裁决 3：法律咨询 4：案件竞拍 5：文书制作】
                serviceName: serviceName,//当前回复的文章标题
                sendUserId : sendUserId,//针对人
                sourceArticleId :articleId//文章编号
            },
            success: function (result) {
                func&&func.call(null, result);
            }
        });
    }

    var $commonReplyList = $('#commonReplyList');


    function initReply(authorUserId) {
       //百姓吐槽回帖列表
        var commonReplyListPage = new Page($commonReplyList, {
            url: 'reply/commonReplyList/{relativeId}/{businessType}/{pageNo}/{pageSize}', //【百姓吐槽详情、阿里裁判详情】的回帖列表
            urlParams: {
                relativeId: articleId,//文章编号
                businessType: 1//业务类型【1：百姓吐槽 2：阿里裁决 3：法律咨询 4：案件竞拍 5：文书制作】
            },
            pageSize: 8,
            addData: {
                author : {
                    userImg : authorUserImg,//发布文章的用户头像
                    userName : authorUserName//发布文章的用户名
                }
            },
            htmlLoad: function (data) {

                //头像加载失败的用默认头像
                $commonReplyList.find('img').one('error',function () {
                    $(this).attr('src', '../img/defalut-head.png');
                });

                //是用户自己的文章且登入才显示一级菜单的回复按钮
                if(null != userInfo && userInfo.userId == authorUserId) {
                    $('.js-replybox-btn').removeClass('ui-hide');
                }
            }
        });

        //一级留言框
        var $firstReplybox = $('.js-first-replybox');
        var firstReplyBoxData = {
            upId : 0,
            userImg: loginUserImg,//当前登录用户头像
            isLogin : loginUserId != -1
        };
        C.template('#replyBoxTmpl').load(firstReplyBoxData, $firstReplybox, function () {
            $firstReplybox.find('img').one('error',function () {
                $(this).attr('src', '../img/defalut-head.png');
            });
            $firstReplybox.find('form').attr('id', 'first_reply_form');
            var login_form_valid = $('#first_reply_form').validator(constants.validator);
            login_form_valid.on('valid.form', function (e, form) {
                var loginUserInfo = utils.getLoginUserInfo(true);
                commonReply($(form), authorUserId, function(result) {
                    C.msg.ok(result.msg || '回复成功');
                    $('#first_reply_form').find('[name="replyContent"]').val('');
                    commonReplyListPage.refresh();
                });
            });
        });


        //评论按钮
        $commonReplyList.on('click', '.js-replybox-btn', function () {
            var _$this = $(this);

            var _info = _$this.data('info');

            //清空留言box
            $commonReplyList.find('.js-replybox').html('');
            var _$replyBox = _$this.parent().next('.js-replybox');
            var _data = {
                upId : _info.replyId,
                userImg : loginUserImg//登录用户的头像
            };
            C.template('#replyBoxTmpl').load(_data, _$replyBox, function () {
                _$replyBox.find('img').one('error',function () {
                    $(this).attr('src', '../img/defalut-head.png');
                });
                var login_form_valid = $('#reply_form').validator(constants.validator);
                login_form_valid.on('valid.form', function (e, form) {
                    commonReply($(form),_info.userId, function (result) {
                        C.msg.ok(result.msg || '回复成功');
                        $commonReplyList.find('.js-replybox').html('');
                        commonReplyListPage.refresh();
                        //
                        // C.template('#replycontentTmpl').loadBefore(result, _$replyBox.next('.js-replycontent'), function () {
                        //
                        // });
                    });
                });
            });
        });



    }

    initContent();

});
