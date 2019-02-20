/*!
 * alilaw - 阿里法律
 * @author Li Hongwei
 * @version v1.0.0
 * @link https://www.yimicat.com
 */
define(function(require, exports, module) { "use strict"; var d, m = require("utils");
    (d = jQuery).fn.extend({ createFileUpload: function(f, e) { var i, u = 5; if (null != d("#maxImageNum").val() && (u = d("#maxImageNum").val()), "object" == typeof f) { u = f.imageNum, d(this).attr("id"); var h = { fileType: ["doc", "docx", "pdf", "jpg", "png"], fileSize: 10485760, count: 0 }; if (null != f.fileUrl && "" != f.fileUrl && 0 < f.fileUrl.length) { f.isShowAddBtn && f.fileUrl.length < f.imageNum ? d(this).parent().show() : d(this).parent().hide(); var l = [],
                        t = d(this).parents(".z_photo");
                    f.fileUrl instanceof Array ? l = l.concat(f.fileUrl) : "string" == typeof f.fileUrl && l.push(f.fileUrl); for (var n = 0; n < l.length; n++) { var a = d("<section class='up-section fl loading'>");
                        t.children(".z_file").before(a), d("<i class='close-upimg'></i>").on("click", function(e) { return 1 == d("<i class='close-upimg'></i>").length && d(this).parent().siblings(".z_file").show(), d(".works-mask").show(), d(this).parent().remove(), !1 }).appendTo(a); var s = d("<a class='up-img up-opcity ui-height-100p ui-block file-look' target='_blank' title='单击查看'></a>");
                        s.attr("href", m.createFileUrl(l[n])), s.appendTo(a); var o = '<input type="hidden" name="' + f.formData.name + '" value="' + l[0] + '">';
                        a.append(o), a.removeClass("loading"), s.removeClass("up-opcity") } } if (f.success) { var r = f.success;
                    delete f.success } if (f.fail) { var p = f.fail;
                    delete f.fail } if (f.error) { var c = f.error;
                    delete f.error }
                d(this).change(function() { new FileReader; var e = d(this).attr("id"),
                        i = document.getElementById(e),
                        l = d(this).parents(".z_photo"),
                        t = i.files,
                        n = (d(this).parent(), []),
                        a = l.find(".up-section").length,
                        s = a + t.length; if (t.length > u || u < s) C.msg.fail("上传图片数目不可以超过" + u + "个，请重新选择");
                    else if (a < u) { t = function(e, i) { for (var l, t = [], n = 0; l = e[n]; n++) { var a = l.name.split("").reverse().join(""); if (null != a.split(".")[0]) { var s = a.split(".")[0].split("").reverse().join("");
                                    console.log(s + "===type==="), -1 < jQuery.inArray(s, i.fileType) ? l.size >= i.fileSize ? C.msg.fail('文件大小"' + l.name + '"超出10M限制！') : t.push(l) : C.msg.fail('您上传的"' + l.name + '"不符合上传类型') } else C.msg.fail('您上传的"' + l.name + '"无法识别类型') } return t }(t, h); for (var o = 0; o < t.length; o++) { var r = window.URL.createObjectURL(t[o]);
                            n.push(r); var p = d("<section class='up-section fl loading'>");
                            l.children(".z_file").before(p), d("<i class='close-upimg'></i>").on("click", function(e) { return 1 == d("<i class='close-upimg'></i>").length && d(this).parent().siblings(".z_file").show(), d(".works-mask").show(), d(this).parent().remove(), !1 }).appendTo(p); var c = d("<a class='up-img up-opcity ui-height-100p ui-block file-look' target='_blank' title='单击查看'>");
                            c.attr("href", n[o]), c.appendTo(p), g(f, t[o], p) } }
                    a = l.find(".up-section").length, u <= a && d(this).parent().hide(), d(this).val("") }), d(".z_photo").on(".close-upimg", "click", function(e) { return d(".works-mask").show(), d(this).parent().remove(), !1 }), d(".wsdel-ok").click(function(e) { e.preventDefault(), e.stopPropagation(), d(".works-mask").hide(), i.siblings(".up-section").length < u + 1 && i.parent().find(".z_file").show(), i.remove() }), d(".wsdel-no").click(function() { d(".works-mask").hide() }) } else C.msg.fail("参数错误!");

            function g(l, e, t) { var i, n, a;
                d("#imguploadFinish").val(!1), l.url, (new FormData).append("file", e), i = e, n = function(e) { var i = { url: "file/uploadFile", data: { fileBase64Str: e }, success: function(e) { d(".up-section").removeClass("loading"), d(".up-img").removeClass("up-opcity"), d("#imguploadFinish").val(!0); var i = '<input type="hidden" name="' + l.formData.name + '" value="' + e.data + '">';
                            t.append(i), r && r(e) }, fail: function(e) { t.siblings(".up-section").length < u + 1 && t.parent().find(".z_file").show(), t.remove(), d("#imguploadFinish").val(!1), p && p(e) }, error: function() { t.siblings(".up-section").length < u + 1 && t.parent().find(".z_file").show(), t.remove(), d("#imguploadFinish").val(!1), c && c("上传失败，请联系管理员！") } };
                    rest.post(i) }, (a = new FileReader).readAsDataURL(i), a.onload = function(e) { console.log(e.target.result), n(e.target.result) } } } }) });