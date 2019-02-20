/*!
 * 案源竞拍详情
 * @author Li Hongwei
 * @date 2018-07-14 15:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var caseId = urlParams.caseId;
    if(null == caseId || '' == caseId) {
        utils.jump404('文章编号为空');
        return false;
    }
    var constants = require('constants');
    var Page = require('page');
    require('validator');

    var authorUserName;//发布文章的用户名
    var authorUserImg;//发布文章的用户头像
    var authorUserId;//发布文章的用户id
    var serviceName;//标题
    var caseState;

    rest.post({
        url: 'case/caseDetails/{caseId}',
        urlParams : {
            caseId : caseId
        },
        success: function (result) {
            authorUserName = result.data.userName;
            authorUserImg = result.data.userImg;
            authorUserId = result.data.userId;
            serviceName = result.data.caseTitle;
            caseState = result.data.caseState;
            C.template('#activeTmpl').renderReplace(result, function($html) {
                require('viewer');
                var viewer = new Viewer(document.getElementById('img_viewer'));

                replyList();
            });
        }
    });


    function replyList() {
        var $commonReplyList = $('#commonReplyList');


        var loginUserInfo = utils.getLoginUserInfo();

        var loginUserImg = '../img/defalut-head.png';//登入用户的头像
        if(null != loginUserInfo) {
            loginUserImg = loginUserInfo.userImg;
        }


        //只限除自己以外的咨询师竞拍
        if(null == loginUserInfo || (loginUserInfo.userType == '2' && loginUserInfo.userId != authorUserId)) {

            //非竞拍状态时，对所有人屏蔽竞拍功能
            if ('0' == caseState) {

                var $firstReplybox = $('.js-first-replybox');
                var firstReplyBoxData = {
                    userImg: loginUserImg,//当前登录用户头像
                    isLogin : loginUserInfo != null,
                    isNotAuthentication : loginUserInfo != null && loginUserInfo.isAuthentication != '2'
                };
                C.template('#replyFirstBoxTmpl').load(firstReplyBoxData, $firstReplybox, function ($html) {

                    //非认证咨询师按钮不可操作
                    if(loginUserInfo && loginUserInfo.userType == '2' && !utils.isAuthenticationUser()) {
                        $html.find('.j-jp-btn').prop('disabled', true).attr('title','请先认证');
                    }


                    $firstReplybox.find('form').attr('id', 'first_reply_form');
                    $firstReplybox.find('img').one('error', function () {
                        $(this).attr('src', '../img/defalut-head.png');
                    });

                    var login_form_valid = $('#first_reply_form').validator(constants.validator);
                    login_form_valid.on('valid.form', function (e, form) {
                        var loginUserInfo = utils.getLoginUserInfo(true);
                        if (loginUserInfo.userType != '2') {
                            C.msg.tips('只有咨询师才可以竞拍');
                            return false;
                        }
                        rest.post({
                            form: $(form),
                            url: 'case/caseReply/{caseId}/{userId}', //【案源详情的竞拍】-咨询师发起竞拍
                            urlParams: {
                                caseId: caseId,
                                userId: loginUserInfo.userId
                            },
                            data: {
                                sendPerson: authorUserId//发帖人的id
                            },
                            success: function (result) {
                                C.msg.ok('竞拍已提交');
                                login_form_valid.validator('cleanUp');
                                commonReplyListPage.refresh();
                            }
                        });
                    });
                });
            }
        }



        //留言列表 需要登录
        if(loginUserInfo) {

        //聘请按钮
        $commonReplyList.on('click', '.j-pq-btn', function () {
            var _$this = $(this);

            C.confirm('点击"取消"，则代表"案件委托"，竞拍合作已达成，即关闭此案件',function () {
                var _offerId = _$this.data('offerid');
                rest.post({
                    url: 'case/employ/{offerId}',
                    async : false,
                    urlParams: {
                        offerId: _offerId//竞拍编号
                    },
                    success: function (result) {
                        if(result.data.code == '2') {//需要支付
                            var index = C.alert.tips(result.data.msg);
                            utils.jumpPay(result.data.businessOrder, function () {
                                layer.close(index);
                                C.msg.ok('继续聘请');
                                commonReplyListPage.refresh();
                            });
                        } else if(result.data.code == '1') {//聘请成功
                            C.msg.ok(result.data.msg);
                            commonReplyListPage.refresh();
                        }
                    }
                });
            },function () {
                //取消按钮事件调用，不要问为什么，需求是这样的
                rest.post({
                    url: 'case/casePrivateDeal/{caseId}',
                    urlParams: {
                        caseId: caseId//竞拍编号
                    },
                    success: function (result) {
                        C.msg.ok(result.msg);
                    }
                });
            }, '是否支付协议律师费？');
        });




        //后台已做处理
            var commonReplyListPage = new Page($commonReplyList, {
                url: 'case/offerList/{caseId}/{sendCaseUserId}/{loginUserId}/{pageNo}/{pageSize}',//【案源竞拍详情与回复】
                urlParams: {
                    caseId: caseId,//案源ID
                    sendCaseUserId: authorUserId,//发帖人的id
                    loginUserId :loginUserInfo.userId //当前登陆人的Id
                },
                pageSize: 8,
                addData: {
                    caseState : caseState,
                    author : {
                        userImg : authorUserImg,//发布文章的用户头像
                        userName : authorUserName//发布文章的用户名
                    }
                },
                htmlLoad: function ($html) {

                    if(null != loginUserInfo && loginUserInfo.userId == authorUserId) {
                        //登录并且是自己 显示评价聘请按钮
                        $html.find('.j-operbtn-box').removeClass('ui-hide');
                    }

                    //头像加载失败的用默认头像
                    $commonReplyList.find('img').one('error',function () {
                        $(this).attr('src', '../img/defalut-head.png');
                    });

                    //是用户自己的文章且登入才显示一级菜单的回复按钮
                    if(null != loginUserInfo && loginUserInfo.userId == authorUserId) {
                        $('.js-replybox-btn').removeClass('ui-hide');
                    }


                }
            });


            //评论按钮
            $commonReplyList.on('click', '.js-replybox-btn', function () {
                var loginUserInfo = utils.getLoginUserInfo();

                var loginUserImg = '../img/defalut-head.png';//登入用户的头像
                if(null != loginUserInfo) {
                    loginUserImg = loginUserInfo.userImg;
                }

                var _$this = $(this);

                var _info = _$this.data('info');

                //清空留言box
                $commonReplyList.find('.js-replybox').html('');
                var _$replyBox = _$this.parent().parent().next('.js-replybox');
                var _data = {
                    upId : 0,//案件竞拍回复传0}
                    userImg : loginUserImg//登录用户的头像
                };
                C.template('#replyBoxTmpl').load(_data, _$replyBox, function () {
                    _$replyBox.find('img').one('error',function () {
                        $(this).attr('src', '../img/defalut-head.png');
                    });
                    var login_form_valid = $('#reply_form').validator(constants.validator);
                    login_form_valid.on('valid.form', function (e, form) {
                        var loginUserInfo = utils.getLoginUserInfo(true);

                        /**
                         * 发布留言
                         * 【百姓吐槽详情、阿里裁判详情 】咨询师对发布人的回帖
                         * 【百姓吐槽详情、阿里裁判详情、案源详情、法律咨询详情】发布人对咨询师的回贴
                         *
                         * @param $form
                         */
                        rest.post({
                            form: $(form),
                            url: 'reply/commonReply',//
                            data: {
                                //表单中取 replyContent回复内容
                                //表单中取 upId针对回复的类型，【0：如果是第一层的回复;1:针对回复的id】百姓吐槽 2：阿里裁决取replyId
                                userId : loginUserInfo.userId,//登陆用户id
                                caseId : _info.offerId,//caseId：传offerId
                                businessType : 4,//业务类型【1：百姓吐槽 2：阿里裁决 3：法律咨询 4：案件竞拍 5：文书制作】
                                serviceName: serviceName,//当前回复的文章标题
                                sendUserId : _info.userId,//针对人
                                sourceArticleId :caseId//文章编号
                            },
                            success: function (result) {
                                C.msg.ok(result.msg || '回复成功');
                                $commonReplyList.find('.js-replybox').html('');
                                commonReplyListPage.refresh();
                                //
                                // C.template('#replycontentTmpl').loadBefore(result, _$replyBox.next('.js-replycontent'), function () {
                                //
                                // });
                            }
                        });
                    });
                });
            });
        }


    }



    //右侧律师推荐
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
