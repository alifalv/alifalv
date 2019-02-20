/*!
 * 企业信息
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var Page = require('page');
    var companyUserId = urlParams.userId;//公司userID
    var companyId;
    rest.post({
        url: 'user/getCompanyInfo/{userId}',
        urlParams : {
            userId  : companyUserId
        },
        success: function (result) {
            companyId = result.data.companyId;
            C.template('#companyInfoTmpl').renderReplace(result, function($html) {

            });
        },error : function (result) {
            C.msg.fail(result.msg);
        }
    });

    var loginUserId = -1;
    var loginUserInfo = utils.getLoginUserInfo();
    if(loginUserInfo) {
        loginUserId = loginUserInfo.userId;
    }

    var $newJobList = $('#newJobList');
    var newJobListPage = new Page($newJobList, {
        pageType : 'home',
        url : 'user/newJobList/{pageNo}/{pageSize}',
        data : {
            state : 1,
            userId: companyUserId,
            loginUserId : loginUserId
        },
        addData : {
            isHide : loginUserId == urlParams.userId
        },
        pageSize : 8
    });

    //用户收藏岗位
    $newJobList.on('click', '.j-starjob-btn', function() {

        var loginUserInfo = utils.getLoginUserInfo(true);
        var _$this = $(this);
        var collectionId = _$this.data('collectionid');


        if(collectionId != '0') {
            //取消收藏
            rest.post({
                url: 'user/deleteCollectionJobList/{collectionId}',
                urlParams : {
                    collectionId  : collectionId
                },
                success: function (result) {
                    newJobListPage.refresh();
                    C.msg.tips('已取消收藏');
                },
                fail : function (result) {
                    C.msg.fail(result.msg);
                }
            });

        } else {
            //收藏
            var advertiseId = _$this.data('advertiseid');
            if(advertiseId == null || advertiseId == '') {
                C.msg.fail('接口查询advertiseId失败');
                return false;
            }
            rest.post({
                url: 'user/collectionJob',
                data : {
                    userId : loginUserInfo.userId,
                    advertiseId  : advertiseId
                },
                success: function (result) {
                    newJobListPage.refresh();
                    _$this.addClass('active');
                    C.msg.tips(result.msg || '收藏成功');
                },
                fail : function (result) {
                    C.msg.fail(result.msg);
                }
            });
        }
        return false;
    });


    /**
     * 88.	用户投递简历；9.19修改
     * @param userId
     * @param advertiseId
     * @param companyId
     * @param resumeId
     */
    function deliverResume(userId, advertiseId, companyId, resumeId) {
        rest.post({
            url: 'user/deliverResume',//用户投递简历
            data: {
                userId: userId,
                advertiseId: advertiseId,
                companyId : companyId,
                resumeId : resumeId
            },
            success: function (result) {
                C.msg.tips(result.msg);
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });

    }


    //申请职位
    $newJobList.on('click', '.j-applyjob-btn', function() {
        var loginUserInfo = utils.getLoginUserInfo(true);
        if(null == companyId) {
            C.msg.fail('未查询到该岗位所属公司编号');
            return false;
        }
        var advertiseId = $(this).data('advertiseid');
        if(advertiseId == null || advertiseId == '') {
            C.msg.fail('接口查询advertiseId失败');
            return false;
        }
        C.confirm('确认申请该职位？', function () {
            rest.post({
                url: 'user/getResumeInfo/{userId}',//用户投递简历
                urlParams: {
                    userId: loginUserInfo.userId
                },
                success: function (result) {
                    if(result.data && result.data.resumeId) {
                        deliverResume(loginUserInfo.userId, advertiseId, companyId, result.data.resumeId);
                    } else {
                        C.msg.tips('请先添加个人简历');
                    }
                },
                fail: function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });
        return false;
    });

});
