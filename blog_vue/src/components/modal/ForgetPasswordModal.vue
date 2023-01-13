<script setup lang='ts'>
import Modal from "@/components/modal/Modal.vue";
import Verify from "@/components/verifition/Verify.vue"
import {PreviewOpen, PreviewClose} from '@icon-park/vue-next'
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import {computed, reactive, Ref, ref} from "vue";
import {matchEmail, matchPassword} from "@/utils/utils";
import {sendEmail} from "@/utils/sendEmailUtils"
import {notify} from "@kyvg/vue3-notification";
import {findPassword} from "@/api/requests/User";

const settingStore = useSettingStore();
const {registerFlag, loginFlag, countDown, loadingFlag, forgetPasswordFlag} = storeToRefs(settingStore)

// 显示密码动画切换
const showPasswordActiveClass = ref(false);
const verify: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 滑动验证码
const forgetPasswordForm = reactive({
  email: "",
  newPassword: "",
  code: "",
  captchaVerification: ""
})
const showPassword = () => {
  showPasswordActiveClass.value = !showPasswordActiveClass.value;
}

// 打开登录框
const loginHandle = () => {
  closeModal();
  loginFlag.value = true;
}

// 打开注册框
const registerHandle = () => {
  closeModal();
  registerFlag.value = true;
}


// 清空注册表单
const clearForgetPasswordForm = () => {
  forgetPasswordForm.email = ""
  forgetPasswordForm.newPassword = ""
  forgetPasswordForm.code = ""
  forgetPasswordForm.captchaVerification = ""
}

const closeModal = () => {
  settingStore.closeModal();
  clearForgetPasswordForm();
}

// 判断是否输入了
const checkForgetPasswordFormComp = computed(() => {
  const email = forgetPasswordForm.email.trim().length === 0;
  const newPassword = forgetPasswordForm.newPassword.length === 0;
  const code = forgetPasswordForm.code.trim().length === 0;
  return email || newPassword || code;
})

const preForgetPasswordHandle = () => {
  if (!matchEmail(forgetPasswordForm.email)) {
    notify({
      text: "邮箱格式不正确",
      type: "warn"
    })
    return;
  }
  if (!matchPassword(forgetPasswordForm.newPassword)) {
    notify({
      text: "密码需要包含字母和数字，最少6位",
      type: "warn"
    })
    return;
  }
  if (forgetPasswordForm.code.trim().length === 0) {
    notify({
      text: "请输入验证码",
      type: "warn"
    })
    return;
  }
  if (checkForgetPasswordFormComp) {
    (<any>verify.value).show();
  }
}

const verifyHandle = async (verify: { captchaVerification: string }) => {
  forgetPasswordForm.captchaVerification = verify.captchaVerification;
  notify({
    text: "正在保存...",
  });
  loadingFlag.value = true;
  await findPassword({
    email: forgetPasswordForm.email,
    newPassword: forgetPasswordForm.newPassword,
    code: forgetPasswordForm.code,
    captchaVerification: forgetPasswordForm.captchaVerification
  }).then((resp) => {
    if (resp.status) {
      notify({
        text: "修改成功",
        type: "success"
      });
      loginHandle();
    } else {
      notify({
        text: resp.message,
        type: "warn"
      });
    }
  }).catch(() => {
    notify({
      text: "修改失败，请重试",
      type: "warn"
    });
  }).finally(() => {
    loadingFlag.value = false;
    clearForgetPasswordForm();
  })
}
</script>

<template>
  <Modal title="忘记密码" v-model:show="forgetPasswordFlag" @closeModal="closeModal">
    <form @submit.prevent="preForgetPasswordHandle" class="flex flex-col relative"
          :class="{'show-password': showPasswordActiveClass}">
      <div class="flex flex-col gap-5px mb-10px">
        <label>邮箱号</label>
        <input type="email"
               placeholder="请输入注册时的邮箱地址"
               v-model="forgetPasswordForm.email"
               @focusin="showPasswordActiveClass = false"
               class="w-full h-full py-6px indent-2px outline-none bg-$theme-bg border-dashed border-1px border-$theme-bg-reverse px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
               autocomplete="off">
      </div>
      <div class="flex flex-col gap-5px relative mb-10px">
        <label>新密码<span class="ml-8px text-12px text-$hover-color2">需要包含字母和数字，最少6位</span></label>
        <input
            class="password-inp w-full h-full py-6px relative z-2 indent-2px outline-none border-none bg-transparent !border-dashed !border-1 border-$theme-bg-reverse py-2px px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
            :type="showPasswordActiveClass ? 'text': 'password'"
            v-model="forgetPasswordForm.newPassword"
            placeholder="请输入新密码"
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
          <input type="text" placeholder="请输入验证码"
                 v-model="forgetPasswordForm.code"
                 @focusin="showPasswordActiveClass = false"
                 class="flex-1 w-full h-full py-6px indent-2px outline-none border-none bg-$theme-bg !border-dashed !border-1 border-$theme-bg-reverse px-4px rounded-6px text-16px justshake focus:(border-$hover-color)">
          <span class="send-code block p-6px shadow relative cursor-not-allowed rounded-6px"
                :class="{'checked !cursor-not-allowed': countDown.countDownIng, 'email-no-empty cursor-pointer': matchEmail(forgetPasswordForm.email)}"
                :data-email="forgetPasswordForm.email"
                @click="sendEmail(forgetPasswordForm.email)">{{
              countDown.countDownIng ? `重新获取 ${countDown.countDownTime}s` : '发送验证码'
            }}</span>
        </div>
      </div>
      <div class="flex justify-between items-center w-full mt-10px">
        <span class="text-$text-color2 text-12px cursor-pointer hover:(text-$hover-color2)" @click="loginHandle">登录</span>
        <button autofocus class="shadow py-6px px-12px rounded-6px text-18px outline-none"
                :class="{'!cursor-not-allowed shadow-inset text-$text-color2': checkForgetPasswordFormComp}"
                :disabled="checkForgetPasswordFormComp"
        >找 回
        </button>
        <span class="text-$text-color2 text-12px cursor-pointer hover:(text-$hover-color2)" @click="registerHandle">注册</span>
      </div>
    </form>
  </Modal>
  <Verify @success="verifyHandle" mode="pop" captchaType="blockPuzzle" ref="verify"/>
</template>

<style scoped>
</style>