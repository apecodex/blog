<script setup lang='ts'>
import type {PropType, Ref} from "vue"
import {computed, nextTick, ref, toRefs} from "vue";
import {Clear, SendOne} from "@icon-park/vue-next"
import {useUserInfoStore, useSettingStore} from "@/store"
import {smilingFaces, humans, animalsAndNature, others} from "@/utils/emojis"
import {storeToRefs} from "pinia";
import {commentBg, emojiLoadingImg} from "@/constant"
import Mask from "@/components/Mask.vue";

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore()
const {loginFlag} = storeToRefs(settingStore);

const showCommentTools = ref(false)
const showEmoji = ref(false);
const emojiIndex = ref(1)

const props = defineProps({
  defaultValue: {
    type: String,
    default: ''
  },
  sendBtnText: {
    type: String,
    default: "评论"
  },
  placeholder: {
    type: String,
    default: "说点啥?"
  },
  btnLock: {
    type: Boolean,
    default: false
  },
  topicId: String,
  type: Number,
  parentId: String,
  replyCommentId: String,
})

const emits = defineEmits(["saveComment"])

// 光标位置
const cursorIndex = ref(0)
// 评论文本
const commentText = ref(props.defaultValue)
const commentTargetRef: Ref<any> = ref(null);

// 随机更换评论贴图
const commentBgComp = computed(() => {
  return Math.random() > 0.5 ? `url(${commentBg[0]})` : `url(${commentBg[1]})`;
})

const saveComment = () => {
  const {parentId, replyCommentId, topicId, type} = toRefs(props);
  emits("saveComment", {commentText, parentId, replyCommentId, topicId, type});
  showEmoji.value = false;
}

// 清空文本
const clearText = () => {
  commentText.value = "";
  closeEmoji();
}

// 记录光标
const onBlur = (event: any) => {
  let target = event.target || event.srcElement; // for IE~
  cursorIndex.value = target.selectionStart;
}

// 插入表情
const insertEmoji = (emojiName: string) => {
  commentText.value = commentText.value.slice(0, cursorIndex.value) + emojiName + commentText.value.slice(cursorIndex.value)
  nextTick(() => {
    // 获取焦点
    commentTargetRef.value?.focus();
    commentTargetRef.value.selectionStart = cursorIndex.value + emojiName.length
    commentTargetRef.value.selectionEnd = cursorIndex.value + emojiName.length
  })
}

// 点击遮罩时关闭表情包且聚焦评论框
const closeEmoji = () => {
  showEmoji.value = false;
  // 聚焦
  commentTargetRef.value.focus();
}
</script>

