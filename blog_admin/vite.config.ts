import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {fileURLToPath, URL} from 'url'
import WindiCSS from 'vite-plugin-windicss'
import viteCompression from 'vite-plugin-compression'
import {Plugin as importToCDN, autoComplete} from "vite-plugin-cdn-import";

// https://vitejs.dev/config/
export default defineConfig({
  base: './',
  plugins: [
    vue(),
    WindiCSS(),
    viteCompression({
      verbose: true,
      disable: false,
      threshold: 10240,
      algorithm: 'gzip',
      ext: '.gz',
    }),
    importToCDN({
      modules: [
        {
          name: 'vue',
          var: 'Vue',
          path: "https://unpkg.com/vue@3.2.37/dist/vue.global.prod.js"
        },
        // 没有这个pinia会报错
        {
          name: 'vue-demi',
          var: 'VueDemi',
          path: 'https://unpkg.com/vue-demi@0.13.11/lib/index.iife.js'
        },
        {
          name: 'pinia',
          var: 'Pinia',
          path: 'https://unpkg.com/pinia@2.0.17/dist/pinia.iife.prod.js'
        },
        {
          name: 'vue-router',
          var: 'VueRouter',
          path: 'https://unpkg.com/vue-router@4.1.3/dist/vue-router.global.prod.js'
        },
        {
          name: 'naive-ui',
          var: 'naive',
          path: 'https://unpkg.com/naive-ui@2.32.2/dist/index.prod.js'
        },
        {
          name: 'md-editor-v3',
          var: 'MdEditorV3',
          path: 'https://unpkg.com/md-editor-v3@2.5.0/lib/md-editor-v3.umd.js',
          css: 'https://unpkg.com/md-editor-v3@2.5.0/lib/style.css'
        },
        {
          name: 'echarts',
          var: 'echarts',
          path: 'https://unpkg.com/echarts@5.3.3/dist/echarts.min.js',
        },
        {
          name: 'axios',
          var: 'axios',
          path: 'https://unpkg.com/axios@0.27.2/dist/axios.min.js',
        },
        {
          name: 'jsencrypt',
          var: 'JSEncrypt',
          path: 'https://unpkg.com/jsencrypt@3.3.2/bin/jsencrypt.min.js',
        },
      ]
    })
  ],
  resolve: {
    alias: {
      '~': fileURLToPath(new URL("./src", import.meta.url))
    }
  },
  build: {
    outDir: 'dist', //指定输出路径
    assetsDir: 'static/img/', // 指定生成静态资源的存放路径
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules')) {
            const arr = id.toString().split('node_modules/')[1].split('/')
            switch (arr[0]) {
              case 'windicss':
              case '@icon-park/vue-next':
                return '_' + arr[0]
                break
              default:
                return '__vendor'
                break
            }
          }
        },
        chunkFileNames: 'static/js1/[name]-[hash].js',
        entryFileNames: 'static/js2/[name]-[hash].js',
        assetFileNames: 'static/[ext]/[name]-[hash].[ext]'
      },
    },
    target: 'es2015',
  },
  server: {
    host: true,
    port: 5173
  }
})