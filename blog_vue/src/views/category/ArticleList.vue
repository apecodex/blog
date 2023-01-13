<script setup lang='ts'>
import type {Ref} from "vue"
import {useRoute} from "vue-router"
import {getArticlesByCondition} from "@/api/requests/Article";
import {onMounted, ref, watch} from "vue";
import ArticleList from "@/views/comm/ArticleList.vue"
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {scrollDown} from "@/utils/utils";

const settingStore = useSettingStore()
const {loadingFlag} = storeToRefs(settingStore)

const route = useRoute()
const articlesData: Ref<{ count: number, recordList: Array<ArticleSearchConditionEntity>, data: string } | null> = ref(null);
const conditionParams = {size: 10, current: 1} as ConditionParams;

const createArticles = async (condition: ConditionParams) => {
  loadingFlag.value = true
  await getArticlesByCondition(condition).then((resp: PageResultWithObject<Array<ArticleSearchConditionEntity>, string>) => {
    if (resp.status) {
      articlesData.value = resp.data
      document.title = resp.data.data
    } else {
      notify({
        text: resp.message,
        type: "warn"
      });
    }
  }).catch(() => {
    notify({
      text: "请求获取失败，请重试",
      type: "warn"
    })
  }).finally(() => {
    loadingFlag.value = false;
    scrollDown();
  })
}

watch(() => route.params?.categoryId, () => {
  const categoryId = route.params?.categoryId as string | null
  if (categoryId) {
    conditionParams.categoryId = categoryId
    createArticles(conditionParams);
  }
})

onMounted(() => {
  const categoryId = route.params?.categoryId as string | null
  if (categoryId) {
    conditionParams.categoryId = categoryId
    createArticles(conditionParams);
  }
})
</script>

<template>
  <ArticleList :articles="articlesData" :condition="conditionParams"/>
</template>

<style scoped>
</style>