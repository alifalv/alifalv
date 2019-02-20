/*!
 * 联系方式
 * @author Li Hongwei
 * @date 2018-07-29 14:12
 * @version 1.0
 */
define(function(require,exports,module) {
    "use strict";

    require(['amap']);

    var map;

    //添加marker标记
    function addMarker() {
        map.clearMap();
        var marker = new AMap.Marker({
            map: map,
            position: [113.029442,28.183498]
        });
        //鼠标点击marker弹出自定义的信息窗体
    }

    function addInfoWindow() {
        //构建信息窗体中显示的内容
        var info = [];
        info.push("<div style=\"padding:0 0 0 4px;\"><b>长房中庭国际</b>");
        var infoWindow = new AMap.InfoWindow({
            content: info.join("<br/>"),
            offset: new AMap.Pixel(0, -20)
        });
        infoWindow.open(map, map.getCenter());
    }

    window.onAMapLoaded = function() {
        map = new AMap.Map('map', {
            resizeEnable: true,
            center: [113.029442,28.183498],
            zoom: 14
        });

        addMarker();
        addInfoWindow()

    };

});