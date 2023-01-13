<script setup lang='ts'>
import {friendLinkForm, checkFriendLinkForm} from "@/components/modal/FriendLinkHooks"
import {createUserFriendLink, userFriendLinkData, updateFriendLinkHandle, openFriendLinkModal} from "./FriendLinkHooks"
import {onMounted} from "vue";
import {getDate} from "@/utils/utils"
import Skeleton from "@/components/skeleton/Skeleton.vue";

onMounted(() => {
  createUserFriendLink();
})
</script>

<template>
  <div v-if="userFriendLinkData">
    <div v-if="userFriendLinkData.length !== 0">
      <div class="flex flex-col gap-5px mb-10px">
        <div class="flex gap-15px">
          <span class="text-$hover-color2">审核状态</span>
          <span class="text-green-600" v-if="!userFriendLinkData[0].isReview">审核通过</span>
          <span class="text-orange-600" v-else>正在审核</span>
        </div>
        <div class="flex gap-15px items-center">
          <span class="text-$hover-color2">申请时间</span>
          <time class="text-$text-color2 text-12px" style="transition: var(--theme-transition-color)">
            {{ getDate(userFriendLinkData[0].createTime) }}
          </time>
        </div>
      </div>
      <form @submit.prevent="updateFriendLinkHandle" class="flex flex-col gap-10px">
        <div class="flex flex-col gap-10px">
          <span class="text-$hover-color2">网站名称</span>
          <input type="text"
                 v-model="friendLinkForm.linkName"
                 class="input-focus border-none bg-transparent rounded-6px py-3px px-8px outline-none shadow w-260px <sm:(w-full)">
        </div>
        <div class="flex flex-col gap-10px">
          <span class="text-$hover-color2">网站地址</span>
          <input type="text"
                 v-model="friendLinkForm.linkUrl"
                 class="input-focus border-none bg-transparent rounded-6px py-3px px-8px outline-none shadow w-260px <sm:(w-full)">
        </div>
        <div class="flex flex-col gap-10px">
          <span class="text-$hover-color2">网站头像</span>
          <img class="w-60px h-60px object-cover rounded-6px mb-5px" :src="userFriendLinkData[0].linkAvatar" alt="">
          <input type="text"
                 v-model="friendLinkForm.linkAvatar"
                 class="input-focus border-none bg-transparent rounded-6px py-3px px-8px outline-none shadow">
        </div>
        <div class="flex flex-col gap-10px">
          <span class="text-$hover-color2">网站简介</span>
          <input type="text"
                 v-model="friendLinkForm.linkIntro"
                 class="input-focus border-none bg-transparent rounded-6px py-3px px-8px outline-none shadow">
        </div>
        <div class="flex justify-center mt-20px">
          <button class="shadow-hover shadow py-3px px-10px rounded-6px outline-none"
                  :class="{'!cursor-not-allowed': checkFriendLinkForm}" :disabled="checkFriendLinkForm">保 存
          </button>
        </div>
      </form>
    </div>
    <!-- 未申请友链 -->
    <p v-else>你还没有申请友链呢，<span class="text-$hover-color2 cursor-pointer"
                                       @click="openFriendLinkModal">点击申请</span></p>
  </div>
  <div v-else class="w-full h-full">
    <Skeleton customize-class="!w-full !h-full"/>
  </div>
</template>

<style scoped>
.input-focus:focus {
  box-shadow: var(--theme-shadow-inset) !important;
}

</style>