<script setup lang='ts'>
import type { Icon } from '@icon-park/vue-next/lib/runtime';
import type { PropType } from 'vue';
import type { MenuOption, MenuDividerOption, MenuGroupOption } from 'naive-ui'
import {NMenu} from "naive-ui"
import { renderIcon } from '~/utils'
import { MenuMode } from '~/settings.js';
import { RouterLink, useRoute} from 'vue-router';
import { useMenuStore } from '~/store'
import iconMap from '~/utils/icons'
import {h, ref, watch} from "vue";
const menuStore = useMenuStore();

const props = defineProps({
  isInverted: Boolean,
  collapsedWidth: Number,
  collapsedIconSize: Number,
  menuMode: {
    type: String as PropType<MenuMode>,
    default: 'vertical'
  }
})

const route = useRoute()
// 菜单数据
const menus = menuStore.menus as UserMenuBackModel[]
// 高亮菜单
let activeKey = ref<string | null>(null)
activeKey.value = route.name as string
watch(
  () => route.fullPath,
  () => {
    activeKey.value = route.name as string
  }
)

const createMenuOptions = (): Array<MenuOption | MenuDividerOption | MenuGroupOption> => {
  const menuOption: Array<MenuOption | MenuDividerOption | MenuGroupOption> = [];
  for (const menu of menus) {
    let m = {}
    if (menu.component === "Layout") {
      m = {
        label: menu.title,
        key: menu.name,
        icon: renderIcon(iconMap.get(menu.icon) as Icon),
        children: [] as UserMenuBackModel[]
      };
      if (menu.children?.length !== 0) {
        for (const childMenu of menu.children as UserMenuBackModel[]) {
          m.children.push({
            label: () =>
              h(
                RouterLink,
                {
                  to: {
                    path: childMenu.path.split("/:")[0],
                  }
                },
                { default: () => childMenu.title }
              ),
            key: childMenu.name,
            icon: renderIcon(iconMap.get(childMenu.icon) as Icon),
          })
        }
      }
    } else {
      m = {
        label: () =>
          h(
            RouterLink,
            {
              to: {
                path: menu.path,
              }
            },
            { default: () => menu.title }
          ),
        key: menu.name,
        icon: renderIcon(iconMap.get(menu.icon) as Icon),
      }
    }
    menuOption.push(m)
  }
  return menuOption;
}
const menuOption = createMenuOptions()

</script>

<template>
  <n-menu ref="menuInstRef" :mode="props.menuMode" :indent="20" :collapsed-width="props.collapsedWidth"
    :collapsed-icon-size="props.collapsedIconSize" :options="(menuOption)" :inverted="props.isInverted"
    v-model:value="activeKey" style="z-index: 1">
  </n-menu>
</template>

<style>
.n-menu.n-menu--horizontal .n-menu-item-content {
  padding: 0 18px 0 0;
}
</style>