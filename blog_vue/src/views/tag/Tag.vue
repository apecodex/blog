<script setup lang='ts'>
import type {Ref} from "vue"
import {getTags} from "@/api/requests/Tag"
import {random, scrollDown} from "@/utils/utils"
import LRLayout from "@/layout/LRLayout.vue"
import BoxComponent from "@/components/BoxComponent.vue"
import NewsArticle from "@/views/comm/NewsArticle.vue";
import NewsComment from "@/views/comm/NewsComment.vue";
import {onMounted, ref} from "vue";
import {notify} from "@kyvg/vue3-notification"
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import Skeleton from "@/components/skeleton/Skeleton.vue";

const settingStore = useSettingStore()
const {loadingFlag} = storeToRefs(settingStore)

const tagsData: Ref<TagFrontEntity | null> = ref(null)

const createTags = async (condition: ConditionParams) => {
  loadingFlag.value = true
  await getTags(condition).then((resp: ResultObject<TagFrontEntity>) => {
    if (resp.status) {
      tagsData.value = resp.data
    }
  }).catch(() => {
    notify({
      text: "标签获取失败，请重试！",
      type: "warn"
    })
  }).finally(() => {
    loadingFlag.value = false
    scrollDown();
  })
}

onMounted(() => {
  createTags({size: 100})
})
</script>

<template>
  <LRLayout v-if="tagsData">
    <template #content>
      <BoxComponent class="p-10px flex flex-col gap-20px" v-if="tagsData">
        <h1 class="text-24px text-center text-shadow-xl">标签</h1>
        <hr class="hr-edge-weak"/>
        <div class="flex flex-wrap gap-12px justify-center">
          <router-link :to="`/tags/${tag.id}`" v-for="tag of tagsData.tags" class="tag-a shadow rounded-6px py-7px px-5px text-16px <sm:(text-14px)">
            <span class="tag-name mr-5px px-3px rounded-6px">{{tag.name}}</span>
            <span class="tag-article-count bg-$theme-bg-reverse text-$text-color-reverse rounded-6px px-3px">{{tag.articleCount}}篇</span>
          </router-link>
        </div>
      </BoxComponent>
    </template>
    <template #info>
      <NewsArticle class="down-effect-animate" :news-article-list="tagsData?.newsArticleList"/>
      <NewsComment class="down-effect-animate" :news-comment-list="tagsData?.newsCommentList"/>
    </template>
  </LRLayout>
  <LRLayout v-else>
    <template #content>
      <Skeleton customize-class="!w-full !h-300px"/>
    </template>
    <template #info>
      <Skeleton customize-class="!w-full !h-140px"/>
      <Skeleton customize-class="!w-full !h-140px"/>
    </template>
  </LRLayout>
</template>

<style scoped>
.tag-name, .tag-article-count {
  transition:  var(--theme-transition-shadow), var(--theme-transition-bg);
}
.tag-a:hover {
  box-shadow: var(--theme-shadow-inset) !important;
  transition:  var(--theme-transition-shadow);
}
.tag-a:hover .tag-name {
  @apply 'bg-$theme-bg-reverse text-$text-color-reverse';
}
.tag-a:hover .tag-article-count {
  @apply 'bg-$theme-bg text-$text-color'
}
</style>