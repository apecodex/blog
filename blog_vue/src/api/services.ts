import BlogXmlRequest from "@/api/config";

let blogBase = import.meta.env.VITE_API_URL;

const request = new BlogXmlRequest({
    timeout: 10000,
    baseURL: `${blogBase}`,
    headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Content-Type': 'application/json; charset=utf-8'
    }
})


export {
    request,
}