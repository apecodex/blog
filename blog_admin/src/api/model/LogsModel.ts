declare type OperationLogBack  = {
    id: string,
    optModule: string,
    optType: string,
    optUrl: string,
    optMethod: string,
    optDesc: string,
    requestParam: string,
    requestMethod: string,
    responseData: string,
    user: SimpleUserInfoModel,
    idAddress: string,
    ipSource: string,
    createTime: string
}

declare type LoginLogBack  = {
    id: string,
    user: SimpleUserInfoModel,
    loginType: number,
    ipAddress: string,
    ipSource: string,
    rectangle: null | string,
    browser: string,
    os: string,
    createTime: string
}

declare type MailLogBack  = {
    msgId: string,
    topicId: string | null,
    email: string,
    type: string,
    status: number,
    routeKey: string,
    exchange: string,
    createTime: string
}