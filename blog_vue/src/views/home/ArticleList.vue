<script setup lang='ts'>
import type {Ref} from "vue";
import {Like, Eyes, CalendarThirtyTwo, CategoryManagement, TagOne} from '@icon-park/vue-next'
import BoxComponent from '@/components/BoxComponent.vue'
import Skeleton from '@/components/skeleton/Skeleton.vue'
import Pagination from '@/components/pagination/Pagination.vue'
import {onMounted, reactive, ref} from "vue";
import {notify} from '@kyvg/vue3-notification'
import {removeHTMLTag, scrollDown} from '@/utils/utils'
import {getHomeArticleList} from '@/api/requests/Article'
import {useSettingStore} from "@/store";
import {storeToRefs} from "pinia";
import {loadingImg} from "@/constant"

const settingStore = useSettingStore()
const {loadingFlag} = storeToRefs(settingStore)

const articleHomeList: Ref<Array<ArticleHomeEntity> | null> = ref(null)

const createArticleHomeList = async (condition: ConditionParams) => {
  loadingFlag.value = true
  articleHomeList.value = null
  await getHomeArticleList(condition).then((resp) => {
    if (resp.status && resp.data.count !== 0) {
      resp.data.recordList.map((article) => {
        article.likeCount = article.likeCount ? article.likeCount : 0
        article.viewsCount = article.viewsCount ? article.viewsCount : 0
        article.createTime = article.createTime.split(" ")[0]
        article.articleContent = removeHTMLTag(article.articleContent)
      })
      pagination.total = resp.data.count
      articleHomeList.value = resp.data.recordList
    }
  }).catch(() => {
    notify({
      text: "文章列表获取失败，请重试",
      type: "warn"
    })
    articleHomeList.value = null
  }).finally(() => {
    scrollLoadHandle()
    loadingFlag.value = false
  })
}

const scrollLoadHandle = () => {
  const articleContentList = document.querySelectorAll('.box-scroll-load')
// 滚动加载
  const scrollLoad = () => {
    // 目标值,屏幕高度的80%
    let targetValue = window.innerHeight * 0.8;
    // 获取每一个盒子距离浏览器顶部的值
    articleContentList.forEach((box) => {
      let boxTop = box.getBoundingClientRect().top
      if (boxTop <= targetValue) {
        box.classList.add("active")
      } else {
        box.classList.remove("active")
      }
    })
  }
  window.addEventListener("scroll", scrollLoad)
}

onMounted(() => {
  createArticleHomeList({})
})

const pagination = reactive({
  total: 0,
  pageSize: 10,
  currentPage: 1
})

const changePage = (currentPage: any) => {
  if (currentPage !== pagination.currentPage) {
    createArticleHomeList({current: currentPage, size: pagination.pageSize})
    pagination.currentPage = currentPage
    scrollDown();
  } else {
    scrollDown();
  }
}
</script>

