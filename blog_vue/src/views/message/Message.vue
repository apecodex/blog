<script setup lang='ts'>
import BoxComponent from "@/components/BoxComponent.vue"
import Pagination from "@/components/pagination/Pagination.vue";
import {loadingImg} from "@/constant"
import {getDate} from "@/utils/utils"
import {
  pagination,
  messagesData,
  messageForm,
  saveMessageHandle,
  createMessagesData,
  changePage,
  clickMessageItem
} from "./MessageHooks"
import {onMounted} from "vue";
import Skeleton from "@/components/skeleton/Skeleton.vue";
import PreviewMessage from "@/views/message/PreviewMessage.vue";

onMounted(() => {
  createMessagesData(pagination);
})

</script>

<template>
  <BoxComponent class="p-10px">
    <div class="message-btn flex flex-col items-center">
      <span v-if="messagesData && messagesData.count" class="mb-15px shadow py-3px px-8px rounded-6px">{{messagesData?.count}}条留言~</span>
      <div class="flex flex-col items-center gap-15px">
        <div class="flex flex-wrap justify-center gap-15px">
          <label class="theme-check">
            <input type="radio" name="theme" value="default" v-model="messageForm.theme">
            <span class="transition"></span>
          </label>
          <label class="theme-check">
            <input type="radio" name="theme" value="orange" v-model="messageForm.theme">
            <span class="transition"></span>
          </label>
          <label class="theme-check">
            <input type="radio" name="theme" value="tomato" v-model="messageForm.theme">
            <span class="transition"></span>
          </label>
          <label class="theme-check">
            <input type="radio" name="theme" value="FlamingoPink" v-model="messageForm.theme">
            <span class="transition"></span>
          </label>
          <label class="theme-check">
            <input type="radio" name="theme" value="watermelon" v-model="messageForm.theme">
            <span class="transition"></span>
          </label>
          <label class="theme-check">
            <input type="radio" name="theme" value="PrestigeBlue" v-model="messageForm.theme">
            <span class="transition"></span>
          </label>
          <label class="theme-check">
            <input type="radio" name="theme" value="UfoGreen" v-model="messageForm.theme">
            <span class="transition"></span>
          </label>
          <label class="theme-check">
            <input type="radio" name="theme" value="BrightGreek" v-model="messageForm.theme">
            <span class="transition"></span>
          </label>
          <label class="theme-check">
            <input type="radio" name="theme" value="wisteria" v-model="messageForm.theme">
            <span class="transition"></span>
          </label>
        </div>
        <div class="w-full flex justify-center">
          <input type="text" class="message-input" v-model="messageForm.content" :class="messageForm.theme" placeholder="想说点啥？~"/>
        </div>
        <button @click="saveMessageHandle"
                class="mt-5px py-0.1em px-0.25em w-8em h-2.5em bg-$theme-bg border-0.08em border-solid border-$theme-bg-reverse rounded-0.3em text-12px outline-none">
            <span
                class="relative flex justify-center items-center bottom-0.3em w-5.9em h-1.7em bg-$theme-bg-reverse rounded-0.2em text-1.25em text-$text-color-reverse border-0.08em border-solid border-$text-color-reverse">留下印记~</span>
        </button>
      </div>
    </div>
    <hr class="hr-twill my-15px">
    <template v-if="messagesData">
      <template v-if="messagesData.count !== 0">
        <div class="message-content grid gap-y-15px gap-x-28px">
          <div class="message-item rounded-6px px-3px py-3px h-140px flex flex-col gap-2px"
               v-for="(message, index) of messagesData.recordList" :key="index">
            <div class="h-30px mb-2px ml-18px flex items-center gap-5px">
              <img v-lazy class="w-30px h-30px rounded-full shake"
                   :data-src="message.avatar" :src="loadingImg" alt="">
              <span class="text-12px">{{ message.nickname }}</span>
            </div>
            <div class="pager cursor-pointer" :class="message.theme" @click="clickMessageItem(message)">
              <div class="tape-corner"></div>
              <p class="message-text text-dark-900 px-8px leading-[1.2] overflow-hidden overflow-ellipsis ">
                {{ message.content }}</p>
              <div class="tape-corner"></div>
              <time class="absolute bottom-2px right-25px text-12px text-dark-900">{{ getDate(message.createTime) }}</time>
            </div>
          </div>
        </div>
        <Pagination v-if="messagesData.count > pagination.size" class="mt-30px" :total="messagesData.count" :page-size="pagination.size" :current-page="pagination.current" @changePage="changePage"/>
        <PreviewMessage/>
      </template>
      <template v-else>
        <p class="text-center text-18px">真可怜，一个留言都没有....</p>
      </template>
    </template>
    <template v-else>
      <div class="message-content grid gap-y-15px gap-x-28px">
        <Skeleton customize-class="!w-full !h-140px" v-for="i in 10"/>
      </div>
    </template>
  </BoxComponent>
