import {ref, Ref} from "vue";
import {beforeUpload, personalAvatarFile} from '~/views/personal/data/usePersonal'
import {UploadCustomRequestOptions, UploadFileInfo} from "naive-ui";
import {updateWebsiteConfigure, uploadWebsiteConfigurePicture} from '~/api/requests/Website'
import {websiteData} from "~/views/website/data/useWebsite";

// 网站头像
const websiteAvatarFile: Ref<Array<UploadFileInfo>> = ref<Array<UploadFileInfo>>([])
// 默认用户头像
const websiteDefaultUserAvatarFile: Ref<Array<UploadFileInfo>> = ref<Array<UploadFileInfo>>([])
// 默认游客头像
const websiteDefaultTouristAvatarFile: Ref<Array<UploadFileInfo>> = ref<Array<UploadFileInfo>>([])
// 首页背景图
const websiteBackgroundImagesFile: Ref<Array<UploadFileInfo>> = ref<Array<UploadFileInfo>>([])
// 微信打赏码
const weiXinQRCodeFile: Ref<Array<UploadFileInfo>> = ref<Array<UploadFileInfo>>([])
// 支付宝打赏码
const alipayQRCodeFile: Ref<Array<UploadFileInfo>> = ref<Array<UploadFileInfo>>([])

// 更新网站头像
const updateWebsiteAvatarHandle = async ({file, onFinish, onError,}: UploadCustomRequestOptions) => {
    let tipMessage = (window as any).$message.create("正在上传...", {
        type: 'loading'
    });
    await uploadWebsiteConfigurePicture(file).then((resp: ResultObject<UploadFileInfoModel>) => {
        if (resp.status) {
            (<WebsiteConfigureModel>websiteData.value).websiteAvatar = resp.data.url
            let fileName = resp.data.url.substring(resp.data.url.lastIndexOf("/") + 1)
            personalAvatarFile.value = new Array({
                id: fileName,
                name: fileName,
                status: "finished",
                url: resp.data.url
            })
            onFinish()
            updateWebsiteConfigure(websiteData.value as WebsiteConfigureModel)
            tipMessage.type = 'success'
            tipMessage.content = '更新成功'
        }
    }).catch(() => {
        onError();
        tipMessage.type = 'error'
        tipMessage.content = '更新失败'
    })
}

// 更新默认用户头像
const updateWebsiteDefaultUserAvatarHandle = async ({file, onFinish, onError,}: UploadCustomRequestOptions) => {
    let tipMessage = (window as any).$message.create("正在上传...", {
        type: 'loading'
    });
    await uploadWebsiteConfigurePicture(file).then((resp: ResultObject<UploadFileInfoModel>) => {
        if (resp.status) {
            (<WebsiteConfigureModel>websiteData.value).defaultAvatar = resp.data.url
            let fileName = resp.data.url.substring(resp.data.url.lastIndexOf("/") + 1)
            websiteDefaultUserAvatarFile.value = new Array({
                id: fileName,
                name: fileName,
                status: "finished",
                url: resp.data.url
            })
            onFinish()
            updateWebsiteConfigure(websiteData.value as WebsiteConfigureModel)
            tipMessage.type = 'success'
            tipMessage.content = '更新成功'
        }
    }).catch(() => {
        onError();
        tipMessage.type = 'error'
        tipMessage.content = '更新失败'
    })
}

// 更新默认游客头像
const updateWebsiteDefaultTouristAvatarHandle = async ({file, onFinish, onError,}: UploadCustomRequestOptions) => {
    let tipMessage = (window as any).$message.create("正在上传...", {
        type: 'loading'
    });
    await uploadWebsiteConfigurePicture(file).then((resp: ResultObject<UploadFileInfoModel>) => {
        if (resp.status) {
            (<WebsiteConfigureModel>websiteData.value).touristAvatar = resp.data.url
            let fileName = resp.data.url.substring(resp.data.url.lastIndexOf("/") + 1)
            websiteDefaultTouristAvatarFile.value = new Array({
                id: fileName,
                name: fileName,
                status: "finished",
                url: resp.data.url
            })
            onFinish()
            updateWebsiteConfigure(websiteData.value as WebsiteConfigureModel)
            tipMessage.type = 'success'
            tipMessage.content = '更新成功'
        }
    }).catch(() => {
        onError();
        tipMessage.type = 'error'
        tipMessage.content = '更新失败'
    })
}

