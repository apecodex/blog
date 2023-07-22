declare type TalkFormParams = {
  id?: string
  files?: Array<string>
  content: string
  isTop: boolean
  status: number
}

/**
 * 说说顶置
 */
declare type TalkTopParams = {
  id: string
  isTop: boolean
}