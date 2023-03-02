<script setup lang='ts'>
import { useUserLogin } from "./data/useUserLogin"
import Verify from "~/components/verifition/Verify.vue"

const { monsterCurrentImg, userLoginInfo, onShow, handleSuccess, loginDivRef, usernameInputRef, passwordInputRef, verify } = useUserLogin()

</script>

<template>
  <div class="h-100vh bg-dark-900 flex justify-center items-center" ref="loginDivRef">
    <div class="login">
      <img :src="monsterCurrentImg" alt="">
      <Verify @success="handleSuccess" mode="pop" captchaType="blockPuzzle" ref="verify"/>
      <form class="form-main" @submit.prevent="onShow">
        <label for="username">用户名</label>
        <input type="text" placeholder="用户名" v-model="userLoginInfo.username" autocomplete="off" ref="usernameInputRef">
        <label for="password">密码</label>
        <input type="password" placeholder="******" v-model="userLoginInfo.password" ref="passwordInputRef">
        <button type="submit">登录</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login {
  @apply h-550px absolute
}

.login img {
  @apply m-auto
}

.form-main {
  transform: translate(-50%);
  @apply w-400px absolute bg-light-50 border-5 border-solid border-fuchsia-900 rounded-10px py-30px px-60px top-290px left-1/2 text-center <sm: w-350px <sm:top-270px <sm:px-50px
}

.form-main label,
input,
button {
  @apply block w-full my-5px mx-0
}

.form-main input {
  @apply h-40px border-2 border-solid border-fuchsia-900 rounded-10px py-0 px-8px outline-none
}

.form-main button {
  box-shadow: 1px 1px 2px #000;
  @apply mt-20px cursor-pointer border-none bg-fuchsia-900 h-40px text-yellow-50 hover: (bg-fuchsia-800 transition duration-200 ease-in-out)
}
</style>