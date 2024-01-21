import {request} from "@/api/services"
import {useWebsiteInfoStore} from "@/store";
import {storeToRefs} from "pinia";

/**
 * 获取说说列表
 * @param size
 * @param current
 */
export function getTalk({size, current}: ConditionParams): Promise<PageResult<Array<TalkEntity>>> {
    return request.get({
        url: "/talks",
        params: {size, current},
        isDecrypt: true
    })
}

/**
 * 根据说说id获取说说
 * @param talkId
 */
export function getTalkByTalkId(talkId: string): Promise<ResultObject<TalkEntity>> {
    const {rectangle} = storeToRefs(useWebsiteInfoStore());
    return request.get({
        headers: {
            "rectangle": rectangle.value
        },
        url: `/talk/${talkId}`,
        isDecrypt: true
    })
}

/**
 * 点赞说说
 * @param talkId
 */
export function talkLike(talkId: string): Promise<ResultObject<null>> {
    return request.post({
        url: `/user/talk/${talkId}/like`
    })
}