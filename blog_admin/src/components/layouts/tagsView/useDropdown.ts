import type { TagView } from "~/store/types";
import type { DropdownOption } from "naive-ui";
import { useTagsViewStore, useGlobalStore } from "~/store";
import useTagsView from "./useTagsView";
import {useRoute, useRouter} from "vue-router";
import {nextTick, ref} from "vue";

const options = [
  {
    label: '刷新页面',
    key: 'refresh'
  },
  {
    label: '关闭当前',
    key: 'current'
  },
  {
    label: '关闭右侧',
    key: 'right'
  },
  {
    label: '关闭其它',
    key: 'other'
  },
  {
    label: '关闭所有',
    key: 'all'
  }
]

const useDropdown = (visitedList: TagView[]) => {

  const route = useRoute()
  const router = useRouter()
  const globalStore = useGlobalStore();
  const { handleClose } = useTagsView()
  const { closeRightView, closeOtherView, closeAllView } = useTagsViewStore()
  const showDropRef = ref(false)
  const x = ref(0)
  const y = ref(0)
  // 点击的 tag
  let currentTag = ref<TagView>()
  const dropOptions = ref<DropdownOption[]>([])

  // 选择
  const handleSelect = async (key: string) => {
    showDropRef.value = false
    switch (key) {
      case 'refresh':
        if (currentTag.value?.fullPath === route.fullPath) {
          globalStore.reload()
        } else {
          await router.replace(currentTag.value?.fullPath as string)
        }
        break
      case 'current':
        await handleClose(currentTag.value as TagView)
        break
      case 'right':
        await closeRightView(currentTag.value as TagView)
        await router.replace(currentTag.value?.fullPath as string)
        break
      case 'other':
        await closeOtherView(currentTag.value as TagView)
        await router.replace(currentTag.value?.fullPath as string)
        break
      case 'all':
        await closeAllView()
        await router.replace('/')
        break
      default:
    }
  }

  // 右击 tag
  const isLastTag = (tag: TagView) => {
    const index = visitedList.findIndex(
      (item: TagView) => tag.fullPath === item.fullPath
    )
    if (index !== -1 && index + 1 !== visitedList.length) return false
    else return true
  }

  // 判断返回的 options
  const checkDropOptions = (tag: TagView) => {
    if (tag.fullPath === '/') {
      // 点击的是首页，不显示关闭当前
      dropOptions.value = options.filter((item) => item.key !== 'current')
    } else if (isLastTag(tag)) {
      dropOptions.value = options.filter((item) => item.key !== 'right')
    } else {
      dropOptions.value = options
    }
  }

  const handleContextMenu = async (e: MouseEvent, tag: TagView) => {
    currentTag.value = tag
    e.preventDefault()
    showDropRef.value = false
    await nextTick()
    checkDropOptions(tag)
    showDropRef.value = true
    x.value = e.clientX
    y.value = e.clientY
  }

  // 点击外面
  const clickoutSide = () => {
    showDropRef.value = false
  }

  return {
    showDropRef,
    x,
    y,
    handleSelect,
    handleContextMenu,
    clickoutSide,
    dropOptions
  }
}

export default useDropdown