<template>
  <template v-if="articleHomeList">
    <BoxComponent v-for="article in articleHomeList" :key="article.id"
                  class="box-scroll-load article-card flex h-200px overflow-hidden">
      <div class="img-box relative w-9/20 h-full rounded-4px overflow-hidden" :class="{'is-top': article.isTop}">
        <router-link :to="`/article/${article.id}`" title="点击查看文章">
          <img v-lazy class="article-cover w-full h-full"
               :data-src="article.articleCover" :src="loadingImg" alt="">
          <div
            class="mask-info absolute top-0 left-0 w-full h-full bg-$theme-translucent opacity-0 transition-opacity duration-300 flex justify-evenly items-center">
            <span class="flex items-center text-24px"><Like fill="red" size="30" class="mr-10px"/>{{
                article.likeCount
              }}</span>
            <span class="flex items-center text-24px"><Eyes size="30" class="mr-10px"/>{{ article.viewsCount }}</span>
          </div>
        </router-link>
      </div>
      <div class="article-content w-11/20 p-10px flex justify-evenly flex-col">
        <h2 class="text-16px hover:(text-$hover-color2) text-shadow-md">
          <router-link :to="`/article/${article.id}`" class="content leading-[1.2] overflow-hidden overflow-ellipsis">
            {{ article.articleTitle }}
          </router-link>
        </h2>
        <hr class="hr-twill py-1px">
        <div class="article-info flex flex-col">
          <div class="flex items-center flex-wrap mb-10px">
            <!-- 发布时间 -->
            <span
              class="flex justify-center items-center px-5px py-3px rounded-6px shadow text-14px <md:(text-12px)"><CalendarThirtyTwo class="mr-3px"/>{{
                article.createTime
              }}</span>
            <span class="border-r-1px border-l-1px border-dashed border-$theme-bg-reverse h-13px mt-2px mx-10px"
                  style="transition: border var(--transition)"></span>
            <!-- 文章发布类型 -->
            <span
              class="flex justify-center items-center px-5px py-3px rounded-6px shadow text-14px <md:(text-12px)">{{
                article.type
              }}</span>
            <span class="border-r-1px border-l-1px border-dashed border-$theme-bg-reverse h-13px mt-2px mx-10px"
                  style="transition: border var(--transition)"></span>
            <router-link :to="`/categories/${article.categoryId}`"
                         class="article-info-hover flex justify-center items-center px-5px py-3px rounded-6px shadow text-14px <md:(text-12px)"
                         :title="`查看 ${article.categoryName} 的其他文章`">
              <CategoryManagement class="mr-3px"/>
              {{ article.categoryName }}
            </router-link>
          </div>
          <!-- 标签 -->
          <div class="flex">
            <router-link v-for="tag in article.tags" :to="`/tags/${tag.id}`"
                         class="article-info-hover flex justify-center items-center rounded-6px py-3px px-5px mr-8px shadow text-14px <md:(text-12px)"
                         :title="`查看 ${tag.name} 的其他文章`">
              <TagOne class="mr-3px"/>
              {{ tag.name }}
            </router-link>
          </div>
        </div>
        <div class="content leading-[1.2] overflow-hidden overflow-ellipsis mt-5px">
          {{ article.articleContent }}
        </div>
      </div>
    </BoxComponent>
    <Pagination v-if="pagination.total > pagination.pageSize" :total="pagination.total" :page-size="pagination.pageSize"
                :current-page="pagination.currentPage" @change-page="changePage"/>
  </template>
  <template v-else>
    <skeleton v-for="i in 10" :key="i" customize-class="!w-full article-card h-200px <md:(h-300px)"
              style="transform: translateX(0)"/>
  </template>
</template>

<style scoped>
.box-scroll-load {
  background-color: var(--theme-bg);
  opacity: 0;
  transition-timing-function: cubic-bezier(.175, .885, .32, 1.275);
  transition: transform .5s ease-in-out, opacity 0.5s linear, var(--theme-transition-shadow), var(--theme-transition-bg);
}

.article-card {
  transform: translateX(-200%);
}

.box-scroll-load.active {
  transform: translateX(0) !important;
  opacity: 1;
}

.article-cover {
  position: relative;
  object-fit: cover;
  object-position: left;
  transition: all .5s ease
}

.img-box.is-top::after {
  @apply 'absolute top-5px -right-25px w-90px bg-red-700 text-light-50 text-16px text-center py-3px';
  content: '置顶';
  transform: rotate(40deg);
  transition: var(--theme-transition-bg);
}

.article-card:hover .article-cover {
  transform: scale(1.1);
}

.article-card:hover .mask-info {
  @apply 'opacity-100';
}

.article-content h2:hover {
  transition: color .3s ease;
}

.article-content:hover hr {
  border-style: var(--theme-bg);
}

.content {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.article-info-hover:hover {
  @apply 'bg-$theme-bg-reverse text-$text-color-reverse';
}

@media screen and (max-width: 760px) {
  .article-card {
    @apply 'h-300px flex-col';
  }

  .img-box {
    @apply 'w-full h-8/20';
    border-radius: 6px 6px 0 0 !important;
  }

  .article-content {
    @apply 'w-full h-full pt-0 flex-1';
  }

  .article-content h2 {
    @apply 'text-center text-16px';
  }

  .article-info {
    justify-content: center;
  }
}
</style>