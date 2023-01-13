<script setup lang='ts'>
import type {Ref} from 'vue'
import {useRoute} from 'vue-router'
import {useSettingStore, useWebsiteInfoStore} from '@/store'
import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import {onMounted, reactive, ref, watch} from "vue";
import {storeToRefs} from "pinia";
import LRLayout from '@/layout/LRLayout.vue'
import {notify} from '@kyvg/vue3-notification'
import {
  Calendar,
  UpdateRotation,
  Text,
  Eyes,
  Comment,
  CategoryManagement,
  TagOne,
  Time,
  Wechat,
  FriendsCircle,
  TencentQq,
  Weibo,
  CopyLink,
  ListTwo,
  GoodTwo
} from '@icon-park/vue-next'
import BoxComponent from "@/components/BoxComponent.vue";
import Skeleton from "@/components/skeleton/Skeleton.vue"
import Modal from "@/components/modal/Modal.vue"
import NewsArticle from "@/views/comm/NewsArticle.vue"
import NewsComment from "@/views/comm/NewsComment.vue";
import CommentComp from "@/components/comment/Comment.vue"
import CommentUser from "@/views/comm/CommentUser.vue";
import CommentList from "@/components/comment/CommentList.vue"
import PreviewPicture from "@/components/preview/PreviewPicture.vue";
import {loadingImg} from "@/constant"
import {
  article,
  hasArticleCatalog,
  articleWordNum,
  articleReadTime,
  showShareModal,
  copyBtnText,
  articleUrl,
  createArticleByArticleId,
  openShareModal,
  shareWechat,
  shareQzShare,
  shareWeibo,
  onGetCatalog,
  isLikeComp,
  clickLikeHandle
} from "./articleHooks"

import {
  saveCommentHandle,
  commentBtnLock
} from "@/components/comment/CommentHooks"

import {pagination} from "@/components/comment/CommentHooks"
// 文章目录
const MdCatalog = MdEditor.MdCatalog

const settingStore = useSettingStore()
const websiteStore = useWebsiteInfoStore()
const {websiteInfo} = storeToRefs(websiteStore)
const {previewTheme, codeTheme, showCodeRowNumber} = storeToRefs(settingStore);

const route = useRoute();

const scrollElement = document.documentElement;
const copyUrlRef: Ref<HTMLElement | null> = ref(null);
const currentPicture = reactive({
  image: "",
  current: 0,
  maxCount: 1,
  pictureName: ""
})

// 复制
const copyUrlHandle = () => {
  (<any>copyUrlRef.value).select();
  document.execCommand("Copy")
  copyBtnText.value = "Copied"
  notify({
    text: "复制成功",
    type: "success"
  })
  let timer: NodeJS.Timeout;
  timer = setInterval(() => {
    copyBtnText.value = "Copy";
    (<any>window.getSelection()).removeAllRanges()
    clearInterval(timer)
  }, 1000)
}

onMounted(() => {
  const articleId = route.params?.articleId as string | null
  if (articleId) {
    createArticleByArticleId(articleId);
  }
})

// 监听文章id
watch(() => route.params?.articleId, () => {
  const articleId = route.params?.articleId as string | null
  if (articleId) {
    createArticleByArticleId(articleId);
  }
})

const previewImageHandle = (image: string, pictureName: string) => {
  currentPicture.image = image
  currentPicture.pictureName = pictureName
  document.body.style.overflow = 'hidden'
}

</script>

