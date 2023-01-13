import axios from "axios";

export function getYiYan(): Promise<YiYanEntity> {
    return axios.get('https://v1.hitokoto.cn').then((resp) => resp.data)
}