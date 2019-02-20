var vm = new Vue({
    el:'#app',
    data:{
        pullId : '#pullrefresh',
        isNoList : false,
        list:[],
        pageNo:1,
        pageSize:10,
        conditionValue : '',
        nowDate : '2019-01'
    },
	created(){
		this.toList()
	},
    mounted: function() {
        var _that = this;



        mui.init({
            pullRefresh : {
                container:_that.pullId,//下拉刷新容器标识，querySelector能定位的css选择器均可，比如：id、.class等
                down : {
                    style:'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                    color:'#2BD009',
                    // auto: true,//可选,默认false.首次加载自动上拉刷新一次
                    indicators: true, //是否显示滚动条
                    callback :_that.pullDownRefresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
                },
                up: {
                    auto:true,
                    indicators: true, //是否显示滚动条
                    callback: _that.pullUpRefresh
                }
            }
        });

        mui("#app").on('tap', ".deletebtn",function(){
            mui.confirm('确定删除?', '提示', ['取消', '确定'], function(e) {
                var id = $(this).data('id');
                var scheduleId = $(this).data('scheduleid');


                if (e.index == 1) {
                    rest.post({
                        url: 'user/deleteSchedule',
                        data: {
                            id : id
                        },
                        success: function (result) {
                            mui.toast(result.msg);
                            _that.pullDownRefresh();
                        },
                        fail: function (result) {
                            mui.toast(result.msg);
                        }
                    });


                    rest.post({
                        url: 'user/deleteScheduleReminderTime',
                        data: {
                            id : scheduleId
                        },
                        success: function (result) {
                            mui.toast(result.msg);
                        },
                        fail: function (result) {
                            mui.toast(result.msg);
                        }
                    });
                }
            });
            return false;
        });


        (function($) {
            $.init();
            var btns = $('.j-btn');
            btns.each(function(i, btn) {
                btn.addEventListener('tap', function() {
                    var _self = this;
                    if(_self.picker) {
                        _self.picker.show(function (rs) {
                            _that.nowDate = rs.text;
//                                result.innerText = '选择结果: ' + rs.text;
                            _self.picker.dispose();
                            _self.picker = null;
                            _that.pullDownRefresh();

                        });
                    } else {
                        var optionsJson = this.getAttribute('data-options') || '{}';
                        var options = JSON.parse(optionsJson);
                        var id = this.getAttribute('id');
                        /*
                         * 首次显示时实例化组件
                         * 示例为了简洁，将 options 放在了按钮的 dom 上
                         * 也可以直接通过代码声明 optinos 用于实例化 DtPicker
                         */
                        _self.picker = new $.DtPicker(options);
                        _self.picker.show(function(rs) {
                            /*
                             * rs.value 拼合后的 value
                             * rs.text 拼合后的 text
                             * rs.y 年，可以通过 rs.y.vaue 和 rs.y.text 获取值和文本
                             * rs.m 月，用法同年
                             * rs.d 日，用法同年
                             * rs.h 时，用法同年
                             * rs.i 分（minutes 的第二个字母），用法同年
                             */
//                                result.innerText = '选择结果: ' + rs.text;
                            _that.nowDate = rs.text;
                            _that.pullDownRefresh();


                            /*
                             * 返回 false 可以阻止选择框的关闭
                             * return false;
                             */
                            /*
                             * 释放组件资源，释放后将将不能再操作组件
                             * 通常情况下，不需要示放组件，new DtPicker(options) 后，可以一直使用。
                             * 当前示例，因为内容较多，如不进行资原释放，在某些设备上会较慢。
                             * 所以每次用完便立即调用 dispose 进行释放，下次用时再创建新实例。
                             */
                            _self.picker.dispose();
                            _self.picker = null;
                        });
                    }
                }, false);
            });
        })(mui);

        mui(".mui-scroll").on('tap',"a",function(){
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });

    },
    methods:{
        tapSearch : function () {
            var _that = this;
            _that.conditionValue = _that.$refs.conditionValue.value;
            _that.pullDownRefresh();
        },

        pullDownRefresh : function() {
            var _that = this;

            setTimeout(function() {
                _that.list = [];
                _that.pageNo = 1;
                _that.toList(_that.downSuccess);//具体取数据的方法
            }, 500);
        },

        pullUpRefresh : function () {
            var _that = this;
            setTimeout(function() {
                _that.toList(_that.upSuccess);//具体取数据的方法
            }, 500);
        },

        upSuccess : function(result){
            var _that = this;
            if(null == result.rows || result.rows.length == 0) {
                mui(_that.pullId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function(){
                    mui(_that.pullId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.pullId).pullRefresh().endPullupToRefresh(false);
                _that.list = _that.list.concat(result.rows);
                _that.pageNo++;
            }
        },

        downSuccess : function(result){
            var _that = this;
            if(null == result.rows || result.rows.length == 0) {
                _that.isNoList = true;
                mui(_that.pullId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.pullId).pullRefresh().endPulldownToRefresh(false);
                _that.list = result.rows;
                _that.pageNo++;
            }
        },

        toList : function(func) {
            var loginUserInfo = utils.getLoginUserInfo(true);

            var _that = this;
            rest.post({
                url: 'user/scheduleSearchALL/{pageNo}/{pageSize}',
                urlParams : {
                    pageNo  : _that.pageNo,
                    pageSize : _that.pageSize
                },
                data: {
                    userId : loginUserInfo.userId,
                    // gregorianCalendar : _that.nowDate,//时间 (格式为：2018-10 )
                    content : ''//内容
                },
                success: function (result) {
                    func(result)
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
        }
    }
});









