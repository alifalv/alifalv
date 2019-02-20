/*!
 * 简历详情
 * @author Li Hongwei
 * @date 2018-10-12 14:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();

    //不存在文章编号
    var userId = urlParams.userId;
    if (null == userId || '' == userId) {
        utils.jump404('用户编号为空');
        return false;
    }

    var constants = require('constants');
    require('validator');


    var loginUserInfo = utils.getLoginUserInfo();

    var isStared = true; //是否已收藏
    var collectionId = '0'; //0未收藏

    /**
     * 查询是否收藏简历
     * @param resumeId
     */
    function seacherisCollectionResume(resumeId, $html, func) {
        var _loginUserInfo = utils.getLoginUserInfo();
        var userId = '-1';
        if (_loginUserInfo) {
            userId = _loginUserInfo.userId;
        }
        rest.post({
            url: 'user/seacherisCollectionResume',
            data: {
                userId: userId, //用户编号  （如果是是游客你自己生成一个userid）不能不传，
                resumeId: resumeId //简历ID
            },
            success: function(result) {
                if (result.data && result.data.collectionId != '0') {
                    isStared = true;
                    collectionId = result.data.collectionId;
                    $html.find('.j-star-btn').find('i').addClass('ui-color-blue3');
                } else {
                    isStared = false;
                    $html.find('.j-star-btn').find('i').removeClass('ui-color-blue3');
                }
                func && func();
            }
        });
    }



    rest.post({
        url: 'user/getResumeInfo/{userId}',
        urlParams: {
            userId: userId
        },
        success: function(result) {
            if (loginUserInfo) {
                result.isLogining = true;
            }
            C.template('#resumeInfoTmpl').renderReplace(result, function($html) {
                var loginUserInfo = utils.getLoginUserInfo();

                //自己 && 普通用户 无法收藏
                if ((loginUserInfo && loginUserInfo.userId == result.data.userId) || (loginUserInfo && loginUserInfo.userType == '1')) {
                    $html.find('.j-star-btn').hide();

                } else {

                    seacherisCollectionResume(result.data.resumeId, $html, function() {

                        $html.on('click', '.j-star-btn', function() {
                            var loginUserInfo = utils.getLoginUserInfo(true);
                            var _$this = $(this);
                            if (isStared) {
                                deleteCollectionResumeList(function() {
                                    seacherisCollectionResume(result.data.resumeId, $html);

                                    C.msg.ok('取消收藏');
                                    _$this.find('i').removeClass('ui-color-blue3');
                                });
                            } else {

                                var resumeId = _$this.data('resumeid');
                                collectionResume(loginUserInfo.userId, resumeId, function() {
                                    seacherisCollectionResume(result.data.resumeId, $html);

                                    C.msg.ok('收藏成功');
                                    _$this.find('i').addClass('ui-color-blue3');
                                });
                            }
                            return false;
                        });
                    });


                }

                $('#loginjump_btn').on('click', function() {
                    utils.jumpLogin(true);
                })
            });
        },
        fail: function(result) {
            C.msg.fail(result.msg);
        }
    });

    /**
     * 134.企业删除收藏的简历；10.15修改
     * @param func
     */
    function deleteCollectionResumeList(func) {
        rest.post({
            url: 'user/deleteCollectionResumeList', //134.企业删除收藏的简历
            data: {
                collectionId: collectionId
            },
            success: function(result) {
                func && func(result);
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    }

    /**
     * 122.企业收藏简历；9.21
     * @param userId
     * @param resumeId
     * @param func
     */
    function collectionResume(userId, resumeId, func) {
        rest.post({
            url: 'user/collectionResume',
            data: {
                userId: userId, // 律师id（不可空）
                resumeId: resumeId //简历ID（不可空）
            },
            success: function(result) {
                func && func(result);
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    }


});