<template>
  <div>
    <LRLayout v-if="article">
      <template #content>
        <div class="flex flex-col gap-20px">
          <!--文章标题 -->
          <BoxComponent class="p-10px">
            <h1 class="text-center text-22px text-shadow-xl <md:(text-20px) <sm:(text-18px)">{{
                article.articleTitle
              }}</h1>
            <div class="flex flex-col <md:(text-12px)">
              <div class="w-full flex justify-center flex-wrap mt-15px">
                <span class="flex justify-center items-center rounded-6px shadow px-5px py-3px mr-10px mb-10px"
                      style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)"><Calendar
                  class="mr-3px"/>发表于 {{
                    article.createTime.split(" ")[0]
                  }}</span>
                <span class="flex justify-center items-center rounded-6px shadow px-5px py-3px mr-10px mb-10px"
                      style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)"><UpdateRotation
                  class="mr-3px"/>更新于 {{
                    article.updateTime.split(" ")[0]
                  }}</span>
                <span class="flex justify-center items-center rounded-6px shadow px-5px py-3px mr-10px mb-10px"
                      style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)">{{
                    article.type
                  }}</span>
                <router-link :to="`/categories/${article.categoryId}`"
                             class="article-info-hover flex justify-center items-center rounded-6px shadow px-5px py-3px mr-10px mb-10px">
                  <CategoryManagement class="mr-3px"/>
                  {{ article.categoryName }}
                </router-link>
              </div>
              <div class="w-full flex justify-center flex-wrap mt-10px">
                <span class="flex justify-center items-center rounded-6px shadow px-5px py-3px mr-10px mb-10px"
                      style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)"><Text
                  class="mr-3px"/>字数统计 {{
                    articleWordNum
                  }}</span>
                <span class="flex justify-center items-center rounded-6px shadow px-5px py-3px mr-10px mb-10px"
                      style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)"><Time
                  class="mr-3px"/>阅读时长 {{
                    articleReadTime
                  }}分钟</span>
                <span class="flex justify-center items-center rounded-6px shadow px-5px py-3px mr-10px mb-10px"
                      style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)"><Eyes
                  class="mr-3px"/>{{
                    article.viewsCount
                  }}</span>
                <span class="flex justify-center items-center rounded-6px shadow px-5px py-3px mr-10px mb-10px"
                      style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)"><Comment
                  class="mr-3px"/>{{
                    pagination.page ? pagination.page : 0
                  }}</span>
              </div>
            </div>
          </BoxComponent>
          <BoxComponent>
            <div class="pt-10px" v-if="article.type === '转载'">
              <span class="pl-10px">原文链接：</span>
              <a :href="article.originalUrl">{{ article.originalUrl }}</a>
              <hr class="hr-twill">
            </div>
            <!-- 文章 -->
            <md-editor v-model="article.articleContent" editorId="my-editor" preview-only
                       :preview-theme="previewTheme"
                       :theme="settingStore.theme"
                       :code-theme="codeTheme"
                       :showCodeRowNumber="showCodeRowNumber"
                       @on-get-catalog="onGetCatalog"/>
            <hr class="hr-twill">
            <!-- 分享、点赞、打赏 -->
            <div class="p-10px flex flex-col">
              <div class="flex justify-center items-center mb-10px">
                <div class="px-20px">
                  <button class="btnShadow w-60px h-30px rounded-6px shadow outline-none" @click="openShareModal">分享
                  </button>
                </div>
                <!-- 点赞 -->
                <div class="heart-btn">
                  <div class="heart-content shadow py-8px px-13px rounded-6px flex cursor-pointer"
                       @click="clickLikeHandle">
                    <span class="heart" :class="{'heart-active': isLikeComp}"></span>
                    <span class="like">点赞</span>
                    <span class="numb" :data-numb="article.likeCount"></span>
                  </div>
                </div>
                <!-- 打赏 -->
                <div class="reward-wrapper px-20px relative">
                  <button class="btnShadow w-60px h-30px rounded-6px shadow outline-none">
                    打赏
                    <div class="reward-content flex gap-10px p-8px">
                      <span class="flex-1">
                        <img class="w-full h-full rounded-8px" :src="websiteInfo?.weiXinQRCode" alt=""
                             @click="previewImageHandle(websiteInfo?.weiXinQRCode, '微信打赏码')">
                      </span>
                      <span class="flex-1">
                        <img class="w-full h-full rounded-8px" :src="websiteInfo?.alipayQRCode" alt=""
                             @click="previewImageHandle(websiteInfo?.alipayQRCode, '支付宝打赏码')">
                      </span>
                    </div>
                  </button>
                </div>
              </div>
              <!-- 版权声明 -->
              <div
                class="copyright-wrapper relative my-10px shadow-inset rounded-6px pl-15px pr-4px py-6px flex flex-col">
                <div class="flex">
                  <span class="block">本文作者：</span><span>{{ article.author.nickname }}</span>
                </div>
                <div class="flex">
                  <span class="block">本文链接：</span>
                  <span class="break-all flex-1"><router-link :to="`/article/${article.id}`"
                                                              class="hover:(text-$hover-color2)"
                                                              style="border-bottom: 1px solid var(--theme-bg-reverse); transition: border-bottom-color var(--transition)"
                  >{{ articleUrl }}</router-link></span>
                </div>
                <div class="flex">
                  <span class="block">版权声明：</span>
                  <span class="break-all flex-1">本博客所有文章除特别声明外，均采用 <a class="hover:(text-$hover-color2)"
                                                                                      style="border-bottom: 1px solid var(--theme-bg-reverse); transition: border-bottom-color var(--transition)"
                                                                                      href="https://creativecommons.org/licenses/by-nc-sa/4.0/"
                                                                                      target="_blank">CC BY-NC-SA 4.0 </a> 许可协议。转载请注明出处！</span>
                </div>
              </div>
              <span class="flex justify-start items-center my-10px">
                <router-link :to="`/tags/${tag.id}`" v-for="tag of article.tags"
                             class="article-info-hover flex justify-center items-center rounded-6px shadow px-5px mr-10px"
                             :key="tag.id">
                                  <TagOne class="mr-3px"/>
                  {{ tag.name }}</router-link>
              </span>
            </div>
          </BoxComponent>
          <!-- 上下篇文章 -->
          <div class="last-next-article flex justify-between gap-10px w-full rounded-6px">
            <div class="flex-1 max-w-1/2 text-left h-60px relative" v-if="article.lastArticle">
              <router-link :to="`/article/${article.lastArticle.id}`"
                           class="relative w-full h-full inline-block overflow-hidden rounded-6px">
                <img v-lazy :data-src="article.lastArticle.articleCover" :src="loadingImg" alt=""
                     class="w-full h-full bg-cover object-cover">
                <div class="absolute w-full h-full top-0 left-30px flex flex-col justify-center items-start">
                  <span>上一篇</span>
                  <h2>{{ article.lastArticle.articleTitle }}</h2>
                </div>
              </router-link>
            </div>
            <div class=" flex-1 max-w-1/2 text-right h-60px relative" v-if="article.nextArticle">
              <router-link :to="`/article/${article.nextArticle.id}`"
                           class="relative w-full h-full inline-block overflow-hidden rounded-6px">
                <img v-lazy :data-src="article.nextArticle.articleCover" :src="loadingImg" alt=""
                     class="w-full h-full bg-cover object-cover">
                <div class="absolute w-full h-full top-0 right-30px flex flex-col justify-center items-end">
                  <span>下一篇</span>
                  <h2>{{ article.nextArticle.articleTitle }}</h2>
                </div>
              </router-link>
            </div>
          </div>
        </div>
        <!-- 推荐文章 -->
        <BoxComponent class="p-10px" v-if="article.recommendArticleList.length !== 0">
          <span class="text-20px flex gap-5px"><GoodTwo/>推荐文章</span>
          <hr class="hr-twill">
          <ul class="recommend-content flex gap-10px flex-wrap mt-10px">
            <li v-for="recommendArticle of article.recommendArticleList" class="flex-1" title="点击查看文章">
              <router-link :to="`/article/${recommendArticle.id}`"
                           class="recommend-article-a flex min-w-260px h-80px overflow-hidden rounded-6px relative">
                <img v-lazy class="w-full h-ull object-cover" :data-src="recommendArticle.articleCover"
                     :src="loadingImg" alt="">
                <div class="absolute w-full h-full flex flex-col justify-center items-start pl-5px gap-8px">
                  <span class="text-14px">{{ recommendArticle.articleTitle }}</span>
                  <span class="text-12px flex items-center gap-5px"><Time/>{{ recommendArticle.createTime }}</span>
                </div>
              </router-link>
            </li>
          </ul>
        </BoxComponent>
        <!-- 分享 -->
        <Modal v-model:show="showShareModal" title="分享" custom-class="!max-w-360px">
          <hr class="hr-twill">
          <div class="share-icon flex justify-evenly items-center mt-20px">
            <span class="w-40px h-40px rounded-full shadow flex justify-center items-center hover:(text-lime-500)"
                  @click="shareWechat"><Wechat size="26"/></span>
            <span class="w-40px h-40px rounded-full shadow flex justify-center items-center hover:(text-emerald-700)"
                  @click="shareWechat"><FriendsCircle size="26"/></span>
            <span class="w-40px h-40px rounded-full shadow flex justify-center items-center hover:(text-cyan-500)"
                  @click="shareQzShare"><TencentQq size="26"/></span>
            <span class="w-40px h-40px rounded-full shadow flex justify-center items-center hover:(text-red-500)"
                  @click="shareWeibo"><Weibo
              size="26"/></span>
          </div>
          <p class="mt-10px flex items-center">
            <CopyLink size="16" class="mr-10px"/>
            Copy Like
          </p>
          <div class="field">
            <input ref="copyUrlRef" type="text" :value="articleUrl">
            <button @click="copyUrlHandle">{{ copyBtnText }}</button>
          </div>
        </Modal>
        <!-- 评论 -->
        <CommentUser>
          <CommentComp @saveComment="saveCommentHandle" :topic-id="article.id" :type="1" :btn-lock="commentBtnLock"/>
        </CommentUser>
        <hr class="hr-twill"/>
        <CommentList :topic-id="article.id" :type="1" :article-author="article.author.nickname"/>
      </template>
      <template #info>
        <!-- 文章目录 -->
        <BoxComponent v-if="hasArticleCatalog">
          <span class="ml-10px mt-10px text-16px flex"><ListTwo class="mr-5px"/>目录</span>
          <hr class="hr-twill">
          <MdCatalog
            class="max-h-260px my-5px overflow-y-scroll"
            editorId="my-editor"
            :scroll-element="scrollElement"
            :theme="settingStore.theme"
          />
        </BoxComponent>
        <!-- 最新文章 -->
        <NewsArticle :news-article-list="article.newsArticleList"/>
        <!-- 最新评论 -->
        <NewsComment :news-comment-list="article.newsCommentList"/>
      </template>
    </LRLayout>
    <LRLayout v-else>
      <template #content>
        <Skeleton class="!w-full"/>
        <Skeleton class="!w-full h-320px"/>
      </template>
      <template #info>
        <Skeleton class="!w-full h-110px"/>
        <Skeleton class="!w-full h-110px"/>
        <Skeleton class="!w-full h-110px"/>
      </template>
    </LRLayout>
    <PreviewPicture :picture="currentPicture"/>
  </div>
