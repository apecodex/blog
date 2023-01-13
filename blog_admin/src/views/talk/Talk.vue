<script setup lang='ts'>
import {UploadPicture} from '@icon-park/vue-next'
import {statusOptions} from '~/components/common/constant'
import {
  saveTalk,
  talkForm,
  clearTalkForm,
  previewFileList,
  uploadTalkPictureOrVideo,
  disabledSendBtn,
  handleRemove,
  pullTalkById
} from './data/useTalk'
import {
  NSpace,
  NSwitch,
  NSelect,
  NUpload,
  NButton
} from "naive-ui"
import CommentComp from "~/components/comment/CommentComp.vue"
import {useRoute, useRouter} from "vue-router";
import {onMounted, watch} from "vue";

const route = useRoute()
const router = useRouter();

const checkHasTalkId = () => {
  if (route.params.talkId) {
    const talkId = route.params.talkId as string;
    pullTalkById(talkId)
  } else {
    delete talkForm.id
    clearTalkForm();
  }
}

watch(() => route.params.talkId, () => {
  checkHasTalkId();
})

onMounted(() => {
  checkHasTalkId();
})

</script>

<template>
  <n-space vertical>
    <comment-comp v-model:default-value="talkForm.content" @enter="saveTalk" placeholder="这一刻的想法..."
                  enter-btn-text="发布" :min-rows="4" :max-rows="4" :disabled="disabledSendBtn">
      <template #other>
        <n-space align="baseLine">
          <n-switch :round="false" v-model:value="talkForm.isTop" size="small">
            <template #checked>
              不顶置
            </template>
            <template #unchecked>
              顶置
            </template>
          </n-switch>
          <n-select v-model:value="talkForm.status" size="tiny" :options="statusOptions"
                    :consistent-menu-width="false" placeholder="说说状态"/>
        </n-space>
      </template>
    </comment-comp>
    <n-upload
        :default-file-list="previewFileList"
        list-type="image-card"
        multiple
        :custom-request="uploadTalkPictureOrVideo"
        @remove="handleRemove"
    >
      <n-button text circle>
        <template #icon>
          <upload-picture theme="outline" size="28"/>
        </template>
      </n-button>
    </n-upload>
  </n-space>
</template>

<style scoped>
</style>