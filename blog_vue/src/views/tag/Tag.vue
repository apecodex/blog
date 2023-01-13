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

const Color = () => {
  let colorAngle = Math.floor(Math.random() * 360);
  return 'hsla(' + colorAngle + ',100%,50%, 0.' + random(4, 10) + ')';
}

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
        <div class="flex flex-wrap gap-10px">
          <router-link :to="`/tags/${tag.id}`" v-for="tag of tagsData.tags" class="tag-a"
                       :style="{ 'font-size': (Math.floor(Math.random() * 10) + 15) + 'px', 'color': Color()}">
            {{ tag.name }}
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
.tag-a {
  transition: color .3s ease-out;
}

.tag-a:hover {
  @apply '!text-$theme-bg-reverse';
}
</style>