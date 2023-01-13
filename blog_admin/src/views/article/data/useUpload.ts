import type { UploadCustomRequestOptions, UploadFileInfo } from 'naive-ui'
import { uploadArticleCover } from '~/api/requests/Article'
import { StatusCode } from '~/api/enum/statusCode'
import { articleInfo } from './useArticle'
import {ref} from "vue";

const fileList = ref<UploadFileInfo[] | never[]>([])

// 上传封面
const uploadCover = async ({
  file,
  onFinish,
  onError,
}: UploadCustomRequestOptions) => {
  let tipMessage = (window as any).$message.create("正在上传...", {
    type: 'loading'
  });
  await uploadArticleCover(file).then((uploadResult: ResultObject<UploadFileInfoModel>) => {
    if (uploadResult.code === StatusCode.SUCCESS && uploadResult.status) {
      articleInfo.articleCover = uploadResult.data.url
      onFinish();
      if (fileList.value.length === 0) {
        fileList.value.push({ id: file.id, name: file.name, status: 'finished', url: uploadResult.data } as never);
      }
      tipMessage.type = 'success'
      tipMessage.content = '上传成功'
    }
  }).catch((error) => {
    onError();
    tipMessage.type = 'error'
    tipMessage.content = '上传失败'
  })
}

// 删除图片
const handleRemove = async () => {
  fileList.value = [];
  articleInfo.articleCover = "";
}

// 文章内容图片上传/粘贴上传
const uploadImg = async (files: Array<File>, callback: (urls: string[]) => void) => {
  let tipMessage = (window as any).$message.create("图片上传中...", {
    type: 'loading'
  });
  const res = await Promise.all(
    Array.from(files).map((file) => {
      return new Promise((rev, rej) => {
        const form = new FormData()
        form.append("file", file);
        uploadArticleCover(form).then((res) => {
          rev(res)
          tipMessage.type = 'success'
          tipMessage.content = file.name + " " + res.message
        }).catch((error) => {
          tipMessage.type = 'error'
          tipMessage.content = '图片上传失败'
        })
      })
    })
  )
  callback(res.map((item: any) => item.data.url));
}

export {
  fileList,
  uploadCover,
  handleRemove,
  uploadImg
}