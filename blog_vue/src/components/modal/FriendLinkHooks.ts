import {computed, reactive, ref} from "vue";

const lockBtn = ref(false)

const friendLinkForm = reactive({
    id: "",
    linkAvatar: "",
    linkIntro: "",
    linkName: "",
    linkUrl: ""
} as FriendLinkParams)

// 清空表单
const clearFriendLinkForm = () => {
    friendLinkForm.id = "";
    friendLinkForm.linkAvatar = "";
    friendLinkForm.linkIntro = "";
    friendLinkForm.linkName = "";
    friendLinkForm.linkUrl = "";
}

// 是否填了
const checkFriendLinkForm = computed(() => {
    const linkAvatar = friendLinkForm.linkAvatar?.length === 0;
    const linkName = friendLinkForm.linkName?.length === 0;
    const linkUrl = friendLinkForm.linkUrl?.length === 0;
    return linkAvatar || linkName || linkUrl || !lockBtn;
})


export {
    lockBtn,
    friendLinkForm,
    clearFriendLinkForm,
    checkFriendLinkForm
}