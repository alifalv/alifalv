/*!
 * rest
 * @author Li Hongwei
 * @date 2015-11-05
 * @version 1.0
 */
var rest = (function () {
    'use strict';
        var ajaxMap = [];
        var ajaxObj;
        var store = {
            get: function (value) {
                return sessionStorage.getItem(value);
            },
            set: function (key, value) {
                return sessionStorage.setItem(key, value);
            }
        };

        var Rest = function (options) {
            var currOptions = null;
            var _this = this;
            var isRefresh = true;

            // 替换字符串中的 {key} 内容
            var _gsub = function (source, pattern, replacement) {
                var result = '', match;
                while (source.length > 0) {
                    match = source.match(pattern);
                    if (match) {
                        result += source.slice(0, match.index);
                        result += replacement(match).toString();
                        source = source.slice(match.index + match[0].length);
                    } else {
                        result += source;
                        source = '';
                    }
                }
                return result;
            };

            // 克隆对象, 这个方法只能克隆简单对象
            this._clone = function(obj) {
                return $.extend(true, {}, obj);
            };

            this._updateOptions = function(source, newValue, force) {
                var result;
                if (source === undefined) {
                    result = newValue;
                } else if (source !== null && $.type(source) === 'object') {
                    result = {};
                    for ( var key in source) {
                        if (!source.hasOwnProperty(key))
                            continue;
                        result[key] = source[key];
                    }
                    for ( var key in newValue) {
                        if (!newValue.hasOwnProperty(key))
                            continue;
                        result[key] = this._updateOptions(source[key],
                            newValue[key], force);
                    }
                } else if (force) {
                    result = newValue;
                } else {
                    result = source;
                }
                return result;
            };

            // 更新当前配置
            this.updateOptions = function(newOptions) {
                currOptions = this._updateOptions(currOptions, newOptions, true);
                return this;
            };


            // 使用新配置替换当前配置
            this.setOptions = function (newOptions) {
                currOptions = newOptions;
                return this;
            };

            // 获取当前配置
            this.getOptions = function () {
                return currOptions;
            };

            this.jump = function() {
                location.href = "nologin.html"
            };

            //检查是否登录
            this.checkLogin = function() {
                if (store.get('t') == null || store.get('t') == "") {
                    this.jump();
                }
                return true;
            };

            this.setRefresh = function(_isRefresh) {
                isRefresh = _isRefresh || true;
                //console.log('set_____' +isRefresh);
            };

            this.getRefresh = function() {
                //console.log('get_____' +isRefresh);
                return isRefresh;
            };

            // 构造请求地址
            this.buildUrl = function (sourceUrl, urlParams) {
                var pathParams = $.extend(true, {}, urlParams);
                var usedPathParams = [];
                var result = !sourceUrl ? "" : _gsub(sourceUrl, /\{(.+?)\}/,
                    function (match) {
                        var key = match[1];
                        var value = pathParams[key];
                        if (value === undefined || value === null) {
                            $.error("Can't found '" + key + "' from urlParams.");
                        }
                        usedPathParams.push(key);
                        return encodeURIComponent(value);
                    });
                return result;
            };

            // 构造请求对象, 该对象可以直接用于$.ajax方法
            // 该方法在构造失败时会调用$.error抛出异常
            this.buildResult = function (method, _result) {
                // alert("defaults" + JSON.stringify(Rest.options.defaults))
                // alert("_result" +JSON.stringify(_result))
                // alert("currOptions" +JSON.stringify(currOptions))

                var result = $.extend(true, {}, Rest.options.defaults,currOptions,_result);

                if(result.form) {
                    var _data = $.extend(true, {}, result.form.serializeObject(), result.data);
                    result.data = _data;
                }

                // console.log("result:" +JSON.stringify(result));
                console.log("data:" +JSON.stringify(result.data));

                var successHandler = result.success;
                var errorHandler = result.error;
                // var okHandler = result.ok;
                var failHandler = result.fail;
                // 序列化请求体
                if (result.serializeRequestBody) {
                    try {
                        result.data = result.serializeRequestBody(result);
                    } catch (e) {
                        console.error('序列化失败. ' + e.message);
                    }
                }

                // 请求成功回调包装方法, 会尝试反序列化返回体
                result.success = function (data, textStatus, jqXHR) {
                    console.log('success_data:' + JSON.stringify(data));

                    if(result.isValidCode) {
                        if(data && data.code && data.code == 1) {
                            if (result.deserializeResponseBody) {
                                try {
                                    data = result.deserializeResponseBody(data, result);
                                } catch (e) {
                                    console.error('发序列化失败. ' + e.message);
                                }
                            }
                            if (successHandler)
                                successHandler(data, textStatus, jqXHR);
                        } else {
                            if (failHandler)
                                failHandler(data, textStatus, jqXHR);
                        }
                    } else {
                        if (result.deserializeResponseBody) {
                            try {
                                data = result.deserializeResponseBody(data, result);
                            } catch (e) {
                                console.error('发序列化失败. ' + e.message);
                            }
                        }
                        if (successHandler)
                            successHandler(data, textStatus, jqXHR);
                    }
                };

                // 请求失败回调包装方法, 会尝试反序列化返回体
                result.error = function (jqXHR, textStatus, errorThrown) {
                    if(jqXHR.status == 401) {
                        // tools.msg.warn('没有权限');
                        console.error('status:' + jqXHR.status + '没有权限');
                    } else if(jqXHR.status == 404) {
                        // tools.msg.fail('请求地址错误');
                        console.error('status:' + jqXHR.status + '请求地址错误');
                    } else {
                        console.error('status:' + jqXHR.status);
                    }

                    // console.log(JSON.stringify(jqXHR) + "," + JSON.stringify(textStatus) +  ","+ JSON.stringify(errorThrown))
                    var message;
                    if (result.deserializeError) {
                        message = result.deserializeError(jqXHR, textStatus, errorThrown, result);
                    } else {
                        message = (jqXHR && jqXHR.responseText) || errorThrown || textStatus;
                    }
                    if (errorHandler) {
                        errorHandler(message, jqXHR, textStatus, errorThrown);
                    }
                };

                // 构造请求地址
                result.url = this.buildUrl(result.url, result.urlParams);
                if (currOptions.baseUrl) {
                    if (result.baseUrl != false && result.baseUrl != null && result.baseUrl != '') {
                        result.url = currOptions.baseUrl + result.url;
                    }
                }

                console.log(result.url)

                result.type = method;

                return result;
            };

            // 立刻发送一个请求, 该请求会捕获构造期异常, 转而调用请求失败回调方法
            this.send = function (method, result) {
                try {
                    var r = this.buildResult(method, result);
                    if(r.isNeedToken) {
                        this.checkLogin();
                    }

                    // if(r.data) {
                    //     if(JSON.stringify(r.data).indexOf('tokenid')>-1) {
                    //         if(r.openPanel) {
                    //             if (!store.get('tokenid')) {
                    //                 //console.log('localstroge timeout')
                    //                 this.checkLogin(r.isJump);
                    //                 return false;
                    //             }
                    //         }
                    //     }
                    // }
                    return $.ajax(r);
                } catch (e) {
                    console.error(e.message);
                }
            };

            this.patch = function (request) {
                return this.send('PATCH', request);
            };

            this.get = function (request) {
                console.log(12312)
                return this.send('GET', request);
            };

            this.post = function (request) {
                return this.send('POST', request);
            };

            this.put = function (request) {
                return this.send('PUT', request);
            };

            this.del = function (request) {
                return this.send('DELETE', request);
            };

            this.setOptions(options || Rest.options.defaults);

        };

        // 配置模板容器
        Rest.options = {};

        // json 默认配置模板
        Rest.options.defaults = {
            isValidCode: true,
            form : null,
            baseUrl:  null,
            isNeedToken : true,
            //compatible: null,
            dataType: "json",
            // contentType: "application/json",
            // headers: {
            //     accept: "application/json"
            // },
            ok : null,
            fail : null,
            serializeRequestBody: function (result) {
                // if (JSON && JSON.stringify) {
                //     return JSON.stringify(result.data);
                // } else if ($.toJSON) {
                //     return $.toJSON(result.data);
                // }
                // throw new Error('需要序列化');
                return result.data;

            },
            deserializeResponseBody: null,
            deserializeError: null,
            // 默认的错误处理
            error: function (message, jqXHR, textStatus, errorThrown) {
                console.error(message)
                console.error(textStatus)
                console.error(errorThrown)

                console.error("网络连接失败")
            }
        };

        // 初始化默认实例
        var _rest = null;
        if(!_rest) {
            _rest = new Rest();
        }
    return _rest;
})();
