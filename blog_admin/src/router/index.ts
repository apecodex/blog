import type { App } from 'vue'
import type { NavigationGuardNext, RouteLocationNormalized, Router } from 'vue-router'
import { createRouter, createWebHistory } from 'vue-router'
import { constantRoutes } from './routes'
import { useMenuStore, useUserInfoStore } from '~/store'

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes,
  scrollBehavior: () => ({ left: 0, top: 0 })
})

export function setupRouter(app: App<Element>) {
  app.use(router)
}
const modules = import.meta.glob('~/views/*/*.vue');

export function setupRouterGuard(router: Router) {
  router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    (<any>window).$loadingBar.start()
    const menuStore = useMenuStore();
    const userInfoStore = useUserInfoStore();
    // // 已登录用户访问登录页面就跳转至首页
    if (to.path === "/login" && userInfoStore.userInfo !== null) {
      next({ path: '/' })
    }
    // 菜单数据
    const menus = menuStore.menus as UserMenuBackModel[];
    // 动态添加路由
    if (router.getRoutes().length <= 2 && menus !== null) {
      for (const menu of menus) {
        if (menu.component === "Layout") {
          if (menu.children?.length !== 0) {
            for (const childMenu of menu.children as UserMenuBackModel[]) {
              router.addRoute('root', {
                path: childMenu.path,
                name: childMenu.name,
                component: modules[`/src/views${childMenu.component}`],
                meta: {
                  title: childMenu.title,
                  icon: childMenu.icon
                }
              })
            }
          }
        } else {
          router.addRoute('root', {
            path: menu.path,
            name: menu.name,
            component: modules[`/src/views${menu.component}`],
            meta: {
              title: menu.title,
              icon: menu.icon
            }
          })
        }
      }
      // 404页面
      router.addRoute('root', {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import("~/views/error/PageNotFound.vue"),
        meta: {
          title: "404",
          icon: 'error'
        }
      })
      next({ ...to, replace: true });
    } else {
      next();
    }
  })

  router.afterEach((to: RouteLocationNormalized, from: RouteLocationNormalized) => {
    document.title = to.meta.title as string || "加载中~";
    window.onblur = () => {
      document.title = "(๑´0`๑)不要走太远哦~"
    }
    window.onfocus = () => {
      document.title = to.meta.title as string;
    }
    (<any>window).$loadingBar.finish();
  })

  router.onError((error) => {
    (<any>window).$message.error(error.message);
    (<any>window).$loadingBar.error();
  })
}