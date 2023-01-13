import type { TagView } from "~/store/types";
import type {RouteRecord} from "vue-router";
import { useTagsViewStore } from "~/store";
import {useRoute, useRouter} from "vue-router";
import {storeToRefs} from "pinia";
import {computed, onMounted, watch} from "vue";

const useTagsView = () => {
  const route = useRoute();
  const router = useRouter();

  const tagsViewStore = useTagsViewStore();
  const { visitedViews } = storeToRefs(tagsViewStore);

  // 固定页
  const affixViews = computed(() =>
    router.getRoutes().filter((item: RouteRecord) => item.path === "/")
  )

  // 固定页
  const initTags = () => {
    affixViews.value.forEach((route: RouteRecord) => {
      tagsViewStore.addView({
        ...route.meta,
        fullPath: route?.path,
        name: route.name
      } as TagView)
    })
  }

  // 添加
  const addTags = () => {
    const { name } = route
    name && tagsViewStore.addView({
      ...route.meta,
      fullPath: route.fullPath,
      name: route.name as string
    })
  }

  // 上一个tag
  const toLastView = () => {
    const lastView = visitedViews.value.slice(-1)[0]
    lastView && router.push(lastView.fullPath)
  }

  // 点击Tag
  const handleClickTag = async (tag: TagView) => {
    await router.replace(tag.fullPath)
  }

  // 点击关闭
  const handleClose = async (tag: TagView) => {
    await tagsViewStore.delView(tag)
    toLastView()
  }

  // 路由变化添加
  watch(
    () => route.fullPath,
    () => addTags()
  )

  onMounted(() => {
    initTags()
    addTags()
  })

  return {
    visitedList: visitedViews,
    initTags,
    addTags,
    handleClickTag,
    handleClose
  }
}

export default useTagsView;