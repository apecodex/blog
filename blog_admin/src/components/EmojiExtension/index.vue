<template>
  <dropdown-toolbar title="emoji" :visible="state.visible" @on-change="onChange">
    <template #overlay>
      <div class="border-1px p-5px rounded-6px">
        <n-tabs type="line" trigger="hover" :bar-width="48" animated>
          <n-tab-pane name="smileys" tab="😀">
            <n-scrollbar style="max-height: 120px; width: 250px">
              <n-space>
              <span class="emojis" v-for="(emoji, index) in smileys" :key="index" @click="emojiHandler(emoji)">{{
                  emoji
                }}</span>
              </n-space>
            </n-scrollbar>
          </n-tab-pane>
          <n-tab-pane name="food" tab="🍎">
            <n-scrollbar style="max-height: 120px; width: 250px">
              <n-space>
              <span class="emojis" v-for="(emoji, index) in foods" :key="index" @click="emojiHandler(emoji)">{{
                  emoji
                }}</span>
              </n-space>
            </n-scrollbar>
          </n-tab-pane>
          <n-tab-pane name="jay chou" tab="🧳">
            <n-scrollbar style="max-height: 120px; width: 250px">
              <n-space>
              <span class="emojis" v-for="(emoji, index) in clothing" :key="index" @click="emojiHandler(emoji)">{{
                  emoji
                }}</span>
              </n-space>
            </n-scrollbar>
          </n-tab-pane>
          <n-tab-pane name="animal" tab="🦁">
            <n-scrollbar style="max-height: 120px; width: 250px">
              <n-space>
              <span class="emojis" v-for="(emoji, index) in animals" :key="index" @click="emojiHandler(emoji)">{{
                  emoji
                }}</span>
              </n-space>
            </n-scrollbar>
          </n-tab-pane>
          <n-tab-pane name="nature" tab="🌞">
            <n-scrollbar style="max-height: 120px; width: 250px">
              <n-space>
              <span class="emojis" v-for="(emoji, index) in nature" :key="index" @click="emojiHandler(emoji)">{{
                  emoji
                }}</span>
              </n-space>
            </n-scrollbar>
          </n-tab-pane>
        </n-tabs>
      </div>
    </template>
    <template #trigger>
      <smiling-face theme="outline"/>
    </template>
  </dropdown-toolbar>
</template>

<script lang="ts" setup>
import {reactive} from 'vue';
import type {PropType} from 'vue';
import {SmilingFace} from '@icon-park/vue-next';
import {smileys, foods, clothing, animals, nature} from "~/utils/emojis";
import MdEditor from 'md-editor-v3';
import {
  NTabs,
  NTabPane,
  NScrollbar,
  NSpace,
} from "naive-ui";

const DropdownToolbar = MdEditor.DropdownToolbar;
const props = defineProps({
  editorId: {
    type: String as PropType<string>,
    default: ''
  }
});
const emit = defineEmits(['onChange']);
const state = reactive({
  visible: false
});
const emojiHandler = (emoji: string) => {
  // 获取输入框
  const textarea = document.querySelector(
      `#${props.editorId}-textarea`
  ) as HTMLTextAreaElement;
  // 获取选中的内容
  const selection = window.getSelection()?.toString();
  // 获取鼠标位置
  const endPoint = textarea.selectionEnd;
  // 根据鼠标位置分割旧文本
  // 前半部分
  const prefixStr = textarea.value.substring(0, endPoint);
  // 后半部分
  const suffixStr = textarea.value.substring(endPoint + (selection?.length || 0));
  emit('onChange', `${prefixStr}${emoji}${suffixStr}`);
  setTimeout(() => {
    const lastPoint = endPoint + emoji.length
    textarea.setSelectionRange(lastPoint, lastPoint);
    textarea.focus();
  }, 0);
};
const onChange = (visible: boolean) => {
  state.visible = visible;
};
</script>

<script lang="ts">
export default {
  name: 'EmojiExtension'
};
</script>
<style scoped>
.emojis {
  @apply text-21px flex border border-transparent rounded-5px w-30px h-30px align-center items-center justify-center hover:(border-dark-500 bg-light-50 transform scale-120 transition-all)
}
</style>