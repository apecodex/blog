<script setup lang='ts'>
import type {PropType} from "vue";
import {Comments} from "@icon-park/vue-next"
import BoxComponent from "@/components/BoxComponent.vue"
import {getDateDiff} from "@/utils/utils"
import {loadingImg} from "@/constant"
import {parseComment} from "@/utils/utils"

const props = defineProps({
  newsCommentList: Array as PropType<NewCommentEntity[]>
})


</script>

<template>
  <BoxComponent v-if="newsCommentList.length !== 0">
    <span class="ml-10px mt-10px text-20px flex gap-5px"><Comments/>最新评论</span>
    <hr class="hr-twill">
    <ul class="flex flex-col gap-10px px-10px pb-10px pt-5px">
      <li class="comment-item flex w-full h-50px gap-5px" v-for="comment of newsCommentList" :key="comment.topicId" title="点击查看">
        <router-link :to="`/${comment.type === 1 ? 'article':'talk'}/${comment.topicId}`"
                     class="comment-avatar rounded-6px w-70px h-50px overflow-hidden">
          <img v-lazy class="w-full h-50px"
               :data-src="comment.user.avatar" :src="loadingImg" alt="">
        </router-link>
        <div class="flex flex-col justify-evenly w-full h-full">
          <router-link :to="`/${comment.type === 1 ? 'article':'talk'}/${comment.topicId}`"
                       class="comment-content !break-all" :title="comment.commentContent" v-html="parseComment(comment.commentContent, '15', '15')">
          </router-link>
          <div class="text-12px break-all text-$text-color2" style="transition: var(--theme-transition-color)">
            <span>{{ comment.user.nickname }}</span>
            <span class="px-3px">|</span>
            <span class="text-$text-color2"
                  style="transition: var(--theme-transition-color)">{{ getDateDiff(comment.createTime) }}</span>
          </div>
        </div>
      </li>
    </ul>
  </BoxComponent>
</template>

<style scoped>
.comment-item {
  border-radius: 6px;
  transition: background-color .2s ease-out;
}

.comment-item:hover {
  background-color: rgba(0, 0, 0, .2);
}

.comment-avatar img {
  transition: transform .2s ease-out;
}

.comment-avatar img:hover {
  transform: scale(1.2);
}

.comment-content {
  @apply 'overflow-hidden text-13px overflow-ellipsis leading-none';
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.comment-content:hover {
  @apply '!text-$hover-color3';
  transition: color .3s ease-out;
}
</style>