<script setup lang='ts'>
import type { PropType, Ref } from 'vue';
import type { MenuMode, TriggerStyle } from '~/settings'
import type { Icon } from '@icon-park/vue-next/lib/runtime';
import {
  DropdownOption,
  NDivider,
  NLayoutHeader,
  NSpace,
  NAvatar,
  NBadge,
  NDrawer,
  NDrawerContent,
  NDropdown,
  NButton,
  NIcon,
  NCard,
  NGradientText,
  NEllipsis,
  NResult,
  NSpin,
  NA,
} from "naive-ui"
import TriggerCollapse from "~/components/layouts/TriggerCollapse.vue"
import Breadcrumb from "~/components/layouts/Breadcrumb.vue"
import BaseLogo from "~/components/layouts/BaseLogo.vue"
import Menu from "~/components/layouts/Menu.vue"
import { renderIcon } from '~/utils'
import { useSettingStore, useUserInfoStore, useWebsiteStore } from '~/store'
import iconMap from '~/utils/icons';
import { Erase, Delete } from '@icon-park/vue-next';
import { listNoticeBack, updateNotice, deleteNotice } from '~/api/requests/Notice'
import { StatusCode } from '~/api/enum/statusCode';
import { imageFallbackSrc } from '~/components/common/constant'
import { useRouter } from 'vue-router'
import { storeToRefs } from "pinia";
import { h, reactive, ref } from "vue";

const router = useRouter()

const props = defineProps({
  isCollapse: Boolean,
  isInverted: Boolean,
  triggerStyle: String as PropType<TriggerStyle>,
  collapsedWidth: Number,
  collapsedIconSize: Number,
  menuMode: {
    type: String as PropType<MenuMode>,
    default: 'vertical'
  }
})

const settingStore = useSettingStore()
const userInfoStore = useUserInfoStore()
const websiteStore = useWebsiteStore()
const { isShowBreadcrumb, isCollapse, isShowBreadcrumbIcon } = storeToRefs(settingStore)
const userUrl = userInfoStore.userInfo?.avatar
const noticeParams: NoticeParams = reactive({ isRead: false, isSystemNotice: true, size: 10, current: 1 })
const noticeBackData: Ref<{ count: number, recordList: Array<NoticeBackModel> } | null> = ref(null);
// 未阅读消息数量
let { noReadNoticeCount } = storeToRefs(websiteStore)
const noticeShow = ref<boolean>(false)
const isLoading = ref<boolean>(false)


const dropOptions: DropdownOption[] = [
  {
    label: '查看消息',
    key: 'remind',
    icon: () => noReadNoticeCount.value === 0 ? h(renderIcon(iconMap.get("message-unread") as Icon)) : h(NBadge, {
      value: noReadNoticeCount.value,
      max: 99
    })
  },
  {
    label: '全局设置',
    key: 'setting',
    icon: renderIcon(iconMap.get("setting") as Icon)
  },
  {
    label: '个人中心',
    key: 'personal',
    icon: renderIcon(iconMap.get("user") as Icon)
  },
  {
    type: 'render',
    key: 'divider',
    render: () => h(NDivider, { style: { padding: '3px', margin: 0 } })
  },
  {
    label: '退出登录',
    key: 'logout',
    icon: renderIcon(iconMap.get("logout") as Icon)
  }
]

// 初始化消息
const requestNotice = async () => {
  isLoading.value = true
  // 数据清零
  noticeBackData.value = null;
  noticeParams.current = 1;
  await listNoticeBack(noticeParams).then((resp) => {
    console.log(resp);

    if (resp.status) {
      if (!resp.data.count) {
        noReadNoticeCount.value = 0;
        isLoading.value = false;
        dropOptions[0].icon = renderIcon(iconMap.get("message-unread") as Icon);
        return;
      } else {
        dropOptions[0].icon = () => h(NBadge, { value: noReadNoticeCount.value, max: 99 });
      }
      noticeBackData.value = resp.data;
      if (!noticeParams.isRead) {
        noReadNoticeCount.value = resp.data.count;
      }
    }
  }).catch(() => {
    (<any>window).$message.error("获取通知列表失败，请重试");
  }).finally(() => {
    isLoading.value = false;
  });
}

