import type {Ref} from "vue";
import {ref} from "vue";
import {notify} from "@kyvg/vue3-notification";
import {useSettingStore} from "@/store";


const rightMenu: Ref<{
  flag: boolean,
  menuWidth: number,
  menuHeight: number,
  left: number,
  top: number,
  pluginMode: boolean
  copyText: string,
  href: string,
  imgSrc: string
}> = ref({
  flag: false,
  menuWidth: 0,
  menuHeight: 0,
  left: 0,
  top: 0,
  pluginMode: false,
  copyText: "",
  href: "",
  imgSrc: ""
});

// 选中的文本
const selectText = () => {
  let txt;
  if (document.selection) {
    txt = document.selection.createRange().text;
  } else {
    txt = window.getSelection() + '';
  }
  if (txt) {
    rightMenu.value.copyText = txt;
  }
}

// 复制
const copyText = (txt: string) => {
  clipboardWriteText(txt);
}

// 百度搜索
const searchBaidu = () => {
  notify({
    text: "即将跳转到百度搜索",
  })
  window.open('https://www.baidu.com/s?wd=' + rightMenu.value.copyText, "_blank");
  rightMenu.value.flag = false;
}

// 复制页面url
const copyPageUrl = () => {
  let url = window.location.href;
  clipboardWriteText(url);
}

// 执行复制文本
const clipboardWriteText = (txt: string)  => {
  if (navigator.clipboard) {
    navigator.clipboard.writeText(txt).then(() => {
      notify({
        text: "复制成功，复制和转载请标注本文地址",
        type: "success"
      })
    }).catch(() => {
      notify({
        text: "复制失败",
        type: "success"
      })
    });
  }
  rightMenu.value.flag = false;
  rightMenu.value.copyText = "";
}

// 修改主题
const changeTheme = () => {
  const settingStore = useSettingStore();
  settingStore.changeTheme()
  rightMenu.value.flag = false;
}

const closeRightMenu = () => {
  rightMenu.value.flag = false;
  rightMenu.value.pluginMode = false;
}

const pauseText = () => {
  notify({
    text: "请按 Ctrl + v"
  });
  rightMenu.value.flag = false;
}

export {
  rightMenu,
  selectText,
  copyText,
  searchBaidu,
  copyPageUrl,
  changeTheme,
  closeRightMenu,
  pauseText
}