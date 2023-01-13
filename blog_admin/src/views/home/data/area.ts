import type { Ref } from 'vue';
import type { ECOption } from '~/echarts/echart';
import chinaJson from '~/assets/json/china.json'
import { registerMap } from 'echarts/core';
import {ref} from "vue"

registerMap('china', chinaJson as any)
// @ts-ignore
let chinaMapOption: Ref<ECOption> = ref<ECOption>({
  geo: {
    type: 'map',
    map: 'china',
    roam: true,  // 允许拖动放大
    label: {
      show: true  // 显示标签
    },
    top: 100,
    zoom: 2.0,
    emphasis: {
      itemStyle: {
        areaColor: "#F5DEB3",
        shadowOffsetX: 0,
        shadowOffsetY: 0,
        borderWidth: 1.5,
        borderColor: '#000'
      }
    }
  },
  title: {
    text: "用户来访地域",
  },
  tooltip: {
    formatter: (e: any) => {
      let value = e.value ? e.value : 0;
      return e.name + "<br />" + e.seriesName + "：" + value;
    },
    borderRadius: 4,
    backgroundColor: "rgba(50,50,50,0.5)",
    borderColor: "#181616",
    textStyle: {
      color: "#ffffff"
    },
  },
  toolbox: {
    feature: {
      saveAsImage: {},  // 导出图片
      dataView: {},  // 数据视图
    }
  },
  series: [
    {
      name: "用户人数",
      type: "map",
      geoIndex: 0,
      data: [] as any,
      areaColor: "#0FB8F0",
    },
  ],
  visualMap: {
    min: 0,
    max: 1000,
    left: 0,
    top: 30,
    pieces: [
      {
        gt: 100,
        label: "100人以上",
        color: "#ED5351"
      },
      {
        gte: 51,
        lte: 100,
        label: "51-100人",
        color: "#F6C021"
      },
      {
        gte: 21,
        lte: 50,
        label: "21-50人",
        color: "#59D9A5"
      },
      {
        label: "1-20人",
        gt: 0,
        lte: 20,
        color: "#6DCAEC"
      },
    ],
    show: true,
    backgroundColor: "rgba(0,0,0,0)"
  }
} as ECOption)

export {
  chinaMapOption
};