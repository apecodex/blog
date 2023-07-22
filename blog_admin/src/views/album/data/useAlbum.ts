import type { UploadFileInfo, UploadCustomRequestOptions } from 'naive-ui'
import type { Ref } from 'vue'
import { listAlbumBack, updateAlbumCover, updateAlbum, deleteAlbum, saveAlbum } from '~/api/requests/Album'
import { createPictureData, pictureData, pagination } from './usePicture'
import { StatusCode } from "~/api/enum/statusCode";
import { computed, reactive, ref } from "vue";

const showPicturesModel = ref(false)
// 显示修改相册模态框
const showUpdateAlbumDrawer = ref(false)
// 显示新增相册模态框
const showNewAlbumDrawer = ref(false)
// 封面
const albumCoverFile = ref<UploadFileInfo[]>([])
const albumData: Ref<Array<AlbumBackModel>> = ref<Array<AlbumBackModel>>([])
const hasAlbumData = ref(false)
// 当前点击的相册信息
const currentClickAlbumInfo: AlbumBackModel = reactive({
  id: '',
  user: null,
  albumName: '',
  albumDesc: '',
  albumCover: '',
  pictureCount: 0,
  status: false,
  createTime: '',
  updateTime: ''
})

// 分页
const albumPagination = ref({
  page: {
    value: 1,
    default: 1,
  },
  pageCount: {
    value: 1,
    default: 1
  },
  pageSize: {
    value: 50,
    default: 50
  },
})

// 搜索数据
const searchData = ref<ConditionParams>({})

const searchAlbumHandle = async () => {
  const data: ConditionParams = {}
  if (searchData.value.keywords?.length !== 0) {
    data.keywords = searchData.value.keywords;
  }
  albumData.value.length = 0
  data.current = albumPagination.value.page.value
  data.size = albumPagination.value.pageSize.value
  await createAlbumData(data);
}

// 新增或修改相册的表单信息
const albumFormInfo: { id: string, albumName: { content: string, status: boolean }, albumDesc: string, albumCover: { url: string, status: boolean }, status: boolean } = reactive({
  id: '',
  albumName: {
    content: '',
    status: false
  },
  albumDesc: '',
  albumCover: {
    url: '',
    status: false
  },
  status: false,
})

// 恢复默认相册信息
const clearFormAlbumInfo = () => {
  albumFormInfo.id = ''
  albumFormInfo.albumName = {
    content: '',
    status: false
  }
  albumFormInfo.albumDesc = ''
  albumFormInfo.albumCover = {
    url: '',
    status: false
  }
  albumFormInfo.status = false
  albumCoverFile.value.length = 0
}

// 展示更新相册抽屉
const showUpdateAlbumDrawerHandle = (album: AlbumBackModel) => {
  albumCoverFile.value.length = 0;
  albumFormInfo.id = album.id;
  albumFormInfo.albumName.content = album.albumName;
  albumFormInfo.albumDesc = album.albumDesc;
  albumFormInfo.albumCover.url = album.albumCover;
  albumFormInfo.status = album.status;
  let fileName = album.albumCover.substring(album.albumCover.lastIndexOf("/") + 1);
  albumCoverFile.value.push({
    id: album.id,
    name: fileName,
    status: 'finished',
    url: album.albumCover
  });
  showUpdateAlbumDrawer.value = true;
}

const createAlbumData = async (condition: ConditionParams) => {
  hasAlbumData.value = false
  await listAlbumBack(condition).then((resp: PageResult<Array<AlbumBackModel>>) => {
    if (resp.code === StatusCode.SUCCESS && resp.status) {
      albumData.value = Array.from(resp.data.recordList);
      hasAlbumData.value = resp.data.count === 0;
    }
  }).catch(() => {
    (window as any).$message.error("获取相册列表失败，请重试");
  })
}

// 查看相册的所有图片
const openPictureHandle = async (album: AlbumBackModel) => {
  currentClickAlbumInfo.id = album.id;
  currentClickAlbumInfo.user = album.user;
  currentClickAlbumInfo.albumName = album.albumName;
  currentClickAlbumInfo.albumDesc = album.albumDesc;
  currentClickAlbumInfo.albumCover = album.albumCover;
  currentClickAlbumInfo.pictureCount = album.pictureCount;
  currentClickAlbumInfo.status = album.status;
  currentClickAlbumInfo.createTime = album.createTime;
  currentClickAlbumInfo.updateTime = album.updateTime;
  showPicturesModel.value = true;
  pictureData.value.length = 0;
  await createPictureData({ albumPath: album.id, current: pagination.value.page.value, size: pagination.value.pageSize.value });
}

// 删除封面
const removeAlbumCoverHandle = (options: { file: UploadFileInfo, fileList: Array<UploadFileInfo>, event?: Event }) => {
  albumFormInfo.albumCover.url = "";
  albumCoverFile.value.length = 0;
}

// 新增或修改相册前进行验证是否填写
const checkAlbumInfo = computed(() => {
  albumFormInfo.albumName.content.length === 0 ? albumFormInfo.albumName.status = true : albumFormInfo.albumName.status = false;
  albumFormInfo.albumCover.url.length === 0 ? albumFormInfo.albumCover.status = true : albumFormInfo.albumCover.status = false;
  return albumFormInfo.albumName.status || albumFormInfo.albumCover.status;
})

