import type { DataTableColumns } from 'naive-ui'
import type { Ref } from 'vue'
import {
  DataTableRowKey,
  NA,
  NAvatar,
  NButton,
  NImage,
  NInput,
  NPopover,
  NSpace,
  NSpin,
  NTag,
  NText,
  SelectOption
} from 'naive-ui'
import { avatarFallbackSrc } from "~/components/common/constant";
import { CheckOne, Forbid } from "@icon-park/vue-next";
import { isMobile } from "~/utils/utils";
import { listFriendLinkBack, UpdateFriendLinkReview, deleteFriendLink, saveOrUpdateFriendLink } from "~/api/requests/FriendLink";
import { ref, h, reactive, computed } from "vue"

const loading = ref(false);
const friendLinkData: Ref<Array<FriendLinkBackModel>> = ref<Array<FriendLinkBackModel>>([]);
const checkedRowKeys: Ref<DataTableRowKey[]> = ref<DataTableRowKey[]>([]);
const showSaveOrUpdateFriendLinkModal = ref(false);
const friendLinkForm: Ref<FriendLinkParams> = ref<FriendLinkParams>({
  id: null,
  linkAvatar: '',
  linkIntro: '',
  linkName: '',
  linkUrl: '',
  remark: '',
  isReview: false
});
const searchData: Ref<ConditionParams> = ref<ConditionParams>({
  keywords: '',
  isReview: null,
  current: 1
});

// 友链分页
const pagination = reactive({
  page: 1,
  pageCount: 1,
  pageSize: 10,
  prefix({ itemCount }: any) {
    return `共有 ${itemCount} 条`
  }
})

// 搜索
const searchFriendLinkHandle = async () => {
  const data: ConditionParams = {}
  if (searchData.value.keywords?.length !== 0) {
    data.keywords = searchData.value.keywords
  }
  if (searchData.value.isReview !== null) {
    data.isReview = searchData.value.isReview
  }
  await createFriendLinkData(data)
}

// 友链审核搜索处理方法
const reviewHandle = async (value: number, option: SelectOption) => {
  searchData.value.isReview = value
  await searchFriendLinkHandle()
}


