import {createApp} from 'vue'
import '@/assets/css/base.css'
import App from './App.vue'
import {setupStore} from '@/store'
import {setupRouter} from '@/router'
import {setupDirective} from "@/directives"
import Notifications from '@kyvg/vue3-notification'
import 'virtual:windi.css'
import 'virtual:windi-devtools'

const app = createApp(App)
setupStore(app)
setupRouter(app)
setupDirective(app)
// 通知状态
app.use(Notifications)
app.mount('#app')

console.clear();
console.log('%c作者：apecode.','color:pink');
console.log('%cQQ: 1473018671','color: red;');
console.log('%cgithub: https://github.com/apecodex','color: green;');