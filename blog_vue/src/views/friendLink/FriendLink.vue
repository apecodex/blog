<script setup lang='ts'>
import BoxComponent from "@/components/BoxComponent.vue";
import {loadingImg} from "@/constant"
import {onMounted} from "vue";
import {useSettingStore} from "@/store"
import {
  friendLinksData,
  createFriendLinksData
} from "./FriendLinkHooks"
import Skeleton from "@/components/skeleton/Skeleton.vue";
import {storeToRefs} from "pinia";

const settingStore = useSettingStore();
const {friendLinkFlag} = storeToRefs(settingStore)

onMounted(() => {
  createFriendLinksData({size: 100, current: 1})
})
</script>

<template>
  <div class="flex flex-col gap-20px">
    <BoxComponent class="p-10px">
      <template v-if="friendLinksData">
        <div class="text-24px text-shadow-xl text-center">大佬博客
          {{ friendLinksData.count ? '(' + friendLinksData.count + ')' : '' }}
        </div>
        <hr class="hr-twill my-20px">
        <div class="friend-link-wrapper grid gap-15px" v-if="friendLinksData.count !== 0">
          <div class="friend-link-card shadow" v-for="(friendLink, index) of friendLinksData.recordList" :key="index">
            <div class="w-62px h-62px <sm:(w-52px h-52px) shadow p-1px rounded-6px">
              <img v-lazy class="w-full h-full rounded-6px object-cover" :data-src="friendLink.linkAvatar"
                   :src="loadingImg" alt="">
            </div>
            <strong>{{ friendLink.linkName }}</strong>
            <p class="text-body break-all flex-1">{{ friendLink.linkIntro }}</p>
            <div class="user-info flex w-full items-center mt-5px gap-5px">
              <img v-lazy class="w-20px h-20px rounded-full object-cover" :data-src="friendLink.user.avatar"
                   :src="loadingImg" alt="">
              <span class="text-12px">{{ friendLink.user.nickname }}</span>
            </div>
            <a :href="friendLink.linkUrl" target="_blank" class="url-a flex flex-col gap-35px p-5px">
              <span class="text-12px break-all">{{ friendLink.linkUrl }}</span>
              <span class="shadow text-16px py-3px px-6px rounded-full">Visit the website</span>
            </a>
          </div>
        </div>
        <div class="text-center text-20px" v-else>
          暂无友链
        </div>
      </template>
      <div class="friend-link-wrapper grid gap-10px" v-else>
        <Skeleton class="!w-full !h-100px" v-for="i in 10" :key="i"/>
      </div>
    </BoxComponent>
    <div class="p-10px flex flex-col items-center gap-10px">
      <p><span class="text-12px mr-5px">♪٩(´ᵕ｀๑)۶⁾⁾</span>来互链叭~</p>
      <button class="friend-link-btn" @click="friendLinkFlag = true">
        <span class="button-content">
          <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M0 0H24V24H0z" fill="none"></path><path
              d="M12.001 4.529c2.349-2.109 5.979-2.039 8.242.228 2.262 2.268 2.34 5.88.236 8.236l-8.48 8.492-8.478-8.492c-2.104-2.356-2.025-5.974.236-8.236 2.265-2.264 5.888-2.34 8.244-.228z"
              fill="currentColor"></path></svg>申请添加友链
        </span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.friend-link-wrapper {
  grid-template-columns: repeat(auto-fill, minmax(190px, 1fr))
}

.friend-link-card {
  @apply 'text-center bg-$theme-bg pt-10px pb-5px px-5px rounded-6px relative overflow-hidden flex flex-col justify-center items-center gap-5px';
  transition: .3s cubic-bezier(.6, .4, 0, 1), transform .15s ease;
}

.friend-link-card > :not(.url-a) {
  transition: .3s cubic-bezier(.6, .4, 0, 1);
}

.friend-link-card > strong {
  @apply 'block text-16px text-red-400 -tracking-0.035em <sm:(text-14px)';
}

.text-body {
  @apply 'mt-5px leading-[1.1] h-full overflow-hidden overflow-ellipsis text-left text-12px';
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.friend-link-card .url-a {
  @apply 'absolute inset-0 w-full h-full flex justify-center items-center color-$text-color rounded-6px font-blod top-full';
  transition: all .3s cubic-bezier(.6, .4, 0, 1);
}

.friend-link-card:hover .url-a {
  @apply 'top-0 text-20px text-shadow-xl <sm:(text-14px)';
}

.friend-link-card:hover {
  @apply 'bg-$theme-bg';
}

.friend-link-card:hover > div,
.friend-link-card:hover > strong,
.friend-link-card:hover .user-info,
.friend-link-card:hover > p {
  @apply 'opacity-0';
}

/* 申请友链按钮*/
.friend-link-btn {
  @apply 'text-17px font-bold p-4px rounded-20px border-none text-dark-900 outline-none';
  box-shadow: rgba(0, 0, 0, 0.1) 0px 20px 25px -5px, rgba(0, 0, 0, 0.04) 0px 10px 10px -5px;
  background: linear-gradient(0deg, rgba(255, 213, 0, 1) 0%, rgba(255, 213, 0, 1) 47%, rgba(0, 91, 187, 1) 47%, rgba(0, 91, 187, 1) 100%);
  transition: background-color var(--transition);
}

.friend-link-btn .button-content {
  @apply 'flex items-center py-5px px-15px bg-light-50 rounded-16px';
  transition: var(--theme-transition);
}

.friend-link-btn svg {
  @apply 'w-26px h-26px mr-6px text-red-600';
}

.friend-link-btn:hover {
  background: linear-gradient(0deg, rgba(0, 91, 187, 1) 0%, rgba(0, 91, 187, 1) 47%, rgba(255, 213, 0, 1) 47%, rgba(255, 213, 0, 1) 100%);
}

.friend-link-btn:hover .button-content {
  @apply 'bg-dark-900 text-light-50';
}

.friend-link-btn:hover svg {
  animation: shake 0.3s linear infinite both;
}


@media screen and (max-width: 420px) {
  .friend-link-wrapper {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr))
  }
}
</style>