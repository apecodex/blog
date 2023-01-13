<script setup lang='ts'>
import {
  archivesData,
  createArchives
} from "./ArchiveHooks"
import {onMounted} from "vue";
import BoxComponent from "@/components/BoxComponent.vue";
import Skeleton from "@/components/skeleton/Skeleton.vue";
import {storeToRefs} from "pinia";
import {Time} from "@icon-park/vue-next"

import {useWebsiteInfoStore} from "@/store"

const websiteInfoStore = useWebsiteInfoStore();
const {websiteInfo} = storeToRefs(websiteInfoStore);

onMounted(() => {
  createArchives();
})
</script>

<template>
  <BoxComponent class="p-10px">
    <template v-if="archivesData">
      <span class="text-18px"><span class="text-16px <md:(text-12px) mr-3px">(•̀ᴗ•́)و ̑̑</span>共发布了{{
          websiteInfo?.articleCount
        }}篇文章~</span>
      <ul class="flex flex-col gap-5px mt-15px">
        <li class="flex gap-15px" v-for="(archive, index) of archivesData" :key="index">
          <span class="line flex flex-col items-center mt-3px"></span>
          <div class="flex flex-col gap-5px w-full">
            <div class="flex items-center gap-5px">
              <time class="text-18px text-$hover-color3">{{ archive.month.replace("-", "年") + "月" }}</time>
              <span class="text-12px">{{ archive.articles.length }}篇</span>
            </div>
            <div class="flex flex-col gap-2px" v-for="article of archive.articles">
              <div>
                <router-link :to="`/article/${article.id}`" class="text-14px hover:(text-$hover-color2)">
                  {{ article.articleTitle }}
                </router-link>
              </div>
              <time class="text-12px text-$text-color2 flex items-center gap-5px"
                    style="transition: var(--theme-transition-color)">
                <Time/>
                <span>{{ article.createTime.substring(0, article.createTime.lastIndexOf(":")) }}</span>
              </time>
            </div>
          </div>
        </li>
      </ul>
    </template>
    <template v-else>
      <div class="flex flex-col gap-20px">
        <Skeleton class="!h-20px"/>
        <Skeleton class="!w-230px !h-25px"/>
        <Skeleton class="!w-120px !h-15px"/>
        <Skeleton class="!w-210px !h-25px"/>
        <Skeleton class="!w-120px !h-15px"/>
        <Skeleton class="!w-260px !h-25px"/>
        <Skeleton class="!w-120px !h-15px"/>
        <Skeleton class="!w-190px !h-25px"/>
        <Skeleton class="!w-120px !h-15px"/>
      </div>
    </template>
  </BoxComponent>
</template>

<style scoped>
.line::before {
  @apply 'w-13px h-13px rounded-full bg-$theme-bg-reverse';
  content: "";
  transition: var(--theme-transition-bg);
}

.line::after {
  @apply 'mt-5px flex-grow';
  border-left: 2px solid var(--theme-bg-reverse);
  content: "";
  transition: border var(--transition);
}
</style>