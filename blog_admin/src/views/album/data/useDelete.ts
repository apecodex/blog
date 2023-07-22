import type { Ref } from 'vue'
import {
  listDeletePicture,
  updatePictureDeleteStatus,
  deletePicture
} from '~/api/requests/Picture'
import { ref } from "vue";

// 图片数据
const pictureData: Ref<Array<PictureBackModel>> = ref<Array<PictureBackModel>>([]);
// 是否有图片数据
const hasPictureData = ref(false);

const createDeletePictureData = async (condition: ConditionParams) => {
  hasPictureData.value = false;
  await listDeletePicture(condition).then((resp: PageResult<Array<PictureBackModel>>) => {
    if (resp.status) {
      pictureData.value = Array.from(resp.data.recordList);
      hasPictureData.value = resp.data.count === 0;
    }
  }).catch(() => {
    (<any>window).$message.error("获取失败，请重试");
  })
}

// 恢复删除的图片
const recoverPictureHandle = async (picture: PictureBackModel) => {
  let tipMessage = (window as any).$message.create("正在恢复...", {
    type: 'loading'
  });
  await updatePictureDeleteStatus({ idList: new Array<string>(picture.id), isDelete: false }).then((resp: ResultObject<null>) => {
    if (resp.status) {
      const p = pictureData.value.filter((p: PictureBackModel) => p.id !== picture.id);
      pictureData.value.length = 0
      pictureData.value = Array.from(p)
      tipMessage.type = 'success'
      tipMessage.content = resp.message
    }
  }).catch(() => {
    tipMessage.type = 'error'
    tipMessage.content = '恢复失败'
  }).finally(() => {
    if (pictureData.value.length === 0) {
      hasPictureData.value = true
    }
  })
}

const deletePictureHandle = (picture: PictureBackModel) => {
  (<any>window).$dialog.warning({
    title: '警告',
    content: '这会永久删除，你确定删除吗？',
    positiveText: '确定',
    negativeText: '手误',
    onPositiveClick: async () => {
      let tipMessage = (window as any).$message.create("正在删除...", {
        type: 'loading'
      });
      await deletePicture(new Array<string>(picture.id)).then((resp: ResultObject<null>) => {
        if (resp.status) {
          const p = pictureData.value.filter((p: PictureBackModel) => p.id !== picture.id);
          pictureData.value.length = 0;
          pictureData.value = Array.from(p);
          tipMessage.type = 'success';
          tipMessage.content = resp.message;
        }
      }).catch(() => {
        tipMessage.type = 'error';
        tipMessage.content = '删除失败';
      }).finally(() => {
        if (pictureData.value.length === 0) {
          hasPictureData.value = true;
        }
      })
    }
  })
}

export {
  pictureData,
  hasPictureData,
  createDeletePictureData,
  recoverPictureHandle,
  deletePictureHandle
}