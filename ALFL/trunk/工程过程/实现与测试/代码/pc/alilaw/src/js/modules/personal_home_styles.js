/*!
 * 账户设置
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');
    utils.checkUserType(constants.userType.counselor);

    var loginUserInfo = utils.getLoginUserInfo(true);
    var loginUserId = loginUserInfo.userId;
    require('validator');

    var Page = require('page');
    var $myMienList = $('#myMienList');
    var myMienListPage = new Page($myMienList, {
        pageType : 'home',
        url : 'article/myMien/{model}/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            model:1,
            userId : loginUserId
        },
        pageSize : 6,
        htmlLoad : function($html){
            require('viewer');
            var viewer = new Viewer(document.getElementById('myMienList'));
        }
    });


    //删除
    $myMienList.on('click', '.j-delete-btn', function() {
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
                        myMienListPage.refresh();
                    }else {
                        C.msg.fail(result.msg);
                    }
                },
                fail : function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });
        return false;
    });

});
