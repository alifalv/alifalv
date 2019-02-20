 //mui初始化
    mui.init();
    var aniShow = {};
    var subpages = ['Index.html', 'Lawsearch.html', 'KuaiJie.html', 'DateSelect.html', 'Myinfo.html'];
    //当前激活选项
    var activeTab = subpages[0];
    var title = document.getElementById("title");
    var mya = document.getElementById("mya");

    //选项卡点击事件
    mui('.mui-bar-tab').on('tap', 'a', function(e) {
        var targetTab = this.getAttribute('href');
		 
		if(targetTab == "https://alifalv.lvpin100.com/"){
			 mui.trigger(mya, 'click');
			// window.location.href='https://alifalv.lvpin100.com/';
			// window.open ('https://alifalv.lvpin100.com/', 'newwindow', 'height=100, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no') //这句要写成一行
			// alert(1)
			return false
		}

        loadThisUrl(targetTab);

        //若点击页面为快捷入口，则隐藏底部导航
        // if (targetTab == subpages[2]) {
        //     animateWindow('zoom-fade-out', subpages[2], subpages[2]);

        //     function animateWindow(type, href, _id) {
        //         var wp;
        //         wp || (wp = plus.webview.create(href, _id, {
        //             scrollIndicator: 'none',
        //             scalable: false,
        //             popGesture: 'none'
        //         }, {
        //             preate: true
        //         }));
        //         wp.show(type);
        //     }
        //     return false;
        // }

        //加载html到当前点击得webview
        function loadThisUrl(currentTap) {
            var currentview = plus.webview.getWebviewById(currentTap);
            var _ThisUrl;
            //   console.log(currentview.getURL());
            if (currentview.getURL() != null) {
                _ThisUrl = currentview.getURL().split('html/')[1];
                plus.webview.show(_ThisUrl, "fade-in", 300);
            };
            if (currentTap != _ThisUrl || _ThisUrl == null || _ThisUrl == '') {
                currentview.loadURL(currentTap);
            }
        }

        if (targetTab == activeTab) {
            return;
        }  
        //更换标题
        title.innerHTML = this.querySelector('.mui-tab-label').innerHTML;
        //显示目标选项卡
        //若为iOS平台或非首次显示，则直接显示
        if (mui.os.ios || aniShow[targetTab]) {
            plus.webview.show(targetTab);
        } else {
            //否则，使用fade-in动画，且保存变量
            plus.webview.show(targetTab, "fade-in", 300);
        }
        //隐藏当前;
        // plus.webview.hide(activeTab);
        // //更改当前活跃的选项卡
        activeTab = targetTab;
    });

    // //自定义事件：模拟点击“首页选项卡”
    document.addEventListener('gohome', function() {
        var defaultTab = document.getElementById("defaultTab");
        //模拟首页点击
        mui.trigger(defaultTab, 'tap');
        title.innerHTML = this.querySelector('#defaultTab .mui-tab-label').innerHTML;
        //切换选项卡高亮
        var current = document.querySelector(".mui-bar-tab>.mui-tab-item.mui-active");
		 
        if (defaultTab !== current) {
// 			if(targetTab == "https://alifalv.lvpin100.com/"){
// 				return
// 			}
			 
			// alert(1)
            current.classList.remove('mui-active');
            defaultTab.classList.add('mui-active');
        }
    });

    //自定义事件：模拟点击“个人信息”
    // document.addEventListener('MyInfo', function() {
    //     var MyInfotab = document.getElementById("MyInfotab");
    //     mui.trigger(MyInfotab, 'tap');
    //     var current = document.querySelector(".mui-bar-tab>.mui-tab-item.mui-active");
    //     if (MyInfotab !== current) {
    //         current.classList.remove('mui-active');
    //         MyInfotab.classList.add('mui-active');
    //     }
    // });

    //自定义事件：修改个人信息之后刷新个人中心
    document.addEventListener('refueshMyInfo', function() {
        var refresh_MyInfo = plus.webview.getWebviewById('MyInfo.html');
        refresh_MyInfo.loadURL('MyInfo.html');
        var MyInfotab = document.getElementById("MyInfotab");
        mui.trigger(MyInfotab, 'tap');
        var current = document.querySelector(".mui-bar-tab>.mui-tab-item.mui-active");
        if (MyInfotab !== current) {
			
            current.classList.remove('mui-active');
            MyInfotab.classList.add('mui-active');
        }
    });

    //自定义事件：登录后刷新购物车和个人中心
    document.addEventListener('refueshpage', function() {
        var refresh_MyInfo = plus.webview.getWebviewById('MyInfo.html');
        var refresh_Trolley = plus.webview.getWebviewById('Trolley.html');
        refresh_MyInfo.loadURL('MyInfo.html');
        refresh_Trolley.loadURL('Trolley.html');
        var MyInfotab = document.getElementById("MyInfotab");
        mui.trigger(MyInfotab, 'tap');
        var current = document.querySelector(".mui-bar-tab>.mui-tab-item.mui-active");
        if (MyInfotab !== current) {
            current.classList.remove('mui-active');
            MyInfotab.classList.add('mui-active');
        }
    });

    //自定义事件：退出登录后刷新购物车和个人中心
    document.addEventListener('refueshpage-logout', function() {
        var refresh_MyInfo = plus.webview.getWebviewById('MyInfo.html');
        var refresh_Trolley = plus.webview.getWebviewById('Trolley.html');
        refresh_MyInfo.loadURL('Unloaded.html');
        refresh_Trolley.loadURL('Unloaded.html');
        var MyInfotab = document.getElementById("MyInfotab");
        mui.trigger(MyInfotab, 'tap');
        var current = document.querySelector(".mui-bar-tab>.mui-tab-item.mui-active");
        if (MyInfotab !== current) {
            current.classList.remove('mui-active');
            MyInfotab.classList.add('mui-active');
        }
    });

    //自定义事件：购物车 ---- 刷新购物车页面时的角标变化
    document.addEventListener('refreshTrolley', function(event) {
        var _goodsnum = parseFloat(event.detail.goodsnum);
        var _badge = document.querySelector('.mui-badge');
        if (_goodsnum > 0) {
            _badge.innerHTML = _goodsnum;
            _badge.classList.remove('hide');
        } else {
            _badge.classList.add('hide');
        }
    });

    //自定义事件：购物车 ---- 退出登陆以后 购物车的角标变化
    document.addEventListener('refreshTrolley-logout', function(event) {
        var _badge = document.querySelector('.mui-badge');
        _badge.classList.add('hide');
    });

    //自定义事件：购物车 ---- 删除商品时 购物车角标变化
    document.addEventListener('refreshTrolley-badge', function(event) {
        var _badge = document.querySelector('.mui-badge');
        var goodsnum = parseFloat(_badge.innerHTML);
        var num_minus;
        if (goodsnum > 1) {
            num_minus = goodsnum - 1;
            _badge.innerHTML = num_minus;
        } else {
            _badge.classList.add('hide');
        }
    });

    //自定义事件：购物车 ----  加入购物车后  购物车内商品列表刷新
    document.addEventListener('refreshTrolleyList', function(event) {
        var refresh_TrolleyList = plus.webview.getWebviewById('Trolley.html');
        refresh_TrolleyList.loadURL('Trolley.html');
    });