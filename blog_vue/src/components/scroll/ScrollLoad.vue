<script setup lang='ts'>
import type {Ref} from "vue"
import {onBeforeUnmount, onMounted, ref} from "vue";

const props = defineProps({
  totalCount: {
    type: Number,
    default: 0
  },
  currentCount: {
    type: Number,
    default: 0
  }
});

const emits = defineEmits([
    "update"
])

const scrollRef: Ref<HTMLElement | null> = ref(null);
let observer: IntersectionObserver | null = null;

onMounted(() => {
    observer = new IntersectionObserver(([entries]: Array<IntersectionObserverEntry>) => {
      if (entries && entries.isIntersecting) {
        // 数据为空或数据全部请求完成，取消观察
        if (props.totalCount === 0 || props.currentCount === props.totalCount) {
          if (observer) {
            observer.unobserve(scrollRef.value!);
            observer.disconnect();
          }
        } else emits("update");
      }
    });
    observer.observe(scrollRef.value!);
})

onBeforeUnmount(() => {
  if (observer) observer.disconnect();
})
</script>

<template>
  <div ref="scrollRef" class="flex flex-col justify-center my-10px">
    <hr class="hr-twill">
    <div class="flex justify-center mt-10px">
      <div v-if="currentCount !== totalCount">(๑･`◡´･๑) 拼命加载中....</div>
      <div v-else>⁽⁽ଘ(ˊᵕˋ)ଓ⁾⁾ 加载好了哦~</div>
    </div>
  </div>
</template>

<style scoped>

</style>