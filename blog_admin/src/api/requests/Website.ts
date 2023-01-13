import requests from '../service'

// 获取后台信息
export function getWebsiteInfoBack(): Promise<ResultObject<WebsiteInfoBackModel>> {
  return requests.get({
    url: "/admin"
  })
}

/**
 * 获取网站配置
 */
export function getWebsiteConfigure(): Promise<ResultObject<WebsiteConfigureModel>> {
  return requests.get({
    url: '/admin/website/config'
  })
}

/**
 * 更新网站配置
 * @param data
 */
export function updateWebsiteConfigure(data: WebsiteConfigureModel): Promise<ResultObject<null>> {
  return requests.put({
    url: '/admin/website/config',
    data
  })
}

// 上传网站配置图片
export function uploadWebsiteConfigurePicture(file: any): Promise<ResultObject<UploadFileInfoModel>> {
  return requests.post({
    url: '/admin/config/images',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: file
  })
}