/*!
 * alilaw - 阿里法律
 * @author Li Hongwei
 * @version v1.0.0
 * @link https://www.yimicat.com
 */
define(function(require, exports, module) {
    "use strict";
    var l = require("store"),
        c = function(e) {
            var u = null,
                a = this,
                t = !0;
            this._clone = function(e) {
                return $.extend(!0, {}, e)
            }, this._updateOptions = function(e, t, s) {
                var r;
                if (void 0 === e) r = t;
                else if (null !== e && "object" === $.type(e)) {
                    for (var n in r = {}, e) e.hasOwnProperty(n) && (r[n] = e[n]);
                    for (var n in t) t.hasOwnProperty(n) && (r[n] = this._updateOptions(e[n], t[n], s))
                } else r = s ? t : e;
                return r
            }, this.updateOptions = function(e) {
                return u = this._updateOptions(u, e, !0), this
            }, this.setOptions = function(e) {
                return u = e, this
            }, this.getOptions = function() {
                return u
            }, this.jump = function() {
                // location.href = "login.html"
            }, this.checkLogin = function() {
                return null != l.get("token") && "" != l.get("token") || this.jump(), !0
            }, this.setRefresh = function(e) {
                t = e || !0
            }, this.getRefresh = function() {
                return t
            }, this.buildUrl = function(e, t) {
                var r = $.extend(!0, {}, t),
                    n = [];
                return e ? function(e, t, s) {
                    for (var r, n = ""; 0 < e.length;)(r = e.match(t)) ? (n += e.slice(0, r.index), n += s(r).toString(), e = e.slice(r.index + r[0].length)) : (n += e, e = "");
                    return n
                }(e, /\{(.+?)\}/, function(e) {
                    var t = e[1],
                        s = r[t];
                    return null == s && $.error("Can't found '" + t + "' from urlParams."), n.push(t), encodeURIComponent(s)
                }) : ""
            }, this.buildResult = function(e, t) {
                var n = $.extend(!0, {}, c.options.defaults, u, t);
                if (n.form) {
                    var s = $.extend(!0, {}, n.form.serializeObject(), n.data);
                    n.data = s
                }
                console.log("data:" + JSON.stringify(n.data));
                var r = n.success,
                    i = n.error,
                    o = n.fail;
                if (n.serializeRequestBody) try {
                    n.data = n.serializeRequestBody(n)
                } catch (e) {
                    console.error("序列化失败. " + e.message)
                }
                return n.success = function(e, t, s) {
                    if (n.isValidCode) {
                        if (e && -9 == e.code) return l.setCache("token", ""), a.jump(), !1;
                        if (e && e.code && 1 == e.code) {
                            if (n.deserializeResponseBody) try {
                                e = n.deserializeResponseBody(e, n)
                            } catch (e) {
                                console.error("发序列化失败. " + e.message)
                            }
                            r && r(e, t, s)
                        } else o && o(e, t, s)
                    } else {
                        if (n.deserializeResponseBody) try {
                            e = n.deserializeResponseBody(e, n)
                        } catch (e) {
                            console.error("发序列化失败. " + e.message)
                        }
                        r && r(e, t, s)
                    }
                }, n.error = function(e, t, s) {
                    var r;
                    401 == e.status ? console.error("status:" + e.status + "没有权限") : 404 == e.status ? console.error("status:" + e.status + "请求地址错误") : console.error("status:" + e.status), r = n.deserializeError ? n.deserializeError(e, t, s, n) : e && e.responseText || s || t, i && i(r, e, t, s)
                }, n.url = this.buildUrl(n.url, n.urlParams), u.baseUrl && 0 != n.baseUrl && null != n.baseUrl && "" != n.baseUrl && (n.url = u.baseUrl + n.url), n.type = e, n
            }, this.send = function(e, t) {
                try {
                    var s = this.buildResult(e, t);
                    return s.isNeedToken && this.checkLogin(), $.ajax(s)
                } catch (e) {
                    console.error(e.message)
                }
            }, this.patch = function(e) {
                return this.send("PATCH", e)
            }, this.get = function(e) {
                return this.send("GET", e)
            }, this.post = function(e) {
                return this.send("POST", e)
            }, this.put = function(e) {
                return this.send("PUT", e)
            }, this.del = function(e) {
                return this.send("DELETE", e)
            }, this.setOptions(e || c.options.defaults)
        };
    c.options = {}, c.options.defaults = {
        isValidCode: !0,
        form: null,
        baseUrl: null,
        isNeedToken: !0,
        dataType: "json",
        fail: null,
        serializeRequestBody: function(e) {
            return e.data
        },
        deserializeResponseBody: null,
        deserializeError: null,
        error: function(e, t, s, r) {
            console.error("网络连接失败")
        }
    };
    var e = null;
    e || (e = new c), module.exports = e
});