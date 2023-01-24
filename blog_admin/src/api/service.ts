import XmlRequest from "./config";

let base = "/api/";

const request = new XmlRequest({
  timeout: 10000,
  baseURL: `${base}`,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
    'Content-Type': 'application/json; charset=utf-8'
  }
})

export default request;