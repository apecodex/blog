import { request } from '@/api/services'

export function getOnlineCount(): Promise<ResultObject<number>> {
  return request.get({
    url: '/getOnlineCount'
  })
}