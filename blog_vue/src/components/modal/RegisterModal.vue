<script setup lang='ts'>
import type {Ref} from "vue"
import Modal from "./Modal.vue"
import Verify from "@/components/verifition/Verify.vue"
import {PreviewOpen, PreviewClose} from '@icon-park/vue-next'
import {useSettingStore} from '@/store'
import {storeToRefs} from "pinia";
import {register, sendEmailCode} from "@/api/requests/User"
import {ref, reactive, computed} from "vue";
import {notify} from "@kyvg/vue3-notification";
import {matchEmail, matchPassword, timingConfetti} from "@/utils/utils";

const settingStore = useSettingStore()
const {registerFlag, loginFlag, countDown, loadingFlag} = storeToRefs(settingStore)

// 显示密码动画切换
const showPasswordActiveClass = ref(false);
const verify: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 滑动验证码
const registerForm = reactive({
  email: "",
  password: "",
  code: "",
  captchaVerification: ""
})

const showPassword = () => {
  showPasswordActiveClass.value = !showPasswordActiveClass.value;
}

// 清空注册表单
const clearRegisterForm = () => {
  registerForm.email = ""
  registerForm.password = ""
  registerForm.code = ""
  registerForm.captchaVerification = ""
}

const closeModal = () => {
  showPasswordActiveClass.value = false
  clearRegisterForm();
  settingStore.closeModal()
}

// 打开登录框
const loginHandle = () => {
  closeModal();
  loginFlag.value = true;
}

// 判断是否输入了
const checkRegisterFormComp = computed(() => {
  const email = registerForm.email.trim().length === 0;
  const password = registerForm.password.length === 0;
  const code = registerForm.code.trim().length === 0;
  return email || password || code;
})

// 预注册处理
const preRegisterHandle = () => {
  if (!matchEmail(registerForm.email)) {
    notify({
      text: "邮箱格式不正确",
      type: "warn"
    })
    return;
  }
  if (!matchPassword(registerForm.password)) {
    notify({
      text: "密码需要包含字母和数字，最少6位",
      type: "warn"
    })
    return;
  }
  if (registerForm.code.trim().length === 0) {
    notify({
      text: "请输入验证码",
      type: "warn"
    })
    return;
  }
  if (checkRegisterFormComp) {
    (<any>verify.value).show();
  }
}

// 发送邮箱
const sendEmailHandle = async () => {
  if (!countDown.value.countDownIng && matchEmail(registerForm.email)) {
    countDown.value.countDownIng = true;
    settingStore.countDownHandle();
    await sendEmailCode(registerForm.email).then((resp) => {
      if (resp.status) {
        notify({
          text: `验证码已发送至 ${registerForm.email} 请注意查收~`,
        })
      } else {
        notify({
          text: resp.message,
          type: "warn"
        });
      }
    }).catch(() => {
      // 发送失败，倒计时清空
      countDown.value.countDownTime = 60;
      countDown.value.countDownIng = false;
      countDown.value.startTime = null;
      if (countDown.value.timer) {
        clearInterval(countDown.value.timer);
        countDown.value.timer = null;
      }
      notify({
        text: `验证码发送失败，请重试`,
      });
    });
  }
}

// 注册
const verifyHandle = async (verify: { captchaVerification: string }) => {
  registerForm.captchaVerification = verify.captchaVerification;
  notify({
    text: "正在保存...",
  });
  loadingFlag.value = true;
  await register({
    email: registerForm.email,
    password: registerForm.password,
    code: registerForm.code,
    captchaVerification: registerForm.captchaVerification
  }).then((resp) => {
    if (resp.status) {
      notify({
        text: "注册成功",
        type: "success"
      });
      loginHandle();
      timingConfetti();
    } else {
      notify({
        text: resp.message,
        type: "warn"
      });
    }
  }).catch(() => {
    notify({
      text: "注册失败，请重试",
      type: "warn"
    });
  }).finally(() => {
    loadingFlag.value = false;
    clearRegisterForm();
  })
}

</script>