// 点击删除背景图
const websiteBackgroundImagesRemoveHandle = async (options: { file: UploadFileInfo, fileList: Array<UploadFileInfo> }) => {
    // 清零，防止上传过多的图片
    (<WebsiteConfigureModel>websiteData.value).websiteBackgroundImages.length = 0
    // 过滤掉被删除的图片
    const images = options.fileList.filter((img) => img.id !== options.file.id).map((img) => img.url);
    // 过滤完后重新添加
    (<WebsiteConfigureModel>websiteData.value).websiteBackgroundImages = Array.from(images) as Array<string>
}

// 更新首页背景图
const websiteBackgroundImagesHandle = async ({file, onFinish, onError,}: UploadCustomRequestOptions) => {
    let tipMessage = (window as any).$message.create("正在上传...", {
        type: 'loading'
    });
    await uploadWebsiteConfigurePicture(file).then((resp: ResultObject<UploadFileInfoModel>) => {
        if (resp.status) {
            (<WebsiteConfigureModel>websiteData.value).websiteBackgroundImages.push(resp.data.url)
            let fileName = resp.data.url.substring(resp.data.url.lastIndexOf("/") + 1)
            websiteDefaultTouristAvatarFile.value.push({
                id: fileName,
                name: fileName,
                status: "finished",
                url: resp.data.url
            })
            onFinish()
            updateWebsiteConfigure(websiteData.value as WebsiteConfigureModel)
            tipMessage.type = 'success'
            tipMessage.content = '更新成功'
        }
    }).catch(() => {
        onError();
        tipMessage.type = 'error'
        tipMessage.content = '更新失败'
    })
}

// 更新微信打赏码
const updateWeiXinQRCodeFileHandle = async ({file, onFinish, onError,}: UploadCustomRequestOptions) => {
    let tipMessage = (window as any).$message.create("正在上传...", {
        type: 'loading'
    });
    await uploadWebsiteConfigurePicture(file).then((resp: ResultObject<UploadFileInfoModel>) => {
        if (resp.status) {
            (<WebsiteConfigureModel>websiteData.value).weiXinQRCode = resp.data.url
            let fileName = resp.data.url.substring(resp.data.url.lastIndexOf("/") + 1)
            weiXinQRCodeFile.value = new Array({
                id: fileName,
                name: fileName,
                status: "finished",
                url: resp.data.url
            })
            onFinish()
            updateWebsiteConfigure(websiteData.value as WebsiteConfigureModel)
            tipMessage.type = 'success'
            tipMessage.content = '更新成功'
        }
    }).catch(() => {
        onError();
        tipMessage.type = 'error'
        tipMessage.content = '更新失败'
    })
}

// 更新支付宝打赏码
const updateAlipayQRCodeFileHandle = async ({file, onFinish, onError,}: UploadCustomRequestOptions) => {
    let tipMessage = (window as any).$message.create("正在上传...", {
        type: 'loading'
    });
    await uploadWebsiteConfigurePicture(file).then((resp: ResultObject<UploadFileInfoModel>) => {
        if (resp.status) {
            (<WebsiteConfigureModel>websiteData.value).alipayQRCode = resp.data.url
            let fileName = resp.data.url.substring(resp.data.url.lastIndexOf("/") + 1)
            alipayQRCodeFile.value = new Array({
                id: fileName,
                name: fileName,
                status: "finished",
                url: resp.data.url
            })
            onFinish()
            updateWebsiteConfigure(websiteData.value as WebsiteConfigureModel)
            tipMessage.type = 'success'
            tipMessage.content = '更新成功'
        }
    }).catch(() => {
        onError();
        tipMessage.type = 'error'
        tipMessage.content = '更新失败'
    })
}

export {
    beforeUpload,
    websiteAvatarFile,
    websiteDefaultUserAvatarFile,
    websiteDefaultTouristAvatarFile,
    websiteBackgroundImagesFile,
    weiXinQRCodeFile,
    alipayQRCodeFile,
    updateWebsiteAvatarHandle,
    updateWebsiteDefaultUserAvatarHandle,
    updateWebsiteDefaultTouristAvatarHandle,
    websiteBackgroundImagesHandle,
    websiteBackgroundImagesRemoveHandle,
    updateWeiXinQRCodeFileHandle,
    updateAlipayQRCodeFileHandle
}