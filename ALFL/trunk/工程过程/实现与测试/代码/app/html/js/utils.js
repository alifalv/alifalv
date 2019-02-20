"use strict";
var utils = (function() {


    // $top_back.hide();
    if ($(document).find('.backtop').length > 1) {
        var $top_back = $('.backtop');

        //回到顶部按钮显示控制
        window.onscroll = function() {
            if ($(this).scrollTop() > 250) {
                $top_back.fadeIn();
            } else {
                $top_back.fadeOut();
            }
        }

        //回到顶部
        $top_back.on('tap', function() {
            $('body,html').animate({ scrollTop: 0 }, 300);
        });

        mui(".backtop").on('tap', function() {
            $('body,html').animate({ scrollTop: 0 }, 300);
            return false;
        });
    }




    var constants = {
        // apiUrl : 'http://39.108.169.135:80/ali-legal/api/',
     // apiUrl: 'http://localhost:8092/ali-legal/api/',
           apiUrl: 'http://www.alifalv.cn/ali-legal/api/',
        // apiUrl: 'http://192.168.31.121:8880/ali-legal/api/',
        // apiUrl : 'http://192.168.31.135:8888/ali-legal/api/',
		// apiUrl: 'http://192.168.31.150:80/ali-api/api/',

        pcUrl: 'http://www.alifalv.cn/',
        shareImgUrl: 'http://www.alifalv.cn/img/logo.png'

    };

    /* uuid 过长  微信不支持 生成18位订单号   */
    var _uuid = function() {
        var s = [];
        var hexDigits = "0123456789";
        for (var i = 0; i < 5; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 10), 1);
        }

        var _uuid = s.join("");

        _uuid = new Date().getTime() + _uuid;

        return _uuid;
    };

    var _getUrlParams = function() {
        var params = {};
        var query = location.search.substring(1);
        var pairs = query.split("&");
        for (var i = 0; i < pairs.length; i++) {
            var pos = pairs[i].indexOf('=');
            if (pos == -1) continue;
            var param_name = pairs[i].substring(0, pos);
            var value = pairs[i].substring(pos + 1);
            value = decodeURI(decodeURI(value));
            params[param_name] = value;
        }
        return params;
    };


    var _loadHtml = function(obj, url, callback) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'html',
            success: function(html) {
                obj.html(html);
                callback && callback();
            }
        });
    };

    function jumpPay(successCallback) {
        window.paySuccess = function() {
            successCallback && successCallback()
        };
        creatMywebview('Order-pay.html?businessOrder=' + businessOrder, 'Order-pay.html?businessOrder=' + businessOrder);
    }


    var _hasClass = function(className) {
        return $(document).find(className).length > 0;
    };

    /**
     * 跳转到登陆页面
     * @param isJumpBack true url带上回掉地址
     * @private
     */
    var _jumpLogin = function(isJumpBack) {
        var loginUrl = 'Login.html';
        if (isJumpBack) {
            loginUrl += '?jump=' + window.location.href;
        }
        window.location.replace(loginUrl);
    };

    var _checkLogin = function() {
        if (constants.debug) {
            return false;
        }
        var userInfo = store.getCache('userInfo');

        if (null === userInfo || '' == userInfo) {
            _jumpLogin(true);
        }

        if (null === userInfo.userId || '' == userInfo.userId) {
            _jumpLogin(true);
        }

        rest.post({
            url: 'user/userInfo/{userId}',
            urlParams: {
                userId: userInfo.userId
            },
            success: function(result) {
                store.setCache('userInfo', result.data)
            },
            fail: function() {
                _jumpLogin(true);
            },
            error: function() {
                _jumpLogin(true);
            }
        });
    };

    /**
     * 获取登录的用户信息
     * @param value true cache为空就跳转登录/ false 只获取用户信息
     * @return {*}
     * @private
     */
    function _getLoginUserInfo(value) {
        var userInfo = store.getCache('userInfo');
        if (!value) {
            return userInfo;
        }
        if (null == userInfo) {
            _jumpLogin(true);
        }

        //企业咨询者没有注册会员跳转到充值页面
        if (userInfo.userType == 3 && userInfo.vipLevel == 0) {
            location.replace(constants.viewUrl + 'reg_vip_enterprise.html?userId=' + userInfo.userId); //TODO
            return;
        }
        return userInfo;
    }

    var _createImgUrl = function(imgName) {
        return constants.apiUrl + imgName; //TODO
    };

    var _bindSendSms = function($sendsms_btn, $mobile) {

        function _click() {
            $mobile.isValid(function(v) {
                if (v) {
                    $sendsms_btn.addClass('ui-btn-disabled').off('click').html('发送中...');
                    rest.post({
                        url: 'sms/send/{mobile}',
                        urlParams: {
                            mobile: $.trim($mobile.val())
                        },
                        success: function(result) {
                            _clicked();
                            C.msg.ok(result.msg);
                        },
                        fail: function(result) {
                            $sendsms_btn.removeClass('ui-btn-disabled').on('click', _click).html('点此获取');
                            C.msg.fail(result.msg);
                        },
                        error: function() {
                            $sendsms_btn.removeClass('ui-btn-disabled').on('click', _click).html('点此获取');
                            C.msg.fail('连接失败');
                        }
                    });
                }
            });
            return false;
        }

        function _clicked() {
            var _intervalTime = 30;
            var _interval = setInterval(function() {
                if (_intervalTime > 0) {
                    $sendsms_btn.html('等待' + _intervalTime + '秒');
                    _intervalTime--;
                } else {
                    clearInterval(_interval);
                    $sendsms_btn.html('点此获取').removeClass('ui-btn-disabled').on('click', _click);
                }
            }, 1000);
        }

        //发短信
        $sendsms_btn.on('click', _click);
    };

    var _jump404 = function() {
        location.replace(constants.viewUrl + '404.html');
    };

    var _jump500 = function(text) {
        if (text) {
            location.replace(constants.viewUrl + '500.html?txt=' + text);
        }
        location.replace(constants.viewUrl + '500.html');
    };


    function _formatTime(str) {
        var t = new Date(str);
        return t.getTime();
    }

    function _getDateDiff(dateTimeStamp) {
        var result;
        var minute = 1000 * 60;
        var hour = minute * 60;
        var day = hour * 24;
        var halfamonth = day * 15;
        var month = day * 30;
        var now = new Date().getTime();
        var diffValue = now - dateTimeStamp;
        if (diffValue < 0) { return; }
        var monthC = diffValue / month;
        var weekC = diffValue / (7 * day);
        var dayC = diffValue / day;
        var hourC = diffValue / hour;
        var minC = diffValue / minute;
        if (monthC >= 1) {
            result = "" + parseInt(monthC) + "月前";
        } else if (weekC >= 1) {
            result = "" + parseInt(weekC) + "周前";
        } else if (dayC >= 1) {
            result = "" + parseInt(dayC) + "天前";
        } else if (hourC >= 1) {
            result = "" + parseInt(hourC) + "小时前";
        } else if (minC >= 1) {
            result = "" + parseInt(minC) + "分钟前";
        } else
            result = "刚刚";
        return result;
    }


    /**
     * 认证状态
     * 0未认证 1审核中 2认证通过 3认证失败
     * @param value 值
     * @private
     */
    function _getAuthenticationTxt(value) {
        if (value == '0') {
            return '<span class="ui-color-gray">未认证</span>';
        } else if (value == '1') {
            return '<span class="ui-color-blue">审核中</span>';
        } else if (value == '2') {
            return '<span class="ui-color-greed">认证通过</span>';
        } else if (value == '3') {
            return '<span class="ui-color-red">认证失败</span>';
        } else {
            return '<span class="ui-color-gray">异常</span>';
        }
    }

    function getCityId() {
        return 199;
    }

    return {
        getAuthenticationTxt: _getAuthenticationTxt,
        uuid: _uuid,
        getUrlParams: _getUrlParams,
        loadHtml: _loadHtml,
        hasClass: _hasClass,
        checkLogin: _checkLogin,
        jumpLogin: _jumpLogin,
        getLoginUserInfo: _getLoginUserInfo,
        createImgUrl: _createImgUrl,
        bindSendSms: _bindSendSms,
        jump404: _jump404,
        jump500: _jump500,
        getDateDiff: _getDateDiff,
        formatTime: _formatTime,
        getCityId: getCityId,
        constants: constants
    }
})();