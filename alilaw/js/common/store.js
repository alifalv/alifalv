/*!
 * store
 * @author Li Hongwei
 * @date 2018-07-08
 * @version 1.0
 */
define(function (require, exports, module) {
    "use strict";

    var _store = {
        getSession: function (value) {
            return JSON.parse(sessionStorage.getItem(value));
        },
        setSession: function (key, value) {
            return sessionStorage.setItem(key, JSON.stringify(value));
        },
        removeSession: function (key) {
            sessionStorage.removeItem(key);
        },
        getCache: function (value) {
            var _cache = JSON.parse(localStorage.getItem(value));
            if(null == _cache) {
                return null;
            }
            if(_cache.timeout == 0
                || new Date().getTime() - _cache.time > _cache.timeout) {
                return _cache.data;
            } else {
                return null;
            }
        },
        setCache: function (key, value, time) {
            var _cache = {};
            _cache.data = value;
            _cache.time = new Date().getTime();
            if(time) {
                _cache.timeout = time;
            } else {
                _cache.timeout = 0;
            }
            return localStorage.setItem(key, JSON.stringify(_cache));
        },
        removeCache: function (key) {
            localStorage.removeItem(key);
        }
    };

    module.exports = _store;
});
