/*!
 * 首页
 * @author Li Hongwei
 * @date 2018-07-02 11:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    if (null == urlParams.userId) {
        var userId = sessionStorage.getItem('domainUserId');
        // sessionStorage.removeItem('domainUserId');
    } else {
        var userId = urlParams.userId;
    }

    if (null == userId || '' == userId) {
        utils.jump404('用户编号为空');
    }

    var Page = require('page');

    //个人信息
    rest.post({
        url: 'user/userInfo/{userId}',
        urlParams: {
            userId: userId
        },
        success: function(result) {
            //title动态赋值
            $(document).attr("title", '阿里法律 - ' + result.data.cityName + result.data.realName + '律师的个人主页');
            C.template('#userInfoTmpl').renderReplace(result, function($html) {

                //当前地址生成二维码
                // require('qrcode');
                // var qrcode = new QRCode(document.getElementById("user_qrcode"), {
                //     width : 145,
                //     height : 145
                // });
                // qrcode.makeCode(location.href);

                //非认证律师按钮不可操作
                if (result.data && result.data.userType == '2' && result.data.isAuthentication != '2') {
                    $html.find('.j-free-btn').prop('disabled', true);
                    $html.find('.j-mark-btn').prop('disabled', true);
                    $html.find('.j-dj-btn').prop('disabled', true);
                }

                //文书制作
                $html.on('click', '.j-mark-btn', function() {

                    var loginUserInfo = utils.getLoginUserInfo(true);

                    C.confirm('确认请他帮助制作文书？', function() {
                        rest.post({
                            url: 'order/makeWrit',
                            async: false,
                            data: {
                                userId: loginUserInfo.userId, //申请人的userId
                                realName: loginUserInfo.realName, //申请人的真实姓名（数据库中的真实姓名）
                                favoreeUserId: userId //制作文书人的userId
                            },
                            success: function(result) {
                                C.alert.tips(result.msg, function() {
                                    utils.jumpPay(result.data.businessOrder, function() {
                                        C.msg.ok('申请成功！');
                                    });
                                });
                            },
                            fail: function(result) {
                                C.msg.fail(result.msg);
                            }
                        });
                    });
                    return false;
                });

                //免费咨询
                $html.on('click', '.j-free-btn', function() {
                    var loginUserInfo = utils.getLoginUserInfo();
                    if (null == loginUserInfo.freeNum || 0 == loginUserInfo.freeNum) {
                        C.msg.tips('免费次数已用完');
                    } else {
                        location.href = constants.viewUrl + 'user_separateconsult.html?userId=' + userId;
                    }
                    return false;
                });


                //支付协议律师费定金
                $html.on('click', '.j-dj-btn', function() {

                    var loginUserInfo = utils.getLoginUserInfo(true);

                    C.confirm('确认现在就支付协议律师费定金？', function() {
                        rest.post({
                            url: 'order/bargainMoney',
                            async: false,
                            data: {
                                userId: loginUserInfo.userId, //登陆用户Id
                                favoreeUserId: userId //律师的Id
                            },
                            success: function(result) {
                                C.alert.tips(result.msg, function() {
                                    utils.jumpPay(result.data.businessOrder, function() {
                                        C.msg.ok('支付成功！');
                                    });
                                });
                            },
                            fail: function(result) {
                                C.msg.fail(result.msg);
                            }
                        });
                    });
                    return false;
                });


                //最近留言
                rest.post({
                    url: 'user/getUserEvaluate/{userId}',
                    urlParams: {
                        userId: userId
                    },
                    success: function(result) {
                        C.template('#userReplyTmpl').loadBefore(result, $html.find('.j-user-reply'), function() {

                        });
                    },
                    fail: function(r) {
                        result.error = '获取留言异常';
                        C.template('#userReplyTmpl').loadBefore(result, $html.find('.j-user-reply'), function() {});
                    }
                });

                var isStar = 1;
                var followId = '';
                var starBtnLoaded = false;

                function searchFollow() {
                    starBtnLoaded = false;
                    rest.post({
                        url: 'user/isAliFollow',
                        data: {
                            userId: loginUserInfo.userId, //用户编号
                            counselorId: result.data.userId //律师ID
                        },
                        success: function(result) {
                            starBtnLoaded = true;
                            if (result.data.followId != '' && result.data.followId != '0') {
                                followId = result.data.followId;
                                isStar = 0;
                                $html.find('#star_btn i').addClass('ui-color-blue2').removeClass('ui-color-gray');
                            } else {
                                isStar = 1;
                                $html.find('#star_btn i').removeClass('ui-color-blue2').addClass('ui-color-gray');
                            }
                        },
                        error: function() {
                            C.msg.fail('收藏状态加载失败,请刷新页面');
                        }
                    });
                }

                function bindStarBtn($html) {
                    //关注律师信息
                    $html.on('click', '#star_btn', function() {
                        var loginUserInfo = utils.getLoginUserInfo(true);
                        if (!starBtnLoaded) {
                            C.msg.tips('正在加载收藏状态');
                            return false;
                        }
                        rest.post({
                            url: 'user/insertAliFollow',
                            data: {
                                userId: loginUserInfo.userId,
                                counselorId: userId,
                                type: isStar, //类型 1：关注 0 取消关注
                                followId: followId //关注id 取消关注才传
                            },
                            success: function(result) {
                                followId = '';
                                searchFollow();
                                if (isStar == '1') {
                                    C.msg.ok('关注成功');
                                } else {
                                    C.msg.ok('取消成功');
                                }
                            },
                            fail: function(result) {
                                C.msg.fail(result.msg);
                            }
                        });
                        return false;
                    })
                }

                var loginUserInfo = utils.getLoginUserInfo();
                if (loginUserInfo) {
                    //有用户信息先加载收藏状态

                    if (loginUserInfo.userId == userId) {
                        //是自己屏蔽收藏按钮
                        $html.find('#star_btn').addClass('ui-hide');

                        //屏蔽相关功能按钮
                        $html.find('.j-operbtn-box').addClass('ui-vhide');

                    } else {
                        searchFollow();
                    }
                }
                bindStarBtn($html);
            });

            new Page($('#workList'), {
                addData: {
                    userId: userId
                },
                hasPage: false,
                url: 'article/myWork/{model}/{userId}/{pageNo}/{pageSize}',
                urlParams: {
                    model: 1,
                    userId: userId
                },
                pageSize: 5
            });

            //我的风采
            new Page($('#styleList'), {
                addData: {
                    userId: userId
                },
                hasPage: false,
                url: 'article/myMien/{model}/{userId}/{pageNo}/{pageSize}',
                urlParams: {
                    model: 1,
                    userId: userId
                },
                pageSize: 6,
                htmlLoad: function($html) {
                    require('viewer');
                    var viewer = new Viewer(document.getElementById('styleList'));
                }
            });

            //我的随笔
            new Page($('#essayList'), {
                addData: {
                    userId: userId
                },
                hasPage: false,
                url: 'article/myEssay/{model}/{userId}/{pageNo}/{pageSize}',
                urlParams: {
                    model: 1,
                    userId: userId
                },
                pageSize: 10,
                htmlLoad: function() {
                    var Swiper = require('swiper');
                    var swiper = new Swiper('.swiper-container', {
                        slidesPerView: 4,
                        prevButton: '.swiper-button-prev',
                        nextButton: '.swiper-button-next'
                    });
                }
            });

            //成功案例
            new Page($('#caseList'), {
                addData: {
                    userId: userId
                },
                hasPage: false,
                url: 'article/mySuccess/{model}/{userId}/{pageNo}/{pageSize}',
                urlParams: {
                    model: 1,
                    userId: userId
                },
                pageSize: 4
            });
        },
        fail: function(result) {
            utils.jump500(result.msg);
        }
    });
});