import {request} from "@/api/services"

/**
 * 获取用户通知列表
 * @param isRead
 * @param current
 * @param size
 */
export function getUserNotices(isRead: boolean, {current, size}: ConditionParams): Promise<PageResult<Array<NoticeEntity>>> {
    return request.get({
        url: "/user/notice",
        params: {isRead, current, size},
        isDecrypt: true
    })
}

/**
 * 更新通知已阅
 * @param noticeList
 */
export function updateNotice(noticeList: Array<String>): Promise<ResultObject<null>> {
    return request.put({
        url: "/user/notice",
        data: noticeList
    })
}

/**
 * 获取用户未阅通知数量
 */
export function getUserNoReadNoticeCount(): Promise<ResultObject<number>> {
    return request.get({
        url: "/user/notice/noread"
    })
}