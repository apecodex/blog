// 当前光标位置
import {UploadCustomRequestOptions, UploadFileInfo} from "naive-ui";
import {saveOrUpdateTalk, saveTalkPictureVideo, getTalkBackOnlyById} from '~/api/requests/Talk'
import {StatusCode} from "~/api/enum/statusCode";
import {router} from '~/router'
import {reactive, ref} from "vue";

// 配图列表
const previewFileList = ref<UploadFileInfo[]>([])
// 在图片上传时禁用发布按钮
const disabledSendBtn = ref(false)

const talkForm: TalkFormParams = reactive({
    content: "",
    isTop: false,
    status: 1
})

// 保存配图
const uploadTalkPictureOrVideo = async ({file, onFinish, onError}: UploadCustomRequestOptions) => {
    let tipMessage = (window as any).$message.create(file.name + "上传中...", {
        type: 'loading'
    });
    disabledSendBtn.value = true
    await saveTalkPictureVideo(file).then((resp: ResultObject<UploadFileInfoModel>) => {
        if (resp.code === StatusCode.SUCCESS && resp.status) {
            previewFileList.value.push({
                id: file.id,
                name: file.name,
                status: "finished",
                url: resp.data.url
            })
            onFinish();
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        onError();
        tipMessage.type = 'error'
        tipMessage.content = '上传失败'
    }).finally(() => {
        disabledSendBtn.value = false
    })
}
// 清空说说内容
const clearTalkForm = () => {
    talkForm.content = ""
    if (talkForm.files && talkForm.files.length !== 0) {
        talkForm.files.length = 0
    }
    previewFileList.value.length = 0
    talkForm.status = 1
    talkForm.isTop = false
}

// 点击删除图片
const handleRemove = (options: { file: UploadFileInfo, fileList: Array<UploadFileInfo> }) => {
    previewFileList.value = previewFileList.value.filter((fl) => fl.id !== options.file.id)
}
// 保存说说
const saveTalk = async (text: string) => {
    talkForm.content = text
    if (talkForm.content.length === 0) {
        (window as any).$message.warning("说说内容不能为空")
        return
    }
    if (previewFileList.value.length !== 0) {
        let pfl = previewFileList.value.map((pfl) => pfl.url);
        talkForm.files = pfl as any
    }
    let tipMessage = (window as any).$message.create("保存中...", {
        type: 'loading'
    });
    await saveOrUpdateTalk(talkForm).then((resp: ResultObject<null>) => {
        if (resp.code == StatusCode.SUCCESS && resp.status) {
            clearTalkForm();
            tipMessage.type = 'success'
            tipMessage.content = resp.message
            // 跳转至说说列表
            router.replace({path: '/talk-list'})
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '保存失败'
    })
}

const pullTalkById = async (talkId: string) => {
    clearTalkForm();
    let tipMessage = (window as any).$message.create("正在获取说说...", {
        type: 'loading'
    });
    talkForm.id = talkId
    await getTalkBackOnlyById(talkId).then((resp: ResultObject<TalkBackOnlyModel>) => {
        if (resp.code === StatusCode.SUCCESS && resp.status) {
            talkForm.content = resp.data.content
            talkForm.id = resp.data.id
            talkForm.isTop = resp.data.isTop
            talkForm.status = resp.data.status
            resp.data.pictureVideos.map((pv: TalkPictureVideoModel) => {
                previewFileList.value.push({
                    id: pv.id,
                    name: pv.fileName,
                    status: "finished",
                    url: pv.src
                })
            });
            tipMessage.type = 'success'
            tipMessage.content = '获取成功'
        }
        if (resp.code === StatusCode.FAIL) {
            tipMessage.type = 'warning'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '说说获取失败，请重试！'
    })
}

export {
    talkForm,
    saveTalk,
    previewFileList,
    uploadTalkPictureOrVideo,
    disabledSendBtn,
    handleRemove,
    clearTalkForm,
    pullTalkById
}