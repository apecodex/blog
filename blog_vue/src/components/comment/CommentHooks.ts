import type {Ref} from "vue";
import {ref, reactive} from "vue";
import {getCommentList, getReplyCommentList, insertComment} from "@/api/requests/Comment";
import {notify} from "@kyvg/vue3-notification";
import {useSettingStore, useWebsiteInfoStore} from "@/store"
import {storeToRefs} from "pinia";
import {StatusCode} from "@/api/enum/StatusCode";

const settingStore = useSettingStore();
const websiteInfoStore = useWebsiteInfoStore();
const {loginFlag, loadingFlag} = storeToRefs(settingStore);
const {websiteInfo} = storeToRefs(websiteInfoStore);

const commentsData: Ref<Array<CommentEntity> | null> = ref(null);
const commentLoading = ref(false);
// 提交按钮锁
const commentBtnLock = ref(false);
const pagination = reactive({
    size: 10,
    current: 1,
    page: 0
});

// 更新评论列表
const insertUpdateCommentDataHandle = async (commentParams: CommentQueryParams) => {
    loadingFlag.value = true;
    commentLoading.value = true;
    commentsData.value = null;
    await getCommentList(commentParams).then((resp: PageResult<Array<CommentEntity>>) => {
        if (resp.status) {
            commentsData.value = resp.data.recordList;
            pagination.page = resp.data.count;
            pagination.current = 2
        }
    }).catch(() => {
        notify({
            text: "评论获取失败，重试",
            type: "warn"
        })
    }).finally(() => {
        commentLoading.value = false
        loadingFlag.value = false
    })
}

// 加载更多评论
const updateCommentDataHandle = async (commentParams: CommentQueryParams) => {
    loadingFlag.value = true;
    await getCommentList(commentParams).then((resp: PageResult<Array<CommentEntity>>) => {
        if (resp.status) {
            pagination.current++;
            commentsData.value?.push(...resp.data.recordList);
        }
    }).catch(() => {
        notify({
            text: "评论获取失败，重试",
            type: "warn"
        });
    }).finally(() => {
        loadingFlag.value = false;
    })
}

// 初始化评论列表
const initCommentDataHandle = async (commentParams: CommentQueryParams) => {
    commentLoading.value = true;
    commentsData.value = null;
    await getCommentList(commentParams).then((resp: PageResult<Array<CommentEntity>>) => {
        if (resp.status) {
            commentsData.value = resp.data.recordList;
            pagination.current = 2
            pagination.page = resp.data.count;
        }
    }).catch(() => {
        notify({
            text: "评论获取失败，重试",
            type: "warn"
        });
    }).finally(() => {
        commentLoading.value = false
    })
}

// 刷新回复列表
const updateReplyCommentHandle = (parentId: string) => {
    commentsData.value?.filter(async (comment: CommentEntity) => {
        if (comment.id === parentId) {
            // 如果大于三个
            if (comment.replyCount && comment.replyCount > 3) {
                comment.replyCount++;
                comment.replyList.length = 3;
            } else {
                loadingFlag.value = true
                await getReplyCommentList(parentId, {
                    size: 3,
                    current: 1
                }).then((resp: ResultObject<Array<ReplyEntity>>) => {
                    if (resp.status) {
                        if (comment.replyCount) {
                            comment.replyList.length = 0;
                            comment.replyCount++;
                        } else {
                            comment.replyCount = 1
                        }
                        comment.replyList = resp.data
                    }
                }).catch(() => {
                    notify({
                        text: "获取回复列表失败，请重试",
                        type: "warn"
                    })
                }).finally(() => {
                    loadingFlag.value = false;
                });
            }
        }
    });
}

// 保存评论
const saveCommentHandle = async ({commentText, topicId, type}: any) => {
    if (commentText.value.trim().length === 0) {
        notify({
            text: "评论内容不能为空",
            type: "warn"
        })
        return;
    }
    // 锁住提交评论按钮
    commentBtnLock.value = true
    if (topicId.value && type.value) {
        const form = new FormData();
        form.append("commentContent", commentText.value);
        form.append("type", type.value);
        form.append("topicId", topicId.value)
        loadingFlag.value = true
        notify({
            text: "正在提交评论...",
        })
        await insertComment(form).then((resp: ResultObject<null>) => {
            // 未登录
            if (resp.code === StatusCode.UNAUTHORIZED) {
                loginFlag.value = true;
            }
            if (resp.status) {
                if (websiteInfo.value?.isCommentReview) {
                    notify({
                        text: "评论成功，待审核中...",
                        type: "success"
                    })
                } else {
                    notify({
                        text: "评论成功",
                        type: "success"
                    })
                }
                // 插入成功后更新评论列表
                insertUpdateCommentDataHandle({
                    topicId: topicId.value,
                    type: type.value,
                    current: 1,
                    size: pagination.size
                })
            }
            commentText.value = ""
        }).catch(() => {
            notify({
                text: "评论失败，请重试",
                type: "warn"
            })
        }).finally(() => {
            // 释放提交评论按钮
            commentBtnLock.value = false
            loadingFlag.value = false
        })
    }
}

// 回复
const replyCommentHandle = async (commentText: string, parentId: string, replyCommentId: string, topicId: string, type: string) => {
    const form = new FormData();
    form.append("commentContent", commentText);
    form.append("parentId", parentId);
    form.append("replyCommentId", replyCommentId);
    form.append("topicId", topicId)
    form.append("type", type)
    return await insertComment(form);
}

// 查看评论下的回复
const moreReplyComment = async (commentData: CommentEntity, pagination: { size: number, current: number }) => {
    loadingFlag.value = true
    await getReplyCommentList(commentData.id, pagination).then((resp: ResultObject<Array<ReplyEntity>>) => {
        if (resp.status) {
            // 首次加载，清空列表
            if (commentData.replyList.length === 3) {
                commentData.replyList.length = 0;
            }
            commentData.replyList.push(...resp.data);
            pagination.current++;
        }
    }).catch(() => {
        notify({
            text: "获取回复失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false
    })
}

export {
    commentLoading,
    commentsData,
    pagination,
    commentBtnLock,
    initCommentDataHandle,
    insertUpdateCommentDataHandle,
    updateCommentDataHandle,
    updateReplyCommentHandle,
    saveCommentHandle,
    replyCommentHandle,
    moreReplyComment
}