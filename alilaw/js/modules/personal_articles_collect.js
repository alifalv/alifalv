/*!
 * 我收藏的文章
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
    var $listCollectionJoinArticleList = $('#listCollectionJoinArticleList');
    var listCollectionJoinArticleListPage = new Page($listCollectionJoinArticleList, {
        pageType : 'home',
        url : 'article/listCollectionJoinArticle/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            userId: userId
        },
        pageSize : 8
    });

    //删除文章收藏
    $listCollectionJoinArticleList.on('click', '.j-delete-btn', function () {
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var _articleId = $(this).data('articleid');
        if(null == _articleId || '' == _articleId) {
            C.msg.fail('文章编号不能为空');
            return false;
        }
        C.confirm('删除收藏？',function () {
            rest.post({
                url: 'article/saveArticleCollection/{userId}/{articleId}',
                urlParams : {
                    userId : _loginUserInfo.userId,
                    articleId: _articleId
                },
                data : {
                    nickName : _loginUserInfo.nickName
                },
                success: function(result) {
                    C.msg.ok(result.msg);
                    listCollectionJoinArticleListPage.refresh();
                },
                fail : function (result) {
                    listCollectionJoinArticleListPage.refresh();
                    C.msg.fail(result.msg);
                }
            });
        });
        return false;
    })

});
