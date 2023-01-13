import { App } from "vue";
import ECharts from 'vue-echarts'
import { use } from "echarts/core";
// 手动引入 ECharts 各模块来减小打包体积
import {
  CanvasRenderer,
} from 'echarts/renderers'
import {
  BarChart,
  MapChart,
  LineChart,
  HeatmapChart,
  EffectScatterChart,
  PieChart,
  GaugeChart
} from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  GeoComponent,
  VisualMapComponent,
  TitleComponent,
  ToolboxComponent,
  GraphicComponent,
  CalendarComponent,
  LegendComponent,
} from 'echarts/components'

use([
  CanvasRenderer,
  BarChart,
  MapChart,
  LineChart,
  HeatmapChart,
  EffectScatterChart,
  PieChart,
  GaugeChart,
  GridComponent,
  TooltipComponent,
  GeoComponent,
  VisualMapComponent,
  TitleComponent,
  ToolboxComponent,
  GraphicComponent,
  CalendarComponent,
  LegendComponent
]);

export function setupECharts(app: App<Element>) {
  app.component('v-chart', ECharts)
}