/*!
 * 消息详情
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var loginUserInfo = utils.getLoginUserInfo(true);

    //标记已读
    rest.post({
        url: 'user/updateMessage',
        data : {
            userId: loginUserInfo.userId,
            messageId : urlParams.messageId
        },
        success: function(result) {
        },
        fail : function (result) {
        }
    });

    //获取消息详情
    rest.post({
        url: 'user/messageDetailsSearch',
        data : {
            messageId : urlParams.messageId
        },
        success: function(result) {
            C.template('#messageInfoTmpl').renderReplace(result, function() {
            });
        },
        fail : function (result) {
            C.msg.fail(result.msg);
        }
    });

    function deleteMsg(userId,messageIds) {
        rest.post({
            url: 'user/deleteMessage?userId={userId}' + messageIds,
            urlParams : {
                userId: userId
            },
            success: function(result) {
                C.msg.ok(result.msg);
                msgCountRefresh();
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    }


    //删除
    $('#messageInfo').on('click', '.j-delete-btn', function() {
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var _messageId = $(this).data('messageid');
        var _messageIds = '';
        _messageIds += ('&messageIds=' +  _messageId);
        C.confirm('确认删除？',function () {
            deleteMsg(_loginUserInfo.userId, _messageIds);
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