<template>
  <Modal title="注册" v-model:show="registerFlag" @closeModal="closeModal">
    <form @submit.prevent="preRegisterHandle" class="flex flex-col relative"
          :class="{'show-password': showPasswordActiveClass}">
      <div class="flex flex-col gap-5px mb-10px">
        <label>邮箱号</label>
        <input type="text" placeholder="请输入邮箱地址" v-model="registerForm.email"
               @focusin="showPasswordActiveClass = false"
               class="w-full h-full py-6px indent-2px outline-none border-none bg-$theme-bg !border-dashed !border-1 border-$theme-bg-reverse px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
               autocomplete="off">
      </div>
      <div class="flex flex-col gap-5px relative mb-10px">
        <label>密码<span class="ml-8px text-12px text-$hover-color2">需要包含字母和数字，最少6位</span></label>
        <input
          v-model="registerForm.password"
          class="password-inp w-full h-full py-6px relative z-2 indent-2px outline-none border-none bg-transparent !border-dashed !border-1 border-$theme-bg-reverse py-2px px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
          :type="showPasswordActiveClass ? 'text': 'password'" placeholder="请输入密码"
          autocomplete="off">
        <PreviewOpen v-if="showPasswordActiveClass"
                     class="absolute z-2 right-1px cursor-pointer w-25px mt-37px  rounded-6px flex justify-center items-center bg-$theme-bg"
                     @click="showPassword"/>
        <PreviewClose v-else
                      class="absolute z-2 right-1px cursor-pointer w-25px mt-37px rounded-6px flex justify-center items-center bg-$theme-bg"
                      @click="showPassword"/>
        <div class="beam w-19/20 h-80px top-1/2 mt-14px right-20px absolute"></div>
      </div>
      <div class="flex flex-col gap-5px mb-10px">
        <label>验证码</label>
        <div class="flex gap-15px">
          <input type="text" placeholder="请输入验证码" v-model="registerForm.code"
                 @focusin="showPasswordActiveClass = false"
                 class="flex-1 w-full h-full py-6px indent-2px outline-none border-none bg-$theme-bg !border-dashed !border-1 border-$theme-bg-reverse px-4px rounded-6px text-16px justshake focus:(border-$hover-color)">
          <span class="send-code block p-6px shadow relative cursor-not-allowed rounded-6px"
                :class="{'checked !cursor-not-allowed': countDown.countDownIng, 'email-no-empty cursor-pointer': matchEmail(registerForm.email)}"
                :data-email="registerForm.email"
                @click="sendEmailHandle">{{
              countDown.countDownIng ? `重新获取 ${countDown.countDownTime}s` : '发送验证码'
            }}</span>
        </div>
      </div>
      <div class="flex justify-center mt-10px">
        <button type="submit" class="py-6px px-12px rounded-6px shadow text-18px outline-none"
                :class="{'!cursor-not-allowed shadow-inset text-$text-color2': checkRegisterFormComp}"
                :disabled="checkRegisterFormComp">注 册
        </button>
      </div>
      <div class="text-12px mt-10px">
        <span class="cursor-pointer text-$text-color2 hover:(text-$hover-color2)" @click="loginHandle"
              style="transition: var(--theme-transition-color)">已有账号？点击登录</span>
      </div>
    </form>
  </Modal>
  <Verify @success="verifyHandle" mode="pop" captchaType="blockPuzzle" ref="verify"/>
</template>

<style>
.send-code {
  transition: var(--theme-transition-shadow);
}

.send-code.email-no-empty:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}

.send-code.email-no-empty:hover::after {
  @apply 'absolute left-1/2 -bottom-32px w-auto w-max py-3px px-6px whitespace-nowrap rounded-6px text-12px bg-$theme-bg';
  content: attr(data-email);
  transform: translateX(-50%);
  box-shadow: var(--theme-shadow);
  transition: var(--theme-transition-shadow), var(--theme-transition-bg);
}

.send-code.email-no-empty:hover::before {
  @apply 'absolute left-1/2 -bottom-15px z-1';
  content: "";
  transform: translateX(-50%);
  border-top: 10px solid transparent;
  border-bottom: 10px solid var(--theme-bg);
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  transition: border var(--transition);
}

.send-code.checked {
  box-shadow: var(--theme-shadow-inset) !important;
}
</style>