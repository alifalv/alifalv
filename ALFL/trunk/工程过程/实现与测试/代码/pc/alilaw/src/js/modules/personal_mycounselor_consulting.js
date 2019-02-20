/*!
 * 我咨询的咨询师
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

    new Page($('#myAdviceConsultantList'), {
        pageType : 'home',
        url : 'user/myAdviceConsultant/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            userId: userId
        },
        pageSize : 8,
        htmlLoad : function($html, tmpl, data){

            //关注与取消关注按钮事件
            $html.on('click', '.j-followoper-btn', function () {
                var $follow_box = $(this).closest('.j-follow-box');
                var counselorId = $follow_box.data('userid');
                var isStar = $follow_box.data('isstar');
                var followId = $follow_box.data('followid');//关注编号

                //关注或取关
                function operation() {
                    rest.post({
                        url: 'user/insertAliFollow',
                        data : {
                            userId: userId,
                            counselorId : counselorId,
                            type : isStar,//类型 1：关注 0已经关注，要进行取消操作
                            followId: followId//关注id 取消关注才传
                        },
                        success: function(result) {
                            if(isStar == '1') {
                                setFollowId($follow_box, result.data.followId);
                                unFollowBtnShow($follow_box);
                                C.msg.ok('关注成功');

                            } else {
                                followBtnShow($follow_box);
                                C.msg.ok('取消成功');
                            }
                        },
                        fail : function (result) {
                            C.msg.fail(result.msg);
                        }
                    });
                }

                //已经关注了就要查询关注编号
                if(isStar == '0') {//0已经关注，要进行取消操作

                    rest.post({
                        url: 'user/isAliFollow',
                        data : {
                            userId: userId, //用户编号
                            counselorId: counselorId//咨询师ID
                        },
                        success: function (result) {
                            if(result.data.followId != '' && result.data.followId != '0') {
                                setFollowId($follow_box, result.data.followId);
                                unFollowBtnShow($follow_box)
                            } else {
                                followBtnShow($follow_box)
                            }

                            //查询完后在执行
                            operation();
                        },
                        error : function () {
                            C.msg.fail('收藏状态加载失败,请刷新页面');
                        }
                    });
                } else {
                    operation();
                }

                return false;
            });

            function setFollowId($follow_box, followId) {
                $follow_box.data('followid', followId);
            }

            function unFollowBtnShow($follow_box, followId) {
                $follow_box.data('isstar', 0);
                $follow_box.find('.j-follow-btn').addClass('ui-hide');
                $follow_box.find('.j-unfollow-btn').removeClass('ui-hide');
            }

            function followBtnShow($follow_box) {
                $follow_box.data('isstar', 1);
                $follow_box.find('.j-follow-btn').removeClass('ui-hide');
                $follow_box.find('.j-unfollow-btn').addClass('ui-hide');
            }

            $html.find('.j-headimg').one('error',function () {
                $(this).attr('src', '../img/head-defalut.png');
            });

            //初始化关注状态
            for(var i = 0; i < data.rows.length; i++) {
                (function (i) {
                    var counselorId = data.rows[i].userId;
                    rest.post({
                        url: 'user/isAliFollow',
                        data : {
                            userId: userId,//用户编号
                            counselorId : counselorId //咨询师ID
                        },
                        success: function(result) {
                            if(result.data.followId != '' && result.data.followId != '0') {
                                setFollowId($html.find('[data-userid="' + counselorId + '"]'), result.data.followId);
                                unFollowBtnShow($html.find('[data-userid="' + counselorId + '"]'));

                            } else {
                                followBtnShow($html.find('[data-userid="' + counselorId + '"]'));
                            }

                        },
                        fail : function (result) {
                            C.msg.fail(result.msg);
                        }
                    });
                })(i);
            }
        }
    });

});
