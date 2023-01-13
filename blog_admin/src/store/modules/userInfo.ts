import type {Ref} from 'vue';
import {Names} from './store-name'
import {useMenuStore} from './menu';
import {useWebsiteStore} from '~/store'
import {router} from '~/router';
import {defineStore} from "pinia";
import {ref} from "vue";

export const useUserInfoStore = defineStore(Names.USERINFO, () => {
  let userInfo: Ref<UserInfoModel | null> = ref<UserInfoModel | null>(null);

  // 保存用户信息
  const saveUserInfo = (userInfoData: UserInfoModel) => {
    userInfo.value = userInfoData
  }

  // 获取用户信息
  const getUserInfo = () => {
    return userInfo.value
  }

  // 返回Token
  const getToken = () => {
    return userInfo.value?.tokenHead + " " + userInfo.value?.token;
  }

  // 判断Token是否有
  const hasToken = () => {
    return userInfo.value?.token.length !== 0
  }

  // 删除用户信息
  const removeUserInfo = () => {
    userInfo.value = null
  }

  // 退出
  const logout = async () => {
    const menuStore = useMenuStore();
    removeUserInfo();
    menuStore.clearMenu();
    // 将未阅读消息清零
    useWebsiteStore().noReadNoticeCount = 0
    await router.replace({ path: '/login' });
    (<any>window).$message.success("退出成功")
  }

  return {
    userInfo,
    saveUserInfo,
    getUserInfo,
    getToken,
    hasToken,
    removeUserInfo,
    logout
  }
}, { persist: { storage: sessionStorage } })