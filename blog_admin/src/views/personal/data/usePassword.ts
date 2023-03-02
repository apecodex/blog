import {sendEmail, updateUserPassword} from '~/api/requests/UserAuth'
import {userInfoData, userInfoStore} from './usePersonal'
import {CountdownProps, FormInst, FormItemInst, FormItemRule, FormRules} from "naive-ui";
import {computed, Ref, ref} from "vue";
import {StatusCode} from "~/api/enum/statusCode";

type updatePasswordType = UpdatePassword & { reenteredPassword: string | null }

export const usePassword = () => {
    const startCount = ref(false);
    const formRef = ref<FormInst | null>(null)
    const rPasswordFormItemRef = ref<FormItemInst | null>(null)
    const verify: Ref<HTMLElement | null> = ref<HTMLElement | null>(null) // 滑动验证码

    const passwordData = ref<updatePasswordType>({
        captchaVerification: "",
        oldPassword: null,
        newPassword: null,
        reenteredPassword: null,
        code: null
    })
    const validatePasswordStartWith = (rule: FormItemRule, value: string): boolean => {
        return (
          !!passwordData.value.newPassword &&
          passwordData.value.newPassword.startsWith(value) &&
          passwordData.value.newPassword.length >= value.length
        )
    }

    const validatePasswordSame = (rule: FormItemRule, value: string): boolean => {
        return value === passwordData.value.newPassword
    }

// 匹配密码
    const validateNewPassword = (rule: FormItemRule, value: string) => {
        const re = /(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$/
        return re.test(value)
    }

    const rules: FormRules = {
        oldPassword: [
            {
                required: true,
                message: '请输入旧密码',
                trigger: ['input', 'blur']
            }
        ],
        newPassword: [
            {
                message: '请输入新密码',
                trigger: 'input'
            },
            {
                validator: validateNewPassword,
                required: true,
                message: '新密码不能少于6位，最多20位，且至少包含一个数字或字母',
                trigger: ['input', 'blur'],
            }
        ],
        reenteredPassword: [
            {
                required: true,
                message: '请再次输入密码',
                trigger: ['input', 'blur']
            },
            {
                validator: validatePasswordStartWith,
                message: '两次密码输入不一致',
                trigger: 'input'
            },
            {
                validator: validatePasswordSame,
                message: '两次密码输入不一致',
                trigger: ['blur', 'password-input']
            }
        ],
        code: {
            required: true,
            message: '请输入验证码',
            trigger: ['input', 'blur']
        }
    }
// 倒计时
    const renderCountdown: CountdownProps['render'] = ({hours, minutes, seconds}) => {
        return `${String(seconds).padStart(2, '0')}`
    }

// 发送验证码处理
    const sendEmailCodeHandle = async () => {
        startCount.value = true
        let tipMessage = (window as any).$message.create(`正在向'${userInfoData.value?.email}'发送验证码...`, {
            type: 'loading'
        });
        await sendEmail({email: userInfoData.value?.email as string}).then((resp: ResultObject<null>) => {
            if (resp.status) {
                tipMessage.type = 'success'
                tipMessage.content = resp.message
            }
        }).catch(() => {
            tipMessage.type = 'error'
            tipMessage.content = '发送失败'
            startCount.value = false
        })
    }

    const loginType = computed(() => {
        let typeText = ''
        switch (userInfoData.value?.loginType) {
            case 0:
                typeText = '邮箱'
                break
            case 1:
                typeText = 'QQ'
                break
        }
        return typeText
    })

    const handlePasswordInput = () => {
        if (passwordData.value.reenteredPassword) {
            rPasswordFormItemRef.value?.validate({trigger: 'password-input'})
        }
    }

// 保存密码
    const handleValidate = (res: any) => {
        passwordData.value.captchaVerification = res.captchaVerification;
        formRef.value?.validate(async (errors) => {
            if (!errors) {
                let tipMessage = (window as any).$message.create("保存中...", {
                    type: 'loading'
                });
                await updateUserPassword({
                    code: passwordData.value.code,
                    oldPassword: passwordData.value.oldPassword,
                    newPassword: passwordData.value.newPassword,
                    captchaVerification: passwordData.value.captchaVerification
                }).then((resp: ResultObject<null>) => {
                    if (resp.status) {
                        tipMessage.type = 'success'
                        tipMessage.content = resp.message
                        passwordData.value.code = ""
                        passwordData.value.oldPassword = ""
                        passwordData.value.newPassword = ""
                        passwordData.value.reenteredPassword = ""
                        startCount.value = false;
                        (<any>window).$message.info("请重新登录");
                        userInfoStore.logout();
                    }
                    if (resp.code === StatusCode.FAIL) {
                        tipMessage.type = 'warning'
                        tipMessage.content = resp.message
                    }
                }).catch(() => {
                    tipMessage.type = 'error'
                    tipMessage.content = '更新失败'
                })
            }
        })
    }

    const showCaptcha = () => {
        (<any>verify.value).show();
    }

    return {
        passwordData,
        verify,
        formRef,
        rules,
        startCount,
        loginType,
        renderCountdown,
        handlePasswordInput,
        sendEmailCodeHandle,
        showCaptcha,
        handleValidate
    }
}