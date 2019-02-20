//mui js
mui.init({
	swipeBack: true //启用右滑关闭功能
});

//主列表点击事件
mui('.page-slideright').on('tap', 'a', function() {
	var _id = this.getAttribute("data-wid");
	var href = this.getAttribute('href');
	
	creatMywebview(href,_id);
});