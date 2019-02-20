define({
    debug: true,
    baseUrl: location.protocol + '//' + window.location.host + '/',
    apiUrl: location.protocol + '//' + window.location.host + '/ali-legal/api/', //api地址
    viewUrl: location.protocol + '//' + window.location.host + '/', //页面地址
    // home_sld : '.simplyto.com',//律师个人主页二级域名
    home_sld: '.alifalv.cn', //律师个人主页二级域名
    validator: {
        debug: true,
        /**
         * 实时验证
         * 0：关闭实时验证，只在提交表单的时候执行验证
         1：输入框失去焦点（focusout）时执行验证
         2：输入框改变值（input）时执行验证
         3：输入框失去焦点和改变值（综合 1 + 2） (v0.8.0+)
         8：同 2，并且详细提示每个规则的结果 (v0.9.0+)
         9：同 3，并且详细提示每个规则的结果 (v0.9.0+)
         */
        timely: 2,
        stopOnError: true, //在第一次错误时停止验证
        theme: 'yellow_right_effect'
    },
    wxAppId: 'wx9ecfe9dff1e2c47c',
    wxAppSecret: 'b6c49586515572a46960bdace4c5fa3b',
    // qqAppId: true ? '101494455' : '101494453', //test
    qqAppId: '101494453',
    umeditorPath: '../js/libs/umeditor/',
    ueditorPath: '../js/libs/ueditor/',
    openType: {
        sina: 'WEIBO',
        qq: 'QQ',
        wechat: 'WECHAT'
    },
    columnCode: {
        0: "百姓吐槽",
        1: "法律疑难问题与观点",
        2: "公务员招聘",
        3: "常用文书模板",
        4: "文书制作知识",
        5: "法律人信息",
        6: "咨询问题",
        7: "法律培训",
        8: "法律法规",
        9: "阿里裁决",
        10: "我的风采",
        11: "工作动态",
        12: "成功案例",
        13: "我的随笔"
    },
    userType: {
        advocate: '1',
        counselor: '2',
        enterprise: '3'
    },
    enterpriseYearCouponNum: [4, 12, 50] //企业会员每年免费文书券的张数
});