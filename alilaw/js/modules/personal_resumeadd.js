/*!
 * 新建简历
 * @author Li Hongwei
 * @date 2018-07-22 17:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');

    //判断是否登录
    utils.getLoginUserInfo(true);

    var constants = require('constants');
    var dataList = require('dataList');
    require('validator');
    require('WdatePicker');

    require('fileUpload');
    $("#file").createFileUpload({
        formData: {
            "name": 'files'
        },
        imageNum : 10,
        success: function(data) {},
        error: function(err) {
            C.msg.fail(err.msg);
        }
    });

    require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
        window.ZeroClipboard = zeroclipboard;
        var ue = UE.getEditor('selfAssessment');
        ue.ready(function() {ue.setHeight(200);});
    });

    var $form = $('#form');

    dataList.getExperienceList(function(result) {
        C.template('#experienceListTmpl').renderReplace(result, function() {});
    });

    dataList.getEducationList(function(result) {
        C.template('#educationListTmpl').renderReplace(result, function() {});
    });

    dataList.getSalList(function(result) {
        C.template('#salListTmpl').renderReplace(result, function() {});
    });

    dataList.getProvinceList(function(result) {
        C.template('#provinceListTmpl').renderReplace(result, function() {
            $('#province').trigger('change');
        });
    });

    $('#province').on('change', function() {
        rest.post({
            url: 'address/cityListByProvince/{pid}',
            urlParams : {
                pid : $('#province').val()
            },
            success: function (result) {
                C.template('#cityListTmpl').renderReplace(result, function() {});
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

    //保存
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        var loginUserInfo = utils.getLoginUserInfo(true);
        var _$form = $(form);
        rest.post({
            url: 'user/addResumeInfo',
            data : {
                userId : loginUserInfo.userId
            },
            form : _$form,
            success: function(result) {
                C.alert.ok(result.msg, function() {
                    if(window.opener) {
                        window.opener.location.reload();
                        window.close();
                    } else {
                        window.location.replace(constants.viewUrl + 'personal_consult.html');
                    }
                });
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

    //工作经历添加
    $form.on('click', '.j-add-workrow-btn', function() {
        var length = $form.find('.j-work-row').length + 1;
        $('.j-work-rownum').val(length);
        C.template('#workRowTmpl').loadAppend({index : length}, $('.j-work-box'), function () {

        });
    });

    /**
     * 排序
     * @param $row
     */
    function sort($row) {
        $row.each(function (index, e) {
            var _$that = $(this);
            var _index = index + 1;
            _$that.find('.j-title-index').html(_index);
            _$that.find('[data-name]').each(function (i, e) {
                var _$this = $(this);
                var name = _$this.data('name') + _index;
                if(_$this.attr('for')) {
                    _$this.attr('for', name);
                }

                if(_$this.attr('id')) {
                    _$this.attr('id', name);
                }

                if(_$this.attr('name')) {
                    _$this.attr('name', name);
                }
            });
        });
    }

    //工作经历删除
    $form.on('click', '.j-work-deleterow-btn', function() {
        $(this).parents('.j-work-row').remove();
        //名称重新排序
        sort($form.find('.j-work-row'));
        var length = $form.find('.j-work-row').length;
        $('.j-work-rownum').val(length);
        return false;
    });

    //教育经历添加
    $form.on('click', '.j-add-edurow-btn', function() {
        var length = $form.find('.j-edu-row').length+1;
        $('.j-edu-rownum').val(length);


        dataList.getEducationList(function(result) {
            var _data = {
                index : length,
                educationList :result.data
            };
            C.template('#eduRowTmpl').loadAppend(_data, $('.j-edu-box'), function () {

            });
        });
    });

    //教育经历删除
    $form.on('click', '.j-edu-deleterow-btn', function() {
        $(this).parents('.j-edu-row').remove();
        sort($form.find('.j-edu-row'));
        var length = $form.find('.j-edu-row').length;
        $('.j-edu-rownum').val(length);
        return false;
    });


});