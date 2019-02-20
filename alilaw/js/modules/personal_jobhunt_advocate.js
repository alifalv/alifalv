/*!
 * 求职招聘(律师)
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');
    utils.checkUserType(constants.userType.advocate);

    require('validator');
    require('laydate');


    var loginUserInfo = utils.getLoginUserInfo(true);
    var userId = loginUserInfo.userId;

    var Page = require('page');

    //我收藏的岗位
    var $collectionJobList = $('#collectionJobList');
    var collectionJobListPage = new Page($collectionJobList, {
        pageType: 'home',
        url: 'user/collectionJobList/{userId}/{pageNo}/{pageSize}',
        urlParams: {
            userId: userId
        },
        pageSize: 5
    });

    //删除收藏岗位
    $collectionJobList.on('click', '.j-delete-btn', function() {
        var collectionId = $(this).data('collectionid');
        C.confirm('确认删除？', function() {

            //129.用户删除收藏的岗位
            rest.post({
                url: 'user/deleteCollectionJobList/{collectionId}',
                urlParams: {
                    collectionId: collectionId
                },
                success: function(result) {
                    C.msg.ok(result.msg);
                    collectionJobListPage.refresh();
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    });

    // dataList.getExperienceList(function(result) {
    //     C.template('#experienceListTmpl').renderReplace(result, function() {});
    // });
    //
    // dataList.getEducationList(function(result) {
    //     C.template('#educationListTmpl').renderReplace(result, function() {});
    // });
    //
    // dataList.getSalList(function(result) {
    //     C.template('#salListTmpl').renderReplace(result, function() {});
    // });

    //新键简历
    $('#addresume_btn').on('click', function() {
        var addResumeDialog = C.dialog({
            title: '新键简历',
            area: ['800px', '600px'],
            content: $('#addResumeDialog')
        });
    });

    var $formOpen = $(document).find('#form');
    var form_valid = $formOpen.validator(constants.validator);

    //注册页面
    form_valid.on('valid.form', function(e, form) {
        //个人咨询开通会员
        rest.post({
            url: 'user/sendJob',
            form: $formOpen,
            data: {
                userId: userId,
                userType: 1 //后台发布，userType 传0过来 ，PC端传非0
            },
            success: function(result) {
                location.reload();
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
    });

    //我的简历-列表
    var $myJobList = $('#myJobList');
    var myJobListPage = new Page($myJobList, {
        hasPage: false,
        url: 'user/getResumeInfo/{userId}', //127.用户投递过的企业列表
        urlParams: {
            userId: loginUserInfo.userId
        },
        htmlLoad: function($html, tmpl, data) {
            if (data.data) {
                $('#addresume_btn').addClass('ui-hide');
            } else {
                $('#addresume_btn').removeClass('ui-hide');
            }

        }
    });


    // //我的简历-列表
    // function getResumeInfo() {
    //
    //     rest.post({
    //         url: 'user/getResumeInfo/{userId}',
    //         urlParams : {
    //             userId: loginUserInfo.userId
    //         },
    //         success: function(result) {
    //             if(result.data) {
    //                 $('#addresume_btn').hide();
    //             } else {
    //                 $('#addresume_btn').show();
    //             }
    //             C.template('#myJobListTmpl').renderReplace(result, function() {
    //             });
    //         },
    //         fail : function (result) {
    //             console.log(result.msg);
    //             // C.msg.fail(result.msg);
    //         }
    //     });
    // }
    // getResumeInfo();


    //我的简历-删除
    $myJobList.on('click', '.j-delete-btn', function() {
        var loginUserInfo = utils.getLoginUserInfo(true);

        C.confirm('确认删除该简历？', function() {
            rest.post({
                url: 'user/deleteResumeInfo',
                data: {
                    userId: loginUserInfo.userId
                },
                success: function(result) {
                    C.msg.ok(result.msg);
                    // getResumeInfo();
                    myJobListPage.refresh();
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    });

    //投递过的企业
    var $userSendAdvertiseList = $('#userSendAdvertiseList');
    var userSendAdvertiseListPage = new Page($userSendAdvertiseList, {
        pageType: 'home',
        url: 'user/userSendAdvertise/{userId}/{pageNo}/{pageSize}', //127.用户投递过的企业列表
        urlParams: {
            userId: userId
        },
        pageSize: 5
    });

    //删除投递过的企业
    $userSendAdvertiseList.on('click', '.j-delete-btn', function() {
        var id = $(this).data('id');
        C.confirm('确认删除？', function() {

            //128.用户删除投递过的岗位
            rest.post({
                url: 'user/userDelSendAdvertise',
                data: {
                    Id: id
                },
                success: function(result) {
                    C.msg.ok(result.msg);
                    userSendAdvertiseListPage.refresh();
                },
                fail: function(result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    });
});