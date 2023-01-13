import type { Ref } from 'vue';
import { Names } from './store-name'
import {defineStore} from "pinia";
import {ref} from "vue";

export const useWebsiteStore = defineStore(Names.WEBSITE, () => {
  let noReadNoticeCount: Ref<number> = ref<number>(0);

  return {
    noReadNoticeCount,
  }
})