/*!
 * 消息
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');

    var Page = require('page');
    var loginUserInfo = utils.getLoginUserInfo(true);

    var $msgList = $('#msgList');

    //左侧在线咨询(咨询列表)
    var msgListPage = new Page($msgList, {
        url : 'user/messageSearch/{pageNo}/{pageSize}',
        data: {
            userId: loginUserInfo.userId//用户ID
        },
        pageSize : 10
    });

    function deleteMsg(userId,messageIds) {
        rest.get({
            url: 'user/deleteMessage?userId={userId}' + messageIds,
            urlParams : {
                userId: userId
            },
            success: function(result) {
                C.msg.ok(result.msg);
                $('.j-checkallbox').prop('checked', false);
                msgCountRefresh();
                msgListPage.refresh();
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    }

    //阅读消息
    $msgList.on('click', '.j-read-btn', function() {
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var _messageId = $(this).data('messageid');
        utils.windowOpen(constants.viewUrl + 'personal_messageinfo.html?messageId=' +  _messageId);
        // window.open(constants.viewUrl + 'personal_messageinfo.html?messageId=' +  _messageId);
        $(this).parent().find('.msg-tips.unread').removeClass('unread');
        return false;
    });

    //删除
    $msgList.on('click', '.j-delete-btn', function() {
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var _messageId = $(this).data('messageid');
        var _messageIds = '';
        _messageIds += ('&messageIds=' +  _messageId);

        C.confirm('确认删除？',function () {
            deleteMsg(_loginUserInfo.userId, _messageIds);
        });
        return false;
    });

    //删除选中
    $msgList.on('click', '.j-deleteall-btn', function() {
        var _loginUserInfo = utils.getLoginUserInfo(true);

        var _messageIds = '';
        $msgList.find('.j-checkbox:checked').each(function(i) {
            _messageIds += ('&messageIds=' +  $(this).val());
        });
        if(_messageIds == '') {
            C.msg.tips('请先选择要删除的信息');
            return false;
        }

        C.confirm('确认删除？',function () {
            deleteMsg(_loginUserInfo.userId, _messageIds);
        });
        return false;
    });

    $msgList.on('change', '.j-checkallbox', function() {
        var _$this = $(this);
        $msgList.find('.j-checkbox').each(function(i) {
            $(this).prop('checked', _$this.prop('checked'));
        });
        return false;
    });

    //全部已读
    $msgList.on('click', '.j-readall-btn', function() {
        var _loginUserInfo = utils.getLoginUserInfo(true);
        rest.post({
            url: 'user/updateMessage',
            data : {
                userId: _loginUserInfo.userId
            },
            success: function(result) {
                C.msg.ok(result.msg);
                msgCountRefresh();
                msgListPage.refresh();
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
        return false;
    });


    function messageTypeCount(businessType, userId, fuc) {
        rest.post({
            url: 'user/messageTypeCount',
            data : {
                businessType:businessType,// 消息类型 1：系统消息 0： 回复消息 空：所有消息
                userId: userId  //用户ID
            },
            success: function(result) {
                fuc && fuc(result);
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    }


    function msgCountRefresh() {
        messageTypeCount('', loginUserInfo.userId, function(result) {
            $('#allmsgcount').html('（' + result.data.msgCount + '）');
        });

        messageTypeCount(1, loginUserInfo.userId, function(result) {
            $('#sysmsgcount').html('（' + result.data.msgCount + '）');
        });

        messageTypeCount(0, loginUserInfo.userId, function(result) {
            $('#replaymsgcount').html('（' + result.data.msgCount + '）');
        });
    }
    msgCountRefresh();
});
