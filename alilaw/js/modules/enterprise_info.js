/*!
 * 企业查询详情页
 * @author dujian
 * @date 2018-12-26 10:30
 * @version 1.0
 */
define(function(require, exports, module) {
    $("#container").load('enterorise_basicInfo.html');
    // 基础信息 法律诉讼……切换
    $("#cut li").mouseenter(function() {
        $(this).siblings('li').removeClass('currer'); // 删除其他兄弟元素的样式
        $(this).addClass('currer'); // 添加当前元素的样式
        // alert($(this).text())
        // 基础信息
        if ($(this).text() == '基础信息') {
            $("#basicInfo").show();
            $("#container").load('enterorise_basicInfo.html');
        } else {
            $("#basicInfo").hide();
        }
        // 法律诉讼
        if ($(this).text() == '法律诉讼') {
            $("#legalAction").show();
            $("#container").load('enterorise_legalAction.html');
        } else {
            $("#legalAction").hide();
        }
        // 经营风险
        if ($(this).text() == '经营风险') {
            $("#businessRisk").show();
            $("#container").load('enterorise_businessRisk.html');
        } else {
            $("#businessRisk").hide();
        }
        // 历史信息
        if ($(this).text() == '历史信息') {
            $("#historicalInfo").show();
        } else {
            $("#historicalInfo").hide();
        }
    });


});