// 上传封面
const uploadAlbumCoverHandel = async ({ file, onFinish, onError, }: UploadCustomRequestOptions) => {
  albumCoverFile.value.length = 0;
  let tipMessage = (window as any).$message.create("封面上传中...", {
    type: 'loading'
  });
  await updateAlbumCover(file).then((resp: ResultObject<UploadFileInfoModel>) => {
    if (resp.status) {
      console.log(resp);

      albumFormInfo.albumCover.url = resp.data.url;
      albumCoverFile.value.push({
        id: file.id,
        name: file.name,
        status: 'finished',
        url: resp.data.url
      });
      onFinish();
      tipMessage.type = 'success';
      tipMessage.content = resp.message;
    }
  }).catch(() => {
    onError();
    tipMessage.type = 'error';
    tipMessage.content = '删除失败';
  })
}

// 修改相册信息
const updateAlbumHandle = async () => {
  if (checkAlbumInfo) {
    let tipMessage = (window as any).$message.create("正在修改...", {
      type: 'loading'
    });
    await updateAlbum({
      albumName: albumFormInfo.albumName.content,
      albumId: albumFormInfo.id,
      albumDesc: albumFormInfo.albumDesc,
      albumCover: albumFormInfo.albumCover.url,
      status: albumFormInfo.status
    }).then((resp: ResultObject<null>) => {
      if (resp.code === StatusCode.SUCCESS && resp.status) {
        showUpdateAlbumDrawer.value = false;
        // 修改相册信息
        albumData.value.map((album: AlbumBackModel) => {
          if (album.id === albumFormInfo.id) {
            album.albumName = albumFormInfo.albumName.content;
            album.albumDesc = albumFormInfo.albumDesc;
            album.albumCover = albumFormInfo.albumCover.url;
            album.status = albumFormInfo.status;
          }
        });
        tipMessage.type = 'success';
        tipMessage.content = resp.message;
      }
    }).catch(() => {
      tipMessage.type = 'error';
      tipMessage.content = '删除失败';
    })
  }
}

// 删除相册
const deleteAlbumHandle = (album: AlbumBackModel) => {
  (window as any).$dialog.warning({
    title: '警告',
    content: '你确定删除吗？将导致该相册的图片全部删除！',
    positiveText: '确定',
    negativeText: '手误',
    onPositiveClick: async () => {
      let tipMessage = (window as any).$message.create(`正在删除${album.albumName}...`, {
        type: 'loading'
      });
      await deleteAlbum(album.id).then((resp: ResultObject<null>) => {
        if (resp.code === StatusCode.SUCCESS && resp.status) {
          const albumFilter = albumData.value.filter((a: AlbumBackModel) => a.id !== album.id);
          albumData.value.length = 0;
          albumData.value = Array.from(albumFilter);
          tipMessage.type = 'success';
          tipMessage.content = `${album.albumName} 删除成功`;
        }
        if (resp.code === StatusCode.FAIL) {
          tipMessage.type = 'warning';
          tipMessage.content = resp.message;
        }
      }).catch(() => {
        tipMessage.type = 'error';
        tipMessage.content = `${album.albumName} 删除失败`;
      }).finally(() => {
        if (albumData.value.length === 0) {
          hasAlbumData.value = true;
        }
      })
    },
  })
}

// 关闭抽屉时清空相册表单
const closeAlbumModel = () => {
  clearFormAlbumInfo();
}

// 新增相册处理
const newAlbumHandle = async () => {
  if (checkAlbumInfo) {
    let tipMessage = (window as any).$message.create("保存中...", {
      type: 'loading'
    });
    await saveAlbum({
      albumName: albumFormInfo.albumName.content,
      albumDesc: albumFormInfo.albumDesc,
      albumCover: albumFormInfo.albumCover.url,
      status: albumFormInfo.status
    }).then((resp: ResultObject<null>) => {
      if (resp.code === StatusCode.SUCCESS && resp.status) {
        albumData.value.length = 0
        createAlbumData({ current: albumPagination.value.page.value, size: albumPagination.value.pageSize.value });
        tipMessage.type = 'success';
        tipMessage.content = resp.message;
      }
      if (resp.code === StatusCode.FAIL) {
        tipMessage.type = 'warning';
        tipMessage.content = resp.message;
      }
    }).catch(() => {
      tipMessage.type = 'error';
      tipMessage.content = '删除失败';
    }).finally(() => {
      showNewAlbumDrawer.value = false;
    })
  }
}

export {
  createAlbumData,
  albumPagination,
  albumData,
  hasAlbumData,
  searchData,
  searchAlbumHandle,
  albumCoverFile,
  openPictureHandle,
  showPicturesModel,
  showUpdateAlbumDrawer,
  showNewAlbumDrawer,
  showUpdateAlbumDrawerHandle,
  currentClickAlbumInfo,
  removeAlbumCoverHandle,
  albumFormInfo,
  uploadAlbumCoverHandel,
  updateAlbumHandle,
  newAlbumHandle,
  deleteAlbumHandle,
  closeAlbumModel,
  checkAlbumInfo
}