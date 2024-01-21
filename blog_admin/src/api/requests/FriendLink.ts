import request from '~/api/service'

/**
 * 获取后台友链
 * @param condition
 */
export function listFriendLinkBack(condition: ConditionParams): Promise<PageResult<Array<FriendLinkBackModel>>> {
  return request.get({
    url: '/admin/links',
    params: condition,
    isDecrypt: true
  })
}

/**
 * 修改友链审核
 * @param data
 */
export function UpdateFriendLinkReview(data: ReviewParams): Promise<ResultObject<null>> {
  return request.put({
    url: '/admin/links',
    data
  })
}

/**
 * 删除友链
 * @param friendLinkId
 */
export function deleteFriendLink(friendLinkId: string): Promise<ResultObject<null>> {
  return request.delete({
    url: '/admin/link/' + friendLinkId
  })
}

/**
 * 新增或修改友链
 * @param data
 */
export function saveOrUpdateFriendLink(data: FriendLinkParams): Promise<ResultObject<null>> {
  return request.post({
    url: '/admin/link',
    data,
    isEncrypt: true
  })
}