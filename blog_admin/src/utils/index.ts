import { NIcon } from 'naive-ui'
import { h } from 'vue'
import { Icon } from '@icon-park/vue-next/lib/runtime'

export const renderIcon = (icon: Icon) => {
  const slots = {
    default: () => h(icon)
  }
  return () => h(NIcon, null, slots)
}

// 根据图标字符串动态渲染图标
// 缺点：打包时会把所有图标都打包
export async function asyncRenderIcon(icon: any) {
  const { [icon]: iconComp }: any = await import('@icon-park/vue-next');
  return () => h(NIcon, null, { default: () => h(iconComp) });
}