<template>
  <div class="w-full relative" :class="{'overflow-hidden': !showCommentTools}">
    <div class="comment-no-login" :class="{'active': !userInfoStore.isLogin}">
      <button class="outline-none" @click="loginFlag = true">登录 or 注册</button>
    </div>
    <span class="comment-bg" :style="{backgroundImage: commentBgComp}"></span>
    <textarea v-model="commentText"
              class="comment-textarea w-full h-60px bg-transparent text-16px outline-none resize-none"
              :placeholder="placeholder" @focusin="showCommentTools = true" @blur="onBlur" ref="commentTargetRef"/>
    <div class="comment-tools w-full h-40px flex justify-between items-center gap-10px pt-10px" v-if="showCommentTools">
      <div class="flex gap-15px">
        <button class="relative comment-hover w-30px h-30px shadow p-3px rounded-full outline-none"
                @click="showEmoji = !showEmoji" title="表情包">
          <img v-lazy class="w-24px h-24px z-0 object-cover" v-if="true" :data-src="smilingFaces['[微笑亲亲]']"
               :src="emojiLoadingImg" alt="">
        </button>
        <button
            class="comment-hover shadow w-30px h-30px p-3px rounded-full outline-none flex justify-center items-center"
            title="清空内容" @click="clearText">
          <Clear size="18"/>
        </button>
      </div>
      <button
          class="comment-hover flex items-center gap-5px text-16px rounded-6px shadow p-5px <md:( text-12px) outline-none"
          @click="saveComment" :class="{'!cursor-not-allowed': btnLock}" :disabled="btnLock">
        <SendOne/>
        {{ sendBtnText }}
      </button>
    </div>
    <!-- 遮罩 -->
    <Mask :show="showEmoji" @click="closeEmoji" class="bg-transparent"/>
    <!-- 表情包 -->
    <div
        class="emoji-wrapper absolute w-255px bg-$theme-bg mt-10px flex flex-col justify-between border border-$theme-bg-reverse rounded-6px z-9"
        style="transition: border var(--transition), var(--theme-transition-bg)" v-if="showEmoji">
      <div class="overflow-hidden p-5px">
        <div :class="{'active': emojiIndex === 1}" class="emojis-content overflow-y-scroll">
          <div class="emoji-item grid gap-10px max-h-100px">
            <span class="w-30px h-30px" v-for="(emoji, emojiName, index) in smilingFaces" :key="index"
                  @click="insertEmoji(emojiName)">
              <img class="emoji-hover" v-lazy :data-src="emoji" :src="emojiLoadingImg"
                   :title="emojiName.substring(1, emojiName.length-1)" alt="">
            </span>
          </div>
        </div>
        <div :class="{'active': emojiIndex === 2}" class="emojis-content overflow-y-scroll">
          <div class="emoji-item grid gap-10px max-h-100px">
            <span class="w-30px h-30px" v-for="(emoji, emojiName, index) in humans" :key="index"
                  @click="insertEmoji(emojiName)">
              <img class="emoji-hover" v-lazy :data-src="emoji" :src="emojiLoadingImg"
                   :title="emojiName.substring(1, emojiName.length-1)" alt="">
            </span>
          </div>
        </div>
        <div :class="{'active': emojiIndex === 3}" class="emojis-content overflow-y-scroll">
          <div class="emoji-item grid gap-10px max-h-100px">
            <span class="w-30px h-30px" v-for="(emoji, emojiName, index) in animalsAndNature" :key="index"
                  @click="insertEmoji(emojiName)">
              <img class="emoji-hover" v-lazy :data-src="emoji" :src="emojiLoadingImg"
                   :title="emojiName.substring(1, emojiName.length-1)" alt="">
            </span>
          </div>
        </div>
        <div :class="{'active': emojiIndex === 4}" class="emojis-content overflow-y-scroll">
          <div class="emoji-item grid gap-10px max-h-100px">
            <span class="w-30px h-30px" v-for="(emoji, emojiName, index) in others" :key="index"
                  @click="insertEmoji(emojiName)">
              <img class="emoji-hover" v-lazy :data-src="emoji" :src="emojiLoadingImg"
                   :title="emojiName.substring(1, emojiName.length-1)" alt="">
            </span>
          </div>
        </div>
      </div>
      <div class="p-3px" style="border-top: 1px dashed var(--theme-bg-reverse)">
        <div class="h-full flex gap-12px">
          <img v-lazy class="w-30px h-30px" :data-src="smilingFaces['[微笑]']" :src="emojiLoadingImg" title="笑脸"
               alt="微笑" @click="emojiIndex = 1"/>
          <img v-lazy class="w-30px h-30px" :data-src="humans['[耸肩]']" :src="emojiLoadingImg" title="人类和身体"
               alt="耸肩" @click="emojiIndex = 2"/>
          <img v-lazy class="w-30px h-30px" :data-src="animalsAndNature['[独角兽]']" :src="emojiLoadingImg"
               title="自然和食物" alt="独角兽" @click="emojiIndex = 3"/>
          <img v-lazy class="w-30px h-30px" :data-src="others['[火箭]']" :src="emojiLoadingImg" title="载具和活动"
               alt="火箭" @click="emojiIndex = 4"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/*评论背景贴图*/
.comment-bg {
  @apply 'absolute w-80px h-75px top-0 right-0 bg-no-repeat bg-right-bottom bg-cover';
  content: "";
}

/* 未登录状态 */
.comment-no-login {
  @apply 'h-0 invisible opacity-0';
}

.comment-no-login.active {
  @apply 'absolute w-full z-1 h-85px -mt-10px bg-$theme-translucent flex justify-center items-center visible opacity-100';
  transition: var(--theme-transition-bg);
}

.comment-no-login.active button {
  @apply 'text-18px text-$text-color-reverse px-6px py-5px h-30px bg-$theme-bg-reverse <md:(text-16px)';
  border-radius: 16px 6px 16px 6px;
}

.comment-hover:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}

.emojis-content {
  @apply 'w-full h-0';
  opacity: 0;
  visibility: hidden;
  transition: opacity .3s ease-out;
}

.emojis-content.active {
  @apply 'h-full';
  opacity: 1;
  visibility: visible;
}

.emoji-item {
  grid-template-columns: repeat(auto-fill, minmax(30px, 1fr));
}

.emoji-hover {
  @apply 'rounded-6px';
  transition: var(--theme-transition-bg);
}

.emoji-hover:hover {
  @apply 'bg-$theme-translucent-reverse';
}

@media screen and (max-width: 620px) {
  .comment-bg {
    background-size: 60px 60px;
  }
}

@media screen and (max-width: 440px) {
  .emoji-wrapper {
    @apply 'w-full relative';
  }
}
</style>