var userId = storage.getItem('userId'),
	userToken = storage.getItem('token');

new Vue({
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
	},
	mounted(){
		var that = this;

		//预渲染竞拍案源
		ajax_json_token(
			"ali-legal/api/case/myOfferCase/" + userId,
			userToken,
			{},
			function (res) {
				if (res.code === 1) {
					that.list1 = res.data;
					// that.pageNo++;
				} else {
					// that.finished = true;
				}
			}
		);

		//预渲染委托案源
		ajax_json_token(
			"ali-legal/api/case/myCaseList/" + userId,
			userToken,
			{},
			function (res) {
				if (res.code === 1) {
					that.list2 = res.data;
					// that.pageNo++;
				} else {
					// that.finished = true;
				}
			}
		);

	},

	methods:{

	},
	
})