// 加载更多消息
const updateMoreNotice = async () => {
  noticeParams.current!++;
  isLoading.value = true
  await listNoticeBack(noticeParams).then((resp: PageResult<Array<NoticeBackModel>>) => {
    if (resp.status) {
      noticeBackData.value?.recordList.push(...resp.data.recordList);
      if (!noticeParams.isRead) {
        noReadNoticeCount.value = resp.data.count;
      }
    }
  }).catch(() => {
    (<any>window).$message.error("获取通知失败，请重试");
  }).finally(() => {
    isLoading.value = false;
  })
}

const handleSelect = (key: string) => {
  switch (key) {
    case 'personal':
      router.replace({ path: '/personal' });
      break;
    case 'remind':
      noticeShow.value = true;
      requestNotice();
      break;
    case 'setting':
      settingStore.changeSetting('isShowDraw', true);
      break;
    case 'logout':
      userInfoStore.logout();
      break;
  }
}

// 更新消息未读状态
const updateNoticeFunc = async (ids: Array<string>) => {
  const result = await updateNotice(ids);
  if (result.code === StatusCode.SUCCESS && result.status) {
    if (noticeBackData.value && noticeBackData.value.count > 0) {
      noticeBackData.value!.count--;
    }
    await requestNotice();
  } else {
    if (result.code === StatusCode.FAIL) {
      (window as any).$message.error(result.message);
    }
  }
}

// 删除消息
const deleteNoticeFunc = async (ids: Array<string>) => {
  const result = await deleteNotice(ids);
  if (result.status) {
    if (noticeBackData.value && noticeBackData.value.count > 0) {
      noticeBackData.value!.count--;
    }
    await requestNotice()
    if (result.code === StatusCode.FAIL) {
      (window as any).$message.error(result.message);
    }
  }
}

// 切换未读和已读状态
const updateShow = (flag: boolean) => {
  noticeParams.isRead = flag
  noticeParams.current = 1;
  requestNotice()
}

// 清除未读
const clearNoRead = async () => {
  await listNoticeBack({ isRead: false, isSystemNotice: true }).then(async (resp) => {
    if (resp.status) {
      const noticeIds = await listNoticeBack({ isRead: false, isSystemNotice: true, size: resp.data.count, current: 1 });
      if (noticeIds.code === StatusCode.SUCCESS && noticeIds.status) {
        if (noticeIds.data.recordList) {
          const ids = noticeIds.data.recordList.filter(notice => !notice.status).map(notice => notice.id);
          await updateNoticeFunc(ids);
        }
      }
    }
  });
}

// 删除已读
const deleteIsRead = async () => {
  await listNoticeBack({ isRead: true, isSystemNotice: true }).then(async (resp) => {
    if (resp.status) {
      const noticeIds = await listNoticeBack({ isRead: true, isSystemNotice: true, size: resp.data.count, current: 1 });
      if (noticeIds.code === StatusCode.SUCCESS && noticeIds.status) {
        if (noticeIds.data.recordList) {
          const ids = noticeIds.data.recordList.filter(notice => notice.status).map(notice => notice.id)
          await deleteNoticeFunc(ids);
        }
      }
    }
  });
}

</script>

