package cn.apecode.crypto.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Random;

/**
 * @description: AES工具类
 * @author: apecode
 * @date: 2023-011-2 2:19
 **/
@Slf4j
public class AESUtil {

    /**
     * 加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * Key长度
     */
    private static final int KEY_LENGTH = 16;


    /**
     * 算法名称/加密模式/数据填充方式
     */
    private static final String ALGORITHMS = "AES/CBC/PKCS7Padding";

    private static final Base64.Encoder Base64Encoder = Base64.getEncoder();

    private static final Base64.Decoder Base64Decoder = Base64.getDecoder();

    /**
     *通过在运行环境中设置以下属性开启AES-256支持
     */
    static {
        Security.setProperty("crypto.policy", "unlimited");
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * @description: 产生16位强随时数
     * @return {@link String}
     * @auther apecode
     * @date 2/11/2023 PM2:37
    */
    public static String getKey() {
        StringBuilder uid = new StringBuilder(KEY_LENGTH);
        Random rd = new SecureRandom();
        for (int i = 0; i < KEY_LENGTH; i++) {
            //产生0-2的3位随机数
            switch (rd.nextInt(3)) {
                case 0:
                    // 0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    uid.append((char) (rd.nextInt(26) + 65));
                    break;
                case 2:
                    uid.append((char) (rd.nextInt(26) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    /**
     * @description: 对明文进行AES加密
     * @param key 密钥
     * @param content 明文
     * @param keyIV 偏移量
     * @return {@link String}
     * @auther apecode
     * @date 2/11/2023 PM2:49
    */
    public static String encrypt(String key, String content, String keyIV) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHMS);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(keyIV.getBytes()));
            byte[] byteEncode = content.getBytes(StandardCharsets.UTF_8);
            byte[] byteAES = cipher.doFinal(byteEncode);
            return Base64Encoder.encodeToString(byteAES);
        } catch (Exception e) {
            log.error("加密字符串 [ {} ] 时遇到异常：{}", content, e.getMessage());
        }
        return null;
    }

    /**
     * @description: 对密文进行解密
     * @param key 密钥
     * @param content 内容
     * @param keyIV 偏移量
     * @return {@link String}
     * @auther apecode
     * @date 2/11/2023 PM2:55
    */
    public static String decrypt(String key, String content, String keyIV) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHMS);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(keyIV.getBytes()));
            byte[] byteContent = Base64Decoder.decode(content);
            byte[] byteDecode = cipher.doFinal(byteContent);
            return new String(byteDecode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("解密字符串 [ {} ] 时遇到异常：{}", content, e.getMessage());
        }
        return null;
    }
}
