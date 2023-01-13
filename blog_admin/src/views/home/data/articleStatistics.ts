import {ref, Ref} from "vue";
import { ECOption } from "~/echarts/echart";

const articleStatisticsData: Array<any> = []

const articleStatisticsOption: Ref<ECOption> = ref<ECOption>({
  title: {
    left: 'center',
    text: '文章统计列表'
  },
  tooltip: {
    formatter: function (params: any) {
      return `${params.data[0].replace(/(\d{4})\-(\d{2})\-(\d{2})/, "$1年$2月$3日")} - ${params.data[1]}篇`;
    },
    borderRadius: 4,
    backgroundColor: "rgba(50,50,50,0.5)",
    borderColor: "#181616",
    textStyle: {
      color: "#ffffff"
    },
  },
  visualMap: {
    min: 0,
    max: 10000,
    type: 'piecewise',
    pieces: [
      {
        gt: 30,
        label: "30人以篇",
        color: "#ED5351"
      },
      {
        gte: 16,
        lte: 30,
        label: "16-30篇",
        color: "#F6C021"
      },
      {
        gte: 6,
        lte: 15,
        label: "6-15篇",
        color: "#59D9A5"
      },
      {
        label: "1-5篇",
        gt: 0,
        lte: 5,
        color: "#6DCAEC"
      },
    ],
    orient: 'horizontal',
    left: 'center',
    top: 45
  },
  calendar: {
    top: 100,
    cellSize: ['auto', 13],
    range: new Date().getFullYear(),
    yearLabel: {
      show: true,
      margin: '40'
    },
    splitLine: {
      show: true,
      lineStyle: {
        color: '#000',
        width: 4,
        type: 'solid'
      }
    },
    dayLabel: {
      margin: 20,
      firstDay: 1,
      nameMap: 'ZH'
    },
    monthLabel: {
      nameMap: 'ZH'
    },
    itemStyle: {
      color: 'transparent',
      borderWidth: 0.5,
      borderColor: '#323c48',
      borderType: 'dashed'
    }
  },
  series: {
    type: 'effectScatter',
    coordinateSystem: 'calendar',
    data: [] as any
  }
} as any)

export {
  articleStatisticsOption,
  articleStatisticsData
}