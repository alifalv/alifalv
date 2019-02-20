var commonUrl = 'http://gd.hngdstny.com/sx/apigd/';
 

mui.plusReady(function() {
    //获取首页webview
    var motherwebview = plus.webview.getWebviewById('Index-nav.html');
    // 动画窗口
    function animateWindow(type, href, _id) {
        wp || (wp = plus.webview.create(href, _id, {
            scrollIndicator: 'none',
            scalable: false,
            popGesture: 'none'
        }, {
            preate: true
        }));
        wp.show(type);
    }
});



//判断用户是否登录
$(document).on('click', 'a', function() {
    var _$this = $(this);

    var _srcStr = _$this.attr('href');

    creatMywebview(_srcStr, _srcStr)
    return false;
});

/**
 * 防止连续点击导致webview打开出错
 * 注：主要用于打开新窗口时候
 * @author dujian
 */
var tap_first = null;

function unsafe_tap() {
    if (!tap_first) {
        tap_first = new Date().getTime();
        setTimeout(function() {
            tap_first = null;
        }, 1000);
    } else {
        return true;
    }
} 
// 　跳转链接时的返回
var oldback = mui.back;
mui.back = function() {
    mui.currentWebview.opener().show('none');

    oldback();
}

//创建并打开新的webview
var creatMywebview = function(href, _id, isOpen) {
    // 防止重复跳转
    if (unsafe_tap()) return;
    if (_id) {
        _id = href.split('?')[0];
    }

    if (href.indexOf('GoodsList.html') >= 0) {
        _id = href.split('?')[0];
    }
    //非plus环境，直接走href跳转
    // if (!mui.os.plus) {
    //     location.href = href;
    //     return;
    // }
    // if (href.indexOf('http') != -1) {
    //     mui.back() = function() {
    //         alert("sss");
    //     }
    // }

    if (href.indexOf('.html') || isOpen) {
        var showloading = false;
        if (~href.indexOf('GoodsList.html') || ~href.indexOf('GoodsIntr.html')) {
            showloading = true;
        }
        mui.openWindow({
            url: href,
            id: _id,
            styles: {
                top: '0px', //新页面顶部位置
                bottom: '0px' //新页面底部位置
            },
            extras: {

            },

            createNew: false, //是否重复创建同样id的webview，默认为false:不重复创建，直接显示
            show: {
                autoShow: true, //页面loaded事件发生后自动显示，默认为true
                event: 'titleUpdate', //页面显示时机，默认为titleUpdate事件时显示
                extras: {} //窗口动画是否使用图片加速
            },
            waiting: {
                autoShow: showloading, //自动显示等待框，默认为true
                title: 'Loading...', //等待对话框上显示的提示内容
                options: {
                    //width: '40px', //等待框背景区域宽度，默认根据内容自动计算合适宽度
                    //height: '40px' //等待框背景区域高度，默认根据内容自动计算合适高度
                }
            }
        });
    }
};

// 新建一个vue loding 对象示例
//var vue_loading = new Vue({
//el: '#load',
//data: {
//  showLoading: false
//},
//components: {
//  'loading': {
//    template: "<div class='loading'> <img src='./images/loading10.svg'></div>",
//  }
//}
//});

//支持从缓存获取图片的懒加载方法（需引用md5.min.js和pluRready之后用）
function lazyload(obj, callback) {
    var debug = false; // 默认打印调试日志
    if (obj.getAttribute('data-loaded')) {
        return;
    }

    var image_url = obj.getAttribute('data-lazyload');
    debug && console.log(image_url);

    // 1. 转换网络图片地址为本地缓存图片路径，判断该图片是否存在本地缓存
    // http://...jpg -> md5
    // 缓存目录 _downloads/image/(md5).jpg
    var image_md5 = md5(image_url);
    var local_image_url = '_downloads/image/' + image_md5 + '.jpg'; // 缓存本地图片url
    var absolute_image_path = plus.io.convertLocalFileSystemURL(local_image_url); // 平台绝对路径

    // new temp_img 用于判断图片文件是否存在
    var temp_img = new Image();
    temp_img.src = absolute_image_path;
    temp_img.onload = function() {
        debug && console.log('存在本地缓存图片文件' + local_image_url + '，直接显示');

        // 1.1 存在，则直接显示（本地已缓存，不需要淡入动画）
        obj.setAttribute('src', absolute_image_path);
        obj.setAttribute('data-loaded', true);
        obj.classList.add('img-lazyload');

        callback && callback();
        return;
    }
    temp_img.onerror = function() {
        debug && console.log('不存在本地缓存图片文件');

        // 1.2 下载图片缓存到本地
        debug && console.log('开始下载图片' + image_url + ' 缓存到本地: ' + local_image_url);

        function download_img() {
            var download_task = plus.downloader.createDownload(image_url, {
                filename: local_image_url // filename:下载任务在本地保存的文件路径
            }, function(download, status) {
                if (status != 200) {
                    // 下载失败,删除本地临时文件
                    debug && console.log('下载失败,status' + status);
                    if (local_image_url != null) {
                        plus.io.resolveLocalFileSystemURL(local_image_url, function(entry) {
                            entry.remove(function(entry) {
                                debug && console.log("临时文件删除成功" + local_image_url);
                                // 重新下载图片
                                download_img();
                            }, function(e) {
                                debug && console.log("临时文件删除失败" + local_image_url);
                            });
                        });
                    }
                } else {
                    // 把下载成功的图片显示
                    // 将本地URL路径转换成平台绝对路径
                    obj.setAttribute('src', plus.io.convertLocalFileSystemURL(local_image_url));
                    obj.setAttribute('data-loaded', true);
                    obj.classList.add('img-lazyload');

                    callback && callback();
                }
            });
            download_task.start();
        }
        download_img();
    }
}

