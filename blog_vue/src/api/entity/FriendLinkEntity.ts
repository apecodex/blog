declare type FriendLinkEntity = {
  user: SimpleUserInfoEntity
  linkName: string
  linkAvatar: string
  linkIntro: string
  linkUrl: string
  createTime: string
}

declare type UserFriendLinkEntity = {
  id: string
  user: SimpleUserInfoEntity | null
  linkName: string
  linkAvatar: string
  linkIntro: string
  linkUrl: string
  isReview: boolean
  remark: string
  createTime: string
  updateTime: string
}