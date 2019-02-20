/*!
 * 修改简历
 * @author Li Hongwei
 * @date 2018-07-22 17:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var urlParams = utils.getUrlParams();
    var resumeId = urlParams.resumeId;
    if(null == resumeId || '' == resumeId) {
        utils.jump404('简历编号为空');
        return false;
    }


    //判断是否登录
    var loginUserInfo = utils.getLoginUserInfo(true);

    var constants = require('constants');
    var dataList = require('dataList');
    require('validator');
    require('WdatePicker');

    require('fileUpload');



    //初始化文件
    function initFileUpload(fileUrl) {
        var _options ={
            isShowAddBtn : true,
            formData: {
                "name": 'files'
            },
            imageNum : 10,
            success: function(data) {},
            error: function(err) {
                C.msg.fail(err.msg);
            }
        };
        if(fileUrl) {
            _options.fileUrl = fileUrl;
        }
        $("#file").createFileUpload(_options);
    }

    var $form = $('#form');

    //初始化教育
    function initEduList(eduList) {
        var eduListLength = eduList.length;
        $('.j-edu-rownum').val(eduListLength);
        dataList.getEducationList(function(selectEducationList) {
            for(var i = 0; i < eduListLength; i++) {
                var _data = {
                    index : i + 1,
                    educationList :selectEducationList.data
                };
                var rowData = $.extend({},_data, {edu : eduList[i]});
                C.template('#eduRowTmpl').loadAppend(rowData, $('.j-edu-box'), function () {});
            }
        });
    }



    //工作经历
    function initWorkList(workList) {
        var workListLength = workList.length;
        $('.j-work-rownum').val(workListLength);

        for(var i = 0; i < workListLength; i++) {
            var rowData = $.extend({},{index : i + 1}, {work : workList[i]});
            C.template('#workRowTmpl').loadAppend(rowData, $('.j-work-box'), function () {});
        }
    }

    function initResumeForm() {
        rest.post({
            url: 'user/getResumeInfo/{userId}',
            urlParams : {
                userId : loginUserInfo.userId
            },
            success: function (result) {
                require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
                    window.ZeroClipboard = zeroclipboard;
                    var ue = UE.getEditor('selfAssessment');
                    ue.ready(function() {ue.setHeight(200);});
                    if(result.data) {
                        ue.setContent(result.data.selfAssessment);
                    }
                });


                if(result.data) {
                    resumeId = result.data.resumeId;
                    initSelect(
                        result.data.workExperience,
                        result.data.qualification,
                        result.data.sal,
                        result.data.province,
                        result.data.city
                    );
                    utils.setFormValue($form, result.data);

                    var fileUrls = [];
                    for(var i = 0; i<result.data.fileList.length; i++) {
                        fileUrls.push(result.data.fileList[i].fileName);
                    }
                    initFileUpload(fileUrls);

                    //初始化教育
                    initEduList(result.data.eduList);
                    initWorkList(result.data.experienceList);
                } else {
                    utils.jump500('接口数据为空');
                }

            },
            fail: function (result) {
                utils.jump500(result.msg);
            }
        });

    }

    /**
     *
     * @param experienceId
     * @param educationId
     * @param salId
     * @param provinceId
     * @param cityId
     */
    function initSelect(experienceId, educationId, salId, provinceId, cityId) {
        dataList.getExperienceList(function(result) {
            if(experienceId) {
                result.selectedId = experienceId;
            }
            C.template('#experienceListTmpl').renderReplace(result, function() {});
        });

        dataList.getEducationList(function(result) {
            if(educationId) {
                result.selectedId = educationId;
            }
            C.template('#educationListTmpl').renderReplace(result, function() {});
        });

        dataList.getSalList(function(result) {
            if(salId) {
                result.selectedId = salId;
            }
            C.template('#salListTmpl').renderReplace(result, function() {});
        });

        dataList.getProvinceList(function(result) {
            if(provinceId) {
                result.selectedId = provinceId;
            }
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
                    if(cityId) {
                        result.selectedId = cityId;
                    }
                    C.template('#cityListTmpl').renderReplace(result, function() {});
                },
                fail: function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    }

    //保存修改
    var form_valid = $form.validator(constants.validator);
    form_valid.on('valid.form', function(e, form){
        var _loginUserInfo = utils.getLoginUserInfo(true);
        var _$form = $(form);
        rest.post({
            url: 'user/updateResumeInfo',
            data : {
                resumeId : resumeId,
                userId : _loginUserInfo.userId
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
        C.template('#workRowTmpl').loadAppend({index : length, work : []}, $('.j-work-box'), function () {});
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
                educationList :result.data,
                edu : []
            };
            C.template('#eduRowTmpl').loadAppend(_data, $('.j-edu-box'), function ($html) {
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

    initResumeForm();

});