//请求错误的错误信息提示
function RequestErrorMsg(_code) {
    var ErrorMsg;
    switch (_code) {
        case 'timeout':
            ErrorMsg = '啊噢~网络有点不给力呢,请重试~';
            break;
        case 'abort':
            ErrorMsg = '啊噢~网络中断了,请检查您的网络~';
            break;
        default:
            ErrorMsg = '啊噢~加载失败了~';
            break;
    }
    return ErrorMsg;
}

//获取url中的数据
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};

//在url后加入需传递的数据
function jointUrl(num1, num2, num3) {
    var Href = num1 + "?id=" + num2 + "&catid=" + num3;
    return Href;
}

//验证姓名
function verifyName(val) {
    var reg = /^[\u4E00-\u9FA5]{2,4}$/;
    if (!reg.test(val)) {
        mui.toast("请输入正确的姓名");
        return false;
    } else {
        return true;
    }
}

//验证中文
function isChinese(temp) {
    if (!temp == "") {
        var re = /[^\u4e00-\u9fa5]/;
        if (re.test(temp)) {
            mui.toast('请输入正确的银行名称');
            return false;
        } else {
            return true;
        }
    } else {
        mui.toast("银行名称不能为空");
    }
}

//验证手机号
function checkPhone(phone) {
    if (!(/^1[34578]\d{9}$/.test(phone))) {
        mui.toast("请输入正确的手机号");
        return false;
    } else {
        return true;
    }
}

//字符串去点空格
function Trim(str, is_global) {
    var result;
    result = str.replace(/(^\s+)|(\s+$)/g, "");
    if (is_global.toLowerCase() == "g")
        result = result.replace(/\s/g, "");
    return result;
};　　
//验证银行卡
function CheckBankNo(t_bankno) {  　　
    var bankno = Trim(t_bankno.val(), "g");
    if (bankno == "") {    　　
        mui.toast("请填写银行卡号");     
        return false;   
    }   
    if (bankno.length < 16 || bankno.length > 19) {     
        mui.toast("银行卡号长度必须在16到19之间");     
        return false;   
    }   
    var num = /^\d*$/; //全数字
       
    if (!num.exec(bankno)) {     
        mui.toast("银行卡号必须全为数字");     
        return false;   
    }    //开头6位
       
    var strBin = "10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99";   
    if (strBin.indexOf(bankno.substring(0, 2)) == -1) {     
        mui.toast("银行卡号开头6位不符合规范");     
        return false;   
    }     
    mui.toast("验证通过!");     
    return true;   
}

