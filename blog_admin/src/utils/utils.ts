import {smilingFaces, humans, animalsAndNature, others} from "~/utils/dynamicEmojis"

const emojis = {...smilingFaces, ...humans, ...animalsAndNature, ...others} as any

/**
 * 判断设备
 */
export const isMobile = () => {
    return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
}

// 解析emoji
export const parseComment = (comment: string, width: string, height: string) => {
    let reg = /\[.+?\]/g;
    return comment.replace(reg, (str: string) => {
        return `<img class="inline-block mx-1px align-text-bottom" title="${str}" src="${emojis[str]}" width="${width}" height="${height}" alt="" />`;
    })
}
