// 图片懒加载
export default {
    mounted: (el: HTMLImageElement) => {
        const imgSrc = el.getAttribute("data-src");
        const observer = new IntersectionObserver(([{isIntersecting}]) => {
            // 元素出现在可视区域，和离开可视区域被触发
            if (isIntersecting) {
                // 加载图片
                el.src = imgSrc as string
                observer.unobserve(el);
            }
        })
        observer.observe(el)
    }
}