import type {AxiosInstance, AxiosRequestConfig, AxiosResponse} from "axios";
import axios from 'axios'
import {StatusCode} from "@/api/enum/StatusCode";
import {notify} from "@kyvg/vue3-notification";
import {useUserInfoStore} from "@/store"

export default class BlogXmlRequest {
  instance: AxiosInstance

  constructor(config: AxiosRequestConfig) {
    this.instance = axios.create(config)
    this.instance.interceptors.request.use(
      (config: AxiosRequestConfig) => {
        const userInfoStore = useUserInfoStore()
        if (userInfoStore.getToken()) {
          config.headers!["Authorization"] = userInfoStore.getToken();
        }
        return config
      },
      (error: any) => {
        return Promise.reject(error)
      }
    )
    this.instance.interceptors.response.use(
      (response: AxiosResponse) => {
        const userInfoStore = useUserInfoStore()
        switch (response.data.code) {
          case StatusCode.UNAUTHORIZED:
          case StatusCode.EXPIRATION:
          case StatusCode.DISABLE:
            // 删除用户信息
            userInfoStore.removeUserInfo()
            notify({
              text: response.data.message,
              type: 'warn'
            });
            break;
          case StatusCode.AUTHORIZED:
          case StatusCode.ACCESS_LIMIT:
            notify({
              text: response.data.message,
              type: 'warn'
            });
            break;
        }
        return response.data
      },
      (error: any) => {
        notify({
          title: "网络请求失败",
          text: "请求错误，请检查网络是否正常",
          type: 'error'
        });
        return Promise.reject(error)
      }
    )
  }

  request<T>(config: AxiosRequestConfig): Promise<T> {
    return new Promise<T>((resolve, reject) => {
      this.instance.request<any, T>(config).then((resp: T) => {
        resolve(resp)
      }).catch((error) => {
        reject(error)
        return error
      })
    })
  }

  get<T>(config: AxiosRequestConfig): Promise<T> {
    return this.request<T>({...config, method: "GET"})
  }

  post<T>(config: AxiosRequestConfig): Promise<T> {
    return this.request<T>({...config, method: "POST"})
  }

  delete<T>(config: AxiosRequestConfig): Promise<T> {
    return this.request<T>({...config, method: "DELETE"})
  }

  put<T>(config: AxiosRequestConfig): Promise<T> {
    return this.request<T>({...config, method: "PUT"})
  }
}
