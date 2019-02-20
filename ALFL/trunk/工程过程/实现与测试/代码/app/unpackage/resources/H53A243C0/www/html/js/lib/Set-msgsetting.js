
var vm = new Vue({
    el:'#app',
    data:{
        mobile:'',
        version:'',
        cache:'',
        is_accept_sysMsg : 0,
        is_accept_replylMsg : 0
    },
    mounted : function(){
        var loginUserInfo = utils.getLoginUserInfo(true);

        var that = this;

        rest.post({
            url: 'user/msgSeacher',
            data : {
                userId  : loginUserInfo.userId
            },
            success: function (result) {
                that.is_accept_sysMsg = result.data.is_accept_sysMsg || 0;
                that.is_accept_replylMsg = result.data.is_accept_replylMsg || 0;
            },
            fail: function (result) {
            }
        });

        document.getElementById("is_accept_replylMsg").addEventListener("toggle",function(event){
            if(event.detail.isActive){
                that.msgSetting(loginUserInfo.userId, 0, 1);
            }else{
                that.msgSetting(loginUserInfo.userId, 0, 0);
            }
        });

        document.getElementById("is_accept_sysMsg").addEventListener("toggle",function(event){
            if(event.detail.isActive){
                that.msgSetting(loginUserInfo.userId, 1, 1);
            }else{
                that.msgSetting(loginUserInfo.userId, 1, 0);
            }
        })
    },

    methods:{
        /**
         * 消息设置
         * @param userId
         * @param type 1：系统消息 0：回复消息
         * @param state 0：不接收，1接收
         */
        msgSetting : function (userId, type, state) {
            rest.post({
                url: 'user/msgSetting',
                data : {
                    userId  : userId,
                    type : type,
                    state: state
                },
                success: function (result) {
                },
                fail: function (result) {
                }
            });
        }

    }

});