<template>
  <n-layout-header bordered :inverted="props.isInverted" class="headerContainer">
    <div class="headerContainer"
      :style="props.menuMode === 'horizontal' ? { width: '1280px', margin: '0 auto', overflow: 'auto' } : {}">
      <template v-if="props.menuMode === 'vertical'">
        <n-space align="center" justify="center">
          <trigger-collapse v-if="props.triggerStyle === 'custom'" @changeSetting="settingStore.changeSetting"
            :is-collapse="isCollapse" />
          <breadcrumb v-show="isShowBreadcrumb" :style="props.triggerStyle !== 'custom' ? { marginLeft: '10px' } : {}"
            :is-show-breadcrumb-icon="isShowBreadcrumbIcon" />
        </n-space>
      </template>
      <template v-else>
        <n-space class="h-50px" align="center" style="flex-flow: row">
          <base-logo />
          <Suspense>
            <Menu :is-inverted="props.isInverted" :collapsed-icon-size="props.collapsedIconSize"
              :collapsed-width="props.collapsedWidth" :menu-mode="props.menuMode" />
          </Suspense>
        </n-space>
      </template>
      <n-space class="h-50px" align="center" justify="center">
        <n-badge :value="noReadNoticeCount" dot :show="noReadNoticeCount !== 0">
          <n-dropdown :options="dropOptions" @select="handleSelect">
            <n-avatar round size="medium" :fallback-src="imageFallbackSrc" :src="userUrl" class="cursor-pointer" />
          </n-dropdown>
        </n-badge>
      </n-space>
    </div>
    <n-drawer v-model:show="noticeShow" width="320">
      <n-drawer-content title="系统消息" :native-scrollbar="false">
        <template #header>
          <n-space justify="start" align="center">
            <n-badge :value="noReadNoticeCount" :max="99">
              <n-button size="tiny" @click="updateShow(false)">
                未读
              </n-button>
            </n-badge>
            <n-button size="tiny" @click="updateShow(true)">
              已读
            </n-button>
            <n-button size="tiny" @click="clearNoRead">
              <template #icon>
                <n-icon>
                  <erase theme="outline" size="12" />
                </n-icon>
              </template>
              清除未读
            </n-button>
            <n-button size="tiny" @click="deleteIsRead">
              <template #icon>
                <n-icon>
                  <delete theme="outline" size="12" />
                </n-icon>
              </template>
              删除已读
            </n-button>
          </n-space>
        </template>
        <n-space justify="center" v-if="!isLoading">
          <template v-if="noticeBackData">
            <n-card v-for="notice of noticeBackData.recordList" :key="notice.id" hoverable size="small"
              :title="notice.title" class="w-272px">
              <template #header-extra>
                <n-gradient-text :size="10" type="info">
                  {{ notice.createTime }}
                </n-gradient-text>
              </template>
              <n-ellipsis line-clamp="1" expand-trigger="click" class="text-12px">
                {{ notice.content }}
              </n-ellipsis>
              <template #footer>
                <n-space align="baseline" justify="end">
                  <n-button size="tiny" secondary strong v-if="notice.url !== null">
                    <n-a :href="notice.url" target="_blank" class="no-underline">查看</n-a>
                  </n-button>
                  <n-button size="tiny" secondary strong v-if="!noticeParams.isRead"
                    @click="updateNoticeFunc([notice.id])">
                    已阅
                  </n-button>
                  <n-button size="tiny" secondary strong v-if="noticeParams.isRead"
                    @click="deleteNoticeFunc([notice.id])">
                    删除
                  </n-button>
                </n-space>
              </template>
            </n-card>
            <n-button size="small" type="tertiary" tertiary @click="updateMoreNotice"
              v-if="noticeBackData.recordList.length !== noticeBackData.count">
              加载剩下的{{ noticeBackData.count - noticeBackData.recordList.length }}条
            </n-button>
          </template>
          <template v-else>
            <n-result status="success" description="无消息">
            </n-result>
          </template>
        </n-space>
        <n-space v-else justify="center">
          <n-spin>
            <template #description>
              拼命加载中...
            </template>
          </n-spin>
        </n-space>
      </n-drawer-content>
    </n-drawer>
  </n-layout-header>
</template>

<style scoped>
.headerContainer {
  @apply w-full h-50px flex justify-between items-center py-0 pr-10px pl-0
}
</style>