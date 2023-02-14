package cn.apecode.blog.utils;

import cn.apecode.blog.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @description: 内容加密
 * @author: apecode
 * @date: 2022-06-29 00:28
 **/
public class SecurityUtils {

    // 原始密匙,自己设置，必须为8位，使得你加密的密码是通过这8位加密的
    private static final String KEY = "apecode-blog-des";
    private final static String DES = "DES";

    /**
     * 密码解密
     * @return
     * @throws Exception
     */
    public static Integer decrypt(String src) {
        try {
            String decrypt = new String(decrypt(hex2byte(src.getBytes()), KEY.getBytes(StandardCharsets.UTF_8)));
            if (StringUtils.isBlank(decrypt)) return null;
            return Integer.valueOf(decrypt);
        } catch (Exception ignored) {
        }
        return null;
    }
    /**
     * 密码解密
     * @return
     * @throws Exception
     */
    public static String decryptLocation(String src) {
        try {
            String decrypt = new String(decrypt(hex2byte(src.getBytes()), KEY.getBytes(StandardCharsets.UTF_8)));
            if (StringUtils.isBlank(decrypt)) return null;
            return decrypt;
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 密码加密
     * @return
     * @throws Exception
     */
    public static String encrypt(String src) {
        try {
            return byte2hex(encrypt(src.getBytes(), KEY.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 加密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     * @throws Exception
     */
    private static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 现在，获取数据并加密正式执行加密操作
        return cipher.doFinal(src);
    }

    /**
     * 解密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     * @throws Exception
     */
    private static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 现在，获取数据并解密正式执行解密操作
        return cipher.doFinal(src);
    }

    /**
     * 字符串转二进制
     * @param b
     * @return
     */
    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new BizException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 二行制转字符串
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (byte value : b) {
            stmp = (Integer.toHexString(value & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString();
    }
}
