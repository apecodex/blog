import type { Ref } from 'vue';
import type { ECOption } from '~/echarts/echart';
import {ref} from "vue";

const categoriesData: Array<any> = [];

const categoryOption: Ref<ECOption> = ref<ECOption>({
  title: {
    text: '分类',
    left: 'left'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{b}<br/>{c}篇文章 ({d}%)',
    borderRadius: 4,
    backgroundColor: "rgba(50,50,50,0.5)",
    borderColor: "#181616",
    textStyle: {
      color: "#ffffff"
    },
  },
  toolbox: {
    show: true,
    feature: {
      saveAsImage: { show: true }
    }
  },
  series: {
    name: 'category',
    type: 'pie',
    radius: [20, 140],
    roseType: 'area',
    itemStyle: {
      borderRadius: 5
    },
    data: [] as any
  }
} as any);

export {
  categoryOption,
  categoriesData
}