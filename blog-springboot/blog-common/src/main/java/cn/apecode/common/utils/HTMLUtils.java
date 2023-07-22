package cn.apecode.common.utils;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;

/**
 * @description: HTML、敏感词工具类
 * @author: apecode
 * @date: 2022-07-07 18:31
 **/
public class HTMLUtils {

    private static final SensitiveWordBs WORD_BS = SensitiveWordBs.newInstance()
            .ignoreCase(true)
            .ignoreWidth(true)
            .ignoreNumStyle(true)
            .ignoreChineseStyle(true)
            .ignoreEnglishStyle(true)
            .ignoreRepeat(true)
            .enableNumCheck(false)
            .enableEmailCheck(false)
            .enableUrlCheck(false)
            .wordAllow(WordAllows.system())
            .init();


    /**
     * @description: 过滤敏感词
     * @param source
     * @return {@link String}
     * @auther apecode
     * @date 2022/7/7 18:35
    */
    public static String filter(String source) {
        // 敏感词过滤
        source = WORD_BS.replace(source);
        // 过滤所有标签
        source = source.replaceAll("<[^>]+>", "")
                .replaceAll("(onload(.*?)=)", "")
                .replaceAll("(onerror(.*?)=)", "");
        return deleteHMTLTag(source);
    }

    /**
     * @description: 删除标签
     * @param source
     * @return {@link String}
     * @auther apecode
     * @date 2022/7/7 18:35
    */
    public static String deleteHMTLTag(String source) {
        // 删除转义字符
        source = source.replaceAll("&.{2,6}?;", "");
        // 删除script标签
        source = source.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", "");
        // 删除style标签
        source = source.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", "");
        return source;
    }

}
