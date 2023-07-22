import { App } from 'vue'
import { createRouter, createWebHistory, NavigationGuardNext, RouteLocationNormalized } from "vue-router";
import { constantRoutes } from './routers'
import { scrollSmoothTo } from "@/utils/utils";
import { useUserInfoStore, useSettingStore, useWebSocketStore, useWebsiteInfoStore } from "@/store"
import { storeToRefs } from "pinia";
import { notify } from "@kyvg/vue3-notification"


export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes,
  scrollBehavior: (to: RouteLocationNormalized, from: RouteLocationNormalized, savedPosition) => {
    if (savedPosition) {
      // 滚动到刷新前的位置
      scrollSmoothTo(savedPosition.top)
    } else {
      return { x: 0, y: 0 } as ScrollOptions
    }
  }
})

export function setupRouter(app: App<Element>) {
  app.use(router)
}

router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const userInfoStore = useUserInfoStore();
  const { socket, socketConnect } = useWebSocketStore();

  if (to.meta?.isLogin) {
    const settingStore = useSettingStore();
    const { loginFlag } = storeToRefs(settingStore);
    if (!userInfoStore.isLogin) {
      settingStore.closeModal();
      loginFlag.value = true;
      next({ path: '/' });
      notify({
        text: "请先登录",
        type: "warn"
      })
      return;
    }
  }
  // 连接WebSocket
  if (!socket.stompClient.connected) {
    socketConnect();
  }
  next();
})

router.afterEach((to: RouteLocationNormalized, from: RouteLocationNormalized) => {
  const websiteInfoStore = useWebsiteInfoStore();
  const { websiteInfo } = storeToRefs(websiteInfoStore);
  const webTitle = to.meta.title + " の " + websiteInfo.value?.websiteName;
  document.title = webTitle || "加载中~";
  window.onblur = () => {
    document.title = "(๑´0`๑)不要走太远哦~"
  }
  window.onfocus = () => {
    document.title = webTitle;
  }
})