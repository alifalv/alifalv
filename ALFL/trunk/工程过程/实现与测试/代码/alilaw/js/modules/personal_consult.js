/*!
 * 我的咨询列表
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */

define(function(require, exports, module) {
    "use strict";
    var utils = require('utils');
    var userInfo = utils.getLoginUserInfo(true);
    var Page = require('page');

    //我的咨询列表
    var myAdviceListPage = new Page($('#myAdviceList'), {
        pageType: 'home',
        url: 'advice/myAdviceList/{userId}/{pageNo}/{pageSize}',
        urlParams: {
            userId: userInfo.userId
        },
        pageSize: 5
    });

    //删除咨询
    $('#myAdviceList').on('click', '.j-delete-btn', function() {
        var adviceId = $(this).data('adviceid');
        C.confirm('确认删除？', function() {
            rest.post({
                url: 'advice/deleteAdvice/{adviceId}',
                urlParams: {
                    adviceId: adviceId
                },
                success: function(result) {
                    if (result.data.ok) {
                        C.msg.ok(result.msg);
                        myAdviceListPage.refresh();
                    } else {
                        C.msg.fail(result.msg);
                    }
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    });
});