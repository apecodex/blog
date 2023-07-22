<script setup lang='ts'>
import {
  Book,
  Bookmark,
  Home,
  Message,
  PersonalCollection,
  Blossom,
  Me,
  Logout,
  DocumentFolder,
  CategoryManagement,
  TagOne,
  PictureAlbum,
  FriendsCircle,
  Seedling,
  Close,
  Communication
} from '@icon-park/vue-next'
import { scrollDown } from '@/utils/utils'
import { computed, ref } from "vue";
import Mask from "@/components/Mask.vue";
import { useSettingStore, useUserInfoStore, useWebsiteInfoStore } from "@/store";
import { storeToRefs } from "pinia";

const websiteInfoStore = useWebsiteInfoStore()
const settingStore = useSettingStore()
const userInfoStore = useUserInfoStore()
const { websiteInfo } = storeToRefs(websiteInfoStore)
const { loginFlag } = storeToRefs(settingStore)
const { userInfo } = storeToRefs(userInfoStore)

const hamburgerClassActive = ref(false)

const openMenuComp = computed({
  get: () => hamburgerClassActive.value,
  set: (v: boolean) => {
    if (v) {
      document.body.style.overflow = 'hidden'
    } else {
      document.body.style.overflow = 'visible'
    }
    hamburgerClassActive.value = v
  }
})


const clickMash = () => {
  hamburgerClassActive.value = false
  document.body.style.overflow = 'visible'
}

const scrollHandle = () => {
  openMenuComp.value = false
  scrollDown()
}

// 打开登录框
const showLoginModalHandle = () => {
  openMenuComp.value = false
  loginFlag.value = true
}

// 退出登录
const logoutHandle = () => {
  openMenuComp.value = false
  userInfoStore.logout()
}

</script>

<template>
  <nav class="nav-small">
    <div class="hamburger" :class="{ 'is-active': openMenuComp }" @click="openMenuComp = !hamburgerClassActive">
      <span></span>
      <span></span>
      <span></span>
    </div>
    <Mask v-model:show="openMenuComp" @click="clickMash" />
    <div class="nav-side shadow" :class="{ 'active': openMenuComp }">
      <div
        class="small-menu-close absolute right-5px top-8px z-11 cursor-pointer w-35px h-35px bg-$theme-bg rounded-full">
        <Close size="24" class="w-full h-full flex justify-center items-center font-black"
          @click="openMenuComp = false" />
      </div>
      <div class="nav-content flex flex-col justify-center items-center">
        <div class="website-avatar shadow mb-15px">
          <div class="website-avatar-inner shadow-inset">
            <div class="avatar-img">
              <img class="rounded-full avatar" :src="websiteInfo?.websiteAvatar" alt="">
            </div>
          </div>
        </div>
        <div class="w-full flex justify-evenly mb-15px">
          <span class="flex flex-col justify-center items-center rounded-6px p-3px shadow">
            <span class="flex items-center">
              <Book class="mr-3px" />文章
            </span>
            <span>{{ websiteInfo?.articleCount }}</span>
          </span>
          <span class="flex flex-col justify-center items-center rounded-6px p-3px shadow">
            <span class="flex items-center">
              <CategoryManagement class="mr-3px" />分类
            </span>
            <span>{{ websiteInfo?.categoryCount }}</span>
          </span>
          <span class="flex flex-col justify-center items-center rounded-6px p-3px shadow">
            <span class="flex items-center">
              <Bookmark class="mr-3px" />标签
            </span>
            <span>{{ websiteInfo?.tagCount }}</span>
          </span>
        </div>
        <ul class="small-menu mt-10px">
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/">
              <Home size="20" />
              首页
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/category">
              <CategoryManagement size="20" />
              <span>分类</span>
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/tags">
              <TagOne size="20" />
              <span>标签</span>
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/archives">
              <DocumentFolder size="20" />
              <span>时间轴</span>
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/albums">
              <PictureAlbum size="20" />
              <span>相册</span>
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/talks">
              <FriendsCircle size="20" />
              <span>说说</span>
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/chat">
              <Communication size="20" />
              <span>聊天室</span>
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/messages">
              <Message size="20" />
              留言
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/links">
              <PersonalCollection size="20" />
              友链
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle">
            <router-link to="/about">
              <Blossom size="20" />
              关于
            </router-link>
          </li>
          <li class="small-nav-link" @click="scrollHandle" v-if="userInfo">
            <router-link to="/profile">
              <Me size="20" />
              个人中心
            </router-link>
          </li>
          <li class="small-nav-link" @click="showLoginModalHandle" v-else>
            <a>
              <Seedling size="20" />
              登录/注册
            </a>
          </li>
          <li class="small-nav-link" @click="logoutHandle" v-if="userInfo">
            <a>
              <Logout size="20" />
              退出
            </a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<style scoped>
