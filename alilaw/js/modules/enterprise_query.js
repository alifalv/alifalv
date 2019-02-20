/*!
 * 企业查询首页
 * @author dujian
 * @date 2018-12-25 10:45
 * @version 1.0
 */
define(function(require, exports, module) {
    var constants = require('constants');

    // 头部搜索框控制
    // 显示
    $("#querybt").mouseenter(function() {
        $("#iptdiv").fadeIn();
        $("#iptdiv").focus();
        event.stopPropagation(); //阻止冒泡
    });
    // 隐藏
    $('#iptdiv').mouseleave(function() {
        $("#iptdiv").fadeOut();
    });

    //输入框提示语 显示隐藏
    // 隐藏
    $('#ipt').click(function() {
        $("#spts").fadeOut();
    });
    // 显示
    $('#ipt').blur(function() {
        var iptvalue = document.getElementById("ipt").value;
        if (iptvalue.length <= 0) {
            $("#spts").fadeIn();
        }
    });

});