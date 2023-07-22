import type { UploadFileInfo, UploadCustomRequestOptions } from 'naive-ui'
import type { Ref } from 'vue'
import { albumData, currentClickAlbumInfo } from './useAlbum'
import {
  listPictureByAlbumId,
  movePictureAlbum, savePictures,
  updatePictureDeleteStatus,
  updatePictureInfo,
  uploadPicture
} from '~/api/requests/Picture'
import { StatusCode } from "~/api/enum/statusCode";
import { nextTick, ref } from "vue";

// 分页
const pagination = ref({
  page: {
    value: 1,
    default: 1,
  },
  pageCount: {
    value: 1,
    default: 1
  },
  pageSize: {
    value: 30,
    default: 30
  },
  loadedCount: 0
})

const pictureNameInputRef = ref<HTMLElement | null>(null)
// 图片数据
const pictureData: Ref<Array<PictureBackModel>> = ref<Array<PictureBackModel>>([])
// 是否有图片数据
const hasPictureData = ref(false)
// 切换图片名称修改
const isEditPictureName = ref(true)
// 显示移动相册模态框
const showMovePictureAlbumModel = ref(false)
// 显示添加图片模态框
const showUploadPictureModel = ref(false)
// 相册选项
const albumOptions = ref()
// 当前相册的id
const albumValue = ref<string | null>(null)
// 当前的图片
const currentPicture = ref<PictureBackModel | null>(null)
// 上传的文件数量
const fileListLength = ref<number>(0)
// 已经上传的图片url列表
const fileUrlList = ref<Array<UploadFileInfoModel>>([])
// 显示更多按钮
const showMoreBtn = ref(false)
// 显示更多按钮加载状态
const moreLoading = ref(false)
// 禁止按钮
const disabledFlag = ref(false);

// 修改图片名称
const pictureNameChangeHandle = async (picture: PictureBackModel) => {
  disabledFlag.value = true
  let tipMessage = (window as any).$message.create("正在修改...", {
    type: 'loading'
  });
  isEditPictureName.value = true
  await updatePictureInfo({ pictureName: picture.pictureName, picturePath: picture.id }).then((resp: ResultObject<null>) => {
    if (resp.status) {
      tipMessage.type = "success"
      tipMessage.content = resp.message
      isEditPictureName.value = true;
    }
    if (resp.code === StatusCode.FAIL) {
      tipMessage.type = 'warning'
      tipMessage.content = resp.message
    }
  }).catch(() => {
    tipMessage.type = "error"
    tipMessage.content = '修改失败'
  }).finally(() => {
    disabledFlag.value = false;
  })
}

// 点击切换修改图片名称按钮
const clickPictureNameHandle = async () => {
  isEditPictureName.value = false
  await nextTick(() => {
    pictureNameInputRef.value?.focus()
  })
}

// 获取图片列表
const createPictureData = async (condition: ConditionParams) => {
  disabledFlag.value = true;
  hasPictureData.value = false;
  moreLoading.value = true;
  condition.size = pagination.value.pageSize.value;
  await listPictureByAlbumId(condition).then((resp: PageResult<Array<PictureBackModel>>) => {
    if (resp.code === StatusCode.SUCCESS && resp.status) {
      pictureData.value.push(...Array.from(resp.data.recordList))
      hasPictureData.value = resp.data.count === 0
      showMoreBtn.value = resp.data.count > pictureData.value.length;
      pagination.value.pageCount.value = resp.data.count;
      // 记录已加载的图片数量
      pagination.value.loadedCount = pictureData.value.length
    }
  }).catch(() => {
    (window as any).$message.error("获取图片列表失败，请重试");
  }).finally(() => {
    moreLoading.value = false
    disabledFlag.value = false
  })
}

// 生成相册选项
const generateAlbumOptionHandel = async (picture: PictureBackModel) => {
  showMovePictureAlbumModel.value = true
  currentPicture.value = picture
  await nextTick(() => {
    albumOptions.value = albumData.value.map((album) => {
      return { label: album.albumName, value: album.id }
    })
    albumValue.value = currentClickAlbumInfo.id as string
  })
}

// 移动相册
const movePictureAlbumHandle = async () => {
  let tipMessage = (window as any).$message.create("图片移动中...", {
    type: 'loading'
  });
  disabledFlag.value = true;
  await movePictureAlbum({
    albumId: albumValue.value as any,
    pictureIdList: new Array<string>((currentPicture.value as PictureBackModel).id)
  }).then((resp: PageResult<null>) => {
    if (resp.status) {
      let albumName = "";
      const p = pictureData.value.filter((p: PictureBackModel) => p.id !== currentPicture.value?.id)
      // 清空图片列表
      pictureData.value.length = 0
      // 过滤移动的图片后重新添加
      pictureData.value = Array.from(p)
      // 当前相册数量-1
      currentClickAlbumInfo.pictureCount--
      // 修改相册图片数量
      albumData.value.map((album: AlbumBackModel) => {
        if (album.id === currentClickAlbumInfo.id) {
          album.pictureCount = currentClickAlbumInfo.pictureCount;
        }
        if (album.id === albumValue.value) {
          album.pictureCount++;
          albumName = album.albumName
        }
      });
      // 关闭移动的模态框
      showMovePictureAlbumModel.value = false;
      tipMessage.type = "success"
      tipMessage.content = `'${currentPicture.value?.pictureName}'已移动至${albumName}`
    }
  }).catch(() => {
    tipMessage.type = "error"
    tipMessage.content = "图片移动失败"
  }).finally(() => {
    if (pictureData.value.length === 0) {
      hasPictureData.value = true
    }
    disabledFlag.value = false
  })
}

