<script setup lang='ts'>
import Mask from "@/components/Mask.vue";
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";

const settingStore = useSettingStore()
const {loadingFlag} = storeToRefs(settingStore)

</script>

<template>
  <Mask :show="loadingFlag"/>
  <div :class="{'show': loadingFlag }" class="loading fixed left-5/10 opacity-0 -top-9/6 z-10 flex flex-col items-center gap-12px" @touchmove.prevent @mousewheel.prevent>
    <div class="w-200px h-60px relative z-1">
      <div class="circle"></div>
      <div class="circle"></div>
      <div class="circle"></div>
      <div class="shadow"></div>
      <div class="shadow"></div>
      <div class="shadow"></div>
    </div>
    <span class="text-18px text-$text-color" style="transition: var(--theme-transition-color)">拼命加载中...</span>
  </div>
</template>

<style scoped>
.loading {
  pointer-events: none;
  transition: opacity 300ms ease-in-out,
  top 1000ms ease-in-out,
  transform 0.2s 0.2s ease-in-out;
}

.loading.show {
  top: 50%;
  opacity: 1;
  pointer-events: auto;
  transform: translate(-50%, -50%) scale(1);
  transition: transform 300ms cubic-bezier(0.18, 0.89, 0.43, 1.19);
}

.circle {
  @apply 'w-20px h-20px absolute rounded-5/10 bg-$theme-bg-reverse left-3/20 origin-center';
  animation: circle7124 .5s alternate infinite ease;
  transition: var(--theme-transition-bg);
}

@keyframes circle7124 {
  0% {
    top: 60px;
    height: 5px;
    border-radius: 50px 50px 25px 25px;
    transform: scaleX(1.7);
  }

  40% {
    height: 20px;
    border-radius: 50%;
    transform: scaleX(1);
  }

  100% {
    top: 0;
  }
}

.circle:nth-child(2) {
  left: 45%;
  animation-delay: .2s;
}

.circle:nth-child(3) {
  left: auto;
  right: 15%;
  animation-delay: .3s;
}

.shadow {
  @apply 'w-20px h-4px rounded-5/10 absolute top-62px origin-center -z-1 left-3/20';
  background-color: rgba(0,0,0,0.9);
  filter: blur(1px);
  animation: shadow046 .5s alternate infinite ease;
}

@keyframes shadow046 {
  0% {
    transform: scaleX(1.5);
  }

  40% {
    transform: scaleX(1);
    opacity: .7;
  }

  100% {
    transform: scaleX(.2);
    opacity: .4;
  }
}

.shadow:nth-child(4) {
  left: 45%;
  animation-delay: .2s
}

.shadow:nth-child(5) {
  left: auto;
  right: 15%;
  animation-delay: .3s;
}

</style>