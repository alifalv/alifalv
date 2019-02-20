/*!
 * 文书制作详情
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function (require, exports, module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();

    //不存在文章编号
    var articleId = urlParams.articleId;
    if(null == articleId || '' == articleId) {
        utils.jump404('文章编号为空');
        return false;
    }


    var loginUserId = -1;
    var loginUserInfo = utils.getLoginUserInfo();
    if(loginUserInfo) {
        loginUserId = loginUserInfo.userId;
    }

    function initShareBox() {
        var shareHtml = '<div class="bdsharebuttonbox ui-inline-block"><a href="javascript:void(0);" class="bds_more" data-cmd="more"></a><a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a><a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a><a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a><a href="#" class="bds_renren" data-cmd="renren" title="分享到人人网"></a><a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a></div>\n' +
            '<script>window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"0","bdSize":"24"},"share":{},"image":{"viewList":["qzone","tsina","tqq","renren","weixin"],"viewText":"分享到：","viewSize":"16"},"selectShare":{"bdContainerClass":null,"bdSelectMiniList":["qzone","tsina","tqq","renren","weixin"]}};with(document)0[(getElementsByTagName(\'head\')[0]||body).appendChild(createElement(\'script\')).src=\'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=\'+~(-new Date()/36e5)];</script>'
        $('.j-share-box').append(shareHtml);
    }


    rest.post({
        url: 'article/articleDetails/{userId}/{articleId}',
        urlParams : {
            userId : loginUserId,
            articleId : articleId
        },
        data : {
            columnCode : 4
        },
        success: function (result) {
            C.template('#activeTmpl').renderReplace(result, function($html) {
                initShareBox();
            });
        }
    });


    $('.ui-article').on('click', '#star_btn', function() {
        var _$this = $(this);
        var loginUserInfo = utils.getLoginUserInfo(true);
        rest.post({
            url: 'article/saveArticleCollection/{userId}/{articleId}',
            urlParams : {
                userId : loginUserInfo.userId,
                articleId : articleId
            },
            data : {
                nickName : loginUserInfo.nickName
            },
            success: function(result) {
                if(result.data.ok) {
                    _$this.addClass('active');
                    C.msg.tips(result.msg);
                } else {
                    _$this.removeClass('active');
                    C.msg.warn(result.msg);
                }
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

});
