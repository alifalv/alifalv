/*!
 * msg
 * @author Li Hongwei
 * @date 2018-07-08
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var C = require('core');

    var _showMsg = function(type, msg, func, time) {
        layer.msg(msg || '&nbsp;', {
            icon: type,
            zIndex: layer.zIndex,
            time: time || 3000 //2秒关闭（如果不配置，默认是3秒）
        }, function(){
            func && func();
        });
    };

    var _msg = {
        ok: function (msg, func, time) {
            _showMsg(1, msg, func, time);
        },
        fail: function (msg, func, time) {
            _showMsg(2, msg, func, time);
        },
        ask: function (msg, func, time) {
            _showMsg(3, msg, func, time);
        },
        lock: function (msg, func, time) {
            _showMsg(4, msg, func, time);
        },
        warn: function (msg, func, time) {
            _showMsg(5, msg, func, time);
        },
        tips: function (msg, func, time) {
            _showMsg(6, msg, func, time);
        }
    };

    var _showAlert = function(type, msg, func) {
        layer.alert(msg || '&nbsp;', {
            icon: type,
            zIndex: layer.zIndex
        }, function(index){
            func && func();
            layer.close(index);
        });
    };

    var _alert = {
        ok: function (msg, func) {
            _showAlert(1, msg, func);
        },
        fail: function (msg, func) {
            _showAlert(2, msg, func);
        },
        ask: function (msg, func) {
            _showAlert(3, msg, func);
        },
        lock: function (msg, func) {
            _showAlert(4, msg, func);
        },
        warn: function (msg, func) {
            _showAlert(5, msg, func);
        },
        tips: function (msg, func) {
            _showAlert(6, msg, func);
        }
    };

    C.register('msg', _msg);
    C.register('alert', _alert);

    function _dialog(options) {
        var _default = {
            type: 1,
            area: ['40%', '600px'],
            content: '',
            zIndex: layer.zIndex,
            success: function(layero, index){
                console.log(layero, index);
            },
            yes: function(index, layero){
                //do something
                layer.close(index); //如果设定了yes回调，需进行手工关闭
            }//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
        };

        var _options = $.extend({}, _default, options);

        return layer.open(_options);
    }

    C.register('dialog', _dialog);

    function _confirm( msg, okFunc, cancelFunc, title) {
        layer.confirm(msg, {icon: 3, title:title || '提示',zIndex: layer.zIndex}, function(index){
            okFunc && okFunc();
            layer.close(index);
        },function(index) {
            cancelFunc && cancelFunc();
            layer.close(index);
        });
    }

    C.register('confirm', _confirm);


    function _load( msg, okFunc, cancelFunc) {
        return layer.load(1, {
            zIndex: layer.zIndex,
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
    }

    C.register('load', _load);

    return C;
});