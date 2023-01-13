import {defineConfig, UserConfigExport} from 'vite'

export default defineConfig({
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
    target: 'es2015'
  },
  server: {
    host: true,
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://ip:8081',
        changeOrigin: true,
        rewrite: (path: string) => path.replace(/^\/api/, '')
      }
    }
  }
}) as UserConfigExport