/* 小屏导航 */
.nav-small {
  @apply 'hidden';
}

.hamburger {
  @apply 'flex flex-col relative z-1 select-none border-none outline-none bg-none';
}

.hamburger span {
  @apply 'block w-33px h-4px relative bg-$theme-bg-reverse rounded-6px z-1';
  transform-origin: 0 0;
  transition: .4s;
}

.hamburger span:nth-child(2) {
  @apply 'my-5px';
}

.hamburger:hover span:nth-child(2) {
  transform: translateX(10px);
  background-color: #8c38ff;
}

.hamburger.is-active span:nth-child(1) {
  transform: translate(0, -2px) rotate(45deg);
}

.hamburger.is-active span:nth-child(2) {
  opacity: 0;
  transform: translateX(15px);
}

.hamburger.is-active span:nth-child(3) {
  transform: translate(-3px, 3px) rotate(-45deg);
}

.hamburger.is-active:hover span {
  background-color: var(--theme-bg);
}

.nav-side {
  @apply 'fixed top-0 right-0 bottom-0 w-230px h-screen p-12px bg-$theme-bg z-999 overflow-y-scroll';
  transform: translateX(105%);
  transition: 0.4s ease-in-out;
}

.nav-side.active {
  transition: 0.3s ease-in-out 0.3s;
  transform: translateX(0);
}

.small-menu-close {
  box-shadow: var(--theme-shadow);
}

.small-menu-close:hover {
  box-shadow: var(--theme-shadow-inset);
  color: var(--theme-bg);
}

/*头像*/
.website-avatar {
  @apply 'w-170px h-170px mt-10px flex rounded-full justify-center items-center ';
  transition: width var(--transition);
}

.website-avatar-inner {
  @apply 'w-160px h-160px rounded-full';
  transition: width var(--transition);
}

.avatar-img {
  @apply 'flex justify-center items-center w-full h-full rounded-full p-8px';
  transition: width var(--transition);
}

.avatar-img img {
  @apply 'w-full h-full rounded-full bg-cover bg-center';
}

/*菜单*/
.small-menu {
  @apply 'flex flex-col justify-center items-center w-full h-full px-20px';
}

.small-menu .small-nav-link {
  @apply 'flex justify-center w-full h-36px mb-15px';
}

.small-menu .small-nav-link:last-child {
  @apply 'mb-0';
}

.small-menu .small-nav-link a {
  @apply 'flex justify-center items-center w-full h-full text-16px gap-12px';
  border-radius: 7px 20px 20px 20px;
  box-shadow: var(--theme-shadow);
  transition: var(--theme-transition-shadow);
}

.small-menu .small-nav-link a:hover {
  box-shadow: var(--theme-shadow-inset);
}

@media screen and (max-width: 960px) {
  .nav-small {
    @apply 'inline';
  }

}

@media screen and (max-width: 420px) {
  .nav-side {
    @apply 'w-full';
  }

  .website-avatar {
    @apply 'w-240px h-240px';
  }

  .website-avatar-inner {
    @apply 'w-230px h-230px';
  }

  .small-menu .small-nav-link a {
    @apply 'justify-center text-20px w-8/10';
    transition: none;
  }
}
</style>