import request from '../service'

/**
 * 获取后台通知
 * @param params 参数
 * @returns
 */

export function listNoticeBack(params: NoticeParams): Promise<PageResult<Array<NoticeBackModel>>> {
  return request.get({
    url: "/admin/notice",
    params,
    isDecrypt: true
  })
}

/**
 * 更新通知已阅
 * @param ids 消息id
 * @returns 
 */
export function updateNotice(ids: Array<string>): Promise<ResultObject<null>> {
  return request.put({
    url: "/user/notice",
    data: ids
  })
}

/**
 * 删除通知
 * @param ids
 */
export function deleteNotice(ids: Array<string>): Promise<ResultObject<null>> {
  return request.delete({
    url: "/admin/notice",
    data: {
      idList: ids,
      isDelete: true
    },
    isEncrypt: true
  })
}

/**
 * 发送通知
 * @param data
 */
export function sendNotice(data: SendNoticeParams): Promise<ResultObject<null>> {
  return request.post({
    url: "/admin/notice",
    data,
    isEncrypt: true
  })
}