// 删除图片处理
const deletePictureHandle = (picture: PictureBackModel) => {
  (window as any).$dialog.warning({
    title: '警告',
    content: `你确定删除 '${picture.pictureName}' 吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      let tipMessage = (window as any).$message.create("正在删除...", {
        type: 'loading'
      });
      disabledFlag.value = true
      await updatePictureDeleteStatus({ idList: new Array(picture.id), isDelete: true }).then((resp: ResultObject<null>) => {
        if (resp.status) {
          const p = pictureData.value.filter((p: PictureBackModel) => p.id !== picture.id)
          pictureData.value.length = 0
          pictureData.value = Array.from(p)
          currentClickAlbumInfo.pictureCount--;
          // 修改相册图片数量
          albumData.value.map((album: AlbumBackModel) => {
            if (album.id === currentClickAlbumInfo.id) {
              album.pictureCount = currentClickAlbumInfo.pictureCount;
            }
          });
          tipMessage.type = 'success'
          tipMessage.content = `'${picture.pictureName}'已移至进回收站`
        }
      }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '删除失败'
      }).finally(() => {
        if (pictureData.value.length === 0) {
          hasPictureData.value = true
        }
        disabledFlag.value = false
      })
    },
  })
}

// 有图片上传处理
const uploadHandleChange = (options: { fileList: UploadFileInfo[] }) => {
  fileListLength.value = options.fileList.length
}

// 上传图片
const uploadPictureHandle = async ({
  file,
  onError
}: UploadCustomRequestOptions) => {
  let tipMessage = (window as any).$message.create(`'${file.name}'正在上传...`, {
    type: 'loading'
  });
  disabledFlag.value = true;
  await uploadPicture(file).then((resp: ResultObject<UploadFileInfoModel>) => {
    if (resp.code === StatusCode.SUCCESS && resp.status) {
      fileUrlList.value.unshift(resp.data)
      // 保存图片至相册
      savePictureToAlbumHandle()
      tipMessage.type = 'success'
      tipMessage.content = file.name + " 上传成功"
    }
  }).catch(() => {
    onError();
    fileListLength.value--;
    tipMessage.type = 'error'
    tipMessage.content = file.name + " 上传失败"
  }).finally(() => {
    disabledFlag.value = false
  })
}

// 上传图片到相册里
const savePictureToAlbumHandle = async () => {
  // 当图片已经全部上传，再进行执行保存到相册
  if (fileUrlList.value.length === fileListLength.value) {
    let tipMessage = (window as any).$message.create("正在保存...", {
      type: 'loading'
    });
    disabledFlag.value = true;
    await savePictures({ albumId: currentClickAlbumInfo.id, pictureUrlList: fileUrlList.value }).then((resp: ResultObject<null>) => {
      if (resp.code === StatusCode.SUCCESS && resp.status) {
        currentClickAlbumInfo.pictureCount += fileListLength.value;
        refreshPictureData();
        // 修改相册图片数量
        albumData.value.map((album: AlbumBackModel) => {
          if (album.id === currentClickAlbumInfo.id) {
            album.pictureCount = currentClickAlbumInfo.pictureCount;
          }
        });
        tipMessage.type = 'success'
        tipMessage.content = resp.message
      }
    }).catch(() => {
      tipMessage.type = 'error'
      tipMessage.content = '保存失败'
    }).finally(() => {
      showUploadPictureModel.value = false;
      disabledFlag.value = false;
    })
  }
}

// 刷新图片列表
const refreshPictureData = async () => {
  disabledFlag.value = true
  let tipMessage = (window as any).$message.create("重新获取中...", {
    type: 'loading'
  });
  pictureData.value.length = 0;
  pagination.value.pageSize.value = pagination.value.loadedCount + fileListLength.value
  pagination.value.page.value = pagination.value.page.default
  await createPictureData({
    albumPath: currentClickAlbumInfo.id,
    current: pagination.value.page.value,
    size: pagination.value.pageSize.value
  }).finally(() => {
    tipMessage.type = "success";
    tipMessage.content = "获取成功";
    disabledFlag.value = false;
  });
}

// 显示更多图片
const morePictureHandle = async () => {
  if (showMoreBtn.value) {
    pagination.value.page.value++;
    await createPictureData({
      albumPath: currentClickAlbumInfo.id,
      current: pagination.value.page.value,
      size: pagination.value.pageSize.value
    })
  }
}

// 关闭图片抽屉时，将分页初始化
const closeModel = () => {
  pagination.value.page.value = pagination.value.page.default
  pagination.value.pageCount.value = pagination.value.pageCount.default
  pagination.value.pageSize.value = pagination.value.pageSize.default
}

export {
  createPictureData,
  pictureData,
  hasPictureData,
  isEditPictureName,
  pictureNameInputRef,
  clickPictureNameHandle,
  pictureNameChangeHandle,
  showMovePictureAlbumModel,
  showUploadPictureModel,
  generateAlbumOptionHandel,
  movePictureAlbumHandle,
  albumOptions,
  albumValue,
  deletePictureHandle,
  fileListLength,
  fileUrlList,
  uploadHandleChange,
  uploadPictureHandle,
  savePictureToAlbumHandle,
  refreshPictureData,
  pagination,
  morePictureHandle,
  showMoreBtn,
  closeModel,
  moreLoading,
  disabledFlag
}