/*!
 * 找回密码
 * @author Li Hongwei
 * @date 2018-08-06 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";
    var constants = require('constants');
    require('validator');
    var utils = require('utils');

    var isReg=0;
    $("#mobile").blur(function(){
	    	var mobel= $('#mobile').val();
	    	$.ajax({
				url:'/ali-legal/api/user/checkMobile',
				type:'post',
				data:{"mobile":mobel},
				dataType:"json",
				success:function(data){
					if(data.mobileNumber=='N'){
						isReg=1;
						C.msg.fail('该手机号码尚未注册！');
						$('#sendsms_btn').addClass('ui-btn-disabled').off('click');
					}else{
						$('#sendsms_btn').removeClass('ui-btn-disabled').on('click');
					}
				}
			});
	    	});


   /* $(document).ready(function(){
      	 $("mobile").blur(function(){
   	    	var mobel= $('#mobile').val();
   	    	$.ajax({
   				url:'/ali-legal/api/user/checkisRegister',
   				type:'post',
   				data:{"mobile":mobel},
   				dataType:"json",
   				success:function(data){
   					alert(data);
   				}
   			});
   	    	});
      	});*/
    /**
     * 跳转步骤
     * @param num 步骤
     */
    function showFindPwdBoxByNum(num) {
    	var mobel1=$("#mobile").val();  
    	var code1=$("#code").val();  
    	if(null==mobel1){
    		 C.msg.fail('请填写手机号！');
    		 return false;
    	}
    	if(null==mobel1){
    		C.msg.fail('请获取验证码！');
    		return false;
    	}
    	var isfalse=0;
    	$.ajax({
			url:'/ali-legal/api/user/ffindPassword',
			type:'post',
			data:{"mobel1":mobel1,"code1":code1},
			dataType:"json",
			async:false,
			success:function(data){
				if(data.mobileNumber=='phoneNo'){
					 C.msg.fail('手机号码不正确！');
					 //return false;
					isfalse=1;
				}
				if(data.mobileNumber=='msgNo'){
					 C.msg.fail('验证码不正确！');
					 //return false;
					 isfalse=1;
				}
				
				if(data.mobileNumber=='phoneCodeNo'){
					 C.msg.fail('验证码不正确！');
					 //return false;
					 isfalse=1;
				}
				
				if(data.mobileNumber=='MsgCodeNo'){
					 C.msg.fail('验证码不正确！');
					 //return false;
					 isfalse=1;
				}
				
				
				
				if(data.mobileNumber=='N'){
					 C.msg.fail('请获取验证码！');
					 //return false;
					 isfalse=1;
				}
			}
		});
    	
    	if(isfalse==1){
    		return false;
    	}
 
        var _index = num-1;
        var $findpwd_tabs = $('.findpwd-tabs li');
        $findpwd_tabs.eq(_index).addClass('active');
        $findpwd_tabs.each(function(index) {
            if(_index != index) {
                $(this).removeClass('active');
            }
        });

        var $findpwd_box_li = $('.findpwd-box li');
        $findpwd_box_li.eq(_index).removeClass('hide');
        $findpwd_box_li.each(function(index) {
            if(_index != index) {
                $(this).addClass('hide');
            }
        });
    }

   
   
    utils.bindSendSms($('#sendsms_btn'), $('#mobile'));
    
  

    //第一步
    var $findpwd1_form = $('#findpwd1_form');
    var findpwd1_form_valid = $findpwd1_form.validator(constants.validator);
    findpwd1_form_valid.on('valid.form', function(e, form){
        showFindPwdBoxByNum(2);
    });

    //第二步
    var $findpwd2_form = $('#findpwd2_form');
    var findpwd2_form_valid = $findpwd2_form.validator(constants.validator);
    findpwd2_form_valid.on('valid.form', function(e, form){
        rest.post({
            url: 'user/findPassword',
            data: {
                mobile: $.trim($findpwd1_form.find('input[name="mobile"]').val()),
                code : $.trim($findpwd1_form.find('input[name="code"]').val()),
                newPassword : $.trim($findpwd2_form.find('input[name="newPassword"]').val())
            },
            success: function(result) {
                showFindPwdBoxByNum(3);
            },
            fail : function (result) {
                C.msg.fail(result.msg);
            }
        });
    });

});
