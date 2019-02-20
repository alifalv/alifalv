/*!
 * alilaw - 阿里法律
 * @author Li Hongwei
 * @version v1.0.0
 * @link https://www.yimicat.com
 */
define(function(require, exports, module) { "use strict";
    $(function() { var u, g = ["jpg", "png", "bmp", "jpeg"],
            d = 10485760;
        $(".file").change(function() { var e = $(this).attr("id"),
                n = document.getElementById(e),
                t = $(this).parents(".z_photo"),
                i = n.files,
                a = ($(this).parent(), []),
                s = t.find(".up-section").length,
                o = s + i.length; if (5 < i.length || 5 < o) alert("上传图片数目不可以超过5个，请重新选择");
            else if (s < 5) { i = function(e) { for (var n, t = [], i = 0; n = e[i]; i++) { var a = n.name.split("").reverse().join(""); if (null != a.split(".")[0]) { var s = a.split(".")[0].split("").reverse().join("");
                            console.log(s + "===type==="), -1 < jQuery.inArray(s, g) ? n.size >= d ? (alert(n.size), alert('您这个"' + n.name + '"文件大小过大')) : t.push(n) : alert('您这个"' + n.name + '"上传类型不符合') } else alert('您这个"' + n.name + '"没有类型, 无法识别') } return t }(i); for (var p = 0; p < i.length; p++) { var l = window.URL.createObjectURL(i[p]);
                    a.push(l); var r = $("<section class='up-section fl loading'>");
                    t.prepend(r), $("<span class='up-span'>").appendTo(r), $("<img class='close-upimg'>").on("click", function(e) { e.preventDefault(), e.stopPropagation(), $(".works-mask").show(), u = $(this).parent() }).attr("src", "../img/a7.png").appendTo(r); var c = $("<img class='up-img up-opcity'>");
                    c.attr("src", a[p]), c.appendTo(r), $("<p class='img-name-p'>").html(i[p].name).appendTo(r), $("<input id='taglocation' name='taglocation' value='' type='hidden'>").appendTo(r), $("<input id='tags' name='tags' value='' type='hidden'/>").appendTo(r) } }
            setTimeout(function() { $(".up-section").removeClass("loading"), $(".up-img").removeClass("up-opcity") }, 450), 5 <= (s = t.find(".up-section").length) && $(this).parent().hide() }), $(".z_photo").delegate(".close-upimg", "click", function() { $(".works-mask").show(), u = $(this).parent() }), $(".wsdel-ok").click(function() { $(".works-mask").hide(), u.siblings().length < 6 && u.parent().find(".z_file").show(), u.remove() }), $(".wsdel-no").click(function() { $(".works-mask").hide() }) }) });