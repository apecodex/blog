const SystemMemoryOption = {
  title: {
    text: '系统内存情况',
    left: 'left'
  },
  tooltip: {
    formatter: '{b} <br/>已使用：{c}GB'
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
      color: '#30cfd0'
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
  SystemMemoryOption
}