/*!
 * 收支明细
 * @author Li Hongwei
 * @date 2018-07-22 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var utils = require('utils');
    var loginUserInfo = utils.getLoginUserInfo(true);

    var constants = require('constants');
    require('validator');

    var Page = require('page');

    var $listWalletRecordList = $('#listWalletRecordList');

    //我的钱包
    var listWalletRecordListPage = new Page($listWalletRecordList, {
        pageType : 'home',
        url : 'walletRecord/listWalletRecord/{pageNo}/{pageSize}',
        data : {
            userId: loginUserInfo.userId
        },
        pageSize : 8,
        htmlLoad : function(html, tmpl, data) {
            $('#balance').html(loginUserInfo.userBalance || '0.00');
            $('#myModal').find('.j-dialog-balance').html(loginUserInfo.userBalance || '0.00')
        }
    });



    //提现
    var tkFormDialog;
    $listWalletRecordList.on('click', '.j-tx-btn', function() {
        tkFormDialog = C.dialog({
            title : false,
            closeBtn : 0,
            area: ['560px', '485px'],
            content: $('#myModal'),
            success : function(layero, index) {
                var realName = loginUserInfo.realName;
                var bankAccount = loginUserInfo.bankAccount;
                if(loginUserInfo.realName) {
                    layero.find('#realNameBankAccount').val(realName).prop('disabled', true);
                }

                layero.on('click', '.ui-tk-close', function() {
                    layer.close(tkFormDialog);
                    return false;
                });

                layero.find('.j-dialog-balance').html($('#balance').html());

                layero.on('change', '.j-payment-btn', function () {
                    if($(this).val() == '微信支付') {
                        layero.find('#realNameBankAccount_title').html('真实姓名');
                        layero.find('#realNameBankAccount').val(realName);
                        if(realName) {
                            layero.find('#realNameBankAccount').prop('disabled', true);
                        } else {
                            layero.find('#realNameBankAccount').prop('disabled', false);
                        }
                        layero.find('#rz_tip').css('visibility','hidden');
                    } else {
                        layero.find('#realNameBankAccount_title').html('银行卡号');
                        layero.find('#realNameBankAccount').val(bankAccount);
                        if(bankAccount) {
                            layero.find('#realNameBankAccount').prop('disabled', true);
                            layero.find('#rz_tip').css('visibility','hidden');
                        } else {
                            layero.find('#rz_tip').css('visibility','inherit');
                            layero.find('#realNameBankAccount').prop('disabled', false);
                        }
                    }
                    return false;
                });
            }
        });
    });

    var valid_options = constants.validator;
    valid_options.theme = 'yellow_bottom';
    var $tk_form = $(document).find('#tk_form');
    var form_valid = $tk_form.validator(valid_options);
    form_valid.on('valid.form', function(e, form){
        var userInfo = utils.getLoginUserInfo(true);
        $tk_form.validator('holdSubmit');

        rest.post({
            form : $tk_form,
            url: 'order/withdrawDeposit',
            data : {
                userId :userInfo.userId
                //money：提现金额
                //payment：//提现方式【微信支付：银行卡支付】二种方式
            },
            success : function(result) {
                C.alert.ok(result.msg, function() {
                    layer.close(tkFormDialog);
                    listWalletRecordListPage.refresh();
                });
            },
            fail: function(result) {
                C.msg.fail(result.msg);
            },
            complete : function() {
                $tk_form.validator('holdSubmit', false);
            }
        });

    });

});
