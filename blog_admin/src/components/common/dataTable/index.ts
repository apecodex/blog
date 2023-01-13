// 表格尺寸设置
import {SelectOption} from "naive-ui";
import {reactive, ref} from "vue";

export const tableSizeOption = [
    {
        label: "宽松",
        value: "large"
    },
    {
        label: "舒适",
        value: "medium"
    },
    {
        label: "小巧",
        value: "small"
    },
    {
        label: "紧凑",
        value: "tiny"
    },
]

// 表格样式设置
export const tableSet = reactive({
    striped: true,
    singleLine: false,
    singleColumn: false
})
// 表格默认尺寸
export const tableSize = ref("medium")

export const tableSizeHandle = (value: string, option: SelectOption) => {
    tableSize.value = value
}