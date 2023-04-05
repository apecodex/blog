import XmlRequest from "./config";

let base = import.meta.env.VITE_API_URL;

const request = new XmlRequest({
  timeout: 10000,
  baseURL: `${base}`,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
    'Content-Type': 'application/json; charset=utf-8'
  }
})

export default request;