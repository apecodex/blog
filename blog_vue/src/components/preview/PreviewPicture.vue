<script setup lang='ts'>
import type {PropType, Ref} from "vue"
import Mask from "@/components/Mask.vue"
import {CloseOne, Undo, Redo, ZoomIn, ZoomOut, Erase} from "@icon-park/vue-next"
import {onBeforeUnmount, onMounted, ref} from "vue";

const imgRef: Ref<HTMLImageElement | null> = ref(null);
const rotate: Ref<number> = ref(0)
const scale: Ref<number> = ref(1);
const props = defineProps({
  picture: {
    type: Object as PropType<{ image: string, current?: number, maxCount?: number, width?: number, height?: number, pictureName?: string }>,
    default: {
      image: "",
      current: 0,
      maxCount: 1,
      pictureName: "图片"
    }
  }
});

const emits = defineEmits([
  "prev", "next"
]);

// 关闭图片
const closePreview = () => {
  props.picture!.image = ""
  initImage();
  document.body.style.overflow = 'visible'
}

// 上一张
const prevHandle = () => {
  emits("prev")
  initImage()
}
// 下一张
const nextHandle = () => {
  emits("next")
  initImage()
}

// 旋转图片
const setImgRotate = (direction: boolean) => {
  if (direction) {
    rotate.value = (rotate.value - 90) % 360;
  } else {
    rotate.value = (rotate.value + 90) % 360;
  }
  imgRef.value!.style.transform = 'rotate(' + rotate.value + 'deg) ' + 'scale('+ scale.value +')';
}

// 重置图片
const initImage = () => {
  rotate.value = 0
  scale.value = 1
  imgRef.value!.style.transform = 'rotate(' + rotate.value + 'deg) ' + 'scale('+ scale.value +')';
  imgRef.value!.style.width = props.picture?.width ? props.picture!.width + "px" : ""
  imgRef.value!.style.height = props.picture?.height ? props.picture!.height + "px" : ""
}

//  缩放
const setImgZoom = (zoom: string) => {
  if (zoom) {
    if (scale.value !== 0.5) {
      scale.value = scale.value - 0.25
    }
  } else {
    if (scale.value !== 2.5) {
      scale.value = scale.value + 0.25
    }
  }
  imgRef.value!.style.transform = 'translate(0,0) ' + 'rotate(' + rotate.value + 'deg) ' + 'scale('+ scale.value +')'
}

//拖拽图片
const moveImgHandle = (event: MouseEvent) => {
  imgRef.value!.style.transition = ""
  // 计算盒子偏移量
  let offsetLeft = event.clientX - imgRef.value!.offsetLeft
  let offsetTop = event.clientY - imgRef.value!.offsetTop
  // 开始拖拽
  document.onmousemove = (ev: MouseEvent) => {
    // 因为有margin使图片居中，产生了距离，也要把图片的margin的距离给删掉
    let left = ev.clientX - offsetLeft - imgRef.value!.offsetLeft;
    let top = ev.clientY - offsetTop - imgRef.value!.offsetTop;
    imgRef.value!.style.transform = 'translate(' + left + 'px,' + top + 'px) ' + 'rotate(' + rotate.value + 'deg) ' + 'scale('+ scale.value +')';
  };
  // 鼠标松开，停止拖拽
  document.onmouseup = () => {
    // 取消onmousemove事件
    document.onmousemove = null;
    // 取消onmouseup事件
    document.onmouseup = null;
    imgRef.value!.style.transition = "all .2s ease-in-out"
    imgRef.value!.style.transform = 'translate(0,0) ' + 'rotate(' + rotate.value + 'deg) ' + 'scale('+ scale.value +')';
  }
  return false;
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
  <div v-if="picture.image.length !== 0">
    <div class="fixed z-1 top-0 left-0 w-full h-full p-10px z-10">
      <Mask :show="picture.image.length !== 0" @click="closePreview"/>
      <div>
        <!-- 当前第几个图片 -->
        <div
            class="fixed top-10px left-10px z-11 py-3px px-10px rounded-full bg-$theme-translucent-reverse hover:(bg-$theme-bg-reverse)"
            v-show="picture.current !== 0 || picture.current+1 !== picture.maxCount">
          <span class="text-16px text-$text-color-reverse font-bold">{{ picture.current + 1 }}/{{ picture.maxCount }}</span>
        </div>
        <div class="fixed top-10px right-10px z-11 py-3px px-10px rounded-full bg-$theme-translucent-reverse hover:(bg-$theme-bg-reverse)">
          <span class="text-$text-color-reverse">{{picture.pictureName}}</span>
        </div>
        <!-- 图片 -->
        <div class="absolute top-0 left-0 right-0 bottom-0 flex pointer-events-none">
          <img ref="imgRef" class="m-auto pointer-events-auto z-10 object-contain"
               style="max-height: calc(100vh - 32px);max-width: calc(100vw - 32px);transition: all .2s ease-in-out;"
               :src="picture.image" alt="" @mousedown.prevent="moveImgHandle">
        </div>
        <div class="relative z-11">
          <div
              class="fixed flex flex-col items-center left-1/2 bottom-15px px-8px py-2px rounded-full text-light-50 bg-dark-500/50 bg-opacity-80"
              style="transform: translateX(-50%)">
            <!-- 上下切换图片 -->
            <div class="absolute flex gap-20px items-center px-5px h-25px -top-35px text-light-50 rounded-full">
              <span class="rounded-6px px-8px py-4px text-18px bg-dark-500/50 bg-opacity-80 hover:(bg-opacity-100)"
                    style="transition: var(--theme-transition-bg)" v-show="picture.current !== 0" @click="prevHandle">上一张</span>
              <span class="rounded-6px px-8px py-4px text-18px bg-dark-500/50 bg-opacity-80 hover:(bg-opacity-100)"
                    style="transition: var(--theme-transition-bg)" v-show="picture.current+1 !== picture.maxCount"
                    @click="nextHandle">下一张</span>
            </div>
            <div class="flex gap-10px text-24px">
              <span title="向左旋转" class="rounded-full p-3px hover:(bg-dark-500/80)"
                    style="transition: var(--theme-transition-bg)" @click="setImgRotate(true)"><Undo/></span>
              <span title="向右旋转" class="rounded-full p-3px hover:(bg-dark-500/80)"
                    style="transition: var(--theme-transition-bg)" @click="setImgRotate(false)"><Redo/></span>
              <span title="恢复" class="rounded-full p-3px hover:(bg-dark-500/80)"
                    style="transition: var(--theme-transition-bg)" @click="initImage"><Erase/></span>
              <span title="缩小" class="rounded-full p-3px hover:(bg-dark-500/80)"
                    style="transition: var(--theme-transition-bg)" @click="setImgZoom(true)"><ZoomOut/></span>
              <span title="放大" class="rounded-full p-3px hover:(bg-dark-500/80)"
                    style="transition: var(--theme-transition-bg)" @click="setImgZoom(false)"><ZoomIn/></span>
              <span title="关闭" @click="closePreview" class="rounded-full p-3px hover:(bg-dark-500/80)"
                    style="transition: var(--theme-transition-bg)"><CloseOne/></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>