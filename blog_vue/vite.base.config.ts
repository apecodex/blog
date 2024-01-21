import { defineConfig, UserConfigExport } from 'vite'
import { fileURLToPath, URL } from 'url'
import vue from '@vitejs/plugin-vue'
import viteCompression from 'vite-plugin-compression'
import WindiCSS from 'vite-plugin-windicss'
import { Plugin as importToCDN, autoComplete } from "vite-plugin-cdn-import";
import legacy from '@vitejs/plugin-legacy'

// https://vitejs.dev/config/
export default defineConfig({
  base: '/',
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
    legacy({
      targets: ['chrome 63', 'ie >= 11'],
      additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
      renderLegacyChunks: true,
      polyfills: [
        'es.symbol',
        'es.array.filter',
        'es.promise',
        'es.promise.finally',
        'es/map',
        'es/set',
        'es.array.for-each',
        'es.object.define-properties',
        'es.object.define-property',
        'es.object.get-own-property-descriptor',
        'es.object.get-own-property-descriptors',
        'es.object.keys',
        'es.object.to-string',
        'web.dom-collections.for-each',
        'esnext.global-this',
        'esnext.string.match-all'
      ],
      modernPolyfills: ['es.string.replace-all']
    }),
    importToCDN({
      modules: [
        {
          name: 'vue',
          var: 'Vue',
          path: "https://unpkg.com/vue@3.2.40/dist/vue.global.prod.js"
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
          path: 'https://unpkg.com/pinia@2.0.22/dist/pinia.iife.prod.js'
        },
        {
          name: 'vue-router',
          var: 'VueRouter',
          path: 'https://unpkg.com/vue-router@4.1.5/dist/vue-router.global.prod.js'
        },
        {
          name: 'md-editor-v3',
          var: 'MdEditorV3',
          path: 'https://unpkg.com/md-editor-v3@2.3.0/lib/md-editor-v3.umd.js',
          css: 'https://unpkg.com/md-editor-v3@2.3.0/lib/style.css'
        },
        {
          name: 'canvas-confetti',
          var: 'confetti',
          path: 'https://unpkg.com/canvas-confetti@1.6.0/dist/confetti.browser.js'
        },
        {
          name: 'axios',
          var: 'axios',
          path: 'https://unpkg.com/axios@0.27.2/dist/axios.min.js',
        },
        {
          name: 'sockjs-client',
          var: 'SockJS',
          path: 'https://unpkg.com/sockjs-client@1.6.1/dist/sockjs.min.js',
        },
        {
          name: '@stomp/stompjs',
          var: 'StompJs',
          path: 'https://unpkg.com/@stomp/stompjs@7.0.0/bundles/stomp.umd.min.js',
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
      '@': fileURLToPath(new URL("./src", import.meta.url))
    }
  },
  css: {
    modules: {
      localsConvention: "camelCaseOnly",
    }
  }
}) as UserConfigExport