</template>

<style scoped>

.theme-check {
  @apply 'block cursor-pointer w-20px h-20px rounded-5px relative overflow-hidden border-3px border-solid border-opacity-0 border-[#ffffff]';
}

.theme-check span {
  @apply 'w-30px h-30px -top-52px -left-52px absolute z-1';
  transform: rotateZ(45deg);
}

.theme-check input[type=radio]:checked + span {
  @apply '-left-10px -top-10px';
}

.theme-check input[type=radio] {
  @apply 'absolute left-50px invisible';
}

.transition {
  transition: 300ms ease;
}

/* default */
.theme-check:first-child {
  box-shadow: 0px 0px 0px 2px #1e90ff;
}
.theme-check:first-child > span {
  @apply 'bg-[#1e90ff]'
}

/* orange */
.theme-check:nth-child(2) {
  box-shadow: 0px 0px 0px 2px #ffa502;
}
.theme-check:nth-child(2) > span {
  @apply 'bg-[#ffa502]';
}

/* tomato */
.theme-check:nth-child(3) {
  box-shadow: 0px 0px 0px 2px #ff6348;
}
.theme-check:nth-child(3) > span {
  @apply 'bg-[#ff6348]';
}

/* FlamingoPink */
.theme-check:nth-child(4) {
  box-shadow: 0px 0px 0px 2px #f78fb3;
}
.theme-check:nth-child(4) > span {
  @apply 'bg-[#f78fb3]';
}

/* watermelon */
.theme-check:nth-child(5) {
  box-shadow: 0px 0px 0px 2px #ff4757;
}
.theme-check:nth-child(5) > span {
  @apply 'bg-[#ff4757]';
}

/* PrestigeBlue */
.theme-check:nth-child(6) {
  box-shadow: 0px 0px 0px 2px #2f3542;
}
.theme-check:nth-child(6) > span {
  @apply 'bg-[#2f3542]';
}

/* UfoGreen */
.theme-check:nth-child(7) {
  box-shadow: 0px 0px 0px 2px #2ed573;
}
.theme-check:nth-child(7) > span {
  @apply 'bg-[#2ed573]';
}

/* BrightGreek */
.theme-check:nth-child(8) {
  box-shadow: 0px 0px 0px 2px #3742fa;
}
.theme-check:nth-child(8) > span {
  @apply 'bg-[#3742fa]';
}

/* wisteria */
.theme-check:nth-child(9) {
  box-shadow: 0px 0px 0px 2px #8e44ad;
}
.theme-check:nth-child(9) > span {
  @apply 'bg-[#8e44ad]';
}

/* 留言评论框 */
.message-input {
  @apply 'max-w-260px w-full h-35px border-2px border-solid border-transparent outline-none bg-$theme-bg p-5px tracking-1px text-16px';
  border-bottom: 2px solid var(--theme-bg-reverse);
  caret-color: var(--theme-bg-reverse);
  transition: var(--theme-transition-bg), border var(--transition);
}

/* default */
.message-input.default:focus {
  @apply 'border-2px border-solid border-[#1e90ff] text-[#1e90ff]';
  caret-color: #1e90ff;
  box-shadow: 4px 4px 10px #1e90ff;
}

.message-input.default:focus::placeholder,
.message-input.default::placeholder{
  @apply 'text-[#1e90ff]';
}

/* orange */
.message-input.orange:focus {
  @apply 'border-2px border-solid border-[#ffa502] text-[#ffa502]';
  caret-color: #ffa502;
  box-shadow: 4px 4px 10px #ffa502;
}

.message-input.orange:focus::placeholder,
.message-input.orange::placeholder{
  @apply 'text-[#ffa502]';
}

/* tomato */
.message-input.tomato:focus {
  @apply 'border-2px border-solid border-[#ff6348] text-[#ff6348]';
  caret-color: #ff6348;
  box-shadow: 4px 4px 10px #ff6348;
}
.message-input.tomato:focus::placeholder,
.message-input.tomato::placeholder {
  @apply 'text-[#ff6348]';
}

/* FlamingoPink */
.message-input.FlamingoPink:focus {
  @apply 'border-2px border-solid border-[#f78fb3] text-[#f78fb3]';
  border: 2px solid #f78fb3;
  caret-color: #f78fb3;
  color: #f78fb3;
  box-shadow: 4px 4px 10px #f78fb3;
}
.message-input.FlamingoPink:focus::placeholder,
.message-input.FlamingoPink::placeholder {
  @apply 'text-[#f78fb3]';
}

