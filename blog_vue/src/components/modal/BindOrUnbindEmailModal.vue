<template>
  <Modal :title="userInfoStore.userInfo?.email ? '解绑邮箱':'绑定邮箱'" v-model:show="bindOrUnbindEmailFlag"
         @closeModal="closeModal">
    <form @submit.prevent="bindEmailHandle">
      <div class="flex flex-col mb-10px">
        <label>邮箱号<span class="text-12px ml-10px text-$hover-color2"
                           v-show="bindOrUnbindEmailForm.email.length !== 0 && !checkEmailFormatComp">邮箱格式有误</span></label>
        <input type="email"
               placeholder="邮箱地址"
               v-model="bindOrUnbindEmailForm.email"
               class="bind-email-modal w-full h-full mt-5px py-6px indent-2px outline-none bg-$theme-bg shadow px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
               autocomplete="off">
      </div>
      <div class="flex flex-col">
        <label>验证码</label>
        <div class="flex">
          <input type="text"
                 placeholder="code"
                 v-model="bindOrUnbindEmailForm.code"
                 class="bind-email-modal flex-1 w-full h-full mt-5px py-6px indent-2px outline-none bg-$theme-bg shadow px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
                 autocomplete="off">
          <div class="flex justify-center items-center">
            <span class="ml-12px shadow py-2px px-5px rounded-6px cursor-pointer"
                  :class="{'checked !cursor-not-allowed': countDown.countDownIng, 'email-no-empty cursor-pointer': !checkEmailFormatComp}"
                  @click="sendEmail(bindOrUnbindEmailForm.email)">{{
                countDown.countDownIng ? `重新获取 ${countDown.countDownTime}s` : '发送验证码'
              }}</span>
          </div>
        </div>
      </div>
      <div class="flex justify-end mt-20px">
        <button class="bind-email-modal shadow py-3px px-8px rounded-6px"
                :class="{'!cursor-not-allowed shadow-inset text-$text-color2': checkBindEmailFormComp}"
                :disabled="checkBindEmailFormComp">确 定
        </button>
      </div>
    </form>
  </Modal>
  <Verify @success="verifyHandle" mode="pop" captchaType="blockPuzzle" ref="verify"/>
</template>

<script setup lang="ts">
import Modal from "@/components/modal/Modal.vue";
import Verify from "@/components/verifition/Verify.vue"
import {useSettingStore, useUserInfoStore, useGlobalStore} from "@/store"
import {storeToRefs} from "pinia";
import {computed, onMounted, reactive, ref, Ref} from "vue";
import {matchEmail} from "@/utils/utils";
import {notify} from "@kyvg/vue3-notification";
import {bindEmail, unbindEmail} from "@/api/requests/User";
import {sendEmail} from "@/utils/sendEmailUtils"

const verify: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 滑动验证码
const globalStore = useGlobalStore();
const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const {bindOrUnbindEmailFlag, countDown, loadingFlag} = storeToRefs(settingStore);
const {userInfo} = storeToRefs(userInfoStore);

const bindOrUnbindEmailForm = reactive({
  captchaVerification: "",
  code: "",
  email: "",
})

onMounted(() => {
  bindOrUnbindEmailForm.email = userInfo.value?.email ? userInfo.value.email : "";
})

// 判断邮箱格式
const checkEmailFormatComp = computed(() => {
  return matchEmail(bindOrUnbindEmailForm.email);
})

// 判断是否都输入了
const checkBindEmailFormComp = computed(() => {
  const email = bindOrUnbindEmailForm.email.trim().length === 0;
  const code = bindOrUnbindEmailForm.code.trim().length === 0;
  return email || code;
})

const closeModal = () => {
  settingStore.closeModal();
  bindOrUnbindEmailForm.email = "";
  bindOrUnbindEmailForm.code = "";
  bindOrUnbindEmailForm.captchaVerification = "";
}


const bindEmailHandle = () => {
  if (!checkEmailFormatComp.value) {
    notify({
      text: "邮箱格式有误",
      type: "warn"
    })
    return;
  }
  if (userInfo.value?.email !== null && bindOrUnbindEmailForm.email !== userInfo.value?.email) {
    notify({
      text: "请输入当前账号的邮箱",
      type: "warn"
    })
    return;
  }
  if (checkBindEmailFormComp) {
    (<any>verify.value).show();
  }
}

const verifyHandle = async (verify: { captchaVerification: string }) => {
  bindOrUnbindEmailForm.captchaVerification = verify.captchaVerification;
  notify({
    text: "正在保存...",
  });
  loadingFlag.value = true;
  if (userInfoStore.userInfo?.email) {
    // 解绑
    await unbindEmail(bindOrUnbindEmailForm).then((resp: ResultObject<string>) => {
      if (resp.status) {
        notify({
          text: resp.message,
          type: "success"
        })
        userInfoStore.updateToken(resp.data);
        userInfoStore.updateEmail(null);
      } else {
        notify({
          text: resp.message,
          type: "warn"
        })
      }
    }).catch(() => {
      notify({
        text: "解绑失败，请重试",
        type: "warn"
      })
    }).finally(() => {
      loadingFlag.value = false;
      closeModal();
    })
  } else {
    // 绑定
    await bindEmail(bindOrUnbindEmailForm).then((resp: ResultObject<string>) => {
      if (resp.status) {
        notify({
          text: resp.message,
          type: "success"
        })
        userInfoStore.updateToken(resp.data);
        userInfoStore.updateEmail(bindOrUnbindEmailForm.email);
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
      loadingFlag.value = false;
      closeModal();
    })
  }
  // 重新刷新组件
  await globalStore.reload();
}
</script>

<style scoped>
.bind-email-modal:focus {
  box-shadow: var(--theme-shadow-inset) !important;
}
</style>