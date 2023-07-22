<script setup lang='ts'>
import type { Ref } from 'vue'
import type { StompSubscription } from '@stomp/stompjs'
import { nextTick, onBeforeUnmount, onMounted, onUnmounted, ref, watch } from 'vue'
import { parseComment, getDate } from '@/utils/utils'
import CommentUser from '@/views/comm/CommentUser.vue'
import Comment from '@/components/comment/Comment.vue'
import { useWebSocketStore, useUserInfoStore, useSettingStore } from "@/store"
import { storeToRefs } from 'pinia'
import { getChatVisitorInfo } from "@/api/requests/ChatRoom"
import { msgList, determineUser } from './chatRoom'
import { notify } from "@kyvg/vue3-notification"

const websocketStore = useWebSocketStore();
const userInfoStore = useUserInfoStore();
const settingStore = useSettingStore();
const { isJoinChat, hasVisitorUser } = storeToRefs(websocketStore);
const { socket, addVisitorUser } = websocketStore;
const { loadingFlag } = storeToRefs(settingStore);

const msgContentRef: Ref<HTMLElement | null> = ref(null);
// 群聊Subscribe
let publicChatSub: StompSubscription | null;


// 发送聊天内容
const sendMessage = ({ commentText }: any) => {
  if (commentText.value.trim().length !== 0) {
    socket.send("/sendChatMessage", commentText.value)
  }
  commentText.value = "";
}

// 监听用户登录状态，重新订阅
watch(() => userInfoStore.isLogin, () => {
  if (publicChatSub) {
    // 退出登录前告诉其他人我走了
    socket.send("/joinOrLeaveChatRoom", null, { type: '3' })
    unSubPublicChat();
    intervalPublicChatSub();
  }
})

// 订阅聊天内容
const publicChatFunc = () => {
  if (socket.stompClient.connected) {
    publicChatSub = socket.subscribe("/public/chat", (res: any) => {
      if (res.data.status) {
        res.data.message = parseComment(res.data.message, '24', '24');
        msgList.push(res as ChatMessage<ResultObject<string>>);
        scrollToBottom();
      }
    })
  }
}

// 消息滚动到最后
const scrollToBottom = () => {
  nextTick(() => {
    msgContentRef.value?.scrollTo({
      behavior: "smooth",
      top: msgContentRef.value?.scrollHeight
    });
  })
}

// 定时订阅，直到订阅成功
const intervalPublicChatSub = () => {
  if (!publicChatSub) {
    let time = setInterval(() => {
      publicChatFunc();
      if (publicChatSub) {
        clearInterval(time);
      }
    }, 1000)
  }
}

// 取消订阅
const unSubPublicChat = () => {
  if (publicChatSub) {
    publicChatSub.unsubscribe();
    publicChatSub = null;
  }
}

// 加入聊天室
const joinChat = async () => {
  // 没有登录且没有游客信息时，获取游客信息
  if (!userInfoStore.isLogin && hasVisitorUser.value) {
    loadingFlag.value = true;
    await getChatVisitorInfo().then((resp: ResultObject<VisitorUser>) => {
      addVisitorUser(resp.data)
    }).catch(() => {
      notify({
        text: "获取游客信息失败，请重试",
        type: "warm"
      });
    }).finally(() => {
      loadingFlag.value = false;
    })
  }
  // 可能会订阅失败，为空重新订阅
  if (!publicChatSub) {
    publicChatFunc();
  }
  if (publicChatSub) {
    // 告诉其他人我来了
    socket.send("/joinOrLeaveChatRoom", null, { type: '2' })
    isJoinChat.value = true;
  }
}

onMounted(() => {
  isJoinChat.value = false;
  intervalPublicChatSub();
})

onBeforeUnmount(() => {
  // 告诉其他人我走了
  if (socket.stompClient.connected) {
    socket.send("/joinOrLeaveChatRoom", null, { type: '3' })
  }
})

onUnmounted(() => {
  unSubPublicChat();
  // 清空聊天内容
  msgList.length = 0;
})
</script>

<template>
  <div ref="msgContentRef" class="py-10px px-15px shadow-inset rounded-8px min-h-500px max-h-500px overflow-y-auto">
    <div class="chat-wrapper flex flex-col gap-25px">
      <div class="flex gap-10px flex-col" v-for="msg of msgList" :key="msg.sender.uid + Math.random()">
        <div class="flex gap-10px" :class="{ 'chat-right': determineUser(msg.sender.uid) }" v-if="msg.type === 'CHAT'">
          <div class="w-50px h-50px <md:(w-40px h-40px)">
            <img class="w-full h-full rounded-full shake" :src="msg.sender.avatar" alt="">
          </div>
          <div class="flex-1 chat-user-info">
            <div class="user-info-inner">
              <span class="text-14px text-$hover-color3 <md:(text-12px)">{{ msg.sender.nickname }}</span>
              <span class="text-10px text-$text-color2 mx-10px">{{ msg.ipSource }}</span>
            </div>
            <div class="inline-block">
              <div class="inline-block mt-10px mb-5px chat-text">
                <p class="shadow rounded-6px py-5px px-10px align-middle whitespace-pre-line break-all <md:(text-12px)"
                  v-html="msg.data.message"></p>
              </div>
              <div class="chat-send-time">
                <span class="text-10px text-$text-color2">{{ getDate(msg.time.toString()) }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="text-center <md:(text-14px)" v-else>
          <template v-if="msg.type === 'JOIN'">
            <span class="text-$hover-color hover:(text-$text-color)">{{ msg.sender.nickname }}</span>
            <span class="text-10px text-$text-color2 ml-5px">加入了</span>
          </template>
          <template v-if="msg.type === 'LEAVE'">
            <span class=" text-$hover-color2 hover:(text-$text-color)">{{ msg.sender.nickname }}</span>
            <span class="text-10px text-$text-color2 ml-5px">离开了</span>
          </template>
        </div>
      </div>
    </div>
  </div>
  <hr class="hr-twill my-20px" />
  <!-- 发送消息 -->
  <CommentUser class="shadow-inset" v-if="isJoinChat">
    <Comment @save-comment="sendMessage" :has-login="false" send-btn-text="发送" placeholder="聊点啥？" />
  </CommentUser>
  <div class="flex justify-center items-center w-full h-50px" v-else>
    <button class="py-8px px-12px rounded-8px shadow text-18px" @click="joinChat">加入聊天室</button>
  </div>
</template>

<style scoped>
.chat-wrapper .chat-right {
  @apply 'flex-row-reverse'
}

.chat-wrapper .chat-right .chat-user-info {
  @apply 'flex flex-col items-end'
}

.chat-wrapper .chat-right .chat-user-info .user-info-inner {
  @apply 'flex flex-row-reverse items-baseline'
}

.chat-wrapper .chat-right .chat-user-info .chat-text {
  @apply 'flex justify-end'
}

.chat-wrapper .chat-right .chat-user-info .chat-send-time {
  @apply 'text-right'
}
</style>