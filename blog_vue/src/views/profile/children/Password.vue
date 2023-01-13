<script setup lang='ts'>
import {PreviewOpen, PreviewClose} from '@icon-park/vue-next'
import Verify from "@/components/verifition/Verify.vue"
import {
  passwordForm,
  showOldPasswordStatus,
  showNewPasswordStatus,
  showOldPassword,
  showNewPassword,
  sendEmailHandle,
  verifyHandle,
  checkPasswordFormComp
} from "./PasswordHooks"
import {useSettingStore} from "@/store";
import {storeToRefs} from "pinia";
import {matchPassword} from "@/utils/utils";
import {notify} from "@kyvg/vue3-notification";
import {onBeforeUnmount, ref, Ref} from "vue";

const settingStore = useSettingStore();
const verify: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 滑动验证码
const {loginFlag, countDown} = storeToRefs(settingStore)

const preUpdatePasswordHandle = () => {
  if (!matchPassword(passwordForm.newPassword)) {
    notify({
      text: "密码需要包含字母和数字，最少6位",
      type: "warn"
    })
    return;
  }
  if (passwordForm.code.trim().length === 0) {
    notify({
      text: "请输入验证码",
      type: "warn"
    })
    return;
  }
  if (checkPasswordFormComp) {
    (<any>verify.value).show();
  }
}


onBeforeUnmount(() => {
  passwordForm.oldPassword = "";
  passwordForm.newPassword = "";
  passwordForm.code = "";
  passwordForm.captchaVerification = "";
  showNewPasswordStatus.value = false;
  showOldPasswordStatus.value = false;
})
</script>

<template>
  <div>
    <form @submit.prevent="preUpdatePasswordHandle" class="flex flex-col gap-10px">
      <div class="flex flex-col gap-10px">
        <span>旧密码</span>
        <div class="relative w-220px <sm:(w-full)">
          <input :type="showOldPasswordStatus ? 'text' : 'password'"
                 v-model="passwordForm.oldPassword"
                 placeholder="目前使用的密码"
                 class="input-focus border-none bg-transparent rounded-6px py-3px px-8px outline-none shadow w-full">
          <span class="absolute text-center top-1/2 right-5px" style="transform: translateY(-50%)">
          <PreviewOpen size="16" v-if="showOldPasswordStatus" @click="showOldPassword"/>
          <PreviewClose size="16" v-else @click="showOldPassword"/>
        </span>
        </div>
      </div>
      <div class="flex flex-col gap-10px">
        <span>新密码</span>
        <div class="relative w-220px <sm:(w-full)">
          <input :type="showNewPasswordStatus ? 'text':'password'"
                 v-model="passwordForm.newPassword"
                 placeholder="要改的新密码"
                 class="input-focus border-none bg-transparent rounded-6px py-3px px-8px outline-none shadow w-full">
          <span class="absolute text-center top-1/2 right-5px" style="transform: translateY(-50%)">
          <PreviewOpen size="16" v-if="showNewPasswordStatus" @click="showNewPassword"/>
          <PreviewClose size="16" v-else @click="showNewPassword"/>
        </span>
        </div>
      </div>
      <div class="flex flex-col gap-10px">
        <span>验证码</span>
        <div class="flex gap-15px w-220px <sm:(w-full)">
          <input type="text"
                 v-model="passwordForm.code"
                 placeholder="验证码"
                 class="input-focus border-none bg-transparent rounded-6px py-3px px-8px outline-none shadow flex-1">
          <span
              class="send-code block whitespace-nowrap w-auto w-max py-2px px-6px shadow cursor-pointer relative rounded-6px"
              :class="{'checked !cursor-not-allowed': countDown.countDownIng}"
              @click="sendEmailHandle">{{
              countDown.countDownIng ? `重新获取 ${countDown.countDownTime}s` : '发送验证码'
            }}</span>
        </div>
      </div>
      <div class="mt-20px ml-90px <sm:(ml-0 flex justify-center)">
        <button class="shadow-hover py-3px px-10px rounded-6px"
                :class="{'!cursor-not-allowed shadow-inset': checkPasswordFormComp}" :disabled="checkPasswordFormComp">保
          存
        </button>
      </div>
    </form>
    <Verify @success="verifyHandle" mode="pop" captchaType="blockPuzzle" ref="verify"/>
  </div>
</template>

<style scoped>
.input-focus:focus {
  box-shadow: var(--theme-shadow-inset) !important;
}
</style>