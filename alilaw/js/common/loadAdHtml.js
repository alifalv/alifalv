/*!
 * 广告
 * @author Li Hongwei
 * @date 2015-11-05
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');

    $(document).find('[ad]').each(function() {
        var adId = $(this).attr('ad');
        var uuid = utils.uuid();
        $(this).attr('id', uuid);

        rest.post({
            url: 'user/getAdvertisingById/{nid}',
            urlParams: {
                nid : adId
            },
            success: function (result) {
                C.template('#' + uuid).renderReplace(result, function($html) {
                    $html.find('img').one('error',function () {
                        $(this).attr('src', '../img/img-error.jpg');
                    });
                });
            },
            fail: function (result) {
                result.error = result.msg || '接口问题';
                C.template('#' + uuid).renderReplace(result, function($html) {
                    $html.find('img').one('error',function () {
                        $(this).attr('src', '../img/img-error.jpg');
                    });
                });
            },
            error :function() {
                var result = {
                    error : '连接失败'
                };
                C.template('#' + uuid).renderReplace(result, function($html) {
                    $html.find('img').one('error',function () {
                        $(this).attr('src', '../img/img-error.jpg');
                    });
                });
            }
        });
    });

});