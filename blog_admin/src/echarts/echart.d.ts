//echart.d.ts
import { ComposeOption } from 'echarts/core';
import {
  BarSeriesOption,
  LineSeriesOption
} from 'echarts/charts';
import {
  // 组件类型的定义后缀都为 ComponentOption
  TitleComponentOption,
  TooltipComponentOption,
  GridComponentOption,
  // 数据集组件
  DatasetComponentOption,
} from 'echarts/components';

type ECOption = ComposeOption<
  | BarSeriesOption
  | LineSeriesOption
  | TitleComponentOption
  | TooltipComponentOption
  | GridComponentOption
  | DatasetComponentOption
>;

export { ECOption }