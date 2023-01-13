import {defineConfig, UserConfigExport} from 'vite'

import viteBaseConfig from './vite.base.config'
import viteDevConfig from './vite.dev.config'
import viteProdConfig from './vite.prod.config'

const envResolver = {
  'build': () => ({...viteBaseConfig, ...viteProdConfig}),
  'serve': () => ({...viteBaseConfig, ...viteDevConfig})
} as UserConfigExport

export default defineConfig(({command}) => {
  return envResolver[command]();
})