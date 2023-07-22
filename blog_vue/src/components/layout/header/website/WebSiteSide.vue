<script setup lang='ts'>
import { ToTop, Theme } from "@icon-park/vue-next"
import { scrollUp } from '@/utils/utils'
import { storeToRefs } from 'pinia'
import { useSettingStore } from '@/store'
import { websiteClassActive } from '../headerHooks'

const settingStore = useSettingStore();
const { articleThemeFlag } = storeToRefs(settingStore);

</script>

<template>
  <!-- 侧边设置 -->
  <div class="website-side-wrapper active fixed bottom-45px -left-full" :class="websiteClassActive"
    style="border-radius: 0 17px 17px 0;">
    <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160" class="effect1config normal">
      <path id="Trazado_200" data-name="Trazado 200" d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
        transform="translate(0 10)"></path>
    </svg>
    <ul class="bg-$theme-bg2 py-8px px-5px flex flex-col gap-8px z-5"
      style="border-radius: 0 17px 17px 0;transition: var(--theme-transition-bg)">
      <li class="setting-item flex justify-center items-center relative" @click="scrollUp">
        <div class="p-3px rounded-full text-$text-color">
          <ToTop size="22" />
        </div>
        <span
          class="setting-tips -z-1 absolute -left-2/1 whitespace-nowrap bg-$text-color text-$text-color-reverse py-2px px-5px rounded-6px w-auto w-max">滚动到顶部</span>
      </li>
      <li class="setting-item flex justify-center items-center relative" @click="articleThemeFlag = true">
        <div class="p-3px rounded-full text-$text-color">
          <Theme size="22" />
        </div>
        <span
          class="setting-tips -z-1 absolute -left-2/1 whitespace-nowrap bg-$text-color text-$text-color-reverse py-2px px-5px rounded-6px w-auto w-max">修改文章预览主题</span>
      </li>
    </ul>
    <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160" class="effect1config invert">
      <path id="Trazado_200" data-name="Trazado 200" d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
        transform="translate(0 10)"></path>
    </svg>
  </div>
</template>

<style scoped>
@import url('@/assets/css/header/website.css');
</style>