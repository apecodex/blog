<script setup lang='ts'>
import Modal from "@/components/modal/Modal.vue";
import {Theme, Platte, ListNumbers} from "@icon-park/vue-next"
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";

const settingStore = useSettingStore();
const {articleThemeFlag, previewTheme, codeTheme, showCodeRowNumber} = storeToRefs(settingStore);

const closeModal = () => {
  settingStore.closeModal();
}
</script>

<template>
  <Modal title="文章预览样式" v-model:show="articleThemeFlag" @closeModal="closeModal">
    <div class="flex flex-col gap-10px">
      <div class="flex flex-col gap-10px">
        <span class="flex gap-10px items-center text-$hover-color2">
          <Theme size="20"/>
          文章主题
        </span>
        <div class="radio-box grid gap-10px">
          <label class="radio-container">
            <span>default</span>
            <div>
              <input type="radio" value="default" name="previewTheme" v-model="previewTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>github</span>
            <div>
              <input type="radio" value="github" name="previewTheme" v-model="previewTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>vuepress</span>
            <div>
              <input type="radio" value="vuepress" name="previewTheme" v-model="previewTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>mk-cute</span>
            <div>
              <input type="radio" value="mk-cute" name="previewTheme" v-model="previewTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>smart-blue</span>
            <div>
              <input type="radio" value="smart-blue" name="previewTheme" v-model="previewTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>cyanosis</span>
            <div>
              <input type="radio" value="cyanosis" name="previewTheme" v-model="previewTheme">
              <span class="checkmark"></span>
            </div>
          </label>
        </div>
      </div>
      <hr class="hr-twill p-1px">
      <div class="flex flex-col gap-10px">
          <span class="flex gap-10px items-center text-$hover-color3">
            <Platte size="20"/>
             代码主题
          </span>
        <div class="radio-box grid gap-10px">
          <label class="radio-container">
            <span>atom</span>
            <div>
              <input type="radio" value="atom" name="codeTheme" v-model="codeTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>a11y</span>
            <div>
              <input type="radio" value="a11y" name="codeTheme" v-model="codeTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>github</span>
            <div>
              <input type="radio" value="github" name="codeTheme" v-model="codeTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>gradient</span>
            <div>
              <input type="radio" value="gradient" name="codeTheme" v-model="codeTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>kimbie</span>
            <div>
              <input type="radio" value="kimbie" name="codeTheme" v-model="codeTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>paraiso</span>
            <div>
              <input type="radio" value="paraiso" name="codeTheme" v-model="codeTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container">
            <span>qtcreator</span>
            <div>
              <input type="radio" value="qtcreator" name="codeTheme" v-model="codeTheme">
              <span class="checkmark"></span>
            </div>
          </label>
          <label class="radio-container gap-10px">
            <span>stackoverflow</span>
            <div>
              <input type="radio" value="stackoverflow" name="codeTheme" v-model="codeTheme">
              <span class="checkmark"></span>
            </div>
          </label>
        </div>
      </div>
      <hr class="hr-twill p-1px">
      <div class="flex flex-col gap-10px">
        <span class="flex gap-10px items-center text-$hover-color">
          <ListNumbers size="20"/>
           是否显示代码行号
        </span>
        <div class="flex justify-center">
          <label class="switch">
            <input type="checkbox" v-model="showCodeRowNumber">
            <span class="slider"></span>
          </label>
        </div>
      </div>
    </div>
  </Modal>
</template>

<style scoped>
.radio-box {
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr))
}

.radio-container input {
  @apply 'absolute opacity-0 cursor-pointer h-0 w-0 outline-none';
}

.radio-container {
  @apply 'flex relative cursor-pointer select-none justify-between';
}

.checkmark {
  @apply 'block relative top-0 left-0 h-25px w-25px rounded-full';
  box-shadow: var(--theme-shadow);
  transition-duration: 0.5s;
}

.radio-container input:checked ~ .checkmark {
  box-shadow: var(--theme-shadow-inset);
}

.checkmark:after {
  @apply 'absolute opacity-0';
  content: "";
}

.radio-container input:checked ~ .checkmark:after {
  @apply 'opacity-100';
}

.radio-container .checkmark:after {
  @apply 'left-0.50em top-0.25em text-20px w-0.30em h-0.65em border-1px border-solid border-$hover-color';
  border-width: 0 0.15em 0.15em 0;
  transform: rotate(45deg);
}

/* 切换 */
.switch {
  @apply 'relative inline-block w-55px h-20px';
}

.switch input {
  @apply 'opacity-0 w-0 h-0';
}

.slider {
  @apply 'absolute cursor-pointer inset-0 bg-$theme-bg-reverse rounded-6px';
  box-shadow: 0 0.2em #dfd9d9;
  transition: var(--theme-transition-bg);
}

.slider:before {
  @apply 'absolute h-1.5em w-1.4em rounded-0.3em left-0.3em bottom-0.7em bg-orange-400';
  content: "";
  transition: 0.4s;
  box-shadow: 0 0.4em #bcb4b4;
}

.slider:hover::before {
  @apply 'bottom-0.5em';
  box-shadow: 0 0.2em #bcb4b4;
}

input:checked + .slider:before {
  @apply 'bg-green-400';
  transform: translateX(2em);
}
</style>