const createColumns = ({ friendLink }: { friendLink: (row: FriendLinkBackModel) => void }): DataTableColumns<FriendLinkBackModel> => {
  return [
    {
      type: 'selection',
      width: 30,
      align: 'center',
      fixed: (isMobile() ? false : 'left') as any,
    },
    {
      title: '用户',
      key: 'user',
      width: 80,
      align: 'center',
      ellipsis: {
        tooltip: true,
      },
      render(row) {
        return row.user ? h(NPopover, {
          trigger: 'hover',
          style: { padding: '5px' }
        }, {
          trigger: () => h(NAvatar, {
            size: 'small',
            color: "transparent",
            src: (row.user as SimpleUserInfoModel).avatar,
            fallbackSrc: avatarFallbackSrc
          }),
          default: () => (row.user as SimpleUserInfoModel).nickname
        }) : null
      }
    },
    {
      title: '友链名称',
      key: 'linkName',
      width: 150,
      align: 'center',
      ellipsis: {
        tooltip: true,
      },
      render(row) {
        return h(NText, { type: 'warning' }, {
          default: () => row.linkName
        })
      }
    },
    {
      title: '头像',
      key: 'linkAvatar',
      width: 80,
      align: 'center',
      ellipsis: {
        tooltip: true,
      },
      render(row) {
        return h(NImage, {
          width: 30,
          src: row.linkAvatar,
          fallbackSrc: avatarFallbackSrc,
          showToolbarTooltip: true,
          objectFit: "contain",
          style: { height: '30px', borderRadius: '4px' },
          lazy: true,
          intersectionObserverOptions: { root: '#friendLink-scroll-container' }
        }, {
          placeholder: () => h(NSpin, { size: 24 })
        })
      }
    },
    {
      title: '简介',
      key: 'linkIntro',
      width: 200,
      align: 'center',
      ellipsis: {
        tooltip: true,
      },
      render(row) {
        return h(NText, { depth: 3 }, {
          default: () => row.linkIntro.length === 0 ? "这个人啥也没写" : row.linkIntro
        })
      }
    },
    {
      title: 'URL',
      key: 'linkUrl',
      width: 170,
      align: 'center',
      ellipsis: {
        tooltip: true,
      },
      render(row) {
        return h(NText, { type: 'success', italic: true }, {
          default: () => h(NA, { href: row.linkUrl, target: '_blank' }, {
            default: () => row.linkUrl
          })
        })
      }
    },
    {
      title: '审核',
      key: 'status',
      width: 90,
      align: 'center',
      ellipsis: {
        tooltip: true,
      },
      render(row) {
        return h(NTag, { type: row.isReview ? "error" : "success", size: 'small' }, {
          default: () => row.isReview ? "待审" : "正常",
          icon: () => row.isReview ? h(Forbid) : h(CheckOne)
        })
      }
    },
    {
      title: '备注',
      key: 'remark',
      width: 100,
      align: 'center',
      ellipsis: {
        tooltip: true,
      },
      render(row) {
        return h(NText, { depth: 3, type: 'info' }, {
          default: () => row.remark
        })
      }
    },
    {
      title: '创建/更新时间',
      key: 'time',
      width: 180,
      align: 'center',
      ellipsis: {
        tooltip: true,
      },
      render(row) {
        return h(NSpace, { vertical: true, style: { fontSize: '12px' } }, {
          default: () => [
            h(NPopover, { trigger: "hover" }, {
              trigger: () => h(NText, { depth: 3 }, {
                default: () => row.createTime
              }),
              default: () => "创建时间"
            }),
            h(NPopover, { trigger: "hover", placement: "bottom" }, {
              trigger: () => h(NText, { depth: 3 }, {
                default: () => row.updateTime
              }),
              default: () => "更新时间"
            })
          ]
        })
      }
    },
    {
      title: '操作',
      key: 'actions',
      width: 190,
      align: 'center',
      fixed: (isMobile() ? false : 'right') as any,
      render(row) {
        return h(NSpace, { justify: 'center' }, {
          default: () => [
            row.isReview ? h(
              NButton,
              {
                strong: true,
                tertiary: true,
                size: 'small',
                type: 'info',
                onClick: async () => {
                  let tipMessage = (window as any).$message.create("保存中...", {
                    type: 'loading'
                  });
                  await UpdateFriendLinkReview({
                    idList: new Array<string>(row.id),
                    isReview: false,
                    remark: '',
                  }).then((resp: ResultObject<null>) => {
                    if (resp.status) {
                      checkedRowKeys.value.length = 0 as any
                      row.isReview = false
                      tipMessage.type = 'success'
                      tipMessage.content = resp.message
                    }
                  }).catch(() => {
                    tipMessage.type = 'error'
                    tipMessage.content = '修改失败'
                  })
                }
              },
              { default: () => '审核' }
            ) : h(
              NButton,
              {
                strong: true,
                tertiary: true,
                size: 'small',
                type: 'warning',
                onClick: () => {
                  const dialog = (<any>window).$dialog.warning({
                    title: '备注',
                    content: () => h(NInput, {
                      placeholder: '待审原由？',
                      value: row.remark,
                      onUpdateValue(v) {
                        row.remark = v
                      }
                    }),
                    action: () => h(NButton, {
                      onClick: async () => {
                        let tipMessage = (window as any).$message.create("保存中...", {
                          type: 'loading'
                        });
                        await UpdateFriendLinkReview({
                          idList: new Array<string>(row.id),
                          isReview: true
                        }).then((resp: ResultObject<null>) => {
                          if (resp.status) {
                            checkedRowKeys.value.length = 0 as any
                            row.isReview = true;
                            tipMessage.type = 'success';
                            tipMessage.content = resp.message;
                            // 销毁
                            dialog.destroy()
                          }
                        }).catch(() => {
                          tipMessage.type = 'error'
                          tipMessage.content = '修改失败'
                        })
                      }
                    }, {
                      default: () => "保存"
                    })
                  })
                }
              },
              { default: () => '待审' }
            ),
            h(NButton, {
              strong: true,
              tertiary: true,
              size: 'small',
              type: 'primary',
              onClick: () => {
                friendLinkForm.value = {
                  id: row.id,
                  linkAvatar: row.linkAvatar,
                  linkIntro: row.linkIntro,
                  linkName: row.linkName,
                  linkUrl: row.linkUrl,
                  remark: row.remark,
                  isReview: row.isReview
                }
                showSaveOrUpdateFriendLinkModal.value = true
              }
            }, {
              default: () => '修改'
            }),
            h(
              NButton,
              {
                strong: true,
                tertiary: true,
                size: 'small',
                type: 'error',
                onClick: () => {
                  (<any>window).$dialog.warning({
                    title: '警告',
                    content: `你确定删除'${row.linkName}'吗？`,
                    positiveText: '确定',
                    negativeText: '算了',
                    onPositiveClick: async () => {
                      let tipMessage = (window as any).$message.create("保存中...", {
                        type: 'loading'
                      });
                      await deleteFriendLink(row.id).then((resp: ResultObject<null>) => {
                        if (resp.status) {
                          createFriendLinkData({ current: pagination.page })
                          tipMessage.type = 'success'
                          tipMessage.content = resp.message
                        }
                      })
                    }
                  })
                }
              },
              { default: () => '删除' }
            )
          ]
        })
      }
    }
  ]
}

