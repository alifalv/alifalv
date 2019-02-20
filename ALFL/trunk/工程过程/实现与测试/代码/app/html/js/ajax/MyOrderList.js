var wxid = localStorage.getItem("m_openid");

var order_all = $('#quanbu');
var order_topay = $('#dfk');
var order_totake = $('#dsh');

getOrderList('', order_all);

$('.yjlb li').click(function() {
	var _thisidx = $(this).index();
	switch(_thisidx) {
		case 0:
			break;
		case 1:
			getOrderList(20, order_topay);
			break;
		case 2:
			getOrderList(50, order_totake);
			break;
		default:
			break;
	}
});

function getOrderList(_status, _conter) {
	mui.ajax(commonUrl + 'apigd/orderlist', {
		data: {
			m_openid: wxid,
			status: _status,
			isgoods: 1,
			page: 1,
			pagesize: 100
		},
		dataType: 'json',
		type: 'post',
		timeout: 6000,
		success: function(data) {
			if(data.code == 1) {
				_conter.empty();
				var _data = data.data.listdata;
				if(_data.length > 0) {
					for(var i = 0; i < _data.length; i++) { //向容器里添加订单信息
						_conter.append('<div class="dfk"><h1 class="ddbt">订单号：<span class="ddh">' + _data[i].orderid + '</span><span class="ddzt">' + _data[i].status_name + '</span></h1><div class="splb page-slideright"></div><div class="order-list"><p>运费<span>¥10.00</span></p></div><div class="order-list orderyouhq"><p>现金抵扣券<span>-¥ '+ _data[i].order_price_quan +'</span></p></div><div class="ddhj"><div class="ddhj-l">共<span>' + _data[i].goodslist.length + '</span>种商品<span style="padding:0 0.2rem;"></span>合计：<span class="hjje">¥' + _data[i].order_price_final + '</span></div><div class="ddhj-r"></div></div></div>');
						if(parseFloat(_data[i].order_price_quan)<=0){
							_conter.find('.dfk').eq(i).find('.orderyouhq').hide();
						}
						_conter.find('.dfk').eq(i).find('.splb').empty();
						var _imgsrc;
						for(var j = 0; j < _data[i].goodslist.length; j++) { //向订单里添加此订单下的商品信息
							var _rules = _data[i].goodslist[j].order_attr.replace(',', ' ');
							if(_rules == 0) {
								_rules = '';
							}
							_imgsrc = _data[i].goodslist[j].goods_detail.data_json.pic_wh_60x60 || _data[i].goodslist[j].goods_detail.data_json.pic_wh_200x200 || _data[i].goodslist[j].goods_detail.data_json.pic || _data[i].goodslist[j].goods_detail.data_json.pics;
							_conter.find('.dfk').eq(i).find('.splb').append('<a href="GoodsIntr.html?id=' + _data[i].goodslist[j].goods_detail.id + '&catid=' + _data[i].goodslist[j].goods_detail.catid + '"><div class="spxx"><img src="' + _imgsrc + '"><div class="spsm"><h2>' + _data[i].goodslist[j].goods_detail.title + '</h2><span class="rules">' + _rules + '</span><p>¥' + _data[i].goodslist[j].goods_detail.price_sell + '</p></div><p>×<span>' + parseInt(_data[i].goodslist[j].order_number) + '</span></p></div></a>');
						};

						var thisStatus = _data[i].status;
						switch(thisStatus) {
							case '20': //订单状态："待付款"
								var __btnhtml = '<a href="javascript:void(0);" class="cancelorder"><p>取消订单</p></a><a href="javascript:void(0);" class="gotoPay"><p>去付款</p></a>';
								setbtnforChangeStatus(i, 'qx', __btnhtml);
								_conter.find('.dfk').eq(i).find('.gotoPay').click(function() {
									var thisorderid = $(this).closest('.dfk').find('.ddh').html();
									creatMywebview('OrderPay.html?orderid=' + thisorderid, 'OrderPay.html');
								});
								break;
							case '50': //订单状态："待收货"
								var __btnhtml = '<a href="javascript:void(0);" class="cancelorder"><p>确认收货</p></a>';
								setbtnforChangeStatus(i, 'msh', __btnhtml);
								break;
							case '70': //订单状态："已取消"
								var __btnhtml = '<a href="javascript:void(0);" class="cancelorder"><p>删除</p></a>';
								setbtnforChangeStatus(i, 'del', __btnhtml);
								break;
							default:
								break;
						}
					}

					function setbtnforChangeStatus(this_I, _optype, _btnhtml) {
						_conter.find('.dfk').eq(this_I).find('.ddhj-r').append(_btnhtml);
						_conter.find('.dfk').eq(this_I).find('.cancelorder').click(function() {
							var _this = $(this);
							var this_orderid = _this.closest('.dfk').find('.ddh').html();
							var this_optype = _optype;

							var _opdialog;
							switch(this_optype) {
								case 'qx': //将要进行的操作："取消订单"
									_opdialog = '取消订单';
									break;
								case 'del': //将要进行的操作："删除订单"
									_opdialog = '删除订单'
									break;
								case 'msh': //将要进行的操作："收货"
									_opdialog = '收货'
									break;
								default:
									break;
							}
							var btnArray = ['确定', '取消'];
							mui.confirm('确定要' + _opdialog+'?', '订单', btnArray, function(e) {
								if(e.index == 1) {} else {
									if(this_optype=='del'){
										_this.closest('.dfk').addClass('hide');
										setTimeout(function(){
											_this.closest('.dfk').remove();
											changeOrderStatus(this_orderid, this_optype);
										},300)
									} else {
										changeOrderStatus(this_orderid, this_optype);
									}
								}
							});
						});
					};
				} else {
					_conter.append('<div class="nomore">无此类订单</div>');
				}
			} else if(data.code == -1) {
				mui.plusReady(function() {
					plus.nativeUI.toast('没有订单');
				})
			}
			$.getScript("js/mui.slideright.js");
		},
		error: function(xhr, type, errorThrown) {
			//异常处理；
			mui.toast(RequestErrorMsg(type));
		}
	});
}

function changeOrderStatus(_orderid, _optiontype) {
	mui.ajax(commonUrl + 'apigd/orderstatus', {
		data: {
			m_openid: wxid,
			orderid: _orderid,
			data_optype: _optiontype
		},
		dataType: 'json',
		type: 'post',
		timeout: 6000,
		success: function(data) {
			if(data.code == 1) {
				mui.plusReady(function() {
					plus.nativeUI.toast('操作成功');
					getOrderList('', order_all);
					getOrderList(20, order_topay);
					getOrderList(50, order_totake);
				})
			} else if(data.code == -1) {
				mui.plusReady(function() {
					plus.nativeUI.toast('此订单已取消');
					getOrderList('', order_all);
					getOrderList(20, order_topay);
					getOrderList(50, order_totake);
				})
			}
		},
		error: function(xhr, type, errorThrown) {
			//异常处理；
			mui.toast(RequestErrorMsg(type));
		}
	});
}