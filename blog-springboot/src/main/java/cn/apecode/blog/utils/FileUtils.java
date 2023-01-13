package cn.apecode.blog.utils;

import cn.apecode.blog.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * @description: 文件工具类
 * @author: apecode
 * @date: 2022-06-23 16:19
 **/
public class FileUtils {

    /**
     * @description: 获取文件md5
     * @param inputStream
     * @return {@link String}
     * @auther apecode
     * @date 2022/6/23 16:19
    */
    public static String getMd5(InputStream inputStream) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            return new String(Hex.encode(md5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("文件上传失败");
        } finally {
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @description: 获取文件后缀名
     * @param fileName
     * @return {@link String}
     * @auther apecode
     * @date 2022/6/23 16:19
    */
    public static String getExtName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

}
