<script setup lang='ts'>
import { onMounted } from "vue";
import { Search } from '@icon-park/vue-next'
import { useSettingStore, useWebsiteInfoStore } from "@/store";
import { storeToRefs } from "pinia";
import { scrollUp } from '@/utils/utils'
import {
  navAreaRef,
  navAreaClassActive,
  effect1configClassActive,
  navScrollHandle,
} from './headerHooks'
import WebSite from './website/WebSite.vue'
import NavPc from './NavPc.vue'
import NavSmall from './NavSmall.vue'

const settingStore = useSettingStore()
const { searchFlag } = storeToRefs(settingStore)

const websiteInfoStore = useWebsiteInfoStore()
const { websiteInfo } = storeToRefs(websiteInfoStore)

onMounted(() => {
  navScrollHandle()
})
</script>

<template>
  <header ref="navAreaRef" class="nav-area fixed top-0 w-full h-44px z-1" :class="navAreaClassActive">
    <div class="flex justify-between items-center h-full py-0 px-10px">
      <div class="nav-logo relative h-full z-1" @click="scrollUp">
        <router-link to="/" class="text-26px leading-44px">
          <h1 class="logo-text" :data-text="websiteInfo?.websiteName">{{ websiteInfo?.websiteName }}</h1>
        </router-link>
      </div>
      <div class="flex justify-center items-center gap-5px">
        <div
          class="nav-search flex justify-center items-center gap-5px p-5px hover:(bg-$theme-bg-reverse text-$text-color-reverse rounded-4px)"
          @click="searchFlag = true">
          <Search size="20" />
          搜索
        </div>
        <nav-pc />
        <nav-small />
      </div>
    </div>
    <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160"
      class="nav-effect1config nav-effect1config-left" :class="effect1configClassActive">
      <path id="Trazado_200" data-name="Trazado 200" d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
        transform="translate(0 10)"></path>
    </svg>
    <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160"
      class="nav-effect1config nav-effect1config-right" :class="effect1configClassActive">
      <path id="Trazado_200" data-name="Trazado 200" d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
        transform="translate(0 10)"></path>
    </svg>
  </header>
  <WebSite />
</template>

<style scoped>
.nav-area {
  transition: top .6s, var(--theme-transition-bg);
}

.logo-text::after {
  content: attr(data-text);
  color: transparent;
  position: absolute;
  top: 0;
  left: 0;
  animation: spotlight 5s infinite;
  background: #833ab4;
  background: -webkit-linear-gradient(to right, #fcb045, #fd1d1d, #833ab4);
  background: linear-gradient(to right, #fcb045, #fd1d1d, #833ab4);
  -webkit-background-clip: text;
  background-clip: text;
}

.nav-search {
  transition: var(--theme-transition-bg);
  border-radius: 7px 20px 20px 20px;
}

.nav-search:hover {
  border-radius: 7px 20px 20px 20px;
}

/*滚动条往下时*/
.nav-area.nav-fixed {
  @apply '-top-71px';
}

/*滚动条往上时*/
.nav-area.nav-sticky {
  @apply 'top-0 z-8 bg-$theme-bg';
}

/*滚动条到顶时*/
.nav-area.nav-top {
  @apply 'top-0 bg-transparent';
}

/* 导航下面贴着的两个小角 */

.nav-effect1config,
.effect1config {
  transition: var(--theme-transition-fill)
}

.nav-effect1config {
  @apply 'w-60px h-60px top-38px absolute fill-transparent';
}

.nav-effect1config-left {
  @apply '-left-7px';
  transform: rotate(280deg);
}

.nav-effect1config-right {
  @apply '-right-7px';
  transform: rotate(-10deg);
}

.nav-effect1config-left.effect-fixed {
  @apply '-left-full';

}

.nav-effect1config-right.effect-fixed {
  @apply '-right-full';
}

/* 向下滚动时隐藏 */
.nav-effect1config-left.effect-fixed,
.nav-effect1config-right.effect-fixed {
  @apply 'opacity-0';
  transition: all 1s ease, opacity .5s ease-out;
}

/* 向上滚动时展示 */
.nav-effect1config-left.effect-sticky,
.nav-effect1config-right.effect-sticky {
  @apply 'fill-$theme-bg';
}

/* 到达顶部时隐藏*/
.nav-effect1config-left.effect-top {
  @apply '-left-full';
  transition: all 1s !important;
}

/* 到达顶部时隐藏*/
.nav-effect1config-right.effect-top {
  @apply '-right-full';
  transition: all 1s !important;
}


@keyframes spotlight {
  0% {
    -webkit-clip-path: ellipse(10px 100px at 0% 50%);
    clip-path: ellipse(10px 100px at 0% 50%);
  }

  50% {
    -webkit-clip-path: ellipse(10px 100px at 100% 50%);
    clip-path: ellipse(10px 1000px at 100% 50%);
  }

  100% {
    -webkit-clip-path: ellipse(10px 100px at 0% 50%);
    clip-path: ellipse(10px 100px at 0% 50%);
  }
}
</style>