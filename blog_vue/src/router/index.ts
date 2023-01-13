import {App} from 'vue'
import {createRouter, createWebHistory, NavigationGuardNext, RouteLocationNormalized} from "vue-router";
import {constantRoutes} from './routers'
import {scrollSmoothTo} from "@/utils/utils";
import {useUserInfoStore, useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"


export const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: constantRoutes,
    scrollBehavior: (to: RouteLocationNormalized, from :RouteLocationNormalized, savedPosition) => {
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
    if (to.meta?.isLogin) {
        const userInfoStore = useUserInfoStore();
        const settingStore = useSettingStore();
        const {loginFlag} = storeToRefs(settingStore);
        if (!userInfoStore.isLogin) {
            settingStore.closeModal();
            loginFlag.value = true;
            next({path: '/'});
            notify({
                text: "请先登录",
                type: "warn"
            })
            return;
        }
    }
    next();
})

router.afterEach( (to: RouteLocationNormalized, from: RouteLocationNormalized) => {
    (document as any).title = to.meta.title || "Blog";
})