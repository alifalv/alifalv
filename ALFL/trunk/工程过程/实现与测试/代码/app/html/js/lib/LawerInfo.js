new Vue({
	el:'#app',
	data:{
		userInfo:{},

		//风采
		loading1: false,//正在加载
		finished1: false,//不在加载
		list1: [],//列表
		pageNo1: 1,//页码
		pageSize1: 9,//每页数量

		//随笔
		loading2: false,
		finished2: false,
		list2: [],
		pageNo2: 1,
		pageSize2: 9,

		//案例
		loading3: false,
		finished3: false,
		list3: [],
		pageNo3: 1,
		pageSize3: 9,

		//动态
		loading4: false,
		finished4: false,
		list4: [],
		pageNo4: 1,
		pageSize4: 9
	},
	mounted:function(){
		var MAX = 1000;
		var that = this;
        // var loginUserInfo = utils.getLoginUserInfo(true);
        var params = utils.getUrlParams();
        that.userId = params.userId;

		// 获取用户信息
        rest.post({
            url: 'user/userInfo/{userId}',
            urlParams : {
                userId : that.userId
            },
            success: function (result) {
                that.userInfo = result.data;
            }
        });

		//风采列表
        rest.post({
            url: 'article/myMien/{model}/{userId}/{pageNo}/{pageSize}',
            urlParams : {
                model: 1,
                userId : that.userId,
                pageNo : 1,
                pageSize : MAX
            },
            success: function (result) {
                that.list1 = result.rows;
            }
        });

		//随笔列表
        rest.post({
            url: 'article/myEssay/{model}/{userId}/{pageNo}/{pageSize}',
            urlParams : {
                model: 1,
                userId : that.userId,
                pageNo : 1,
                pageSize : MAX
            },
            success: function (result) {
                that.list2 = result.rows;
            }
        });

		//案例列表
        rest.post({
            url: 'article/mySuccess/{model}/{userId}/{pageNo}/{pageSize}',
            urlParams : {
                model: 1,
                userId : that.userId,
                pageNo : 1,
                pageSize : MAX
            },
            success: function (result) {
                that.list3 = result.rows;
            }
        });

		//动态列表
        rest.post({
            url: 'article/myWork/{model}/{userId}/{pageNo}/{pageSize}',
            urlParams : {
                model: 1,
                userId : that.userId,
                pageNo : 1,
                pageSize : MAX
            },
            success: function (result) {
                that.list4 = result.rows;
            }
        });

		
	},

	methods:{
	    dj : function() {
	        var that = this;
            var loginUserInfo = utils.getLoginUserInfo(true);

            mui.confirm('确认现在就支付协议律师费定金？', '提示', ['取消', '确定'], function(e) {
                if(e.index == 1) {
                    rest.post({
                        url: 'order/bargainMoney',
                        async : false,
                        data: {
                            userId :loginUserInfo.userId,//登陆用户Id
                            favoreeUserId :that.userId//律师的Id
                        },
                        success: function (result) {
                            creatMywebview('Order-pay.html?businessOrder=' + result.data.businessOrder, 'Order-pay.html?businessOrder=' + result.data.businessOrder);
                        },
                        fail: function (result) {
                            mui.toast(result.msg);
                        }
                    });
                }
            });

        }
	}
	
});
