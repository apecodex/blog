<script setup lang='ts'>
import {
  NImage,
  NSpace,
  NCard,
  NInput,
  NPopover,
  NButton,
  NTabs,
  NTabPane,
  NScrollbar,
} from "naive-ui"
import {computed, h, nextTick, ref} from "vue"
import {smilingFaces, humans, animalsAndNature, others} from "~/utils/dynamicEmojis"
import {ConfoundedFace} from '@icon-park/vue-next'

const props = defineProps({
  defaultValue: {
    type: String,
    default: ''
  },
  enterBtnText: {
    type: String,
    default: '确定'
  },
  placeholder: {
    type: String,
    default: "这一刻的想法..."
  },
  style: Object,
  minRows: {
    type: Number,
    default: 4
  },
  maxRows: {
    type: Number,
    default: 4
  },
  disabled: {
    type: Boolean,
    default: false
  },
  maxlength: String,
  showCount: Boolean,
  size: {
    type: String,
    default: 'small'
  }
})

const emit = defineEmits([
  'update:defaultValue',
  'enter'
])

const defaultValue = computed({
  get: () => props.defaultValue,
  set: (value) => emit('update:defaultValue', value)
})

const commentTargetRef = ref<any>(null)
// 光标位置
const cursorIndex = ref(0)

// 点击确定按钮
const enter = () => {
  emit('enter', defaultValue.value)
}

// 插入表情包
const clickEmojis = (emoji: any) => {
  defaultValue.value = defaultValue.value.slice(0, cursorIndex.value) + emoji + defaultValue.value.slice(cursorIndex.value)
  nextTick(() => {
    commentTargetRef.value?.focus()
    commentTargetRef.value.selectionStart = cursorIndex.value + emoji.length
    commentTargetRef.value.selectionEnd = cursorIndex.value + emoji.length
  })
}

// 聚焦时记录光标
const onBlur = (event: any) => {
  let target = event.target || event.srcElement; //for IE~
  commentTargetRef.value = target
  cursorIndex.value = target.selectionStart
}

const inputHandle = (e: any) => {
  defaultValue.value = e
}

</script>

<template>
  <n-space vertical :style="style">
    <n-card size="small"
            :content-style="{padding: '0'}"
            :footer-style="{padding: '5px'}">
      <n-input
          v-model:value="defaultValue"
          type="textarea"
          clearable
          @blur="onBlur"
          @update:value="inputHandle"
          autofocus
          :placeholder="placeholder"
          :autosize="{
            minRows: minRows,
            maxRows: maxRows,
          }"
          :maxlength="maxlength"
          :show-count="showCount"
          :size="size"
      />
      <template #footer>
        <n-space justify="space-between">
          <n-space align="center">
            <n-popover trigger="click" placement="bottom">
              <template #trigger>
                <n-button quaternary circle :size="size">
                  <template #icon>
                    <confounded-face theme="outline"/>
                  </template>
                </n-button>
              </template>
              <n-tabs type="line" trigger="click" :bar-width="48" animated>
                <n-tab-pane name="smilingFaces"
                            :tab="() => h(NImage, {width: 24, src: smilingFaces['[嘿嘿]'], previewDisabled: true})">
                  <n-scrollbar style="max-height: 120px; width: 250px" id="emoji-smilingFaces-scroll-container">
                    <n-space>
                    <span class="emojis" v-for="(emoji, emojiName, index) in smilingFaces" :key="index"
                          @click="clickEmojis(emojiName)">
                      <NImage
                          :src="emoji"
                          width="24"
                          height="24"
                          preview-disabled
                          lazy
                          :intersection-observer-options="{root: '#emoji-smilingFaces-scroll-container'}"
                          :title="emojiName"
                      >
                        <template #placeholder>
                          <NImage
                              src="https://apecode.oss-cn-chengdu.aliyuncs.com/config/075864206c78ca820b742056ea0d85c1.svg"
                              width="24"
                              height="24"
                              preview-disabled
                          />
                        </template>
                      </NImage>
                    </span>
                    </n-space>
                  </n-scrollbar>
                </n-tab-pane>
                <n-tab-pane name="humans"
                            :tab="() => h(NImage, {width: 24, src: humans['[耸肩]'], previewDisabled: true})">
                  <n-scrollbar style="max-height: 120px; width: 250px" id="emoji-humans-scroll-container">
                    <n-space>
                    <span class="emojis" v-for="(emoji, emojiName, index) in humans" :key="index"
                          @click="clickEmojis(emojiName)">
                      <NImage
                          :src="emoji"
                          width="24"
                          height="24"
                          preview-disabled
                          lazy
                          :intersection-observer-options="{root: '#emoji-humans-scroll-container'}"
                      >
                        <template #placeholder>
                          <NImage
                              src="https://apecode.oss-cn-chengdu.aliyuncs.com/config/075864206c78ca820b742056ea0d85c1.svg"
                              width="24"
                              height="24"
                              preview-disabled
                          />
                        </template>
                      </NImage>
                    </span>
                    </n-space>
                  </n-scrollbar>
                </n-tab-pane>
                <n-tab-pane name="animalsAndNature"
                            :tab="() => h(NImage, {width: 24, src: animalsAndNature['[独角兽]'], previewDisabled: true})">
                  <n-scrollbar style="max-height: 120px; width: 250px" id="emoji-animalsAndNature-scroll-container">
                    <n-space>
                    <span class="emojis" v-for="(emoji, emojiName, index) in animalsAndNature" :key="index"
                          @click="clickEmojis(emojiName)">
                      <NImage
                          :src="emoji"
                          width="24"
                          height="24"
                          preview-disabled
                          lazy
                          :intersection-observer-options="{root: '#emoji-animalsAndNature-scroll-container'}"
                          :title="emojiName"
                      >
                        <template #placeholder>
                          <NImage
                              src="https://apecode.oss-cn-chengdu.aliyuncs.com/config/075864206c78ca820b742056ea0d85c1.svg"
                              width="24"
                              height="24"
                              preview-disabled
                          />
                        </template>
                      </NImage>
                    </span>
                    </n-space>
                  </n-scrollbar>
                </n-tab-pane>
                <n-tab-pane name="others"
                            :tab="() => h(NImage, {width: 24, src: others['[火箭]'], previewDisabled: true})">
                  <n-scrollbar style="max-height: 120px; width: 250px" id="emoji-others-scroll-container">
                    <n-space>
                    <span class="emojis" v-for="(emoji, emojiName, index) in others" :key="index"
                          @click="clickEmojis(emojiName)">
                      <NImage
                          :src="emoji"
                          width="24"
                          height="24"
                          preview-disabled
                          lazy
                          :intersection-observer-options="{root: '#emoji-others-scroll-container'}"
                          :title="emojiName"
                      >
                        <template #placeholder>
                          <NImage
                              src="https://apecode.oss-cn-chengdu.aliyuncs.com/config/075864206c78ca820b742056ea0d85c1.svg"
                              width="24"
                              height="24"
                              preview-disabled
                          />
                        </template>
                      </NImage>
                    </span>
                    </n-space>
                  </n-scrollbar>
                </n-tab-pane>
              </n-tabs>
            </n-popover>
            <slot name="other"/>
          </n-space>
          <n-button @click="enter" secondary strong :disabled="disabled" :size="size">
            {{ props.enterBtnText }}
          </n-button>
        </n-space>
      </template>
    </n-card>
  </n-space>
</template>

<style scoped>
.emojis {
  @apply text-21px flex border border-transparent rounded-5px w-30px h-30px align-center items-center justify-center hover:(border-dark-500 bg-light-50 transform scale-120 transition-all)
}
</style>