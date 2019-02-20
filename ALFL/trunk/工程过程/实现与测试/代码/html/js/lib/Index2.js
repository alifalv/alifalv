
new Vue({
    el:'#app',
    data:{
		newAsk: [],//最新资讯
		newCase: [],//最新案源
		loading1: false,//正在加载
		finished1: false,//不再加载
		loading2: false,//正在加载
		finished2: false,//不再加载
        pageNo:1,
        pageSize :10
	},
	mounted: function () {
		var that = this;
		//预渲染最新咨询列表
		ajax_json(
			"ali-legal/api/advice/adviceList/1/" + that.pageNo + "/" + that.pageSize,
			{},
			function (res) {
				console.log(res)
				if (res.code === 1) {
					that.newAsk = res.rows;
					that.pageNo++;
				} else {
					that.finished1 = true;
				}
			}
		);
		//预渲染最新案源列表
		ajax_json(
			"ali-legal/api/article/adviceList/1/" + that.pageNo + "/" + that.pageSize,
			{},
			function (res) {
				alert(1)
				console.log(res)
				if (res.code === 1) {
					that.newCase = res.rows;
					that.pageNo++;
				} else {
					that.finished1 = true;
				}
			}
		)

	},
	methods:{
		// 加载更多咨询
		loadMore1: function () {
			var that = this;
			that.loading = true;

			setTimeout(function () {
				ajax_json(
					"ali-legal/api/article/bookMakeModelList/1/" + that.pageNo + "/" + that.pageSize,
					{},
					function (res) {

						if (res.code === 1) {
							if (res.rows > 0) {//判断是否有数据
								that.newAsk = that.newAsk.concat(res.rows);
								that.pageNo++;
								that.loading = false;
							} else {
								that.loading = false;
								that.finished = true;
							}
						}
					}
				)
			}, 500);
		},

		// 加载更多案源
		loadMore2: function () {
			var that = this;
			that.loading = true;

			setTimeout(function () {
				ajax_json(
					"ali-legal/api/article/bookMakeModelList/1/" + that.pageNo + "/" + that.pageSize,
					{},
					function (res) {

						if (res.code === 1) {
							if (res.rows > 0) {//判断是否有数据
								that.newCase = that.newCase.concat(res.rows);
								that.pageNo++;
								that.loading = false;
							} else {
								that.loading = false;
								that.finished = true;
							}
						}
					}
				)
			}, 500);
		}
	}
})


function myswiper() {
	var swiper = new Swiper('.swiper-container', {
		//effect: 'flip',
		loop: true,
		autoplay: 5000,
		pagination: '.swiper-pagination',
		paginationClickable: true
	});
}
function myswiper2() {
	var swiper = new Swiper('.swiper-container2', {
		loop: true,
		autoplay: 2000,
		direction: 'vertical',
		pagination: '.swiper-pagination',
		paginationClickable: true
	});
}
myswiper();
myswiper2();
