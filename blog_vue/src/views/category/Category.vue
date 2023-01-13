<script setup lang='ts'>
import type {Ref} from "vue"
import {getCategories} from "@/api/requests/Category"
import {onMounted, ref} from "vue";
import BoxComponent from "@/components/BoxComponent.vue"
import Skeleton from "@/components/skeleton/Skeleton.vue"
import LRLayout from "@/layout/LRLayout.vue";
import NewsArticle from "@/views/comm/NewsArticle.vue";
import NewsComment from "@/views/comm/NewsComment.vue";
import {useSettingStore} from "@/store";
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {scrollDown} from "@/utils/utils";

const settingStore = useSettingStore()
const {loadingFlag} = storeToRefs(settingStore)

const categoriesData: Ref<CategoryFrontEntity | null> = ref(null)

const createCategory = async () => {
  loadingFlag.value = true
  await getCategories().then((resp: ResultObject<CategoryFrontEntity>) => {
    if (resp.status) {
      categoriesData.value = resp.data
    } else {
      notify({
        text: resp.message,
        type: "warn"
      });
    }
  }).catch(() => {
    notify({
      text: "分类获取失败，请重试",
      type: "warn"
    })
  }).finally(() => {
    loadingFlag.value = false;
    scrollDown();
  })
}

onMounted(() => {
  createCategory()
})
</script>

<template>
  <LRLayout v-if="categoriesData">
    <template #content>
      <BoxComponent class="px-10px py-5px flex flex-col category-animate">
        <h1 class="text-24px text-center text-shadow-xl">分类</h1>
        <hr class="hr-edge-weak my-20px"/>
        <ul class="ml-10px" v-if="categoriesData">
          <li v-for="category of categoriesData.categories" class="relative my-5px flex items-center  gap-5px">
            <router-link class="ml-25px break-words" :to="`/categories/${category.id}`">{{ category.name }}</router-link>
            <span style="opacity: .7;">({{ category.articleCount }})</span>
          </li>
        </ul>
      </BoxComponent>
    </template>
    <template #info>
      <NewsArticle class="down-effect-animate" :news-article-list="categoriesData?.newsArticleList"/>
      <NewsComment class="down-effect-animate" :news-comment-list="categoriesData?.newsCommentList"/>
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
ul li:before {
  @apply 'absolute left-0 w-15px h-15px rounded-full';
  content: "";
  border: 3px solid var(--theme-bg-reverse);
  transition: border var(--transition);
}

ul li:hover::before {
  border-color: var(--hover-color2);
}

ul li a:hover {
  color: var(--hover-color3);
  transition: var(--theme-transition-color);
}
</style>