/*!
 * 案件委托列表
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');

    var userInfo = utils.getLoginUserInfo(true);
    var userId = userInfo.userId;

    var Page = require('page');
    var myCaseListPage = new Page($('#myCaseList'), {
        pageType : 'home',
        url : 'case/myCaseList/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            userId: userId
        },
        pageSize : 5
    });


    //删除
    $('#myCaseList').on('click', '.j-delete-btn', function() {
        var loginUserInfo = utils.getLoginUserInfo(true);
        var _caseId = $(this).data('caseid');
        C.confirm('确认删除？',function () {
            rest.post({
                url: 'case/deleteCase/{caseId}',
                urlParams : {
                    caseId: _caseId
                },
                success: function(result) {
                    if(result.data.ok) {
                        C.msg.ok(result.msg);
                        myCaseListPage.refresh();
                    }else {
                        C.msg.fail(result.msg);
                    }
                },
                fail : function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    });



});
