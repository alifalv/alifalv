/* 
 *这只是其中一部分，在裁剪过程中需要用到的库函数 
 *主要是方便，随时可以获取保存在localstorage中的头像base64图
*/

(function($, owner) {
    
     // 获取用户个人信息
    owner.getUserInfo = function() {
        var userText = localStorage.getItem('$user') || "{}";
        return JSON.parse(userText);
    }

     // 存储用户个人信息
    owner.setUserInfo = function(user) {
        user = user || {};
        localStorage.setItem('$user', JSON.stringify(user));
    }

    // 获取用户头像
    owner.getHeadImg = function() {
        var headImg = owner.getUserInfo().my_icon || "";
        return headImg;
    }

    //设置用户头像
    owner.setHeadImg = function(baseData) {
        var userInfo = owner.getUserInfo();
        userInfo.my_icon = baseData; //只对my_icon这一项进行修改，其他不动
        owner.setUserInfo(userInfo);
    }
    

}(mui, window.app = {}));