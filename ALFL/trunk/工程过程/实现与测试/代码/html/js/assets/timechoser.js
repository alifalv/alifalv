var nameEl = document.getElementById('sel_timer');

var first = [];
var second = [];
var third = [];

var selectedIndex = [myidx1, myidx2, myidx3]; /* 默认选中的 */

var checked = [0, 0, 0]; /* 已选选项 */

function creatList(obj, list) {
	obj.forEach(function(item, index, arr) {
		var temp = new Object();
		temp.text = item.name;
		temp.value = index;
		list.push(temp);
	})
}

creatList(years, first);
creatList(monthes, second);
creatList(days, third);

var picker = new Picker({
	data: [first, second, third],
	selectedIndex: selectedIndex,
	title: ''
	//title: '地址选择'
});

picker.on('picker.select', function(selectedVal, selectedIndex) {
	var text1 = first[selectedIndex[0]].text;
	var text2 = second[selectedIndex[1]].text;
	var text3 = third[selectedIndex[2]] ? third[selectedIndex[2]].text : '';

	nameEl.innerText = text1 + ' ' + text2 + ' ' + text3;
});

picker.on('picker.valuechange', function(selectedVal, selectedIndex) {
	console.log(selectedVal);
	console.log(selectedIndex);
});

nameEl.addEventListener('click', function() {
	picker.show();
});