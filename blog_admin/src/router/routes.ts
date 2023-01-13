import { RouteRecordRaw } from 'vue-router';
import PageLayout from '../layout/PageLayout.vue';

export const constantRoutes: RouteRecordRaw[] = [
  {
    path: '/pageLayout',
    name: 'root',
    component: PageLayout,
    redirect: '/home',
    children: []
  },
  {
    path: '/login',
    name: "Login",
    component: () => import("~/views/login/Login.vue"),
    meta: {
      title: "后台登录"
    }
  }
]

