package cn.apecode.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.apecode.common.enums.ZoneEnum.SHANGHAI;

/**
 * @description: 公共工具类
 * @author: apecode
 * @date: 2022-06-09 23:24
 **/
public class CommonUtils {

    /**
     * @description: 邮箱正则匹配
     * @param email
     * @return {@link boolean}
     * @auther apecode
     * @date 2022/6/9 23:46
    */
    public static boolean checkEmail(String email) {
        String rule = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(rule);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }

    /**
     * @description: 密码正则匹配
     * @param password
     * @return {@link boolean}
     * @auther apecode
     * @date 2022/6/9 23:46
    */
    public static boolean checkPassword(String password) {
        String rule = "(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(rule);
        //正则表达式的匹配器
        Matcher m = p.matcher(password);
        //进行正则匹配
        return m.matches();
    }

    /**
     * @description: 获取当前时间
     * @return {@link LocalDateTime}
     * @auther apecode
     * @date 2022/6/11 1:14
    */
    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now(ZoneId.of(SHANGHAI.getZone()));
    }

    /**
     * @description: 通过用户ip地址生成md5，取后10位
     * @param ipAddress
     * @return {@link String}
     * @auther apecode
     * @date 2022/6/24 17:22
    */
    public static String getTouristMd5ByIpAddress(String ipAddress) {
        String digest = DigestUtils.md5Hex(ipAddress);
        return digest.substring(digest.length() - 10);
    }
}
