/*!
 * dataList
 * @author Li Hongwei
 * @date 2018-07-03 17:36
 * @version 1.0
 */
define(function (require, exports, module) {
    var _urlList = {
        jobList : 'systemConfig/jobList', //职业列表
        caseList : 'systemConfig/caseList',
        provinceList : 'address/provinceList', //省份信息
        driverLevelList : 'systemConfig/driverList',//驾驶证等级
        vipLevelList : 'systemConfig/levelList',
        allCityList : 'address/allCityList',
        experienceList : 'systemConfig/experienceList',//工作经验
        educationList : 'systemConfig/educationList',//学历要求
        salList : 'systemConfig/salList',//工资范围
        effectivenessGradeList : 'systemConfig/effectivenessGradeList',//效力等级
        unitList : 'systemConfig/unitList' //发文单位列表
    };

    function _getList(url, callback, failback, errorback) {
        rest.post({
            url: url,
            success: function (result) {
                callback&&callback(result);
            },
            fail: function (result) {
                failback&&failback(result);
            },
            error : function() {
                errorback&&errorback();
            }
        });
    }

    return {
        getJobList: function (callback) {
            _getList(_urlList.jobList, callback);
        },

        getCaseList : function(callback) {
            _getList(_urlList.caseList, callback);
        },

        getProvinceList : function(callback) {
            _getList(_urlList.provinceList, callback);
        },

        getDriverLevelList : function(callback) {
            _getList(_urlList.driverLevelList, callback);
        },

        getVipLevelList : function(callback) {
            _getList(_urlList.vipLevelList, callback);
        },

        getAllCityList : function(callback, failback, errorback) {
            _getList(_urlList.allCityList, callback, failback, errorback);
        },

        getExperienceList : function (callback, failback, errorback) {
            _getList(_urlList.experienceList, callback, failback, errorback);
        },

        getEducationList : function (callback, failback, errorback) {
            _getList(_urlList.educationList, callback, failback, errorback);
        },

        getSalList : function (callback, failback, errorback) {
            _getList(_urlList.salList, callback, failback, errorback);
        },

        getEffectivenessGradeList : function (callback, failback, errorback) {
            _getList(_urlList.effectivenessGradeList, callback, failback, errorback);
        },

        getUnitList : function (callback, failback, errorback) {
            _getList(_urlList.unitList, callback, failback, errorback);
        }
    };



});