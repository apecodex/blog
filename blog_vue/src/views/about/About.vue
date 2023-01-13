<script setup lang='ts'>
import {onMounted} from "vue";
import MdEditor from 'md-editor-v3';
import {useSettingStore} from "@/store"
import {
  aboutData,
  createAboutData
} from "./AboutHooks"
import BoxComponent from "@/components/BoxComponent.vue";
import Skeleton from "@/components/skeleton/Skeleton.vue";
import {storeToRefs} from "pinia";

const settingStore = useSettingStore();
const {previewTheme, codeTheme, showCodeRowNumber} = storeToRefs(settingStore);

onMounted(() => {
  createAboutData();
})
</script>

<template>
  <BoxComponent>
    <template v-if="aboutData">
      <md-editor v-model="aboutData" editorId="my-editor" preview-only
                 :preview-theme="previewTheme"
                 :theme="settingStore.theme"
                 :code-theme="codeTheme"
                 :showCodeRowNumber="showCodeRowNumber"/>
    </template>
    <template v-else>
      <div class="p-10px">
        <Skeleton customize-class="!w-full !h-300px"/>
      </div>
    </template>
  </BoxComponent>
</template>

<style scoped>
.md {
  @apply 'bg-$theme-bg rounded-8px px-5px';
  transition: var(--theme-transition), var(--theme-transition-fill), border var(--transition);
}
</style>