<script setup lang='ts'>
import { getDate } from "@/utils/utils"
import { useUserInfoStore } from "@/store"
import {
  pagination,
  noticesData,
  createNoticesData,
  updateNoticesDataHandle,
  changeIsRead,
  updateNoticeReadHandle,
  createUserNoReadCountHandle,
  clearNoReadHandle
} from "./NoticeHooks"
import { onMounted } from "vue";
import { storeToRefs } from "pinia";

const userInfoStore = useUserInfoStore();
const { userNoticeCount } = storeToRefs(userInfoStore);

onMounted(() => {
  createNoticesData(false, { current: pagination.current, size: pagination.size });
  createUserNoReadCountHandle();
})
</script>

<template>
  <div>
    <div class="flex justify-center gap-60px <sm:(justify-between)">
      <button class="is-read-btn py-4px px-10px shadow rounded-6px relative outline-none"
        :class="{ 'active': !pagination.isRead }" @click="changeIsRead(false)">
        未 读
        <span
          class="absolute -top-5px left-45px p-3px rounded-full shadow bg-$theme-bg text-12px font-bold text-red-600"
          style="transition: var(--theme-transition-shadow), var(--theme-transition-bg)" v-if="userNoticeCount !== 0">{{
            userNoticeCount !== 0 ? userNoticeCount : ''
          }}</span>
      </button>
      <button class="is-read-btn py-4px px-10px shadow rounded-6px outline-none" v-if="userNoticeCount !== 0"
        @click="clearNoReadHandle">清除未读</button>
      <button class="is-read-btn py-4px px-10px shadow rounded-6px outline-none" :class="{ 'active': pagination.isRead }"
        @click="changeIsRead(true)">已 读
      </button>
    </div>
    <hr class="hr-twill py-1px my-15px">
    <div v-if="noticesData" class="flex flex-col gap-20px">
      <div class="notice-item p-10px rounded-6px flex flex-col gap-3px" v-for="notice of noticesData.recordList"
        :key="notice.id">
        <h3 class="text-17px">{{ notice.title }}</h3>
        <p>{{ notice.content }}</p>
        <div class="flex justify-between items-center">
          <time class="text-12px text-$text-color2" style="transition: var(--theme-transition-color)">
            {{ getDate(notice.createTime) }}
          </time>
          <div class="flex gap-25px">
            <button class="notice-btn shadow px-8px rounded-6px outline-none" v-if="notice.url" :title="notice.url">
              <a :href="notice.url" target="_blank">查看</a>
            </button>
            <button class="notice-btn text-$hover-color2 shadow px-8px rounded-6px outline-none"
              @click="updateNoticeReadHandle(new Array(notice.id))" v-if="!notice.status">朕已阅
            </button>
          </div>
        </div>
      </div>
      <div v-if="noticesData.count === 0" class="flex justify-center items-center">
        暂无消息
      </div>
    </div>
    <div class="flex justify-center mt-20px" v-if="noticesData?.recordList.length !== noticesData?.count">
      <button class="notice-btn shadow py-3px px-8px rounded-6px outline-none" @click="updateNoticesDataHandle">
        查看剩下的{{ noticesData?.count - noticesData?.recordList.length }}条
      </button>
    </div>
  </div>
</template>

<style scoped>
.is-read-btn.active {
  box-shadow: var(--theme-shadow-inset) !important;
}

.notice-item {
  transition: var(--theme-transition-shadow);
}

.notice-item:nth-child(odd) {
  box-shadow: var(--theme-shadow-inset);
}

.notice-item:nth-child(even) {
  box-shadow: var(--theme-shadow);
}

.notice-btn:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}
</style>