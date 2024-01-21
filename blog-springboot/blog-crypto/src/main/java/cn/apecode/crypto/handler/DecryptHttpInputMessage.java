package cn.apecode.crypto.handler;

import cn.apecode.common.exception.BizException;
import cn.apecode.crypto.config.CryptoConfig;
import cn.apecode.crypto.utils.AESUtil;
import cn.apecode.crypto.utils.RSAUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author apecode.
 * @description: 解密处理
 * @date 2024/1/19 11:21
 */
@Slf4j
public class DecryptHttpInputMessage implements HttpInputMessage {

    private HttpHeaders headers;
    private InputStream body;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage, CryptoConfig cryptoConfig) {
        this.headers = inputMessage.getHeaders();
        try {
            // 得到aes加密的内容
            byte[] content =  new byte[inputMessage.getBody().available()];
            inputMessage.getBody().read(content);
            RequestDataExtend requestDataExtend = JSON.parseObject(content, RequestDataExtend.class);
            // 通过rsa解码得到aes密钥
            String decryptAesKey = RSAUtil.decryptByPrivateKey(requestDataExtend.getSym(), cryptoConfig.getRsaPrivateKey());
            AESKey aesKey = JSON.parseObject(decryptAesKey, AESKey.class);
            // 通过aes密钥解码获取原始请求内容
            String decryptContent = AESUtil.decrypt(aesKey.getKey(), requestDataExtend.getData(), aesKey.getKeyIV());
            if (cryptoConfig.isShowLog()) {
                log.info("加密内容：{}，解密后：{}", requestDataExtend.getData(), decryptContent);
            }
            if (StringUtils.isNotEmpty(decryptContent)) {
                // 重新写入controller
                this.body = new ByteArrayInputStream(decryptContent.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            log.error("解码异常：{}", e.getMessage());
            throw new BizException("解码异常：" + e.getMessage());
        }
    }

    @Override
    public InputStream getBody() throws IOException {
        return this.body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.headers;
    }
}
