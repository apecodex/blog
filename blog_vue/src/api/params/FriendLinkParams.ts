declare type FriendLinkParams = {
  id?: string
  linkAvatar: string
  linkIntro: string
  linkName: string
  linkUrl: string
}

/**
 * 用户保存或修改友链
 */
declare type SaveOrUpdateFriendLink = {
  id?: string
  linkName: string
  linkAvatar: string
  linkUrl: string
  linkIntro: string
}