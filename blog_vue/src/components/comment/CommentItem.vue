<script setup lang='ts'>
import type {PropType} from "vue";
import Comment from "@/components/comment/Comment.vue"
import Reply from "@/components/comment/Reply.vue"
import {ThumbsUp} from "@icon-park/vue-next"
import {loadingImg} from "@/constant"
import {computed, reactive, ref} from "vue";
import {useSettingStore, useUserInfoStore, useWebsiteInfoStore} from "@/store";
import {storeToRefs} from "pinia";
import {parseComment} from "@/utils/utils"
import {replyCommentHandle, moreReplyComment, updateReplyCommentHandle} from "./CommentHooks"
import {notify} from "@kyvg/vue3-notification";
import {StatusCode} from "@/api/enum/StatusCode";
import {commentLike} from "@/api/requests/Comment";

const userInfoStore = useUserInfoStore();
const settingStore = useSettingStore();
const websiteInfoStore = useWebsiteInfoStore();
const {loginFlag, loadingFlag} = storeToRefs(settingStore);
const {websiteInfo} = storeToRefs(websiteInfoStore);
const {userInfo} = storeToRefs((userInfoStore));

const props = defineProps({
  commentData: Object as PropType<CommentEntity>,
  currentIndex: {
    type: Number,
    default: 0
  },
  topicId: String,
  type: Number,
  articleAuthor: {
    type: String,
    default: ""
  }
})

const pagination = {
  size: 5,
  current: 1
}

// 展示评论框
const showReplyComment = ref(false);

// 判断是否已经点赞
const isLikeComp = computed(() => {
  if (userInfoStore.isLogin) {
    if (userInfo.value?.commentLikeSet.indexOf(props.commentData!.id) !== -1) {
      return true;
    }
  }
  return false;
})

// 评论点赞
const clickLikeHandle = async () => {
  if (!userInfoStore.isLogin) {
    loginFlag.value = true;
    return;
  }
  await commentLike(props.commentData!.id).then((resp) => {
    if (resp.status) {
      if (userInfo.value?.commentLikeSet.indexOf(props.commentData!.id) !== -1) {
        (<any>props.commentData!.likeCount) -= 1;
      } else {
        (<any>props.commentData!.likeCount) += 1;
      }
      userInfoStore.commentLike(props.commentData!.id);
    }
  })
}

// 判断是回复用户还是补充评论
const replyUserComp = computed(() => {
  if (userInfoStore.isLogin) {
    if (props.commentData && props.commentData.user.uid === userInfo.value?.uid) {
      return "还有要补充的吗？";
    }
  }
  return "回复：@" + props.commentData!.user.nickname;
})

const replyCountComp = computed(() => {
  return props.commentData?.replyCount ? props.commentData!.replyCount - props.commentData!.replyList.length : null;
})

//  评论
const replyComment = ({commentText, parentId, replyCommentId, topicId, type}: any) => {
  if (commentText.value.length === 0) {
    notify({
      text: "评论内容不能为空",
      type: "warn"
    });
    return;
  }
  if (parentId.value && replyCommentId.value && topicId.value && type.value) {
    loadingFlag.value = true
    notify({
      text: "正在提交评论...",
    })
    replyCommentHandle(commentText.value, parentId.value, replyCommentId.value, topicId.value, type.value).then((resp) => {
      // 未登录
      if (resp.code === StatusCode.UNAUTHORIZED) {
        loginFlag.value = true;
      }
      if (resp.status) {
        if (websiteInfo.value?.isCommentReview) {
          notify({
            text: "评论成功，待审核中...",
            type: "success"
          })
        } else {
          notify({
            text: "评论成功",
            type: "success"
          })
        }
        // 插入成功后更新回复列表
        updateReplyCommentHandle(parentId.value);
        pagination.current = 1;
      }
      commentText.value = "";
      // 关闭评论框
      showReplyComment.value = false;
    }).catch(() => {
      notify({
        text: "回复失败，请重试",
        type: "warn"
      });
    }).finally(() => {
      loadingFlag.value = false
    })
  } else {
    notify({
      text: "回复失败，回复谁？",
      type: "warn"
    });
  }
}

