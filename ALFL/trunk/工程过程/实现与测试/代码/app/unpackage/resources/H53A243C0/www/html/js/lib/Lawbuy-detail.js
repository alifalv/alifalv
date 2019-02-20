var vm = new Vue({
    el: '#app',
    data: {
        data: {},
        caseId: '',
        authorUserName: null, //发布文章的用户名
        authorUserImg: null, //发布文章的用户头像
        authorUserId: null, //发布文章的用户id
        serviceName: null //标题
    },
    mounted: function() {
        var _that = this;
        var urlParams = utils.getUrlParams();
        _that.caseId = urlParams.caseId

        rest.post({
            url: 'case/caseDetails/{caseId}',
            urlParams: {
                caseId: urlParams.caseId,
            },
            success: function(result) {
                _that.data = result.data;
                _that.authorUserName = result.data.userName;
                _that.authorUserImg = result.data.userImg;
                _that.authorUserId = result.data.userId;
                _that.serviceName = result.data.title;
            }
        });

    },

    methods: {

        jinPai: function() {
            var _that = this;
            window.location.href = 'Lawbuy-JingPai.html?caseId=' + _that.caseId + '&authorUserId=' + _that.authorUserId;
        }
    }

});