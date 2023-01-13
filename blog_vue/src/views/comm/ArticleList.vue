<script setup lang='ts'>
import type {PropType} from "vue"
import {loadingImg} from "@/constant"
import {
  Time,
  CategoryManagement,
  TagOne
} from "@icon-park/vue-next"
import BoxComponent from "@/components/BoxComponent.vue"
import Skeleton from "@/components/skeleton/Skeleton.vue";
import {getArticlesByCondition} from "@/api/requests/Article"
import {toRefs} from "vue";

type articleType = {
  count: number,
  recordList: Array<ArticleSearchConditionEntity>,
  data: string
}

const props = defineProps({
  articles: Object as PropType<articleType>,
  condition: Object as PropType<ConditionParams>
})

const {condition} = toRefs(props);

//  查看更多
const moreArticleHandle = async () => {
  if (condition?.value) {
    if (props.articles!.recordList.length !== props.articles!.count) {
      condition.value.current = condition.value.current! + 1;
      await getArticlesByCondition(condition.value).then((resp: PageResultWithObject<Array<ArticleSearchConditionEntity>, string>) => {
        if (resp.status) {
          props.articles!.recordList.push(...resp.data.recordList)
        }
      })
    }
  }
}

</script>

<template>
  <BoxComponent class="px-10px py-20px flex flex-col gap-10px" v-if="articles">
    <h1 class="text-26px text-center">{{ articles.data }}（{{ articles.count }}篇）</h1>
    <hr class="hr-edge-weak my-10px"/>
    <div class="article-wrapper grid gap-15px px-5px" v-if="articles.count !== 0">
      <div class="article-item relative shadow bg-$theme-bg rounded-6px overflow-hidden"
           v-for="article in articles.recordList">
        <router-link :to="`/article/${article.articleId}`"
                     class="inline-block w-full h-100px overflow-hidden rounded-6px">
          <img v-lazy class="article-cover w-full h-full object-cover"
               :data-src="article.articleCover" :src="loadingImg" alt="" title="点击查看文章">
        </router-link>
        <span class="absolute -top-1px -right-1px w-45px text-center bg-$theme-bg"
              style="transition: var(--theme-transition-bg); border-radius: 0 0 0 10px">{{ article.type }}</span>
        <div class="flex justify-between flex-col px-10px py-5px text-12px">
          <router-link :to="`/article/${article.articleId}`" class="text-16px <md:(text-14px) hover:(text-$hover-color2)">{{ article.articleTitle }}</router-link>
          <div class="flex justify-between mt-15px">
            <span class="shadow py-3px px-5px rounded-6px flex items-center gap-5px"
                  style="transition: var(--theme-transition-bg), var(--theme-transition-shadow);"><Time/>{{ article.createTime.split(" ")[0] }}</span>
            <router-link :to="`/categories/${article.categoryId}`"
                         class="hover-a shadow py-3px px-5px rounded-6px flex items-center gap-5px"
                         style="transition: var(--theme-transition-bg), var(--theme-transition-shadow);" :title="`点击查看 ${article.categoryName} 相关的文章`">
              <CategoryManagement/>
              {{ article.categoryName }}
            </router-link>
          </div>
          <hr class="hr-twill mt-10px p-1px"/>
          <div class="py-5px flex gap-8px">
            <router-link :to="`/tags/${tag.id}`" class="hover-a flex items-center gap-5px shadow rounded-6px py-3px px-5px"
                         v-for="tag of article.tags"
                         style="transition: var(--theme-transition-bg), var(--theme-transition-shadow);" :title="`点击查看 ${tag.name} 相关的文章`">
              <TagOne/>
              {{ tag.name }}
            </router-link>
          </div>
        </div>
      </div>
      <div class="more-article flex justify-center items-center" v-if="articles.count !== 0 && articles.recordList.length !== articles.count">
        <button class="h-30px rounded-6px px-10px bg-$theme-bg-reverse text-$text-color-reverse" @click="moreArticleHandle">查看剩下的{{articles.count - articles.recordList.length}}篇</button>
      </div>
    </div>
    <div class="text-center text-18px" v-else>
      暂无文章
    </div>
  </BoxComponent>
  <BoxComponent class="px-10px py-20px flex flex-col gap-10px" v-else>
    <div class="flex justify-center">
      <Skeleton customize-class="!w-120px h-30px"/>
    </div>
    <hr class="hr-edge-weak my-10px">
    <div class="article-wrapper grid gap-15px px-5px">
      <Skeleton v-for="i in 9" customize-class="article-item relative !w-full h-200px"/>
    </div>
  </BoxComponent>
</template>

<style scoped>
.article-wrapper {
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
}

.article-item {
  transition: var(--theme-transition-shadow), var(--theme-transition-bg);
}

.article-cover {
  transition: transform .3s ease;
}

.article-cover:hover {
  transform: scale(1.2);
}

.hover-a:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}

.more-article button {
  transition: var(--theme-transition), var(--theme-transition-shadow);
}

.more-article button:hover {
  @apply 'bg-$theme-bg text-$text-color';
  box-shadow: var(--theme-shadow);
}

</style>