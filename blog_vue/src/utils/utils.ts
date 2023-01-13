import {smilingFaces, humans, animalsAndNature, others} from "@/utils/emojis"
import confetti from "canvas-confetti";

const emojis = {...smilingFaces, ...humans, ...animalsAndNature, ...others} as any

/**
 * 判断设备
 */
export const isMobile = () => {
    return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
}

export const scrollDown = () => {
    scrollSmoothTo(document.documentElement.clientHeight)
}

export const scrollUp = () => {
    scrollSmoothTo(0)
}

export const scrollCustomize = (scrollNum: number) => {
    window.scrollTo({
        behavior: "smooth",
        top: scrollNum
    });
}

export const scrollSmoothTo = (position: any) => {
    if (!window.requestAnimationFrame) {
        (<any>window.requestAnimationFrame) = function (callback: FrameRequestCallback, element: any) {
            return setTimeout(callback, 17);
        };
    }
    // 当前滚动高度
    let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    // 滚动step方法
    let step = function () {
        // 距离目标滚动距离
        let distance = position - scrollTop;
        // 目标滚动位置
        scrollTop = scrollTop + distance / 5;
        if (Math.abs(distance) < 1) {
            window.scrollTo(0, position);
        } else {
            window.scrollTo(0, scrollTop);
            requestAnimationFrame(step);
        }
    };
    if (typeof window.getComputedStyle(document.body).scrollBehavior == "undefined") {
        step();
    } else {
        scrollCustomize(position)
    }
}

export const handlerDateDurationCurrent = (time: number) => {
    if (String(time).length !== 13) {
        time = time * 1000
    }
    let msPerDay = 24 * 60 * 60 * 1000;
    let timeOld = new Date().getTime() - new Date(time).getTime();
    let daysOld = Math.floor(timeOld / msPerDay);
    let str = "";
    let day = new Date();
    str += daysOld + "天";
    str += day.getHours() + "时";
    str += day.getMinutes() + "分";
    str += day.getSeconds() + "秒";
    return str;
}

export function removeHTMLTag(htmlStr: string) {
    htmlStr = htmlStr.replace(/<\/?[^>]*>/g, ''); //去除HTML tag
    htmlStr = htmlStr.replace(/[\\\`\*\_\[\]\#\+\-\!\>]/g, '');
    htmlStr = htmlStr.replace(/[ | ]*\n/g, '\n'); //去除行尾空白
    htmlStr = htmlStr.replace(/\n[\s| | ]*\r/g, '\n'); //去除多余空行
    htmlStr = htmlStr.replace(/&nbsp;/ig, '');//去掉&nbsp;
    return htmlStr.trim();
}

// 防抖
export const debounce = (delayedFunc: Function, delay: number, immediatelyFunc?: Function) => {
    // 定时器
    let timer: NodeJS.Timeout;
    // 将debounce处理结果当作函数返回
    return () => {
        if (immediatelyFunc) immediatelyFunc()
        // 每次事件被触发时，都去清除之前的旧定时器
        if (timer) {
            clearTimeout(timer)
        }
        // 设立新定时器
        timer = setTimeout(() => {
            delayedFunc()
        }, delay)
    }
}


export const random = (min: number, max: number) => {
    return Math.floor(Math.random() * (max - min)) + min;
}

// 时间戳转多少分钟之前
export const getDateDiff = (dateTimeStamp: string) => {
    // 时间字符串转时间戳
    let timestamp = new Date(dateTimeStamp).getTime();
    let minute = 1000 * 60;
    let hour = minute * 60;
    let day = hour * 24;
    let month = day * 30;
    let year = day * 365;
    let now = new Date().getTime();
    let diffValue = now - timestamp;
    let result;
    if (diffValue < 0) {
        return;
    }
    let yearC = diffValue / year;
    let monthC = diffValue / month;
    let weekC = diffValue / (7 * day);
    let dayC = diffValue / day;
    let hourC = diffValue / hour;
    let minC = diffValue / minute;
    if (yearC >= 1) {
        result = "" + parseInt(String(yearC)) + "年前";
    } else if (monthC >= 1) {
        result = "" + parseInt(String(monthC)) + "月前";
    } else if (weekC >= 1) {
        result = "" + parseInt(String(weekC)) + "周前";
    } else if (dayC >= 1) {
        result = "" + parseInt(String(dayC)) + "天前";
    } else if (hourC >= 1) {
        result = "" + parseInt(String(hourC)) + "小时前";
    } else if (minC >= 1) {
        result = "" + parseInt(String(minC)) + "分钟前";
    } else
        result = "刚刚";
    return result;
}

// 解析emoji
export const parseComment = (comment: string, width: string, height: string) => {
    let reg = /\[.+?\]/g;
    return comment.replace(reg, (str: string) => {
        return `<img class="inline-block mx-1px align-text-bottom" title="${str}" src="${emojis[str]}" width="${width}" height="${height}" alt="" />`;
    })
}

export const getDate = (dateStr: string) => {
    const date = new Date(dateStr);
    const hours = date.getHours() >= 10 ? date.getHours() : `0${date.getHours()}`
    const minutes = date.getMinutes() >= 10 ? date.getMinutes() : `0${date.getMinutes()}`
    const seconds = date.getSeconds() >= 10 ? date.getSeconds() : `0${date.getSeconds()}`
    return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日 ${hours}:${minutes}:${seconds}`
}

// 五彩纸片
export const customConfetti = () => {
    confetti({
        angle: 60,
        spread: 55,
        decay: 0.91,
        scalar: 1.4,
        origin: {x: 0},
    });
    confetti({
        angle: 120,
        spread: 55,
        decay: 0.91,
        scalar: 1.4,
        origin: {x: 1},
    });
}

export const timingConfetti = () => {
    customConfetti();
    let runTime = 0;
    const time = setInterval(() => {
        if (runTime == 1) {
            clearInterval(time);
        }
        runTime++;
        customConfetti();
        customConfetti();
    }, 1200);
}

export const matchEmail = (email: string): boolean => {
    let emailReg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    return emailReg.test(email);
}

export const matchPassword = (password: string): boolean => {
    let passwordReg = /(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$/
    return passwordReg.test(password);
}