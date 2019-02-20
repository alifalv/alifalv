/*!
 * 求职招聘(企业)
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var constants = require('constants');
    utils.checkUserType(constants.userType.enterprise);


    require('validator');

    var loginUserInfo = utils.getLoginUserInfo(true);
    var loginUserId = loginUserInfo.userId;
    var Page = require('page');

    var dataList = require('dataList');
    var $companyInfoForm = $('#companyInfoForm');

    /**
     * form表单设值
     * @param _data
     */
    function setFormValue($form, _data) {
        $form.find('option').prop('selected', false);
        for(var key in _data) {
            var _$obj = $form.find('[name="' + key + '"]');
            if(_$obj.is("select")) {
                _$obj.find('option[value="' + _data[key] + '"]').prop('selected', true);
            } else {
                _$obj.val(_data[key]);
            }
        }
    }

    /**
     * 初始化地区下拉菜单
     * @param provinceSelectedId
     * @param citySelectedId
     */
    function initProvinceCitySelect(provinceSelectedId, citySelectedId) {
        dataList.getProvinceList(function(result) {
            if(provinceSelectedId) {
                result.selectedId = provinceSelectedId;
            }
            C.template('#province_tmpl').renderReplace(result, function() {
                $('#province_select').trigger('change');
            });
        });

        $('#province_select').on('change', function() {
            rest.post({
                url: 'address/cityListByProvince/{pid}',
                urlParams : {
                    pid : $('#province_select').val()
                },
                success: function (result) {
                    if(provinceSelectedId) {
                        result.selectedId = citySelectedId;
                    }
                    C.template('#city_tmpl').renderReplace(result, function() {

                    });
                },
                fail: function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    }

    /**
     * 初始化公司信息
     */
    function initCompanyInfoForm() {
        rest.post({
            url: 'user/getCompanyInfo/{userId}',
            urlParams : {
                userId : loginUserId
            },
            success: function (data) {
                require([ 'zeroclipboard','ueditor.ext'], function (zeroclipboard) {
                    window.ZeroClipboard = zeroclipboard;
                    var ue = UE.getEditor('companyDesc');
                    ue.ready(function() {ue.setHeight(200);});
                    if(data.data) {
                        ue.setContent(data.data.companyDesc);
                    }
                });

                if(data.data) {
                    initProvinceCitySelect(data.data.province, data.data.city);
                    setFormValue($companyInfoForm, data.data);
                } else {
                    initProvinceCitySelect();
                }
            },
            fail: function (result) {
                C.msg.fail(result.msg);
            }
        });
    }

    initCompanyInfoForm();


    //保存企业信息
    var companyInfoFormValid = $companyInfoForm.validator(constants.validator);
    companyInfoFormValid.on('valid.form', function(e, form){
        rest.post({
            url: 'user/addCompany',
            form : $companyInfoForm,
            data : {
                type : 1,//1企业 2咨询师
                userId: loginUserId
            },
            success: function(result) {
                if(result.data) {
                    setFormValue($companyInfoForm, result.data);
                }
                C.msg.ok(result.msg);
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

    //收藏的简历
    var $collectionResumeList = $('#collectionResumeList');
    var collectionResumeListPage = new Page($collectionResumeList, {
        pageType : 'home',
        url : 'user/collectionResumeList/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            userId: loginUserId
        },
        pageSize : 5
    });

    //删除收藏的简历
    $collectionResumeList.on('click', '.j-delete-btn', function() {
        var collectionId = $(this).data('collectionid');
        C.confirm('确认删除该简历？',function () {
            rest.post({
                url: 'user/deleteCollectionResumeList',//134.企业删除收藏的简历
                data : {
                    collectionId: collectionId
                },
                success: function(result) {
                    C.msg.ok(result.msg);
                    collectionResumeListPage.refresh();
                },
                fail : function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    });


    //发布的岗位
    var $seacherJobList = $('#seacherJobList');
    var seacherJobListPage = new Page($seacherJobList, {
        pageType : 'home',
        url : 'user/seacherJob/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            userId: loginUserId
        },
        pageSize : 5
    });

    //发布的岗位-删除
    $seacherJobList.on('click', '.j-delete-btn', function() {
        var advertiseId = $(this).data('advertiseid');
        C.confirm('确认删除该岗位？',function () {
            rest.post({
                url: 'user/deleteJob',
                data : {
                    userId: loginUserId,
                    advertiseId : advertiseId
                },
                success: function(result) {
                    C.msg.ok(result.msg);
                    seacherJobListPage.refresh();
                },
                fail : function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    });


    //收到的简历
    var $collecResumeList = $('#collecResumeList');
    var collecResumeListPage = new Page($collecResumeList, {
        pageType : 'home',
        url : 'user/collecResumeList/{userId}/{pageNo}/{pageSize}',
        urlParams : {
            userId: loginUserId
        },
        pageSize : 5
    });

    //删除收到的简历
    $collecResumeList.on('click', '.j-delete-btn', function() {
        var collectionId = $(this).data('collectionid');
        C.confirm('确认删除？',function () {

            //130.企业删除收到的简历
            rest.post({
                url: 'user/companyDelSendAdvertise',
                data : {
                    Id : collectionId
                },
                success: function(result) {
                    C.msg.ok(result.msg);
                    collecResumeListPage.refresh();
                },
                fail : function (result) {
                    C.msg.fail(result.msg);
                }
            });
        });
    });

});
