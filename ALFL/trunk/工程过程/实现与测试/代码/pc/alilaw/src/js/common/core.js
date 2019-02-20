/*!
 * 核心
 * @author Li Hongwei
 * @date 2016-03-15 10:49
 * @version 1.0.0
 */
define(["jquery", "template"], function($, template) {
    var core = (function ($) {
        var Core = function () {
            this.$ = $;
            this.options = {
                version: '1.0.0',
                clickEvent: $ == window.Zepto ? 'click' : (('ontouchstart' in window) ? 'tap' : 'click'),
                coreReadyEvent: 'coreready',
                readyEvent: 'ready', //宿主容器的准备事件，默认是document的ready事件
                complete: false, //是否启动完成
                crossDomainHandler: null, //跨域请求的处理类
                viewSuffix: '.html', //加载静态文件的默认后缀
                usePageParam: true
            };
        };

        var _launchMap = {};

        /**
         * 注册Core框架的各个部分，可扩充
         * @param {String} 控制器的唯一标识
         * @param {Object} 任意可操作的对象，建议面向对象方式返回的对象
         */
        Core.prototype.register = function (key, obj) {
            this[key] = obj;
            if (obj.launch) {
                _launchMap[key] = obj.launch;
            }
        };
        var _doLaunch = function () {
            for (var k in _launchMap) {
                try {
                    _launchMap[k]();
                } catch (e) {
                    console.log(e);
                }
            }
            core.options.complete = true;
            $(document).trigger(core.options.coreReadyEvent);
        };

        /**
         * 启动Core
         * @param {Object} 要初始化的一些参数
         */
        Core.prototype.launch = function (opts) {
            if (core.options.complete == true) return;
            _initPageInfo.apply(this);
            $.extend(this.options, opts);
            var _this = this;
            if (!this.options.readyEvent) {
                _doLaunch();
            } else if ($(document)[this.options.readyEvent]) {
                $(document)[this.options.readyEvent](_doLaunch);
            } else {
                $(document).on(this.options.readyEvent, _doLaunch);
            }
        };

        var _initPageInfo = function () {
            var urlObj = core.util.parseURL(location.href);
            var params = core.JSON.stringify(urlObj.getQueryobject());
            var hash = location.hash.replace('#', '');
            this.pageInfo = core.util.decodeHash(hash);
        };

        Core.prototype.noConflict = function () {
            return this;
        };

        return window.C ? new Core() : window.C = new Core();
    })(window.Zepto || window.jQuery || window.$);

//控制器
    (function ($) {
        /**
         * 控制器的基本结构，形如{key:{selector:'控制器选择器'，默认为body',handler:function($trigger){}}}
         * selector为选择器，handler为处理函数
         * @private
         */
        var _controllers = {
            default: {//默认控制器
                selector: '[data-toggle="view"]',
                handler: function (hash, el) {
                    $el = $(el);
                    var toggleType = $el.data('toggle');
                    var urlObj = core.util.parseURL(hash);
                    var hashObj = urlObj.getHashobject();
                    var controllerObj = _controllers[toggleType] || {};
                    var $target = $(hashObj.tag);
                    var $container = $(controllerObj.container);

                    function _event($target, $current) {
                        var targetRole = $target.data('role') || '';
                        var show = function ($el) {
                            if (!$el.hasClass('active')) $el.addClass('active');
                            if (!$el.attr('__init_controller__')) {
                                $el.attr('__init_controller__', true);
                                $el.trigger(targetRole + 'load');
                            }
                            $el.trigger(targetRole + 'show');
                        };

                        var hide = function ($el) {
                            $el.removeClass('active').trigger(targetRole + 'hide');
                        };
                        if (controllerObj.isToggle || $el.data('istoggle')) {
                            if ($target.hasClass('active')) {
                                hide($target);
                            } else {
                                show($target);
                            }
                            return;
                        }

                        show($target);
                        if ($current && $current.length > 0) {
                            hide($current);
                        }
                    }

                    function _setDefaultTransition($el) {
                        var targetTransition = $el.data('transition') || controllerObj.transition;
                        if (targetTransition) $el.data('transition', targetTransition);
                    }

                    function _next() {
                        var targetRole = $target.data('role');
                        var toggleSelector = targetRole ? '[data-role="' + targetRole + '"].active' : '.active';
                        $target.data('url', urlObj.getURL());
                        if (urlObj.getQueryobject()) $target.data('params', core.JSON.stringify(urlObj.getQueryobject()));
                        if ($target.hasClass('active')) {
                            _event($target);
                            controllerObj.complete && controllerObj.complete($target, {result: 'thesame'});
                        } else {
                            var $current = $target.siblings(toggleSelector);
                            _setDefaultTransition($current);
                            _setDefaultTransition($target);
                            core.anim.change($current, $target, false, function () {
                                _event($target, $current);
                            });
                            controllerObj.complete && controllerObj.complete($target, {result: 'success'});
                        }
                    }

                    if ($target.length == 0) {
                        if ($container.length == 0) {
                            controllerObj.complete && controllerObj.complete($target, {result: 'nocontainer'});
                            return;
                        }
                        ;
                        if ($el.attr('data-useTemplate')) {
                            $target = $(controllerObj.template.replace('{id}', hashObj.tag.replace('#', '')));
                            $container.append($target);
                            _next();
                            return;
                        }
                        if (core.options.showPageLoading) core.showMask();
                        core.util.getHTML(urlObj.getURL(), function (html) {
                            if (core.options.showPageLoading) core.hideMask();
                            if (!html) {
                                controllerObj.complete && controllerObj.complete($target, {result: 'requesterror'});
                                return;
                            }
                            $target = $(html);
                            $container.append($target);
                            $target = $(hashObj.tag);
                            _next();
                        });
                    } else {
                        _next();
                    }

                }
            },
            modal: {
                selector: '[data-toggle="modal"]',
                container: 'body',
                template: '<div id="{id}" data-role="modal" class="modal"></div>',
                isToggle: true
            }
        };

        var controller = {};//定义控制器
        /**
         * 添加控制器
         * @param {Object} 控制器对象，形如{key:{selector:'',handler:function(){}}}
         */
        controller.add = function (c) {
            $.extend(_controllers, c);
        };

        /**
         * 获取全部控制器
         * @param {String} 控制器的key，如果有key则返回当前key的控制器，没有key则返回全部控制器
         */
        controller.get = function (key) {
            return key ? _controllers[key] : _controllers;
        };

        /**
         * 为所有控制器创建调用方法
         * @private 只能初始化一次，启动后添加的不生效
         */
        var _makeHandler = function () {
            for (var k in _controllers) {
                (function (k) {
                    //定义JS调用函数
                    controller[k] = function (hash, el) {
                        var toggleType = k;
                        var $el = el ? $(el) : $('<a data-toggle="' + k + '" href="' + hash + '"></a>');
                        // var $el = el?$(el):$('[data-toggle="'+k+'"][href="'+hash+'"]');
                        var curr = _controllers[toggleType] ? (_controllers[toggleType].handler ? toggleType : 'default') : 'default';
                        _controllers[curr].handler(hash, $el);
                    };
                    //定义点击触发事件
                    $(document).on(core.options.clickEvent, _controllers[k]['selector'], function () {
                        $('input:focus,textarea:focus').blur();
                        var k = $(this).data('toggle');
                        var hash = $(this).attr('href') || '#';
                        (controller[k] || controller['default'])(hash, $(this));
                        return false;
                    });
                })(k);
            }
        };

        /**
         * 启动控制器，如果需要在启动core的时候启动，则函数名必须叫launch
         */
        controller.launch = function () {
            _makeHandler();
        };

        core.register('Controller', controller);
    })(core.$);

//组件
    (function ($) {
        var component = {};

        var _components = {
            default: {
                selector: '[data-role="view"]',
                handler: function (el, roleType) {
                    var $el = $(el);
                    roleType = roleType == 'default' ? $el.data('role') : roleType;
                    var componentObj = _components[roleType] || {};
                    if (componentObj.isToggle || $el.data('istoggle')) {
                        $el[$el.hasClass('active') ? 'removeClass' : 'addClass']('active');
                        return;
                    } else if ($el.hasClass('active')) {
                        return;
                    }

                    var $current;
                    var curSelector = '[data-role="' + roleType + '"].active';
                    if (componentObj.container) {
                        $current = $el.parents(componentObj.container).first().find(curSelector);
                    } else {
                        $current = $el.siblings(curSelector + '.active');
                    }
                    $el.addClass('active');
                    $current.removeClass('active');
                }
            },
            tab: {
                selector: '[data-role="tab"]',
                handler: function (el, roleType) {
                    var $el = $(el);
                    var toggleType = $el.data('toggle');
                    if ((!toggleType) || toggleType == 'section' || toggleType == 'modal' || toggleType == 'html') {
                        return;
                    }
                    _components['default'].handler(el, roleType);
                }
            },
            scroll: {
                selector: '[data-role="article"].active',
                event: 'articleload',
                handler: function (el, roleType) {
                    var options = {
                        verticle: {},
                        horizontal: {
                            scrollbars: false, scrollX: true, scrollY: false, bindToWrapper: true
                        },
                        scroll: {
                            scrollX: true, scrollY: true
                        },
                        pulldown: {},
                        pullup: {},
                        pull: {}
                    };

                    var _doScroll = function ($scroll) {
                        var opts = options[$scroll.data('scroll')];
                        core.Scroll('#' + $scroll.attr('id'), opts).refresh();
                    };

                    var _doRefresh = function ($el) {
                        core.Refresh('#' + $el.attr('id'), options[$el.data('scroll')]).refresh();
                    };

                    var $el = $(el || this);
                    var scrollType = $el.data('scroll');

                    if (scrollType == 'verticle' || scrollType == 'horizontal' || scrollType == 'scroll') _doScroll($el);

                    if (scrollType == 'pulldown' || scrollType == 'pullup' || scrollType == 'pull') _doRefresh($el);

                    var scrolls = $el.find('[data-scroll="verticle"],[data-scroll="horizontal"],[data-scroll="scroll"]');
                    for (var i = 0; i < scrolls.length; i++) {
                        (function ($this) {
                            setTimeout(function () {
                                _doScroll($this);
                            }, 100);
                        })($(scrolls[i]));

                    }

                    scrolls = $el.find('[data-scroll="pulldown"],[data-scroll="pullup"],[data-scroll="pull"]');
                    for (var i = 0; i < scrolls.length; i++) {
                        (function ($this) {
                            setTimeout(function () {
                                _doRefresh($this);
                            }, 100);
                        })($(scrolls[i]));
                    }
                }
            },
            formcheck: {
                selector: '[data-role="article"].active',
                event: 'articleload',
                handler: function (el, roleType) {

                    var $el = $(el);
                    var _doInit = function ($el) {
                        $el.removeAttr('href');
                        /*$el.on(core.options.clickEvent, function(e){
                     var isInScroll = $el.data('__isinscroll__');
                     if(typeof isInScroll=='undefined'){
                     isInScroll = $el.closest('[data-scroll]').length==0?false:true;
                     $el.data('__isinscroll__', isInScroll);
                     }
                     var tag = e.target.tagName.toLowerCase();
                     if((isInScroll&&tag!='input')||(!isInScroll&&tag!='input'&&tag!='label')){
                     var checkObj = $el.find('input')[0];
                     if(checkObj.type=='radio'){
                     checkObj.checked = true;
                     }else{
                     checkObj.checked = !checkObj.checked;
                     }
                     }
                     });*/
                    };

                    if ($el.data('role') == 'checkbox' || $el.data('role') == 'radio') {
                        _doInit($el);
                    } else {
                        var els = $el.find('[data-role="checkbox"],[data-role="radio"]');
                        for (var i = 0; i < els.length; i++) {
                            _doInit($(els[i]));
                        }
                    }
                }
            },
            toggle: {
                selector: '[data-role="article"].active',
                event: 'articleload',
                handler: function (el, roleType) {
                    var $el = $(el);
                    var _getVal = function (isActive, $el) {
                        var onValue = $el.data('on-value');
                        var offValue = $el.data('off-value');
                        var val = isActive ? onValue : offValue;
                        return typeof val == 'undefined' ? '' : val;
                    };
                    var _doToggle = function ($el) {
                        var isActive = $el.hasClass('active');
                        if ($el.find('div.toggle-handle').length > 0) {//已经初始化
                            $el[isActive ? 'removeClass' : 'addClass']('active');
                            $el.find('input[data-com-refer="toggle"]').val(_getVal(isActive ? false : true, $el));
                            return;
                        }
                        var name = $el.data('name');
                        //添加隐藏域，方便获取值
                        $el.append('<input type="hidden" data-com-refer="toggle" ' + (name ? 'name="' + name + '" ' : '') + ' value="' + _getVal(isActive, $el) + '"/>');
                        $el.append('<div class="toggle-handle"></div>');
                        $el.on(core.options.clickEvent, function () {
                            var $t = $(this), v = _getVal($el.hasClass('active') ? false : true, $el);
                            $t.toggleClass('active').trigger('datachange', [v]);//定义toggle事件
                            $t.find('input').val(v);
                            return false;
                        });
                    };

                    if ($el.data('role') == 'toggle') {
                        _doToggle($el);
                    } else {
                        var toggles = $el.find('[data-role="toggle"]');
                        for (var i = 0; i < toggles.length; i++) {
                            _doToggle($(toggles[i]));
                        }
                    }
                }
            },
            'lazyload': {
                selector: '[data-role="article"].active',
                event: 'articleload',
                handler: function (el, roleType) {
                    var $el = $(el);
                    var _doLazyload = function ($el) {
                        var placeholder = $el.attr('placeholder') || core.options.lazyloadPlaceholder;
                        if (!$el.attr('src') && placeholder) $el.attr('src', placeholder);
                        var source = core.util.script($el.data('source'));
                        var eTop = $el.offset().top;//元素的位置
                        var validateDistance = 100;
                        var winHeight = $(window).height() + validateDistance;
                        if (eTop < 0 || eTop > winHeight) return;

                        var type = $el.data('type');
                        if (type == 'base64') {
                            core.ajax({
                                url: source,
                                success: function (data) {
                                    _injectImg($el, data);
                                },
                                isBlock: false
                            });
                        } else {
                            var img = new Image();
                            img.src = source;
                            if (img.complete) {
                                _injectImg($el, source);
                                img = null;
                            } else {
                                img.onload = function () {
                                    _injectImg($el, source);
                                    img = null;
                                };
                                img.onerror = function () {
                                    _injectImg($el, placeholder || '');
                                    img = null;
                                };
                            }
                        }
                    };

                    var _injectImg = function ($el, data) {
                        if (!$el.data('source')) return;
                        core.anim.run($el, 'fadeOut', function () {
                            $el.css('opacity', '0');
                            $el[0].onload = function () {
                                core.anim.run($el, 'fadeIn', function () {
                                    $el.css('opacity', '1');
                                    var $sd = $el.closest('[data-role="slider"]');
                                    if ($sd.hasClass('active')) core.Slider($sd).refresh();
                                    core.Component.scroll($el.closest('[data-scroll]'));
                                });
                            };
                            $el.attr('src', data);
                        });
                        $el.removeAttr('data-source');
                    };

                    if ($el.data('source')) {
                        _doLazyload($el);
                    } else {
                        var lazyloads = $el.find('img[data-source]');
                        for (var i = 0; i < lazyloads.length; i++) {
                            _doLazyload($(lazyloads[i]));
                        }
                    }
                }
            },
            scrollTop: {
                selector: '[data-role="article"].active',
                event: 'articleload',
                handler: function (el, roleType) {
                    var _work = function ($el) {
                        $el.on(core.options.clickEvent, function () {
                            $el.removeClass('active');
                        });
                        var $target = $($el.attr('href'));
                        if ($target.data('scroll')) {
                            var scroll = core.Scroll($el.attr('href'));
                            scroll.on('scrollEnd', function () {
                                $el[this.y < -120 ? 'addClass' : 'removeClass']('active');
                            });
                        } else {
                            var scroll = $target;
                            scroll.on('scrollEnd', function () {
                                $el[scroll.scrollTop() > 120 ? 'addClass' : 'removeClass']('active');
                            });
                            core.util.jquery.scrollEnd(scroll);
                        }
                    };
                    var $el = $(el);
                    if ($el.data('role') == 'scrollTop') {
                        _work($el);
                    } else {
                        var comps = $el.find('[data-role="scrollTop"]');
                        for (var i = 0; i < comps.length; i++) {
                            _work($(comps[i]));
                        }
                    }
                }
            },
            pictureShow: {
                type: 'function',
                handler: function (opts) {
                    var id = opts.id;
                    var index = opts.index || 0;
                    var list = opts.list;
                    var htmlArr = [];
                    htmlArr.push('<section id="' + id + '" data-role="section" data-cache="false" style="background:#000;">');
                    htmlArr.push('<header class="picture_show_header">');
                    htmlArr.push(opts.header || '<a data-toggle="back" href="#" style="float:right;color:#fff;text-align:center; padding:8px; font-size:18px;">X</a>');
                    htmlArr.push('</header>');
                    htmlArr.push('<article id="' + id + '_article" data-role="article" class="active" style="overflow:hidden;background:rgba(255, 255, 255, 0.1) none repeat scroll 0 0 !important;">');
                    htmlArr.push('<div id="' + id + '_slide" data-role="slider" class="full" style="overflow: hidden;"><div class="scroller">');
                    for (var i = 0; i < list.length; i++) {
                        htmlArr.push('<div class="slide" style="padding-top:100px;">');
                        htmlArr.push('<img data-source="' + list[i].imgURL + '" class="full-width"/>');
                        htmlArr.push('</div>');
                    }
                    htmlArr.push('</div></div>');

                    htmlArr.push('</article>');

                    htmlArr.push('<div class="picture_show_footer" style="position:relative;bottom:0px;left:0px;right:0px;padding:8px;color:#fff;min-height:140px;background:#111;">');
                    htmlArr.push('<div><span style="float:left;font-size:20px;">' + opts.title + '</span><p class="picture_show_num" style="float:right;"></p></div>');
                    htmlArr.push('<div style="clear:both;line-height:20px;"><span class="picture_show_content"></span></div>');
                    htmlArr.push('</div>');

                    htmlArr.push('</section>');
                    $('#section_container').append(htmlArr.join(''));
                    core.Controller.section('#' + id);
                    var $num = $('#' + id + ' .picture_show_num');
                    var $content = $('#' + id + ' .picture_show_content');
                    var $header = $('#' + id + ' .picture_show_header');
                    var $footer = $('#' + id + ' .picture_show_footer');
                    var $slider = core.Slider('#' + id + '_slide', {
                        dots: 'hide',
                        change: function (i) {
                            $num.html('<span style="font-size:18px;">' + (i + 1) + '</span>/' + list.length);
                            $content.html(list[i].content || opts.title);
                        }
                    });
                    $slider.index(index);
                    $('#' + id + '_article').on(core.options.clickEvent, function () {
                        $header.toggle();
                        $footer.toggle();
                        return false;
                    });
                }
            }
        };

        /**
         * 添加组件
         * @param {Object} 控制器对象，形如{key:{selector:'',handler:function(){}}}
         */
        component.add = function (c) {
            $.extend(_components, c);
        };

        /**
         * 获取全部组件
         * @param {String} 组件的key，如果有key则返回当前key的组件，没有key则返回全部组件
         */
        component.get = function (key) {
            return key ? _components[key] : _components;
        };

        /**
         * 获取全部组件
         * @param 要初始化的组件选择器，可以是$对象
         */
        component.initComponents = function (selector) {
            var $el = $(selector);
            for (var k in _components) {
                if (_components[k].type == 'function' || k == 'default') continue;
                core.Component[k]($el);
            }
        };


        /**
         * 初始化组件
         * @private 仅能调用一次
         */
        _makeHandler = function () {
            for (var k in _components) {
                if (_components[k].type == 'function') {
                    component[k] = _components[k].handler;
                    continue;
                }
                (function (k) {
                    //定义JS调用函数
                    component[k] = function (hash) {
                        var roleType = k;
                        var curr = _components[roleType] ? (_components[roleType].handler ? roleType : 'default') : 'default';
                        return _components[curr].handler(hash, roleType);
                    };
                    //定义触发事件
                    if (!_components[k]['selector']) return;
                    $(document).on(_components[k].event || core.options.clickEvent, _components[k]['selector'], function () {
                        var curr = _components[k].handler ? k : 'default';
                        _components[curr].handler(this, k);
                        return false;
                    });

                })(k);
            }

        };
        /**
         * 获取控制器传给组件的参数,hash为被控制的组件的#id
         */
        component.params = function (hash) {
            return core.JSON.parse($(hash).data('params')) || {};
        };

        /**
         * 设置并返回原始初始化状态
         */
        component.isInit = function ($el) {
            if ($el.data('com-init')) {
                return true;
            } else {
                $el.data('com-init', 'init');
                return false;
            }
        };

        /**
         * type组件的名称，hash组件的#id
         */
        component.getObject = function (type, hash) {
            var $el = $(hash);
            var returnObj = {};
            var comObj = component.get(type);
            if (!comObj || !comObj.extend) return returnObj;
            var _extend = comObj.extend;
            for (var k in _extend) {
                (function (k) {
                    returnObj[k] = function () {
                        return _extend[k].apply($el, arguments);
                    };
                })(k);
            }
            return returnObj;
        };

        //启动组件
        component.launch = function () {
            _makeHandler();
        };

        core.register('Component', component);
    })(core.$);

//动画封装
    (function ($) {

        var anim = {};

        anim.classes = {//形如：[[currentOut,targetIn],[currentOut,targetIn]]
            empty: [['empty', 'empty'], ['empty', 'empty']],
            slide: [['slideLeftOut', 'slideLeftIn'], ['slideRightOut', 'slideRightIn']],
            cover: [['', 'slideLeftIn'], ['slideRightOut', '']],
            slideUp: [['', 'slideUpIn'], ['slideDownOut', '']],
            slideDown: [['', 'slideDownIn'], ['slideUpOut', '']],
            popup: [['', 'scaleIn'], ['scaleOut', '']],
            flip: [['slideLeftOut', 'flipOut'], ['slideRightOut', 'flipIn']]
        };

        anim.addClass = function (cssObj) {
            $.extend(anim.classes, cssObj || {});
        };

        /**
         * 添加控制器
         * @param {Object} 要切换动画的jQuery对象
         * @param {String} 动画样式
         * @param {Number} 动画时间
         * @param {Function} 动画结束的回调函数
         */

        anim.run = function ($el, cls, cb) {
            var $el = $($el);
            var _eName = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';
            if ($el.length == 0) {
                cb && cb();
                return;
            }
            if (typeof cls == 'object') {
                $el.animate(cls, 250, 'linear', function () {
                    cb && cb();
                });
                return;
            }
            cls = (cls || 'empty') + ' anim';
            $el.addClass(cls).one(_eName, function () {
                $el.removeClass(cls);
                cb && cb();
                $el.unbind(_eName);
            });
        };

        anim.css3 = function ($el, cssObj, cb) {
            $el = $($el);
            var _eName = 'webkitTransitionEnd otransitionend transitionend';
            $el.addClass('comm_anim').css(cssObj).one(_eName, function () {
                $el.removeClass('comm_anim');
                cb && cb();
                $el.unbind(_eName);
            });
        };

        /**
         * 添加控制器
         * @param {String} 要切换的当前对象selector
         * @param {String} 要切换的目标对象selector
         * @param {Boolean} 是否返回
         * @param {Function} 动画结束的回调函数
         */
        anim.change = function (current, target, isBack, callback) {

            var $current = $(current);
            var $target = $(target);

            if ($current.length + $target.length == 0) {
                callback && callback();
                return;
            }

            var targetTransition = $target.data('transition');
            if (!anim.classes[targetTransition]) {
                callback && callback();
                return;
            }
            var transitionType = anim.classes[targetTransition][isBack ? 1 : 0];
            anim.run($current, transitionType[0]);
            anim.run($target, transitionType[1], function () {
                callback && callback();
            });
        };

        core.register('anim', anim);

    })(core.$);

//ajax封装
    (function ($) {

        //用法继承jQuery的$.ajax
        var ajax = function (opts) {

            var success = opts.success;
            var error = opts.error;


            // var random = '__ajax_random__=' + Math.random();
            // opts.url += (opts.url.split(/\?|&/i).length == 1 ? '?' : '&') + random;

            var _success = function (data) {
                success && success(data);
            };
            var _error = function (data) {
                error && error();
            };

            opts.success = _success;
            opts.error = _error;

            $.ajax(opts);
        };

        core.register('ajax', ajax);
    })(core.$);

    (function ($) {
        var util = {};

        util.jquery = {
            scrollEnd: function ($el) {
                var _step;
                var _handler = function () {
                    if (_step) clearTimeout(_step);
                    _step = null;
                    _step = setTimeout(function () {
                        clearTimeout(_step);
                        _step = null;
                        $el.trigger('scrollEnd');
                    }, 100);
                };
                $el.off('scroll', _handler).on('scroll', _handler);
            }
        };

        util.getCSSTransform = function ($el) {
            var transform = $el.css('transform');
            if (!transform || transform == 'none') return [];
            console.log(transform.replace(/matrix.([^\\)]+)./, '$1'))
            //update by Li Hongwei on 2017-07-17 11:41:27
            // var matrix = new Function('return ['+transform.replace(/matrix.([^\\)]+)./, '$1')+'];');
            // return matrix();
            return [];
        };

        util.script = function (str) {
            str = (str || '').trim();
            var tag = false;

            str = str.replace(/\$\{([^\}]*)\}/g, function (s, s1) {
                try {
                    tag = true;
                    return eval(s1.trim());
                } catch (e) {
                    return '';
                }

            });

            return tag ? util.script(str) : str;
        };

        util.provider = function (str, data) {
            str = (str || '').trim();
            return str.replace(/\$\{([^\}]*)\}/g, function (s, s1) {
                return data[s1.trim()] || '';
            });
        };

        var URLParser = function (url) {

            this._fields = {
                'Username': 4,   //用户
                'Password': 5,   //密码
                'Port': 7,       //端口
                'Protocol': 2,   //协议
                'Host': 6,       //主机
                'Pathname': 8,   //路径
                'URL': 0,
                'Querystring': 9,//查询字符串
                'Fragment': 10,   //锚点
                'Filename': -1,//文件名,不含后缀
                'Queryobject': -1, //查询字串对象
                'Hashobject': -1 //hash对象
            };

            this._values = {};
            this._regex = null;
            this.version = 0.1;
            this._regex = /^((\w+):\/\/)?((\w+):?(\w+)?@)?([^\/\?:]+):?(\d+)?(\/?[^\?#]+)?\??([^#]+)?#?(\w*)/;
            for (var f in this._fields) {
                this['get' + f] = this._makeGetter(f);
            }

            if (typeof url != 'undefined') {
                this._parse(url);
            }
        };
        URLParser.prototype.setURL = function (url) {
            this._parse(url);
        };

        URLParser.prototype._initValues = function () {
            for (var f in this._fields) {
                this._values[f] = '';
            }
        };

        var removeURLPre = function (url) {
            if (url.indexOf('file:') == 0) {
                url = (url.replace(/file:\/*/, ''));
            }
            return url;
        };

        URLParser.prototype._path = function (url) {
            var up = new URLParser(removeURLPre(location.href.split(/\?\#/)[0]));
            var baseUrl = up.getProtocol() + '://' + up.getHost() + (up.getPort() ? (':' + up.getPort()) : '');
            url = removeURLPre(url);
            if (url.indexOf('/') == 0) {
                return baseUrl + url;
            } else {
                var curPaths = up.getPathname().split('/');
                curPaths.length -= url.split('../').length;
                return baseUrl + curPaths.join('/') + '/' + url.replace(/[\.]{1,2}\//g, '');
            }
        };

        URLParser.prototype._parse = function (url) {
            this._initValues();
            url = removeURLPre(url);
            var r = this._regex.exec(url);
            if (!r) {
                url = this._path(url);
                r = this._regex.exec(url);
                if (!r) {
                    console.log('url invalid:' + url);
                    return;
                }
            }

            for (var f in this._fields) if (typeof r[this._fields[f]] != 'undefined') {
                this._values[f] = r[this._fields[f]];
            }
        };
        URLParser.prototype._makeGetter = function (field) {
            return function () {
                if (field == 'Filename') {
                    var path = (this._values['URL'].split('?')[0]).split('/');
                    return path[path.length - 1].split('.')[0];
                } else if (field == 'Queryobject') {
                    var query = this._values['Querystring'] || '';
                    var seg = query.split('&');
                    var obj = {};
                    for (var i = 0; i < seg.length; i++) {
                        var s = seg[i].split('=');
                        obj[s[0]] = s[1];
                    }
                    return obj;
                } else if (field == 'Hashobject') {
                    return {
                        url: this.getURL(),
                        hash: '#' + (this.getFragment() || this.getFilename()),
                        tag: '#' + (this.getFragment() || this.getFilename()),
                        query: this.getQuerystring(),
                        param: this.getQueryobject()
                    };
                }

                return this._values[field];
            };
        };

        //url解析类
        util.parseURL = function (url) {
            url = util.script(url || '');
            if (url.indexOf('#') == 0) {
                url = url.replace('#', '') + (core.options.viewSuffix ? core.options.viewSuffix : '');
            }

            return new URLParser(url);
        };

        util.isCrossDomain = function (url) {
            if (!url || url.indexOf(':') < 0) return false;
            var urlOpts = util.parseURL(url);
            if (!urlOpts.getProtocol()) return false;
            return !((location.protocol.replace(':', '') + location.host) == (urlOpts.getProtocol() + urlOpts.getHost() + ':' + urlOpts.getPort()));
        };

        util.checkBoolean = function () {
            var result = false;
            for (var i = 0; i < arguments.length; i++) {
                if (typeof arguments[i] == 'boolean') {
                    return arguments[i];
                }
            }
            return result;
        };

        var _isCrossDomain = function (url) {
            if (!url || url.indexOf(':') < 0) return false;

            var urlOpts = core.util.parseURL(url);

            if (!urlOpts.getProtocol()) return false;

            return !((location.protocol.replace(':', '') + location.host) == (urlOpts.getProtocol() + urlOpts.getHost() + ':' + urlOpts.getPort()));
        };

        /*
     * $.ajax函数封装，判断是否有跨域，并且设置跨域处理函数
     * @param ajax json对象，进行简单统一处理，如果需要完整功能请使用$.ajax
     * */
        util.ajax = function (opts) {
            if (!opts || !opts.url) return;
            opts.url = util.script(opts.url);
            //update by Li Hongwei on 2017-07-17 11:35:23
            //var _isBlock = util.checkBoolean(opts.isBlock,core.options.showPageLoading);
            var _isBlock = opts.isBlock;
            opts.dataType = (opts.dataType || 'text').toLowerCase();
            var ajaxData = {
                url: opts.url,
                timeout: 20000,
                type: opts.type || 'get',
                dataType: opts.dataType,
                success: function (html) {
                    if (_isBlock) core.hideMask();
                    opts.success && opts.success(opts.dataType == 'text' ? util.script(html) : html);
                },
                error: function (html) {
                    if (_isBlock) core.hideMask();
                    opts.error && opts.error(null);
                },
                reqCharset: opts.reqCharset || 'utf-8'
            };
            if (opts.data) ajaxData.data = opts.data;
            if (opts.headers) ajaxData.headers = opts.headers;
            var isCross = _isCrossDomain(opts.url);
            var handler = core.ajax;
            if (isCross) {
                ajaxData.dataType = core.options.crossDomainHandler ? ajaxData.dataType : 'jsonp';
                handler = core.options.crossDomainHandler || handler;
            }
            if (ajaxData.dataType.toLowerCase() == 'jsonp') {
                ajaxData.jsonp = opts.jsonp || 'corecallback';
            }
            if (_isBlock) core.showMask();
            handler(ajaxData);
        };

        /*
     * $.ajax函数封装，不允许跨域
     * @param url地址，必选
     * @param callback回调函数，可选
     *
     * */
        util.getHTML = function (url, callback) {
            core.ajax({
                url: url,
                type: 'get',
                cache : true,
                dataType: 'text',
                success: function (html) {
                    callback && callback(core.util.script(html || ''));
                },
                error: callback
            });
        };

        util.readyAlarm = function ($inner, targetName, eventName) {
            var _return = {};
            for (var k in $inner) {
                if (typeof $inner[k] != 'function') {
                    _return[k] = $inner[k];
                    continue;
                }
                _return[k] = (function (k) {
                    return function () {
                        try {
                            return $inner[k].apply(this, arguments);
                        } catch (e) {
                            console.log('提示', '请在' + (eventName || core.options.readyEvent) + '之后调用' + targetName + '.' + k + '桥接函数');
                        }
                    };
                })(k);
            }
            return _return;
        };

        util.encodeHash = function (hash) {
            return hash;

            // return encodeURIComponent(core.Base64.encode(hash));

        };

        util.decodeHash = function (enHash) {
            return enHash;
            // return core.Base64.decode(decodeURIComponent(enHash));
        };

        core.register('util', util);

    })(core.$);

    (function ($) {
        var Template = function (selecotr) {
            this.$el = $(selecotr);
        };

        Template.prototype.getTemplate = function (cb) {
            var $el = this.$el;
            var source = $el.attr('src');
            var tmpl = $el.html().trim();
            if (tmpl) {
                cb && cb(tmpl);
            } else if (source) {
                core.util.getHTML(core.util.script(source), function (html) {
                    $el.text(html);
                    cb && cb(html);
                });
            } else {
                cb && cb('');
            }
        };

        Template.prototype.render = function (url, cb) {
            var data;
            if (typeof url == 'object') {
                data = url;
                url = '';
            }
            if (!data && !url) {
                cb && cb();
                return;
            }
            var tmplHTML = this.$el.html().trim();
            if (data && tmplHTML) {
                var html = '';
                try {
                    html = template.compile(tmplHTML)(data);
                } catch (e) {
                }
                cb && cb(html, tmplHTML, data);
                return html;
            }

            this.getTemplate(function (tmpl) {
                if (url) {
                    core.util.ajax({
                        url: url,
                        dataType: 'json',
                        success: function (data) {
                            var render = template.compile(tmpl);
                            html = render(data);
                            cb && cb(html, tmpl, data);
                        },
                        error: function () {
                            cb && cb();
                        }
                    });
                } else {
                    var render = template.compile(tmpl);
                    html = render(data);
                    cb && cb(html, tmpl, data);
                }
            });

        };

        var _render = function (type, url, cb) {
            var $el = this.$el;
            this.render(url, function (html, tmpl, data) {
                var $referObj = $el;
                var id = $el.attr('id');
                var tag = '#' + $el.attr('id');
                var $oldObj = $el.parent().find('[__inject_dependence__="' + tag + '"]');
                if (type == 'replace') {
                    $oldObj.remove();
                    //$referObj = $el;
                } else if (type == 'after') {
                    $referObj = $oldObj.length == 0 ? $el : $oldObj.last();
                } else if (type == 'before') {
                    //$referObj = $el;
                }
                var $html = $(html).attr('__inject_dependence__', tag);
                $referObj.after($html);
                cb && cb($html, tmpl, data);
                $el.trigger('renderEnd', [$html]);
            });
        };

        Template.prototype.load = function(url, $oldObj, cb){
            this.render(url, function(html, tmpl, data){
                // var $oldObj = $('#t_dialog_oper');
                $oldObj.html(html);
                cb&&cb($oldObj, tmpl, data);
            });
        };

        Template.prototype.loadBefore = function(url, $oldObj, cb){
            this.render(url, function(html, tmpl, data){
                // var $oldObj = $('#t_dialog_oper');
                $oldObj.before(html);
                cb&&cb($oldObj, tmpl, data);
            });
        };

        Template.prototype.loadAfter = function(url, $oldObj, cb){
            this.render(url, function(html, tmpl, data){
                // var $oldObj = $('#t_dialog_oper');
                $oldObj.after(html);
                cb&&cb($oldObj, tmpl, data);
            });
        };

        Template.prototype.loadAppend = function(url, $oldObj, cb){
            this.render(url, function(html, tmpl, data){
                // var $oldObj = $('#t_dialog_oper');
                $oldObj.append(html);
                cb&&cb($oldObj, tmpl, data);
            });
        };

        Template.prototype.renderReplace = function (url, cb) {
            _render.call(this, 'replace', url, cb);
        };

        Template.prototype.renderAfter = function (url, cb) {
            _render.call(this, 'after', url, cb);
        };
        Template.prototype.renderBefore = function (url, cb) {
            _render.call(this, 'before', url, cb);
        };

        core.register('template', function (selecotr) {
            return new Template(selecotr);
        });
    })(core.$);

    /*
 * 扩展JSON:core.JSON.stringify和core.JSON.parse，用法你懂
 * */
    (function () {
        var JSON = {};
        JSON.parse = function (str) {
            try {
                return eval("(" + str + ")")
            } catch (e) {
                return null
            }
        };
        JSON.stringify = function (o) {
            var r = [];
            if (typeof o == "string") {
                return '"' + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + '"'
            }
            if (typeof o == "undefined") {
                return ""
            }
            if (typeof o != "object") {
                return o.toString()
            }
            if (o === null) {
                return null
            }
            if (o instanceof Array) {
                for (var i = 0; i < o.length; i++) {
                    r.push(this.stringify(o[i]))
                }
                r = "[" + r.join() + "]"
            } else {
                for (var i in o) {
                    r.push('"' + i + '":' + this.stringify(o[i]))
                }
                r = "{" + r.join() + "}"
            }
            return r
        };
        JSON.toParams = function (json) {
            var arr = [];
            for (var k in json) {
                var str = [];
                var v = json[k];
                v = v instanceof Array ? v : [v];
                for (var i = 0; i < v.length; i++) {
                    str.push(v[i])
                }
                arr.push(k + "=" + str.join(","))
            }
            return arr.join("&")
        };
        core.register('JSON', JSON);
    })();


    /*! jf.mask.js v1.0.0 MIT License By Yimi https://github.com/yimicat/jf */
    ;(function($){
        function Mask(obj, options) {
            this.obj = obj;
            this.defaults = {
                isOpen : true
            };
            this.options = $.extend({}, this.defaults, options);
            this.init();
        }

        Mask.prototype.init = function () {
            if(this.options.isOpen) {
                this.show();
            }
        };

        Mask.prototype.show = function() {
            if(this.obj.find('[js-mask]').length < 1) {
                this.obj.append('<div js-mask style="position: absolute;top: 0;left: 0;width: 100%;height:100%;z-index: 19891018;">' +
                    '<div style="background-color: rgb(255, 255, 255); width:100%;height:100%;opacity: 0.5;"></div>' +
                    '<div class="layui-layer layui-layer-loading" style="z-index: 19891019; position: absolute;top: 50%; left: 50%;margin-left: -18.5px;margin-top: -18.5px;"><div id="" class="layui-layer-content layui-layer-loading1"></div></div>' +
                    '</div>');
            }
        };

        Mask.prototype.hide =function() {
            if(this.obj.find('[js-mask]').length > 0) {
                this.obj.find('[js-mask]').remove();
            }
        };

        var _mask = function(options) {
            return new Mask(options);
        };
        core.register('mask', _mask);
    })(core.$);


    (function () {
        var Base64 = {
            table: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "/"],
            UTF16ToUTF8: function (str) {
                var res = [], len = str.length;
                for (var i = 0; i < len; i++) {
                    var code = str.charCodeAt(i);
                    if (code > 0 && code <= 127) {
                        res.push(str.charAt(i))
                    } else {
                        if (code >= 128 && code <= 2047) {
                            var byte1 = 192 | ((code >> 6) & 31);
                            var byte2 = 128 | (code & 63);
                            res.push(String.fromCharCode(byte1), String.fromCharCode(byte2))
                        } else {
                            if (code >= 2048 && code <= 65535) {
                                var byte1 = 224 | ((code >> 12) & 15);
                                var byte2 = 128 | ((code >> 6) & 63);
                                var byte3 = 128 | (code & 63);
                                res.push(String.fromCharCode(byte1), String.fromCharCode(byte2), String.fromCharCode(byte3))
                            } else {
                                if (code >= 65536 && code <= 2097151) {
                                } else {
                                    if (code >= 2097152 && code <= 67108863) {
                                    } else {
                                    }
                                }
                            }
                        }
                    }
                }
                return res.join("")
            },
            UTF8ToUTF16: function (str) {
                var res = [], len = str.length;
                var i = 0;
                for (var i = 0; i < len; i++) {
                    var code = str.charCodeAt(i);
                    if (((code >> 7) & 255) == 0) {
                        res.push(str.charAt(i))
                    } else {
                        if (((code >> 5) & 255) == 6) {
                            var code2 = str.charCodeAt(++i);
                            var byte1 = (code & 31) << 6;
                            var byte2 = code2 & 63;
                            var utf16 = byte1 | byte2;
                            res.push(Sting.fromCharCode(utf16))
                        } else {
                            if (((code >> 4) & 255) == 14) {
                                var code2 = str.charCodeAt(++i);
                                var code3 = str.charCodeAt(++i);
                                var byte1 = (code << 4) | ((code2 >> 2) & 15);
                                var byte2 = ((code2 & 3) << 6) | (code3 & 63);
                                utf16 = ((byte1 & 255) << 8) | byte2;
                                res.push(String.fromCharCode(utf16))
                            } else {
                                if (((code >> 3) & 255) == 30) {
                                } else {
                                    if (((code >> 2) & 255) == 62) {
                                    } else {
                                    }
                                }
                            }
                        }
                    }
                }
                return res.join("")
            },
            encode: function (str) {
                if (!str) {
                    return ""
                }
                var utf8 = this.UTF16ToUTF8(str);
                var i = 0;
                var len = utf8.length;
                var res = [];
                while (i < len) {
                    var c1 = utf8.charCodeAt(i++) & 255;
                    res.push(this.table[c1 >> 2]);
                    if (i == len) {
                        res.push(this.table[(c1 & 3) << 4]);
                        res.push("==");
                        break
                    }
                    var c2 = utf8.charCodeAt(i++);
                    if (i == len) {
                        res.push(this.table[((c1 & 3) << 4) | ((c2 >> 4) & 15)]);
                        res.push(this.table[(c2 & 15) << 2]);
                        res.push("=");
                        break
                    }
                    var c3 = utf8.charCodeAt(i++);
                    res.push(this.table[((c1 & 3) << 4) | ((c2 >> 4) & 15)]);
                    res.push(this.table[((c2 & 15) << 2) | ((c3 & 192) >> 6)]);
                    res.push(this.table[c3 & 63])
                }
                return res.join("")
            },
            decode: function (str) {
                if (!str) {
                    return ""
                }
                var len = str.length;
                var i = 0;
                var res = [];
                while (i < len) {
                    code1 = this.table.indexOf(str.charAt(i++));
                    code2 = this.table.indexOf(str.charAt(i++));
                    code3 = this.table.indexOf(str.charAt(i++));
                    code4 = this.table.indexOf(str.charAt(i++));
                    c1 = (code1 << 2) | (code2 >> 4);
                    c2 = ((code2 & 15) << 4) | (code3 >> 2);
                    c3 = ((code3 & 3) << 6) | code4;
                    res.push(String.fromCharCode(c1));
                    if (code3 != 64) {
                        res.push(String.fromCharCode(c2))
                    }
                    if (code4 != 64) {
                        res.push(String.fromCharCode(c3))
                    }
                }
                return this.UTF8ToUTF16(res.join(""))
            }
        };
        core.register('Base64', {
            encode: function (str) {
                try {
                    return Base64.encode(str);
                } catch (e) {
                    return '';
                }
            },
            decode: function (str) {
                try {
                    return Base64.decode(str);
                } catch (e) {
                    return '';
                }
            }
        });
    })();

    return C;
});

