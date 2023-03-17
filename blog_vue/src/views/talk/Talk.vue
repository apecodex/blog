<script setup lang='ts'>
import {useRoute} from "vue-router"
import {onBeforeUnmount, onMounted} from "vue";
import {Time, Like, Comments} from "@icon-park/vue-next"
import {loadingImg} from "@/constant"
import {parseComment, getDate} from "@/utils/utils"
import {
  talkData,
  currentPicture,
  createTalkData,
  previewHandle,
  nextHandle,
  prevHandle,
  commentBtnLock,
  isLikeComp,
  likeHandle
} from "./TalkHooks"
import {saveCommentHandle} from "@/components/comment/CommentHooks"
import BoxComponent from "@/components/BoxComponent.vue";
import PreviewPicture from "@/components/preview/PreviewPicture.vue";
import CommentUser from "@/views/comm/CommentUser.vue";
import Comment from "@/components/comment/Comment.vue";
import CommentList from "@/components/comment/CommentList.vue";
import Skeleton from "@/components/skeleton/Skeleton.vue";

const route = useRoute();

onMounted(() => {
  const talkId = route.params?.talkId as string;
  if (talkId) {
    createTalkData(talkId)
  }
})

onBeforeUnmount(() => {
  // 切换说说前清空，不然再次点击其他说说时获取评论会请求两次，会把上一次的说说评论加载到当前的说说里
  talkData.value = null;
})
</script>

<template>
  <div v-if="talkData">
    <BoxComponent class="p-10px">
      <div class="flex gap-10px">
        <div class="w-60px h-60px rounded-6px overflow-hidden">
          <img v-lazy class="w-full h-full zoomImg object-cover"
               :data-src="talkData.user.avatar" :src="loadingImg" alt="">
        </div>
        <div class="flex-1 flex flex-col gap-5px">
          <div>
            <span class="text-$hover-color2">{{ talkData.user.nickname }}</span>
          </div>
          <div>
            <p class="whitespace-pre-wrap" v-html="parseComment(talkData.content, '24', '24')"></p>
          </div>
          <div class="max-w-300px"
               :class="{'!max-w-400px': talkData.src.length > 4, '!max-w-500px': talkData.src.length > 9}"
               v-if="talkData.src.length !== 0">
            <div class="talk-picture grid gap-2px"
                 :class="{'!grid-cols-none': talkData.src.length === 1, 'talk-picture-md': talkData.src.length > 4}">
              <img v-for="(img, index) of talkData.src" v-lazy :data-src="img" :src="loadingImg" alt=""
                   class="rounded-2px" :class="{'!max-h-160px': talkData.src.length === 1}"
                   @click="previewHandle(index)"/>
            </div>
          </div>
          <div>
            <time title="发布时间" class="text-12px text-$text-color2 flex gap-5px mt-5px"
                  style="transition: var(--theme-transition-color)">
              <Time/>
              <span>{{ getDate(talkData.createTime) }}</span></time>
          </div>
          <div class="flex gap-20px mt-15px">
          <span class="flex text-16px gap-5px shadow py-3px px-8px rounded-full cursor-pointer"
                @click="likeHandle">
              <Like :theme="isLikeComp ? 'filled':''" class="hover:(text-red-600)"
                    :class="{'text-red-600': isLikeComp}"/>{{
              talkData.likeCount
            }}</span>
            <span class="flex text-16px gap-5px shadow py-3px px-8px rounded-full">
              <Comments/>{{ talkData.commentCount }}</span>
          </div>
          <PreviewPicture :picture="currentPicture" @next="nextHandle" @prev="prevHandle"/>
        </div>
      </div>
    </BoxComponent>
    <div class="mt-20px">
      <CommentUser>
        <comment style="transition: var(--theme-transition-shadow)" @saveComment="saveCommentHandle"
                 :topic-id="talkData.id" :type="2"
                 :btn-lock="commentBtnLock"/>
      </CommentUser>
      <hr class="hr-twill mt-20px">
      <CommentList class="mt-20px" :topic-id="talkData.id" :type="2" :article-author="talkData.user.nickname"/>
    </div>
  </div>
  <div v-else>
    <div class="flex gap-20px">
      <Skeleton customize-class="!w-60px !h-60px"/>
      <div class="flex-1 flex flex-col gap-15px">
        <Skeleton customize-class="!w-100px !h-20px"/>
        <Skeleton customize-class="flex-grow !w-full !h-60px"/>
        <Skeleton customize-class="!w-130px !h-20px"/>
        <div class="flex gap-15px">
          <Skeleton customize-class="!w-50px !h-20px"/>
          <Skeleton customize-class="!w-50px !h-20px"/>
        </div>
      </div>
    </div>
    <div class="mt-30px flex flex-col gap-20px">
      <Skeleton customize-class="!w-full !h-80px"/>
      <Skeleton customize-class="!w-full !h-160px"/>
    </div>
  </div>
</template>

<style scoped>
.talk-picture {
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
}

.talk-picture > img {
  @apply 'max-h-140px h-full w-full object-cover object-center';
}

@media screen and (max-width: 468px) {
  .talk-picture-md {
    grid-template-columns: repeat(auto-fill, minmax(80px, 1fr)) !important;
  }
}

@media screen and (max-width: 320px) {
  .talk-picture {
    grid-template-columns: repeat(auto-fill, minmax(60px, 1fr));
  }
}
</style>