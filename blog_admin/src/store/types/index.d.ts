import type { RouterMeta } from 'vue-router'

export interface TagView extends RouterMeta {
  fullPath: string,
  [key: string]: any
}