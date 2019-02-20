/*!
 * 诉讼费用计算
 * @author Li Hongwei
 * @date 2015-11-05
 * @version 1.0
 */
function isNumber(n) {
	return !isNaN(parseFloat(n)) && isFinite(n);
}

function ccjs(theBD) {
	var result = "";
	if(theBD <= 1.0) {
		result = "50";
	}
	if(theBD > 1.0 && theBD <= 10.0) {
		result = (50.0 + (theBD - 1.0) * 10000.0 * 0.025).toString();
	}
	if(theBD > 10.0 && theBD <= 20.0) {
		result = (2300.0 + (theBD - 10.0) * 10000.0 * 0.02).toString();
	}
	if(theBD > 20.0 && theBD <= 50.0) {
		result = (4300.0 + (theBD - 20.0) * 10000.0 * 0.015).toString();
	}
	if(theBD > 50.0 && theBD <= 100.0) {
		result = (8800.0 + (theBD - 50.0) * 10000.0 * 0.01).toString();
	}
	if(theBD > 100.0 && theBD <= 200.0) {
		result = (13800.0 + (theBD - 100.0) * 10000.0 * 0.009).toString();
	}
	if(theBD > 200.0 && theBD <= 500.0) {
		result = (22800.0 + (theBD - 200.0) * 10000.0 * 0.008).toString();
	}
	if(theBD > 500.0 && theBD <= 1000.0) {
		result = (46800.0 + (theBD - 500.0) * 10000.0 * 0.007).toString();
	}
	if(theBD > 1000.0 && theBD <= 2000.0) {
		result = (81800.0 + (theBD - 1000.0) * 10000.0 * 0.006).toString();
	}
	if(theBD > 2000.0) {
		result = (141800.0 + (theBD - 2000.0) * 10000.0 * 0.005).toString();
	}
	return result;
}

function litigationCost(costAmount, type) {
	var text;
	var result;

	var text2 = "";
	var num = 0.0;

	if(costAmount != "") {
		if(!isNumber(costAmount)) {
			return "请输入正确的诉讼标的！";
		}
		num = parseFloat(costAmount);
		num /= 10000.0;
	}
	switch(type) {
		case "1":
			text = ccjs(num);
			result = "受理费：" + text.toString() + "元";
			break;
		case "2":
			if(num <= 20.0) {
				text = "50—300";
			} else {
				text = (50.0 + (num - 20.0) * 10000.0 * 0.005).toString() + "—" + (300.0 + (num - 20.0) * 10000.0 * 0.005).toString();
			}
			result = "受理费：" + text.toString() + "元";
			break;
		case "3":
			if(num <= 5.0) {
				text = "100—500";
			} else {
				if(num > 5.0 && num <= 10.0) {
					text = (100.0 + (num - 5.0) * 10000.0 * 0.01).toString() + "—" + (500.0 + (num - 5.0) * 10000.0 * 0.01).toString();
				} else {
					text = (600.0 + (num - 10.0) * 10000.0 * 0.005).toString() + "—" + (1000.0 + (num - 10.0) * 10000.0 * 0.005).toString();
				}
			}
			result = "受理费：" + text.toString() + "元";
			break;
		case "4":
			text = "50—100";
			result = "受理费：" + text.toString() + "元";
			break;
		case "5":
			if(num == 0.0) {
				text = "500—1000";
			} else {
				if(num <= 1.0) {
					text = "50";
				}
				if(num > 1.0 && num <= 10.0) {
					text = (50.0 + (num - 1.0) * 10000.0 * 0.025).toString();
				}
				if(num > 10.0 && num <= 20.0) {
					text = (2300.0 + (num - 10.0) * 10000.0 * 0.02).toString();
				}
				if(num > 20.0 && num <= 50.0) {
					text = (4300.0 + (num - 20.0) * 10000.0 * 0.015).toString();
				}
				if(num > 50.0 && num <= 100.0) {
					text = (8800.0 + (num - 50.0) * 10000.0 * 0.01).toString();
				}
				if(num > 100.0 && num <= 200.0) {
					text = (13800.0 + (num - 100.0) * 10000.0 * 0.009).toString();
				}
				if(num > 200.0 && num <= 500.0) {
					text = (22800.0 + (num - 200.0) * 10000.0 * 0.008).toString();
				}
				if(num > 500.0 && num <= 1000.0) {
					text = (46800.0 + (num - 500.0) * 10000.0 * 0.007).toString();
				}
				if(num > 1000.0 && num <= 2000.0) {
					text = (81800.0 + (num - 1000.0) * 10000.0 * 0.006).toString();
				}
				if(num > 2000.0) {
					text = (141800.0 + (num - 2000.0) * 10000.0 * 0.005).toString();
				}
			}
			result = "受理费：" + text.toString() + "元";
			break;
		case "6":
			text = "10";
			result = "受理费：" + text.toString() + "元";
			break;
		case "7":
			text = "100";
			result = "受理费：" + text.toString() + "元";
			break;
		case "8":
			text = "50";
			result = "受理费：" + text.toString() + "元";
			break;
		case "9":
			if(num == 0.0) {
				text2 = "50—100";
			} else {
				if(num < 1.0) {
					text2 = "50";
				}
				if(num > 1.0 && num <= 50.0) {
					text2 = (50.0 + (num - 1.0) * 10000.0 * 0.015).toString();
				}
				if(num > 50.0 && num <= 500.0) {
					text2 = (7400.0 + (num - 50.0) * 10000.0 * 0.01).toString();
				}
				if(num > 500.0 && num <= 1000.0) {
					text2 = (52400.0 + (num - 500.0) * 10000.0 * 0.005).toString();
				}
				if(num > 1000.0) {
					text2 = (77400.0 + (num - 1000.0) * 10000.0 * 0.001).toString();
				}
			}
			result = "申请费：" + text2.toString() + "元";
			break;
		case "10":
			if(num <= 0.1) {
				text2 = "30";
			} else {
				if(num > 0.1 && num <= 10.0) {
					text2 = (30.0 + (num - 0.1) * 10000.0 * 0.01).toString();
				}
				if(num > 10.0) {
					text2 = (1020.0 + (num - 10.0) * 10000.0 * 0.005).toString();
				}
			}
			if(text2 != "" && parseFloat(text2) > 5000.0) {
				text2 = "5000";
			}
			result = "申请费：" + text2.toString() + "元";
			break;
		case "11":
			text2 = ccjs(num);
			if(text2 != "") {
				text2 = (parseFloat(text2) / 3.0).toString();
			}
			result = "申请费：" + text2.toString() + "元";
			break;
		case "12":
			result = "申请费：100元";
			break;
		case "13":
			result = "申请费：400元";
			break;
		case "14":
			text2 = ccjs(num);
			if(text2 != "") {
				text2 = (parseFloat(text2) / 2.0).toString();
				if(parseFloat(text2) > 300000.0) {
					text2 = "300000";
				}
			}
			result = "申请费：" + text2.toString() + "元";
			break;
		case "15":
			result = "申请费：1000—10000元";
			break;
	}
	return result;
}