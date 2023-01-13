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