</template>

<style scoped>
.md {
  @apply 'bg-$theme-bg rounded-8px px-5px';
  transition: var(--theme-transition), var(--theme-transition-fill), border var(--transition);
}

.article-info-hover:hover {
  @apply 'bg-$text-color text-$text-color-reverse';
  transition: none !important;
}

.btnShadow {
  transition: var(--theme-transition-bg), var(--theme-transition-shadow);
}

.btnShadow:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}

.share-icon span:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}

.field {
  @apply 'flex items-center !my-10px gap-10px rounded-6px';
}

.field input {
  @apply 'outline-none w-full h-25px px-3px rounded-6px overflow-hidden bg-$theme-bg border-1px border-dashed';
}

.field button {
  @apply 'px-5px h-25px rounded-6px';
  box-shadow: var(--theme-shadow);
}

.field button:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}

/* 点赞 */
.heart-content {
  @apply 'relative';
  transition: var(--theme-transition-shadow);
}

.heart-content .heart {
  @apply 'w-90px h-90px absolute top-1/2 left-1/5';
  background: url("@/assets/imgs/heart.png") no-repeat left;
  background-size: 2900%;
  transform: translate(-50%, -50%);
}

.heart-content .heart:hover {
  background-position: right;
  animation: heart-animate .8s steps(28) 1;
}

