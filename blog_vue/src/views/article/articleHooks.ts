import type {Ref} from "vue";
import {computed, ref} from "vue";
import {getArticleByArticleId} from "@/api/requests/Article";
import {removeHTMLTag, scrollDown} from "@/utils/utils";
import {notify} from "@kyvg/vue3-notification";
import {useSettingStore, useUserInfoStore, useWebsiteInfoStore} from "@/store";
import {storeToRefs} from "pinia";
import {router} from "@/router"
import {articleLike} from "@/api/requests/Article";

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const websiteInfoStore = useWebsiteInfoStore();
const {loadingFlag, loginFlag} = storeToRefs(settingStore);
const {userInfo} = storeToRefs(userInfoStore);
const {rectangle} = storeToRefs(websiteInfoStore);

const article: Ref<ArticleEntity | null> = ref(null);
// 文章是否有目录
const hasArticleCatalog = ref(false);
// 文章字数统计
const articleWordNum = ref(0);
// 阅读时长
const articleReadTime = ref(0);
// 显示分享模态框
const showShareModal = ref(false);
// 分享的复制按钮文本
const copyBtnText = ref("Copy");
// 当前文章url地址
const articleUrl = computed(() => window.location.origin + router.currentRoute.value.fullPath)

const createArticleByArticleId = async (articleId: string) => {
    loadingFlag.value = true
    article.value = null
    await getArticleByArticleId(articleId).then((resp: ResultObject<ArticleEntity>) => {
        if (resp.status) {
            document.title = resp.data.articleTitle
            article.value = resp.data
            if (resp.data.rectangle) {
                rectangle.value = resp.data.rectangle;
            }
            if (article.value) {
                article.value.likeCount = article.value.likeCount ? article.value.likeCount : 0
                article.value.viewsCount = article.value.viewsCount ? article.value.viewsCount : 0
            }
            // 文章字数
            articleWordNum.value = removeHTMLTag(resp.data.articleContent).length
            // 阅读时间
            articleReadTime.value = Math.round(articleWordNum.value / 400)
        } else {
            notify({
                text: resp.message,
                type: "warn"
            })
            router.replace({name: "NotFound"})
        }
    }).catch(() => {
        notify({
            text: "文章获取失败,请重试！",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false
        scrollDown()
    })
}

const openShareModal = () => {
    showShareModal.value = true
    document.body.style.overflow = 'hidden'
}

const shareWechat = () => {
    window.open("https://api.pwmqr.com/qrcode/create/?url=" + articleUrl.value)
}
const shareQzShare = () => {
    const url = `https://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=${articleUrl.value}&sharesource=qzone&title=${article.value?.articleTitle}&pics=${article.value?.articleCover}&summary=${article.value?.articleTitle}`
    window.open(url)
}
const shareWeibo = () => {
    const url = `https://service.weibo.com/share/share.php?url=${articleUrl.value}&sharesource=weibo&title=${article.value?.articleTitle}&pic=${article.value?.articleCover}`
    window.open(url)
}

// 是否有目录
const onGetCatalog = (list: Array<any>) => {
    hasArticleCatalog.value = list.length !== 0
};

// 判断是否已经点赞
const isLikeComp = computed(() => {
    if (userInfoStore.isLogin) {
        if (userInfo.value?.articleLikeSet.indexOf(article.value?.id as string) !== -1) {
            return true;
        }
    }
    return false;
})

// 点赞
const clickLikeHandle = async () => {
    // 判断登录
    if (!userInfoStore.isLogin) {
        loginFlag.value = true
        return;
    }
    await articleLike(article.value?.id as string).then((resp: ResultObject<null>) => {
        if (resp.status) {
            if (userInfo.value?.articleLikeSet.indexOf(article.value?.id as string) !== -1) {
                (<any>article.value!.likeCount) -= 1
            } else {
                (<any>article.value!.likeCount) += 1
            }
            userInfoStore.articleLike(article.value?.id as string)
        }
    })
}
export {
    article,
    hasArticleCatalog,
    articleWordNum,
    articleReadTime,
    showShareModal,
    copyBtnText,
    articleUrl,
    createArticleByArticleId,
    openShareModal,
    shareWechat,
    shareQzShare,
    shareWeibo,
    onGetCatalog,
    isLikeComp,
    clickLikeHandle
}