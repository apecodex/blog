<script setup lang='ts'>
import {
  userInfoData,
  userInfoForm,
  createUserInfoData,
  loginType,
  saveIntroHandle,
  updateUserInfoHandle,
  openBindEmailModal,
} from "./UserInfoHooks"
import {getDate} from "@/utils/utils"
import {qqLogin, showLoginIcon} from "@/utils/socialLoginUtils"
import {Unlock, EmailLock} from "@icon-park/vue-next"
import {onMounted} from "vue";
import Comment from "@/components/comment/Comment.vue"
import BoxComponent from "@/components/BoxComponent.vue"
import {unbindQQ} from "@/api/requests/User";
import {notify} from "@kyvg/vue3-notification";
import {storeToRefs} from "pinia";
import {useUserInfoStore, useSettingStore, useWebsiteInfoStore, useGlobalStore} from "@/store";

const globalStore = useGlobalStore();
const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const websiteInfoStore = useWebsiteInfoStore();
const {userInfo, isBindQQ} = storeToRefs(userInfoStore);
const {loadingFlag} = storeToRefs(settingStore);
const {websiteInfo} = storeToRefs(websiteInfoStore);


onMounted(() => {
  createUserInfoData();
})

const bindOrUnbindQQHandle = async () => {
  isBindQQ.value = userInfoData.value!.bindQQ;
  if (isBindQQ.value) {
    // 解绑
    loadingFlag.value = true;
    await unbindQQ().then((resp: ResultObject<null>) => {
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
        text: "解绑失败，请重试",
        type: "warn"
      })
    }).finally(() => {
      loadingFlag.value = false;
      globalStore.reload();
    })
  } else {
    // 绑定
    qqLogin();
  }
}

</script>

<template>
  <div v-if="userInfoData">
    <div class="flex flex-col w-full h-full gap-20px">
      <!-- uid -->
      <div class="flex items-baseline">
        <span class="w-56px text-right text-$hover-color2 mr-15px">uid</span>
        <div class="flex-1">
          <span class="shadow-inset text-$hover-color rounded-6px py-3px px-10px"
                style="transition: var(--theme-transition-shadow)">{{ userInfoData.uid }}</span>
        </div>
      </div>
      <!-- 注册时间 -->
      <div class="flex items-baseline">
        <span class="w-56px text-right text-$hover-color2 mr-15px">注册时间</span>
        <div class="flex-1 text-$text-color2 text-12px" style="transition: var(--theme-transition-color)">
          {{ getDate(userInfoData.createTime) }}
        </div>
      </div>
      <!-- 登录方式 -->
      <div class="flex items-baseline">
        <span class="w-56px text-right text-$hover-color2 mr-15px">登录方式</span>
        <div class="flex-1">{{ loginType[userInfoData.loginType] }}</div>
      </div>
      <!-- 用户名 -->
      <div class="flex items-baseline">
        <span class="w-56px text-right text-$hover-color2 mr-15px">用户名</span>
        <div class="flex-1">
          <input type="text" v-model="userInfoForm.nickname" @change="updateUserInfoHandle"
                 class="user-nickname-input w-200px h-full text-14px indent-2px outline-none bg-$theme-bg border-none shadow py-3px px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
                 style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)">
        </div>
      </div>
      <!-- 邮件地址 -->
      <div class="flex items-start">
        <span class="w-56px text-right text-$hover-color2 mr-15px">邮箱地址</span>
        <div class="flex-1">
          <div v-if="userInfoData.email" class="flex">
            <span>{{ userInfoData.email }}</span>
            <button class="text-12px outline-none rounded-6px flex items-center ml-10px shadow py-1px px-8px"
                    @click="openBindEmailModal">
              <Unlock class="mr-5px"/>
              解绑
            </button>
          </div>
          <div v-else>
            <button class="shadow py-1px px-8px rounded-6px outline-none flex items-center" @click="openBindEmailModal">
              <EmailLock class="mr-5px"/>
              绑定邮箱
            </button>
          </div>
        </div>
      </div>
      <!-- 简介 -->
      <div class="flex">
        <span class="w-56px text-right text-$hover-color2 mr-15px">简介</span>
        <div class="flex-1">
          <BoxComponent class="p-10px">
            <Comment :default-value="userInfoForm.intro" @saveComment="saveIntroHandle" send-btn-text="保存"/>
          </BoxComponent>
        </div>
      </div>
      <!-- 个人网站 -->
      <div class="flex items-baseline">
        <span class="w-56px text-right text-$hover-color2 mr-15px">个人网站</span>
        <div class="flex-1">
          <input type="text" v-model="userInfoForm.webSite" placeholder="个人网站" @change="updateUserInfoHandle"
                 class="user-nickname-input w-200px h-full text-14px indent-2px outline-none bg-$theme-bg border-none shadow py-3px px-4px rounded-6px text-16px justshake focus:(border-$hover-color)"
                 style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)">
        </div>
      </div>
      <!-- 是否开启邮件通知 -->
      <div class="flex">
        <span class="w-56px text-right text-$hover-color2 mr-15px">邮件通知</span>
        <div class="flex-1 flex">
          <label class="label">
            <div class="toggle">
              <input class="toggle-state" type="checkbox" name="check" @change="updateUserInfoHandle"
                     v-model="userInfoForm.isEmailNotice">
              <div class="indicator"></div>
            </div>
          </label>
        </div>
      </div>
    <!-- 社交登录 -->
      <div class="flex" v-if="Object.keys(websiteInfo?.socialLogin).length !== 0">
        <span class="w-56px text-right text-$hover-color2 mr-15px">社交登录</span>
        <div class="flex text-12px">
          <div class="flex items-center shadow py-2px px-8px rounded-6px cursor-pointer" v-if="showLoginIcon('QQ')"
               @click="bindOrUnbindQQHandle">
            <div class="flex items-center text-12px">
              <img src="@/assets/imgs/qq_login.png" alt="" class="mr-3px">
              QQ
            </div>
            <span class="ml-10px">{{ userInfoData.bindQQ ? "解绑" : "绑定" }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.user-nickname-input:focus {
  box-shadow: var(--theme-shadow-inset) !important;
}

.label {
  @apply 'inline-flex items-center cursor-pointer';
}

.toggle {
  @apply 'relative h-20px w-40px rounded-15px overflow-hidden isolate';
  box-shadow: var(--theme-shadow-inset);
  transition: var(--theme-transition-shadow);
}

.toggle-state {
  @apply 'hidden';
}

.indicator {
  @apply 'w-[200%] h-full rounded-15px bg-$hover-color2';
  transform: translate3d(-75%, 0, 0);
  transition: transform 0.4s cubic-bezier(0.85, 0.05, 0.18, 1.35);
  box-shadow: var(--theme-shadow);
}

.toggle-state:checked ~ .indicator {
  @apply 'bg-lime-500';
  transform: translate3d(25%, 0, 0);
}

@media screen and (max-width: 420px) {
  .user-nickname-input {
    @apply 'w-full';
  }
}
</style>