<script setup lang='ts'>
import "@/assets/css/dark.css"
import "@/assets/css/light.css"
import LoginModal from "@/components/modal/LoginModal.vue"
import RegisterModal from "@/components/modal/RegisterModal.vue"
import SearchModal from "@/components/modal/SearchModal.vue"
import Loading from "@/components/loading/Loading.vue"
import FriendLinkModal from "@/components/modal/FriendLinkModal.vue";
import ArticleThemeModal from "@/components/modal/ArticleThemeModal.vue";
import ForgetPasswordModal from "@/components/modal/ForgetPasswordModal.vue";
import BindOrUnbindEmailModal from "@/components/modal/BindOrUnbindEmailModal.vue";
import {onMounted} from "vue";
import {useSettingStore} from "@/store";
import {storeToRefs} from "pinia";

const settingStore = useSettingStore();
const {countDown} = storeToRefs(settingStore);

onMounted(() => {
  // 加载倒计时
  if (countDown.value.startTime) {
    countDown.value.countDownIng = true;
    settingStore.countDownHandle();
  }
})
</script>

<template>
  <div class="bg-$theme-bg text-$text-color" style="transition: var(--theme-transition)">
    <slot/>
  </div>
  <!-- 登录模态框 -->
  <LoginModal/>
  <!-- 注册模态框 -->
  <RegisterModal/>
  <!-- 搜索模态框 -->
  <SearchModal/>
  <!-- 加载 -->
  <Loading/>
  <!-- 添加友链 -->
  <FriendLinkModal/>
  <!-- 修改文章主题 -->
  <ArticleThemeModal/>
  <!-- 忘记密码 -->
  <ForgetPasswordModal/>
  <!-- 绑定或解绑邮箱 -->
  <BindOrUnbindEmailModal/>
</template>

<style scoped>
</style>