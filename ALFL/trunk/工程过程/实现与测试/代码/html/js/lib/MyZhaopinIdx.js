var vm = new Vue({
	el:'#app',
	data:{
		userType:''
	},
	mounted :function(){
		var userInfo = utils.getLoginUserInfo(true);
        this.userType = userInfo.userType;
	},

	methods:{
	}
});



