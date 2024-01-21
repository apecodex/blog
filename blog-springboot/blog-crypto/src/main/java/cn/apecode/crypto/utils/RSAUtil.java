package cn.apecode.crypto.utils;

import cn.apecode.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @description: RSA工具类
 * @author: apecode
 * @date: 2023-011-2 2:58
 **/
@Slf4j
public class RSAUtil {
    private static final String KEY_ALGORITHM = "RSA";
    private static final int INITIALIZE_LENGTH = 2048;
    private static final Base64.Encoder Base64Encoder = Base64.getEncoder();
    private static final Base64.Decoder Base64Decoder = Base64.getDecoder();

    /**
     * @param
     * @return {@link KeyPair}
     * @description: 生成公钥私钥
     * @auther apecode
     * @date 2/11/2023 PM3:08
     */
    public static KeyPair getKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(INITIALIZE_LENGTH);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("生成密钥对时遇到异常：{}", e.getMessage());
            throw new BizException("生成密钥对出现异常");
        }
    }

    /**
     * @param keyPair
     * @return {@link byte}
     * @description: 获取公钥
     * @auther apecode
     * @date 2/11/2023 PM3:11
     */
    public static byte[] getPublicKey(KeyPair keyPair) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return rsaPublicKey.getEncoded();
    }

    /**
     * @param keyPair
     * @return {@link byte}
     * @description: 获取私钥
     * @auther apecode
     * @date 2/11/2023 PM3:13
     */
    public static byte[] getPrivateKey(KeyPair keyPair) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return rsaPrivateKey.getEncoded();
    }

    /**
     * @param publicKey
     * @return {@link PublicKey}
     * @description: 公钥字符串转PublicKey实例
     * @auther apecode
     * @date 2/11/2023 PM3:26
     */
    public static PublicKey getPublicKey(String publicKey) {
        try {
            byte[] publicKeyBytes = Base64Decoder.decode(publicKey.getBytes());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("公钥字符串转PublicKey出现异常：{}", e.getMessage());
            throw new BizException("公钥字符串转私钥出现异常");
        }
    }

    /**
     * @param privateKey
     * @return {@link PrivateKey}
     * @description: 私钥字符串转PrivateKey实例
     * @auther apecode
     * @date 2/11/2023 PM3:31
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        try {
            byte[] privateKeyBytes = Base64Decoder.decode(privateKey.getBytes());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("私钥字符串转PrivateKey出现异常：{}", e.getMessage());
            throw new BizException("私钥字符串转公钥出现异常");
        }
    }

    /**
     * @description: 获取RSA公钥
     * @param publicKey
     * @return {@link RSAPublicKey}
     * @auther apecode.
     * @date 2024/1/19 0:44
    */
    public static RSAPublicKey getRSAPublicKeyByString(String publicKey){
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey)keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("String转PublicKey出错：{}", e.getMessage());
            throw new BizException("String转PublicKey出错" + e.getMessage());
        }
    }

    /**
     * @description: 获取RSA私钥
     * @param privateKey
     * @return {@link RSAPrivateKey}
     * @auther apecode.
     * @date 2024/1/19 0:45
    */
    public static RSAPrivateKey getRSAPrivateKeyByString(String privateKey){
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey)keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            log.error("String转PrivateKey出错：{}", e.getMessage());
            throw new BizException("String转PrivateKey出错" + e.getMessage());
        }
    }

    /**
     * @param keyPair
     * @return {@link String}
     * @description: 获取公钥字符串
     * @auther apecode
     * @date 2/11/2023 PM3:49
     */
    public static String getPublicKeyString(KeyPair keyPair) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return Base64Encoder.encodeToString(rsaPublicKey.getEncoded());
    }

    /**
     * @param keyPair
     * @return {@link String}
     * @description: 获取私钥字符串
     * @auther apecode
     * @date 2/11/2023 PM3:51
     */
    public static String getPrivateKeyString(KeyPair keyPair) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return Base64Encoder.encodeToString(rsaPrivateKey.getEncoded());
    }

    /**
     * @param data
     * @param publicKey
     * @return {@link String}
     * @description: 公钥加密
     * @auther apecode
     * @date 2/11/2023 PM4:13
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(StandardCharsets.UTF_8), publicKey.getModulus().bitLength());
            return Base64Encoder.encodeToString(bytes);
        } catch (Exception e) {
            log.error("RSA公钥加密 [ {} ] 时出现异常：{}", data, e.getMessage());
            throw new BizException("RSA加密出现异常");
        }
    }

    /**
     * @description: 私钥解密
     * @param data
     * @param privateKey
     * @return {@link String}
     * @auther apecode.
     * @date 2024/1/19 0:29
    */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data), privateKey.getModulus().bitLength()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA私钥解密字符串[ {} ]时遇到异常：{}", data, e.getMessage());
            throw new BizException("RSA解密出现异常");
        }
    }

    /**
     * @description: 私钥加密
     * @param content
     * @param privateKey
     * @return {@link String}
     * @auther apecode.
     * @date 2024/1/19 0:33
    */
    public static String encryptByPrivateKey(String content, RSAPrivateKey privateKey){
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] bytes = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE,content.getBytes(StandardCharsets.UTF_8), privateKey.getModulus().bitLength());
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(bytes));
        } catch (Exception e) {
            log.error("RSA私钥加密 [ {} ] 时出现异常：{}", content, e.getMessage());
            throw new BizException("RSA加密出现异常");
        }
    }

    /**
     * @description: 公钥解密
     * @param content
     * @param publicKey
     * @return {@link String}
     * @auther apecode.
     * @date 2024/1/19 0:39
    */
    public static String decryptByPublicKey(String content, RSAPublicKey publicKey){
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(content), publicKey.getModulus().bitLength()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA公钥解密字符串[" + content + "]时遇到异常" +e.getMessage());
            throw new BizException("RSA解密出现异常");
        }
    }

    /**
     * @param cipher
     * @param opMode
     * @param data
     * @param keySize
     * @return {@link byte}
     * @description: 逐行加解密
     * @auther apecode
     * @date 2/11/2023 PM4:11
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opMode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opMode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    //可以调用以下的doFinal（）方法完成加密或解密数据：
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            log.error("加密阀值为：[ {} ] 的数据时发生异常：{}", maxBlock, e.getMessage());
        }
        byte[] resultData = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            log.error("io流关闭失败，{}", e.getMessage());
        }
        return resultData;
    }
}
