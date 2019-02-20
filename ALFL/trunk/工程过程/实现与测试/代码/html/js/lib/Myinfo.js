var vm = new Vue({
	el:'#app',
	data:{
		userInfo:null,
		vipList: ['黄金', '白金', '钻石'],
        msgCount : 0
	},
	mounted: function(){
		
		var that = this;
		var loginUserInfo = utils.getLoginUserInfo(true);

        rest.post({
            url: 'user/messageTypeCount',
            data : {
                businessType:'',// 消息类型 1：系统消息 0： 回复消息 空：所有消息
                isRead : 0,//0 ：未读 1 ：已读 ，不传 ：全部；
                userId: loginUserInfo.userId  //用户ID
            },
            success: function(result) {
                if(result.data.msgCount && result.data.msgCount > 0) {
                    that.msgCount = result.data.msgCount;
                }
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });

        rest.post({
            url: 'user/userInfo/{userId}',
            urlParams : {
                userId: loginUserInfo.userId
            },
            success: function (result) {
                that.userInfo = result.data;


                rest.post({
                    url: 'systemConfig/levelList',
                    urlParams : {
                        userId: that.userName
                    },
                    success: function (result) {
                        that.vipList = [];
                        for (var i in result.data) {
                            that.vipList.push(result.data[i].levelName);
                        }
                    },
                    fail: function (result) {
                        mui.toast('获取会员等级列表失败')
                    }
                });
            },
            fail: function (result) {
            }
        });
	},

	methods:{
        setErrorImg : function(e) {
            $(e.target).attr('src', 'images/index/idx-defaluthead.png');
        },

		goMyInfoEdit:function () {
			creatMywebview('MyInfoEdit.html', 'MyInfoEdit.html');
		}
	}
	
});



