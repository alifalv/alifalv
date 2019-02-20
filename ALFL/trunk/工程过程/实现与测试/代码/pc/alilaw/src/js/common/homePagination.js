/*!
 * 个人主页分页
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    //
    // 4 5 6 7 8
    // (5 - 1)/2

    function Pagination(pageElement, options) {
        this.$pageElement = pageElement;
        this.defaults = {
            isInitRefresh : true,//是否初始化就加载列表
            $mask : null,
            $form : null,
            pageSize : 10,//每页的记录数

            pageNo : 1,//当前页码
            totalPage : 0,//总页数
            records : 0,//总记录条数
            scopePage : 5,//显示按键个数
            refresh : function(pageNo, pageSize) {},
            init :function() {}
        };
        this.options = $.extend({}, this.defaults, options);
        this.init();
    }

    Pagination.prototype.setPageValue = function(totalPage, records, pageNo) {
        var _that = this;

        _that.options.totalPage = totalPage;
        _that.options.records = records;
        // _that.options.pageNo = pageNo
        _that.$pageElement.find('.j-currentPage').html(_that.options.pageNo);
        _that.$pageElement.find('.j-totalPage').html(_that.options.totalPage);
        _that.refreshHtml();
    };

    Pagination.prototype.getPageNo = function(totalPage, records, pageNo) {
        var _that = this;
        return _that.options.pageNo;
    };


    Pagination.prototype.createNumPageHtml = function(page) {
        var _that = this;
        if(page != _that.options.pageNo) {
            return '<li><a href="javascript:void(0);" class="j-page">' + page + '</a></li>';
        }
        return '<li class="active"><a href="javascript:void(0);" class="j-page">' + page + '</a></li>';
    };

    //刷新分页插件html
    Pagination.prototype.refreshHtml = function(){
        var _that = this;
        var html = '';

        var _$pagination = _that.$pageElement.find('.pagination');
        _$pagination.html('');


        //上一页
        html = html + '<li><a href="javascript:void(0);" class="j-previous">&lt;</a></li>';

        if(_that.options.totalPage <= _that.options.scopePage) {
            for(var i = 1; i <= _that.options.totalPage; i++) {
                html = html + _that.createNumPageHtml(i);
            }
        } else {
            var flag = (_that.options.scopePage - 1) / 2;
            var startIndex = _that.options.pageNo - flag;
            var endIndex = _that.options.pageNo + flag;

            //左边 开始页小于等于0
            if (startIndex <= 0) {
                startIndex = _that.options.pageNo - startIndex - 1;
                endIndex = endIndex + startIndex + 1;
            }
            // if(startIndex + 1 > flag) {
            //     html = html + '<li><a href="#">...</a></li>';
            // }

            //计算的止页大于总页数
            if (endIndex > _that.options.totalPage) {
                startIndex = startIndex + (_that.options.totalPage - endIndex);
                endIndex = _that.options.totalPage;
            }
            for(var i = startIndex; i <= endIndex; i++) {
                html = html + _that.createNumPageHtml(i);
            }

            // //省略页
            // if(endIndex + flag - 1 <= _that.options.totalPage) {
            //     html = html + '<li><a href="#">...</a></li>';
            // }
        }

        //下一页
        html = html + '<li><a href="javascript:void(0);" class="j-next">&gt;</a></li>';


        //首页
        html = html + '<li><a href="javascript:void(0);" class="j-first">首页</a></li>';

        //尾页
        html = html + '<li><a href="javascript:void(0);" class="j-last">末页</a></li>';

        _$pagination.append(html)
    };

    //插件html初始化
    Pagination.prototype.initHtml = function() {
        this.$pageElement.html('<ul class="pagination home-pagination"></ul>');
    };

    //初始化
    Pagination.prototype.init = function() {
        var _that = this;
        _that.initHtml();
        _that.bindEvent();
        setTimeout(function() {
            _that.options.init();
        },1);
        if(_that.options.isInitRefresh) {
            _that.refresh();
        }
    };

    Pagination.prototype.search = function() {
        var _that = this;
        _that.options.pageNo = 1;
        _that.options.refresh.call(_that, _that.options.pageNo, _that.options.pageSize);
    };

    //刷星
    Pagination.prototype.refresh = function() {
        var _that = this;
        _that.options.refresh.call(_that, _that.options.pageNo, _that.options.pageSize);
    };

    //绑定事件
    Pagination.prototype.bindEvent = function () {
        var _that = this;
        var _$pagination = _that.$pageElement;

        //上一页
        _$pagination.on('click', '.j-previous', function() {
            if(_that.options.pageNo > 1) {
                _that.options.pageNo = _that.options.pageNo - 1;
                _that.refresh();
            }
        });

        //下一页
        _$pagination.on('click', '.j-next', function() {
            if(_that.options.pageNo < _that.options.totalPage) {
                _that.options.pageNo = _that.options.pageNo + 1;
                _that.refresh();
            }
        });

        //跳转
        _$pagination.on('click', '.j-go', function() {
            _that.options.pageNo = parseInt(_$pagination.find('.j-goPage').val());
            if(_that.options.pageNo >= 1 && _that.options.pageNo <= _that.options.totalPage) {
                _that.refresh();
            }
        });

        //跳转
        _$pagination.on('click', '.j-page', function() {
            _that.options.pageNo = parseInt($(this).html());
            if(_that.options.pageNo >= 1 && _that.options.pageNo <= _that.options.totalPage) {
                _that.refresh();
            }
        });


        //首页
        _$pagination.on('click', '.j-first', function() {
            _that.options.pageNo = 1;
            _that.refresh();
        });

        //尾页
        _$pagination.on('click', '.j-last', function() {
            _that.options.pageNo = _that.options.totalPage;
            _that.refresh();
        });
    };


    return Pagination;

});
