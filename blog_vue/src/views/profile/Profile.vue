<script setup lang='ts'>
import BoxComponent from "@/components/BoxComponent.vue";
import { Edit, Me, Message, LinkOne, PeopleSafe } from "@icon-park/vue-next"
import { updateUserAvatarHandle } from "./ProfileHooks"
import { useUserInfoStore, useSettingStore } from "@/store"
import { storeToRefs } from "pinia";
import Skeleton from "@/components/skeleton/Skeleton.vue";
import { useRoute } from "vue-router"

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const { loadingFlag, loginFlag } = storeToRefs(settingStore)
const { userInfo, userNoticeCount } = storeToRefs(userInfoStore);

const route = useRoute();

</script>

<template>
  <BoxComponent class="p-15px">
    <div class="profile-wrapper flex gap-20px <md:(flex-col)" v-if="userInfo">
      <div class="profile-nav flex flex-col gap-20px">
        <BoxComponent class="p-10px <md:(!shadow-none p-0)">
          <div class="profile-avatar w-100px h-100px rounded-6px overflow-hidden relative m-auto <md:(w-80px h-80px)">
            <label>
              <input type="file" name="file" class="hidden" accept="image/png,image/gif,image/jpeg,image/jpg"
                @change="updateUserAvatarHandle">
              <img class="rounded-6px shake w-full h-full" :src="userInfo.avatar" alt="">
              <span class="absolute bottom-0 left-0 bg-dark-900/80 p-3px opacity-0 invisible"
                style="border-radius: 0 8px 0 0">
                <Edit size="15" fill="#fff" />
              </span>
            </label>
          </div>
        </BoxComponent>
        <BoxComponent class="p-10px">
          <ul class="flex flex-col items-center justify-center gap-15px <md:(flex-row gap-15px) <sm:(justify-between)">
            <li class="rounded-6px">
              <router-link to="/profile"
                class="flex justify-center items-center text-center gap-5px rounded-6px bg-$theme-bg py-6px px-10px <md:(flex-col text-14px) <sm:(py-10px px-6px text-12px)"
                :class="{ 'active': route.name === 'ProfileInfoNav' }">
                <Me class="<md:(text-20px)" />
                个人资料
              </router-link>
            </li>
            <li class="notice-li rounded-6px relative">
              <router-link to="/profile/notices"
                class="flex justify-center items-center text-center gap-5px rounded-6px bg-$theme-bg py-6px px-10px <md:(flex-col text-14px) <sm:(py-10px px-6px text-12px)"
                :class="{ 'active': route.name === 'ProfileNoticeNav' }">
                <Message class="<md:(text-20px)" />
                消息通知
              </router-link>
              <span class="notice-span" v-show="userNoticeCount !== 0">{{ userNoticeCount }}</span>
            </li>
            <li class="rounded-6px">
              <router-link to="/profile/friendLink"
                class="flex justify-center items-center text-center gap-5px rounded-6px bg-$theme-bg py-6px px-10px <md:(flex-col text-14px) <sm:(py-10px px-6px text-12px)"
                :class="{ 'active': route.name === 'ProfileFriendLinkNav' }">
                <LinkOne class="<md:(text-20px)" />
                我的友链
              </router-link>
            </li>
            <li class="rounded-6px">
              <router-link to="/profile/password"
                class="flex justify-center items-center text-center gap-5px bg-$theme-bg rounded-6px py-6px px-10px <md:(flex-col text-14px) <sm:(py-10px px-6px text-12px)"
                :class="{ 'active': route.name === 'ProfilePasswordNav' }">
                <PeopleSafe class="<md:(text-20px)" />
                修改密码
              </router-link>
            </li>
          </ul>
        </BoxComponent>
      </div>
      <BoxComponent class="profile-content p-10px">
        <suspense>
          <router-view v-slot="{ Component }">
            <transition name="fade-transform">
              <component :is="Component" />
            </transition>
          </router-view>
        </suspense>
      </BoxComponent>
    </div>
    <div v-else class="flex gap-20px <md:(flex-col)">
      <div class="profile-nav flex flex-col gap-20px items-center justify-center">
        <Skeleton class="!w-80px !h-80px !rounded-full" />
        <div class="w-full flex flex-col gap-20px">
          <Skeleton class="!w-full !h-200px <md:(!h-50px)" />
        </div>
      </div>
      <div class="profile-content">
        <Skeleton class="!w-full !min-h-200px !h-full" />
      </div>
    </div>
  </BoxComponent>
</template>

<style scoped>
.fade-transform-enter-active {
  animation: right-effect .6s ease-in-out;
}

.profile-nav {
  @apply 'sticky top-10px left-0 w-full h-full';
}

.profile-avatar span {
  transition: all .3s ease;
}

.profile-avatar:hover span {
  @apply 'opacity-100 visible'
}

.profile-wrapper ul li,
.profile-wrapper ul li a {
  transition: var(--theme-transition-bg), var(--theme-transition-shadow);
}

.profile-wrapper ul li:hover {
  box-shadow: var(--theme-shadow);
}

.active {
  box-shadow: var(--theme-shadow-inset) !important;
}

.profile-content {
  flex: 0 0 75%;
}

/* 未读通知数量 */
.notice-li .notice-span {
  @apply 'absolute -top-8px left-88px p-4px rounded-full text-12px text-red-600 bg-$theme-bg font-bold';
  content: attr(data-notice-count);
  box-shadow: var(--theme-shadow);
  transition: var(--theme-transition-shadow), var(--theme-transition-bg);
}

@media screen and (max-width: 768px) {
  .profile-nav {
    @apply 'static top-0';
  }

  .profile-content {
    @apply 'w-full flex-1';
  }
}
</style>