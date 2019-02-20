/*!
 * ueditor 扩展
 * @author Li Hongwei
 * @date 2018-09-14 15:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    UE.registerUI('uploadImg',function(editor,uiName){
        var dialog = new UE.ui.Dialog({
            editor:editor,
            name:uiName,
            title:"选择一张图片插入",
            cssRules:"width:240px;height:220px;",
            iframeUrl:'imgUploadTmpl.html',
            buttons:[{
                className:'edui-cancelbutton',
                label:'取消',
                onclick:function () {
                    dialog.close(false);
                }
            }]
        });

        // 创建一个按钮，点击后出发对话框
        var btn = new UE.ui.Button({
            title:'插入图片',
            //自定义按钮样式
            name:'myFirst'+uiName,
            cssRules :'background-position: -380px 0;',
            onclick:function () {
                //渲染对话框并弹出
                dialog.render();
                dialog.open();
            }
        });

        return btn;
    });
});
