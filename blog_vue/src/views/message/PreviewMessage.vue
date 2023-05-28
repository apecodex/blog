<script setup lang='ts'>
import Mask from "@/components/Mask.vue";
import {currentMessage} from "./MessageHooks"
import {onBeforeUnmount, onMounted} from "vue";

const closePreview = () => {
  currentMessage.value = null;
  document.body.style.overflow = 'visible'
}

onMounted(() => {
  document.onkeydown = (e: KeyboardEvent) => {
    if (e.keyCode! === 27) {
      closePreview();
    }
  }
})

onBeforeUnmount(() => {
  document.onkeydown = null;
})
</script>

<template>
  <Mask :show="currentMessage !== null" @click="closePreview"/>
  <div :class="{'show': currentMessage}, currentMessage?.theme"
       class="preview-message opacity-0 fixed top-1/2 left-1/2 max-w-300px w-full max-h-200px rounded-6px z-9 overflow-y-auto overflow-x-hidden break-all">
    <p class="flex justify-center items-center p-10px min-h-100px">{{ currentMessage?.content }}</p>
  </div>
</template>

<style scoped>

.preview-message {
  top: -150%;
  pointer-events: none;
  box-shadow: 0 0 3px #000;
  transition: opacity 300ms ease-in-out,
  top 1000ms ease-in-out,
  background-color 1000ms ease-in-out,
  transform 0.2s 0.2s ease-in-out;
}

.preview-message:before {
  @apply 'absolute top-0 w-full h-full opacity-20 bg-cover bg-no-repeat bg-center bg-origin-border -z-1';
  content: "";
  background-image: url("https://apecode.oss-cn-chengdu.aliyuncs.com/config/237ae48b0c702d50d0bb02353ecb453a.png");
}

.preview-message.show {
  @apply 'top-1/2 opacity-100 pointer-events-auto';
  transform: translate(-50%, -50%) scale(1);
  transition: transform 300ms cubic-bezier(0.18, 0.89, 0.43, 1.19);
}

.preview-message.show.default {
  @apply 'bg-[#1e90ff]';
}

.preview-message.show.orange {
  @apply 'bg-[#ffa502]';
}

.preview-message.show.tomato {
  @apply 'bg-[#ff6348]';
}

.preview-message.show.FlamingoPink {
  @apply 'bg-[#f78fb3]';
}

.preview-message.show.watermelon {
  @apply 'bg-[#ff4757]';
}

.preview-message.show.PrestigeBlue {
  @apply 'bg-[#2f3542]';
}

.preview-message.show.UfoGreen {
  @apply 'bg-[#2ed573]';
}

.preview-message.show.BrightGreek {
  @apply 'bg-[#3742fa]';
}

.preview-message.show.wisteria {
  @apply 'bg-[#8e44ad]';
}
</style>