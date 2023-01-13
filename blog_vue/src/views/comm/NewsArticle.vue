<script setup lang='ts'>
import type {PropType} from "vue"
import {UpdateRotation, Time} from "@icon-park/vue-next"
import BoxComponent from "@/components/BoxComponent.vue"
import {loadingImg} from "@/constant"

const props = defineProps({
  newsArticleList: Array as PropType<NewsArticleEntity[]>
})
</script>

<template>
  <BoxComponent v-if="newsArticleList.length !== 0">
    <span class="ml-10px mt-10px text-20px flex gap-5px"><UpdateRotation/>最新文章</span>
    <hr class="hr-twill">
    <ul class="px-10px pb-10px pt-5px flex flex-col gap-8px">
      <li v-for="newArticle of newsArticleList" :key="newArticle.id" title="点击查看文章">
        <router-link :to="`/article/${newArticle.id}`"
                     class="new-article-a flex w-full h-70px rounded-6px overflow-hidden relative">
          <img v-lazy class="w-full h-full object-cover" :data-src="newArticle.articleCover" :src="loadingImg" alt="">
          <div class="absolute w-full h-full flex flex-col justify-center items-start pl-5px gap-8px">
            <span class="text-14px">{{ newArticle.articleTitle }}</span>
            <span class="text-12px flex items-center gap-5px"><Time/>{{newArticle.createTime}}</span>
          </div>
        </router-link>
      </li>
    </ul>
  </BoxComponent>
</template>

<style scoped>
.new-article-a img, .recommend-article-a img {
  opacity: .3;
  transition: opacity .3s, transform .3s;
}

.new-article-a:hover {
  border-radius: 20px 6px 6px 6px;
  transition: border-radius .3s linear;
}

.new-article-a:hover img, .recommend-article-a:hover img {
  transform: scale(1.2);
  opacity: .1;
}

</style>