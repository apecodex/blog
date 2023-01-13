import type { Ref } from 'vue';
import { Names } from './store-name'
import {defineStore} from "pinia";
import {ref} from "vue";

export const useMenuStore = defineStore(Names.MENU, () => {

  let menus: Ref<Array<UserMenuBackModel> | null> = ref<Array<UserMenuBackModel> | null>(null);

  // 保存菜单
  const saveMenu = (menu: Array<UserMenuBackModel>) => {
    menus.value = menu
  };

  // 清除菜单
  const clearMenu = () => {
    menus.value = null
  }

  return { menus, saveMenu, clearMenu }
}, { persist: { storage: sessionStorage } })