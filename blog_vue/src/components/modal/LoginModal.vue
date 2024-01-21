<script setup lang='ts'>
import type { Ref } from "vue"
import { computed, reactive, ref } from "vue";
import Modal from "./Modal.vue"
import { PreviewClose, PreviewOpen } from '@icon-park/vue-next'
import { useSettingStore, useUserInfoStore, useWebSocketStore } from '@/store'
import { storeToRefs } from "pinia";
import Verify from "@/components/verifition/Verify.vue"
import { Login } from "@/api/requests/User"
import { notify } from "@kyvg/vue3-notification"
import { customConfetti } from "@/utils/utils";
import { qqLogin, showLoginIcon } from "@/utils/socialLoginUtils"
import { getUserNoReadNoticeCount } from "@/api/requests/Notice";

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const webSocketStore = useWebSocketStore();
const { loginFlag, registerFlag, forgetPasswordFlag } = storeToRefs(settingStore);
const { userNoticeCount } = storeToRefs(userInfoStore);
const { isBindQQ } = storeToRefs(userInfoStore);
const { socket, socketConnect } = webSocketStore;
const { isJoinChat } = storeToRefs(webSocketStore);


// 显示密码动画切换
const showPasswordActiveClass = ref(false);
const verify: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 滑动验证码
// 登录表单
const loginForm: UserLoginParams = reactive({
  username: "",
  password: "",
  captchaVerification: ""
})

const registerHandle = () => {
  closeModal()
  registerFlag.value = true
}

const forgetPasswordHandle = () => {
  closeModal();
  forgetPasswordFlag.value = true;
}

const showPassword = () => {
  showPasswordActiveClass.value = !showPasswordActiveClass.value
}

const closeModal = () => {
  showPasswordActiveClass.value = false
  loginForm.username = ""
  loginForm.password = ""
  loginForm.captchaVerification = ""
  settingStore.closeModal()
}
// 检查是否经输入用户和密码
const checkFormComp = computed(() => {
  const emailStatus = loginForm.username.trim().length === 0
  const passwordStatus = loginForm.password.length === 0
  return emailStatus || passwordStatus
})

// 登录处理
const loginHandle = (event: SubmitEvent) => {
  event.preventDefault();
  let reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
  if (!reg.test(loginForm.username)) {
    notify({
      text: "邮箱格式不正确",
      type: "warn"
    })
    return;
  }
  if (loginForm.password.trim().length === 0) {
    notify({
      text: "密码不能为空",
      type: "warn"
    })
    return;
  }
  if (checkFormComp) {
    (<any>verify.value).show();
  }
}

const verifyHandle = async (verify: { captchaVerification: string }) => {
  loginForm.captchaVerification = verify.captchaVerification
  const form = new FormData()
  form.append('username', loginForm.username)
  form.append('password', loginForm.password)
  form.append('captchaVerification', loginForm.captchaVerification)
  notify({
    text: "正在登录...",
  })
  await Login(form).then(async (resp: ResultObject<UserEntity>) => {
    if (resp.status) {
      notify({
        text: `登录成功，欢迎回来！${resp.data.nickname}`,
        type: 'success'
      })
      // 保存用户信息
      userInfoStore.saveUserInfo(resp.data);
      // 关闭登录窗口
      closeModal();
      customConfetti();
      // 获取用户未读通知数量
      await getUserNoReadNoticeCount().then((respNotice: ResultObject<number>) => {
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
      loginForm.username = "";
      loginForm.password = "";
      loginForm.captchaVerification = ""
    }
  }).catch(() => {
    notify({
      text: "登录失败，请重试",
      type: 'warn'
    })
  })
}

// QQ登录
const qqLoginHandle = () => {
  isBindQQ.value = true;
  qqLogin();
}

</script>

<template>
  <Modal title="登录" v-model:show="loginFlag" @closeModal="closeModal">
    <form @submit="loginHandle" class="flex flex-col justify-center items-center relative"
      :class="{ 'show-password': showPasswordActiveClass }">
      <div class="w-full rounded-6px h-35px">
        <input
          class="w-full h-full indent-2px outline-none border-none bg-$theme-bg !border-dashed !border-1 border-$theme-bg-reverse px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
          type="text" v-model="loginForm.username" placeholder="邮箱号" autocomplete="off"
          @focusin="showPasswordActiveClass = false">
      </div>
      <div class="w-full">
        <div class="w-full rounded-6px h-35px relative flex justify-center items-center mt-20px">
          <input
            class="password-inp w-full h-full relative z-2 indent-2px outline-none border-none bg-transparent !border-dashed !border-1 border-$theme-bg-reverse py-2px px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
            :type="showPasswordActiveClass ? 'text' : 'password'" v-model="loginForm.password" placeholder="密码"
            autocomplete="off">
          <PreviewOpen v-if="showPasswordActiveClass"
            class="absolute z-2 right-1px cursor-pointer w-25px rounded-6px h-14/15 flex justify-center items-center bg-$theme-bg"
            @click="showPassword" />
          <PreviewClose v-else
            class="absolute z-2 right-1px cursor-pointer w-25px rounded-6px h-14/15 flex justify-center items-center bg-$theme-bg"
            @click="showPassword" />
          <div class="beam w-19/20 h-80px top-1/2 right-20px absolute"></div>
        </div>
      </div>
      <div class="flex items-center w-full mt-15px">
        <div class="flex-1">
          <span class="text-$text-color2 text-12px cursor-pointer hover:(text-$hover-color2)"
            @click="registerHandle">注册</span>
        </div>
        <div class="flex-1 text-center">
          <button autofocus class="shadow py-6px px-12px rounded-6px text-18px outline-none"
            :class="{ '!cursor-not-allowed shadow-inset text-$text-color2': checkFormComp }" :disabled="checkFormComp">登 录
          </button>
        </div>
        <div class="flex-1 text-right">
          <span class="text-$text-color2 text-12px cursor-pointer hover:(text-$hover-color2)"
            @click="forgetPasswordHandle">忘记密码?</span>
        </div>
      </div>
    </form>
    <hr class="hr-twill mt-10px p-1px ">
    <!--  第三方登录按钮  -->
    <div class="w-full">
      <p class="text-12px text-center">社交登录</p>
      <div class="flex mt-5px" v-if="showLoginIcon('QQ')">
        <img src="@/assets/imgs/qq_login.png" alt="" @click="qqLoginHandle">
      </div>
    </div>
  </Modal>
  <Verify @success="verifyHandle" mode="pop" captchaType="blockPuzzle" ref="verify" />
</template>

<style>
/*模拟灯光*/
.beam {
  clip-path: polygon(100% 50%, 100% 50%, 0 0, 0 100%);
  transform: translateY(-50%) rotate(0deg);
  transform-origin: 100% 50%;
  transition: transform .2s ease-out, var(--theme-transition-bg);
}

.show-password .password-inp {
  color: var(--theme-bg);
}

.show-password .beam {
  background-color: var(--theme-bg-reverse);
  animation: beam-animate 1s ease-in-out forwards;
}
</style>