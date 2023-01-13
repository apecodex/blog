<script setup lang='ts'>
import CommentItem from "@/components/comment/CommentItem.vue"
import BoxComponent from "@/components/BoxComponent.vue";
import Skeleton from "@/components/skeleton/Skeleton.vue"
import {onMounted} from "vue";
import {
  commentLoading,
  commentsData,
  pagination,
  initCommentDataHandle,
  updateCommentDataHandle
} from "./CommentHooks"

const props = defineProps({
  topicId: String,
  type: Number,
  articleAuthor: {
    type: String,
    default: ""
  }
})

onMounted(() => {
  if (props.topicId) {
    initCommentDataHandle({topicId: props.topicId, type: props.type as number, current: 1, size: pagination.size})
  }
})

// 点击加载更多
const moreCommentHandle = () => {
  updateCommentDataHandle({
    topicId: props.topicId as string,
    type: props.type as number,
    current: pagination.current,
    size: pagination.size
  });
}
</script>

<template>
  <!--  首次加载时先展示骨架屏 -->
  <BoxComponent v-if="commentLoading" class="flex flex-col gap-25px p-10px">
    <div class="flex gap-15px" v-for="i in 3" :key="i">
      <Skeleton class="!w-70px !rounded-6px"/>
      <Skeleton class="!w-full !h-90px"/>
    </div>
  </BoxComponent>
  <div v-else>
    <BoxComponent class="p-10px" v-if="commentsData">
      <!-- 评论数量 -->
      <div class="text-18px mb-20px font-bold">{{ pagination.page }} 评论</div>
      <!-- 评论列表 -->
      <CommentItem v-for="(comment, index) of commentsData" :key="comment.id" :comment-data="comment" :current-index="pagination.page - index" :topic-id="topicId" :type="type" :articleAuthor="articleAuthor"/>
      <!-- 加载按钮 -->
      <div class="flex justify-center mt-15px mb-10px" v-if="commentsData.length !== pagination.page">
        <button class="more-btn px-10px h-30px shadow bg-$theme-bg rounded-6px outline-none" @click="moreCommentHandle"  style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)">查看剩下的{{ pagination.page - commentsData.length }}条</button>
      </div>
    </BoxComponent>
    <BoxComponent class="p-10px" v-else>
      <p class="text-center">暂无评论</p>
    </BoxComponent>
  </div>
</template>

<style scoped>
.more-btn:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}
</style>