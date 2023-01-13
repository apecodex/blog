import type {Ref} from 'vue'
import {getAboutMe, updateAboutMe} from '~/api/requests/About'
import {ref} from "vue";

const aboutMeData: Ref<string> = ref<string>("")

const createAboutMeData = async () => {
    await getAboutMe().then((resp: ResultObject<string>) => {
        if (resp.status) {
            aboutMeData.value = resp.data
        }
    }).catch(() => {
        (<any>window).$message.error("获取失败，请重试");
    })
}

// 保存
const updateAboutMeHandle = async () => {
    const form = new FormData()
    form.append('aboutMe', aboutMeData.value)
    let tipMessage = (window as any).$message.create("保存中...", {
        type: 'loading'
    });
    await updateAboutMe(form).then((resp: ResultObject<null>) => {
        if (resp.status) {
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '保存失败'
    })
}

export {
    aboutMeData,
    createAboutMeData,
    updateAboutMeHandle
}