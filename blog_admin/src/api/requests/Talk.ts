import request from '~/api/service'

/**
 * 保存或更新说说
 * @param data
 */
export function saveOrUpdateTalk(data: TalkFormParams): Promise<ResultObject<null>> {
  return request.post({
    url: '/admin/talk',
    data,
    isEncrypt: true
  })
}

/**
 * 保存说说配图
 * @param data
 */
export function saveTalkPictureVideo(data: any): Promise<ResultObject<UploadFileInfoModel>> {
  return request.post({
    url: '/admin/talk/upload',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}

/**
 * 根据id获取后台说说
 * @param id
 */
export function getTalkBackOnlyById(id: string): Promise<ResultObject<TalkBackOnlyModel>> {
  return request.get({
    url: `/admin/talk/${id}`,
    isDecrypt: true
  })
}

/**
 * 获取后台说说列表
 * @param condition
 */
export function listTalkBack(condition: ConditionParams): Promise<PageResult<Array<TalkBackModel>>> {
  return request.get({
    url: '/admin/talks',
    params: condition,
    isDecrypt: true
  })
}

/**
 * 删除说说
 * @param data
 */
export function deleteTalk(data: string): Promise<ResultObject<null>> {
  return request.delete({
    url: '/admin/talk',
    data
  })
}

/**
 * 修改说说顶置
 * @param data
 */
export function updateTalkTop(data: TalkTopParams): Promise<ResultObject<null>> {
  return request.put({
    url: '/admin/talk/top',
    data
  })
}