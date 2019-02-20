new Vue({
	el:'#app',
	data:{
		vipIndex: 1,
		vipList: [['1','黄金'],['2','白金会员'],['3','钻石会员']],
		carIndex: 0,
		carList: [['0','其它',200], ['1','货车',400], ['2','客车',500]],
		yearIndex: 1,
		yearList: [[1,'1年'], [2,'2年'], [3,'3年'],[4,'4年'],[5,'5年'],[6,'6年'],[7,'7年'],[8,'8年'],[9,'9年'],[10,'10年'],[11,'11年'],[12,'12年']],
		ziliao:'未补充',
		equipmentType:1,//设备类型
		checked:false,//是否同意服务协议
        realName: '',
        idCard:'',
        idCardFront : '',
        idCardBack : '',
        jinE : 0
	},
	mounted : function(){
		var that = this;

        window.addEventListener("changeZiLiao", function(e) {
            that.ziliao = '已填写';
            that.realName = e.detail.realName;
            that.idCard = e.detail.idCard;
            that.idCardFront = e.detail.idCardFront;
            that.idCardBack = e.detail.idCardBack;
        });
	},
    ready :function() {
	    var that = this;

    },

	methods:{
	    sumJinE : function () {
            this.jinE = this.yearIndex * this.carList[this.carIndex][2];
        },



		a : function () {
            this.checked = !this.checked;
        },

		//选择会员类型
		// changeVip:function (event) {
		// 	console.log(event)
		// },

		// 开通
		kaitong:function () {
            var urlParams = utils.getUrlParams();
            var userId = urlParams.userId;

			var
			that = this,
			carType = that.carIndex,
			vipLevel = that.vipIndex,
			openYear = that.yearIndex;

			
			if (!that.checked){
				mui.toast('请阅读并同意《平台服务协议》');
				return;
			}

			if (that.ziliao == '未补充') {
                mui.toast('请补充资料');

			}else{
				//补充了资料
                rest.post({
                    url: 'user/openVipByConsultant',
                    data : {
                        userType : 1,//1：咨询者  ； 2 ： 咨询师
                        userId: userId,
                        carType: carType,
                        vipLevel: vipLevel,
                        openYear: openYear,
                        realName: that.realName,
                        idCard:that.idCard,
                        idCardFront: that.idCardFront,
                        idCardBack: that.idCardBack
                    },
                    success: function (res) {
                        mui.toast(result.msg);
                        creatMywebview('Order-pay.html?businessOrder=' + res.data.businessOrder, 'Order-pay.html?businessOrder=' + res.data.businessOrder);
                    },
                    fail: function (result) {
                        mui.toast(result.msg);
                    }
                });
			}
			
		}
	}
	
});

