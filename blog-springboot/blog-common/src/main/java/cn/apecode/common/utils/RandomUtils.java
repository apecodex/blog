package cn.apecode.common.utils;

import java.util.UUID;

/**
 * @description: 随机生成
 * @author: apecode
 * @date: 2022-06-09 23:33
 **/
public class RandomUtils {

    /**
     * @description: 获得一个UUID
     * @param length
     * @return {@link String}
     * @auther apecode
     * @date 2022/6/9 23:34
    */
    public static String getUUID(Integer length){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "").substring(0, length);
    }

    /**
     * @description: UUID生成纯数字ID
     * @param
     * @return {@link Integer}
     * @auther apecode
     * @date 2022/6/9 23:35
    */
    public static Integer getUUIDInOrderId(){
        Integer orderId=UUID.randomUUID().toString().hashCode();
        orderId = orderId < 0 ? -orderId : orderId; //String.hashCode() 值会为空
        return orderId;
    }

    /**
     * @description: 获得用户uid
     * @return {@link String}
     * @auther apecode
     * @date 2022/6/9 23:35
    */
    public static String getUid() {
        return getUUIDInOrderId().toString().substring(0, 4) + String.valueOf(System.currentTimeMillis()).substring(9);
    }
}
