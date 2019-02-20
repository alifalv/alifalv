//ajax_all("true", "false", "GET", "newjson.json", "json", "", myfunc); //调用函数

/*
 * AJAX针对所有的数据类型的函数
 * 
 * @param {type} sync 是否异步传输 默认是true是异步。 false就是同步传输 
 * @param {type} cache 是否开启缓存
 * @param {type} type ajax的传输类型 POST 或 GET
 * @param {type} url  是传输的目标url （注意：这个url要非常注意，如果路径不对就会导致错误  !--所以是重点--）
 * @param {type} datatype 传输数据的类型可以是 text:纯文本 | html:HTML信息包括script标签会在插入DOM时执行 | script:返回纯文本JavaScript代码 | json:json数据 | jsonp:jsonp数据格式 | xml:返回XML文档 
 * @param {type} data 是要传输的数据（注意:数据是什么格式datatype就要是对应的格式，否则传输失败）
 * @param {type} func 当ajax执行成功之后跳转到自己的函数  注意:这里只需要写上自己函数的名称即可
 * @returns {undefined}
 */

var storage = window.localStorage;//缓存

function ajax_all(sync, cache, type, url, datatype, data, func, complete) { //封装ajax的一些常用参数  //data数据可以为空
	$.ajax({
		sync: sync,
		cache: cache,
		type: type,
		url: url,
		dataType: datatype,
		data: data,
		beforSend: function() {
			plus.nativeUI.showWaiting( "等待中..." )
		},
		error: function(data) {
			//发送请求失败，请检查网络时被调用的函数 
			mui.toast( '发送请求失败，请检查网络')
		},
		success: function(data) {
			func(data);
		},
		complete: function (data) {
			if (complete) {
				complete(data);
			}
		}
	});
}

function ajax_json(url, json_data, success, error, complete) { //data数据可以为空
	$.ajax({
		type: 'POST',
		url: url,
		dataType: "json",
		// headers: {
		// 	Accept: "application/json; charset=utf-8",
		// 	userToken: "" + userToken
		// },
		data: json_data,
		beforSend: function () {
			plus.nativeUI.showWaiting("等待中...")
		},
		success: function (data) {
			success(data);
		},
		error: function (data) {
			//发送请求失败，请检查网络时被调用的函数 
			if (error && error != '') { error(data) }
			else { mui.toast('发送请求失败，请检查网络') }
		},
		complete: function (data) {
			if (complete && complete != '') { complete(data) }
		},
	});
}

function ajax_json_token(url, userToken, json_data, success, error, complete) { //data数据可以为空
	$.ajax({
		type: 'POST',
		url: url,
		dataType: "json",
		headers: {
			Accept: "application/json; charset=utf-8",
			"token": "" + userToken
		},
		data: json_data,
		success: function (data) {
			if (data.code == 1) {
				success(data);
			}else {
				// creatMywebview('Login.html', 'Login.html');
				// storage.clear();
			}
			
		},
		error: function (data) {
			//发送请求失败，请检查网络时被调用的函数 
			if (error && error != '') { error(data) }
			else { mui.toast('发送请求失败，请检查网络') }
		},
		complete: function (data) {
			if (complete && complete != '') { complete(data) } 
		},
	});
}