import type { Ref } from 'vue';
import { Login } from '~/api/requests/UserAuth';
import { getUserMenus } from '~/api/requests/Menu'
import monsterImg from './monsterImg';
import { StatusCode } from '~/api/enum/statusCode';
import { router } from '~/router';
import { useUserInfoStore, useMenuStore } from '~/store'
import {onMounted, reactive, ref} from "vue";

const userInfoStore = useUserInfoStore();
const menuStore = useMenuStore();

export const useUserLogin = () => {
  const usernameInputRef: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 用户名输入框
  const passwordInputRef: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 密码输入框
  const loginDivRef: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 登录div盒子
  const verify: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 滑动验证码

  // 登录表单
  let userLoginInfo: UserLoginParams = reactive({
    username: '',
    password: '',
    captchaVerification: ''
  })

  // 当前图片
  let monsterCurrentImg = ref(monsterImg.idle1);
  // 当焦点在输入框中时，停止更换图片
  let seguirPunteroMouse = true;
  // 宽度的一半
  const anchoMitad = window.innerWidth / 2;
  // 高度的三分之一
  const altoMitad = window.innerHeight / 3;

  onMounted(() => {
    // 将界面分为4块，分别为：左上角，左下角，右上角，右下角
    // 鼠标移动的当前角度时，更换怪兽
    (loginDivRef.value as any).addEventListener('mousemove', (m: MouseEvent) => {
      if (seguirPunteroMouse) {
        if (m.clientX < anchoMitad && m.clientY < altoMitad) {
          monsterCurrentImg.value = monsterImg.idle2;
        } else if (m.clientX < anchoMitad && m.clientY > altoMitad) {
          monsterCurrentImg.value = monsterImg.idle3;
        } else if (m.clientX > anchoMitad && m.clientY < altoMitad) {
          monsterCurrentImg.value = monsterImg.idle5;
        } else {
          monsterCurrentImg.value = monsterImg.idle4;
        }
      }
    })
    // @ts-ignore
    // 用户名输入框
    usernameInputRef.value.addEventListener('focus', () => {
      seguirPunteroMouse = false;
    })
    // @ts-ignore
    usernameInputRef.value.addEventListener('blur', () => {
      seguirPunteroMouse = true;
    })
    // @ts-ignore
    // 根据输入的内容长度更换怪兽眼睛
    usernameInputRef.value.addEventListener('keydown', () => {
      let usernameLenght = userLoginInfo.username.length;
      if (usernameLenght >= 0 && usernameLenght <= 14) {
        monsterCurrentImg.value = monsterImg.read1;
      } else if (usernameLenght >= 15 && usernameLenght <= 20) {
        monsterCurrentImg.value = monsterImg.read2;
      } else if (usernameLenght >= 21 && usernameLenght <= 26) {
        monsterCurrentImg.value = monsterImg.read3;
      } else {
        monsterCurrentImg.value = monsterImg.read4;
      }
    })
    // @ts-ignore
    // 输入密码时怪兽遮挡眼睛
    passwordInputRef.value.addEventListener('focus', () => {
      seguirPunteroMouse = false;
      let current = 0;
      const cubrirOjo = setInterval(() => {
        monsterCurrentImg.value = monsterImg.cover[current]
        if (current < 7) {
          current++;
        } else {
          clearInterval(cubrirOjo);
        }
      }, 60)
    });
    (passwordInputRef.value as any).addEventListener('blur', () => {
      let current = 7;
      const descubrirOjo = setInterval(() => {
        monsterCurrentImg.value = monsterImg.cover[current]
        if (current > 0) {
          current--;
        } else {
          clearInterval(descubrirOjo);
          seguirPunteroMouse = true;
        }
      }, 60)
    })
  });

  let handleSuccess = async (res: any) => {
    userLoginInfo.captchaVerification = res.captchaVerification
    const form = new FormData()
    form.append('username', userLoginInfo.username)
    form.append('password', userLoginInfo.password)
    form.append('captchaVerification', userLoginInfo.captchaVerification)
    await Login(form).then((resp: ResultObject<UserInfoModel>) => {
      // 登录成功
      let tipMessage = (window as any).$message.create("正在登录，请稍后...", {
        type: 'loading'
      });
      if (resp && resp.status && resp.code === StatusCode.SUCCESS) {
        userInfoStore.saveUserInfo(resp.data);
        getUserMenus().then((response: ResultObject<Array<UserMenuBackModel>>) => {
          if (response.status && response.data.length !== 0) {
            menuStore.saveMenu(response.data);
            tipMessage.type = 'success'
            tipMessage.content = "登录成功"
            // 跳转至首页
            router.replace({ path: "/" });
          } else {
            router.replace({ path: "/login" });
          }
        });
        if (menuStore.menus !== null) {
          // 刷新一次，以便其他组件拿到Pinia的数据
          window.location.reload();
        }
      } else {
        tipMessage.type = 'error'
        tipMessage.content = resp.message
      }
    })
  }

  let onShow = (type: string) => {
    if (userLoginInfo.username.length === 0) {
      (<any>window).$message.error("请输入用户名");
    } else if (userLoginInfo.password.length === 0) {
      (<any>window).$message.error("请输入密码");
    } else {
      (<any>verify.value).show();
    }
  }
  return {
    loginDivRef,
    usernameInputRef,
    passwordInputRef,
    verify,
    monsterImg,
    monsterCurrentImg,
    userLoginInfo,
    onShow,
    handleSuccess
  }
}