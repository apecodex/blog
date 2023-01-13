<script setup lang='ts'>
import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import EmojiExtension from '~/components/EmojiExtension/index.vue'
import DescSetting from "~/components/DescSetting.vue"
import {useSettingStore} from '~/store';
import {toolbars} from '~/components/EmojiExtension/staticConfig';
import {statusOptions, typeOptions} from '~/components/common/constant'
import {useRoute} from 'vue-router'
import {
  NSpace,
  NDrawer,
  NDrawerContent,
  NButton,
  NInput,
  NSelect,
  NSwitch,
  NDivider,
  NDynamicTags,
  NUpload
} from "naive-ui"
import {
  showSaveArticleDrawer,
  articleInfo,
  articleInfoStatus,
  categoryOptions,
  categoryOptionLoading,
  initCategoryOption,
  categorySearch,
  fileList,
  uploadCover,
  uploadImg,
  handleRemove,
  clearArticleInfo,
  saveOrUpdateArticleFunc,
  saveDraftArticleFunc,
  pullArticleById
} from './data/useArticle'
import {storeToRefs} from "pinia";
import {watch, onMounted} from "vue";

const settingStore = useSettingStore()
const {editorTheme} = storeToRefs(settingStore)
const editorId = 'editor-preview';
const insertEmoji = (v: string) => (articleInfo.articleContent = v);
const route = useRoute()

// 判断是否带有文章id
const checkHasArticleId = () => {
  if (route.params.articleId) {
    const articleId = route.params.articleId as string;
    pullArticleById(articleId);
  } else {
    delete articleInfo.id
    clearArticleInfo();
  }
}

watch(() => route.params.articleId, () => {
  checkHasArticleId()
})

onMounted(() => {
  checkHasArticleId()
})

</script>

<template>
  <n-space vertical>
    <md-editor v-model="articleInfo.articleContent" :theme="editorTheme" :editor-id="editorId"
               :toolbars="toolbars" @on-save="showSaveArticleDrawer = true" code-theme="github"
               placeholder="写点什么..."
               show-code-row-number style="height: 84vh;overflow: hidden;" @on-upload-img="uploadImg">
      <template #defToolbars>
        <emoji-extension :editor-id="editorId" @on-change="insertEmoji"/>
      </template>
    </md-editor>
    <n-drawer v-model:show="showSaveArticleDrawer" :width="320">
      <n-drawer-content closable :native-scrollbar="false" footer-style="justify-content: space-around">
        <template #header>
          <n-space align="baseline">
            <span>文章信息</span>
            <n-button size="small" strong dashed @click="clearArticleInfo">
              重置
            </n-button>
          </n-space>
        </template>
        <n-space vertical>
          <div>
            <n-input v-model:value="articleInfo.articleTitle" size="small" clearable
                     :status="articleInfoStatus.articleTitleStatus ? 'error' : 'success'" type="text"
                     placeholder="文章标题"/>
            <span class="text-tip" v-show="articleInfoStatus.articleTitleStatus">文章标题不可为空</span>
          </div>
          <div>
            <n-select v-model:value="articleInfo.categoryName" filterable
                      :status="articleInfoStatus.categoryStatus ? 'error' : 'success'"
                      placeholder="选择分类"
                      label-field="name"
                      value-field="name"
                      :loading="categoryOptionLoading"
                      :options="categoryOptions"
                      :consistent-menu-width="false"
                      clearable
                      remote @search="categorySearch" @focus="initCategoryOption()"/>
            <span class="text-tip" v-show="articleInfoStatus.categoryStatus">文章分类不可为空</span>
          </div>
          <desc-setting title="是否顶置">
            <n-switch :round="false" v-model:value="articleInfo.isTop">
              <template #checked>
                不顶置
              </template>
              <template #unchecked>
                顶置
              </template>
            </n-switch>
          </desc-setting>
          <desc-setting title="文章状态">
            <n-select class="w-1/2" v-model:value="articleInfo.status" size="small" :options="statusOptions"
                      :consistent-menu-width="false"/>
          </desc-setting>
          <desc-setting title="文章类型">
            <n-select class="w-1/2" v-model:value="articleInfo.type" size="small" :options="typeOptions"
                      :consistent-menu-width="false"/>
          </desc-setting>
          <div v-if="articleInfo.type === 2">
            <n-input v-model:value="articleInfo.originalUrl" size="small"
                     :status="articleInfoStatus.originalUrlStatus ? 'error' : 'success'" type="text"
                     placeholder="原文链接"/>
            <span class="text-tip" v-show="articleInfoStatus.originalUrlStatus">原文链接不可为空</span>
          </div>
          <n-divider>标签</n-divider>
          <div>
            <n-dynamic-tags style="margin-top:0;margin-button:0" v-model:value="articleInfo.tagNameList"/>
            <span class="text-tip" v-show="articleInfoStatus.tagsStatus">至少需要一个标签</span>
          </div>
          <n-divider>文章封面</n-divider>
          <n-space align="center" vertical>
            <n-upload :max="1" :custom-request="uploadCover" @remove="handleRemove"
                      :default-file-list="fileList" list-type="image-card">
            </n-upload>
            <span class="text-tip" v-show="articleInfoStatus.articleCoverStatus">需上传文章封面</span>
          </n-space>
        </n-space>
        <template #footer>
          <n-button strong tertiary type="info" @click="saveDraftArticleFunc">
            保存为草稿
          </n-button>
          <n-button strong tertiary type="success" @click="saveOrUpdateArticleFunc">
            保存
          </n-button>
        </template>
      </n-drawer-content>
    </n-drawer>
  </n-space>
</template>

<style scoped>
.md-editor-dark {
  --md-bk-color: rgb(24, 24, 28)
}

/* 修改分割线margin */
.n-divider:not(.n-divider--vertical) {
  margin-top: 0 !important;
  margin-bottom: 0 !important;
}

.text-tip {
  @apply text-red-600 text-12px
}
</style>