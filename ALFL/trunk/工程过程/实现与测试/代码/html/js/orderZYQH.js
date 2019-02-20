$(function() {
	"use strict";
	var bannerImgs = document.querySelector(".zyhd");
	var orderList = document.querySelectorAll(".yjlb ul li");
	var screenWidth = document.body.offsetWidth;
	var index = 0;
	Hidden(index);
	/*标题下边框切换*/
	function orderListqiehuan(e) {
		for(var i = 0; i < orderList.length; i++) {
			orderList[i].className = " ";
		}
		orderList[e].className = "xzbk";
	}
	/*添加列表点击切换效果*/
	var $liList = $(".yjlb ul li");
	$liList.click(function() {
		index = $liList.index(this);
		orderListqiehuan(index);
		Hidden(index);
	});
	//	添加订单隐藏效果
	function Hidden(index) {
		if(index == 0) {
			$('#quanbu').show()
				.siblings().hide();
		} else {
			if(index == 1) {
				$("#dfk").show().siblings().hide();
			} else {
				$("#dsh").show().siblings().hide();
			}
		}
	}
	/* 添加touch事件*/
	var startX = 0;
	var moveX = 0;
	var isMove = false;

//	bannerImgs.addEventListener("touchstart", function(event) {
//		isMove = false;
//		startX = event.touches[0].clientX;
//	});
//
//	bannerImgs.addEventListener("touchmove", function(event) {
//		isMove = true;
//		moveX = event.touches[0].clientX - startX;
//	});
//
//	bannerImgs.addEventListener("touchend", function() {
//		if(isMove && Math.abs(moveX) > screenWidth / 3) {
//			if(moveX < 0 && index !== 2) {
//				index++;
//				orderListqiehuan(index);
//			} else if(moveX > 0 && index !== 0) {
//				index--;
//				orderListqiehuan(index);
//			}
//			Hidden(index);
//		}
//	});
});