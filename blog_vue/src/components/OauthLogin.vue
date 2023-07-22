<template>
  <div
    class="fixed top-0 left-0 right-0 bottom-0 z-999 w-full h-full bg-$theme-bg flex justify-center items-center flex-col">
    <div class="loader"></div>
    <p class="text-center mt-20px">{{ loginTip }}</p>
  </div>
</template>

<script setup lang="ts">

import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router"
import { bindQQ, qqLogin } from "@/api/requests/User";
import { useSettingStore, useUserInfoStore, useWebsiteInfoStore, useGlobalStore, useWebSocketStore } from "@/store";
import { customConfetti } from "@/utils/utils";
import { getUserNoReadNoticeCount } from "@/api/requests/Notice";
import { storeToRefs } from "pinia";
import { notify } from "@kyvg/vue3-notification";


const globalStore = useGlobalStore();
const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const websiteInfoStore = useWebsiteInfoStore();
const webSocketStore = useWebSocketStore();
const { userNoticeCount, isBindQQ } = storeToRefs(userInfoStore);
const { originalLoginUrl } = storeToRefs(websiteInfoStore);
const { socket, socketConnect } = webSocketStore;
const { isJoinChat } = storeToRefs(webSocketStore);


const route = useRoute();
const router = useRouter();
notify({
  text: "正在登录",
})
const loginTip = ref("登录中...");
onMounted(() => {
  settingStore.closeModal();
  if (route.path === "/oauth/qq/callback") {
    if (QC.Login.check()) {
      QC.Login.getMe((openId: string, accessToken: string) => {
        // 绑定QQ
        if (!isBindQQ.value) {
          bindQQ({ openId, accessToken }).then((resp) => {
            if (resp.status) {
              notify({
                text: resp.message,
                type: "success"
              })
            } else {
              notify({
                text: resp.message,
                type: "warn"
              })
            }
          }).catch(() => {
            notify({
              text: "绑定失败，请重试",
              type: "warn"
            })
          }).finally(() => {
            loginTip.value = "正在跳转..."
            globalStore.reload();
          })
        } else {
          qqLogin({ openId, accessToken }).then(async (resp) => {
            if (resp.status) {
              notify({
                text: `登录成功，欢迎回来！${resp.data.nickname}`,
                type: 'success'
              })
              if (resp.data.email === null) {
                notify({
                  text: "请绑定邮箱，以便及时收到回复",
                  type: "warn"
                })
              }
              // 保存用户信息
              userInfoStore.saveUserInfo(resp.data);
              customConfetti();
              // 获取用户未读通知数量
              await getUserNoReadNoticeCount().then((respNotice) => {
                if (respNotice.status) {
                  userNoticeCount.value = respNotice.data;
                }
              })
              // 关闭socket
              socket.close();
              socket.stompClient.onDisconnect = (frame) => {
                // 连接WebSocket
                if (!socket.stompClient.connected) {
                  socketConnect();
                }
              }
              isJoinChat.value = false;
            } else {
              notify({
                text: resp.message,
                type: 'warn'
              })
            }
          }).catch(() => {
            notify({
              text: "登录失败，请重试",
              type: 'warn'
            })
          }).finally(() => {
            loginTip.value = "正在跳转..."
          })
        }
      })
    }
  }
  // 跳转回原页面
  if (originalLoginUrl.value !== null && originalLoginUrl.value !== "") {
    router.push({ path: originalLoginUrl.value });
  } else {
    router.push({ path: "/" });
  }
})


</script>

<style scoped>
.loader {
  @apply 'w-50px h-50px relative z-1';
  transform: translateX(-50%);
}

.loader::before,
.loader::after {
  @apply 'absolute rounded-1/2';
  content: '';
  width: inherit;
  height: inherit;
  mix-blend-mode: multiply;
  animation: rotate92523 2s infinite cubic-bezier(0.77, 0, 0.175, 1);
}

.loader::before {
  background-color: #75e2ff;
}

.loader::after {
  background-color: #ff8496;
  animation-delay: 1s;
}

@keyframes rotate92523 {

  0%,
  100% {
    left: 35px;
  }

  25% {
    transform: scale(.3);
  }

  50% {
    left: 0%;
  }

  75% {
    transform: scale(1);
  }
}
</style>