<script setup lang='ts'>
import {Left, Right} from "@icon-park/vue-next"
// total ：用来传递数据总条数
// pageSize ：每页展示几条数据
// currentPage ：当前默认页码
// change-page ：页码改变时触发的事件，参数为当前页码

const props = defineProps({
  //数据总条数
  total: {
    type: Number,
    default: 88
  },
  //页面大小
  pageSize: {
    type: Number,
    default: 16
  },
  //当前显示的页码
  currentPage: {
    type: Number,
    default: 1
  }
});

let currentNum = ref(props.currentPage);

import {computed, ref} from 'vue'

// 页码显示组合
// 计算总页数
const pages = computed(() => Math.ceil(props.total / props.pageSize ));

const list = computed(() => {
  const result = []
  // 总页数小于等于5页的时候
  if (pages.value <= 5) {
    for (let i = 1; i <= pages.value; i++) {
      result.push(i)
    }
  } else {
    // 总页数大于5页的时候
    // 控制两端的省略号的有无，页码的显示个数与选中页码居中
    if (currentNum.value <= 2) {
      for (let i = 1; i <= 5; i++) {
        result.push(i)
      }
    } else if (currentNum.value >= 3 && currentNum.value <= pages.value - 2) {
      for (let i = currentNum.value - 2; i <= currentNum.value + 2; i++) {
        result.push(i)
      }
    } else if (currentNum.value > pages.value - 2) {
      for (let i = pages.value - 4; i <= pages.value; i++) {
        result.push(i)
      }
    }
  }
  return result;
})

const emit = defineEmits(["changePage"])
const changePage = (type: any) => {
  // 点击上一页按钮
  if (type === false) {
    if (currentNum.value <= 1)
      return
    currentNum.value -= 1
  } else if (type === true) {
    // 点击下一页按钮
    if (currentNum.value >= pages.value)
      return
    currentNum.value += 1
  } else {
    // 点击页码
    currentNum.value = type
  }
  emit('changePage',currentNum.value);
}
</script>

<template>
  <div class="pagination">
    <span class="arrow shadow flex justify-center items-center rounded-6px" :class="{ disabled: currentNum === 1 }" @click="changePage(false)"><Left size="24"/></span>
    <span v-if="currentNum > 3">...</span>
    <a
        v-for="item in list"
        :key="item"
        :class="{ active: currentNum === item }"
        @click="changePage(item)"
    >{{ item }}</a>
    <span v-if="currentNum < pages - 2">...</span>
    <span class="arrow shadow flex justify-center items-center rounded-6px" :class="{ disabled: currentNum === pages }" @click="changePage(true)"><Right size="24"/></span>
  </div>
</template>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  gap: 12px;
}
.pagination a {
  display: inline-block;
  padding: 3px 10px;
  box-shadow: var(--theme-shadow);
  border-radius: 6px;
  transition: var(--theme-transition-shadow);
}

.pagination a:hover {
  box-shadow: var(--theme-shadow-inset);
}
.pagination a.active {
  background: var(--theme-bg-reverse);
  color: var(--theme-bg);
  box-shadow: none;
  transition: var(--theme-transition);
}

.pagination .arrow {
  transition: var(--theme-transition-shadow);
}
.pagination .arrow:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}
.pagination .arrow.disabled {
  cursor: not-allowed;
  opacity: 0.4;

}
.pagination .arrow.disabled:hover {
  box-shadow: var(--theme-shadow-inset);
}

</style>