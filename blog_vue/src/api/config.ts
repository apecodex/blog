import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from "axios";
import axios from 'axios'
import { StatusCode } from "@/api/enum/StatusCode";
import { notify } from "@kyvg/vue3-notification";
import { useUserInfoStore } from "@/store"
import AESUtil from '@/utils/aes';
import RSAUtil from "@/utils/rsa"

export interface EncryptAxiosRequestConfig extends AxiosRequestConfig {
  isDecrypt?: boolean,
  isEncrypt?: boolean,
}

export default class BlogXmlRequest {
  instance: AxiosInstance

  constructor(config: EncryptAxiosRequestConfig) {
    this.instance = axios.create(config)
    this.instance.interceptors.request.use(
      (config: EncryptAxiosRequestConfig) => {
        const userInfoStore = useUserInfoStore()
        if (userInfoStore.getToken()) {
          config.headers!["Authorization"] = userInfoStore.getToken();
        }
        // 需要加密
        if (config.isEncrypt && config.isEncrypt == true) {
          let data = JSON.stringify(config.data);
          const key: string = AESUtil.getKey();
          const keyIV: string = AESUtil.getKey();
          const aesKey: {key: string, keyIV: string} = {key: key, keyIV: keyIV};
          // 通过aes密钥和aes偏移量加密请求体
          const aesEncodeParams: string = AESUtil.encrypt(key, data, keyIV) as string;
          // 通过rsa加密aes密钥
          const aesEncryptByPublicKey: string = RSAUtil.encryptByPublicKey(aesKey) as string;
          const requestDataExtend: {data: string, sym: string} = {
            data: aesEncodeParams,
            sym: aesEncryptByPublicKey
          }
          config.data = requestDataExtend
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
        // 需要解密
        if (response.config.isDecrypt && response.config.isDecrypt === true) {
          let respDataExtend: { body: string, sym: string } = response.data.data;
          // 通过rsa私钥解密获取aes密钥
          const aesDncryptByPrivateKey: string = RSAUtil.decryptByPrivateKey(respDataExtend.sym) as string;
          const aesKey: { key: string, keyIV: string } = JSON.parse(aesDncryptByPrivateKey);
          // 通过aes密钥解密获取原数据
          const body = AESUtil.decrypt(aesKey.key, respDataExtend.body, aesKey.keyIV) as string;
          response.data.data = JSON.parse(body);
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

  request<T>(config: EncryptAxiosRequestConfig): Promise<T> {
    return new Promise<T>((resolve, reject) => {
      this.instance.request<any, T>(config).then((resp: T) => {
        resolve(resp)
      }).catch((error) => {
        reject(error)
        return error
      })
    })
  }

  get<T>(config: EncryptAxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: "GET" })
  }

  post<T>(config: EncryptAxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: "POST" })
  }

  delete<T>(config: EncryptAxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: "DELETE" })
  }

  put<T>(config: EncryptAxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: "PUT" })
  }
}
