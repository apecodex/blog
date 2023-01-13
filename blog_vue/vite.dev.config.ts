import {defineConfig, UserConfigExport} from 'vite'

export default defineConfig({
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
