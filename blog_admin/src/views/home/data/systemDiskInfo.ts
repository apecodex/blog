const SystemDiskInfoOption = {
  title: {
    text: '系统硬盘情况',
    left: 'left'
  },
  tooltip: {
    formatter: '{b} 已使用：{c}GB'
  },
  series: {
    name: 'Pressure',
    type: 'gauge',
    min: 0,
    max: 0,
    radius: '90%',
    progress: {
      show: true
    },
    itemStyle: {
      color: '#FFAB91'
    },
    detail: {
      valueAnimation: true,
      formatter: '{value} GB',
    },
    title: {
      offsetCenter: [0, '85%']
    },
    data: [
      {
        value: 0,
        name: ''
      }
    ]
  }
};

export {
  SystemDiskInfoOption
}