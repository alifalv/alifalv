/*!
 * 分页扩展
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    var Pagination = require('pagination');
    var HomePagination = require('homePagination');

    function Page(pageElement, options) {
        this.$pageElement = pageElement;
        this.defaults = {
            isInitRefresh : true,//是否初始化就加载列表
            pageType : 'def',//def or home
            url : null,
            urlParams : null,
            data : null,
            hasPage : true,//是否开启分页
            $form : this.$pageElement.find('[pagination="searchForm"]'),//搜索框加上 pagination="searchForm"
            tmplId : '#' + this.$pageElement.attr('id') + 'Tmpl',//模板id
            $mask : this.$pageElement.find('[pagination="mask"]'),//遮罩层 pagination="mask"
            $search : this.$pageElement.find('[pagination="searchBtn"]'),//搜索按钮 pagination="searchBtn"
            pageSize : 10,//每页的记录数
            addData : null,//附加到ajax成功数据
            htmlLoad : function() {},//每次页面刷星加载完成事件
            init : function () {},//初始化完成事件
            dataFilter: function () {

            }
        };
        this._page = null;//分页对象
        this.options = $.extend({}, this.defaults, options);
        this.init();
    }



    Page.prototype.init = function() {
        var _that = this;

        //没有分页
        if(!_that.options.hasPage) {
            var _mask = C.mask(_that.options.$mask);

            //rest配置
            var _restOptions = {
                url: _that.options.url,
                success: function (result) {
                    if(_that.options.addData) {
                        result = $.extend({}, result, _that.options.addData);
                    }
                    C.template(_that.options.tmplId).renderReplace(result, function (html, tmpl, data) {
                        _that.options.htmlLoad.call(null, html, tmpl, data);
                    });
                },
                fail : function() {
                    C.template(_that.options.tmplId).renderReplace({error : '接口异常'}, function () {});
                },
                error: function () {
                    C.template(_that.options.tmplId).renderReplace({error : '连接失败'}, function () {});
                },
                complete :function () {
                    _mask.hide();
                }
            };


            //组装urlParams
            if(null != _that.options.urlParams) {
                var pageParams = {
                    pageNo: 1,//页码
                    pageSize: _that.options.pageSize//每页的记录数
                };
                _restOptions.urlParams = $.extend({}, pageParams, _that.options.urlParams);
            } else {
                var pageParams = {
                    pageNo: 1,//页码
                    pageSize: _that.options.pageSize//每页的记录数
                };
                _restOptions.urlParams = $.extend({}, pageParams);
            }

            //组装data
            if(null != _that.options.data) {
                _restOptions.data = _that.options.data;
            }

            //组装$form
            if(_that.options.$form.length > 0) {
                _restOptions.form = _that.options.$form;
            }
            rest.post(_restOptions);

            return false;
        }


        //分页插件配置
        var _options = {//分页 pagination="page"
            init : _that.options.init,
            isInitRefresh : _that.options.isInitRefresh,
            pageSize : _that.options.pageSize,
            refresh : function(pageNo, pageSize) {
                var _mask = C.mask(_that.options.$mask);
                var _this = this;

                //rest配置
                var _restOptions = {
                    url: _that.options.url,
                    success: function (result) {
                        if(_that.options.addData) {
                            result = $.extend({}, result, _that.options.addData);
                        }
                        _that.options.dataFilter.call(null, result);
                        _this.setPageValue(result.total, result.records, result.page);
                        C.template(_that.options.tmplId).renderReplace(result, function (html, tmpl, data) {
                            _that.options.htmlLoad.call(null, html, tmpl, data);
                        });
                    },
                    fail : function() {
                        C.template(_that.options.tmplId).renderReplace({error : '接口异常'}, function () {});
                    },
                    error: function () {
                        C.template(_that.options.tmplId).renderReplace({error : '连接失败'}, function () {});
                    },
                    complete :function () {
                        _mask.hide();
                    }
                };

                //组装urlParams
                if(null != _that.options.urlParams) {
                    var pageParams = {
                        pageNo: pageNo,//页码
                        pageSize: pageSize//每页的记录数
                    };
                    _restOptions.urlParams = $.extend({}, pageParams, _that.options.urlParams);
                }else {
                    var pageParams = {
                        pageNo: pageNo,//页码
                        pageSize: pageSize//每页的记录数
                    };
                    _restOptions.urlParams = $.extend({}, pageParams);
                }

                //组装data
                if(null != _that.options.data) {
                    _restOptions.data = _that.options.data;
                }

                //组装$form
                if(_that.options.$form.length > 0) {
                    _restOptions.form = _that.options.$form;
                }
                rest.post(_restOptions);
            }
        };

        if(_that.options.pageType == 'def') {
            //默认分页类型初始化Pagination
            _that._page = new Pagination(_that.$pageElement.find('[pagination="page"]'), _options);


        } else if(_that.options.pageType == 'home') {
            //home分页类型初始化HomePagination
            _that._page = new HomePagination(_that.$pageElement.find('[pagination="page"]'), _options);
        }

        //存在查询按钮绑定查询事件
        // if(_that.options.$search.length > 0) {
        //
        //     _that.options.$search.on('click', function() {
        //         _that._page.refresh();
        //     });
        // }

        _that.$pageElement.on('click.pagination', '[pagination="searchBtn"]', function() {
            _that._page.search();
        });

        //有form对象 && form对象是指定的
        if(_that.options.$form.length > 0 && _that.$pageElement.find(_that.options.$form).length === 0) {
            _that.options.$form.on('click.pagination', '[pagination="searchBtn"]', function() {
                _that._page.search();
            });
        }


    };

    //暴露刷新方法
    Page.prototype.refresh = function() {
        this._page.refresh();
    };

    return Page;
});
