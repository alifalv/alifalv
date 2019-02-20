/*!
 * 法律咨询列表
 * @author Li Hongwei
 * @date 2018-07-22 17:12
 * @version 1.0
 */
define(function(require, exports, module) {
    "use strict";

    var Page = require('page');
    var utils = require('utils');
    require('WdatePicker');

    var dataList = require('dataList');

    dataList.getProvinceList(function(result) {
        C.template('#province_tmpl').renderReplace(result, function() {
            $('#province_select').trigger('change');
        });
    });

    $('#province_select').on('change', function() {
        var province_select_val = $('#province_select').val();
        if (province_select_val == '') {
            C.template('#city_tmpl').renderReplace({}, function() {});
            return false;
        }
        rest.post({
            url: 'address/cityListByProvince/{pid}',
            urlParams: {
                pid: province_select_val
            },
            success: function(result) {
                C.template('#city_tmpl').renderReplace(result, function() {});
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            }
        });
        return false;
    });

    //法律培训
    var loginUserInfo = utils.getLoginUserInfo();
    new Page($('#trainingList'), {
        $form: $('#trainingListForm'),
        url: 'article/legalTrainList/{pageNo}/{pageSize}',
        data: {
            model: 1
        },
        htmlLoad: function($html) {

            //如果是登录的普通用户隐藏发布咨询按钮
            if (loginUserInfo && loginUserInfo.userType && loginUserInfo.userType == '1') {
                $('.j-fbpx-btn').hide();
            } else {
                $('.j-fbpx-btn').show();
            }
        },
        pageSize: 8
    });

    //法律人的信息
    new Page($('#lawPersionList'), {
        url: 'article/legalPersonInfoList/{model}/{pageNo}/{pageSize}',
        urlParams: {
            model: 1
        },
        pageSize: 8
    });


});