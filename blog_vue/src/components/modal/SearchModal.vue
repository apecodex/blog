<script setup lang='ts'>
import type {Ref} from "vue";
import Modal from "./Modal.vue";
import BoxComponent from "@/components/BoxComponent.vue";
import {DoubleDown, DoubleUp} from '@icon-park/vue-next';
import {useSettingStore} from '@/store';
import {storeToRefs} from "pinia";
import {ref} from "vue";
import {notify} from "@kyvg/vue3-notification"
import {search} from "@/api/requests/Article";
import {debounce} from "@/utils/utils";

const settingStore = useSettingStore();
const {searchFlag, loadingFlag} = storeToRefs(settingStore);

const keywords: Ref<string> = ref("");
// 文章结果
const expandArticleClassActive: Ref<Boolean> = ref(false);
// 分类结果
const expandCategoryClassActive: Ref<Boolean> = ref(false);
// 标签结果
const expandTagClassActive: Ref<Boolean> = ref(false);
// 搜索结果
const searchData: Ref<SearchEntity | null> = ref(null);

// 展开文章搜索结果
const expandArticleHandle = () => {
  expandArticleClassActive.value = !expandArticleClassActive.value;
  expandCategoryClassActive.value = false;
  expandTagClassActive.value = false;
}

const expandCategoryHandle = () => {
  expandCategoryClassActive.value = !expandCategoryClassActive.value;
  expandArticleClassActive.value = false;
  expandTagClassActive.value = false;
}

const expandTagHandle = () => {
  expandTagClassActive.value = !expandTagClassActive.value;
  expandArticleClassActive.value = false;
  expandCategoryClassActive.value = false;
}

const createSearchData = async ({keywords}: ConditionParams) => {
  searchData.value = null;
  loadingFlag.value = true;
  await search({keywords}).then((resp) => {
    if (resp.status) {
      searchData.value = resp.data;
    }
  }).catch(() => {
    notify({
      text: "搜索失败，请重试",
      type: "warn"
    })
  }).finally(() => {
    loadingFlag.value = false;
  })
}

const searchHandle = () => {
  if (keywords.value.length !== 0) {
    createSearchData({keywords: keywords.value})
  } else {
    searchData.value = null;
  }
}

const searchDebounce = debounce(searchHandle, 500);

// 点击跳转
const jumpHandle = () => {
  settingStore.closeModal();
  closeHandle();
}

// 关闭搜索框
const closeHandle = () => {
  keywords.value = ""
  searchData.value = null
}
</script>

<template>
  <Modal v-model:show="searchFlag" :title="`搜索 ${searchData?.count ? '('+ searchData.count +')' : ''}`"
         custom-class="!max-w-460px overflow-y-scroll" @closeModal="closeHandle">
    <div class="search-modal flex w-full rounded-6px flex flex-col mt-15px ml-3px">
      <input type="text"
             placeholder="想搜点什么呢？"
             v-model="keywords"
             @input="searchDebounce"
             class="border-none w-full h-30px outline-none bg-transparent text-$text-color indent-7px text-16px">
      <hr class="hr-twill my-10px">
      <div class="" v-if="searchData">
        <div class="flex flex-col gap-15px" v-if="searchData.count !== 0">
          <BoxComponent class="p-5px" v-if="searchData.articleSearch.length !== 0">
            <div class="flex justify-between items-center" @click="expandArticleHandle">
              <span class="text-16px">文章</span>
              <span>{{ searchData.articleSearch.length }}条</span>
              <DoubleUp v-if="expandArticleClassActive" size="22"/>
              <DoubleDown v-else size="22"/>
            </div>
            <hr v-show="expandArticleClassActive" class="hr-twill">
            <div class="result-item result-article overflow-y-scroll"
                 :class="{'expand-active': expandArticleClassActive}">
              <div v-if="searchData.articleSearch.length !== 0" class="flex flex-col gap-15px">
                <router-link :to="`/article/${articleItem.id}`"
                             @click="jumpHandle"
                             class="flex flex-col gap-10px cursor-pointer p-5px border-1px border-$theme-bg-reverse border-dashed rounded-6px"
                             v-for="articleItem of searchData.articleSearch" :key="articleItem.id">
                  <h2 class="text-16px" v-html="articleItem.articleTitle"></h2>
                  <p class="leading-[1.1] p-5px shadow-inset rounded-6px"
                     v-html="articleItem.articleContent"></p>
                </router-link>
              </div>
              <div v-else class="text-center">未找到</div>
            </div>
          </BoxComponent>
          <BoxComponent class="p-5px" v-if="searchData.categorySearch.length !== 0">
            <div class="flex justify-between items-center" @click="expandCategoryHandle">
              <span class="text-16px">分类</span>
              <span>{{ searchData.categorySearch.length }}条</span>
              <DoubleUp v-if="expandCategoryClassActive" size="22"/>
              <DoubleDown v-else size="22"/>
            </div>
            <hr v-show="expandCategoryClassActive" class="hr-twill">
            <div class="result-item overflow-y-scroll flex flex-wrap gap-10px"
                 :class="{'expand-active': expandCategoryClassActive}">
              <router-link :to="`/categories/${categoryItem.id}`" @click="jumpHandle"
                           v-for="categoryItem of searchData.categorySearch" :key="categoryItem.id"
                           v-html="categoryItem.name"></router-link>
            </div>
          </BoxComponent>
          <BoxComponent class="p-5px" v-if="searchData.tagSearch.length !== 0">
            <div class="flex justify-between items-center" @click="expandTagHandle">
              <span class="text-16px">标签</span>
              <span>{{searchData.tagSearch.length}}条</span>
              <DoubleUp v-if="expandTagClassActive" size="22"/>
              <DoubleDown v-else size="22"/>
            </div >
            <hr v-show="expandTagClassActive" class="hr-twill">
            <div class="result-item overflow-y-scroll flex flex-wrap gap-10px" :class="{'expand-active': expandTagClassActive}">
              <router-link :to="`/tags/${tagItem.id}`" @click="jumpHandle"
                           v-for="tagItem of searchData.tagSearch" :key="tagItem.id"
                           v-html="tagItem.name"></router-link>
            </div>
          </BoxComponent>
        </div>
        <div class="text-center" v-else>啥也没找到~</div>
      </div>
      <div class="text-center text-16px" v-show="loadingFlag">
        拼命搜索中...
      </div>
    </div>
  </Modal>
</template>

<style scoped>

/* 搜索 */
.search-modal input {
  background-color: var(--theme-bg);
  border-radius: 7px 20px 20px 20px;
  box-shadow: var(--theme-shadow);
}

.search-modal input:focus {
  box-shadow: var(--theme-shadow-inset);
}

/*搜索结果*/
.result-item {
  width: 100%;
  max-height: 0;
  transition: all .3s;
}

.result-item.expand-active {
  max-height: 360px;
}
</style>