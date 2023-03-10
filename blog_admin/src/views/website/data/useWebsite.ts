import type {Ref} from 'vue'
import {getWebsiteConfigure, updateWebsiteConfigure} from '~/api/requests/Website'
import {websiteAvatarFile, websiteDefaultUserAvatarFile, websiteDefaultTouristAvatarFile, websiteBackgroundImagesFile, weiXinQRCodeFile, alipayQRCodeFile} from './useAvatar'
import {ref} from "vue";

const websiteData: Ref<WebsiteConfigureModel | null> = ref<WebsiteConfigureModel | null>(null)
const commentReviewLoading = ref(false)
const messageReviewLoading = ref(false)
const emailNoticeLoading = ref(false)
const messageNoticeLoading = ref(false)
const autoPlayerLoading = ref(false)

const createWebsiteData = async () => {
    await getWebsiteConfigure().then((resp: ResultObject<WebsiteConfigureModel>) => {
        if (resp.status) {
            websiteData.value = resp.data
            let websiteAvatarFileName = resp.data.websiteAvatar.substring(resp.data.websiteAvatar.lastIndexOf("/") + 1)
            let defaultUserAvatarFileName = resp.data.defaultAvatar.substring(resp.data.defaultAvatar.lastIndexOf("/") + 1)
            let defaultTouristAvatarFileName = resp.data.touristAvatar.substring(resp.data.touristAvatar.lastIndexOf("/") + 1)
            let weiXinQRCodeFileName = resp.data.touristAvatar.substring(resp.data.touristAvatar.lastIndexOf("/") + 1)
            let alipayQRCodeFileName = resp.data.touristAvatar.substring(resp.data.touristAvatar.lastIndexOf("/") + 1)
            websiteAvatarFile.value = new Array({
                id: websiteAvatarFileName,
                name: websiteAvatarFileName,
                status: "finished",
                url: resp.data.websiteAvatar
            })
            websiteDefaultUserAvatarFile.value = new Array({
                id: defaultUserAvatarFileName,
                name: defaultUserAvatarFileName,
                status: "finished",
                url: resp.data.defaultAvatar
            })
            websiteDefaultTouristAvatarFile.value = new Array({
                id: defaultTouristAvatarFileName,
                name: defaultTouristAvatarFileName,
                status: "finished",
                url: resp.data.touristAvatar
            })
            weiXinQRCodeFile.value = new Array({
                id: weiXinQRCodeFileName,
                name: weiXinQRCodeFileName,
                status: "finished",
                url: resp.data.weiXinQRCode
            })
            alipayQRCodeFile.value = new Array({
                id: alipayQRCodeFileName,
                name: alipayQRCodeFileName,
                status: "finished",
                url: resp.data.alipayQRCode
            })
            websiteBackgroundImagesFile.value.length = 0
            resp.data.websiteBackgroundImages.map((img) => {
                let bgName = img.substring(resp.data.touristAvatar.lastIndexOf("/") + 1)
                websiteBackgroundImagesFile.value.push({
                    id: Math.random().toString(),
                    name: bgName,
                    status: "finished",
                    url: img
                })
            })
        }
    }).catch(() => {
        (<any>window).$message.error("????????????????????????????????????");
    })
}

const updateWebsiteConfigureHandle = async (data: WebsiteConfigureModel) => {
    let tipMessage = (<any>window).$message.create('?????????...', {
        type: 'loading'
    })
    return await updateWebsiteConfigure(data).then((resp: ResultObject<null>) => {
        if (resp.status) {
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '????????????'
    })
}

// ????????????
const updateWebsite = async () => {
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel)
}

// ??????????????????
const updateWebsiteIntro = async (text: string) => {
    (<WebsiteConfigureModel>websiteData.value).websiteIntro  = text as string
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel)
}

// ??????????????????
const updateSocialLogin = async () => {
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel)
}

// ??????????????????
const updateCommentReview = async () => {
    commentReviewLoading.value = true
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel).finally(() => {
        commentReviewLoading.value = false
    })

}

// ??????????????????
const updateMessageReview = async () => {
    messageReviewLoading.value = true
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel).finally(() => {
        messageReviewLoading.value = false
    })
}

// ??????????????????
const updateEmailNotice = async () => {
    emailNoticeLoading.value = true
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel).finally(() => {
        emailNoticeLoading.value = false
    })
}

// ??????????????????
const updateMessageNotice = async () => {
    messageNoticeLoading.value = true
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel).finally(() => {
        messageNoticeLoading.value = false
    })
}

// ??????????????????
const updateAutoPlayer = async () => {
    autoPlayerLoading.value = true
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel).finally(() => {
        autoPlayerLoading.value = false
    })
}

/// ??????????????????
const updateWebsiteNotice = async (text: string) => {
    (<WebsiteConfigureModel>websiteData.value).websiteNotice  = text as string
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel)
}

// ???????????????????????????
const updateTyperText = async (value: string[]) => {
    (<WebsiteConfigureModel>websiteData.value).homeTyperTexts = value
    await updateWebsiteConfigureHandle(websiteData.value as WebsiteConfigureModel)
}

export {
    websiteData,
    createWebsiteData,
    updateWebsite,
    updateWebsiteIntro,
    updateSocialLogin,
    updateCommentReview,
    updateMessageReview,
    updateEmailNotice,
    updateMessageNotice,
    updateAutoPlayer,
    updateWebsiteNotice,
    commentReviewLoading,
    messageReviewLoading,
    emailNoticeLoading,
    messageNoticeLoading,
    autoPlayerLoading,
    updateTyperText
}