<script setup lang='ts'>
import type { StompSubscription } from '@stomp/stompjs'
import { useWebSocketStore, useUserInfoStore, useSettingStore } from "@/store"
import { onlineUserList } from './chatRoom'
import BoxComponent from '@/components/BoxComponent.vue'
import Skeleton from '@/components/skeleton/Skeleton.vue'
import { watch, onMounted, onUnmounted } from 'vue';
import { getOnlineUserList } from '@/api/requests/ChatRoom'
import { storeToRefs } from 'pinia';
import { notify } from '@kyvg/vue3-notification'
import { loadingImg } from "@/constant"

const userInfoStore = useUserInfoStore();
const settingStore = useSettingStore();
const websocketStore = useWebSocketStore();
const { socket } = websocketStore;
const { loadingFlag } = storeToRefs(settingStore);
// 在线用户列表Subscribe
let onlineUserSub: StompSubscription | null;

// 订阅在线用户列表
const onlineUserSubFunc = async () => {
  if (socket.stompClient.connected) {
    onlineUserSub = socket.subscribe("/public/onlineUser", (resp: any) => {
      if (resp.status) {
        onlineUserList.value = resp.data as Array<OnlineUser>;
      }
    })
    loadingFlag.value = true;
    // 首次进入，获取在线用户列表
    await getOnlineUserList().then((resp: ResultObject<Array<OnlineUser>>) => {
      if (resp.status) {
        onlineUserList.value = resp.data;
      }
    }).catch(() => {
      notify({
        text: "获取在线用户列表失败，请重试",
        type: "warm"
      });
    }).finally(() => {
      loadingFlag.value = false;
    });
  }
}

// 定时订阅，直到订阅成功
const intervalOnlineUserSub = () => {
  if (!onlineUserSub) {
    let time = setInterval(() => {
      onlineUserSubFunc();
      if (onlineUserSub) {
        clearInterval(time);
      }
    }, 1000)
  }
}

// 取消订阅
const unSubOnlineUser = () => {
  if (onlineUserSub) {
    onlineUserSub.unsubscribe();
    onlineUserSub = null;
  }
}

// 监听用户登录状态，重新订阅
watch(() => userInfoStore.isLogin, () => {
  if (onlineUserSub) {
    unSubOnlineUser();
    intervalOnlineUserSub();
  }
})

onMounted(() => {
  intervalOnlineUserSub();
})

onUnmounted(() => {
  unSubOnlineUser();
})
</script>

<template>
  <BoxComponent class="px-10px py-5px" v-if="onlineUserList">
    <div class="flex justify-between items-center">
      <span class="text-14px text-$hover-color2">在线用户</span>
      <span class="text-12px">
        在线：<span class="text-$hover-color">{{ onlineUserList.length }}</span> 人
      </span>
    </div>
    <ul class="online-user mt-10px max-h-400px overflow-y-auto flex flex-col gap-3px">
      <li class="online-user-item px-10px py-6px rounded-6px" v-for="user of onlineUserList" :key="user.uid">
        <div class="flex items-center">
          <img v-lazy :data-src="user.avatar" :src="loadingImg" alt=""
            class="w-40px h-40px rounded-full <md:(w-30px h-30px) mr-10px shake">
          <div class="flex flex-col">
            <span class="text-14px text-$text-color mb-5px">{{ user.nickname }}</span>
            <span class="text-10px text-$text-color2">{{ user.ipSource }}</span>
          </div>
        </div>
      </li>
    </ul>
  </BoxComponent>
  <BoxComponent v-else>
    <Skeleton class="!w-full !h-100px" />
  </BoxComponent>
</template>

<style scoped>
.online-user .online-user-item:hover {
  background-color: rgba(0, 0, 0, .2);
}
</style>