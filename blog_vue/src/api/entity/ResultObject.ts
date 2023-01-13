/**
 * 返回结果
 */

declare type ResultObject<T> = {
    status: boolean;
    data: T,
    code: number,
    message: string
}

/**
 * 多页结果
 */
declare type PageResult<T> = {
    status: boolean
    data: {
        count: number
        recordList: T
    },
    code: number,
    message: string
}

/**
 * 带有其他对象的页面结果
 */
declare type PageResultWithObject<T, E> = {
    status: boolean
    data: {
        count: number
        recordList: T
        data: E
    },
    code: number,
    message: string
}