//验证密码
function isPasswd(s) {
    var patrn = /^(?:\d+|[a-zA-Z]+|[!@#$%^&*]+){6,15}$/;
    if (!patrn.exec(s)) {
        mui.toast('请输入正确密码');
        return false;
    } else {
        return true;
    }
}

//二次验证密码
function toispasswd(a, b) {
    if (a === b) {
        return true;
    } else {
        mui.toast('两次输入的账号不一样')
        return false;
    };
}

//以下为原生js方法
function hasClass(element, csName) {　　　
    return !!element.className.match(new RegExp("(\\s|^)" + csName + "(\\s|$)"));　　
}　　　　　　
function addClass(element, csName) {　　　　　　　
    if (!hasClass(element, csName)) {　　　　　　　　　
        element.className += ' ' + csName;　　　　　　
    }
}

function removeClass(element, csName) {
    if (hasClass(element, csName)) {
        element.className = element.className.replace(new RegExp("(\\s|^)" + csName + "(\\s|$)"), "");
    }
};

Vue.filter('createImgUrl', function(value) {
    return utils.createImgUrl(value);
});
Vue.filter('subDay', function(value) {
    return value.substring(0, 10);
});
Vue.filter('removeHtmlTag', function(text) {
    return text.replace(/&nbsp;/g, '').replace(/<[^>]+>/g, '');
});

Vue.filter('split', function(text) {
    return text;
});

Vue.filter('dateDiff', function(str) {
    var dayTime = 1000 * 60 * 60 * 24;
    var time = utils.formatTime(str);
    var nowTime = (new Date()).getTime();

    //在3天内的才转换
    if (nowTime - time <= dayTime * 3) {
        return utils.getDateDiff(time);
    }
    return str;
});


Vue.filter('getArticleUrlByBusinessType', function(businessType, type, id, businessOrder) {

    if (businessType == '阿里裁决') {
        return 'AlicaipanInfo.html?articleId=' + id;

    } else if (businessType == '法律咨询') {
        return 'Lawask-Info.html?articleId=' + id;

    } else if (businessType == '百姓吐槽') {
        return 'complaininfo.html?articleId=' + id;

    } else if (businessType == '案件委托') {
        return 'Lawbuy-Info.html?caseId=' + id;

    } else if (businessType == '协议律师费') {
        return 'Order-pay.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

    } else if (businessType == '会员充值') {
        return 'Order-pay.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

    } else if (businessType == '法律文书制作') {
        return 'Order-pay.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

    } else if (businessType == '法律咨询赏金') {
        return 'Order-pay.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

    } else if (businessType == '法律咨询打赏') {
        return 'Order-pay.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

    } else if (businessType == '法律文书制作赏金') {
        return 'Order-pay.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

    } else if (businessType == '阿里裁决打赏') {
        return 'Order-pay.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

    } else if (businessType == '案件委托赏金') {
        return 'Order-pay.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

    } else if (businessType == '百姓吐槽打赏') {
        return 'Order-pay.html?businessOrder=' + (type == 'msg' ? id : businessOrder);

    } else {
        return 'javascript:void(0);'
    }
});


Vue.filter('getArticleUrlByColumnCode', function(columnCode, articleId, userId) {
    if (columnCode == '0') { //百姓吐槽
        return 'complaininfo.html?articleId=' + articleId;

    } else if (columnCode == '1') { //法律疑难问题与观点
        return 'HighestPoints.html?articleId=' + articleId;

    } else if (columnCode == '2') { //公务员招聘
        return 'civilrecruit.html?articleId=' + articleId;

    } else if (columnCode == '3') { //常用文书模板
        return 'DocInfo.html?articleId=' + articleId;

    } else if (columnCode == '4') { //文书制作知识
        return 'lawdocmarkinfo.html?articleId=' + articleId;

    } else if (columnCode == '5') { //法律人信息
        return 'articles_lawerinfo.html?articleId=' + articleId;

    } else if (columnCode == '6') { //咨询问题
        return 'Lawask-Info.html?articleId=' + articleId;

    } else if (columnCode == '7') { //法律培训
        return 'lawtraininginfo.html?articleId=' + articleId;

    } else if (columnCode == '8') { //法律法规
        return 'statuteinfo.html?articleId=' + articleId;

    } else if (columnCode == '9') { //阿里裁决
        return 'AlicaipanInfo.html?articleId=' + articleId;

    } else if (columnCode == '10') { //我的风采
        return 'home_style.html?articleId=' + articleId + '&userId=' + userId;

    } else if (columnCode == '11') { //工作动态
        return 'home_work.html?articleId=' + articleId + '&userId=' + userId;

    } else if (columnCode == '12') { //成功案例
        return 'home_case.html?articleId=' + articleId + '&userId=' + userId;

    } else if (columnCode == '13') { //我的随笔
        return 'home_essay.html?articleId=' + articleId + '&userId=' + userId;

    } else if (columnCode == '14') { //法律咨询
        return 'Lawask-Info.html?articleId=' + articleId;

    } else if (columnCode == '15') { //案件委托
        return 'Lawbuy-Info.html?caseId=' + articleId;

    } else {
        return '文章类型错误';
    }
});


var restConfig = {
    isNeedToken: false,
    //baseUrl: "ali-legal/api/",
    baseUrl: utils.constants.apiUrl,
    headers: {
        token: store.getSession('t')
    },
    fail: function(result) {
        mui.toast(result.msg || result.error);
    },
    error: function() {
        mui.toast("网络连接失败");
    }
};
rest.setOptions(restConfig);