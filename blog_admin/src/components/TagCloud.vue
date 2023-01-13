<script setup lang='ts'>
import type { Ref } from 'vue';
import {onMounted, onUnmounted, reactive, ref} from "vue";

const props = defineProps({
  wordCloud: {
    type: Array,
    required: true
  },
  colorList: {
    type: Array,
    default: [
      '#a18cd1', '#fad0c4', '#ff8177',
      '#fecfef', '#fda085', '#f5576c',
      '#330867', '#30cfd0', '#38f9d7',
      '#22a6b3', '#eb4d4b', '#f0932b',
      '#ea8685', '#f19066', '#303952',
      '#34ace0', '#cc8e35', '#FD7272'
    ]
  }
})
// 容器节点ref
let wordCloudRef: Ref<HTMLElement | null> = ref(null)
// 容器节点的边界坐标
let ContainerSize = reactive({})
// 词云Dom节点
let wordDomList = ref([])
// 定义定时器，组件卸载时销毁
let timer: Ref<HTMLElement | null> = ref(null)

// 初始化词云DOM位置
function initWordPos() {
  // 计算每个词的真实位置和容器的位置
  wordDomList.forEach((value: any) => {
    value.local.realPos = {
      minx: value.offsetLeft,
      maxx: value.offsetLeft + value.offsetWidth,
      miny: value.offsetTop,
      maxy: value.offsetTop + value.offsetHeight
    };
  });
  ContainerSize = getContainerSize();
}
// 初始化词云DOM节点
function dealSpan() {
  const wordArr: any = [];
  props.wordCloud.forEach((value) => {
    // 根据词云数量生成span数量设置字体颜色和大小
    const spanDom: any = document.createElement('span');
    spanDom.style.position = 'relative';
    spanDom.style.display = 'inline-block';
    spanDom.style.color = randomColor();
    spanDom.style.fontSize = randomNumber(10, 20) + 'px';
    spanDom.innerHTML = value;
    spanDom.local = {
      position: {
        // 位置
        x: 0,
        y: 0
      },
      direction: {
        // 方向 正数往右 负数往左
        x: 1,
        y: 1
      },
      velocity: {
        // 每次位移初速度
        x: -0.5 + Math.random(),
        y: -0.5 + Math.random()
      },
    };
    (wordCloudRef.value as any).appendChild(spanDom);
    wordArr.push(spanDom);
  });
  wordDomList = wordArr;
}
// 动态词云
function wordFly() {
  wordDomList.forEach((value: any) => {
    // 设置运动方向 大于边界或者小于边界的时候换方向
    if (value.local.realPos.minx + value.local.position.x < ContainerSize.leftPos.x || value.local.realPos.maxx + value.local.position.x > ContainerSize.rightPos.x) value.local.direction.x = -value.local.direction.x;
    if (value.local.realPos.miny + value.local.position.y < ContainerSize.leftPos.y || value.local.realPos.maxy + value.local.position.y > ContainerSize.rightPos.y) value.local.direction.y = -value.local.direction.y;
    value.local.position.x += value.local.velocity.x * value.local.direction.x;
    value.local.position.y += value.local.velocity.y * value.local.direction.y;
    // 给每个词云加动画过渡
    value.style.transform = 'translateX(' + value.local.position.x + 'px) translateY(' + value.local.position.y + 'px)';
  });

  timer = window.requestAnimationFrame(wordFly)
}
// 获取容器位置
function getContainerSize() {
  // 判断容器大小控制词云位置
  const el: any = wordCloudRef.value;
  return {
    leftPos: {
      // 容器左侧的位置和顶部位置
      x: el.offsetLeft,
      y: el.offsetTop
    },
    rightPos: {
      // 容器右侧的位置和底部位置
      x: el.offsetLeft + el.offsetWidth,
      y: el.offsetTop + el.offsetHeight
    }
  };
}
// 获得一个包含最小值和最大值之间的随机数。
function randomNumber(lowerInteger: number, upperInteger: number) {
  const choices = upperInteger - lowerInteger + 1;
  return Math.floor(Math.random() * choices + lowerInteger);
}

// 获取随机颜色
function randomColor() {
  var colorIndex = Math.floor(props.colorList.length * Math.random());
  return props.colorList[colorIndex];
}

onMounted(() => {
  dealSpan()
  initWordPos()
  timer = window.requestAnimationFrame(wordFly)
})

onUnmounted(() => {
  // 组件销毁，关闭定时执行
  cancelAnimationFrame(timer);
})

</script>

<template>
  <div class="wordCloud" ref="wordCloudRef">
  </div>
</template>

<style scoped>
.wordCloud {
  width: 100%;
  height: 300px;
  overflow: hidden;
}
</style>