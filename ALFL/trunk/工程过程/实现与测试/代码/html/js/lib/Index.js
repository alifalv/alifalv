new Vue({
    el: '#app',
    data: {
        tjlsList: [],

        newAskList: [], //最新资讯
        isNoNewAskList: false,
        newAskPageNo: 1,
        newAskPageSize: 5,
        newAskListId: '#newAskList',
        legalList: [],
        newCaseList: [], //最新案源
        isNoNewCaseList: false,
        newCasePageNo: 1,
        newCasePageSize: 5,
        newCaseListId: '#newCaseList'
    },
    mounted: function() {
        var that = this;

        rest.post({
            url: 'article/legalList/{pageNo}/{pageSize}',
            data: {
                title: '',
                articleContent: '',
                effectLevel: '',
                sendUnit: '',
                startTime: '',
                endTime: ''
            },
            urlParams: {
                pageNo: 1,
                pageSize: 10
            },

            success: function(result) {
                that.legalList = result.rows;
                myswiper();
                myswiper2();
            },
            fail: function(result) {
                mui.toast(result.msg);
            }
        });

        rest.post({
            isNeedToken: false,
            url: 'user/adminRecommendCounselorList',
            data: {
                city: utils.getCityId()
            },
            success: function(result) {
                that.tjlsList = result.data;
            },
            fail: function(result) {}
        });


        mui.init();
        mui(that.newAskListId).pullRefresh({

            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.newAskUpRefresh
            }
        });

        mui(that.newCaseListId).pullRefresh({

            up: {
                auto: true,
                indicators: true, //是否显示滚动条
                callback: that.newCaseUpRefresh
            }
        });
        mui('#newAskList').pullRefresh().endPulldownToRefresh(true); //暂时禁止滚动
        mui("#consultation,#newAskList, #newCaseList").on('tap', "a", function() {
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });
        mui("#lihref").on('tap', ".flex_Horizontal", function() {
            var href = this.getAttribute("href");
            creatMywebview(href, href);
            return false;
        });
    },
    methods: {

        newAskDownRefresh: function() {
            var _that = this;

            setTimeout(function() {
                _that.newAskList = [];
                _that.newAskPageNo = 1;
                _that.toNewAskList(_that.newAskDownSuccess); //具体取数据的方法
            }, 500);
        },

        newAskUpRefresh: function() {
            var _that = this;
            setTimeout(function() {
                _that.toNewAskList(_that.newAskUpSuccess); //具体取数据的方法
            }, 500);
        },

        newAskUpSuccess: function(result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.newAskListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function() {
                    mui(_that.newAskListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.newAskListId).pullRefresh().endPullupToRefresh(false);
                _that.newAskList = _that.newAskList.concat(result.rows);
                _that.newAskPageNo++;
            }
        },

        newAskDownSuccess: function(result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.isNoNewAskList = true;
                mui(_that.newAskListId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.newAskListId).pullRefresh().endPulldownToRefresh(false);
                _that.newAskList = result.rows;
                _that.newAskPageNo++;
            }
        },

        //最新咨询
        toNewAskList: function(func) {
            var _that = this;
            rest.post({
                isNeedToken: false,
                url: 'advice/adviceList/{pageNo}/{pageSize}',
                urlParams: {
                    pageNo: _that.newAskPageNo,
                    pageSize: _that.newAskPageSize
                },
                data: {
                    queryType: 1,
                    conditionValue: _that.conditionValue
                },
                success: function(result) {
                    func(result)
                },
                fail: function(result) {}
            });
        },

        newCaseDownRefresh: function() {
            var _that = this;

            setTimeout(function() {
                _that.newCaseList = [];
                _that.newCasePageNo = 1;
                _that.toNewCaseList(_that.newCaseDownSuccess); //具体取数据的方法
            }, 500);
        },

        newCaseUpRefresh: function() {
            var _that = this;
            setTimeout(function() {
                _that.toNewCaseList(_that.newCaseUpSuccess); //具体取数据的方法
            }, 500);
        },

        newCaseUpSuccess: function(result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                mui(_that.newCaseListId).pullRefresh().endPullupToRefresh(true);
                setTimeout(function() {
                    mui(_that.newCaseListId).pullRefresh().refresh(true);
                }, 2000);
            } else {
                mui(_that.newCaseListId).pullRefresh().endPullupToRefresh(false);
                _that.newCaseList = _that.newCaseList.concat(result.rows);
                _that.newCasePageNo++;
            }
        },

        newCaseDownSuccess: function(result) {
            var _that = this;
            if (null == result.rows || result.rows.length == 0) {
                _that.isNoNewCaseList = true;
                mui(_that.newCaseListId).pullRefresh().endPulldownToRefresh(true);
            } else {
                mui(_that.newCaseListId).pullRefresh().endPulldownToRefresh(false);
                _that.newCaseList = result.rows;
                _that.newCasePageNo++;
            }
        },

        //最新案源
        toNewCaseList: function(func) {
            var _that = this;
            rest.post({
                isNeedToken: false,
                url: 'case/caseList/{pageNo}/{pageSize}',
                urlParams: {
                    pageNo: _that.newCasePageNo,
                    pageSize: _that.newCasePageSize
                },
                data: {
                    conditionValue: _that.conditionValue,
                    is_delete: 0
                },
                success: function(result) {
                    func(result)
                },
                fail: function(result) {}
            });
        }
    }
});


function myswiper() {
    var swiper = new Swiper('.swiper-container', {
        //effect: 'flip',
        loop: true,
        autoplay: 5000,
        pagination: '.swiper-pagination',
        paginationClickable: true
    });
}

function myswiper2() {
    var swiper = new Swiper('.swiper-container2', {
        loop: true,
        autoplay: 2000,
        direction: 'vertical',
        pagination: '.swiper-pagination',
        paginationClickable: true
    });
}