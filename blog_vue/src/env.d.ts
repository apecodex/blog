/// <reference types="vite/client" />

// 获取自定义环境变量的 智能提示
interface ImportMetaEnv {
    readonly VITE_API_URL: string
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}