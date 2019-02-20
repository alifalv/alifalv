/*!
 * 我的随笔详情
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    var utils = require('utils');
    utils.checkUserType(constants.userType.counselor);


    var urlParams = utils.getUrlParams();
    var userId = urlParams.userId;

    var loginUserInfo = utils.getLoginUserInfo(true);
    var Page = require('page');
    var mySuccessListPage = new Page($('#allList'), {
        pageType : 'home',
        url : 'article/listArticle/{model}/{pageNo}/{pageSize}',
        urlParams : {
            model:1
        },
        data : {
            userId : loginUserInfo.userId,
            homepage : 'A'
        },
        pageSize : 3,
        htmlLoad : function($html){
            require('viewer');
            var viewer = new Viewer(document.getElementById('allList'));
        }
    });

    //删除
    $('#allList').on('click', '.j-delete-btn', function() {
        var loginUserInfo = utils.getLoginUserInfo(true);
        var _articleId = $(this).data('articleid');
        C.confirm('确认删除？',function () {
            rest.post({
                url: 'article/deleteArticle/{articleId}',
                urlParams : {
                    articleId: _articleId
                },
                success: function(result) {
                    if(result.data.ok) {
                        C.msg.ok(result.msg);
                        mySuccessListPage.refresh();
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
