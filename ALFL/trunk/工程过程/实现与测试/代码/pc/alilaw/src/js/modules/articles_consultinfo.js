/*!
 * 咨询问题详情
 * @author Li Hongwei
 * @date 2018-07-01 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    //因涉及到优先前3名回复可获得赏金，咨询师不允许删除自己回复的贴

    //不存在文章编号
    var adviceId = urlParams.articleId;
    if(null == adviceId || '' == adviceId) {
        utils.jump404('文章编号为空');
        return false;
    }

    var authorUserName;//发布文章的用户名
    var authorUserImg;//发布文章的用户头像
    var authorUserId;//发布文章的用户id
    var serviceName;
    rest.post({
        url: 'advice/adviceDetails/{adviceId}',
        urlParams : {
            adviceId : adviceId
        },
        success: function (result) {
            authorUserName = result.data.userName;//发布文章的用户名
            authorUserImg = result.data.userImg;//发布文章的用户头像
            serviceName = result.data.title;
            authorUserId = result.data.userId;
            initReplyList();
            C.template('#activeInfoTmpl').renderReplace(result, function($html) {
                $('#activeInfo ').find('.ui-imglist img').one('error',function () {
                    $(this).attr('src', '../img/img-error.jpg');
                });

                require('viewer');
                var viewer = new Viewer(document.getElementById('img_viewer'));
            });

        }
    });

    var loginUserInfo = utils.getLoginUserInfo();
    var Page = require('page');
    require('validator');
    var constants = require('constants');

    var loginUserImg = '../img/defalut-head.png';//登入用户的头像
    if(null != loginUserInfo) {
        loginUserImg = loginUserInfo.userImg;
    }


    /**
     * 发布留言
     * 【百姓吐槽详情、阿里裁判详情、法律咨询】咨询师对发布人的回帖
     * 【百姓吐槽详情、阿里裁判详情、案源详情、法律咨询详情】发布人对咨询师的回贴
     *
     * @param $form
     */
    function commonReply($form,replyId, func) {
        var _loginUserInfo = utils.getLoginUserInfo(true);

        rest.post({
            form: $form,
            url: 'advice/sendReply/{replyId}',//
            urlParams : {
                replyId : replyId
            },
            data: {
                //表单中取 replyContent回复内容
                loginUserId : _loginUserInfo.userId//登陆用户id
            },
            success: function (result) {
                func&&func.call(null, result);
            }
        });
    }

    var $commonReplyList = $('#commonReplyList');



    function initReplyList() {

        var _loginUserInfo = utils.getLoginUserInfo();
        //对发布人和除咨询师屏蔽
        if(null == _loginUserInfo || (_loginUserInfo && _loginUserInfo.userType == '2' && _loginUserInfo.userId != authorUserId)) {

            //一级留言框
            var $firstReplybox = $('.js-first-replybox');
            var firstReplyBoxData = {
                userImg: loginUserImg,//当前登录用户头像
                isLogin : _loginUserInfo != null ,
                isNotAuthentication : _loginUserInfo != null && _loginUserInfo.isAuthentication != 2
            };
            C.template('#replyBoxTmpl').load(firstReplyBoxData, $firstReplybox, function ($html) {

                $firstReplybox.find('img').one('error',function () {
                    $(this).attr('src', '../img/defalut-head.png');
                });

                //非认证咨询师按钮不可操作
                if(_loginUserInfo && _loginUserInfo.userType == '2' && !utils.isAuthenticationUser()) {
                    $html.find('.js-replysubmit-btn').prop('disabled', true).attr('title','请先认证');
                }

                $firstReplybox.find('form').attr('id', 'first_reply_form');
                var login_form_valid = $('#first_reply_form').validator(constants.validator);
                login_form_valid.on('valid.form', function (e, form) {
                    var _loginUserInfo = utils.getLoginUserInfo(true);

                    commonReply($(form),adviceId, function(result) {
                        C.msg.ok(result.msg || '回复成功');
                        $('#first_reply_form').find('[name="replyContent"]').val('');
                        commonReplyListPage.refresh();
                    });
                });
            });
        }

        //评论按钮
        $commonReplyList.on('click', '.js-replybox-btn', function () {
            var _$this = $(this);
            var _loginUserInfo = utils.getLoginUserInfo(true);

            var replyId = _$this.data('replyid');
            var _userId = _$this.data('userid');

            //清空留言box
            $commonReplyList.find('.js-replybox').html('');
            var _$replyBox = _$this.closest('.media-body').find('.js-replybox');
            var _data = {
                upId : 0,
                userImg : loginUserImg,//登录用户的头像
                isLogin : true,
                isNotAuthentication : _loginUserInfo != null && _loginUserInfo.isAuthentication != 2
            };
            C.template('#replyBoxTmpl').load(_data, _$replyBox, function () {
                _$replyBox.find('img').one('error',function () {
                    $(this).attr('src', '../img/defalut-head.png');
                });
                var login_form_valid = $('#reply_form').validator(constants.validator);
                login_form_valid.on('valid.form', function (e, form) {
                    var _loginUserInfo = utils.getLoginUserInfo(true);

                        rest.post({
                            form: $(form),
                            url: 'reply/commonReply',//
                            data: {
                                //表单中取 replyContent回复内容
                                //表单中取 upId针对回复的类型，【0：如果是第一层的回复;1:针对回复的id】百姓吐槽 2：阿里裁决取replyId
                                userId : _loginUserInfo.userId,//登陆用户id
                                caseId : replyId,//回复上级编号
                                businessType : 3,//业务类型【1：百姓吐槽 2：阿里裁决 3：法律咨询 4：案件竞拍 5：文书制作】
                                serviceName: serviceName,//当前回复的文章标题
                                sendUserId : _userId,//针对人
                                sourceArticleId :adviceId//文章编号
                            },
                            success: function (result) {
                                C.msg.ok(result.msg || '回复成功');
                                $commonReplyList.find('.js-replybox').html('');
                                commonReplyListPage.refresh();

                                //
                                // result.pUserName = _info.userName;
                                // C.template('#replycontentTmpl').loadBefore(result, _$replyBox.next('.js-replycontent'), function () {
                                //
                                // });
                            }
                        });

                    //bug 200 【法律咨询】咨询问题解答后评论，调用接口错误
                    // commonReply($(form),_info.replyId, function (result) {
                    //     C.msg.ok(result.msg || '回复成功');
                    //     $commonReplyList.find('.js-replybox').html('');
                    //     result.pUserName = _info.userName;
                    //     C.template('#replycontentTmpl').loadBefore(result, _$replyBox.next('.js-replycontent'), function () {
                    //
                    //     });
                    // });
                });
            });
        });


        $commonReplyList.on('click', '.j-like-btn', function() {
            var _$this = $(this);
            var replyId = _$this.data('replyid');

            var _loginUserInfo = utils.getLoginUserInfo(true);
            if(null == _loginUserInfo) {
                C.msg.tips('请先登录');
                return false;
            }
            rest.post({
                url: 'advice/replyLike/{replyId}',
                urlParams : {
                    replyId : replyId
                },
                data : {
                    loginUserId : _loginUserInfo.userId
                },
                success: function(result) {
                    C.msg.ok(result.msg);
                    _$this.next('span').html(result.data.likeNum);
                    // _$this.next('span').html((new Date).getTime());
                },
                fail : function(result) {
                    C.msg.fail(result.msg);
                }
            });
        });



        //留言列表
        //自己登录了才能看列表
        if(loginUserInfo) {
            var commonReplyListPage = new Page($commonReplyList, {
                url: 'advice/adviceReplyList/{adviceId}/{loginUserId}/{pageNo}/{pageSize}',
                urlParams: {
                    adviceId: adviceId,//文章编号
                    loginUserId: loginUserInfo.userId
                },
                pageSize: 8,
                addData: {
                    author : {
                        userImg : authorUserImg,//发布文章的用户头像
                        userName : authorUserName//发布文章的用户名
                    }
                },
                htmlLoad: function ($html) {

                    //头像加载失败的用默认头像
                    $commonReplyList.find('img').one('error',function () {
                        $(this).attr('src', '../img/defalut-head.png');
                    });

                    //是用户自己的文章且登入才显示一级菜单的回复按钮
                    if(null != loginUserInfo && loginUserInfo.userId == authorUserId) {
                        $('.js-replybox-btn').removeClass('ui-hide');
                    }

                    //打赏
                    $html.on('click', '.j-reward-btn', function () {
                        var _loginUserInfo = utils.getLoginUserInfo(true);

                        var _$this = $(this);
                        var favoreeUserId = _$this.data('userid');
                        var replyid = _$this.data('replyid');

                        rest.post({
                            url: 'order/gotoReward',
                            async : false,
                            data : {
                                title : serviceName,//文章标题
                                userId : _loginUserInfo.userId,//发起人的userID
                                favoreeUserId: favoreeUserId,//被打赏用户的id
                                connectionId: adviceId,//文章id
                                businessType : '法律咨询',//业务类型 【传入中文】{"百姓吐槽","法律疑难问题与观点","公务员招聘","常用文书模板","文书制作知识","法律人信息","常见咨询问题","法律培训","法律法规","阿里裁决","我的风采","工作动态","成功案例","我的随笔","法律咨询","案件委托"};
                                currentId: replyid//针对打赏数据的Id（【法律咨询】 咨询解答这一行的Id【阿里裁决】 阿里裁决的Id；【百姓吐槽】 百姓吐槽的Id）
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

                    var isLikeClicking = false;
                    $html.on('click', '.j-like-btn', function() {
                        var _loginUserInfo = utils.getLoginUserInfo(true);
                        if(isLikeClicking) {
                            return false;
                        }
                        isLikeClicking = true;
                        var _$this = $(this);
                        var replyId = _$this.data('replyid');

                        if(null == _loginUserInfo) {
                            C.msg.tips('请先登录');
                            return false;
                        }
                        rest.post({
                            url: 'advice/replyLike/{replyId}',//30.	咨询解答点赞；9.11修改
                            urlParams : {
                                replyId : replyId
                            },
                            data : {
                                loginUserId : _loginUserInfo.userId
                            },
                            success: function(result) {
                                C.msg.ok(result.msg);
                                _$this.next('span').html(result.data.likeNum);
                                // _$this.next('span').html((new Date).getTime());
                            },
                            fail : function(result) {
                                C.msg.fail(result.msg);
                            },
                            complete : function() {
                                isLikeClicking = false;
                            }
                        });
                        return false;
                    });
                }
            });
        }

    }


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
