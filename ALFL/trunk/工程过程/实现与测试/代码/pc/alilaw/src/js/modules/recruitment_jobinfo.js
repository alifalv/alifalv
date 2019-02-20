/*!
 * 求职招聘(咨询师)
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var companyId = null;
    //不存在岗位编号
    var advertiseId = urlParams.advertiseId;
    if(null == advertiseId || '' == advertiseId) {
        utils.jump404('岗位编号为空');
        return false;
    }

    //岗位详情
    rest.post({
        url: 'user/seacherJobDetail',
        data : {
            advertiseId  : advertiseId
        },
        success: function (result) {
            companyId = result.data.companyId;
            C.template('#jobInfoTmpl').renderReplace(result, function($html) {

                var starId = false;
                var _userId = -1;//判断收藏的编号
                var _loginUserInfo = utils.getLoginUserInfo();
                if(_loginUserInfo) {
                    _userId = _loginUserInfo.userId;
                }
                rest.post({
                    url: 'user/seacherisCollectionJob',
                    data : {
                        userId  : _userId,
                        advertiseId : advertiseId
                    },
                    success: function (result) {
                        starId = result.data.collectionId;
                        if(result.data.collectionId == '0') {//Id=0 为没收藏否则收藏了；
                            $html.find('#star_btn').removeClass('active');
                        } else {
                            $html.find('#star_btn').addClass('active');
                        }
                    }
                });


                //如果是自己发布的岗位就无法收藏和申请
                var loginUserInfo = utils.getLoginUserInfo();
                if((loginUserInfo && loginUserInfo.userId == result.data.userId) || (loginUserInfo && loginUserInfo.userType == '3')) {
                    $html.find('#star_btn').hide();
                    $html.find('#send_btn').hide();
                }
                //用户收藏岗位
                $html.on('click', '#star_btn', function() {
                    var _$this = $(this);
                    var loginUserInfo = utils.getLoginUserInfo(true);

                    if(starId == '0') {///Id=0 为没收藏
                        rest.post({
                            url: 'user/collectionJob',
                            data : {
                                userId : loginUserInfo.userId,
                                advertiseId  : advertiseId
                            },
                            success: function (result) {
                                starId = result.data.collectionId;
                                _$this.addClass('active');
                                C.msg.tips(result.msg || '收藏成功');
                            },
                            fail : function (result) {
                                C.msg.fail(result.msg);
                            }
                        });
                    } else {
                        rest.post({
                            url: 'user/deleteCollectionJobList/{collectionId}',
                            urlParams : {
                                collectionId  : starId
                            },
                            success: function (result) {
                                starId = 0;
                                _$this.removeClass('active');
                                C.msg.tips('已取消收藏');
                            },
                            fail : function (result) {
                                C.msg.fail(result.msg);
                            }
                        });
                    }

                    return false;
                });


                //举报
                $html.on('click', '#jubao_btn', function() {
                    var _$this = $(this);
                    var loginUserInfo = utils.getLoginUserInfo(true);
                    C.confirm('确认举报该职位？',function () {
                        rest.post({
                            url: 'user/complainJob',
                            data: {
                                reportTitle : result.data.jobName,
                                advertiseId: advertiseId,
                                userId : loginUserInfo.userId,
                                userName : loginUserInfo.userName,
                                mobile : loginUserInfo.mobile
                            },
                            success: function (result) {
                                C.msg.ok(result.msg);
                            },
                            fail: function (result) {
                                C.msg.fail(result.msg);
                            }
                        });
                    });
                });
            });
        }
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
    $(document).on('click', '#send_btn', function() {
        var loginUserInfo = utils.getLoginUserInfo(true);
        if(null == companyId) {
            C.msg.fail('接口未查询到该岗位所属公司编号');
            return false;
        }
        C.confirm('确认申请该职位？',function () {

            rest.post({
                url: 'user/seacherisResumeJob',//是否投递过简历
                data: {
                    userId: loginUserInfo.userId,
                    advertiseId : advertiseId
                },
                success: function (result) {
                    if(result.data && result.data.id == '0') {//0未投递
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
                    } else {
                        C.msg.tips('您已经投递过简历了');
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
