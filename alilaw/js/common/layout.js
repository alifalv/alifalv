/*!
 * 布局
 * @author Li Hongwei
 * @date 2018-07-02 11:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var utils = require('utils');
    var Vcity = require('./citySelect');
    var store = require('store');
    var dataList = require('dataList');
    var constants = require('constants');
    require('./sidebarRight');
    require('./loadAdHtml');

    //如果没有缓存城市列表，就重新ajax获取
    if (!store.getCache('allCity')) {
        dataList.getAllCityList(function(result) {
            console.log(result);
            store.setCache('allCity', result.data);
        }, function(result) {
            console.log(result.msg);
        }, function() {
            console.log('获取所有城市接口调用失败');
        });
    }

    //退出登录
    function _logout() {
        C.confirm('退出登录？', function() {
            store.removeCache('userInfo');
            store.removeCache('token');
            // location.replace(constants.viewUrl + 'login.html');
            location.replace('http://www.alifalv.cn/login.html');
        });
    }

    //保存当前城市编号
    function setCityId(cityName) {
        var allCityList = store.getCache('allCity');
        for (var i = 0; i < allCityList.length; i++) {
            if (cityName.indexOf(allCityList[i].cityName) > -1 || allCityList[i].cityName.indexOf(cityName) > -1) {
                store.setCache('cityId', allCityList[i].cityId);
                return;
            }
        }
    }

    //top
    if (utils.hasClass('.top')) {
        var userInfo = store.getCache('userInfo');
        if (!userInfo) {
            userInfo = {};
        }
        C.template('.top').renderReplace(userInfo, function() {
            require('thirdLogin');

            var cityName = store.getCache('cityName');
            if (cityName) {
                //缓存存在当前城市名称，就缓存当前城市编号
                console.log('取缓存：' + cityName);
                setCityId(cityName);
                $('#citySelect').val(cityName);

            } else {
                //调用高德地图插件获取当前城市名称
                require(['amap']);
                window.onAMapLoaded = function() {
                    AMap.plugin(['AMap.CitySearch'], function() { //异步同时加载多个插件
                        //实例化城市查询类
                        var citysearch = new AMap.CitySearch();
                        //自动获取用户IP，返回当前城市
                        citysearch.getLocalCity(function(status, result) {
                            if (status === 'complete' && result.info === 'OK') {
                                if (result && result.city && result.bounds) {
                                    var cityName = result.city;
                                    var citybounds = result.bounds;
                                    console.log('您当前所在城市：' + cityName);
                                    //地图显示当前城市
                                    setCityId(cityName);
                                    $('#citySelect').val(cityName);
                                    store.setCache('cityName', cityName);
                                }
                            } else {
                                console.log('您当前所在城市：' + result.info);
                            }
                        });
                    });
                };
            }

            //初始化top城市插件
            var test = new Vcity.CitySelector({
                input: 'citySelect',
                clickBtn: function(cityName) {
                    setCityId(cityName);
                    store.setCache('cityName', cityName);
                    $('#citySelect').val(cityName);
                }
            });

            //绑定退出按钮事件
            $('#logout_top_btn').on('click', function() {
                _logout();
            });
        });
    }



    //页头
    if (utils.hasClass('.header')) {
        C.template('.header').renderReplace({}, function() {
            $('#allsearch_btn').on('click', function() {
                if ($('#allsearch_text').val().trim().length > 0) {
                    window.location.href = constants.viewUrl + 'search.html?s=' + $.trim($('#allsearch_text').val());
                } else {
                    C.msg.fail('请输入搜索内容！');
                }
            });
        });
    }

    //页脚
    if (utils.hasClass('.footer')) {
        var color = $(document).find('.footer').data("color");
        var data = {};
        if (color == 'black') {
            data.color = 'ui-black'
        }

        C.template('.footer').renderReplace(data, function() {});
    }


    //用户中心菜单
    if (utils.hasClass('.user_menu')) {
        var userInfo = utils.getLoginUserInfo(true);
        C.template('.user_menu').renderReplace(userInfo, function() {});

        //用户中心菜单
        if (utils.hasClass('.user_reply')) {
            if (userInfo.userType == '2') {
                rest.post({
                    url: 'accessLog/listAccessLog', //112.律师最近访客和统计个数;9.20
                    data: {
                        userId: userInfo.userId
                    },
                    success: function(result) {
                        result.userType = userInfo.userType;
                        C.template('.user_reply').renderReplace(result, function() {});
                    },
                    fail: function(result) {
                        result.error = '接口异常';
                        C.template('.user_reply').renderReplace(result, function() {});
                    }
                });
            } else {

                rest.post({
                    url: 'advice/myReplyRecently/{userId}', //68.	个人普通用户账户中心-最近回复；9.20
                    urlParams: {
                        userId: userInfo.userId
                    },
                    success: function(result) {
                        result.userType = userInfo.userType;
                        C.template('.user_reply').renderReplace(result, function() {});
                    },
                    fail: function(result) {
                        result.error = '接口异常';
                        C.template('.user_reply').renderReplace(result, function() {});
                    }
                });
            }

        }

        //用户中心右侧统计
        if (utils.hasClass('.user_count')) {
            rest.post({
                url: 'article/counts/{userId}', //111.律师/咨询/关注/收藏数统计;9.19
                urlParams: {
                    userId: userInfo.userId
                },
                success: function(result) {
                    C.template('.user_count').renderReplace(result, function() {});
                },
                fail: function(result) {
                    C.alert.fail(result.msg);
                }
            });
        }
    }


    //页头
    if (utils.hasClass('.user_header')) {
        utils.refreshUserInfoCache(function() {
            var userInfo = utils.getLoginUserInfo(true);
            C.template('.user_header').renderReplace(userInfo, function($html) {
                rest.post({
                    url: 'user/messageTypeCount',
                    data: {
                        businessType: '', // 消息类型 1：系统消息 0： 回复消息 空：所有消息
                        isRead: 0, //0 ：未读 1 ：已读 ，不传 ：全部；
                        userId: userInfo.userId //用户ID
                    },
                    success: function(result) {
                        if (result.data.msgCount && result.data.msgCount > 0) {
                            $html.find('.j-allmsg-count').html(result.data.msgCount).removeClass('ui-hide');

                        } else {
                            $html.find('.j-allmsg-count').addClass('ui-hide');
                        }
                    },
                    fail: function(result) {
                        C.msg.fail(result.msg);
                    }
                });

                $('#logout_user_btn').on('click', function() {
                    _logout();
                });
            });

            //用户中心wrapper
            if (utils.hasClass('.user_wrapper')) {
                userInfo.home_sld = constants.home_sld;
                C.template('.user_wrapper').renderReplace(userInfo, function($html) {

                    if ((null == userInfo.freeNum || '0' == userInfo.freeNum) && userInfo.userType != 3) {


                        $html.find('.j-free-btn').addClass('disabled');
                        $html.find('.j-free-btn').on('click', function() {
                            C.msg.tips('免费次数已用完');
                            return false;
                        });
                    } else {
                        $html.find('.j-free-btn').on('click', function() {
                            utils.windowOpen(constants.viewUrl + 'user_consult.html');
                            return false;
                        });
                    }

                    //控制文书制作券按钮是否置灰
                    rest.post({
                        url: 'user/voucherCount', //147.免费文书制作申请;10.8
                        data: {
                            state: 0, //查所有不传此参数 0：默认 1：提交申请使用 2 过期 3，使用完毕
                            userId: userInfo.userId
                        },
                        success: function(result) {
                            if (null == result.data.voucherCount || result.data.voucherCount <= 0) {
                                $html.find('.j-quan-btn').addClass('disabled')
                            }
                        }
                    });

                    $html.find('.j-headimg').one('error', function() {
                        $(this).attr('src', '../img/defalut-head.png');
                    });

                    $('body').append('<div id="jtsgamfls_div"></div>');
                    var _$jtsgamfls_div = $('#jtsgamfls_div');
                    //交通事故案免费律师
                    $('#jtsgamfls_btn').on('click', function() {
                        // isExpire  //0:正常会员 1：过期会员 2 不是会员
                        if (!userInfo.hasOwnProperty('isExpire')) {
                            C.msg.tips('先开通会员');
                            return false;
                        }
                        if (userInfo.isExpire == '1') {
                            C.msg.tips('请先续费');
                            return false;

                        } else if (userInfo.isExpire == '2') {
                            C.msg.tips('请先开通会员');
                            return false;
                        }

                        utils.loadHtml(_$jtsgamfls_div, '../tmpl/jtsgamfls_dialog.html', function() {
                            var $jtsgamfls_dialog = $('#jtsgamfls_dialog');
                            var jtsgamflsDialog = C.dialog({
                                title: false,
                                closeBtn: false,
                                area: ['765px', '300px'],
                                content: $jtsgamfls_dialog //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                            });
                            _$jtsgamfls_div.find('.js-close-btn').on('click', function() {
                                layer.close(jtsgamflsDialog);
                            });

                            var isSending = false;
                            _$jtsgamfls_div.find('.js-ok-btn').on('click', function() {
                                if (isSending) {
                                    return false;
                                }
                                var load = C.load();
                                isSending = true;
                                rest.post({
                                    url: 'user/freeCaseDeclare', //78.	个人普通用户账户中心-免费申请 交通事故案件委托；9.14完成
                                    data: {
                                        userId: userInfo.userId
                                    },
                                    success: function(result) {
                                        C.alert.ok('您的申请已经提交成功，平台将尽快委派律师与您联系！', function() {
                                            layer.close(jtsgamflsDialog);
                                        });
                                    },
                                    fail: function(result) {
                                        C.alert.fail(result.msg);
                                    },
                                    complete: function() {
                                        isSending = false;
                                        layer.close(load);
                                    }
                                });
                            });
                        });
                    });
                });
            }
            //个人中心导航
            if (utils.hasClass('.user_managetabs')) {
                C.template('.user_managetabs').renderReplace(userInfo, function() {});
            }
        });
    }


    /**
     * 记录访问者与被访问者
     */
    function addBrowse(userId, counselorId) {
        rest.post({
            url: 'user/addBrowse', //6.	律师个人信息/普通用户/企业普通用户查询
            data: {
                userId: userId, //用户Id
                counselorId: counselorId //被访问者的Id
            },
            success: function(result) {},
            error: function() {}
        });
    }


    //个人中心
    if (utils.hasClass('.home_right') ||
        utils.hasClass('.home_evaluation') ||
        utils.hasClass('.home_header')) {

        var urlParams = utils.getUrlParams();
        // var userId = urlParams.userId;
        if (null == urlParams.userId) {
            var userId = sessionStorage.getItem('domainUserId');
        } else {
            var userId = urlParams.userId;
        }

        if (null == userId || '' == userId) {
            utils.jump404('用户编号为空');
        }

        var _loginUserInfo = utils.getLoginUserInfo();
        //用户登录且访问的不是自己的主页
        if (_loginUserInfo && _loginUserInfo.userId != userId) {
            addBrowse(_loginUserInfo.userId, userId);
        }

        rest.post({
            url: 'user/userInfo/{userId}', //6.	律师个人信息/普通用户/企业普通用户查询
            urlParams: {
                userId: userId
            },
            success: function(result) {

                //非律师跳转404
                if (result.data.userType != '2') {
                    utils.jump404('找不到该律师');
                }

                //个人主页右侧个人信息
                if (utils.hasClass('.home_right')) {
                    C.template('.home_right').renderReplace(result, function($html) {
                        // require('qrcode');
                        // var qrcode = new QRCode(document.getElementById("user_right_qrcode"), {
                        //     width : 145,
                        //     height : 145
                        // });
                        // qrcode.makeCode(location.href);

                        var loginUserInfo = utils.getLoginUserInfo();
                        if (loginUserInfo && loginUserInfo.userId == userId) {
                            //屏蔽相关功能按钮
                            $html.find('.j-operbtn-box').addClass('ui-hide');
                        }

                        if (result.data && result.data.userType == '2' && result.data.isAuthentication != '2') {
                            $html.find('.j-markdoc-btn').prop('disabled', true);
                            $html.find('.j-free-btn').prop('disabled', true);
                            $html.find('.j-dj-btn').prop('disabled', true);
                        }

                        //免费咨询
                        $html.on('click', '.j-free-btn', function() {
                            var loginUserInfo = utils.getLoginUserInfo();
                            if (null == loginUserInfo.freeNum || '0' == loginUserInfo.freeNum) {
                                C.msg.tips('免费次数已用完');
                            } else {
                                location.href = constants.viewUrl + 'user_separateconsult.html?userId=' + userId;
                            }
                            return false;
                        });

                        //文书制作
                        $html.on('click', '.j-markdoc-btn', function() {

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


                    });
                }

                //个人主页右侧评价信息
                if (utils.hasClass('.home_evaluation')) {

                    //最近留言
                    rest.post({
                        url: 'user/getUserEvaluate/{userId}',
                        urlParams: {
                            userId: userId
                        },
                        success: function(resultEvaluate) {
                            result.data.evaluationList = resultEvaluate.data;
                            C.template('.home_evaluation').renderReplace(result, function($html) {});
                        },
                        error: function() {}
                    });


                }

                //个人中心导航
                if (utils.hasClass('.home_header')) {
                    C.template('.home_header').renderReplace(result, function() {});
                }

            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    }

});