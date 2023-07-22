export interface SettingConfig {
    globalTheme: 'darkTheme' | 'lightTheme',
    // 文章预览主题
    previewTheme: 'default' | 'github' | 'vuepress' | 'mk-cute' | 'smart-blue' | 'cyanosis',
    // 文章代码主题
    codeTheme: 'atom' | 'a11y' | 'github' | 'gradient' | 'kimbie' | 'paraiso' | 'qtcreator' | 'stackoverflow'
    // 代码是否展示行号
    showCodeRowNumber: boolean
    loginFlag: boolean,
    registerFlag: boolean,
    searchFlag: boolean,
    loadingFlag: boolean,
    friendLinkFlag: boolean,
    articleThemeFlag: boolean,
    forgetPasswordFlag: boolean,
    bindOrUnbindEmailFlag: boolean,
}

export default {
    globalTheme: 'darkTheme',
    previewTheme: 'cyanosis',
    codeTheme: 'github',
    showCodeRowNumber: true,
    loginFlag: false,
    registerFlag: false,
    searchFlag: false,
    loadingFlag: false,
    friendLinkFlag: false,
    articleThemeFlag: false,
    forgetPasswordFlag: false,
    bindOrUnbindEmailFlag: false
} as SettingConfig