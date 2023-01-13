<script setup lang='ts'>
import type {Ref} from "vue"
import {computed, onBeforeUnmount, onMounted, ref} from "vue";
import Mask from '@/components/Mask.vue'
import {Close} from '@icon-park/vue-next'
import {useSettingStore} from "@/store"

const settingStore = useSettingStore()

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ""
  },
  customClass: String
})

const emits = defineEmits(['update:show', 'closeModal'])

const closeModal = () => {
  showComp.value = false
  settingStore.closeModal();
  emits('closeModal');
}

const showComp = computed({
  get: () => {
    if (props.show) {
      document.body.style.overflow = 'hidden'
    }
    return props.show
  },
  set: (value) => {
    document.body.style.overflow = 'visible'
    emits("update:show", false)
  }
})
</script>

<template>
  <Mask v-model:show="showComp"/>
  <div class="modal" :class="[showComp ? 'show':'', customClass]">
    <div
        class="modal-close absolute right-5px top-5px z-11 cursor-pointer w-30px h-30px bg-$theme-bg rounded-full"
         @click="closeModal">
      <Close class="w-full h-full flex justify-center items-center" style="transition: var(--theme-transition-color)"
             size="18"/>
    </div>
    <header class="flex justify-between items-center mb-5px">
      <div class="text-20px h-30px" v-if="title">{{ title }}</div>
    </header>
    <div class="modal-content">
      <slot/>
    </div>
  </div>
</template>

<style scoped>
.modal {
  @apply 'fixed left-1/2 bg-$theme-bg p-10px rounded-10px max-w-350px w-19/20 max-h-19/20 opacity-0 -top-[150%] z-10 pointer-events-none';
  transform: translate(-50%, -50%) scale(0.5);
  box-shadow: var(--theme-shadow);
  transition: opacity 300ms ease-in-out,
  top 1000ms ease-in-out,
  transform 0.2s 0.2s ease-in-out;
}

.modal.show {
  @apply 'top-1/2 opacity-100 pointer-events-auto';
  transform: translate(-50%, -50%) scale(1);
  transition: transform 300ms cubic-bezier(0.18, 0.89, 0.43, 1.19);
}

/*头部区域*/
.modal header {
  @apply 'relative top-10px opacity-0';
  transition: all 100ms ease-in-out 250ms;
}

.modal.show header {
  @apply 'top-0 opacity-100';
}

.modal-close {
  box-shadow: var(--theme-shadow);
}

.modal-close:hover {
  box-shadow: var(--theme-shadow-inset);
  color: var(--theme-bg);
}

/*内容区域*/
.modal .modal-content {
  @apply 'relative top-10px opacity-0';
  transition: all 300ms ease-in-out 250ms;
}

.modal.show .modal-content {
  @apply 'top-0 opacity-100';
}

</style>