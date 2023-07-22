<script setup lang='ts'>
import { computed, onBeforeMount } from 'vue'
import { SettingTwo } from '@icon-park/vue-next'
import { storeToRefs } from "pinia"
import { useSettingStore } from "@/store"
import {
  websiteClassActive
} from "../headerHooks"
import WebSiteSide from './WebSiteSide.vue'
import ChatRoomBtn from './ChatRoomBtn.vue'


const settingStore = useSettingStore();
const { globalTheme, theme } = storeToRefs(settingStore);
const themeValue = computed(() => globalTheme.value !== "darkTheme")

onBeforeMount(() => {
  // 初始化主题
  document.documentElement.className = theme.value;
})
</script>

<template>
  <!-- 左下角设置 -->
  <div
    class="website-setting z-5 fixed left-0 bottom-0 w-90px h-30px bg-$theme-bg2 flex justify-center items-center gap-12px text-$theme-bg-reverse"
    :class="websiteClassActive">
    <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160" class="effect1config">
      <path id="Trazado_200" data-name="Trazado 200" d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
        transform="translate(0 10)"></path>
    </svg>
    <div class="setting-icon cursor-pointer avatar">
      <SettingTwo size="24" />
    </div>
    <label class="text-12px relative inline-block w-41px h-22px rounded-30px mt-2px">
      <input class="theme-checkbox opacity-0 w-0 h-0" type="checkbox" v-model="themeValue"
        @click="settingStore.changeTheme">
      <span
        class="theme-slider absolute cursor-pointer top-0 left-0 right-0 bottom-0 bg-$theme-bg rounded-30px duration-500 cursor-pointer"></span>
    </label>
    <WebSiteSide />
    <ChatRoomBtn />
  </div>
</template>

<style scoped>
@import url('@/assets/css/header/website.css');
</style>