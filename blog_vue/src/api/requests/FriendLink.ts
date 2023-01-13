import {request} from "@/api/services"

/**
 * 获取友链列表
 * @param size
 * @param current
 */
export function getFriendLink({size, current}: ConditionParams): Promise<PageResult<Array<FriendLinkEntity>>> {
    return request.get({
        url: "/links",
        params: {size, current}
    })
}

/**
 * 保存或修改友链
 * @param data
 */
export function saveOrUpdateFriendLink(data: FormData): Promise<ResultObject<null>> {
    return request.post({
        url: "/user/link",
        data
    })
}

/**
 * 获取用户友链
 */
export function getUserFriendLink(): Promise<ResultObject<Array<UserFriendLinkEntity>>> {
    return request.get({
        url: "/user/link",
    })
}