const createFriendLinkData = async (condition: ConditionParams) => {
  loading.value = true
  friendLinkData.value.length = 0
  await listFriendLinkBack(condition).then((resp: PageResult<Array<FriendLinkBackModel>>) => {
    if (resp.status) {
      friendLinkData.value = Array.from(resp.data.recordList)
      pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
      pagination.page = condition.current as number
      // @ts-ignore
      pagination.itemCount = resp.data.count
    }
  }).catch(() => {
    (<any>window).$message.error('获取友链列表失败，请重试');
  }).finally(() => {
    loading.value = false
  })
}

const columns = createColumns({
  friendLink(row: FriendLinkBackModel) {
  }
})

// 选中
const handleCheck = (rowKeys: DataTableRowKey[]) => {
  checkedRowKeys.value = rowKeys
}

// 批量更新审核状态
const updateCheckedReview = async (isReview: boolean) => {
  if (checkedRowKeys.value.length !== 0) {
    let tipMessage = (window as any).$message.create("正在修改...", {
      type: 'loading'
    });
    await UpdateFriendLinkReview({
      idList: Array.from(checkedRowKeys.value) as Array<string>,
      isReview
    }).then((resp: ResultObject<null>) => {
      if (resp.status) {
        checkedRowKeys.value.length = 0 as any
        tipMessage.type = 'success'
        tipMessage.content = resp.message
        searchFriendLinkHandle()
      }
    }).catch(() => {
      tipMessage.type = 'error'
      tipMessage.content = '修改失败'
    })
  }
}

// 判断友链表单
const checkFriendLinkFormComputed = computed(() => {
  const name = friendLinkForm.value.linkName.length !== 0
  const avatar = friendLinkForm.value.linkAvatar.length !== 0
  const url = friendLinkForm.value.linkUrl.length !== 0
  return name && avatar && url
})

// 新增友链
const saveFriendLinkHandle = () => {
  friendLinkForm.value = {
    id: null,
    linkAvatar: '',
    linkIntro: '',
    linkName: '',
    linkUrl: '',
    remark: '',
    isReview: false
  }
  showSaveOrUpdateFriendLinkModal.value = true
}

// 新增或修改友链
const saveOrUpdateFriendLinkHandle = async () => {
  if (!checkFriendLinkFormComputed) {
    return;
  }
  const form = new FormData()
  form.append('id', friendLinkForm.value.id ? friendLinkForm.value.id : '')
  form.append('linkName', friendLinkForm.value.linkName)
  form.append('linkAvatar', friendLinkForm.value.linkAvatar)
  form.append('linkUrl', friendLinkForm.value.linkUrl)
  form.append('linkIntro', friendLinkForm.value.linkIntro)
  form.append('remark', friendLinkForm.value.remark)
  form.append('isReview', friendLinkForm.value.isReview as any)
  let tipMessage = (window as any).$message.create("保存中...", {
    type: 'loading'
  });
  const friendLinkParams: FriendLinkParams = {
    id: friendLinkForm.value.id ? friendLinkForm.value.id : '',
    linkName: friendLinkForm.value.linkName,
    linkAvatar: friendLinkForm.value.linkAvatar,
    linkUrl: friendLinkForm.value.linkUrl,
    linkIntro: friendLinkForm.value.linkIntro,
    remark: friendLinkForm.value.remark,
    isReview: friendLinkForm.value.isReview,
  }
  await saveOrUpdateFriendLink(friendLinkParams).then((resp: ResultObject<null>) => {
    if (resp.status) {
      showSaveOrUpdateFriendLinkModal.value = false
      if (friendLinkForm.value.id) {
        createFriendLinkData({ current: pagination.page })
      } else {
        createFriendLinkData({ current: 1 })
      }
      tipMessage.type = 'success'
      tipMessage.content = resp.message
    }
  }).catch(() => {
    tipMessage.type = 'error'
    tipMessage.content = '保存失败'
  })
}
export {
  friendLinkData,
  columns,
  loading,
  createFriendLinkData,
  pagination,
  handleCheck,
  checkedRowKeys,
  searchData,
  searchFriendLinkHandle,
  reviewHandle,
  updateCheckedReview,
  showSaveOrUpdateFriendLinkModal,
  saveFriendLinkHandle,
  friendLinkForm,
  checkFriendLinkFormComputed,
  saveOrUpdateFriendLinkHandle
}