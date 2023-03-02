import type {UploadFileInfo, UploadCustomRequestOptions} from 'naive-ui'
import type {Ref} from 'vue'
import {getUserInfo, updateUserAvatar, updateUserInfo} from '~/api/requests/UserInfo'
import {useUserInfoStore} from '~/store'
import {ref} from "vue";

const userInfoStore = useUserInfoStore()
const userInfo = userInfoStore.getUserInfo();
// 当前用户个人信息
const userInfoData: Ref<UserPersonalInfoModel | null> = ref<UserPersonalInfoModel | null>(null)
const personalAvatarFile = ref<UploadFileInfo[]>([])


// 限制上传格式
const beforeUpload = (data: {
    file: UploadFileInfo
    fileList: UploadFileInfo[]
}) => {
    const fileType = ['image/gif', 'image/jpeg', 'image/jpg', 'image/png']
    if (fileType.indexOf(data.file.file?.type as string) === -1) {
        (<any>window).$message.warning("只能上传png/jpg/gif/jpeg格式的图片文件，请重新上传");
        return false;
    }
    return true
}

// 获取用户信息
const getUserInfoData = async () => {
    await getUserInfo().then((resp: ResultObject<UserPersonalInfoModel>) => {
        if (resp.status) {
            userInfoData.value = resp.data
            let fileName = resp.data.avatar.substring(resp.data.avatar.lastIndexOf("/") + 1)
            personalAvatarFile.value = new Array({
                id: fileName,
                name: fileName,
                status: "finished",
                url: resp.data.avatar
            })
        }
    }).catch(() => {
        (<any>window).$message.error("用户信息获取失败，请重试");
    })
}

// 更新用户头像
const updateUserAvatarHandle = async ({file, onFinish, onError,}: UploadCustomRequestOptions) => {
    let tipMessage = (window as any).$message.create("正在上传...", {
        type: 'loading'
    });
    await updateUserAvatar(file).then((resp: ResultObject<UploadFileInfoModel>) => {
        if (resp.status) {
            (<UserInfoModel>userInfo).avatar = resp.data.url;
            userInfoStore.saveUserInfo(userInfo as UserInfoModel);
            (<UserPersonalInfoModel>userInfoData.value).avatar = resp.data.url
            let fileName = resp.data.url.substring(resp.data.url.lastIndexOf("/") + 1)
            personalAvatarFile.value = new Array({
                id: fileName,
                name: fileName,
                status: "finished",
                url: resp.data.url
            })
            onFinish()
            tipMessage.type = 'success'
            tipMessage.content = '更新成功'
        }
    }).catch(() => {
        onError();
        tipMessage.type = 'error'
        tipMessage.content = '更新失败'
    })
}

// 更新用户信息处理
const updateUserInfoHandle = async () => {
    let tipMessage = (window as any).$message.create("保存中...", {
        type: 'loading'
    });
    await updateUserInfo({
        nickname: userInfoData.value!.nickname,
        intro: userInfoData.value!.intro,
        webSite: userInfoData.value!.webSite,
        isEmailNotice: userInfoData.value!.isEmailNotice,
    }).then((resp: ResultObject<null>) => {
        if (resp.status) {
            (<UserInfoModel>userInfo).nickname = userInfoData.value?.nickname as string
            userInfoStore.saveUserInfo(userInfo as UserInfoModel)
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '更新失败'
    })
}

// 更新用户简介
const updateIntro = async (text: any) => {
    (<UserPersonalInfoModel>userInfoData.value).intro = text
    await updateUserInfoHandle()
}

export {
    personalAvatarFile,
    beforeUpload,
    userInfoData,
    getUserInfoData,
    updateUserAvatarHandle,
    updateUserInfoHandle,
    updateIntro,
    userInfoStore
}