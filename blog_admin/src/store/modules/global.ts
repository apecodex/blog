import type { Ref } from 'vue';
import { Names } from './store-name'
import {defineStore} from "pinia"
import {ref, nextTick} from "vue"

export const useGlobalStore = defineStore(Names.GLOBAL, () => {
  let isRouterAlive: Ref<boolean> = ref<boolean>(true)

  // 刷新局部组件
  const reload = async () => {
    isRouterAlive.value = false
    await nextTick(() => {
      isRouterAlive.value = true
    })
  }

  return {
    isRouterAlive,
    reload
  }
})