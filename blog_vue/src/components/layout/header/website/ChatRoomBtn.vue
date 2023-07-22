<script setup lang='ts'>
import { Communication } from '@icon-park/vue-next'
import { websiteClassActive } from '../headerHooks'
import { scrollDown } from '@/utils/utils'
</script>

<template>
  <!-- 右下角进入聊天室按钮 -->
  <router-link to="/chat" tag="div" @click="scrollDown"
    class="website-chat-btn z-5 <xl:(hidden) fixed bottom-10px w-50px h-50px bg-$theme-bg border-2px border-$theme-bg-reverse border-double flex justify-center items-center shadow-inset text-$theme-bg-reverse hover:(bg-$theme-bg-reverse text-$theme-bg)"
    :class="websiteClassActive">
    <communication theme="outline" size="28" />
    <span
      class="chatroom-btn-tips -z-1 absolute -right-200px whitespace-nowrap bg-$text-color text-$text-color-reverse py-2px px-5px rounded-6px w-auto w-max">进入聊天室</span>
  </router-link>
</template>

<style scoped>
@import url('@/assets/css/header/website.css');
</style>