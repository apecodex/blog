<script setup lang='ts'>
import {useRouter} from 'vue-router'
const router = useRouter()
</script>

<template>
  <div class="not-found-wrapper fixed w-full h-full top-0 left-0 right-0 bottom-0 bg-light-[#FFF] text-dark-900 z-99 overflow-hidden" @touchmove.prevent @mousewheel.prevent>
    <div class="not-found-inner absolute left-0 bottom-0 object-cover -z-1 w-full h-full flex flex-col justify-center items-center text-24px">
      <div class="flex flex-col justify-center items-center -mt-340px gap-15px">
        <h1 class="text-5em font-bold">4<span></span>4</h1>
        <p class="text-30px font-normal leading-tight mt-10px">NOT FOUND PAGE</p>
        <button @click="router.back()">返回</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.not-found-wrapper {
  animation: header-effect .3s ease-in;
}

.not-found-inner {
  background-image: url("https://apecode.oss-cn-chengdu.aliyuncs.com/config/642035e796d63a289017eea7764f117c.jpg");
  background-size: 55%;
  background-position: bottom;
  background-repeat: no-repeat;
}

h1 span:after {
  content: '🕛';
  animation: clock 5s linear infinite;
}

.not-found-inner button {
  @apply 'w-100px border-0 text-light-50 font-bold rounded-3px';
  padding-block: 8px;
  background: rgb(64, 192, 87);
}

.not-found-inner button:hover {
  background-image: linear-gradient(to right, rgb(250, 82, 82), rgb(250, 82, 82) 16.65%, rgb(190, 75, 219) 16.65%, rgb(190, 75, 219) 33.3%, rgb(76, 110, 245) 33.3%, rgb(76, 110, 245) 49.95%, rgb(64, 192, 87) 49.95%, rgb(64, 192, 87) 66.6%, rgb(250, 176, 5) 66.6%, rgb(250, 176, 5) 83.25%, rgb(253, 126, 20) 83.25%, rgb(253, 126, 20) 100%, rgb(250, 82, 82) 100%);
  animation: 2s linear dance6123 infinite;
  transform: scale(1.1) translateY(-1px);
}

@keyframes dance6123 {
  to {
    background-position: 100px;
  }
}


@media screen and (max-width: 960px) {
  .not-found-inner {
    background-size: 70%;
  }
}
@media screen and (max-width: 760px) {
  .not-found-inner {
    background-size: 100%;
  }
}

@keyframes clock {
  8.333% {
    content: '🕐';
  }
  16.666% {
    content: '🕑';
  }
  24.999% {
    content: '🕒';
  }
  33.333% {
    content: '🕓';
  }
  41.666% {
    content: '🕔';
  }
  49.999% {
    content: '🕕';
  }
  58.333% {
    content: '🕖';
  }
  66.666% {
    content: '🕗';
  }
  74.999% {
    content: '🕘';
  }
  83.333% {
    content: '🕙';
  }
  91.666% {
    content: '🕚';
  }
}
</style>