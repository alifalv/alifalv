/*!
 * template 函数扩展
 * @author Li Hongwei
 * @date 2018-08-05 11:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var template = require('template');
    var constants = require('constants');
    var utils = require('utils');

    //开启模板debug
    template.defaults.debug = constants.debug;

    window.template = template;

    function createImgUrl(imgName) {
        return utils.createImgUrl(imgName);
    }

    /**
     * 生成图片地址
     * @param imgName 图片名称
     * @return {string} 图片地址
     */
    window.template.defaults.imports.createImgUrl = function(imgName) {
        return createImgUrl(imgName);
    };

    /**
     * 去掉所有的html标签
     * @param text
     * @return {XML|string|void|*}
     */
    window.template.defaults.imports.removeHtmlTag = function(text) {
        var temp = document.createElement("div");
        temp.innerHTML = text;
        var output = temp.innerText || temp.textContent;
        temp = null;
        return output.replace(/&nbsp;/g, '').replace(/<[^>]+>/g, '');
    };

    /**
     * 生成文件地址
     * @param fileName 文件名称
     * @return {string} 文件地址
     */
    window.template.defaults.imports.createFileUrl = function(fileName) {
        return createImgUrl(fileName);
    };

    /**
     * 长度获取
     * @param value
     * @return {number}
     */
    window.template.defaults.imports.getLength = function(value) {
        if(value) {
            return value.length;
        } else {
            return 0;
        }
    };

    /**
     * json序列化
     * @param value
     */
    window.template.defaults.imports.toJSON = function(value) {
        return JSON.stringify(value);
    };

    /**
     * 日期转距离天数、小时数
     * @param str
     * @return {*}
     */
    window.template.defaults.imports.dateDiff = function(str) {
        var dayTime = 1000 * 60 * 60 * 24;
        var time = utils.formatTime(str);
        var nowTime = (new Date()).getTime();

        //在3天内的才转换
        if(nowTime - time <= dayTime * 3) {
            return utils.getDateDiff(time);
        }
        return str;
    };

    //只显示日期
    window.template.defaults.imports.subDay = function(value) {
        return value.substring(0,10);
    };

    //只显示年
    window.template.defaults.imports.subYear = function(value) {
        return value.substring(0,4);
    };

    //只显示月日
    window.template.defaults.imports.subMonthDay = function(value) {
        return value.substring(5,10);
    };


    /**
     *
     * @param value url
     * @return {string} active url
     */
    window.template.defaults.imports.createMenuUrl = function(value) {
        if(location.href.indexOf(value) > -1) {
            return 'class="active" href="' + value + '"';
        } else {
            return 'href="' + value + '"';
        }
    };

    /**
     *
     * @param value url
     * @return {string} active url
     */
    window.template.defaults.imports.createHomeHeadUrl = function(value) {
        if(location.href.indexOf(value) > -1) {
            return 'class="active"';
        } else {
            return '';
        }
    };

    /**
     *
     * @param value url
     * @return {string} active url
     */
    window.template.defaults.imports.createActiveTabs = function(url) {
        if(location.href.indexOf(url) > -1) {
            return 'class="active"';
        } else {
            return '';
        }
    };

    //显示详情页图片
    window.template.defaults.imports.createImgs = function(value) {
        var html = '';
        if(value) {
            var splitArr = value.split(',');
            for(var k in splitArr) {
                html += '<li><img imgzoom src="' + createImgUrl(splitArr[k]) + '" alt="" class="ui-docimg ui-bg-gray" style="min-height: 100px"></li>';
            }

        } else {
            html = '无'
        }
        return html;
    };

    /**
     * 订单列表的文章编号和订单编号是分开放的 id or businessOrder
     * 消息详情的文章编号和订单编号是放一起的id
     * @param businessType
     * @param type 类型 order or msg 用于于区别订单列表或消息详情页面的跳转链接
     * @param id
     * @param businessOrder
     * @return {*}
     */
    window.template.defaults.imports.getArticleUrlByBusinessType = function(businessType, type, id, businessOrder) {

        if(businessType == '阿里裁决') {
            return constants.viewUrl + 'referee_info.html?articleId=' + id;

        } else if(businessType == '法律咨询') {
            return constants.viewUrl + 'articles_consultinfo.html?articleId=' + id;

        } else if(businessType == '百姓吐槽') {
            return constants.viewUrl + 'complaininfo.html?articleId=' + id;

        } else if(businessType == '案件委托') {
            return constants.viewUrl + 'casebiddinginfo.html?caseId=' + id;

        } else if(businessType == '协议律师费') {
            return constants.viewUrl + 'user_orderinfo.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

        } else if(businessType == '会员充值') {
            return constants.viewUrl + 'user_orderinfo.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

        } else if(businessType == '法律文书制作') {
            return constants.viewUrl + 'user_orderinfo.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

        } else if(businessType == '法律咨询赏金') {
            return constants.viewUrl + 'user_orderinfo.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

        } else if(businessType == '法律咨询打赏') {
            return constants.viewUrl + 'user_orderinfo.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

        } else if(businessType == '法律文书制作赏金') {
            return constants.viewUrl + 'user_orderinfo.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

        } else if(businessType == '阿里裁决打赏') {
            return constants.viewUrl + 'user_orderinfo.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

        } else if(businessType == '案件委托赏金') {
            return constants.viewUrl + 'user_orderinfo.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

        } else if(businessType == '百姓吐槽打赏') {
            return constants.viewUrl + 'user_orderinfo.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

        } else {
            return 'javascript:void(0);'
        }
    };

    window.template.defaults.imports.getColumnCodeName = function(columnCode) {
        return constants.columnCode[columnCode];
    };


    window.template.defaults.imports.getArticleUrlByColumnCode = function(columnCode, articleId, userId) {
        if(columnCode == '0') {//百姓吐槽
            return constants.viewUrl + 'complaininfo.html?articleId=' + articleId;

        } else if(columnCode == '1') {//法律疑难问题与观点
            return constants.viewUrl + 'articles_probleminfo.html?articleId=' + articleId;

        } else if(columnCode == '2') {//公务员招聘
            return constants.viewUrl + 'civilrecruit.html?articleId=' + articleId;

        } else if(columnCode == '3') {//常用文书模板
            return constants.viewUrl + 'doctemplateinfo.html?articleId=' + articleId;

        } else if(columnCode == '4') {//文书制作知识
            return constants.viewUrl + 'lawdocmarkinfo.html?articleId=' + articleId;

        } else if(columnCode == '5') {//法律人信息
            return constants.viewUrl + 'articles_lawerinfo.html?articleId=' + articleId;

        } else if(columnCode == '6') {//咨询问题
            return constants.viewUrl + 'articles_consultinfo.html?articleId=' + articleId;

        } else if(columnCode == '7') {//法律培训
            return constants.viewUrl + 'lawtraininginfo.html?articleId=' + articleId;

        } else if(columnCode == '8') {//法律法规
            return constants.viewUrl + 'statuteinfo.html?articleId=' + articleId;

        } else if(columnCode == '9') {//阿里裁决
            return constants.viewUrl + 'referee_info.html?articleId=' + articleId;

        } else if(columnCode == '10') {//我的风采
            return constants.viewUrl + 'home_style.html?articleId=' + articleId + '&userId=' + userId;

        } else if(columnCode == '11') {//工作动态
            return constants.viewUrl + 'home_work.html?articleId=' + articleId + '&userId=' + userId;

        } else if(columnCode == '12') {//成功案例
            return constants.viewUrl + 'home_case.html?articleId=' + articleId + '&userId=' + userId;

        } else if(columnCode == '13') {//我的随笔
            return constants.viewUrl + 'home_essay.html?articleId=' + articleId + '&userId=' + userId;

        } else if(columnCode == '14') {//法律咨询
            return constants.viewUrl + 'articles_consultinfo.html?articleId=' + articleId;

        } else if(columnCode == '15') {//案件委托
            return constants.viewUrl + 'casebiddinginfo.html?caseId=' + articleId;

        } else {
            return '文章类型错误';
        }
    };

    /**
     * 搜索高亮
     * @param text 文本
     * @param key 关键字
     * @return {*}
     */
    window.template.defaults.imports.searchLightKey = function(text, key) {
        if(null == text || '' == text) {
            return '无'
        }
        if(null == key || '' == key) {
            return text;
        }
        var a = new RegExp(key, 'g');
        // '/' + key + '/g'
        var content  = text.replace(a, '<span class="ui-color-red">' + key + '</span>');
        return content;
    };


    /**
     * 去掉路径
     * @param text 文本
     * @return {*}
     */
    window.template.defaults.imports.ridPath = function(text) {
        var flags = text.split('/');
        text = flags[flags.length-1];
        return text;
    };


    /**
     * 根据二级域名生成主页地址
     * @param personUrl 二级域名
     * @return {string}
     */
    window.template.defaults.imports.createHomeUrl = function(personUrl) {
        if(personUrl) {
            return '<a class="ui-color-white" href="http://' + personUrl + constants.home_sld +'">http://' + personUrl + constants.home_sld + '</a>'
        } else {
            return '<a class="ui-color-blue3" href="' + constants.viewUrl + 'personal_homesetting.html">立即设置</a>'
        }
    };
});
