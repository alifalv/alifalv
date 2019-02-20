/*! 配置文件 */
requirejs.config({
    // baseUrl : '',
    urlArgs: 'v=201502100127',
    paths: {

        'wxLogin': 'http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin',
        'qqLogin': 'http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader',
        'sinaLogin': 'http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=3335709324&debug=true',

        'WdatePicker': '../js/libs/My97DatePicker/WdatePicker',

        // 'amap' : 'https://webapi.amap.com/maps?v=1.3.29&key=9b85a4f124d833daa8ca7559a259b998&plugin=AMap.CitySearch',

        'amap': 'https://webapi.amap.com/maps?v=1.3.29&key=9b85a4f124d833daa8ca7559a259b998&callback=onAMapLoaded',
        'BMap': 'http://api.map.baidu.com/api?v=2.0&ak=GSHRZ9Khi5tBx16ZEzmoFyIWaIGGY1kE&callback=initialize',
        'template': '../js/libs/template-web',
        'template.ext': '../js/common/template.ext',


        'jquery': '../js/libs/jquery.min',
        'bootstrap': '../js/libs/bootstrap.min',
        'swiper': '../js/libs/swiper.min',
        'core': '../js/common/core',
        'constants': '../js/common/constants',
        'rest': '../js/common/rest',
        'utils': '../js/common/utils',
        'layui.all': '../js/libs/layer/layer',
        'dataList': '../js/common/dataList',
        'msg': '../js/common/msg',
        'pagination': '../js/common/pagination',
        'homePagination': '../js/common/homePagination',
        'page': '../js/common/page',
        'validator': '../js/libs/validator/local/zh-CN',
        'cropper': '../js/libs/cropper.min',
        'store': '../js/common/store',

        'thirdLogin': '../js/common/thirdLogin',

        "laydate": "../js/libs/laydate/laydate",
        // 'store' : '../js/libs/store.modern.min',
        'qrcode': '../js/libs/qrcode.min',

        'ueditor': '../js/libs/ueditor/ueditor.all',
        'ueditor.zh': '../js/libs/ueditor/lang/zh-cn/zh-cn',
        'ueditor.ext': '../js/common/ueditor.ext',
        'ueditor.internal': '../js/libs/ueditor/dialogs/internal',
        'zeroclipboard': '../js/libs/ueditor/third-party/zeroclipboard/ZeroClipboard.min',

        'imgUpload': '../js/common/imgUpload',

        'fileUpload': '../js/common/fileUpload',
        'viewer': '../js/libs/viewer.min',
        'css.min': '../js/libs/css.min',
        entry: "../js/config"
    },
    shim: {
        bootstrap: {
            deps: ['jquery'],
            exports: '$'
        },
        "laydate": {
            exports: 'laydate'
        },
        validator: {
            deps: ['../js/libs/validator/jquery.validator.min.js?css'],
            exports: '$'
        },
        msg: {
            deps: ['core'],
            exports: 'C'
        },
        'BMap': {
            deps: ['jquery'],
            exports: 'BMap'
        },

        'viewer': {
            deps: ['css!../css/viewer.min.css']
        },
        // 'swiper' : {
        //     deps: ['css!../css/swiper.min.css']
        // },

        'imgUpload': {
            deps: ['css!../css/img.css']
        },

        'ueditor': {
            deps: ['../js/libs/ueditor/ueditor.config', 'css!../js/libs/ueditor/themes/default/css/ueditor.min.css', 'jquery']
        },
        'ueditor.zh': {
            deps: ['ueditor']
        },
        'ueditor.ext': {
            deps: ['ueditor.zh']
        },
        'template.ext': {
            deps: ['template']
        }

        // other:['../js/common/init.js']
    },
    map: {
        '*': {
            css: 'css.min'
        }
    },

    //不打包的静态文件
    // excludeShallow: [
    //     'jquery','bootstrap','validator', 'laydate', 'viewer', 'layui.all', 'css.min'
    // ],
    waitSeconds: 0,
    name: "entry",
    out: '../../dist/js/main.js'
});
// require([
//     'css!../css/reset.css',
//     'css!../css/bootstrap.min.css',
//     'css!../css/iconfont.css',
//     'css!../css/swiper.min.css',
//     'css!../css/style.css',
//     'css!../css/ctiySelect.css'
// ]);
require(['msg', 'constants', 'rest', 'template.ext', 'jquery', 'store', 'utils', 'layui.all', 'bootstrap'], function(C, constants, rest, template, $, store, utils, layer) {
    window.UEDITOR_HOME_URL = constants.ueditorPath;

    $(function() {
        $('[data-toggle="popover"]').popover();
    });

    function windowOpen(url) {
        var a = document.createElement('a');
        a.setAttribute('href', url);
        a.setAttribute('target', '_blank');
        a.setAttribute('id', 'winopen');
        a.click();
    }

    /**
     * 链接跳转
     * @param url 链接地址
     * @param _isOpenNewWindow 是否在新窗口打开
     */
    function jumpUrl(url, _isOpenNewWindow) {
        if (_isOpenNewWindow) {
            windowOpen(url);
            // window.open(url);
        } else {
            location.href = url;
        }
    }

    //判断用户是否登录
    $(document).on('click', 'a', function() {
        var _$this = $(this);

        var _srcStr = _$this.attr('href');
        if (_srcStr == null || _srcStr == '' || _srcStr.indexOf('javascript') > -1) {
            return false;
        }

        //是否从新窗口打开
        var _isOpenNewWindow = false;

        if (_$this.is('[target="_blank"]')) {
            _isOpenNewWindow = true;
        }

        var _srcArr = _srcStr.split("\/");
        var _url = _srcArr[_srcArr.length - 1];

        console.log("url" + constants.viewUrl + "  ---  " + _url);
        //用户登录的页面
        if (_url.indexOf('personal_') > -1 || _url.indexOf('user_') > -1) {
            var loginUserInfo = store.getCache('userInfo');
            console.log("user:" + loginUserInfo);
            //缓存用户信息不为空
            if (null != loginUserInfo) {
                //是否是企业用户 且 未注册会员
                if (loginUserInfo.userType == 3 && loginUserInfo.vipLevel == 0) {
                    var _jumpUrl = constants.viewUrl + 'reg_vip_enterprise.html?userId=' + loginUserInfo.userId;
                    jumpUrl(_jumpUrl, _isOpenNewWindow);

                } else {
                    jumpUrl(_srcStr, _isOpenNewWindow);
                }

            } else {
                var _jumpUrl = constants.viewUrl + 'login.html?jump=' + constants.viewUrl + _srcStr;
                jumpUrl(_jumpUrl, _isOpenNewWindow);
            }

        } else {
            jumpUrl(_srcStr, _isOpenNewWindow);
        }
        return false;
    });





    //启动配置
    var config = {
        viewSuffix: '',
        readyEvent: 'ready', //触发ready的事件
        usePageParam: false,
        crossDomainHandler: function(opts) {}
    };
    C.launch(config);
    window.C = C;

    var restConfig = {
        isNeedToken: false,
        baseUrl: constants.apiUrl,
        headers: {
            token: store.getCache('token')
        },
        fail: function(result) {
            C.msg.fail(result.msg || result.error);
        },
        error: function() {
            C.msg.warn('连接失败');
        }
    };
    rest.setOptions(restConfig);
    window.rest = rest;


    layer.config({
        path: '../js/libs/layer/' //layer.js所在的目录，可以是绝对目录，也可以是相对目录
    });
    window.layer = layer;

    /**
     * form 参数转json
     * @returns {{}} json
     */
    $.fn.serializeObject = function(e, flag) {
        var o = {};
        var a = this.serializeArray();
        var f = function() {
            if (o[this.name]) {
                // if (!o[this.name].push) {
                //     o[this.name] = [o[this.name]];
                // }
                // o[this.name].push(this.value || '');
                o[this.name] = o[this.name] + ',' + this.value || ''; //数组用,号方式
            } else {
                o[this.name] = this.value || '';
            }
        };
        if (e) {
            $.each(a, function() {
                if (this.value != '' & this.value != null & this.value !== undefined) {
                    f.call(this);
                }
            });
        } else {
            $.each(a, function() {
                f.call(this);
            });
        }
        return o;
    };
    window.$ = $;


    // var loadMask;
    // $().ready(function(){
    //     loadMask = layer.load(0, {shade:  [.8,'#fff']});
    // });


    //页头页尾
    require(['../js/common/layout'], function(index) {
        // layer.close(loadMask);
        //页面淡入加载，解决布局问题
        var bodyMask = (function() {
            var opacity = 0;
            var bodyLoadingOpacity = 1;

            var index = 0;
            var $body = $('body');
            $body.before('<div class="layui-layer layui-layer-loading" id="body_loading"><div class="layui-layer-content layui-layer-loading0"></div></div>');
            var $bodyLoading = $('#body_loading');
            var interval = setInterval(function() {
                index++;
                if (index < 2) {
                    opacity += 0.1;

                } else {
                    opacity += 0.2;
                }
                bodyLoadingOpacity -= 0.3;
                $body.css('opacity', opacity);
                $bodyLoading.css('opacity', bodyLoadingOpacity);
                if (opacity >= 1) {
                    clearInterval(interval);
                    $body = null;
                    opacity = null;
                    interval = null;
                    index = null;
                    $bodyLoading.remove();
                }
            }, 100);
        })();

        //引入页面的js
        var _pathname = window.location.pathname;
        var _name = _pathname.split("/");
        var _jsName = _name[_name.length - 1].split('.')[0];
        if (!_jsName) { //域名访问首页下特殊情况
            _jsName = 'index';
        }
        require(['../js/modules/' + _jsName], function(index) {});
    });
});