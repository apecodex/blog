import type {AxiosInstance, AxiosRequestConfig, AxiosResponse} from 'axios'
import axios from 'axios'
import {StatusCode} from './enum/statusCode'
import {router} from '~/router';
import {useMenuStore, useUserInfoStore} from '~/store'

export default class XmlRequest {
  instance: AxiosInstance

  constructor(config: AxiosRequestConfig) {
    this.instance = axios.create(config);
    this.instance.interceptors.request.use(
      (config: any) => {
        let userInfoStore = useUserInfoStore();
        if (userInfoStore.getToken()) {
          config.headers['Authorization'] = userInfoStore.getToken()
        }
        return config;
      },
      (error) => {
        (<any>window).$message.error("未知错误！");
        return Promise.reject(error)
      }
    )
    this.instance.interceptors.response.use(
      (response: AxiosResponse<any, any>) => {
        const userInfoStore = useUserInfoStore();
        const menuStore = useMenuStore();
        switch (response.data.code) {
          case StatusCode.UNAUTHORIZED:
          case StatusCode.EXPIRATION:
          case StatusCode.DISABLE:
            userInfoStore.removeUserInfo();
            menuStore.clearMenu();
            (<any>window).$message.error(response.data.message);
            router.replace({path: "/login"});
            break;
          case StatusCode.AUTHORIZED:
          case StatusCode.ACCESS_LIMIT:
            (<any>window).$message.error(response.data.message);
            break;
        }
        return response.data
      },
      (error) => {
        (<any>window).$message.error("请求错误，请检查网络是否正常");
        return Promise.reject(error)
      }
    )
  }

  request<T>(config: AxiosRequestConfig): Promise<T> {
    return new Promise((resolve, reject) => {
      this.instance.request<any, T>(config).then((res: T) => {
        resolve(res)
      }).catch((err) => {
        reject(err)
        return err
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