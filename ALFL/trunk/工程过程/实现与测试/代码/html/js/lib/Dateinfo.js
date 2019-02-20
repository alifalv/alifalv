var vm = new Vue({
    el:'#app',
    data:{
        isNoList : false,
        list:[],
        pageNo:1,
        pageSize:10000,
        nowDate : '',
        dateText: '',
        editList : {},
        contentList : {}
    },
    mounted: function() {
        var _that = this;
        var urlParams = utils.getUrlParams();
        _that.nowDate =  urlParams.gregorianCalendar;
        _that.dateText =  urlParams.text;
        _that.refresh();
    },
    methods:{
        refresh : function () {
            var _that = this;
            _that.toList(function (result) {
                _that.list = result.rows;
                if(null == result.rows || result.rows.length == 0) {
                    _that.isNoList = true;
                }
            });

            // _that.list = [{"gregorianCalendar":"2018-11-10 9:10","week":"星期六","lunarCalendar":"2018年十月初三","name":"aa","id":14,"userId":1,"content":"332112","scheduleId":16}]
        },

        editbtn : function(id, scheduleId) {
            var _that = this;
            _that.editList[id] = true;
            _that.refresh();
        },

        savebtm : function(id, scheduleId) {
            var _that = this;

            rest.post({
                url: 'user/updateSchedule',

                data: {
                    id: id,//日程id
                    name: '默认标题',//标题
                    content: $('#content' + id).val()//内容
                },
                success: function (result) {
                    _that.editList[id] = false;

                    mui.toast(result.msg);
                    _that.refresh()
                },
                fail: function (result) {
                    mui.toast(result.msg);
                }
            });
        },

        deletebtn : function(id, scheduleId) {
            var _that = this;
            mui.confirm('确定删除?', '提示', ['取消', '确定'], function(e) {
                if (e.index == 1) {
                    rest.post({
                        url: 'user/deleteSchedule',
                        data: {
                            id: id//日程id
                        },
                        success: function (result) {
                            mui.toast(result.msg);
                            _that.refresh();
                        },
                        fail: function (result) {
                            mui.toast(result.msg);
                        }
                    });

                    rest.post({
                        url: 'user/deleteScheduleReminderTime',
                        data: {
                            id: scheduleId//日程id
                        },
                        success: function (result) {
                        },
                        fail: function (result) {
                            mui.toast(result.msg);
                        }
                    });
                }
            });
        },

        toList : function(func) {
            var loginUserInfo = utils.getLoginUserInfo(true);

            var _that = this;
            rest.post({
                url: 'user/scheduleSearch/{pageNo}/{pageSize}',
                urlParams : {
                    pageNo  : _that.pageNo,
                    pageSize : _that.pageSize
                },
                data: {
                    userId : loginUserInfo.userId,
                    gregorianCalendar : _that.nowDate,//时间 (格式为：2018-10 )
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









