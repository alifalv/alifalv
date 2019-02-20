/*!
 * fileUpload
 * @author Li Hongwei
 * @date 2018-09-21 15:36
 * @version 1.0
 */
//define(function(require,exports,module) {
//    "use strict";
//
//    var utils = require('utils');
    ;(function ($) {


        $.fn.extend({
        		takungaeFileup: function (opt, serverCallBack) {
                var imageNum = 5;
                if ($("#maxImageNum").val() != null) {
                    imageNum = $("#maxImageNum").val();
                }
                var delParent;

                if (typeof opt != "object") {
                    alert('参数错误!');
                    return;
                }
                imageNum = opt.imageNum;
                var $fileInput = $(this);
                var $fileInputId = $fileInput.attr('id');

                var defaults = {
                    fileType: ["txt", "doc", "docx", "pdf", "jpg", "png"], // 上传文件的类型
                    fileSize: 1024 * 1024 * 10, // 上传文件的大小 10M
                    count: 0
                    // 计数器
                };

                if(opt.imgUrl) {
                    $(this).parent().hide();
                    var imgArr = [];
                    var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                    imgArr.push(opt.imgUrl);
                    var $section = $("<section class='up-section fl loading'>");
                    imgContainer.children(".z_file").before($section);
                    // var $span = $("<span class='up-span'>");
                    // $span.appendTo($section);
                    var $closeimg_btn = $("<i class='close-upimg'></i>").on("click", function (event) {
                        var numUp = $("<i class='close-upimg'></i>").length;
                        if (numUp == 1) {
                            $(this).parent().siblings(".z_file").show();
                        }
                        $(".works-mask").show();
                        $(this).parent().remove();
                        return false;
                    });
                    $closeimg_btn.appendTo($section);
                    var $img = $("<a class='up-img up-opcity ui-height-100p ui-block file-look' target='_blank' title='单击查看'>" + opt.formData.name + "</a>");
                    $img.attr("href", utils.createImgUrl(imgArr[0]));
                    $img.appendTo($section);
                    var htmlStr = '<input type="hidden" name="' + opt.formData.name + '" value="' + imgArr[0] + '">';
                    $section.append(htmlStr);
                    $section.removeClass("loading");
                    $img.removeClass("up-opcity");
                }

                // 组装参数;

                if (opt.success) {
                    var successCallBack = opt.success;
                    delete opt.success;
                }

                if (opt.fail) {
                    var failCallBack = opt.fail;
                    delete opt.fail;
                }

                if (opt.error) {
                    var errorCallBack = opt.error;
                    delete opt.error;
                }

                /* 点击图片的文本框 */
                $(this).change(function () {
                    var reader = new FileReader(); // 新建一个FileReader();
                    var idFile = $(this).attr("id");
                    var file = document.getElementById(idFile);
                    var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                    var fileList = file.files; // 获取的图片文件
                    var input = $(this).parent();// 文本框的父亲元素
                    var imgArr = [];
                    // 遍历得到的图片文件
                    var numUp = imgContainer.find(".up-section").length;
                    var totalNum = numUp + fileList.length; // 总的数量
                    if (fileList.length > imageNum || totalNum > imageNum) {
                        alert('上传文件数目不可以超过' + imageNum +'个，请重新选择'); // 一次选择上传超过5个
                        // 或者是已经上传和这次上传的到的总数也不可以超过5个
                    } else if (numUp < imageNum) {
                        fileList = validateUp(fileList, defaults);
                        for (var i = 0; i < fileList.length; i++) {
                            var imgUrl = window.URL.createObjectURL(fileList[i]);
                            imgArr.push(imgUrl);
                            var $section = $("<section class='up-section fl loading'>");
                            imgContainer.children(".z_file").before($section);
                            // var $span = $("<span class='up-span'>");
                            // $span.appendTo($section);
                            var $closeimg_btn = $("<i class='close-upimg'></i>").on("click", function (event) {
                                var numUp = $("<i class='close-upimg'></i>").length;
                                if (numUp == 1) {
                                    $(this).parent().siblings(".z_file").show();
                                }
                                $(".works-mask").show();
                                $(this).parent().remove();
                                return false;
                            });
                            $closeimg_btn.appendTo($section);
                            var $img = $("<a class='up-img up-opcity ui-height-100p ui-block file-look' target='_blank' title='单击查看'>");
                            $img.attr("href", imgArr[i]);
                            $img.appendTo($section);
                            // var $p = $("<p class='img-name-p'>");
                            // $p.html(fileList[i].name).appendTo($section);
                            // var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
                            // $input.appendTo($section);
                            // var $input2 = $("<input id='tags' name='tags' value='' type='hidden'/>");
                            // $input2.appendTo($section);
                            uploadImg(opt, fileList[i], $section);
                        }
                        ;
                    }

                    numUp = imgContainer.find(".up-section").length;
                    if (numUp >= imageNum) {
                        $(this).parent().hide();
                    }
                    ;
                    //input内容清空
                    $(this).val("");
                });


                $(".z_photo").on(".close-upimg", "click", function (event) {
                    $(".works-mask").show();
                    $(this).parent().remove();
                    return false;
                });

                $(".wsdel-ok").click(function (event) {
                    event.preventDefault();
                    event.stopPropagation();
                    $(".works-mask").hide();
                    var numUp = delParent.siblings(".up-section").length;
                    if (numUp < imageNum + 1) {
                        delParent.parent().find(".z_file").show();
                    }
                    delParent.remove();
                });

                $(".wsdel-no").click(function () {
                    $(".works-mask").hide();
                });

                // 验证文件的合法性
                function validateUp(files, defaults) {
                    var arrFiles = [];// 替换的文件数组
                    for (var i = 0, file; file = files[i]; i++) {
                        // 获取文件上传的后缀名
                        var newStr = file.name.split("").reverse().join("");
                        if (newStr.split(".")[0] != null) {
                            var type = newStr.split(".")[0].split("")
                                .reverse().join("");
                            console.log(type + "===type===");
                            if (jQuery.inArray(type, defaults.fileType) > -1) {
                                // 类型符合，可以上传
                                if (file.size >= defaults.fileSize) {
                                    alert('文件大小"' + file.name + '"超出10M限制！');
                                } else {
                                    arrFiles.push(file);
                                }
                            } else {
                                alert('您上传的"' + file.name + '"不符合上传类型');
                            }
                        } else {
                            alert('您上传的"' + file.name + '"无法识别类型');
                        }
                    }
                    return arrFiles;
                }
                ;

                function uploadImg(opt, file, obj) {
                    $("#imguploadFinish").val(false);
                    // 验证通过图片异步上传
                    var url = opt.url;
                    var data = new FormData();
                    // data.append("path", opt.formData.path);
                    data.append("file", file);

                    ImgToBase64(file,function(base64img) {

                        var ajaxOptions = {
                        		type:"POST",
                            url: requestUrl_pc+'api/file/uploadFile',
                            data: {
                                fileBase64Str: base64img
                            },
                            success: function (result) {
                                $(".up-section").removeClass("loading");
                                $(".up-img").removeClass("up-opcity");
                                $("#imguploadFinish").val(true);
                                var htmlStr = '<input type="hidden" name="' + opt.formData.name + '" value="' + result.data + '">';
                                obj.append(htmlStr);
                                if (successCallBack) {
                                    successCallBack(result);
                                }
                            },
                            fail: function (result) {
                                var numUp = obj.siblings(".up-section").length;
                                if (numUp < imageNum + 1) {
                                    obj.parent().find(".z_file").show();
                                }
                                obj.remove();
                                $("#imguploadFinish").val(false);
                                if (failCallBack) {
                                    failCallBack(result);
                                }
                            },
                            error: function () {
                                var numUp = obj.siblings(".up-section").length;
                                if (numUp < imageNum + 1) {
                                    obj.parent().find(".z_file").show();
                                }
                                obj.remove();
                                var err = "上传失败，请联系管理员！";
                                $("#imguploadFinish").val(false);
                                if (errorCallBack) {
                                    errorCallBack(err);
                                }
                            }
                        };
                        $.ajax(ajaxOptions);
                    });

                }

            }
        });

        /**
         * 将文件转成base64
         * @param obj
         * @param callback
         * @returns {boolean}
         */
        function ImgToBase64(file, callBack) {
            var reader = new FileReader();//读取客户端上的文件
            reader.readAsDataURL(file);
            reader.onload = function (e) {
                console.log(e.target.result);
                callBack(e.target.result);
            }

        }

    })(jQuery);



//});