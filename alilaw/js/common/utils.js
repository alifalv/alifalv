/*!
 * utils
 * @author Li Hongwei
 * @date 2018-07-03 17:36
 * @version 1.0
 */
define(function(require, exports, module) {

    "use strict";
    var store = require('store');
    var constants = require('constants');

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


    var _hasClass = function(className) {
        return $(document).find(className).length > 0;
    };

    /**
     * 跳转到登陆页面
     * @param isJumpBack true url带上回掉地址
     * @private
     */
    var _jumpLogin = function(isJumpBack) {
        var loginUrl = constants.viewUrl + 'login.html';
        if (isJumpBack) {
            loginUrl += '?jump=' + window.location.href;
        }
        window.location.replace(loginUrl);
    };

    var _checkLogin = function() {
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
     * 新窗口打开
     * @param url
     */
    function windowOpen(url) {
        // var a = document.createElement('a');
        // a.setAttribute('href', url);
        // a.setAttribute('target', '_blank');
        // a.setAttribute('id', 'winopen');
        // // // 防止反复添加
        // // if(!document.getElementById('winopen')) {
        //     document.body.appendChild(a);
        // // }
        // // a.click();
        // if($('body').find('#winopen').length == 0) {
        //     $('body').append('<a href="' + url + '" target="_blank" id="winopen"></a>');
        //
        // }else {
        //     $('#winopen').attr('href', url);
        // }
        // $('body').find('#winopen')[0].click();
        //
        // var openLink = $("#winopen");
        // openLink.attr('href', url);
        // openLink[0].click();

        var a = $('<a href="' + url + '" target="_blank">链接</a>'); //生成一个临时链接对象
        var d = a.get(0);
        var e = document.createEvent('MouseEvents');
        e.initEvent('click', true, true); //模拟点击操作
        d.dispatchEvent(e);
        a.remove(); // 点击后移除该对象
    }


    /**
     * 防止跨角色操作
     * @param value constants.js userType
     */
    function checkUserType(value) {
        var _loginUserInfo = _getLoginUserInfo();
        if (_loginUserInfo.userType != value) {
            _jump500('非法操作');
        }
    }

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

        //企业普通用户没有注册会员跳转到充值页面
        if (userInfo.userType == 3 && userInfo.vipLevel == 0) {
            location.replace(constants.viewUrl + 'reg_vip_enterprise.html?userId=' + userInfo.userId);
            return;
        }
        return userInfo;
    }

    var _createImgUrl = function(imgName) {
        return constants.apiUrl + imgName;
    };

    var _createFileUrl = function(fileName) {
        return constants.apiUrl + fileName;
    };

    var _bindSendSms = function($sendsms_btn, $mobile, url) {

        function _click() {
            $mobile.isValid(function(v) {
                if (v) {
                    $sendsms_btn.addClass('ui-btn-disabled').off('click').html('发送中...');
                    rest.post({
                        url: url || 'sms/send/{mobile}',
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

    var _jump404 = function(text) {
        if (text) {
            location.replace(constants.viewUrl + '404.html?txt=' + text);
        } else {
            location.replace(constants.viewUrl + '404.html');
        }
        return false;
    };

    var _jump500 = function(text) {
        if (text) {
            location.replace(constants.viewUrl + '500.html?txt=' + text);
        } else {
            location.replace(constants.viewUrl + '500.html');
        }
        return false;
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


    function setFormValue($form, _data) {
        $form.find('option').prop('selected', false);
        for (var key in _data) {
            var _$obj = $form.find('[name="' + key + '"]');
            if (_$obj.is("select")) {
                _$obj.find('option[value="' + _data[key] + '"]').prop('selected', true);
            } else {
                _$obj.val(_data[key]);
            }
        }
    }

    function dateFormat(date, fmt) {
        var o = {
            "M+": date.getMonth() + 1, //月份
            "d+": date.getDate(), //日
            "h+": date.getHours(), //小时
            "m+": date.getMinutes(), //分
            "s+": date.getSeconds(), //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    function getNowDateStr() {
        var date = new Date();
        return dateFormat(date, 'yyyy-MM-dd hh:mm:ss');
    }

    /**
     * 链接跳转
     * @param url 链接地址
     * @param _isOpenNewWindow 是否在新窗口打开
     */
    function jumpUrl(url, _isOpenNewWindow) {
        if (_isOpenNewWindow) {

            windowOpen(url);
            // window.open(url)
        } else {
            location.href = url;
        }
    }


    /**
     * 支付跳转
     * @param businessOrder 订单编号
     * @param successCallback 支付成功回调
     */
    function jumpPay(businessOrder, successCallback) {
        window.paySuccess = function() {
            successCallback && successCallback()
        };
        windowOpen(constants.viewUrl + 'user_payinfo.html?businessOrder=' + businessOrder);
        // window.open(constants.viewUrl + 'user_payinfo.html?businessOrder=' + businessOrder)
    }

    /**
     * 刷新用户信息缓存
     * @param fnc 回调
     */
    function refreshUserInfoCache(fnc) {
        var loginUserInfo = _getLoginUserInfo(true);
        rest.post({
            url: 'user/userInfo/{userId}', //6.	律师个人信息/普通用户/企业普通用户查询
            urlParams: {
                userId: loginUserInfo.userId
            },
            success: function(result) {
                store.setCache('userInfo', result.data);
                fnc && fnc();
            }
        });
    }

    /**
     * 是否已认证
     */
    function isAuthenticationUser() {
        var loginUserInfo = _getLoginUserInfo();
        if (loginUserInfo.isAuthentication == '2') {
            return true;
        }
        return false;
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
        createFileUrl: _createFileUrl,
        bindSendSms: _bindSendSms,
        jump404: _jump404,
        jump500: _jump500,
        getDateDiff: _getDateDiff,
        formatTime: _formatTime,
        setFormValue: setFormValue,
        getNowDateStr: getNowDateStr,
        jumpUrl: jumpUrl,
        jumpPay: jumpPay,
        refreshUserInfoCache: refreshUserInfoCache,
        windowOpen: windowOpen,
        isAuthenticationUser: isAuthenticationUser,
        checkUserType: checkUserType
    }

});