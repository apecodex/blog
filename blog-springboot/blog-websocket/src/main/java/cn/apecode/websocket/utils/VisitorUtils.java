package cn.apecode.websocket.utils;

import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.IpUtils;
import cn.apecode.websocket.pojo.VisitorUser;

import javax.servlet.http.HttpServletRequest;

import static cn.apecode.common.constant.CommonConst.DEFAULT_AVATAR;
import static cn.apecode.common.constant.CommonConst.DEFAULT_TOURIST;

/**
 * @description: 获取游客信息
 * @author: apecode
 * @date: 2023-07-19 18:02
 **/
public class VisitorUtils {

    public static VisitorUser getVisitorUserInfo(HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        String uid = CommonUtils.getTouristMd5ByIpAddress(ipAddress);
        VisitorUser visitorUser = new VisitorUser();
        visitorUser.setUid(uid);
        visitorUser.setNickname(DEFAULT_TOURIST + uid);
        visitorUser.setAvatar(DEFAULT_AVATAR);
        visitorUser.setIpAddress(ipAddress);
        visitorUser.setIpSource(ipSource);
        return visitorUser;
    }
}