</script>

<template>
  <div class="flex flex-col gap-8px">
    <div class="flex gap-10px">
      <!-- 评论用户头像 -->
      <div class="comment-user-avatar w-60px h-60px rounded-6px overflow-hidden">
        <img v-lazy class="w-full h-full shake"
             :data-src="commentData.user.avatar" :src="loadingImg" alt="">
      </div>
      <div class="flex-1 w-full flex flex-col gap-3px">
        <div class="w-full mb-5px">
          <!-- 评论用户名称 -->
          <span class="text-18px <md:(text-14px)">{{ commentData.user.nickname }}</span>
          <span v-if="articleAuthor === commentData.user.nickname" class="ml-5px bg-$theme-bg-reverse text-$theme-bg rounded-6px px-5px text-12px" style="transition: var(--theme-transition)">博主</span>
          <div class="flex items-center flex-wrap gap-5px mt-3px text-12px text-$text-color2 opacity-70"
               style="transition: var(--theme-transition-color)">
            <span>第{{ currentIndex }}楼</span>
            <span>|</span>
            <span title="浏览器" class="cursor-pointer">{{ commentData.browser }}</span>
            <span>|</span>
            <span title="操作系统" class="cursor-pointer">{{ commentData.os }}</span>
            <span v-if="commentData.distance">|</span>
            <span title="直线距离" class="cursor-pointer" v-if="commentData.distance">距离你: {{
                commentData.distance
              }}km</span>
          </div>
        </div>
        <!-- 评论内容 -->
        <div class="max-h-120px overflow-y-scroll overflow-x-hidden">
          <p class="whitespace-pre-line" v-html="parseComment(commentData.commentContent, '24', '24')"></p>
        </div>
        <div class="flex items-center text-12px text-$text-color2 gap-15px"
             style="transition: var(--theme-transition-color)">
          <!-- 评论时间 -->
          <span>{{ commentData.createTime.substring(0, commentData.createTime.lastIndexOf(":")) }}</span>
          <!-- 点赞 -->
          <button class="flex items-center gap-3px outline-none" @click="clickLikeHandle">
            <ThumbsUp class="hover:(text-red-600)" :class="{'text-red-600': isLikeComp}" theme="filled" size="15"/>
            {{ commentData.likeCount ? commentData.likeCount : 0 }}
          </button>
          <button class="text-14px outline-none" @click="showReplyComment = !showReplyComment">回复</button>
        </div>
        <!-- 回复评论框 -->
        <div class="mt-10px p-10px shadow-inset rounded-6px"
             v-if="showReplyComment">
          <Comment @saveComment="replyComment" :parent-id="commentData.id" :reply-comment-id="commentData.id"
                   :placeholder="replyUserComp" :topic-id="topicId" :type="type"
                   send-btn-text="回复"/>
        </div>
        <hr class="hr-twill py-1px opacity-40">
        <!-- 回复者 -->
        <div v-if="commentData.replyCount">
          <Reply v-for="(reply, index) of commentData.replyList" :key="reply.id" :reply-comment-data="reply"
                 :current-index="index + 1" :topic-id="topicId" :type="type" :pagination="pagination" :articleAuthor="articleAuthor"/>
          <div v-if="commentData.replyCount > 3 && commentData.replyList.length !== commentData.replyCount"
               class="mt-15px mb-10px">
            <span class="mr-10px">还有{{ replyCountComp }}条回复</span>
            <button class="outline-none text-$hover-color3" @click="moreReplyComment(commentData, pagination)">
              查看更多
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@media screen and (max-width: 760px) {
  .comment-user-avatar {
    @apply 'w-40px h-40px';
  }
}
</style>