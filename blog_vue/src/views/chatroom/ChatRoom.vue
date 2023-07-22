<script setup lang='ts'>
import BoxComponent from '@/components/BoxComponent.vue'
import Chat from './Chat.vue'
import OnlineUsers from './OnlineUsers.vue'
import { getChatRoomNotice } from './chatRoom'

</script>

<template>
  <div class="relative w-full h-full flex justify-between gap-20px <md:(flex-col)">
    <div class="chat-window w-full flex flex-col">
      <!-- 聊天主体 -->
      <Chat />
    </div>
    <div class="chat-wrapper w-full flex flex-col gap-20px">
      <!-- 聊天室公告 -->
      <BoxComponent class="px-10px py-5px">
        <span class="text-14px text-$hover-color2 flex items-center gap-8px">公告</span>
        <div class="my-5px">
          <p class="text-12px whitespace-pre-line break-all" v-html="getChatRoomNotice()"></p>
        </div>
      </BoxComponent>
      <!-- 在线用户列表 -->
      <OnlineUsers />
    </div>
  </div>
</template>

<style scoped>
.chat-window {
  flex: 0 0 75%;
}

.chat-wrapper {
  @apply 'sticky top-15px left-0 w-full h-full <md:(static)';
}
</style>@/utils/WebSocketUtils