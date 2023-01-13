// 全局样式
import './style/base.css'
import '@icon-park/vue-next/styles/index.css';

import { createApp } from 'vue'
import App from './App.vue'
import { setupRouter, setupRouterGuard, router } from '~/router';
import { setupStore } from '~/store'
import { setupECharts } from './echarts';

const app = createApp(App)
setupRouter(app)
setupRouterGuard(router)
setupStore(app)
setupECharts(app)
import 'virtual:windi.css'
import 'virtual:windi-devtools'
app.mount('#app')
