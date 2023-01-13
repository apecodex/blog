package cn.apecode.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 上传模式枚举
 * @author: apecode
 * @date: 2022-06-23 16:35
 **/
@Getter
@AllArgsConstructor
public enum UploadModeEnum {

    OSS("oss", "ossUploadFileStrategyImpl"),
    LOCAL("local", "localUploadFileStrategyImpl");
    private final String mode;
    private final String strategy;

    /**
     * @description: 获取策略
     * @param mode
     * @return {@link String}
     * @auther apecode
     * @date 2022/6/23 16:36
    */
    public static String getStrategy(String mode) {
        return valueOf(mode.toUpperCase()).getStrategy();
    }
}
