import {saveOrUpdateArticle, getArticleBackByArticleId} from '~/api/requests/Article'
import {StatusCode} from '~/api/enum/statusCode';
import {router} from '~/router';
import {fileList} from './useUpload';
import {reactive, ref, toRaw} from "vue";

export {
  categoryOptions,
  categoryOptionLoading,
  initCategoryOption,
  categorySearch
} from './useCategory'

export {
  fileList,
  uploadCover,
  handleRemove,
  uploadImg
} from './useUpload'

const showSaveArticleDrawer = ref<boolean>(false)
// 文章信息
const articleInfo: SaveOrUpdateArticleParams = reactive<SaveOrUpdateArticleParams>({
  articleTitle: "",
  articleCover: "",
  categoryName: null,
  articleContent: "",
  isTop: false,
  originalUrl: "",
  status: 2,
  type: 1,
  tagNameList: []
})

// 提示
const articleInfoStatus = reactive({
  articleTitleStatus: false,
  categoryStatus: false,
  originalUrlStatus: false,
  articleCoverStatus: false,
  tagsStatus: false
})

const clearArticleInfo = () => {
  articleInfo.articleTitle = "";
  articleInfo.articleCover = "";
  articleInfo.categoryName = null;
  articleInfo.isTop = false;
  articleInfo.originalUrl = "";
  articleInfo.status = 2;
  articleInfo.type = 1;
  articleInfo.tagNameList = [];
  articleInfo.articleContent = ""
  fileList.value = []
  // 重置提示
  articleInfoStatus.articleTitleStatus = false
  articleInfoStatus.categoryStatus = false
  articleInfoStatus.originalUrlStatus = false
  articleInfoStatus.articleCoverStatus = false
  articleInfoStatus.tagsStatus = false
}

// 保存或更新文章
const saveOrUpdateArticleHandle = async (articleParams: SaveOrUpdateArticleParams) => {
  let tipMessage = (window as any).$message.create("保存中...", {
    type: 'loading'
  });
  await saveOrUpdateArticle(articleParams).then((resp: ResultObject<null>) => {
    if (resp.code === StatusCode.SUCCESS && resp.status) {
      // 关闭抽屉
      showSaveArticleDrawer.value = false;
      // 清除数据
      clearArticleInfo();
      tipMessage.type = 'success'
      tipMessage.content = resp.message
      // 跳转至文章列表
      router.replace({path: '/article-list'})
    }
  }).catch(() => {
    tipMessage.type = 'error'
    tipMessage.content = '删除失败'
  })
}

const checkArticleInfo = (): boolean => {
  articleInfo.articleTitle.length === 0 ? articleInfoStatus.articleTitleStatus = true : articleInfoStatus.articleTitleStatus = false
  articleInfo.categoryName === null ? articleInfoStatus.categoryStatus = true : articleInfoStatus.categoryStatus = false;
  articleInfo.type === 2 && articleInfo.originalUrl?.length === 0 ? articleInfoStatus.originalUrlStatus = true : articleInfoStatus.originalUrlStatus = false;
  articleInfo.articleCover.length === 0 ? articleInfoStatus.articleCoverStatus = true : articleInfoStatus.articleCoverStatus = false;
  articleInfo.tagNameList.length === 0 ? articleInfoStatus.tagsStatus = true : articleInfoStatus.tagsStatus = false;
  if (
    articleInfo.articleTitle.length === 0 ||
    articleInfo.categoryName === null ||
    articleInfo.articleCover.length === 0 ||
    articleInfo.tagNameList.length === 0) {
    return false
  }
  if (articleInfo.type === 2 && articleInfo.originalUrl?.length === 0) {
    return false
  } else {
    articleInfo.originalUrl = null as any
  }
  if (articleInfo.articleContent.length === 0) {
    (window as any).$message.error("还未撰写文章")
    return false
  }
  return true
}
// 处理保存文章按钮
const saveOrUpdateArticleFunc = async () => {
  const isOk = checkArticleInfo()
  if (isOk) {
    const params = toRaw<any>(articleInfo);
    await saveOrUpdateArticleHandle(params);
  }

}

// 保存草稿
const saveDraftArticleFunc = async () => {
  const isOk = checkArticleInfo()
  if (isOk) {
    articleInfo.status = 3
    const params = toRaw<any>(articleInfo);
    await saveOrUpdateArticleHandle(params)
  }
}

const pullArticleById = async (id: string) => {
  clearArticleInfo();
  let tipMessage = (window as any).$message.create("正在获取文章...", {
    type: 'loading'
  });
  articleInfo.id = id as any
  await getArticleBackByArticleId(id).then((articleBack) => {
    if (articleBack.code === StatusCode.SUCCESS && articleBack.status) {
      articleInfo.articleTitle = articleBack.data.articleTitle
      articleInfo.categoryName = articleBack.data.categoryName
      articleInfo.isTop = articleBack.data.isTop
      articleInfo.status = articleBack.data.status
      articleInfo.type = articleBack.data.type
      articleInfo.originalUrl = articleBack.data.originalUrl
      articleInfo.tagNameList = articleBack.data.tagNameList
      articleInfo.articleCover = articleBack.data.articleCover
      articleInfo.articleContent = articleBack.data.articleContent
      let fileName = articleInfo.articleCover.substring(articleInfo.articleCover.lastIndexOf("/") + 1)
      fileList.value.push({
        id: articleBack.data.id,
        name: fileName,
        status: 'finished',
        url: articleBack.data.articleCover
      } as never);
      tipMessage.type = 'success'
      tipMessage.content = '获取成功'
    }
  }).catch(() => {
    tipMessage.type = 'error'
    tipMessage.content = '文章获取失败，请重试！'
  })
}

export {
  showSaveArticleDrawer,
  articleInfo,
  articleInfoStatus,
  clearArticleInfo,
  saveOrUpdateArticleFunc,
  saveDraftArticleFunc,
  pullArticleById
}