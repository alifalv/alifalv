//mui js
mui.init({
	swipeBack: true //启用右滑关闭功能
});

var aniShow = "pop-in";
//只有ios支持的功能需要在Android平台隐藏；
if(mui.os.android) {
	var list = document.querySelectorAll('.ios-only');
	if(list) {
		for(var i = 0; i < list.length; i++) {
			list[i].style.display = 'none';
		}
	}
	//Android平台暂时使用slide-in-right动画
	if(parseFloat(mui.os.version) < 4.4) {
		aniShow = "slide-in-right";
	}
}

//主列表点击事件
mui('.page-slideright').on('tap', 'a', function() {
	var id = this.getAttribute("data-wid");
	
	if(!id) {
		id = this.getAttribute('href');
	}
	
	var href = this.getAttribute('href');
	
	if(href.indexOf('GoodsList.html')>=0) {
		id = this.getAttribute('href').split('?')[0];
	}

	//非plus环境，直接走href跳转
	if(!mui.os.plus) {
		location.href = href;
		return;
	}

	var titleType = this.getAttribute("data-title-type");

	var webview_style = {
		popGesture: "close",
		hardwareAccelerated:true
	}
	var extras = {};

	if(titleType == "native") {
		//如下场景不适用下拉回弹：
		//1、单webview下拉刷新；2、底部有fixed定位的div的页面
		if(!~id.indexOf('pullrefresh.html') && !~href.indexOf("examples/tabbar.html") && !~href.indexOf("list-to-detail/listview.html")) {
			webview_style.bounce = "vertical";
		}
		//图标页面需要启动硬件加速
		if(~id.indexOf('icons.html') || ~id.indexOf("echarts.html")) {
			webview_style.hardwareAccelerated = true;
		}
		if(~id.indexOf('im-chat.html')) {
			extras.acceleration = "none";
		}

		webview_style.statusbar = {
			background: "#f7f7f7"
		}

		mui.openWindowWithTitle({
			url: href,
			id: id,
			styles: webview_style,
			show: {
				event: "loaded",
				extras: extras
			},
			waiting: {
				autoShow: false
			}
		}, {
			title: {
				text: this.innerText.trim()
			},
			back: {
				image: {
					base64Data: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAb1BMVEUAAAAAev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8Aev8AAACubimgAAAAI3RSTlMAGfUTGfQTGPMSGPIYGhgaGBsXGxcbFxwXHBccFhwWHRYdHWufDPQAAAABYktHRACIBR1IAAAAB3RJTUUH4QETEBwooeTlkQAAAJVJREFUSMft1EkSgkAQRNFGUXFWHBDBibr/HTUwD5B/48Ig1y+io7u6MqUhf5hsNEY+j5hMgZ/FJ8Xc9ovos3T96utjbfqN/Nb0O/m96Uv5g+mP8ifTn+Ur01/ka9Nf5RvTt/I309/lH6Z/yr9Mn+Q71/MT8B34K/E58Enzv8R/K98HvnF8p3lr8F7izce7lbf3kJ/lDQp9HdBhgg3PAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE3LTAxLTE5VDE2OjI4OjQwKzA4OjAwpTDFwQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNy0wMS0xOVQxNjoyODo0MCswODowMNRtfX0AAAAASUVORK5CYII="
				}
			}
		});
	} else if(href && ~href.indexOf('.html')) {
		var extras = {};
		if(id && id == "viewgroup") { //强制启用截屏
			extras.acceleration = "capture";
		}

		if(titleType && titleType == "transparent_native") {
			webview_style.titleNView = {
				'backgroundColor': '#f7f7f7',
				'titleText': this.innerHTML.trim(),
				'titleColor': '#000000',
				type: 'transparent',
				autoBackButton: true,
				splitLine: {
					color: '#cccccc'
				}
			}
		} else {
			webview_style.statusbar = {
				background: "#f7f7f7"
			}
		}

		var webview = plus.webview.create(this.href, id, webview_style, extras);
		webview.onloading = onLoading();
		webview.onloaded = onLoaded();
		webview.addEventListener("titleUpdate", function() {
			setTimeout(function() {
				webview.show(aniShow, 300);
			}, 0);
		});
		function onLoading(e){
			plus.nativeUI.showWaiting();
		};
		function onLoaded(e){
			plus.nativeUI.closeWaiting();
		}
	}
});