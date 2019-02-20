var userId = storage.getItem('userId'),
	userToken = storage.getItem('token');

	var vm = new Vue({
	el:'#app',
	data:{
		loading1: false,//正在加载
		finished1: true,//不再加载
		list1: [],
		pageNo1: 1,
		pageSize1: 10,

		loading2: false,//正在加载
		finished2: true,//不再加载
		list2: [],
		pageNo2: 1,
		pageSize2: 10,

		userType:''
	},
	mounted : function(){
		
		var
		that = this;

		// 投递的岗位


		// 收藏的岗位
		ajax_json_token(
			'/ali-legal/api/user/collectionJobList/' + userId, userToken,
			{}, 
			function (res) {
				if (res.code === 1) {
					that.list2 = res.data;
				}
			}
		)


		
	
	},

	methods:{
	}
	
})



