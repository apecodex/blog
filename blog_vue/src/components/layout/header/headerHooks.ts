import { computed, ref, Ref } from "vue";
import { rightMenu } from "@/components/rightMenu/rightMenuHooks"

const navAreaRef: Ref<HTMLElement | null> = ref(null)
const navAreaClassActive = ref("")
const effect1configClassActive = ref("")
const websiteClassActive = ref("")

const navAreaClassComp = computed({
  get: () => navAreaClassActive.value,
  set: (v: string) => {
    navAreaClassActive.value = v
  }
})

const websiteClassComp = computed({
  get: () => websiteClassActive.value,
  set: (v: string) => {
    websiteClassActive.value = v
  }
})

const effect1configClassComp = computed({
  get: () => effect1configClassActive.value,
  set: (v: string) => {
    effect1configClassActive.value = v
  }
})

const navScrollHandle = () => {
  let lastScrollTop = navAreaRef.value?.scrollHeight as number;
  window.addEventListener('scroll', () => {
    let scrollTop = window.pageYOffset || document.documentElement.scrollTop
    if (scrollTop > lastScrollTop) {
      navAreaClassComp.value = "nav-fixed"
      websiteClassComp.value = 'website-sticky'
      effect1configClassComp.value = "effect-fixed"
    } else {
      navAreaClassComp.value = 'nav-sticky'
      websiteClassComp.value = "website-fixed"
      effect1configClassComp.value = "effect-sticky"
    }
    if (scrollTop === 0) {
      navAreaClassComp.value = "nav-top"
      effect1configClassComp.value = "effect-top"
      websiteClassComp.value = ""
    }
    lastScrollTop = scrollTop;
    // 关闭右键菜单
    rightMenu.value.flag = false;
  })
}

export {
  navAreaRef,
  navAreaClassActive,
  effect1configClassActive,
  websiteClassActive,
  navScrollHandle
}