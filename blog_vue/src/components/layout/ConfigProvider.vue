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
import RightMenu from "@/components/rightMenu/RightMenu.vue"
import {onMounted} from "vue";
import {useSettingStore} from "@/store";
import {storeToRefs} from "pinia";

import {rightMenu, selectText} from "@/components/rightMenu/rightMenuHooks"
import {isMobile} from "@/utils/utils";

const settingStore = useSettingStore();
const {countDown} = storeToRefs(settingStore);


// 右键菜单
const openMenu = (event: PointerEvent) => {
  if (!isMobile()) {
    event.preventDefault();
    let href = event.target!.href;
    let imgSrc = event.target!.currentSrc;
    // 复制文本
    if (window.getSelection()!.isCollapsed) {
      rightMenu.value.pluginMode = false;
      rightMenu.value.copyText = "";
    } else {
      rightMenu.value.pluginMode = true;
    }
    // 复制链接
    if (href && window.getSelection()!.anchorOffset !== 0) {
      rightMenu.value.href = href;
      rightMenu.value.pluginMode = true;
    } else {
      rightMenu.value.href = "";
    }
    // 复制图片链接
    if (imgSrc) {
      rightMenu.value.imgSrc = imgSrc;
      rightMenu.value.pluginMode = true;
    } else {
      rightMenu.value.imgSrc = "";
    }
    // 鼠标点击的坐标
    let oX = event.clientX;
    let oY = event.clientY;
    let winWidth = window.innerWidth;
    let winHeight = window.innerHeight;
    let cmWidth = rightMenu.value.menuWidth;
    let cmHeight = rightMenu.value.menuHeight;
    oX = oX > winWidth - cmWidth ? winWidth - cmWidth - 40 : oX;
    oY = oY > winHeight - cmHeight ? winHeight - cmHeight : oY;
    rightMenu.value.top = oY;
    rightMenu.value.left = oX;
    rightMenu.value.flag = true;
    return false;
  }

}

onMounted(() => {
  // 获取选中的文本
  document.onmouseup = document.ondblclick = selectText;
  // 加载倒计时
  if (countDown.value.startTime) {
    countDown.value.countDownIng = true;
    settingStore.countDownHandle();
  }
})
</script>

<template>
  <div @contextmenu="openMenu">
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
    <!-- 右键菜单 -->
    <right-menu/>
  </div>
</template>

<style scoped>
</style>