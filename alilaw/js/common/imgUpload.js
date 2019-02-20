/*!
 * alilaw - 阿里法律
 * @author Li Hongwei
 * @version v1.0.0
 * @link https://www.yimicat.com
 */
define(function(require, exports, module) {
    "use strict";
    var f, d = require("utils");
    (f = jQuery).fn.extend({
        createImgUpload: function(u, e) {
            var i, m = 5;
            if (null != f("#maxImageNum").val() && (m = f("#maxImageNum").val()), "object" == typeof u) {
                m = u.imageNum, f(this).attr("id");
                var g = { fileType: ["jpg", "png", "bmp", "jpeg", "JPG", "PNG", "JPEG", "BMP"], fileSize: 4194304, count: 0 };
                if (null != u.imgUrl && "" != u.imgUrl && 0 < u.imgUrl.length) {
                    u.isShowAddBtn && u.fileUrl.length < u.imageNum ? f(this).parent().show() : f(this).parent().hide();
                    var n = [],
                        t = f(this).parents(".z_photo");
                    u.imgUrl instanceof Array ? n = n.concat(u.imgUrl) : "string" == typeof u.imgUrl && n.push(u.imgUrl);
                    for (var a = 0; a < n.length; a++) {
                        var s = f("<section class='up-section fl loading'>");
                        t.children(".z_file").before(s), f("<i class='close-upimg'></i>").on("click", function(e) { return 1 == f("<i class='close-upimg'></i>").length && f(this).parent().siblings(".z_file").show(), f(".works-mask").show(), f(this).parent().remove(), !1 }).appendTo(s);
                        var l = f("<img class='up-img up-opcity'>");
                        l.attr("src", d.createImgUrl(n[a])), l.appendTo(s);
                        var r = '<input type="hidden" name="' + u.formData.name + '" value="' + n[0] + '">';
                        s.append(r), s.removeClass("loading"), l.removeClass("up-opcity")
                    }
                }
                if (u.success) {
                    var o = u.success;
                    delete u.success
                }
                if (u.fail) {
                    var p = u.fail;
                    delete u.fail
                }
                if (u.error) {
                    var c = u.error;
                    delete u.error
                }
                f(this).change(function() {
                    new FileReader;
                    var e = f(this).attr("id"),
                        i = document.getElementById(e),
                        n = f(this).parents(".z_photo"),
                        t = i.files,
                        a = (f(this).parent(), []),
                        s = n.find(".up-section").length,
                        l = s + t.length;
                    if (t.length > m || m < l) C.msg.fail("上传图片数目不可以超过" + m + "个，请重新选择");
                    else if (s < m) {
                        t = function(e, i) {
                            for (var n, t = [], a = 0; n = e[a]; a++) {
                                var s = n.name.split("").reverse().join("");
                                if (null != s.split(".")[0]) {
                                    var l = s.split(".")[0].split("").reverse().join("");
                                    console.log(l + "===type==="), -1 < jQuery.inArray(l, i.fileType) ? n.size >= i.fileSize ? C.msg.fail('文件大小"' + n.name + '"超出4M限制！') : t.push(n) : C.msg.fail('您上传的"' + n.name + '"不符合上传类型')
                                } else C.msg.fail('您上传的"' + n.name + '"无法识别类型')
                            }
                            return t
                        }(t, g);
                        for (var r = 0; r < t.length; r++) {
                            var o = window.URL.createObjectURL(t[r]);
                            a.push(o);
                            var p = f("<section class='up-section fl loading'>");
                            n.children(".z_file").before(p), f("<i class='close-upimg'></i>").on("click", function(e) { return 1 == f("<i class='close-upimg'></i>").length && f(this).parent().siblings(".z_file").show(), f(".works-mask").show(), f(this).parent().remove(), !1 }).appendTo(p);
                            var c = f("<img class='up-img up-opcity'>");
                            c.attr("src", a[r]), c.appendTo(p), h(u, t[r], p)
                        }
                    }
                    s = n.find(".up-section").length, m <= s && f(this).parent().hide(), f(this).val("")
                }), f(".z_photo").on(".close-upimg", "click", function(e) { return f(".works-mask").show(), f(this).parent().remove(), !1 }), f(".wsdel-ok").click(function(e) { e.preventDefault(), e.stopPropagation(), f(".works-mask").hide(), i.siblings(".up-section").length < m + 1 && i.parent().find(".z_file").show(), i.remove() }), f(".wsdel-no").click(function() { f(".works-mask").hide() })
            } else C.msg.fail("参数错误!");

            function h(n, e, t) {
                var i, a, s, l;
                f("#imguploadFinish").val(!1), n.url, (new FormData).append("file", e), i = e, a = function(e) {
                    rest.post({
                        url: "file/uploadImg",
                        data: { imgBase64Str: e },
                        success: function(e) {
                            f(".up-section").removeClass("loading"), f(".up-img").removeClass("up-opcity"), f("#imguploadFinish").val(!0);
                            var i = '<input type="hidden" name="' + n.formData.name + '" value="' + e.data + '">';
                            t.append(i), o && o(e)
                        },
                        fail: function(e) { t.siblings(".up-section").length < m + 1 && t.parent().find(".z_file").show(), t.remove(), f("#imguploadFinish").val(!1), p && p(e) },
                        error: function() { t.siblings(".up-section").length < m + 1 && t.parent().find(".z_file").show(), t.remove(), f("#imguploadFinish").val(!1), c && c("上传失败，请联系管理员！") }
                    })
                }, s = new Image, (l = new FileReader).onload = function() {
                    var e = l.result;
                    s.src = e
                }, s.onload = function() {
                    s.width, s.height;
                    var e = document.createElement("canvas"),
                        i = e.getContext("2d");
                    e.width = s.width, e.height = s.height, i.drawImage(s, 0, 0, s.width, s.height);
                    var n = e.toDataURL();
                    a(n)
                }, l.readAsDataURL(i)
            }
        }
    })
});