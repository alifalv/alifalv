var sel_sex = document.getElementById('sel_sex');

var Sexfirst = []; 

var SexselectedIndex = [0]; /* 默认选中的项 */

var Sexchecked = [0]; /* 已选项 */

function creatSexList(obj, list) {
	obj.forEach(function(item, index, arr) {
		var temp = new Object();
		temp.text = item.name;
		temp.value = index;
		list.push(temp);
	})
}

creatSexList(sexms, Sexfirst);

var Sexpicker = new Picker({
	data: [Sexfirst],
	SexselectedIndex: SexselectedIndex,
	title: ''
});

Sexpicker.on('picker.select', function(selectedVal, SexselectedIndex) {
	var text1 = Sexfirst[SexselectedIndex[0]].text;
	sel_sex.innerText = text1;
});

Sexpicker.on('picker.valuechange', function(selectedVal, SexselectedIndex) {
	console.log(selectedVal);
	console.log(SexselectedIndex);
});

sel_sex.addEventListener('click', function() {
	Sexpicker.show();
});