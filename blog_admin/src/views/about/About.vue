<script setup lang='ts'>
import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import EmojiExtension from '~/components/EmojiExtension/index.vue'
import {toolbars} from '~/components/EmojiExtension/staticConfig';
import {useSettingStore} from "~/store";
import {uploadImg} from '~/views/article/data/useUpload'
import {
  aboutMeData, createAboutMeData,
  updateAboutMeHandle
} from './data/useAbout'
import {NSpace} from "naive-ui";
import {storeToRefs} from "pinia";
import {onMounted} from "vue";

const settingStore = useSettingStore()
const {editorTheme} = storeToRefs(settingStore)
const aboutMeEditorId = 'about-me-editor-preview';
const insertEmoji = (v: string) => (aboutMeData.value = v);

onMounted(() => {
  createAboutMeData()
})
</script>

<template>
  <n-space vertical>
    <md-editor v-model="aboutMeData" :theme="editorTheme" :editor-id="aboutMeEditorId"
               :toolbars="toolbars" @on-save="updateAboutMeHandle" code-theme="github"
               placeholder="写点什么..."
               show-code-row-number style="height: 84vh;overflow: hidden;" @on-upload-img="uploadImg">
      <template #defToolbars>
        <emoji-extension :editor-id="aboutMeEditorId" @on-change="insertEmoji"/>
      </template>
    </md-editor>
  </n-space>
</template>

<style scoped>
.md-editor-dark {
  --md-bk-color: rgb(24, 24, 28)
}
</style>