/* watermelon */
.message-input.watermelon:focus {
  @apply 'border-2px border-solid border-[#ff4757] text-[#ff4757]';
  caret-color: #ff4757;
  box-shadow: 4px 4px 10px #ff4757;
}
.message-input.watermelon:focus::placeholder,
.message-input.watermelon::placeholder {
  @apply 'text-[#ff4757]';
}

/* PrestigeBlue */
.message-input.PrestigeBlue:focus {
  @apply 'border-2px border-solid border-[#2f3542] text-[#2f3542]';
  caret-color: #2f3542;
  box-shadow: 4px 4px 10px #2f3542;
}
.message-input.PrestigeBlue:focus::placeholder,
.message-input.PrestigeBlue::placeholder {
  @apply 'text-[#2f3542]';
}

/* UfoGreen */
.message-input.UfoGreen:focus {
  @apply 'border-2px border-solid border-[#2ed573] text-[#2ed573]';
  caret-color: #2ed573;
  box-shadow: 4px 4px 10px #2ed573;
}
.message-input.UfoGreen:focus::placeholder,
.message-input.UfoGreen::placeholder {
  @apply 'text-[#2ed573]';
}

/* BrightGreek */
.message-input.BrightGreek:focus {
  @apply 'border-2px border-solid border-[#3742fa] text-[#3742fa]';
  caret-color: #3742fa;
  box-shadow: 4px 4px 10px #3742fa;
}
.message-input.BrightGreek:focus::placeholder,
.message-input.BrightGreek::placeholder {
  @apply 'text-[#3742fa]';
}

/* wisteria */
.message-input.wisteria:focus {
  @apply 'border-2px border-solid border-[#8e44ad] text-[#8e44ad]';
  caret-color: #8e44ad;
  box-shadow: 4px 4px 10px #8e44ad;
}
.message-input.wisteria:focus::placeholder,
.message-input.wisteria::placeholder {
  @apply 'text-[#8e44ad]';
}

/* 按钮 */
.message-btn button span {
  box-shadow: 0 0.4em 0.1em 0.019em var(--theme-bg);
}

.message-btn button span:hover {
  transition: all 0.5s;
  transform: translate(0, 0.3em);
  box-shadow: 0 0 0 0 var(--text-color-reverse);
}

.message-btn button span:not(hover) {
  transition: all .8s;
}

.message-content {
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
}

.pager {
  @apply 'flex justify-center items-center relative w-full h-full rounded-3px';
  box-shadow: 2px 2px 5px rgba(0, 0, 0, .2);
}

.pager::after {
  @apply 'w-full h-full';
  content: "";
  position: absolute;
}

.pager:before {
  @apply 'absolute top-0 w-full h-full opacity-20 bg-cover bg-no-repeat bg-center bg-origin-border';
  content: "";
  background-image: url("https://apecode.oss-cn-chengdu.aliyuncs.com/config/237ae48b0c702d50d0bb02353ecb453a.png");
}

.pager.default {
  @apply 'bg-[#1e90ff]';
}

.pager.orange {
  @apply 'bg-[#ffa502]';
}

.pager.FlamingoPink {
  @apply 'bg-[#f78fb3]';
}

.pager.tomato {
  @apply 'bg-[#ff6348]';
}

.pager.watermelon {
  @apply 'bg-[#ff4757]';
}

.pager.PrestigeBlue {
  @apply 'bg-[#2f3542]';
}

.pager.UfoGreen {
  @apply 'bg-[#2ed573]';
}

.pager.BrightGreek {
  @apply 'bg-[#3742fa]';
}

.pager.wisteria {
  @apply 'bg-[#8e44ad]';
}

.tape-corner::before,
.tape-corner::after {
  @apply 'w-35px h-20px absolute opacity-60 rounded-2px';
  content: "";
  background-color: var(--theme-translucent-reverse);
  border-left: 2px dotted var(--theme-translucent);
  border-right: 2px dotted var(--theme-translucent);
  transition: border var(--transition), var(--theme-transition-bg);
}

.tape-corner:first-of-type:before {
  @apply '-top-6px -right-13px';
  transform: rotate(45deg);
}

.tape-corner:first-of-type:after {
  @apply '-top-6px -left-13px';
  transform: rotate(-45deg);
}

.tape-corner:last-of-type:before {
  @apply '-bottom-6px -right-13px';
  transform: rotate(-45deg);
}

.tape-corner:last-of-type:after {
  @apply '-bottom-6px -left-13px';
  transform: rotate(45deg);
}

.message-text {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>