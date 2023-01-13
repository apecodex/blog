// 分类前台实体
declare type CategoryFrontModel =  {
  id: string,
  name: string,
  articleCount: number,
}

declare type CategorySearchModel =  {
  id: string,
  name: string
}

declare type CategoryBackModel  = {
  id: string,
  name: string,
  articleCount: number,
  createTime: string,
  updateTime: string
}