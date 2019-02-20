/*!
 * 上传图片
 * @author Li Hongwei
 * @date 2018-07-14 15:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    require('ueditor.internal');
    var utils = require('utils');

    require('imgUpload');
    $("#file").createImgUpload({
        formData: {
            "name": 'imgs'
        },
        imageNum : 1,
        success: function(data) {
            editor.execCommand('inserthtml', '<img src="' + utils.createImgUrl(data.data) + '">');
            dialog.close();
        },
        fail: function(err) {
            C.msg.fail(err.msg);
        }
    });



});
