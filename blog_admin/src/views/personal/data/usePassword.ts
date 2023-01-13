import {sendEmail} from '~/api/requests/UserAuth'
import {userInfoData} from './usePersonal'
import {CountdownProps, FormItemRule, FormRules} from "naive-ui";
import {ref} from "vue";

interface ModelType {
    oldPassword: string | null
    newPassword: string | null
    reenteredPassword: string | null
    code: string | null
}

const startCount = ref(false)

const model = ref<ModelType>({
    oldPassword: null,
    newPassword: null,
    reenteredPassword: null,
    code: null
})

const validatePasswordStartWith = (rule: FormItemRule, value: string): boolean => {
    return (
        !!model.value.newPassword &&
        model.value.newPassword.startsWith(value) &&
        model.value.newPassword.length >= value.length
    )
}

const validatePasswordSame = (rule: FormItemRule, value: string): boolean => {
    return value === model.value.newPassword
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

export {
    model,
    rules,
    startCount,
    renderCountdown,
    sendEmailCodeHandle
}