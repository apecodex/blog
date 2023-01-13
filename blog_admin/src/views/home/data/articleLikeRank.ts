import type { Ref } from 'vue';
import type { ECOption } from '~/echarts/echart';
import {ref} from "vue";

const articleLikeRankData: Array<any> = []

const articleLikeRankOption: Ref<ECOption> = ref<ECOption>({
  visualMap: {
    min: 0,
    max: 10000,
    show: false,
    type: 'piecewise',
    pieces: [
      {
        gt: 1000,
        color: "#ED5351"
      },
      {
        gte: 501,
        lte: 1000,
        color: "#F6C021"
      },
      {
        gte: 101,
        lte: 500,
        color: "#59D9A5"
      },
      {
        gt: 0,
        lte: 100,
        color: "#6DCAEC"
      },
    ],
    orient: 'horizontal',
    left: 'center',
    top: 45
  },
  title: {
    text: '文章点赞排行',
  },
  tooltip: {
    trigger: 'axis',
    formatter: "标题：《{b}》<br/>点赞量: {c}",
    borderRadius: 4,
    backgroundColor: "rgba(50,50,50,0.5)",
    borderColor: "#181616",
    textStyle: {
      color: "#ffffff"
    },
  },
  toolbox: {
    show: true,
    orient: 'horizontal',
    left: 'right',
    top: '0',
    feature: {
      magicType: { show: true, type: ['line', 'bar'] },
      dataZoom: {
        show: true
      },
      saveAsImage: { show: true },
    }
  },
  xAxis: [
    {
      type: 'category',
      show: false,
    }
  ],
  yAxis: [
    {
      type: 'value',
      minInterval: 1,
      axisLabel: {
        formatter: '{value} 次'
      }
    }
  ],
  series: {
    name: 'articleLikeRank',
    type: 'bar',
    label: {
      show: true,
      position: 'inside',
      distance: 15,
      verticalAlign: 'middle',
      rotate: 90,
      formatter: '《{b}》',
      fontSize: 12,
      color: '#000',
    },
    emphasis: {
      focus: 'series'
    },
    data: [] as any
  }
});

export {
  articleLikeRankOption,
  articleLikeRankData
}