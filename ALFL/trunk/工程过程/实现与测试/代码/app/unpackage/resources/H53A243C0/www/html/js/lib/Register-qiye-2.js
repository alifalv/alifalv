new Vue({
	el:'#app',
	data:{
        userId: '',
        jinE : 0,
		vipIndex: 1,
        vipList: [['1','黄金会员'],['2','白金会员'],['3','钻石会员']],
        vipListJinE : [2000, 5000, 20000],
        yearIndex: 1,
        yearList: [[1,'1年'], [2,'2年'], [3,'3年'],[4,'4年'],[5,'5年'],[6,'6年'],[7,'7年'],[8,'8年'],[9,'9年'],[10,'10年'],[11,'11年'],[12,'12年']],
        equipmentType:1,//设备类型
		checked:false//是否同意服务协议
	},
	mounted : function(){
		var that = this;
        var urlParams = utils.getUrlParams();
        userId = urlParams.userId;
        that.sumJinE();
	},

	methods:{
        sumJinE : function () {
            this.jinE = this.yearIndex * this.vipListJinE[this.vipIndex-1];
        },

        /**
         * 企业咨询者开通会员
         * @param type 开通时的类型【0：初次；1：续充；2补差价】
         * @param vipLevel
         * @param userId
         * @param openYear 开通年数
         * @param func
         */
        openVipByCompany : function (type, vipLevel, userId, openYear, func) {
		rest.post({
			url: 'user/openVipByCompany',
			async : false,
			data : {
				vipLevel: vipLevel,
				userId : userId,
				openYear : openYear,
				type : type
			},
			success: function(result) {
				func && func(result)
			},
			fail : function (result) {
                mui.toast(result.msg);
			}
		});
	},

		// 开通
		kaitong:function () {
			var 
			that = this,
			vipLevel = that.vipIndex,
			openYear = that.yearIndex;

			if (!that.checked){
				mui.toast('请阅读并同意《平台服务协议》');
				return;
			}
            that.openVipByCompany(0, vipLevel, that.userId, openYear, function (result) {

                creatMywebview('Order-pay.html?businessOrder=' + result.data.businessOrder, 'Order-pay.html?businessOrder=' + result.data.businessOrder);


                //TODO 支付
                // utils.jumpPay(result.data.businessOrder, function () {
                //     creatMywebview('Register-qiye-sucess.html', 'Register-qiye-sucess.html');
                // });
            });
		}
	}
	
});

