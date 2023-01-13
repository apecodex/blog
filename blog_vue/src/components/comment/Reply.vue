<script setup lang='ts'>
import type {PropType} from "vue"
import {ThumbsUp} from "@icon-park/vue-next"
import Comment from "@/components/comment/Comment.vue"
import {computed, reactive, ref} from "vue";
import {loadingImg} from "@/constant"
import {useSettingStore, useUserInfoStore, useWebsiteInfoStore} from "@/store"
import {storeToRefs} from "pinia";
import {replyCommentHandle, updateReplyCommentHandle} from "./CommentHooks"
import {parseComment} from "@/utils/utils"
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
  replyCommentData: Object as PropType<ReplyEntity>,
  currentIndex: {
    type: Number,
    default: 0
  },
  topicId: String,
  type: Number,
  articleAuthor: {
    type: String,
    default: ""
  },
  pagination: Object as PropType<{ size: number, current: number }>}
)

// 显示回复评论框
const showReplyComment = ref(false);

// 判断是否已经点赞
const isLikeComp = computed(() => {
  if (userInfoStore.isLogin) {
    if (userInfo.value?.commentLikeSet.indexOf(props.replyCommentData!.id) !== -1) {
      return true;
    }
  }
  return false;
})

//  点击回复时，判断是否是回复自己的评论
const replyUserComp = computed(() => {
  if (userInfoStore.isLogin) {
    if (props.replyCommentData && props.replyCommentData.user.uid === userInfo.value?.uid) {
      return true;
    }
  }
  return false;
})

// 展示回复评论时，判断回复用户是否是自己回复自己
const checkReplyUserComp = computed(() => {
  if (props.replyCommentData && props.replyCommentData.replyUser) {
    if (props.replyCommentData.replyUser.uid !== props.replyCommentData.user.uid) return true;
  }
  return false;
})

// 回复
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
        // 评论分页初始化
        props.pagination!.current = 1;
      }
      commentText.value = ""
      // 关闭评论框
      showReplyComment.value = false
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

// 评论点赞
const clickLikeHandle = async () => {
  if (!userInfoStore.isLogin) {
    loginFlag.value = true;
    return;
  }
  await commentLike(props.replyCommentData!.id).then((resp) => {
    if (resp.status) {
      if (userInfo.value?.commentLikeSet.indexOf(props.replyCommentData!.id) !== -1) {
        (<any>props.replyCommentData!.likeCount) -= 1;
      } else {
        (<any>props.replyCommentData!.likeCount) += 1;
      }
      userInfoStore.commentLike(props.replyCommentData!.id);
    }
  })
}


</script>

<template>
  <div>
    <div class="flex gap-10px" v-if="replyCommentData">
      <div class="comment-user-avatar w-30px h-30px rounded-6px overflow-hidden">
        <img v-lazy class="w-full h-full shake"
             :data-src="replyCommentData.user.avatar" :src="loadingImg" alt="">
      </div>
      <div class="flex-1 w-full flex flex-col gap-3px">
        <div class="w-full mb-5px">
          <span class="text-14px">{{ replyCommentData.user.nickname }}</span>
          <span v-if="articleAuthor === replyCommentData.user.nickname" class="ml-5px bg-$theme-bg-reverse text-$theme-bg rounded-6px px-5px text-12px" style="transition: var(--theme-transition)">博主</span>
          <div class="flex items-center flex-wrap gap-5px mt-3px text-12px text-$text-color2 opacity-70"
               style="transition: var(--theme-transition-color)">
            <span>第{{ currentIndex }}楼</span>
            <span>|</span>
            <span title="浏览器" class="cursor-pointer">{{ replyCommentData.browser }}</span>
            <span>|</span>
            <span title="操作系统" class="cursor-pointer">{{ replyCommentData.os }}</span>
            <span v-if="replyCommentData.distance">|</span>
            <span title="直线距离" class="cursor-pointer"
                  v-if="replyCommentData.distance">距离你: {{ replyCommentData.distance }}km</span>
          </div>
        </div>
        <div class="max-h-120px overflow-y-scroll overflow-x-hidden">
        <span class="mr-8px cursor-pointer text-$hover-color2 hover:(text-$text-color)"
              v-if="checkReplyUserComp">@{{ replyCommentData.replyUser.nickname }}</span>
          <p class="whitespace-pre-line inline-block"
             v-html="parseComment(replyCommentData.commentContent, '24','24')"></p>
        </div>
        <div class="flex items-center text-12px text-$text-color2 gap-15px"
             style="transition: var(--theme-transition-color)">
          <span>{{ replyCommentData.createTime.substring(0, replyCommentData.createTime.lastIndexOf(":")) }}</span>
          <button class="flex items-center gap-3px outline-none" @click="clickLikeHandle">
            <ThumbsUp :theme="isLikeComp ? 'filled':''" class="hover:(text-red-600)" :class="{'text-red-600': isLikeComp}" size="15"/>
            {{ replyCommentData.likeCount ? replyCommentData.likeCount : 0 }}
          </button>
          <button class="text-14px outline-none" @click="showReplyComment = !showReplyComment">回复</button>
        </div>
      </div>
    </div>
    <div class="mt-10px p-10px shadow-inset rounded-6px"
         v-if="showReplyComment">
      <Comment @saveComment="replyComment" :parent-id="replyCommentData.parentId" :reply-comment-id="replyCommentData.id"
               :placeholder="`${replyUserComp ? '还有要补充的吗？':'回复：@'+ replyCommentData.user.nickname}`"
               send-btn-text="回复" :topic-id="topicId" :type="type"/>
    </div>
    <hr class="hr-twill py-1px opacity-40">
  </div>
</template>

<style scoped>
@media screen and (max-width: 760px) {
  .comment-user-avatar {
    @apply 'w-40px h-40px';
  }
}
</style>