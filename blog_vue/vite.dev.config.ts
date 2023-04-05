import {defineConfig, UserConfigExport} from 'vite'

export default defineConfig({
    server: {
        host: true,
        port: 5173
    }
}) as UserConfigExport
