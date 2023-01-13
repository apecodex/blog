import local from "~/assets/imgs/local.png";

const generateMap = (domId: string, lng: string = "116.404", lat: string = '39.915') => {
    let BMap = (window as any).BMap; // 注意要带window，不然会报错（注意官方api,会有改动，之前是Bmap,后面3.0版本改为了BMap,最好查文档或者打印一下window）
    let map = new BMap.Map(domId); // domId必须和dom上的id一直
    map.clearOverlays()
    map.enableScrollWheelZoom(true)
    map.enableContinuousZoom(true)
    map.addControl(new BMap.NavigationControl());
    map.addControl(new BMap.ScaleControl());
    map.addControl(new BMap.OverviewMapControl());
    let point = new BMap.Point(lng, lat);
    map.centerAndZoom(point, 12)
    let myIcon = new BMap.Icon(local, new BMap.Size(40, 40));
    let marker = new BMap.Marker(point, {icon: myIcon})
    map.addOverlay(marker)
};

export {
    generateMap
}