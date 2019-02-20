/*!
 * alilaw - 阿里法律
 * @author Li Hongwei
 * @version v1.0.0
 * @link https://www.yimicat.com
 */
define(["jquery", "template"], function($, template) {
    var core = function(t) {
            var e = function() { this.$ = t, this.options = { version: "1.0.0", clickEvent: t == window.Zepto ? "click" : "ontouchstart" in window ? "tap" : "click", coreReadyEvent: "coreready", readyEvent: "ready", complete: !1, crossDomainHandler: null, viewSuffix: ".html", usePageParam: !0 } },
                r = {};
            e.prototype.register = function(e, t) {
                (this[e] = t).launch && (r[e] = t.launch)
            };
            var o = function() {
                for (var e in r) try { r[e]() } catch (e) { console.log(e) }
                core.options.complete = !0, t(document).trigger(core.options.coreReadyEvent)
            };
            e.prototype.launch = function(e) {
                if (1 != core.options.complete) {
                    a.apply(this), t.extend(this.options, e);
                    this.options.readyEvent ? t(document)[this.options.readyEvent] ? t(document)[this.options.readyEvent](o) : t(document).on(this.options.readyEvent, o) : o()
                }
            };
            var a = function() {
                var e = core.util.parseURL(location.href),
                    t = (core.JSON.stringify(e.getQueryobject()), location.hash.replace("#", ""));
                this.pageInfo = core.util.decodeHash(t)
            };
            return e.prototype.noConflict = function() { return this }, window.C ? new e : window.C = new e
        }(window.Zepto || window.jQuery || window.$),
        o, p, q, ca, da, ea, gc, hc, zc, ff;
    return o = core.$, p = {
            default: {
                selector: '[data-toggle="view"]',
                handler: function(e, t) {
                    $el = o(t);
                    var r = $el.data("toggle"),
                        a = core.util.parseURL(e),
                        n = a.getHashobject(),
                        i = p[r] || {},
                        c = o(n.tag),
                        l = o(i.container);

                    function s(e, t) {
                        var r = e.data("role") || "",
                            o = function(e) { e.hasClass("active") || e.addClass("active"), e.attr("__init_controller__") || (e.attr("__init_controller__", !0), e.trigger(r + "load")), e.trigger(r + "show") },
                            a = function(e) { e.removeClass("active").trigger(r + "hide") };
                        i.isToggle || $el.data("istoggle") ? e.hasClass("active") ? a(e) : o(e) : (o(e), t && 0 < t.length && a(t))
                    }

                    function u(e) {
                        var t = e.data("transition") || i.transition;
                        t && e.data("transition", t)
                    }

                    function d() {
                        var e = c.data("role"),
                            t = e ? '[data-role="' + e + '"].active' : ".active";
                        if (c.data("url", a.getURL()), a.getQueryobject() && c.data("params", core.JSON.stringify(a.getQueryobject())), c.hasClass("active")) s(c), i.complete && i.complete(c, { result: "thesame" });
                        else {
                            var r = c.siblings(t);
                            u(r), u(c), core.anim.change(r, c, !1, function() { s(c, r) }), i.complete && i.complete(c, { result: "success" })
                        }
                    }
                    if (0 == c.length) {
                        if (0 == l.length) return void(i.complete && i.complete(c, { result: "nocontainer" }));
                        if ($el.attr("data-useTemplate")) return c = o(i.template.replace("{id}", n.tag.replace("#", ""))), l.append(c), void d();
                        core.options.showPageLoading && core.showMask(), core.util.getHTML(a.getURL(), function(e) { core.options.showPageLoading && core.hideMask(), e ? (c = o(e), l.append(c), c = o(n.tag), d()) : i.complete && i.complete(c, { result: "requesterror" }) })
                    } else d()
                }
            },
            modal: { selector: '[data-toggle="modal"]', container: "body", template: '<div id="{id}" data-role="modal" class="modal"></div>', isToggle: !0 }
        }, q = { add: function(e) { o.extend(p, e) }, get: function(e) { return e ? p[e] : p } }, q.launch = function() {
            ! function() {
                for (var e in p) ! function(i) {
                    q[i] = function(e, t) {
                        var r = i,
                            a = o(t || '<a data-toggle="' + i + '" href="' + e + '"></a>'),
                            n = p[r] && p[r].handler ? r : "default";
                        p[n].handler(e, a)
                    }, o(document).on(core.options.clickEvent, p[i].selector, function() {
                        o("input:focus,textarea:focus").blur();
                        var e = o(this).data("toggle"),
                            t = o(this).attr("href") || "#";
                        return (q[e] || q.default)(t, o(this)), !1
                    })
                }(e)
            }()
        }, core.register("Controller", q), ca = core.$, da = {}, ea = {
            default: {
                selector: '[data-role="view"]',
                handler: function(e, t) {
                    var r = ca(e);
                    t = "default" == t ? r.data("role") : t;
                    var o = ea[t] || {};
                    if (o.isToggle || r.data("istoggle")) r[r.hasClass("active") ? "removeClass" : "addClass"]("active");
                    else if (!r.hasClass("active")) {
                        var a, n = '[data-role="' + t + '"].active';
                        a = o.container ? r.parents(o.container).first().find(n) : r.siblings(n + ".active"), r.addClass("active"), a.removeClass("active")
                    }
                }
            },
            tab: {
                selector: '[data-role="tab"]',
                handler: function(e, t) {
                    var r = ca(e).data("toggle");
                    r && "section" != r && "modal" != r && "html" != r && ea.default.handler(e, t)
                }
            },
            scroll: {
                selector: '[data-role="article"].active',
                event: "articleload",
                handler: function(e, t) {
                    var r = { verticle: {}, horizontal: { scrollbars: !1, scrollX: !0, scrollY: !1, bindToWrapper: !0 }, scroll: { scrollX: !0, scrollY: !0 }, pulldown: {}, pullup: {}, pull: {} },
                        o = function(e) {
                            var t = r[e.data("scroll")];
                            core.Scroll("#" + e.attr("id"), t).refresh()
                        },
                        a = function(e) { core.Refresh("#" + e.attr("id"), r[e.data("scroll")]).refresh() },
                        n = ca(e || this),
                        i = n.data("scroll");
                    "verticle" != i && "horizontal" != i && "scroll" != i || o(n), "pulldown" != i && "pullup" != i && "pull" != i || a(n);
                    for (var c = n.find('[data-scroll="verticle"],[data-scroll="horizontal"],[data-scroll="scroll"]'), l = 0; l < c.length; l++) ! function(e) { setTimeout(function() { o(e) }, 100) }(ca(c[l]));
                    c = n.find('[data-scroll="pulldown"],[data-scroll="pullup"],[data-scroll="pull"]');
                    for (l = 0; l < c.length; l++) ! function(e) { setTimeout(function() { a(e) }, 100) }(ca(c[l]))
                }
            },
            formcheck: {
                selector: '[data-role="article"].active',
                event: "articleload",
                handler: function(e, t) {
                    var r = ca(e),
                        o = function(e) { e.removeAttr("href") };
                    if ("checkbox" == r.data("role") || "radio" == r.data("role")) o(r);
                    else
                        for (var a = r.find('[data-role="checkbox"],[data-role="radio"]'), n = 0; n < a.length; n++) o(ca(a[n]))
                }
            },
            toggle: {
                selector: '[data-role="article"].active',
                event: "articleload",
                handler: function(e, t) {
                    var r = ca(e),
                        o = function(e, t) {
                            var r = t.data("on-value"),
                                o = t.data("off-value"),
                                a = e ? r : o;
                            return void 0 === a ? "" : a
                        },
                        a = function(r) {
                            var e = r.hasClass("active");
                            if (0 < r.find("div.toggle-handle").length) return r[e ? "removeClass" : "addClass"]("active"), void r.find('input[data-com-refer="toggle"]').val(o(!e, r));
                            var t = r.data("name");
                            r.append('<input type="hidden" data-com-refer="toggle" ' + (t ? 'name="' + t + '" ' : "") + ' value="' + o(e, r) + '"/>'), r.append('<div class="toggle-handle"></div>'), r.on(core.options.clickEvent, function() {
                                var e = ca(this),
                                    t = o(!r.hasClass("active"), r);
                                return e.toggleClass("active").trigger("datachange", [t]), e.find("input").val(t), !1
                            })
                        };
                    if ("toggle" == r.data("role")) a(r);
                    else
                        for (var n = r.find('[data-role="toggle"]'), i = 0; i < n.length; i++) a(ca(n[i]))
                }
            },
            lazyload: {
                selector: '[data-role="article"].active',
                event: "articleload",
                handler: function(e, t) {
                    var r = ca(e),
                        o = function(t) {
                            var e = t.attr("placeholder") || core.options.lazyloadPlaceholder;
                            !t.attr("src") && e && t.attr("src", e);
                            var r = core.util.script(t.data("source")),
                                o = t.offset().top,
                                a = ca(window).height() + 100;
                            if (!(o < 0 || a < o))
                                if ("base64" == t.data("type")) core.ajax({ url: r, success: function(e) { i(t, e) }, isBlock: !1 });
                                else {
                                    var n = new Image;
                                    n.src = r, n.complete ? (i(t, r), n = null) : (n.onload = function() { i(t, r), n = null }, n.onerror = function() { i(t, e || ""), n = null })
                                }
                        },
                        i = function(t, e) {
                            t.data("source") && (core.anim.run(t, "fadeOut", function() {
                                t.css("opacity", "0"), t[0].onload = function() {
                                    core.anim.run(t, "fadeIn", function() {
                                        t.css("opacity", "1");
                                        var e = t.closest('[data-role="slider"]');
                                        e.hasClass("active") && core.Slider(e).refresh(), core.Component.scroll(t.closest("[data-scroll]"))
                                    })
                                }, t.attr("src", e)
                            }), t.removeAttr("data-source"))
                        };
                    if (r.data("source")) o(r);
                    else
                        for (var a = r.find("img[data-source]"), n = 0; n < a.length; n++) o(ca(a[n]))
                }
            },
            scrollTop: {
                selector: '[data-role="article"].active',
                event: "articleload",
                handler: function(e, t) {
                    var r = function(e) {
                            e.on(core.options.clickEvent, function() { e.removeClass("active") });
                            var t, r = ca(e.attr("href"));
                            r.data("scroll") ? (t = core.Scroll(e.attr("href"))).on("scrollEnd", function() { e[this.y < -120 ? "addClass" : "removeClass"]("active") }) : ((t = r).on("scrollEnd", function() { e[120 < t.scrollTop() ? "addClass" : "removeClass"]("active") }), core.util.jquery.scrollEnd(t))
                        },
                        o = ca(e);
                    if ("scrollTop" == o.data("role")) r(o);
                    else
                        for (var a = o.find('[data-role="scrollTop"]'), n = 0; n < a.length; n++) r(ca(a[n]))
                }
            },
            pictureShow: {
                type: "function",
                handler: function(t) {
                    var e = t.id,
                        r = t.index || 0,
                        o = t.list,
                        a = [];
                    a.push('<section id="' + e + '" data-role="section" data-cache="false" style="background:#000;">'), a.push('<header class="picture_show_header">'), a.push(t.header || '<a data-toggle="back" href="#" style="float:right;color:#fff;text-align:center; padding:8px; font-size:18px;">X</a>'), a.push("</header>"), a.push('<article id="' + e + '_article" data-role="article" class="active" style="overflow:hidden;background:rgba(255, 255, 255, 0.1) none repeat scroll 0 0 !important;">'), a.push('<div id="' + e + '_slide" data-role="slider" class="full" style="overflow: hidden;"><div class="scroller">');
                    for (var n = 0; n < o.length; n++) a.push('<div class="slide" style="padding-top:100px;">'), a.push('<img data-source="' + o[n].imgURL + '" class="full-width"/>'), a.push("</div>");
                    a.push("</div></div>"), a.push("</article>"), a.push('<div class="picture_show_footer" style="position:relative;bottom:0px;left:0px;right:0px;padding:8px;color:#fff;min-height:140px;background:#111;">'), a.push('<div><span style="float:left;font-size:20px;">' + t.title + '</span><p class="picture_show_num" style="float:right;"></p></div>'), a.push('<div style="clear:both;line-height:20px;"><span class="picture_show_content"></span></div>'), a.push("</div>"), a.push("</section>"), ca("#section_container").append(a.join("")), core.Controller.section("#" + e);
                    var i = ca("#" + e + " .picture_show_num"),
                        c = ca("#" + e + " .picture_show_content"),
                        l = ca("#" + e + " .picture_show_header"),
                        s = ca("#" + e + " .picture_show_footer");
                    core.Slider("#" + e + "_slide", { dots: "hide", change: function(e) { i.html('<span style="font-size:18px;">' + (e + 1) + "</span>/" + o.length), c.html(o[e].content || t.title) } }).index(r), ca("#" + e + "_article").on(core.options.clickEvent, function() { return l.toggle(), s.toggle(), !1 })
                }
            }
        }, da.add = function(e) { ca.extend(ea, e) }, da.get = function(e) { return e ? ea[e] : ea }, da.initComponents = function(e) { var t = ca(e); for (var r in ea) "function" != ea[r].type && "default" != r && core.Component[r](t) }, _makeHandler = function() {
            for (var e in ea) "function" != ea[e].type ? function(o) {
                da[o] = function(e) {
                    var t = o,
                        r = ea[t] && ea[t].handler ? t : "default";
                    return ea[r].handler(e, t)
                }, ea[o].selector && ca(document).on(ea[o].event || core.options.clickEvent, ea[o].selector, function() { var e = ea[o].handler ? o : "default"; return ea[e].handler(this, o), !1 })
            }(e) : da[e] = ea[e].handler
        }, da.params = function(e) { return core.JSON.parse(ca(e).data("params")) || {} }, da.isInit = function(e) { return !!e.data("com-init") || (e.data("com-init", "init"), !1) }, da.getObject = function(e, t) {
            var r = ca(t),
                o = {},
                a = da.get(e);
            if (!a || !a.extend) return o;
            var n = a.extend;
            for (var i in n) ! function(e) { o[e] = function() { return n[e].apply(r, arguments) } }(i);
            return o
        }, da.launch = function() { _makeHandler() }, core.register("Component", da), gc = core.$, hc = {
            classes: {
                empty: [
                    ["empty", "empty"],
                    ["empty", "empty"]
                ],
                slide: [
                    ["slideLeftOut", "slideLeftIn"],
                    ["slideRightOut", "slideRightIn"]
                ],
                cover: [
                    ["", "slideLeftIn"],
                    ["slideRightOut", ""]
                ],
                slideUp: [
                    ["", "slideUpIn"],
                    ["slideDownOut", ""]
                ],
                slideDown: [
                    ["", "slideDownIn"],
                    ["slideUpOut", ""]
                ],
                popup: [
                    ["", "scaleIn"],
                    ["scaleOut", ""]
                ],
                flip: [
                    ["slideLeftOut", "flipOut"],
                    ["slideRightOut", "flipIn"]
                ]
            },
            addClass: function(e) { gc.extend(hc.classes, e || {}) },
            run: function(e, t, r) {
                e = gc(e);
                var o = "webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend";
                0 != e.length ? "object" != typeof t ? (t = (t || "empty") + " anim", e.addClass(t).one(o, function() { e.removeClass(t), r && r(), e.unbind(o) })) : e.animate(t, 250, "linear", function() { r && r() }) : r && r()
            },
            css3: function(e, t, r) {
                e = gc(e);
                var o = "webkitTransitionEnd otransitionend transitionend";
                e.addClass("comm_anim").css(t).one(o, function() { e.removeClass("comm_anim"), r && r(), e.unbind(o) })
            },
            change: function(e, t, r, o) {
                var a = gc(e),
                    n = gc(t);
                if (a.length + n.length != 0) {
                    var i = n.data("transition");
                    if (hc.classes[i]) {
                        var c = hc.classes[i][r ? 1 : 0];
                        hc.run(a, c[0]), hc.run(n, c[1], function() { o && o() })
                    } else o && o()
                } else o && o()
            }
        }, core.register("anim", hc), zc = core.$, core.register("ajax", function(e) {
            var t = e.success,
                r = e.error;
            e.success = function(e) { t && t(e) }, e.error = function(e) { r && r() }, zc.ajax(e)
        }),
        function($) {
            var util = {};
            util.jquery = {
                scrollEnd: function(e) {
                    var t, r = function() { t && clearTimeout(t), t = null, t = setTimeout(function() { clearTimeout(t), t = null, e.trigger("scrollEnd") }, 100) };
                    e.off("scroll", r).on("scroll", r)
                }
            }, util.getCSSTransform = function(e) { var t = e.css("transform"); return t && "none" != t && console.log(t.replace(/matrix.([^\\)]+)./, "$1")), [] }, util.script = function(str) { str = (str || "").trim(); var tag = !1; return str = str.replace(/\$\{([^\}]*)\}/g, function(s, s1) { try { return tag = !0, eval(s1.trim()) } catch (e) { return "" } }), tag ? util.script(str) : str }, util.provider = function(e, r) { return (e = (e || "").trim()).replace(/\$\{([^\}]*)\}/g, function(e, t) { return r[t.trim()] || "" }) };
            var URLParser = function(e) {
                for (var t in this._fields = { Username: 4, Password: 5, Port: 7, Protocol: 2, Host: 6, Pathname: 8, URL: 0, Querystring: 9, Fragment: 10, Filename: -1, Queryobject: -1, Hashobject: -1 }, this._values = {}, this._regex = null, this.version = .1, this._regex = /^((\w+):\/\/)?((\w+):?(\w+)?@)?([^\/\?:]+):?(\d+)?(\/?[^\?#]+)?\??([^#]+)?#?(\w*)/, this._fields) this["get" + t] = this._makeGetter(t);
                void 0 !== e && this._parse(e)
            };
            URLParser.prototype.setURL = function(e) { this._parse(e) }, URLParser.prototype._initValues = function() { for (var e in this._fields) this._values[e] = "" };
            var removeURLPre = function(e) { return 0 == e.indexOf("file:") && (e = e.replace(/file:\/*/, "")), e };
            URLParser.prototype._path = function(e) {
                var t = new URLParser(removeURLPre(location.href.split(/\?\#/)[0])),
                    r = t.getProtocol() + "://" + t.getHost() + (t.getPort() ? ":" + t.getPort() : "");
                if (0 == (e = removeURLPre(e)).indexOf("/")) return r + e;
                var o = t.getPathname().split("/");
                return o.length -= e.split("../").length, r + o.join("/") + "/" + e.replace(/[\.]{1,2}\//g, "")
            }, URLParser.prototype._parse = function(e) {
                this._initValues(), e = removeURLPre(e);
                var t = this._regex.exec(e);
                if (t || (e = this._path(e), t = this._regex.exec(e)))
                    for (var r in this._fields) void 0 !== t[this._fields[r]] && (this._values[r] = t[this._fields[r]]);
                else console.log("url invalid:" + e)
            }, URLParser.prototype._makeGetter = function(n) {
                return function() {
                    if ("Filename" == n) { var e = this._values.URL.split("?")[0].split("/"); return e[e.length - 1].split(".")[0] }
                    if ("Queryobject" == n) {
                        for (var t = (this._values.Querystring || "").split("&"), r = {}, o = 0; o < t.length; o++) {
                            var a = t[o].split("=");
                            r[a[0]] = a[1]
                        }
                        return r
                    }
                    return "Hashobject" == n ? { url: this.getURL(), hash: "#" + (this.getFragment() || this.getFilename()), tag: "#" + (this.getFragment() || this.getFilename()), query: this.getQuerystring(), param: this.getQueryobject() } : this._values[n]
                }
            }, util.parseURL = function(e) { return 0 == (e = util.script(e || "")).indexOf("#") && (e = e.replace("#", "") + (core.options.viewSuffix ? core.options.viewSuffix : "")), new URLParser(e) }, util.isCrossDomain = function(e) { if (!e || e.indexOf(":") < 0) return !1; var t = util.parseURL(e); return !!t.getProtocol() && !(location.protocol.replace(":", "") + location.host == t.getProtocol() + t.getHost() + ":" + t.getPort()) }, util.checkBoolean = function() {
                for (var e = 0; e < arguments.length; e++)
                    if ("boolean" == typeof arguments[e]) return arguments[e];
                return !1
            };
            var _isCrossDomain = function(e) { if (!e || e.indexOf(":") < 0) return !1; var t = core.util.parseURL(e); return !!t.getProtocol() && !(location.protocol.replace(":", "") + location.host == t.getProtocol() + t.getHost() + ":" + t.getPort()) };
            util.ajax = function(t) {
                if (t && t.url) {
                    t.url = util.script(t.url);
                    var r = t.isBlock;
                    t.dataType = (t.dataType || "text").toLowerCase();
                    var e = { url: t.url, timeout: 2e4, type: t.type || "get", dataType: t.dataType, success: function(e) { r && core.hideMask(), t.success && t.success("text" == t.dataType ? util.script(e) : e) }, error: function(e) { r && core.hideMask(), t.error && t.error(null) }, reqCharset: t.reqCharset || "utf-8" };
                    t.data && (e.data = t.data), t.headers && (e.headers = t.headers);
                    var o = _isCrossDomain(t.url),
                        a = core.ajax;
                    o && (e.dataType = core.options.crossDomainHandler ? e.dataType : "jsonp", a = core.options.crossDomainHandler || a), "jsonp" == e.dataType.toLowerCase() && (e.jsonp = t.jsonp || "corecallback"), r && core.showMask(), a(e)
                }
            }, util.getHTML = function(e, t) { core.ajax({ url: e, type: "get", cache: !0, dataType: "text", success: function(e) { t && t(core.util.script(e || "")) }, error: t }) }, util.readyAlarm = function(e, r, o) { var t = {}; for (var a in e) "function" == typeof e[a] ? t[a] = function(t) { return function() { try { return e[t].apply(this, arguments) } catch (e) { console.log("提示", "请在" + (o || core.options.readyEvent) + "之后调用" + r + "." + t + "桥接函数") } } }(a) : t[a] = e[a]; return t }, util.encodeHash = function(e) { return e }, util.decodeHash = function(e) { return e }, core.register("util", util)
        }(core.$),
        function(u) {
            var t = function(e) { this.$el = u(e) };
            t.prototype.getTemplate = function(t) {
                var r = this.$el,
                    e = r.attr("src"),
                    o = r.html().trim();
                o ? t && t(o) : e ? core.util.getHTML(core.util.script(e), function(e) { r.text(e), t && t(e) }) : t && t("")
            }, t.prototype.render = function(t, o) {
                var a;
                if ("object" == typeof t && (a = t, t = ""), a || t) {
                    var e = this.$el.html().trim();
                    if (a && e) { var n = ""; try { n = template.compile(e)(a) } catch (e) {} return o && o(n, e, a), n }
                    this.getTemplate(function(r) {
                        if (t) core.util.ajax({
                            url: t,
                            dataType: "json",
                            success: function(e) {
                                var t = template.compile(r);
                                n = t(e), o && o(n, r, e)
                            },
                            error: function() { o && o() }
                        });
                        else {
                            var e = template.compile(r);
                            n = e(a), o && o(n, r, a)
                        }
                    })
                } else o && o()
            };
            var r = function(c, e, l) {
                var s = this.$el;
                this.render(e, function(e, t, r) {
                    var o = s,
                        a = (s.attr("id"), "#" + s.attr("id")),
                        n = s.parent().find('[__inject_dependence__="' + a + '"]');
                    "replace" == c ? n.remove() : "after" == c && (o = 0 == n.length ? s : n.last());
                    var i = u(e).attr("__inject_dependence__", a);
                    o.after(i), l && l(i, t, r), s.trigger("renderEnd", [i])
                })
            };
            t.prototype.load = function(e, o, a) { this.render(e, function(e, t, r) { o.html(e), a && a(o, t, r) }) }, t.prototype.loadBefore = function(e, o, a) { this.render(e, function(e, t, r) { o.before(e), a && a(o, t, r) }) }, t.prototype.loadAfter = function(e, o, a) { this.render(e, function(e, t, r) { o.after(e), a && a(o, t, r) }) }, t.prototype.loadAppend = function(e, o, a) { this.render(e, function(e, t, r) { o.append(e), a && a(o, t, r) }) }, t.prototype.renderReplace = function(e, t) { r.call(this, "replace", e, t) }, t.prototype.renderAfter = function(e, t) { r.call(this, "after", e, t) }, t.prototype.renderBefore = function(e, t) { r.call(this, "before", e, t) }, core.register("template", function(e) { return new t(e) })
        }(core.$),
        function() {
            var JSON = {
                parse: function(str) { try { return eval("(" + str + ")") } catch (e) { return null } },
                stringify: function(e) {
                    var t = [];
                    if ("string" == typeof e) return '"' + e.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + '"';
                    if (void 0 === e) return "";
                    if ("object" != typeof e) return e.toString();
                    if (null === e) return null;
                    if (e instanceof Array) {
                        for (var r = 0; r < e.length; r++) t.push(this.stringify(e[r]));
                        t = "[" + t.join() + "]"
                    } else {
                        for (var r in e) t.push('"' + r + '":' + this.stringify(e[r]));
                        t = "{" + t.join() + "}"
                    }
                    return t
                },
                toParams: function(e) {
                    var t = [];
                    for (var r in e) {
                        var o = [],
                            a = e[r];
                        a = a instanceof Array ? a : [a];
                        for (var n = 0; n < a.length; n++) o.push(a[n]);
                        t.push(r + "=" + o.join(","))
                    }
                    return t.join("&")
                }
            };
            core.register("JSON", JSON)
        }(),
        function(r) {
            function t(e, t) { this.obj = e, this.defaults = { isOpen: !0 }, this.options = r.extend({}, this.defaults, t), this.init() }
            t.prototype.init = function() { this.options.isOpen && this.show() }, t.prototype.show = function() { this.obj.find("[js-mask]").length < 1 && this.obj.append('<div js-mask style="position: absolute;top: 0;left: 0;width: 100%;height:100%;z-index: 19891018;"><div style="background-color: rgb(255, 255, 255); width:100%;height:100%;opacity: 0.5;"></div><div class="layui-layer layui-layer-loading" style="z-index: 19891019; position: absolute;top: 50%; left: 50%;margin-left: -18.5px;margin-top: -18.5px;"><div id="" class="layui-layer-content layui-layer-loading1"></div></div></div>') }, t.prototype.hide = function() { 0 < this.obj.find("[js-mask]").length && this.obj.find("[js-mask]").remove() };
            core.register("mask", function(e) { return new t(e) })
        }(core.$), ff = {
            table: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "/"],
            UTF16ToUTF8: function(e) {
                for (var t = [], r = e.length, o = 0; o < r; o++) {
                    var a = e.charCodeAt(o);
                    if (0 < a && a <= 127) t.push(e.charAt(o));
                    else if (128 <= a && a <= 2047) {
                        var n = 192 | a >> 6 & 31,
                            i = 128 | 63 & a;
                        t.push(String.fromCharCode(n), String.fromCharCode(i))
                    } else if (2048 <= a && a <= 65535) {
                        n = 224 | a >> 12 & 15, i = 128 | a >> 6 & 63;
                        var c = 128 | 63 & a;
                        t.push(String.fromCharCode(n), String.fromCharCode(i), String.fromCharCode(c))
                    }
                }
                return t.join("")
            },
            UTF8ToUTF16: function(e) {
                var t = [],
                    r = e.length,
                    o = 0;
                for (o = 0; o < r; o++) {
                    var a = e.charCodeAt(o);
                    if (0 == (a >> 7 & 255)) t.push(e.charAt(o));
                    else if (6 == (a >> 5 & 255)) {
                        var n = (31 & a) << 6 | 63 & (i = e.charCodeAt(++o));
                        t.push(Sting.fromCharCode(n))
                    } else { var i; if (14 == (a >> 4 & 255)) n = (255 & (a << 4 | (i = e.charCodeAt(++o)) >> 2 & 15)) << 8 | ((3 & i) << 6 | 63 & e.charCodeAt(++o)), t.push(String.fromCharCode(n)) }
                }
                return t.join("")
            },
            encode: function(e) {
                if (!e) return "";
                for (var t = this.UTF16ToUTF8(e), r = 0, o = t.length, a = []; r < o;) {
                    var n = 255 & t.charCodeAt(r++);
                    if (a.push(this.table[n >> 2]), r == o) { a.push(this.table[(3 & n) << 4]), a.push("=="); break }
                    var i = t.charCodeAt(r++);
                    if (r == o) { a.push(this.table[(3 & n) << 4 | i >> 4 & 15]), a.push(this.table[(15 & i) << 2]), a.push("="); break }
                    var c = t.charCodeAt(r++);
                    a.push(this.table[(3 & n) << 4 | i >> 4 & 15]), a.push(this.table[(15 & i) << 2 | (192 & c) >> 6]), a.push(this.table[63 & c])
                }
                return a.join("")
            },
            decode: function(e) { if (!e) return ""; for (var t = e.length, r = 0, o = []; r < t;) code1 = this.table.indexOf(e.charAt(r++)), code2 = this.table.indexOf(e.charAt(r++)), code3 = this.table.indexOf(e.charAt(r++)), code4 = this.table.indexOf(e.charAt(r++)), c1 = code1 << 2 | code2 >> 4, c2 = (15 & code2) << 4 | code3 >> 2, c3 = (3 & code3) << 6 | code4, o.push(String.fromCharCode(c1)), 64 != code3 && o.push(String.fromCharCode(c2)), 64 != code4 && o.push(String.fromCharCode(c3)); return this.UTF8ToUTF16(o.join("")) }
        }, core.register("Base64", { encode: function(e) { try { return ff.encode(e) } catch (e) { return "" } }, decode: function(e) { try { return ff.decode(e) } catch (e) { return "" } } }), C
});