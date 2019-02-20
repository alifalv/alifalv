/*!
 * 侧边栏
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var utils = require('utils');

    //取窗口可视范围的高度 
    function getClientHeight() {
        var clientHeight = 0;
        if (document.body.clientHeight && document.documentElement.clientHeight) {
            var clientHeight = (document.body.clientHeight < document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight;
        } else {
            var clientHeight = (document.body.clientHeight > document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight;
        }
        return clientHeight;
    }

    //是上传页面就不加载侧边
    if (location.href.indexOf('imgUploadTmpl.html') > -1 || location.href.indexOf('qqLogin.html') > -1 || location.href.indexOf('weChatLogin.html') > -1) {
        return false;
    }

    $('body').append('<script class="sidebarRight" type="text/html" src="../tmpl/sidebarRight.html"></script>');
    if (utils.hasClass('.sidebarRight')) {


        C.template('.sidebarRight').renderReplace({ error: '未登录' }, function($html) {


            var $top_back = $('#top_back');
            $top_back.hide();

            //回到顶部按钮显示控制
            $(window).scroll(function() {
                if ($(this).scrollTop() > 350) {
                    $top_back.fadeIn();
                } else {
                    $top_back.fadeOut();
                }

                // 滚动条距离底部的高度
                var sceollBottom = document.body.scrollHeight - getClientHeight() - $(this).scrollTop();


                if (sceollBottom < 200 && document.body.clientWidth < 1300) {
                    $(rightSidebar).css("top", "40%");
                } else {
                    $(rightSidebar).css("top", "70%");
                }
            });

            //回到顶部
            $top_back.on('click', function() {
                $('body,html').animate({ scrollTop: 0 }, 300);
            });

            var loginUserInfo = utils.getLoginUserInfo();

            if (loginUserInfo) {

                rest.post({
                    url: 'user/messageTypeCount',
                    data: {
                        businessType: '', // 消息类型 1：系统消息 0： 回复消息 空：所有消息
                        isRead: 0, //0 ：未读 1 ：已读 ，不传 ：全部；
                        userId: loginUserInfo.userId //用户ID
                    },
                    success: function(result) {

                        if (result.data.msgCount && result.data.msgCount > 0) {
                            $html.find('.j-msg-unread').removeClass('ui-hide'); ////显示未读小红点
                        } else {
                            $html.find('.j-msg-unread').addClass('ui-hide'); ////显示未读小红点
                        }
                    },
                    fail: function(result) {
                        C.msg.fail(result.msg);
                    }
                });

                rest.post({
                    url: 'user/messageSearch/{pageNo}/{pageSize}',
                    urlParams: {
                        pageNo: 1,
                        pageSize: 5
                    },
                    data: {
                        businessType: '', //消息类型 1：系统消息 0： 回复消息
                        userId: loginUserInfo.userId //用户ID
                    },
                    success: function(result) {

                        var html = '';
                        if (result.rows && result.rows.length > 0) {
                            for (var i = 0; i < result.rows.length; i++) {
                                html += ('<li class="ui-margin-t-10"><a href="personal_messageinfo.html?messageId=' + result.rows[i].messageId + '" title="" class="ui-cut-1"><i class="iconfont icon-yuandian ui-color-red ui-margin-r-5"' + (result.rows[i].isRead == 0 ? '' : 'style="visibility: hidden"') + '></i>【' + result.rows[i].messageType + '】' + result.rows[i].messageContent + '<span class="ui-color-yellow ui-margin-l-5">去查看</span></a></li>')
                            }
                        } else {
                            html = '<li class="text-center"><div class="ui-color-gray">暂无记录</div></li>';
                        }
                        $html.find('#sidebarMsg').html(html);
                    },
                    fail: function(result) {
                        var html = '<li class="text-center"><div class="ui-color-gray">' + result.msg + '</div></li>';
                        $html.find('#sidebarMsg').html(html);
                    }
                });
            }
        });
    }


});