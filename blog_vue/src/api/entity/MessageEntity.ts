declare type MessageTheme = "default" | "orange" | "tomato" | "FlamingoPink" | "watermelon" | "PrestigeBlue" | "UfoGreen" | "BrightGreek" | "wisteria"

declare type MessageEntity = {
    user: SimpleUserInfoEntity | null
    nickname: string
    avatar: string
    content: string
    theme: MessageTheme
    createTime: string
}