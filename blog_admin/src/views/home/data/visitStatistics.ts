import type { Ref } from 'vue';
import type { ECOption } from '~/echarts/echart';
import {ref} from "vue";

const dataList: Array<string> = []
const valueList: Array<number> = []

const visitStatisticsOption: Ref<ECOption> = ref<ECOption>({
  visualMap: [
    {
      show: false,
      type: 'continuous',
      seriesIndex: 0,
      min: 0,
      max: 400
    },
    {
      show: false,
      type: 'continuous',
      seriesIndex: 1,
      dimension: 0,
      min: 0,
      max: dataList.length - 1
    }
  ],
  title: {
    text: '用户周访问统计',
  },
  tooltip: {
    trigger: 'axis',
  },
  toolbox: {
    show: true,
    feature: {
      dataView: { readOnly: true },
      magicType: { type: ['line', 'bar'] },
      saveAsImage: {}
    },
  },
  xAxis: {
    type: 'category',
    data: [] as any,
    axisLabel: {
      interval: 0,
      rotate: 60,
    },
  },
  yAxis: {
    type: 'value',
    min: 0,
    minInterval: 1,
    axisLabel: {
      formatter: '{value} 人'
    }
  },
  series: {
      name: '访问人数',
      type: 'line',
      label: {
        show: true,
      },
      data: [] as any
    }
})

export {
  visitStatisticsOption,
  dataList,
  valueList
}