.heart.heart-active {
  background-position: right;
  animation: heart-animate .8s steps(28) 1;
}

.heart-content .like {
  margin-left: 30px;
  color: var(--theme-translucent-reverse);
  transition: var(--theme-transition-color);
}

.heart-content .numb::before {
  @apply 'font-semibold ml-7px';
  content: attr(data-numb);
}

@keyframes heart-animate {
  0% {
    background-position: left;
  }
  100% {
    background-position: right;
  }
}

/* 打赏 */
.reward-content {
  @apply 'absolute w-260px h-120px -left-80px bg-$theme-bg rounded-6px opacity-0';
  visibility: hidden;
  top: -460%;
  box-shadow: var(--theme-shadow);
  transition: var(--theme-transition-bg), var(--theme-transition-shadow);
}

.reward-content::after {
  @apply 'w-full h-20px absolute left-0 -bottom-20px';
  content: '';
}

.reward-content::before {
  @apply 'absolute -bottom-20px right-1/2';
  content: '';
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-bottom: 10px solid transparent;
  border-top: 10px solid var(--theme-bg);
  transform: translateX(50%);
  transition: border var(--transition);
}

.reward-wrapper button:hover .reward-content {
  @apply 'opacity-100';
  animation: flipInX .5s ease-in;
  visibility: visible;
}

.reward-content span img {
  transition: transform .3s ease;
}

.reward-content span img:hover {
  transform: scale(1.2);
}

/*版权*/
.copyright-wrapper {
  transition: var(--theme-transition-bg), var(--theme-transition-shadow);
}

.copyright-wrapper::before {
  @apply 'absolute w-5px h-full bg-$theme-bg-reverse top-0 left-0';
  content: "";
  border-radius: 6px 0 0 6px;
  transition: var(--theme-transition-bg);
}

/* 上下篇文章 */
.last-next-article img {
  @apply 'opacity-30';
  transition: transform .3s ease-in-out, opacity .3s ease-in-out;
}

.last-next-article a:hover img {
  @apply 'opacity-100';
  transform: scale(1.2);
}

/* 推荐文章 */
.recommend-article-a img {
  @apply 'opacity-30';
  transition: opacity .3s, transform .3s;
}

.recommend-article-a:hover img {
  @apply 'opacity-10';
  transform: scale(1.2);
}

.recommend-content li:nth-child(odd) .recommend-article-a {
  border-radius: 17px 6px 17px 6px;
}

.recommend-content li:nth-child(even) .recommend-article-a {
  border-radius: 6px 17px 6px 17px;
}

@media screen and (max-width: 760px) {
  .last-next-article {
    @apply 'flex-col gap-5px';
  }

  .last-next-article div {
    @apply 'max-w-full';
  }

  .last-next-article a {
    @apply 'h-60px';
  }
}

@media screen and (max-width: 480px) {
  .reward-content {
    @apply '-left-180px';
  }

  .reward-content::before {
    @apply 'right-20px';
  }
}
</style>