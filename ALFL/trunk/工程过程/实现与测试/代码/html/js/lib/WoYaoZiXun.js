var jquery = "//cdn.bootcss.com/jquery/2.1.4/jquery.js";
var three_part_apis = [
        "//cdn.bootcss.com/webuploader/0.1.1/webuploader.js"
    ],
    _isLoaded = false;

function loadQueue(cb) {
    if (!jquery) {
        cb && cb();
        return;
    }
    getScript(jquery, function() {
        var list = three_part_apis;
        if (_isLoaded) {
            cb();
            return;
        }
        var len = list.length,
            i = 0,
            c = 0,
            ready = function() {
                if (++c === len) {
                    cb && cb();
                }
            };
        _isLoaded = true;
        while (i < len) {
            getScript(list[i++], ready);
        }

        three_part_apis.length = 0;
    });
    jquery = null;
}

//  function createCssLink() {
//    var d = document, i = three_part_css.length, s, frg = d.createDocumentFragment();
//    while (i--) {
//      s = d.createElement("link");
//      s.rel = "stylesheet";
//      s.href = three_part_css[i];
//      frg.appendChild(s);
//    }
//    d.head.appendChild(frg);
//  }

function getScript(src, cb) {
    var d = document,
        s = d.createElement("script");
    s.async = true;
    s.onload = cb;
    s.src = src;
    d.body.appendChild(s);
}

if (!Object.assign) {
    Object.assign = function(t, s) {
        for (var o in s) {
            t[o] = s[o];
        }
    }
}


new Vue({
    el: '#app',
    data: {
        title: '',
        adviceContent: '',
        reward: '',

        registerAddress: '',
        workAddress: '',
        weChat: '',
        qq: '',
        email: '',
        workImg1: '',
        workImg2: '',
        workImg3: '',
        caseImgs: '',
        caseImg1: '',
        caseImg2: '',
        caseImg3: '',
        ImgBase64Str: '', //临时存放上传图片
        caseId: '',
        caseTitle: '',
        caseList: [],
        adviceCount: [],
        btn: '提交审核',

    },
    mounted: function() {
        var that = this;
        var loginUserInfo = utils.getLoginUserInfo(true);
        that.userId = loginUserInfo.userId;

        $("#myFile1").change(function() {
            alert($(this)[0].files[0].content)
                //创建blob对象，浏览器将文件放入内存中，并生成标识
            var img_src = URL.createObjectURL($(this)[0].files[0]);
            //给img标检的src赋值
            document.getElementById("preview_img").src = img_src;
            //URL.revokeObjectURL(img_src);// 手动 回收，
        });
        // 案源类型
        rest.post({
            url: 'systemConfig/caseList',
            success: function(result) {
                that.caseList = result.data;
            }
        });
        //免费咨询次数
        rest.post({
            hasPage: false,
            url: 'advice/adviceCount/' + loginUserInfo.userId,
            success: function(result) {
                that.adviceCount = result.data;
            }
        });
    },

    methods: {
        // 上传照片
        onRead: function(file) {
            var that = this;
            that.uploadImg(1, file);
        },
        onRead1: function(file) {
            var that = this;
            that.uploadImg(2, file);
        },

        onRead2: function(file) {
            var that = this;
            that.uploadImg(3, file);
        },

        uploadImg: function(type, file) {
            var that = this;
            that.ImgBase64Str = file.content;

            rest.post({
                url: 'file/uploadImg',
                data: { imgBase64Str: that.ImgBase64Str },
                success: function(result) {
                    if (type == 1) {
                        that.workImg1 = utils.constants.apiUrl + result.data;
                        if (that.caseImgs.length > 0) {
                            that.caseImg1 = ',' + result.data;
                        } else {
                            that.caseImg1 = result.data;
                        }
                    } else if (type == 2) {
                        that.workImg2 = utils.constants.apiUrl + result.data;
                        if (that.caseImgs.length > 0) {
                            that.caseImg2 = ',' + result.data;
                        } else {
                            that.caseImg2 = result.data;
                        }
                    } else if (type == 3) {
                        that.workImg3 = utils.constants.apiUrl + result.data;
                        that.caseImgs = result.data;
                        if (that.caseImgs.length > 0) {
                            that.caseImg3 = ',' + result.data;
                        } else {
                            that.caseImg3 = result.data;
                        }
                    }
                    that.caseImgs = result.data;

                },
                fail: function(result) {
                    mui.toast(result.msg);
                }
            });
        },


        // 提交审核
        saveFn: function() {
            var that = this;
            // 基础验证
            that.caseImgs = that.caseImg1 + that.caseImg2 + that.caseImg3;
            alert(that.caseImgs);

            // 基础验证
            if (that.title == '') {
                mui.toast('请输入咨询标题');
                return;
            } else if (that.adviceContent == '') {
                mui.toast("请输入咨询内容");
                return;
            }

            //开始提交
            rest.post({
                isNeedToken: false,
                // url: 'user/companyAuthentication',
                url: 'advice/sendAdvice',
                data: {
                    userId: that.userId,
                    adviceContent: that.adviceContent,
                    title: that.title,
                    isofficial: 1, //是否属于官方发布 1用户 0 官方
                    reward: that.reward,
                    caseType: that.caseId,
                    imgs: that.caseImgs

                },
                success: function(result) {
                    if (that.reward == 0 || result.data.businessOrder == '') {
                        // 跳转到法律咨询页面
                        window.location.href = 'Lawask.html';
                    } else {
                        creatMywebview('Order-pay.html?businessOrder=' + result.data.businessOrder, 'Order-pay.html?businessOrder=' + result.data.businessOrder);
                        // utils.jumpPay(result.data.businessOrder, function() {
                        //     location.replace('user_consult_success.html?articleId=' + result.data.adviceId)
                        // });
                    }
                },
                fail: function(result) {
                    //用户名已存在
                    //验证码错误
                }
            });
        }
    }

});