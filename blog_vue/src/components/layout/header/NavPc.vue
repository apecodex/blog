<script setup lang='ts'>
import {
  Home,
  BookOpen,
  Game,
  Message,
  PersonalCollection,
  Blossom,
  MessageUnread,
  Me,
  Logout,
  DocumentFolder,
  CategoryManagement,
  TagOne,
  PictureAlbum,
  FriendsCircle,
  Seedling,
  Down,
  Communication
} from '@icon-park/vue-next'
import { scrollDown } from '@/utils/utils'
import { useSettingStore, useUserInfoStore } from "@/store";
import { storeToRefs } from "pinia";

const settingStore = useSettingStore()
const userInfoStore = useUserInfoStore()
const { loginFlag } = storeToRefs(settingStore)
const { userInfo, userNoticeCount } = storeToRefs(userInfoStore)

// 退出登录
const logoutHandle = () => {
  userInfoStore.logout()
}

</script>

<template>
  <nav class="nav-pc">
    <ul>
      <li class="nav-link" @click="scrollDown">
        <router-link to="/">
          <Home size="20" />
          首页
        </router-link>
      </li>
      <li class="nav-link">
        <a href="javascript:void(0)">
          <BookOpen size="20" />
          文章
          <Down size="20" />
        </a>
        <div class="nav-dropdown">
          <ul>
            <span class="arrow"></span>
            <li class="nav-dropdown-link" @click="scrollDown">
              <router-link to="/category">
                <CategoryManagement size="20" />
                <span>分类</span>
              </router-link>
            </li>
            <li class="nav-dropdown-link" @click="scrollDown">
              <router-link to="/tags">
                <TagOne size="20" />
                <span>标签</span>
              </router-link>
            </li>
            <li class="nav-dropdown-link" @click="scrollDown">
              <router-link to="/archives">
                <DocumentFolder size="20" />
                <span>时间轴</span>
              </router-link>
            </li>
          </ul>
        </div>
      </li>
      <li class="nav-link">
        <a href="javascript:void(0)">
          <Game size="20" />
          娱乐
          <Down size="20" />
        </a>
        <div class="nav-dropdown">
          <ul>
            <span class="arrow"></span>
            <li class="nav-dropdown-link" @click="scrollDown">
              <router-link to="/albums">
                <PictureAlbum size="20" />
                <span>相册</span>
              </router-link>
            </li>
            <li class="nav-dropdown-link" @click="scrollDown">
              <router-link to="/talks">
                <FriendsCircle size="20" />
                <span>说说</span>
              </router-link>
            </li>
            <li class="nav-dropdown-link" @click="scrollDown">
              <router-link to="/chat">
                <Communication size="20" />
                <span>聊天室</span>
              </router-link>
            </li>
          </ul>
        </div>
      </li>
      <li class="nav-link" @click="scrollDown">
        <router-link to="/messages">
          <Message size="20" />
          留言
        </router-link>
      </li>
      <li class="nav-link" @click="scrollDown">
        <router-link to="/links">
          <PersonalCollection size="20" />
          友链
        </router-link>
      </li>
      <li class="nav-link" @click="scrollDown">
        <router-link to="/about">
          <Blossom size="20" />
          关于
        </router-link>
      </li>
      <li class="nav-link ml-5px" v-if="userInfo">
        <div class="relative">
          <img class="w-35px h-35px rounded-full avatar" :src="userInfo?.avatar" alt="">
          <span class="user-notice-point" :class="{ 'active': userNoticeCount !== 0 }"></span>
        </div>
        <div class="nav-dropdown">
          <ul class="right-0">
            <span class="arrow arrow-r"></span>
            <li class="nav-dropdown-link" @click="scrollDown">
              <router-link to="/profile">
                <Me size="20" />
                <span>个人中心</span>
              </router-link>
            </li>
            <li class="nav-dropdown-link" @click="scrollDown">
              <router-link to="/profile/notices">
                <MessageUnread size="20" />
                <span v-if="userNoticeCount !== 0"><span class="text-$hover-color2">{{ userNoticeCount >= 100 ? '99+' :
                  userNoticeCount }}</span>条未读</span>
                <span v-else>暂无消息</span>
              </router-link>
            </li>
            <li class="nav-dropdown-link" @click="logoutHandle">
              <a>
                <Logout size="20" />
                <span>退出</span>
              </a>
            </li>
          </ul>
        </div>
      </li>
      <li class="nav-link" @click="loginFlag = true" v-else>
        <a>
          <Seedling size="20" />
          登录/注册
        </a>
      </li>
    </ul>
  </nav>
</template>

<style scoped>
.nav-pc>ul {
  @apply 'flex justify-center items-center gap-5px';
}

.nav-pc ul .nav-link>a {
  @apply 'flex justify-center items-center gap-5px p-5px';
  transition: var(--theme-transition-bg);
  border-radius: 7px 20px 20px 20px;
}

.nav-dropdown ul {
  @apply 'absolute top-50px p-5px opacity-0 invisible duration-300 z-9 bg-$theme-bg-reverse text-$theme-bg2 rounded-6px';
  pointer-events: none;
  border-left: 1px solid var(--theme-bg);
  border-right: 1px solid var(--theme-bg);
  border-bottom: 1px solid var(--theme-bg);
  border-radius: 7px 20px 20px 20px;
}

.nav-dropdown ul:before {
  @apply 'absolute w-full h-20px -top-20px left-0 z-1';
  content: "";
}

.nav-link:hover .nav-dropdown ul {
  @apply 'opacity-100 visible';
  transform: translate(0, 0);
  pointer-events: auto;
}

.nav-dropdown-link a {
  @apply 'flex justify-start items-center p-5px duration-300 gap-12px';
}

.nav-link:hover>a {
  @apply 'bg-$theme-bg-reverse text-$text-color-reverse rounded-4px';
}

.arrow:before {
  @apply 'absolute h-10px w-10px -top-8px left-3/20 z-1';
  content: "";
  border-right: solid 10px transparent;
  border-left: solid 10px transparent;
  border-bottom: solid 10px var(--theme-bg-reverse);
}

.arrow-r:before {
  @apply '!right-3/20';
  left: inherit;
}

.nav-dropdown-link {
  @apply 'rounded-4px duration-100';
  border-radius: 7px 20px 20px 20px;
}

.nav-dropdown-link:hover {
  @apply 'bg-$theme-bg2 text-$text-color';
}

/* 消息小圆点 */
.user-notice-point.active::after {
  @apply 'absolute w-8px h-8px top-2px left-0px bg-orange-600/80 rounded-full';
  content: '';
}

@media screen and (max-width: 960px) {
  .nav-pc {
    display: none;
  }
}
</style>